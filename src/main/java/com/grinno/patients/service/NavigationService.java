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

@Service
public class NavigationService {

    private final MessageSource messageSource;

    private final List<NavigationNode> rootNodes = new ArrayList<>();

    @Autowired
    public NavigationService(MessageSource messageSource) {
        this.messageSource = messageSource;

        rootNodes.add(new NavigationNode("user_users", "user.Container", true, null, "x-fa fa-users", "users",
                Authority.ADMIN));

        rootNodes.add(new NavigationNode("patient_patients", "patient.Container", true, null, "x-fa fa-address-book", "patients",
                Authority.ADMIN, Authority.USER));

        rootNodes.add(new NavigationNode("contact_dictionary", "contact.Container", true, null, "x-fa fa-book", "contacts",
                Authority.ADMIN, Authority.EMPLOYEE));

        rootNodes.add(new NavigationNode("Blank", "main.BlankPage", true, null, "x-fa fa-clock-o", "blank",
                Authority.USER));
    }

    @ExtDirectMethod(TREE_LOAD)
    public List<NavigationNode> getNavigation(Locale locale, @AuthenticationPrincipal MongoUserDetails userDetails) {

        if (userDetails != null && !userDetails.isPreAuth()) {
            return this.rootNodes.stream()
                    .map(n -> NavigationNode.copyOf(n, userDetails.getAuthorities(), locale, this.messageSource))
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
