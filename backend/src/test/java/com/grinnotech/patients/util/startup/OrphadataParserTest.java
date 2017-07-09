/*
 * Copyright (C) 2016 Jacek Sztajnke
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

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 * @author Jacek Sztajnke
 */
public class OrphadataParserTest {
    @Test
    public void checkOrphadataInfo() throws ParseException, MalformedURLException {
        OrphadataParser parser = new OrphadataParser(new URL("http://www.orphadata.org/data/export/pl_product1.json"));
        parser.parse(20);

//        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pl_PL"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pl"));
        assertEquals(dateFormat.parse("2016-12-01 04:17:21"), parser.getInfo().getDate());
        assertEquals("1.2.4 / 4.1.6 [2016-04-08] (orientdb version)", parser.getInfo().getVersion());
        assertEquals("Orphanet (c) 2016", parser.getInfo().getCopyright());
    }

}