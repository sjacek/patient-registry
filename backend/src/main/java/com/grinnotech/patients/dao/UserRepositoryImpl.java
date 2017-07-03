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
package com.grinnotech.patients.dao;

import com.grinnotech.patients.model.Organization;
import com.grinnotech.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by jacek.sztajnke on 2017-07-03.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public UserRepositoryImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void loadOrganizationsData(User user) {
        user.setOrganizations(new HashSet<>((Collection<Organization>) organizationRepository.findAll(user.getOrganizationIds())));
    }

    @Override
    public void loadOrganizationsData(Collection<User> users) {
        users.forEach(this::loadOrganizationsData);
    }
}
