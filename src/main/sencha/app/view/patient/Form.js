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
    requires: ['Ext.form.field.Date', 'Ext.grid.Panel'],
    reference: 'editPanel',
    header: {
        title: i18n.patient_card,
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
    default: {
        flex: 1,
        margin: 5,
        border: true
    },
    items: [{
            xtype: 'textfield',
            name: 'id',
            fieldLabel: i18n.id,
            labelAlign: 'right',
            allowBlank: false,
            bind: '{selectedObject.id}',
            disabled: true
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
                    fieldDefaults: {
                        labelAlign: 'right'
                    },
                    default: {
                        flex: 1,
                        margin: 5,
                        border: true
                    },
                    items: [{
                            xtype: 'textfield',
                            name: 'firstName',
                            fieldLabel: i18n.patient_firstname,
                            allowBlank: false,
                            bind: '{selectedObject.firstName}'
                        }, {
                            xtype: 'textfield',
                            name: 'secondName',
                            fieldLabel: i18n.patient_secondname,
                            allowBlank: true,
                            bind: '{selectedObject.secondName}'
                        }, {
                            xtype: 'textfield',
                            name: 'lastName',
                            fieldLabel: i18n.patient_lastname,
                            allowBlank: false,
                            bind: '{selectedObject.lastName}'
                        }, {
                            xtype: 'textfield',
                            name: 'pesel',
                            fieldLabel: i18n.patient_pesel,
                            allowBlank: false,
                            bind: '{selectedObject.pesel}'
                        }, {
                            xtype: 'datefield',
                            name: 'birthday',
                            fieldLabel: i18n.patient_birthday,
                            allowBlank: false,
                            bind: '{selectedObject.birthday}'
                        }]
                }, {
                    xtype: 'container',
                    border: true,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    default: {
                        flex: 1,
                        margin: 5,
                        border: true
                    },
                    items: [{
                            defaults: {
                                flex: 0
                            },
                            layout: {
                                type: 'hbox',
                                align: 'stretch'
                            },
                            items: [{
                                    xtype: 'label',
                                    flex: 1,
                                    forId: 'contactsgrid',
                                    text: i18n.patient_contacts
                                }, {
                                    xtype: 'button',
                                    iconCls: 'x-fa fa-plus',
                                    text: i18n.new
//                                    align: 'right',
//                                    margin: '0 0 0 5'
//                                }, {
//                                    xtype: 'button',
//                                    iconCls: 'x-fa fa-remove',
////                                    align: 'right',
//                                    margin: '0 0 0 5'
                                }]
                        }, {
                            xtype: 'grid',
                            id: 'contactsgrid',
                            name: 'contacts',
                            store: {
                                fields: ['method', 'contact'],
                                data: [
                                    {method: 'telefon', contact: '123456'},
                                    {method: 'email', contact: 'bla@bla.com'}
                                ]
                            },
                            columns: [
                                {dataIndex: 'method', text: 'Telefon'},
                                {dataIndex: 'contact', text: 'Kontakt'}
                            ],
                            validate: function () {
                                return true;
                            }
//                                        bind: {
//                                        bind: '{selectedObject.contacts}'
//                                        },
//                                        valueField: 'value',
//                                        queryMode: 'local',
//                                        bind: '{selectedObject.contacts}'
                        }]
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
                    defaults: {
                        flex: 1,
                        margin: 5
                    },
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                            xtype: 'label',
                            flex: 1,
                            forId: 'address',
                            text: i18n.patient_address
                        }, {
                            xtype: 'address',
                            id: 'address',
//                            bind: '{selectedObject.address}'
                            bind: {
                                address: '{selectedObject.address}'
                            },
                            name: 'address'
//                            displayField: 'value',
//                            valueField: 'value',
//                            queryMode: 'local'
                        }]
                }, {
//                    defaults: {
//                        flex: 1,
//                        margin: 5
//                    },
//                    layout: {
//                        type: 'vbox',
//                        align: 'stretch'
//                    },
//                    items: [{
//                            xtype: 'label',
//                            flex: 1,
//                            forId: 'correspondence_address',
//                            text: i18n.patient_correspondence_address
//                        }, {
//                            xtype: 'address',
//                            id: 'correspondence_address',
//                            bind: '{selectedObject.correspondence_address}'
//                        }]
                }]
        }]
});