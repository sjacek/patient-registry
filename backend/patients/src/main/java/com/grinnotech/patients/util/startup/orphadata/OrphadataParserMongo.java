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
package com.grinnotech.patients.util.startup.orphadata;

//import com.grinnotech.patients.dao.orphadata.DisorderRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.invoke.MethodHandles;
//import java.net.URL;
//
///**
// *
// * @author Jacek Sztajnke
// */
//public class OrphadataParserMongo extends OrphadataParser {
//
//    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//
//    private DisorderRepository disorderRepository;
//
//    public OrphadataParserMongo(URL url, DisorderRepository disorderRepository) {
//        super(url, OrphadataParserMongo.class);
//        this.disorderRepository = disorderRepository;
//    }
//
//    public static void parse(URL url, DisorderRepository disorderRepository) {
//        OrphadataParserMongo parser = new OrphadataParserMongo(url, disorderRepository);
//        parser.parse((Integer) null);
//    }
//
//    public void on_JDBOR_DisorderList_Disorder_end_object() {
//        super.on_JDBOR_DisorderList_Disorder_end_object();
//        disorderRepository.save(getDisorder());
//    }
//
//}
