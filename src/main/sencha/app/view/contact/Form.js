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

Ext.define('Patients.view.contact.Form', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.form.field.TextArea'],
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
                fieldLabel: i18n.Id,
                allowBlank: false,
                bind: '{selectedObject.id}',
                disabled: true
            }, {
                xtype: 'textfield',
                name: 'method',
                fieldLabel: i18n.contact_method,
                allowBlank: false,
                bind: '{selectedObject.method}'
            }, {
                xtype: 'textareafield',
                name: 'description',
                fieldLabel: i18n.contact_description,
                allowBlank: true,
                bind: '{selectedObject.description}'
            }
        ],
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