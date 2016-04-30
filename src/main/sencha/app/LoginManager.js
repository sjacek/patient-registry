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
 * This class manages the login process.
 */
Ext.define('Patients.LoginManager', {
    config: {
        /**
         * @cfg {Class} model
         * The model class from which to create the "user" record from the login.
         */
        model: null,

        /**
         * @cfg {Ext.data.Session} session
         */
        session: null
    },

    constructor: function (config) {
        this.initConfig(config);
    },

    applyModel: function(model) {
        return model && Ext.data.schema.Schema.lookupEntity(model);
    },

    login: function(options) {
        Ext.Ajax.request({
            url: '/authenticate',
            method: 'GET',
            params: options.data,
            scope: this,
            callback: this.onLoginReturn,
            original: options
        });
    },
    
    onLoginReturn: function(options, success, response) {
        options = options.original;
        var session = this.getSession(),
            resultSet;
        
        if (success) {
            resultSet = this.getModel().getProxy().getReader().read(response, {
                recordCreator: session ? session.recordCreator : null
            });
                
            if (resultSet.getSuccess()) {
                Ext.callback(options.success, options.scope, [resultSet.getRecords()[0]]);
                return;
            }
        }

        Ext.callback(options.failure, options.scope, [response, resultSet]);
    }
});
