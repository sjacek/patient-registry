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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

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
//        DateFormat dateFormat = new SimpleDateFormat("yy-M-d HH:mm:ss", new Locale("pl"));
//        Date date = parser.getInfo().getDate();
//        assertEquals(dateFormat.parse("18-7-6 15:59:45"), date);
        assertEquals("1.2.7 / 4.1.6 [2017-03-09] (orientdb version)", parser.getInfo().getVersion());
        assertEquals("Orphanet (c) 2018", parser.getInfo().getCopyright());
    }

}