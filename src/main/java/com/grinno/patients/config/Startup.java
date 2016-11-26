/*
 * Copyright (C) 2016 Pivotal Software, Inc.
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
package com.grinno.patients.config;

import com.grinno.patients.dao.AddressDictionaryRepository;
import com.grinno.patients.dao.ContactRepository;
import com.grinno.patients.dao.ZipCodePolandRepository;
import com.grinno.patients.domain.AbstractPersistable;
import com.grinno.patients.model.AddressDictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import static com.grinno.patients.model.Authority.ADMIN;
import static com.grinno.patients.model.Authority.EMPLOYEE;
import static com.grinno.patients.model.Authority.USER;
import com.grinno.patients.model.ContactMethod;
import com.grinno.patients.model.User;
import com.grinno.patients.model.ZipCodePoland;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Component
class Startup {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final MongoDb mongoDb;

    private final PasswordEncoder passwordEncoder;

    private final ContactRepository contactRepository;
    
    private final AddressDictionaryRepository addressDictionaryRepository;
    
    private final ZipCodePolandRepository zipCodePolandRepository;

    @Autowired
    public Startup(MongoDb mongoDb,
            ContactRepository contactRepository, AddressDictionaryRepository addressDictionaryRepository, ZipCodePolandRepository zipCodePolandRepository, 
            PasswordEncoder passwordEncoder) {
        this.mongoDb = mongoDb;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressDictionaryRepository = addressDictionaryRepository;
        this.zipCodePolandRepository = zipCodePolandRepository;
        init();
    }

    private void init() {
        initUsers();
        initContactMethods();
        initAddressDictionary();
        initZipCodePoland();
    }
    
    private void initUsers() {
        MongoCollection<User> userCollection = mongoDb.getCollection(User.class);
        if (userCollection.count() == 0) {
            // admin user
            User adminUser = new User();
            adminUser.setEmail("admin@starter.com");
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setLocale("en");
            adminUser.setPasswordHash(passwordEncoder.encode("admin"));
            adminUser.setEnabled(true);
            adminUser.setDeleted(false);
            adminUser.setAuthorities(asList(ADMIN.name()));
            userCollection.insertOne(adminUser);

            // normal user
            User normalUser = new User();
            normalUser.setEmail("user@starter.com");
            normalUser.setFirstName("user");
            normalUser.setLastName("user");
            normalUser.setLocale("de");
            normalUser.setPasswordHash(passwordEncoder.encode("user"));
            normalUser.setEnabled(true);
            adminUser.setDeleted(false);
            normalUser.setAuthorities(asList(USER.name(), EMPLOYEE.name()));
            userCollection.insertOne(normalUser);
        }
    }
    
    private void initContactMethods() {
        if (contactRepository.count() == 0) {
            insert(new ContactMethod() {{
                setMethod("telefon domowy");
                setDescription("Telefon domowy");
            }});
            insert(new ContactMethod() {{
                setMethod("telefon komórkowy");
                setDescription("Telefon komórkowy");
            }});
            insert(new ContactMethod() {{
                setMethod("telefon służbowy");
                setDescription("Telefon służbowy");
            }});
            insert(new ContactMethod() {{
                setMethod("e-mail");
                setDescription("Poczta elektroniczna");
            }});
        }
    }
    
    private void initAddressDictionary() {
        final String CSV = "iso_panstwa.csv";

        LOGGER.debug("initAddressDictionary start");
        
        if (addressDictionaryRepository.count() == 0) {
            try {
                CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(CSV).getFile()), ';', '"', 1);

//                ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
//                strat.setType(AddressDictionary.class);
//                strat.setColumnMapping(new String[] {"nazwa"});
//                new CsvToBean().parse(strat, reader).forEach(address -> insert((AbstractPersistable) address));

//                HeaderColumnNameMappingStrategy<AddressDictionary> strategy = new HeaderColumnNameMappingStrategy<>();
//                strategy.setType(AddressDictionary.class);
//                
//                CsvToBean<AddressDictionary> csvToBean = new CsvToBean<>();
//                
//                csvToBean.parse(strategy, reader).forEach(address -> insert(address));

                String[] line;
                while ((line = reader.readNext()) != null) {
                    // line[] is an array of values from the line
                    LOGGER.debug("initAddressDictionary: " + line[2]);
                    insert(new AddressDictionary(line[2]));
                }                
            } catch (FileNotFoundException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            } catch (IOException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            }
        }
    }

    private void initZipCodePoland() {
        final String CSV = "kody-pocztowe_GUS.csv";

        LOGGER.debug("initZipCodePoland start");
        
        if (zipCodePolandRepository.count() == 0) {
            try {
                CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(CSV).getFile()), ';', '"', 1);

                String[] line;
                final char[] delimiters = {' ', ',','.','-','(',')'};
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
                    }
                    else {
                        LOGGER.debug("****************** found, don't insert!");
                    }
                }                
            } catch (FileNotFoundException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            } catch (IOException ex) {
                LOGGER.error("File " + CSV + " not found", ex);
            }
        }
    }
    
    private void insert(AbstractPersistable record) {
        record.setVersion(1);
        record.setActive(true);

        if (record instanceof ContactMethod) {
            ContactMethod contact = contactRepository.insert((ContactMethod)record);
            contact.setChainId(contact.getId());
            contactRepository.save(contact);
        }
        else if (record instanceof AddressDictionary) {
            AddressDictionary address = addressDictionaryRepository.insert((AddressDictionary)record);
            address.setChainId(address.getId());
            addressDictionaryRepository.save(address);
        }
        else if (record instanceof ZipCodePoland) {
            ZipCodePoland zipCode = zipCodePolandRepository.insert((ZipCodePoland)record);
            zipCode.setChainId(zipCode.getId());
            zipCodePolandRepository.save(zipCode);
        }
    }
    
}
