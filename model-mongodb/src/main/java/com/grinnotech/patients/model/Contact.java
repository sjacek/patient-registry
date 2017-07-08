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
package com.grinnotech.patients.model;

import ch.rasc.extclassgenerator.Model;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.GET;

/**
 *
 * @author Jacek Sztajnke
 */
@Model(value = "Patients.model.Contact")
@Getter
@Setter
public class Contact {

    @NotBlank(message = "{fieldrequired}")
    private String method;

    @NotBlank(message = "{fieldrequired}")
    private String contact;
}
