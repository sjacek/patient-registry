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
 * The main application class. An instance of this class is created by app.js when it
 * calls Ext.application(). This is the ideal place to handle application launch and
 * initialization details.
 */
Ext.define('Patients.Application', {
    extend: 'Ext.app.Application',
    requires: [ 'Patients.store.Authority' ],
    name: 'Patients',

    stores: [
        // TODO: add global / shared stores here
        'Authority'
    ],
    
    views: [
        'Patients.view.login.Login',
        'Patients.view.main.Main'
    ],
    controllers: [
        'Root@Patients.controller'
    ],

/*
    launch: function () {
        // It's important to note that this type of application could use
        // any type of storage, i.e., Cookies, LocalStorage, etc.
        var loggedIn;

        // Check to see the current value of the localStorage key
        loggedIn = localStorage.getItem("PatientsLoggedIn");

        // This ternary operator determines the value of the PatientsLoggedIn key.
        // If PatientsLoggedIn isn't true, we display the login window,
        // otherwise, we display the main view
        Ext.create({
//            xtype: loggedIn ? 'app-main' : 'login'
            xtype: 'login'
        });
    },
*/
    onAppUpdate: function () {
        Ext.Msg.confirm('Application Update', 'This application has an update, reload?',
            function (choice) {
                if (choice === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});
