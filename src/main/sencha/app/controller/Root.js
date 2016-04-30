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
 * This global controller manages the login view and ensures that view is created when
 * the application is launched. Once login is complete we then create the main view.
 */
Ext.define('Patients.controller.Root', {
    extend: 'Ext.app.Controller',

    requires: [
        'Patients.view.login.Login',
        'Patients.view.main.Main',
        'Patients.LoginManager'
    ],

    loadingText: 'Loading, loading...',

    onLaunch: function () {
        if (Ext.isIE8) {
            Ext.Msg.alert('Not Supported', 'This example is not supported on Internet Explorer 8. Please use a different browser.');
            return;
        }
        
        this.session = new Ext.data.Session({
            autoDestroy: false
        });

/*        this.login = new Patients.view.login.Login({
            session: this.session,
            autoShow: true,
            listeners: {
                scope: this,
                login: 'onLogin'
            }
        });
*/
/*
        Ext.create('login', {
            session: this.session,
            autoShow: true,
            listeners: {
                scope: this,
                login: 'onLogin'
            }
        });
*/        this.showUI();
    },

    /**
     * Called when the login controller fires the "login" event.
     *
     * @param loginController
     * @param user
     * @param organization
     * @param loginManager
     */
    onLogin: function (loginController, user, organization, loginManager) {
        this.login.destroy();

        this.loginManager = loginManager;
        this.organization = organization;
        this.user = user;

//        this.showUI();
    },

    showUI: function() {
        this.viewport = new Patients.view.main.Main({
            session: this.session,
            viewModel: {
                data: {
                    currentOrg: this.organization,
                    currentUser: this.user
                }
            }
        });
    },

    getSession: function() {
        return this.session;
    }

});
