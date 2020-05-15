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
package com.grinnotech.patientsorig.util.startup;

import com.grinnotech.patients.model.info.OrphadataInfo;
import com.grinnotech.patients.model.orphadata.Disorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.lang.Integer.parseInt;

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
        logger.trace("******** OrphadataParser start ***************");
        info = new OrphadataInfo();
    }

    public void on__end_object() {
        logger.trace("******** OrphadataParser end ***************");
    }

    private int countDisorder = 0;

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

    private Disorder.DisorderBuilder disorderBuilder = null;

    private Disorder disorder = null;

    public Disorder getDisorder() {
        return disorder;
    }

    private String externalReference;

    public void on_JDBOR_DisorderList_Disorder_start_object() {
        disorderBuilder = Disorder.builder();
        disorder = null;
        externalReference = null;
    }

    public void on_JDBOR_DisorderList_Disorder_end_object() {
        assert disorderBuilder != null;
        disorder = disorderBuilder.build();
        countDisorder++;
        logger.trace("******** Disorder end {} ***************", countDisorder);
        logger.debug("{}", disorder);
    }

    public void on_JDBOR_DisorderList_Disorder_DisorderType_Name_label_value_string(String type) {
        assert disorderBuilder != null;
        disorderBuilder.type(type);
    }

    public void on_JDBOR_DisorderList_Disorder_DisorderType_OrphaNumber_value_string(String orphaNumber) {
        assert disorderBuilder != null;
        disorderBuilder.typeOrphaNumber(parseInt(orphaNumber));
    }

    public void on_JDBOR_DisorderList_Disorder_ExpertLink_link_value_string(String expertLink) {
        assert disorderBuilder != null;
        disorderBuilder.expertLink(expertLink);
    }

    public void on_JDBOR_DisorderList_Disorder_Name_label_value_string(String name) {
        assert disorderBuilder != null;
        disorderBuilder.name(name);
    }

    public void on_JDBOR_DisorderList_Disorder_OrphaNumber_value_string(String orphaNumber) {
        assert disorderBuilder != null;
        disorderBuilder.orphaNumber(parseInt(orphaNumber));
    }

    public void on_JDBOR_DisorderList_Disorder_ExternalReferenceList_ExternalReference_Reference_value_string(String reference) {
        this.externalReference = reference;
    }

    public void on_JDBOR_DisorderList_Disorder_ExternalReferenceList_ExternalReference_Source_value_string(String source) {
        assert disorderBuilder != null;
        switch (source) {
            case "ICD-10":
                disorderBuilder.icd10(externalReference);
                externalReference = null;
                break;
        }
    }

    public void on_JDBOR_DisorderList_Disorder_SynonymList_Synonym_label_value_string(String name) {
        assert disorderBuilder != null;
        disorderBuilder.synonym(name);
    }
}
