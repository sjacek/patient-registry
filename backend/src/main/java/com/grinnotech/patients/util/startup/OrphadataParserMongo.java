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

import com.grinnotech.patients.dao.OrphadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;

/**
 *
 * @author Jacek Sztajnke
 */
public class OrphadataParserMongo extends OrphadataParser {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private OrphadataRepository orphadataRepository;

    public OrphadataParserMongo(URL url, OrphadataRepository orphadataRepository) {
        super(url, OrphadataParserMongo.class);
        this.orphadataRepository = orphadataRepository;
    }

    public void on__start_object() {
        logger.info("******** OrphadataParserMongo start ***************");
        super.on__start_object();
    }

    public void on__end_object() {
        logger.info("******** OrphadataParserMongo end ***************");
        super.on__end_object();
    }

}
