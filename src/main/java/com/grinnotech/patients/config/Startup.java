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

import com.grinnotech.patients.dao.ContactRepository;
import com.grinnotech.patients.dao.ZipCodePolandRepository;
import com.grinnotech.patients.model.CountryDictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import static com.grinnotech.patients.model.Authority.ADMIN;
import static com.grinnotech.patients.model.Authority.EMPLOYEE;
import static com.grinnotech.patients.model.Authority.USER;
import com.grinnotech.patients.model.ContactMethod;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.model.ZipCodePoland;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import com.grinnotech.patients.dao.CountryDictionaryRepository;
import com.grinnotech.patients.dao.OrganizationRepository;
import com.grinnotech.patients.model.Organization;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import static java.util.UUID.randomUUID;

@Component
class Startup {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MongoDb mongoDb;

    private final PasswordEncoder passwordEncoder;

    private final OrganizationRepository organizationRepository;

    private final ContactRepository contactRepository;

    private final CountryDictionaryRepository addressDictionaryRepository;

    private final ZipCodePolandRepository zipCodePolandRepository;

    @Autowired
    public Startup(MongoDb mongoDb,
            OrganizationRepository organizationRepository,
            ContactRepository contactRepository, CountryDictionaryRepository addressDictionaryRepository, ZipCodePolandRepository zipCodePolandRepository,
            PasswordEncoder passwordEncoder) {
        this.mongoDb = mongoDb;
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
        LOGGER.debug("initOrganizations start");
        if (organizationRepository.count() == 0) {
            Organization root = new Organization();
            root.setId(randomUUID().toString());
            root.setName("ROOT");
            root.setCode("ROOT");
            insert(root);

            Organization ppmdPoland = new Organization();
            ppmdPoland.setId(randomUUID().toString());
            ppmdPoland.setName("Fundacja Parent Project Muscular Dystrophy");
            ppmdPoland.setCode("PPMDPoland");
            ppmdPoland.setParentId(root.getId());
            insert(ppmdPoland);

            Organization test = new Organization();
            test.setId(randomUUID().toString());
            test.setName("Test");
            test.setCode("test");
            test.setParentId(root.getId());
            insert(test);
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
        LOGGER.debug("initUsers start");
        MongoCollection<User> userCollection = mongoDb.getCollection(User.class);
        if (userCollection.count() == 0) {
            Organization root = organizationRepository.findByCodeActive("ROOT");
            Organization ppmd = organizationRepository.findByCodeActive("PPMDPoland");
            
            // admin user
            User admin = new User();
            admin.setEmail("admin@starter.com");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setLocale("pl");
            admin.setOrganizationId(root.getId());
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setEnabled(true);
            admin.setAuthorities(asList(ADMIN.name()));
            userCollection.insertOne(admin);

            // normal user
            User user = new User();
            user.setEmail("user@starter.com");
            user.setFirstName("user");
            user.setLastName("user");
            user.setLocale("pl");
            user.setOrganizationId(ppmd.getId());
            user.setPasswordHash(passwordEncoder.encode("user"));
            user.setEnabled(true);
            user.setAuthorities(asList(USER.name(), EMPLOYEE.name()));
            userCollection.insertOne(user);

            // for activiti purposes
            User jbarrez = new User();
            jbarrez.setEmail("jbarrez@starter.com");
            jbarrez.setFirstName("Joram");
            jbarrez.setLastName("Barrez");
            jbarrez.setLocale("en");
            jbarrez.setOrganizationId(ppmd.getId());
            jbarrez.setPasswordHash(passwordEncoder.encode("user"));
            jbarrez.setEnabled(true);
            jbarrez.setAuthorities(asList(USER.name(), EMPLOYEE.name()));
            userCollection.insertOne(jbarrez);

            User trademakers = new User();
            trademakers.setEmail("trademakers@starter.com");
            trademakers.setFirstName("Tijs");
            trademakers.setLastName("Rademakers");
            trademakers.setLocale("en");
            trademakers.setOrganizationId(ppmd.getId());
            trademakers.setPasswordHash(passwordEncoder.encode("user"));
            trademakers.setEnabled(true);
            trademakers.setAuthorities(asList(USER.name(), EMPLOYEE.name()));

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            c.add(Calendar.MONTH, -1);
            trademakers.setLastAccess(c.getTime());
            userCollection.insertOne(trademakers);
        }
    }

    private void initContactMethods() {
        LOGGER.debug("initContactMethods start");
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
        LOGGER.debug("initAddressDictionary start");

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
                    String code = line[0];
                    String country_en = line[2];
                    String country_pl = !"".equals(line[3]) ? line[3] : country_en;
                    String country_de = !"".equals(line[4]) ? line[4] : country_en;

                    LOGGER.debug("initAddressDictionary: " + code + "," + country_en);
                    insert(new CountryDictionary(code, country_en, country_pl, country_de));
                }
            } catch (FileNotFoundException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
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
        LOGGER.debug("initZipCodePoland start");

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
            } catch (FileNotFoundException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
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
