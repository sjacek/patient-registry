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

Ext.define('Patients.view.auth.SigninTwoFa', {
    extend: 'Patients.view.base.LockingWindow',
    title: '<i class="x-fa fa-rocket"></i> ' + i18n.app_name + ': ' + i18n.auth_signin,
    defaultFocus: 'form',
    items: [{
            xclass: 'Patients.view.auth.Dialog',
            defaultButton: 'loginButton',
            autoComplete: false,
            bodyPadding: '20 20',
            header: false,
            width: 415,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            api: {
                submit: 'securityService.signin2fa'
            },
            paramsAsHash: true,
            defaults: {
                margin: '5 0'
            },
            items: [{
                    xtype: 'label',
                    text: i18n.auth_2fa_info
                }, {
                    xtype: 'numberfield',
                    hideTrigger: true,
                    minValue: 0,
                    maxValue: 999999,
                    name: 'code',
                    height: 55,
                    hideLabel: true,
                    allowBlank: false,
                    cls: 'verificationcode'
                }, {
                    xtype: 'button',
                    reference: 'loginButton',
                    scale: 'large',
                    ui: 'soft-green',
                    iconAlign: 'right',
                    iconCls: 'x-fa fa-angle-right',
                    text: i18n.auth_signin,
                    formBind: true,
                    listeners: {
                        click: 'onLoginButtonClick'
                    }
                }, {
                    xtype: 'component',
                    html: '<div style="text-align:right">' + '<a href="#auth.signin" class="link-forgot-password">' + i18n.auth_backtosignin + '</a></div>'
                }]
        }]
});
