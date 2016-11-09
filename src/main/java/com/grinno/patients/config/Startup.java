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

import com.grinno.patients.dao.ContactRepository;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import com.grinno.patients.model.Authority;
import com.grinno.patients.model.Contact;
import com.grinno.patients.model.User;

@Component
class Startup {

    private final MongoDb mongoDb;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ContactRepository contactRepository;
    
    @Autowired
    public Startup(MongoDb mongoDb, ContactRepository contactRepository, PasswordEncoder passwordEncoder) {
        this.mongoDb = mongoDb;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        init();
    }

    private void init() {

        MongoCollection<User> userCollection = mongoDb.getCollection(User.class);
        if (userCollection.count() == 0) {
            // admin user
            User adminUser = new User();
            adminUser.setEmail("admin@starter.com");
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setLocale("en");
            adminUser.setPasswordHash(this.passwordEncoder.encode("admin"));
            adminUser.setEnabled(true);
            adminUser.setDeleted(false);
            adminUser.setAuthorities(Arrays.asList(Authority.ADMIN.name()));
            userCollection.insertOne(adminUser);

            // normal user
            User normalUser = new User();
            normalUser.setEmail("user@starter.com");
            normalUser.setFirstName("user");
            normalUser.setLastName("user");
            normalUser.setLocale("de");
            normalUser.setPasswordHash(this.passwordEncoder.encode("user"));
            normalUser.setEnabled(true);
            adminUser.setDeleted(false);
            normalUser.setAuthorities(Arrays.asList(Authority.USER.name(), Authority.EMPLOYEE.name()));
            userCollection.insertOne(normalUser);
        }

        if (contactRepository.count() == 0) {
            {
                Contact contact = new Contact();
                contact.setMethod("telefon domowy");
                contact.setDescription("Telefon domowy");
                contact.setVersion(1);
                contact.setActive(true);
                contact = contactRepository.insert(contact);
                contact.setChainId(contact.getId());
                contactRepository.save(contact);
            }
            {
                Contact contact = new Contact();
                contact.setMethod("telefon komórkowy");
                contact.setDescription("Telefon komórkowy");
                contact.setVersion(1);
                contact.setActive(true);
                contact = contactRepository.insert(contact);
                contact.setChainId(contact.getId());
                contactRepository.save(contact);
            }
            {
                Contact contact = new Contact();
                contact.setMethod("telefon służbowy");
                contact.setDescription("Telefon służbowy");
                contact.setVersion(1);
                contact.setActive(true);
                contact = contactRepository.insert(contact);
                contact.setChainId(contact.getId());
                contactRepository.save(contact);
            }
            {
                Contact contact = new Contact();
                contact.setMethod("e-mail");
                contact.setDescription("Poczta elektroniczna");
                contact.setVersion(1);
                contact.setActive(true);
                contact = contactRepository.insert(contact);
                contact.setChainId(contact.getId());
                contactRepository.save(contact);
            }
        }
    }

}
