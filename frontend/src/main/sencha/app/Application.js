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


/* global Ext, serverUrl, POLLING_URLS, REMOTING_API, securityService */

/**
 * The main application class. An instance of this class is created by app.js when it
 * calls Ext.application(). This is the ideal place to handle application launch and
 * initialization details.
 */
Ext.define('Patients.Application', {
    extend: 'Ext.app.Application',
    requires: ['Ext.direct.*', 'Ext.form.action.DirectSubmit', 'Patients.*', 'Ext.state.Manager', 'Ext.state.LocalStorageProvider', 'Ext.container.Container'],
    name: 'Patients',

    stores: ['Navigation', 'Languages', 'Authority', 'DisabilityLevel', 'ProjectStatus', 'Gender'],

    globals: {
        organizationId: null
    },
    constructor: function () {
        // <debug>
        Ext.Ajax.on('beforerequest', function (conn, options, eOpts) {
            options.withCredentials = true;
        }, this);
        // </debug>

        var heartbeat = new Ext.direct.PollingProvider({
            id: 'heartbeat',
            type: 'polling',
            interval: 5 * 60 * 1000, // 5 minutes
            url: serverUrl + POLLING_URLS.heartbeat
        });

        REMOTING_API.url = serverUrl + REMOTING_API.url;
        REMOTING_API.maxRetries = 0;

        Ext.direct.Manager.addProvider(REMOTING_API, heartbeat);
        Ext.direct.Manager.getProvider('heartbeat').disconnect();

        Ext.state.Manager.setProvider(new Ext.state.LocalStorageProvider());

        this.callParent(arguments);
    },

    getUrlParam: function (param) {
        var params = Ext.urlDecode(location.search.substring(1));
        return param ? params[param] : params;
    },

    launch: function () {
        Ext.getBody().removeCls('loading');
        Ext.fly('loading_container').destroy();

        console.log("################### Location: " + location);
        console.log("################### individual: " + this.getUrlParam('individual'));

        var me = this;
        var token = window.location.search.split('token=')[1];
        if (token) {
            me.fireEvent('pwreset', me, token);
        } else if (window.location.search === '?logout') {
            me.fireEvent('logout', me);
        } else {
            Patients.Util.getCsrfToken().then(function () {
                securityService.getAuthUser(function (user, e, success) {
                    if (user) {
                        me.fireEvent('signedin', me, user);
                    } else {
                        me.fireEvent('notsignedin', me);
                    }
                });
            });

        }
    },

    onAppUpdate: function () {
        window.location.reload();
    }
});
