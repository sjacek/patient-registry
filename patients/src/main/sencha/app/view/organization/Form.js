/* 
 * Copyright (C) 2017 Jacek Sztajnke
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

Ext.define('Patients.view.organization.Form', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.form.field.Date'],
    reference: 'editPanel',
    xtype: 'organization.form',
    header: {
        title: i18n.organization,
        defaults: {
            xtype: 'button',
            margin: '0 0 0 5'
        },
        items: [{
                text: i18n.back,
                handler: 'back',
                iconCls: 'x-fa fa-arrow-left',
                ui: 'soft-green'
            }, {
                text: 'Reset',
                handler: function () {
                    this.up('form').getForm().reset();
                },
                ui: 'soft-red'
            }, {
                text: Patients.Util.underline(i18n.save, 'S'),
                accessKey: 's',
                ui: 'soft-green',
                iconCls: 'x-fa fa-floppy-o',
                formBind: true,
                handler: 'save'
            }]
    },
    cls: 'shadow',
    bodyPadding: 20,
    modelValidation: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
            xtype: 'form',
            fieldDefaults: {
                labelAlign: 'right'
            },
            tabConfig: {
                title: i18n.organization_data,
                tootip: i18n.organization_data_tooltip
            },
            items: [{
                    layout: 'hbox',
                    defaults: {
                        margin: 5,
                        flex: 1
                    },
                    items: [{
                            xtype: 'textfield',
                            name: 'id',
                            fieldLabel: i18n.id,
                            bind: '{selectedObject.id}',
                            disabled: true
                        }, {
                            xtype: 'textfield',
                            name: 'version',
                            fieldLabel: i18n.version,
                            bind: '{selectedObject.version}',
                            disabled: true
                        }]
                }, {
                    defaults: {
                        margin: 5,
                        flex: 1
                    },
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [{
                            xtype: 'form',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            defaults: {
                                flex: 1,
                                margin: 5,
                                border: true
                            },
                            items: [{
                                    xtype: 'textfield',
                                    name: 'name',
                                    fieldLabel: i18n.organization_name,
                                    allowBlank: false,
                                    bind: '{selectedObject.name}'
                                }, {
                                    xtype: 'textfield',
                                    name: 'code',
                                    fieldLabel: i18n.organization_code,
                                    allowBlank: false,
                                    bind: '{selectedObject.code}'
//                                }, {
//                                    xtype: 'combo',
//                                    name: 'parent',
//                                    fieldLabel: i18n.organization_parent,
//                                    allowBlank: false,
//                                    bind: '{selectedObject.code}'
                                }]
                        }]
                }]
        }]
});