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

Ext.define('Patients.view.user.Form', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.form.field.ComboBox', 'Ext.form.field.Tag'],
    defaultFocus: 'textfield[name=email]',
    reference: 'editPanel',
    cls: 'shadow',
    defaultType: 'textfield',
    defaults: {
        anchor: '50%'
    },
    bodyPadding: 20,
    modelValidation: true,
    items: [{
            bind: '{selectedObject.email}',
            name: 'email',
            fieldLabel: i18n.user_email
        }, {
            bind: '{selectedObject.firstName}',
            name: 'firstName',
            fieldLabel: i18n.user_firstname
        }, {
            bind: '{selectedObject.lastName}',
            name: 'lastName',
            fieldLabel: i18n.user_lastname
        }, {
            xtype: 'combobox',
            fieldLabel: i18n.language,
            bind: {
                value: '{selectedObject.locale}'
            },
            name: 'locale',
            store: 'languages',
            valueField: 'value',
            queryMode: 'local',
            emptyText: i18n.language_select,
            forceSelection: true,
            editable: false
        }, {
            bind: '{selectedObject.enabled}',
            fieldLabel: i18n.user_enabled,
            name: 'enabled',
            xtype: 'checkboxfield',
            msgTarget: 'side',
            inputValue: 'true',
            uncheckedValue: 'false'
        }, {
            xtype: 'tagfield',
            fieldLabel: i18n.user_authorities,
            store: 'authority',
            bind: {
                value: '{selectedObject.authorities}'
            },
            name: 'authorities',
            displayField: 'value',
            valueField: 'value',
            queryMode: 'local',
            forceSelection: true,
            autoSelect: true,
            editable: false,
            selectOnFocus: false,
            filterPickList: true
        }],
    dockedItems: [{
            xtype: 'toolbar',
            dock: 'top',
            items: [{
                    text: i18n.back,
                    handler: 'back',
                    iconCls: 'x-fa fa-arrow-left'
                }, {
                    text: Patients.Util.underline(i18n.save, 'S'),
                    accessKey: 's',
                    ui: 'soft-green',
                    iconCls: 'x-fa fa-floppy-o',
                    formBind: true,
                    handler: 'save'
                }, '-', {
                    text: i18n.destroy,
                    iconCls: 'x-fa fa-trash-o',
                    handler: 'erase',
                    ui: 'soft-red',
                    bind: {
                        hidden: '{isPhantomObject}'
                    }
                }, {
                    text: i18n.user_send_pwresetreq,
                    iconCls: 'x-fa fa-envelope-o',
                    handler: 'sendPwResetReq',
                    bind: {
                        hidden: '{isUserDisabled}'
                    }
                }, {
                    text: i18n.user_switchto,
                    iconCls: 'x-fa fa-user-secret',
                    handler: 'switchTo',
                    bind: {
                        hidden: '{isUserDisabled}'
                    }
                }, {
                    text: i18n.user_unlock,
                    handler: 'unlock',
                    iconCls: 'x-fa fa-unlock',
                    bind: {
                        hidden: '{!selectedObject.lockedOutUntil}'
                    }
                }, {
                    text: i18n.user_disable_twofactorauth,
                    iconCls: 'x-fa fa-trash-o',
                    handler: 'disableTwoFactorAuth',
                    bind: {
                        hidden: '{!selectedObject.twoFactorAuth}'
                    }
                }]
        }]

});