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

Ext.define('Patients.view.patient.Form', {
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
    items: {
        xtype: 'form',
        reference: 'form',
        items: [{
                xtype: 'textfield',
                name: 'id',
                fieldLabel: 'Id',
                allowBlank: false,
                bind: '{selectedObject.id}',
                publishes: ['value'],
                disabled: true
            }, {
                xtype: 'textfield',
                name: 'firstName',
                fieldLabel: 'First Name',
                allowBlank: false,
                bind: '{selectedObject.firstName}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'secondName',
                fieldLabel: 'Second Name',
                allowBlank: true,
                bind: '{selectedObject.secondName}',
                publishes: ['value']

            }, {
                xtype: 'textfield',
                name: 'lastName',
                fieldLabel: 'Last Name',
                allowBlank: false,
                bind: '{selectedObject.lastName}',
                publishes: ['value']

            }, {
                xtype: 'textfield',
                name: 'pesel',
                fieldLabel: 'PESEL',
                allowBlank: false,
                bind: '{selectedObject.pesel}',
                publishes: ['value']

            }],
        tbar: [{
                text: i18n.back,
                handler: 'back',
                iconCls: 'x-fa fa-arrow-left'
            }, {
                text: 'Reset',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            }, {
                text: Patients.Util.underline(i18n.save, 'S'),
                accessKey: 's',
                ui: 'soft-green',
                iconCls: 'x-fa fa-floppy-o',
                formBind: true,
                handler: 'save'
            }]
    }

});