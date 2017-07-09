/*
 * Copyright (C) 2017 Jacek Sztajnke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.grinnotech.patients.util.startup;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.grinnotech.patients.model.info.OrphadataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;
import java.util.Stack;

/**
 *
 * @author Jacek Sztajnke
 */
public class JsonEventParser {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private URL url;

    protected JsonParser jsonParser = null;

    private Class<? extends JsonEventParser> clazz;

    protected JsonEventParser(URL url, Class<? extends JsonEventParser> clazz) {
        this.url = url;
        this.clazz = clazz;
    }

    public void on__start_object() {
        logger.info("******** JsonEventParser start ***************");
    }

    public void on__end_object() {
        logger.info("******** JsonEventParser end ***************");
    }

    public void parse(Integer repeat) {
        Stack<String> tokenPath = new Stack<>();

        OrphadataInfo info = new OrphadataInfo();

        JsonFactory jsonfactory = new JsonFactory();
        try (JsonParser parser = jsonfactory.createParser(url)) {
            this.jsonParser = parser;

            JsonToken token = parser.nextToken();
            for (int i = 0;
                 token != null && (repeat == null || i <= repeat);
                 i++, token = parser.nextToken()) {

                String name = parser.getCurrentName();
                String path = tokenPath.stream().reduce((a, b) -> a + "_" + b).orElse("");

                if (repeat != null)
                    logger.debug("{} : {} : {} name: {}", i, path, token, name);
                else
                    logger.trace("{} : {} : {} name: {}", i, path, token, name);

                switch (token) {
                    case FIELD_NAME:
                        tokenPath.push(name);
                        break;
                    case END_ARRAY:
                    case END_OBJECT:
                    case VALUE_STRING:
                        if (!tokenPath.empty() && tokenPath.peek().equals(name)) {
                            tokenPath.pop();
                        }
                        break;
                }

                String value = null;
                try {
                    value = parser.getValueAsString();
                } catch (IOException ignored) {}

                String methodName = "on_" + path + "_" + token.name().toLowerCase();
                try {
                    if (value != null) {
                        Method method = clazz.getMethod(methodName, String.class);
                        method.invoke(this, value);
                    }
                    else {
                        Method method = clazz.getMethod(methodName);
                        method.invoke(this);
                    }
                } catch (NoSuchMethodException ignored) {
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    logger.error("{} invoking error", methodName, ex);
                }
            }

        } catch (IOException ex) {
            logger.error("Can't create parser for {}", url, ex);
        }

        this.jsonParser = null;
    }
}
