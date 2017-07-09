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

import com.grinnotech.patients.model.info.OrphadataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Jacek Sztajnke
 */
public class OrphadataParser extends JsonEventParser {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//    private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pl"));
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pl"));
    protected OrphadataInfo info;

    public OrphadataParser(URL url) {
        super(url, OrphadataParser.class);
    }

    public OrphadataParser(URL url, Class<? extends OrphadataParser> clazz) {
        super(url, clazz);
    }

    public static void parse(URL url) {
        OrphadataParser parser = new OrphadataParser(url);
        parser.parse((Integer) null);
    }

    public void on__start_object() {
        logger.info("******** OrphadataParser start ***************");
        info = new OrphadataInfo();
    }

    public void on__end_object() {
        logger.info("******** OrphadataParser end ***************");
    }

    public void on_JDBOR_date_value_string(String date) {
        try {
            info.setDate(dateFormat.parse(date));
        } catch (ParseException ex) {
            logger.error("Can't parse date {}; {}.", date, dateFormat.getNumberFormat(), ex);
        }
    }

    public void on_JDBOR_version_value_string(String version) {
        info.setVersion(version);
    }

    public void on_JDBOR_copyright_value_string(String copyright) {
        info.setCopyright(copyright);
    }

    public OrphadataInfo getInfo() {
        return info;
    }
}
