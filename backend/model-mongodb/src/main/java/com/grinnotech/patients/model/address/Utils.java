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
package com.grinnotech.patients.model.address;

import com.grinnotech.patients.model.Patient;

public class Utils {

    public static String address(Patient patient) {
        Address address = patient.getAddress();
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(address.getAddress1()))
            sb.append(address.getAddress1());
        if (isNotEmpty(address.getAddress2())) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getAddress2());
        }
        if (sb.length() > 0) sb.append(", ");
        sb.append(address.getZipCode()).append(" ").append(address.getCity());
        sb.append(", ").append(address.getCountry());
        return sb.toString();
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }
}
