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

Ext.define('Patients.view.auth.PwReset', {
    extend: 'Patients.view.base.LockingWindow',
    title: '<img src="resources/images/logo.png"/> ' + i18n.app_name + ': ' + i18n.auth_pwreset,
    defaultFocus: 'form',
    items: [{
            xclass: 'Patients.view.auth.Dialog',
            width: 455,
            defaultButton: 'resetPassword',
            autoComplete: true,
            bodyPadding: '20 20',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            defaults: {
                margin: '5 0'
            },
            api: {
                submit: 'securityService.reset'
            },
            paramsAsHash: true,
            cls: 'auth-dialog-login',
            items: [{
                    xtype: 'label',
                    cls: 'lock-screen-top-label',
                    text: i18n.auth_pwreset_newpw
                }, {
                    xtype: 'textfield',
                    height: 55,
                    hideLabel: true,
                    emptyText: i18n.auth_signin_password,
                    inputType: 'password',
                    name: 'newPassword',
                    allowBlank: false,
                    enableKeyEvents: true,
                    triggers: {
                        glyphed: {
                            cls: 'trigger-glyph-noop auth-password-trigger'
                        }
                    },
                    listeners: {
                        render: 'onPasswordRender',
                        keypress: 'onPasswordKeypress',
                        blur: 'onPasswordBlur'
                    }
                }, {
                    xtype: 'textfield',
                    height: 55,
                    hideLabel: true,
                    emptyText: i18n.auth_signin_password,
                    inputType: 'password',
                    name: 'newPasswordRetype',
                    allowBlank: false,
                    enableKeyEvents: true,
                    triggers: {
                        glyphed: {
                            cls: 'trigger-glyph-noop auth-password-trigger'
                        }
                    },
                    listeners: {
                        render: 'onPasswordRender',
                        keypress: 'onPasswordKeypress',
                        blur: 'onPasswordBlur'
                    }
                }, {
                    xtype: 'button',
                    reference: 'resetPassword',
                    scale: 'large',
                    ui: 'soft-blue',
                    formBind: true,
                    iconAlign: 'right',
                    iconCls: 'x-fa fa-angle-right',
                    text: i18n.auth_pwreset,
                    listeners: {
                        click: 'onResetClick'
                    }
                }, {
                    xtype: 'component',
                    html: '<div style="text-align:right">' + '<a href="#auth.signin" class="link-forgot-password">' + i18n.auth_backtosignin + '</a></div>'
                }]
        }]
});
