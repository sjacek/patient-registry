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
/* global Ext, i18n */

Ext.define('Patients.view.auth.Signout', {
    extend: 'Patients.view.base.LockingWindow',
    title: '<img src="resources/images/logo.png"/> ' + i18n.app_name + ': ' + i18n.auth_signout,
    cls: 'signout-page-container',
    items: [{
            xtype: 'container',
            cls: 'error-page-inner-container',
            layout: {
                type: 'vbox',
                align: 'center',
                pack: 'center'
            },
            items: [{
                    xtype: 'label',
                    cls: 'signout-page-top-text',
                    text: i18n.auth_signout_success
                }, {
                    xtype: 'label',
                    cls: 'signout-page-desc',
                    html: '<div><a href="#auth.signin">' + i18n.auth_backtosignin + '</a></div>'
                }]
        }]

});
