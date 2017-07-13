/* 
 * Copyright (C) 2015 Jacek Sztajnke
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

/* global Ext */

Ext.define('Patients.store.CountryDictionary', {
    extend: 'Ext.data.Store',
    alias: 'store.countryDictionary',
    requires: ['Patients.model.CountryDictionary'],
    model: 'Patients.model.CountryDictionary',
    autoLoad: true,
    autoSync: true,
    pageSize: 100,
    buffered: true,
    remoteSort: true,
    remoteFilter: true,
    sorters: [{
            property: 'countryEn',
            direction: 'ASC'
        }],
    leadingBufferZone: 200
});
