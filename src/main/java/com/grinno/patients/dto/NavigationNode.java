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
package com.grinno.patients.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.grinno.patients.model.Authority;

@JsonInclude(Include.NON_NULL)
public class NavigationNode {

    private final String text;

    private final String view;

    private final Boolean leaf;

    private final Boolean selectable;

    private final String iconCls;

    private final String routeId;

    @JsonIgnore
    private EnumSet<Authority> authorities;

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

    public static NavigationNode copyOf(NavigationNode source,
            Collection<? extends GrantedAuthority> authorities, Locale locale,
            MessageSource messageSource) {
        if (hasAuthority(source, authorities)) {
            NavigationNode menuNode = new NavigationNode(
                    messageSource.getMessage(source.getText(), null, source.getText(),
                            locale),
                    source.getView(), source.getLeaf(), source.getSelectable(),
                    source.getIconCls(), source.getRouteId());

            List<NavigationNode> children = new ArrayList<>();
            for (NavigationNode sourceChild : source.getChildren()) {
                if (hasAuthority(sourceChild, authorities)) {
                    NavigationNode copy = NavigationNode.copyOf(sourceChild, authorities,
                            locale, messageSource);
                    if (copy != null) {
                        children.add(copy);
                    }
                }
            }

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
        if (node.authorities == null || node.authorities.isEmpty()) {
            return true;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (node.authorities.contains(Authority.valueOf(grantedAuthority.getAuthority()))) {
                return true;
            }
        }
        return false;
    }

    public String getText() {
        return this.text;
    }

    public String getView() {
        return this.view;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

    public Boolean getSelectable() {
        return this.selectable;
    }

    public String getIconCls() {
        return this.iconCls;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public List<NavigationNode> getChildren() {
        return this.children;
    }

    public void addChild(NavigationNode menuNode) {
        this.children.add(menuNode);

    }

}
