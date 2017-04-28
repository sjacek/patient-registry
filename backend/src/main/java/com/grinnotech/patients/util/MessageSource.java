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
package com.grinnotech.patients.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageSource {

    private ResourceBundle resourceBundle;

    public MessageSource(Locale locale)
    {
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
        // TODO: remove
        Enumeration<String> e = resourceBundle.getKeys();
        int a = 10;
    }

    public String getMessage(String key) {
        return resourceBundle.getString(key);
    }

    public String getMessage(String key, Object[] parameters) {
        try {
            return MessageFormat.format(resourceBundle.getString(key), parameters);
        }
        catch (MissingResourceException ex) {
            return '!' + key + '!';
        }
    }

}
