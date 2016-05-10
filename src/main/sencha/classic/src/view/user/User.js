/* 
 * Copyright (C) 2015 jsztajnke
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

Ext.define('Patients.view.user.User', {
    extend: 'Ext.panel.Panel',
    xtype: 'user',

    requires: [
        'Patients.view.user.UserController',
        'Ext.form.Panel',
        'Ext.form.field.Tag',
        'Patients.store.Authority'
    ],

    controller: 'user',
    viewModel: {
        type: 'user'
    },

    bind: {
        title: 'User - {theUser.loginName}'
    },
    
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    
    componentCls: 'user-detail',
    bodyPadding: 20,
    
//    title: 'User Data',
    closable: true,
    autoShow: true,
    grid: null,
    items: {
        xtype: 'form',
        reference: 'form',
        items: [{
                xtype: 'textfield',
                name: 'loginname',
                fieldLabel: 'Login Name',
                allowBlank: false,
                bind: '{theUser.loginName}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'firstname',
                fieldLabel: 'First Name',
                allowBlank: false,
                bind: '{theUser.firstName}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'lastname',
                fieldLabel: 'Last Name',
                allowBlank: false,
                bind: '{theUser.lastName}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'email',
                fieldLabel: 'e-mail',
                allowBlank: true,
                bind: '{theUser.email}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'newPassword',
		fieldLabel: 'New Password',
                allowBlank: true,
                bind: '{theUser.newPassword}',
		inputType: 'password',
                publishes: ['value'],
		validator: function() {
                    var newPasswordRetypeField = this.up().getForm().findField('newPasswordRetype');
                    var newPasswordRetype = newPasswordRetypeField.getValue();
                    var newPassword = this.getValue();
                    if ((Ext.isEmpty(newPassword) && Ext.isEmpty(newPasswordRetype)) || (newPassword === newPasswordRetype)) {
			newPasswordRetypeField.clearInvalid();
			return true;
                    }
                    newPasswordRetypeField.markInvalid('Passwords do not match!');
                    return 'Passwords do not match!';
		}
            }, {
                xtype: 'textfield',
                name: 'newPasswordRetype',
                fieldLabel: 'Retype New Password',
                bind: '{theUser.newPasswordRetype}',
                inputType: 'password',
                publishes: ['value'],
                validator: function() {
                    var newPasswordField = this.up().getForm().findField('newPassword');
                    var newPassword = newPasswordField.getValue();
                    var newPasswordRetype = this.getValue();
                    if ((Ext.isEmpty(newPassword) && Ext.isEmpty(newPasswordRetype)) || (newPassword === newPasswordRetype)) {
                        newPasswordField.clearInvalid();
                        return true;
                    }
                    newPasswordField.markInvalid('Passwords do not match!');
                    return 'Passwords do not match!';
                }
            }, {
		xtype: 'tagfield',
		fieldLabel: 'Authorities',
		store: 'authority',
		bind: {
			value: '{theUser.authorities}'
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
        tbar: [{
                text: 'Reset',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            }, {
                text: 'Save',
                formBind: true,
                listeners: {
                    click: 'onSaveClick'
                }
            }]
    }
});
