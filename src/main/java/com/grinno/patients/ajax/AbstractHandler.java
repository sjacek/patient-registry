/*
 * Copyright (C) 2016 jacek
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
package com.grinno.patients.ajax;

import com.grinno.patients.domain.JsonItem;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jacek
 */
public class AbstractHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @param results
     * @return
     */
    public static String getJsonSuccessData(List<? extends JsonItem> results) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("success", true);
        
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        results.stream().forEach((ji) -> {
            arrayBuilder.add(ji.toJson());
        });
        
        builder.add("data", arrayBuilder);
        
        return toJsonString(builder.build());
    }

    /**
     *
     * @param jsonItem
     * @return
     */
    public static String getJsonSuccessData(JsonItem jsonItem) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("success", true);
        builder.add("data", jsonItem.toJson());
        
        return toJsonString(builder.build());
    }
    
    /**
     *
     * @param jsonItem
     * @param totalCount
     * @return
     */
    public static String getJsonSuccessData(JsonItem jsonItem, int totalCount) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("success", true);
        builder.add("total", totalCount);
        builder.add("data", jsonItem.toJson());
        
        return toJsonString(builder.build());
    }

    /**
     *
     * @param theErrorMessage
     * @return
     */
    public static String getJsonErrorMsg(String theErrorMessage) {
        return getJsonMsg(theErrorMessage, false);
    }    
    
    /**
     *
     * @param msg
     * @return
     */
    public static String getJsonSuccessMsg(String msg) {
        return getJsonMsg(msg, true);
    }
    
    /**
     *
     * @param msg
     * @param success
     * @return
     */
    public static String getJsonMsg(String msg, boolean success) {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("success", success);
        builder.add("msg", msg);
        return toJsonString(builder.build());
    }

    /**
     *
     * @param model
     * @return
     */
    public static String toJsonString(JsonObject model) {
        final StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            jsonWriter.writeObject(model);
        }
        
        return stWriter.toString();
    }
    
    /**
     *
     * @param jsonString
     * @return
     */
    protected JsonObject parseJsonObject(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        return reader.readObject();
    }
    
    /**
     *
     * @param jsonValue
     * @return
     */
    protected Integer getIntegerValue(JsonValue jsonValue) {
        Integer value = null;
        
        switch (jsonValue.getValueType()) {
            case NUMBER:
                JsonNumber num = (JsonNumber)jsonValue;
                value = num.intValue();
                break;
            case NULL:
                break;
        }
        return value;
    }
}
