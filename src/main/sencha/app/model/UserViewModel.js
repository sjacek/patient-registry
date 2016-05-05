/* 
 * Copyright (C) 2016 jsztajnke
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

/**
 * This class is the ViewModel for the ticket details view.
 */
Ext.define('Patients.model.UserViewModel', {
    extend: 'Ext.app.ViewModel',
    requires: [
        'Patients.model.User'
    ],

    // This enables "viewModel: { type: 'user' }" in the view:
    alias: 'viewmodel.user',
    stores: {
        users: {
            model: 'Patients.model.User',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: 'user/findAll.json',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                },
                writer: {
                    type: 'json'
                }
            }
        }
    }
});
