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

Ext.define('Patients.view.auth.LockScreen', {
    extend: 'Patients.view.base.LockingWindow',
    title: '<img src="resources/images/logo.png"/> ' + i18n.app_name + ': ' + i18n.auth_locked,
    defaultFocus: 'form',
    items: [{
            xclass: 'Patients.view.auth.Dialog',
            defaultButton: 'resumeButton',
            autoComplete: false,
            bodyPadding: '20 20',
            header: false,
            width: 455,
            defaults: {
                margin: '5 0'
            },
            api: {
                submit: 'securityService.disableScreenLock'
            },
            paramsAsHash: true,
            cls: 'auth-dialog-login',
            defaultFocus: 'textfield[inputType=password]',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [{
                    xtype: 'label',
                    text: i18n.auth_locked_text
                }, {
                    xtype: 'textfield',
                    hideLabel: true,
                    height: 55,
                    inputType: 'password',
                    name: 'password',
                    allowBlank: false,
                    enableKeyEvents: true,
                    triggers: {
                        glyphed: {
                            cls: 'trigger-glyph-noop password-trigger'
                        }
                    },
                    listeners: {
                        render: 'onPasswordRender',
                        keypress: 'onPasswordKeypress',
                        blur: 'onPasswordBlur'
                    }
                }, {
                    xtype: 'button',
                    reference: 'resumeButton',
                    scale: 'large',
                    ui: 'soft-blue',
                    iconAlign: 'right',
                    iconCls: 'x-fa fa-angle-right',
                    text: i18n.auth_locked_resume,
                    formBind: true,
                    listeners: {
                        click: 'onLoginResumeButtonClick'
                    }
                }]

        }]
});
