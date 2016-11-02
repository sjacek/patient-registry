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
/* global Ext, serverUrl, i18n */

Ext.define('Patients.Util', {
    singleton: true,
    requires: ['Ext.window.Toast'],
    getCsrfToken: function () {
        return Ext.Ajax.request({
            url: serverUrl + 'csrf',
            method: 'GET'
        }).then(function (r) {
            var csrfToken = JSON.parse(r.responseText);
            Ext.Ajax.setDefaultHeaders({
                'X-CSRF-TOKEN': csrfToken.token
            });
        });
    },
    successToast: function (msg) {
        Ext.toast({
            html: msg,
            title: i18n.successful,
            align: 'br',
            shadow: true,
            width: 200,
            height: 100,
            paddingX: 20,
            paddingY: 20,
            slideInDuration: 100,
            hideDuration: 100,
            bodyStyle: {
                background: '#90b962',
                color: 'white',
                textAlign: 'center',
                fontWeight: 'bold'
            }
        });
    },
    errorToast: function (msg) {
        Ext.toast({
            html: msg,
            title: i18n.error,
            align: 'br',
            shadow: true,
            width: 200,
            height: 100,
            paddingX: 20,
            paddingY: 20,
            slideInDuration: 100,
            hideDuration: 100,
            bodyStyle: {
                background: '#d24352',
                color: 'white',
                textAlign: 'center',
                fontWeight: 'bold'
            }
        });
    },
    underline: function (str, c) {
        var pos = str.indexOf(c);
        if (pos !== -1) {
            return str.substring(0, pos) + '<u>' + c + '</u>' + str.substring(pos + 1);
        }
        return str;
    },
    markInvalidFields: function (form, validations) {
        validations.forEach(function (validation) {
            var field = form.findField(validation.field);
            if (field) {
                field.markInvalid(validation.messages);
            }
        });
    }

});