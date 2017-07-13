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
package com.grinnotech.patients.config.profiles.development;

import com.grinnotech.patients.config.OrphadataProperties;
import com.grinnotech.patients.dao.*;
import com.grinnotech.patients.dao.orphadata.DisorderRepository;
import com.grinnotech.patients.domain.AbstractPersistable;
import com.grinnotech.patients.model.*;
import com.grinnotech.patients.util.startup.OrphadataParser;
import com.grinnotech.patients.util.startup.OrphadataParserMongo;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import static com.grinnotech.patients.model.Authority.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

/**
 * @author Jacek Sztajnke
 */
@Component
@Profile("development")
class Startup {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final ContactRepository contactRepository;

    private final CountryDictionaryRepository addressDictionaryRepository;

    private final ZipCodePolandRepository zipCodePolandRepository;

    private final DisorderRepository disorderRepository;

    private String uuidRoot;
    private String uuidPpmdPoland;
    private String uuidTest;
    private OrphadataProperties orphadataProperties;

    @Autowired
    public Startup(UserRepository userRepository,
                   OrganizationRepository organizationRepository,
                   ContactRepository contactRepository,
                   CountryDictionaryRepository addressDictionaryRepository,
                   ZipCodePolandRepository zipCodePolandRepository,
                   DisorderRepository disorderRepository,
                   PasswordEncoder passwordEncoder,
                   OrphadataProperties orphadataProperties) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressDictionaryRepository = addressDictionaryRepository;
        this.zipCodePolandRepository = zipCodePolandRepository;
        this.disorderRepository = disorderRepository;
        this.orphadataProperties = orphadataProperties;
        init();
    }

    private void init() {
        initOrganizations();
        initUsers();
        initContactMethods();
        initAddressDictionary();
        initZipCodePoland();
        initOrphaData();
    }

    private void initOrganizations() {
        Organization root = organizationRepository.findByCodeActive("ROOT");
        if (root == null)
            root = insert(Organization.builder().name("ROOT").code("ROOT").build(), organizationRepository);
        uuidRoot = root.getId();

        Organization ppmdPoland = organizationRepository.findByCodeActive("PPMDPoland");
        if (ppmdPoland == null)
            ppmdPoland = insert(Organization.builder()
                            .name("Fundacja Parent Project Muscular Dystrophy").code("PPMDPoland").parentId(uuidRoot).build(),
                    organizationRepository);
        uuidPpmdPoland = ppmdPoland.getId();

        Organization test = organizationRepository.findByCodeActive("test");
        if (test == null)
            test = insert(Organization.builder().name("Test").code("test").parentId(uuidRoot).build(),organizationRepository);
        uuidTest = test.getId();
    }

    private void initUsers() {
        if (userRepository.count() != 0)
            return;

        // admin user
        insert(User.builder()
                        .email("admin@starter.com")
                        .firstName("admin").lastName("admin")
                        .locale("pl")
                        .organizationIds(singleton(uuidRoot))
                        .passwordHash(passwordEncoder.encode("admin"))
                        .enabled(true)
                        .authorities(singleton(ADMIN.name())).build(),
                userRepository);

        // normal user
        insert(User.builder()
                        .email("user@starter.com")
                        .firstName("user").lastName("user")
                        .locale("pl")
                        .organizationIds(new HashSet<>(asList(uuidPpmdPoland, uuidTest)))
                        .passwordHash(passwordEncoder.encode("user"))
                        .enabled(true)
                        .authorities(new HashSet<>(asList(USER.name(), EMPLOYEE.name()))).build(),
                userRepository);
    }

    private void initContactMethods() {
        if (contactRepository.count() != 0)
            return;

        insert(ContactMethod.builder().method("telefon domowy").description("Telefon domowy").locale("pl_PL").build(), contactRepository);
        insert(ContactMethod.builder().method("telefon komórkowy").description("Telefon komórkowy").locale("pl_PL").build(), contactRepository);
        insert(ContactMethod.builder().method("telefon służbowy").description("Telefon służbowy").locale("pl_PL").build(), contactRepository);
        insert(ContactMethod.builder().method("e-mail").description("Poczta elektroniczna").locale("pl_PL").build(), contactRepository);
    }

    private void initAddressDictionary() {
        final String CSV = "countries.csv";

        if (addressDictionaryRepository.count() != 0)
            return;

        try (
                CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(CSV).getInputStream()), ';', '"', 1)
        ) {

//                ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
//                strat.setType(CountryDictionary.class);
//                strat.setColumnMapping(new String[] {"nazwa"});
//                new CsvToBean().parse(strat, reader).forEach(address -> insert((AbstractPersistable) address));
//                HeaderColumnNameMappingStrategy<AddressDictionary> strategy = new HeaderColumnNameMappingStrategy<>();
//                strategy.setType(CountryDictionary.class);
//
//                CsvToBean<AddressDictionary> csvToBean = new CsvToBean<>();
//
//                csvToBean.parse(strategy, reader).forEach(address -> insert(address));
            String[] line;
            while ((line = reader.readNext()) != null) {
                // line[] is an array of values from the line
                CountryDictionary cd = insert(CountryDictionary.builder()
                                .code(line[0])
                                .countryEn(line[2])
                                .countryPl(isNotEmpty(line[3]) ? line[3] : line[2])
                                .countryDe(isNotEmpty(line[4]) ? line[4] : line[2])
                                .build(),
                        addressDictionaryRepository);
                logger.debug("initAddressDictionary: {}, {}" + cd.getCode(), cd.getCountryEn());
            }
        } catch (IOException ex) {
            logger.error("File " + CSV + " not found", ex);
        }
    }

    private void initZipCodePoland() {
        final String CSV = "kody-pocztowe_GUS.csv";

        if (zipCodePolandRepository.count() != 0)
            return;

        try (
                InputStreamReader isReader = new InputStreamReader(new ClassPathResource(CSV).getInputStream());
                CSVReader reader = new CSVReader(isReader, ';', '"', 1)
        ) {
            String[] line;
            final char[] delimiters = {' ', ',', '.', '-', '(', ')'};

            while ((line = reader.readNext()) != null) {
                final String zipCode = line[0];
                final String postOffice = line[1];
                final String city = capitalizeFully(line[2], delimiters);
                final String voivodship = capitalizeFully(line[3], delimiters);
                final String street = line[4];
                final String county = capitalizeFully(line[5], delimiters);

                if (!zipCodePolandRepository.existsByExample(zipCode, postOffice, city, voivodship, street, county)) {
                    logger.debug("initZipCodePoland: {}, {}, {}, {}, {}, {}, ()",
                            zipCode, postOffice, city, voivodship, street, county);
                    insert(ZipCodePoland.builder()
                                    .zipCode(zipCode)
                                    .postOffice(postOffice)
                                    .city(city)
                                    .voivodship(voivodship)
                                    .street(street)
                                    .county(county).build(),
                            zipCodePolandRepository);

                } else {
                    logger.debug("****************** duplicate found, don't insert!");
                }
            }
        } catch (IOException ex) {
            logger.error("File " + CSV + " not found", ex);
        }
    }

    private void initOrphaData() {
        if (disorderRepository.count() != 0)
            return;

        URL urlPl;
        try {
            urlPl = new URL(orphadataProperties.getUrlPl());
        } catch (MalformedURLException ex) {
            logger.error("Bad orphaData JSON address: {}", orphadataProperties.getUrlPl(), ex);
            return;
        }

        OrphadataParser parser = new OrphadataParser(urlPl);
        parser.parse(20);
        logger.info("Orphadata version: {}", parser.getInfo().getVersion());

        OrphadataParserMongo.parse(urlPl, disorderRepository);
    }

    private <T extends AbstractPersistable, R extends MongoRepository<T, String>>
    T insert(T record, R repository) {
        record.setVersion(1);
        record.setActive(true);

        T newRecord = repository.insert(record);
        newRecord.setChainId(newRecord.getId());
        repository.save(newRecord);

        return newRecord;
    }
}
