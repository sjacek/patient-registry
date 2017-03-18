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
import com.grinnotech.patients.domain.AbstractPersistable;
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
    
    private final UUID uuidRoot = randomUUID();
    
    private final UUID uuidPpmdPoland = randomUUID();
    
    private void initOrganizations() {
        LOGGER.debug("initOrganizations start");
        if (organizationRepository.count() == 0) {
            insert(new Organization() {{
                setId(uuidRoot.toString());
                setName("ROOT");
                setCode("ROOT");
            }});
            insert(new Organization() {{
                setId(uuidPpmdPoland.toString());
                setName("Fundacja Parent Project Muscular Dystrophy");
                setCode("PPMDPoland");
                setParentId(uuidRoot.toString());
            }});
        }
    }

    private void initUsers() {
        LOGGER.debug("initUsers start");
        MongoCollection<User> userCollection = mongoDb.getCollection(User.class);
        if (userCollection.count() == 0) {
            // admin user
            User adminUser = new User();
            adminUser.setEmail("admin@starter.com");
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setLocale("pl");
            adminUser.setOrganizationId(uuidRoot.toString());
            adminUser.setPasswordHash(passwordEncoder.encode("admin"));
            adminUser.setEnabled(true);
            adminUser.setAuthorities(asList(ADMIN.name()));
            userCollection.insertOne(adminUser);

            // normal user
            User normalUser = new User();
            normalUser.setEmail("user@starter.com");
            normalUser.setFirstName("user");
            normalUser.setLastName("user");
            normalUser.setLocale("pl");
            normalUser.setOrganizationId(uuidPpmdPoland.toString());
            normalUser.setPasswordHash(passwordEncoder.encode("user"));
            normalUser.setEnabled(true);
            normalUser.setAuthorities(asList(USER.name(), EMPLOYEE.name()));
            userCollection.insertOne(normalUser);
        }
    }
    
    private void initContactMethods() {
        LOGGER.debug("initContactMethods start");
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

    private void initZipCodePoland() {
        LOGGER.debug("initZipCodePoland start");
        
        final String CSV = "kody-pocztowe_GUS.csv";

        if (zipCodePolandRepository.count() == 0) {
            try {
                CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(CSV).getInputStream()), ';', '"', 1);

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
    
    private void insert(AbstractPersistable record) {
        record.setVersion(1);
        record.setActive(true);

        if (record instanceof Organization) {
            Organization organization = organizationRepository.insert((Organization)record);
            organization.setChainId(organization.getId());
            organizationRepository.save(organization);
        }
        else if (record instanceof ContactMethod) {
            ContactMethod contact = contactRepository.insert((ContactMethod)record);
            contact.setChainId(contact.getId());
            contactRepository.save(contact);
        }
        else if (record instanceof CountryDictionary) {
            CountryDictionary address = addressDictionaryRepository.insert((CountryDictionary)record);
            address.setChainId(address.getId());
            addressDictionaryRepository.save(address);
        }
        else if (record instanceof ZipCodePoland) {
            ZipCodePoland zipCode = zipCodePolandRepository.insert((ZipCodePoland)record);
            zipCode.setChainId(zipCode.getId());
            zipCodePolandRepository.save(zipCode);
        }
        else {
            throw new UnsupportedOperationException(record.getClass().getSuperclass() + " not supported in the insert method");
        }
    }
    
}
