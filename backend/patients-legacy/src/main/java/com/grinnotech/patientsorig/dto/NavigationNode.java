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
package com.grinnotech.patientsorig.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinnotech.patientsorig.model.Authority;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.grinnotech.patientsorig.model.Authority.valueOf;

@JsonInclude(NON_NULL)
@Getter
public class NavigationNode {

    private final String text;

    private final String view;

    private final Boolean leaf;

    private final Boolean selectable;

    private final String iconCls;

    private final String routeId;

    @JsonIgnore
    private final EnumSet<Authority> authorities;

    private final List<NavigationNode> children = new ArrayList<>();

    public NavigationNode(String text, String view, Boolean leaf, Boolean selectable,
            String iconCls, String routeId, Authority... authorities) {
        this.text = text;
        this.view = view;
        this.leaf = leaf;
        this.selectable = selectable;
        this.iconCls = iconCls;
        this.routeId = routeId;
        if (authorities != null && authorities.length > 0) {
            this.authorities = EnumSet.copyOf(Arrays.asList(authorities));
        } else {
            this.authorities = EnumSet.noneOf(Authority.class);
        }
    }

    public static NavigationNode copyOf(NavigationNode source, Collection<? extends GrantedAuthority> authorities, Locale locale, MessageSource messageSource) {
        if (hasAuthority(source, authorities)) {
            NavigationNode menuNode = new NavigationNode(
                    messageSource.getMessage(source.getText(), null, source.getText(), locale),
                    source.getView(), source.getLeaf(), source.getSelectable(),
                    source.getIconCls(), source.getRouteId());

            List<NavigationNode> children = new ArrayList<>();
            source.getChildren().stream()
                    .filter(sourceChild -> (hasAuthority(sourceChild, authorities)))
                    .map(sourceChild -> NavigationNode.copyOf(sourceChild, authorities, locale, messageSource))
                    .filter(copy -> (copy != null))
                    .forEach(children::add);

            if (!children.isEmpty()) {
                menuNode.children.addAll(children);
                return menuNode;
            }

            if (menuNode.view != null) {
                return menuNode;
            }
        }

        return null;
    }

    private static boolean hasAuthority(NavigationNode node, Collection<? extends GrantedAuthority> authorities) {
        return node.authorities == null || node.authorities.isEmpty()
                || authorities.stream().anyMatch(
                        grantedAuthority -> node.authorities.contains(valueOf(grantedAuthority.getAuthority())));
    }

    public void addChild(NavigationNode menuNode) {
        children.add(menuNode);
    }
}
