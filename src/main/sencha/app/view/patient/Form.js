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
    requires: ['Ext.form.field.Date', 'Ext.form.field.ComboBox', 'Ext.grid.Panel', 'Ext.grid.plugin.CellEditing', 'Patients.store.ContactMethod'],
    reference: 'editPanel',
    xtype: 'patient.form',
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
                            xtype: 'combobox',
                            fieldLabel: i18n.patient_status,
                            store: 'patientStatus',
                            bind: {
                                value: '{selectedObject.status}'
                            },
                            name: 'status',
                            valueField: 'value',
                            displayField: 'text',
                            queryMode: 'local',
                            forceSelection: true,
                            editable: false
                        }, {
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
                                    text: i18n.new,
                                    handler: 'onContactsNewClick'
                                }]
                        }, {
                            xtype: 'grid',
                            bind: {
                                store: '{selectedObject.contacts}'
                            },
                            plugins: {
                                ptype: 'cellediting',
                                clicksToEdit: 2
                            },
                            selModel: 'cellmodel',
                            columns: [{
                                    dataIndex: 'method',
                                    text: i18n.patient_contact_method,
                                    editor: {
                                        completeOnEnter: false,
                                        xtype: 'combo',
                                        store: Ext.create('Patients.store.ContactMethod'),
                                        valueField: 'method',
                                        displayField: 'method',
                                        listConfig: {
                                            minWidth: 200
                                        }
                                    }
                                }, {
                                    dataIndex: 'contact',
                                    text: i18n.patient_contact,
                                    editor: {
                                        completeOnEnter: false,
                                        // If the editor config contains a field property, then
                                        // the editor config is used to create the <a href='Ext.grid.CellEditor.html'>Ext.grid.CellEditor</a>
                                        // and the field property is used to create the editing input field.
                                        field: {
                                            xtype: 'textfield',
                                            allowBlank: false
                                        }
                                    }
                                }, {
                                    xtype: 'actioncolumn',
                                    width: 30,
//                                    items: [{
                                    iconCls: 'x-fa fa-times',
                                    tooltip: i18n.patient_delete_contact,
                                    handler: 'onContactDeleteClick'
//                                        }]
                                }],
                            validate: function () {
                                return true;
                            }
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
                            forId: 'address_street',
                            text: i18n.patient_address
                        }, {
                            xtype: 'textfield',
                            id: 'address_street',
                            name: 'street',
                            fieldLabel: i18n.address_street,
                            allowBlank: false,
                            bind: '{address.street}'
                        }, {
                            layout: 'hbox',
                            items: [{
                                    xtype: 'textfield',
                                    name: 'house',
                                    fieldLabel: i18n.address_house_no,
                                    allowBlank: false,
                                    bind: '{address.house}'
                                }, {
                                    xtype: 'textfield',
                                    name: 'flat',
                                    fieldLabel: i18n.address_flat_no,
                                    allowBlank: true,
                                    bind: '{address.flat}'
                                }]
                        }, {
                            layout: 'hbox',
                            items: [{
                                    xtype: 'textfield',
                                    name: 'zipcode',
                                    fieldLabel: i18n.address_zipcode,
                                    allowBlank: false,
                                    bind: '{address.zipCode}'
                                }, {
                                    xtype: 'textfield',
                                    name: 'city',
                                    fieldLabel: i18n.address_city,
                                    allowBlank: false,
                                    bind: '{address.city}'
                                }]
                        }, {
                            xtype: 'textfield',
                            name: 'county',
                            fieldLabel: i18n.address_county,
                            allowBlank: false,
                            bind: '{address.county}'
                        }, {
                            xtype: 'textfield',
                            name: 'voivodship',
                            fieldLabel: i18n.address_voivodship,
                            allowBlank: false,
                            bind: '{address.voivodship}'
                        }, {
                            xtype: 'textfield',
                            name: 'country',
                            fieldLabel: i18n.address_country,
                            allowBlank: false,
                            bind: '{address.country}'
                        }]
                }, {
                    defaults: {
                        flex: 1,
                        margin: 5
                    },
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                            xtype: 'checkbox',
                            flex: 1,
                            fieldLabel: i18n.patient_correspondence_address,
                            bind: '{correspondenceAddressEnabled}'
                        }, {
                            xtype: 'textfield',
                            name: 'street',
                            fieldLabel: i18n.address_street,
                            allowBlank: false,
                            bind: {
                                value: '{correspondenceAddress.street}',
                                disabled: '{!correspondenceAddressEnabled}'
                            }
                        }, {
                            layout: 'hbox',
                            items: [{
                                    xtype: 'textfield',
                                    name: 'house',
                                    fieldLabel: i18n.address_house_no,
                                    allowBlank: false,
                                    bind: {
                                        value: '{correspondenceAddress.house}',
                                        disabled: '{!correspondenceAddressEnabled}'
                                    }
                                }, {
                                    xtype: 'textfield',
                                    name: 'flat',
                                    fieldLabel: i18n.address_flat_no,
                                    allowBlank: true,
                                    bind: {
                                        value: '{correspondenceAddress.flat}',
                                        disabled: '{!correspondenceAddressEnabled}'
                                    }
                                }]
                        }, {
                            layout: 'hbox',
                            items: [{
                                    xtype: 'textfield',
                                    name: 'zipcode',
                                    fieldLabel: i18n.address_zipcode,
                                    allowBlank: false,
                                    bind: {
                                        value: '{correspondenceAddress.zipCode}',
                                        disabled: '{!correspondenceAddressEnabled}'
                                    }
                                }, {
                                    xtype: 'textfield',
                                    name: 'city',
                                    fieldLabel: i18n.address_city,
                                    allowBlank: false,
                                    bind: {
                                        value: '{correspondenceAddress.city}',
                                        disabled: '{!correspondenceAddressEnabled}'
                                    }
                                }]
                        }, {
                            xtype: 'textfield',
                            name: 'county',
                            fieldLabel: i18n.address_county,
                            allowBlank: false,
                            bind: {
                                value: '{correspondenceAddress.county}',
                                disabled: '{!correspondenceAddressEnabled}'
                            }
                        }, {
                            xtype: 'textfield',
                            name: 'voivodship',
                            fieldLabel: i18n.address_voivodship,
                            allowBlank: false,
                            bind: {
                                value: '{correspondenceAddress.voivodship}',
                                disabled: '{!correspondenceAddressEnabled}'
                            }
                        }, {
                            xtype: 'textfield',
                            name: 'country',
                            fieldLabel: i18n.address_country,
                            allowBlank: false,
                            bind: {
                                value: '{correspondenceAddress.country}',
                                disabled: '{!correspondenceAddressEnabled}'
                            }
                        }]
                }]
        }, {
            layout: 'hbox',
            items: [{
                    xtype: 'combobox',
                    fieldLabel: i18n.patient_disabilitylevel,
                    store: 'disabilityLevel',
                    bind: '{selectedObject.disabilityLevel}',
                    name: 'disabilityLevel',
                    valueField: 'value',
                    displayField: 'text',
                    queryMode: 'local',
                    forceSelection: false,
                    editable: false,
                    listeners: {
                        select: 'onDisabilityLevelSelect'
                    }
                }, {
                    xtype: 'checkbox',
                    fieldLabel: i18n.expiration_date,
                    bind: {
                        value: '{certificateOfDisabilityExpirationEnabled}',
                        disabled: '{!certificateOfDisabilityEnabled}'
                    },
                    handler: 'onExpirationDateChange'
                }, {
                    xtype: 'datefield',
                    name: 'certificateOfDisabilityExpiration',
                    allowBlank: false,
                    bind: {
                        value: '{selectedObject.certificateOfDisabilityExpiration}',
                        hidden: '{!certificateOfDisabilityExpirationEnabled}'
                    }
                }, {
                    xtype: 'textfield',
                    flex: 1,
                    value: i18n.patient_certificateofdisability_indefinitely,
                    editable: false,
                    bind: {
                        disabled: '{!certificateOfDisabilityEnabled}',
                        hidden: '{certificateOfDisabilityExpirationEnabled}'
                    }
                }]
        }]
});