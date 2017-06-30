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
package com.grinnotech.patients.config;

import com.grinnotech.patients.dao.*;
import com.grinnotech.patients.model.*;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.UUID;

import static com.grinnotech.patients.model.Authority.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

@Component
class Startup {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final ContactRepository contactRepository;

    private final CountryDictionaryRepository addressDictionaryRepository;

    private final ZipCodePolandRepository zipCodePolandRepository;

    private String uuidRoot;
    private String uuidPpmdPoland;
    private String uuidTest;

    @Autowired
    public Startup(UserRepository userRepository,
                   OrganizationRepository organizationRepository, ContactRepository contactRepository, CountryDictionaryRepository addressDictionaryRepository, ZipCodePolandRepository zipCodePolandRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressDictionaryRepository = addressDictionaryRepository;
        this.zipCodePolandRepository = zipCodePolandRepository;
        init();
    }

    private void init() {
        initOrganizations();
        initUsers();
        initContactMethods();
        initAddressDictionary();
        initZipCodePoland();
    }

    private void initOrganizations() {
        Organization root = organizationRepository.findByCodeActive("ROOT");
        if (root == null) {
            root = new Organization();
            root.setId(uuidRoot = randomUUID().toString());
            root.setName("ROOT");
            root.setCode("ROOT");
            insert(root);
        }
        else {
            uuidRoot = root.getId();
        }

        Organization ppmdPoland = organizationRepository.findByCodeActive("PPMDPoland");
        if (ppmdPoland == null) {
            ppmdPoland = new Organization();
            ppmdPoland.setId(uuidPpmdPoland = randomUUID().toString());
            ppmdPoland.setName("Fundacja Parent Project Muscular Dystrophy");
            ppmdPoland.setCode("PPMDPoland");
            ppmdPoland.setParentId(root.getId());
            insert(ppmdPoland);
        }
        else {
            uuidPpmdPoland = ppmdPoland.getId();
        }

        Organization test = organizationRepository.findByCodeActive("test");
        if (test == null) {
            test = new Organization();
            test.setId(uuidTest = randomUUID().toString());
            test.setName("Test");
            test.setCode("test");
            test.setParentId(root.getId());
            insert(test);
        }
        else {
            uuidTest = test.getId();
        }
    }

    private void insert(Organization record) {
        record.setVersion(1);
        record.setActive(true);

        Organization organization = organizationRepository.insert(record);
        organization.setChainId(organization.getId());
        organizationRepository.save(organization);
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            // admin user
            User admin = new User();
            admin.setEmail("admin@starter.com");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setLocale("pl");
            admin.setOrganizationIds(singleton(uuidRoot));
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setEnabled(true);
            admin.setAuthorities(singleton(ADMIN.name()));
            admin.setActive(true);
            insert(admin);

            // normal user
            User user = new User();
            user.setEmail("user@starter.com");
            user.setFirstName("user");
            user.setLastName("user");
            user.setLocale("pl");
            user.setOrganizationIds(new HashSet<>(asList(uuidPpmdPoland, uuidTest)));
            user.setPasswordHash(passwordEncoder.encode("user"));
            user.setEnabled(true);
            user.setAuthorities(new HashSet<>(asList(USER.name(), EMPLOYEE.name())));
            user.setActive(true);
            insert(user);
        }
    }

    private void insert(User record) {
        record.setVersion(1);
        record.setActive(true);

        User user = userRepository.insert(record);
        user.setChainId(user.getId());
        userRepository.save(user);
    }

    private void initContactMethods() {
        if (contactRepository.count() == 0) {
            {
                ContactMethod method = new ContactMethod();
                method.setMethod("telefon domowy");
                method.setDescription("Telefon domowy");
                insert(method);
            }
            {
                ContactMethod method = new ContactMethod();
                method.setMethod("telefon komórkowy");
                method.setDescription("Telefon komórkowy");
                insert(method);
            }
            {
                ContactMethod method = new ContactMethod();
                method.setMethod("telefon służbowy");
                method.setDescription("Telefon służbowy");
                insert(method);
            }
            {
                ContactMethod method = new ContactMethod();
                method.setMethod("e-mail");
                method.setDescription("Poczta elektroniczna");
                insert(method);
            }
        }
    }

    private void insert(ContactMethod record) {
        record.setVersion(1);
        record.setActive(true);

        ContactMethod method = contactRepository.insert(record);
        method.setChainId(method.getId());
        contactRepository.save(method);
    }

    private void initAddressDictionary() {
        final String CSV = "countries.csv";

        if (addressDictionaryRepository.count() == 0) {
            try {
                CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(CSV).getInputStream()), ';', '"', 1);

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
                    final String code = line[0];
                    final String country_en = line[2];
                    final String country_pl = !"".equals(line[3]) ? line[3] : country_en;
                    final String country_de = !"".equals(line[4]) ? line[4] : country_en;

                    LOGGER.debug("initAddressDictionary: " + code + "," + country_en);
                    insert(new CountryDictionary(code, country_en, country_pl, country_de));
                }
            } catch (IOException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            }
        }
    }

    private void insert(CountryDictionary record) {
        record.setVersion(1);
        record.setActive(true);

        CountryDictionary country = addressDictionaryRepository.insert(record);
        country.setChainId(country.getId());
        addressDictionaryRepository.save(country);
    }

    private void initZipCodePoland() {
        final String CSV = "kody-pocztowe_GUS.csv";

        if (zipCodePolandRepository.count() == 0) {
            try {
                CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(CSV).getInputStream()), ';', '"', 1);

                String[] line;
                final char[] delimiters = {' ', ',', '.', '-', '(', ')'};
                while ((line = reader.readNext()) != null) {
                    final String zipCode = line[0];
                    final String postOffice = line[1];
                    final String city = capitalizeFully(line[2], delimiters);
                    final String voivodship = capitalizeFully(line[3], delimiters);
                    final String street = line[4];
                    final String county = capitalizeFully(line[5], delimiters);

                    LOGGER.debug("initZipCodePoland: " + zipCode + ", " + postOffice + ", " + city + ", " + voivodship + ", " + street + ", " + county);

                    int count = zipCodePolandRepository.CountByExample(zipCode, postOffice, city, voivodship, street, county);
                    if (count == 0) {
                        insert(new ZipCodePoland(zipCode, postOffice, city, voivodship, street, county));
                    } else {
                        LOGGER.debug("****************** duplicate found, don't insert!");
                    }
                }
            } catch (IOException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            }
        }
    }

    private void insert(ZipCodePoland record) {
        record.setVersion(1);
        record.setActive(true);

        ZipCodePoland zipCode = zipCodePolandRepository.insert(record);
        zipCode.setChainId(zipCode.getId());
        zipCodePolandRepository.save(zipCode);
    }

}
