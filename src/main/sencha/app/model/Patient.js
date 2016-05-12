/* 
 * Copyright (C) 2015 jsztajnke
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

Ext.define('Patients.model.Patient', {
    extend: 'Ext.data.Model',
    config: {
        fields: [{
            name: 'internal_id',
            type: 'int',
            persist: false
        },{
            name: 'id',
            type: 'string'
        }, {
            name: 'firstName',
            type: 'string',
            validators : [
                { type : "notBlank" },
                {
                    type : "length",
                    min : 0,
                    max : 255
                } ]
        }, {
            name: 'secondName',
            type: 'string',
            validators : [
                {
                    type : "length",
                    min : 0,
                    max : 255
                } ]
        }, {
            name: 'lastName',
            type: 'string',
            validators : [
                { type : "notBlank" },
                {
                    type : "length",
                    min : 0,
                    max : 255
                } ]
        }, {
            name: 'pesel',
            type: 'string',
            validators : [
                {
                    type : "length",
                    min : 11,
                    max : 11
                } ]
        }]
    },
    idProperty: 'internal_id',
    proxy: {
        type: 'ajax',
        idParam: 'id',
        api: {
            create:  'patient/store.json',
            read:    'patient/find.json',
            update:  'patient/store.json',
            destroy: 'patient/remove.json'
        },
        reader: {
            type: 'json',
            rootProperty: 'data'
        },
        writer: {
            type: 'json',
            allowSingle: true,
            encode: true,
            rootProperty: 'data',
            writeAllFields: true
        }
    }
});
