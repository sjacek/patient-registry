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
 * This is the View Model associated to the login view.
 */
Ext.define('Patients.view.login.LoginModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.login',

    // Just some data to seed the process. This might be pulled from a cookie or other
    // in a real app.
    data: {
        defaultOrg: 1,
        username: 'Don'
    },

    stores: {
        /**
         * @property {Ext.data.Store} organizations
         * This store definition populates the Organization combobox.
         */
        organizations: {
            model: 'Organization',

            autoLoad: true,

            // Associate this store with the data session (an Ext.data.Session).
            // This ensures the Organization records are cached and distinct going forward.
            session: true
        }
    }
});
