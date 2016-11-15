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
package com.grinno.patients.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.TREE_LOAD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.dto.NavigationNode;
import com.grinno.patients.model.Authority;
import static com.grinno.patients.model.Authority.ADMIN;
import static com.grinno.patients.model.Authority.EMPLOYEE;
import static com.grinno.patients.model.Authority.USER;

/**
 *
 * @author jacek
 */
@Service
public class NavigationService {

    private final MessageSource messageSource;

    private final List<NavigationNode> rootNodes = new ArrayList<>();

    @Autowired
    public NavigationService(MessageSource messageSource) {
        this.messageSource = messageSource;

        rootNodes.add(new NavigationNode("user_users", "user.Container", true, null, "x-fa fa-users", "users", ADMIN));
        rootNodes.add(new NavigationNode("patient_patients", "patient.Container", true, null, "x-fa fa-address-book", "patients", ADMIN, USER));
        rootNodes.add(new NavigationNode("contact_dictionary", "contactmethod.Container", true, null, "x-fa fa-book", "contacts", ADMIN, EMPLOYEE));
        rootNodes.add(new NavigationNode("Blank", "main.BlankPage", true, null, "x-fa fa-clock-o", "blank", USER));
        
        if (rootNodes.isEmpty())
            rootNodes.add(new NavigationNode("Blank", "main.BlankPage", true, null, "x-fa fa-clock-o", "blank"));
    }

    @ExtDirectMethod(TREE_LOAD)
    public List<NavigationNode> getNavigation(Locale locale, @AuthenticationPrincipal MongoUserDetails userDetails) {

        if (userDetails != null && !userDetails.isPreAuth()) {
            return rootNodes.stream().map(n -> NavigationNode.copyOf(n, userDetails.getAuthorities(), locale, messageSource))
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
