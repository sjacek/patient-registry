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
package com.grinnotech.patients.domain;

import ch.rasc.extclassgenerator.ModelField;

/**
 *
 * @author Jacek Sztajnke
 */
public class AbstractTreePersistable extends AbstractPersistable {

    private String _parentId;

    @ModelField(defaultValue = "0")
    private int _level;

    public String getParentId() {
        return _parentId;
    }

    public void setParentId(String _parentId) {
        this._parentId = _parentId;
    }

    public int getLevel() {
        return _level;
    }

    public void setLevel(int _level) {
        this._level = _level;
    }
}
