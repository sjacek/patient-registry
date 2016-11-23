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
    items: {
        xtype: 'tabpanel',
        border: true,
        items: [{
                xtype: 'form',
//                scrollable: true,
                fieldDefaults: {
                    labelAlign: 'right'
                },
                tabConfig: {
                    title: i18n.patient_personal_data,
                    tootip: i18n.patient_personal_data_tooltip
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
                                xtype: 'fieldset',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                defaults: {
                                    flex: 1,
                                    margin: 5
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
                                xtype: 'fieldset',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
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
                                        xtype: 'fieldset',
                                        title: i18n.patient_contacts,
                                        border: false,
                                        margins: '0 0 0 0',
//                                        defaults: {
//                                            flex: 0
//                                        },
                                        items: [{
                                                xtype: 'button',
                                                iconCls: 'x-fa fa-plus',
                                                text: i18n.new,
                                                handler: 'onContactsNewClick',
                                            }, {
                                                xtype: 'grid',
                                                reference: 'contactsGrid',
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
                                                            allowBlank: false,
                                                            listConfig: {
                                                                minWidth: 200
                                                            }
                                                        }
                                                    }, {
                                                        dataIndex: 'contact',
                                                        text: i18n.patient_contact,
                                                        editor: {
                                                            completeOnEnter: false,
                                                            allowBlank: false,
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
//                                                      items: [{
                                                        iconCls: 'x-fa fa-times',
                                                        tooltip: i18n.patient_delete_contact,
                                                        handler: 'onContactDeleteClick'
//                                                          }]
                                                    }]
//                                        validate: 'validateContacts'
                                            }]
                                    }]
                            }]
                    }, {
                        xtype: 'fieldset',
                        title: i18n.patient_certificateofdisability,
                        defaults: {
                            flex: 0,
                            margin: 5
                        },
                        layout: 'vbox',
                        items: [{
                                defaults: {
                                    flex: 0,
                                    margin: 5
                                },
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
                                        xtype: 'datefield',
                                        fieldLabel: i18n.patient_issue_date,
                                        reference: 'certificateOfDisabilityIssueDate',
                                        allowBlank: false,
                                        bind: {
                                            value: '{selectedObject.certificateOfDisabilityIssue}',
                                            disabled: '{!certificateOfDisabilityEnabled}'
                                        }
                                    }, {
                                        xtype: 'textfield',
                                        flex: 1,
                                        fieldLabel: i18n.patient_issuing_unit,
                                        reference: 'certificateOfDisabilityIssuingUnit',
                                        allowBlank: false,
                                        bind: {
                                            value: '{selectedObject.certificateOfDisabilityIssuingUnit}',
                                            disabled: '{!certificateOfDisabilityEnabled}'
                                        }
                                    }]
                            }, {
                                defaults: {
                                    flex: 0,
                                    margin: 5
                                },
                                layout: {
                                    type: 'hbox',
                                    align: 'stretch',
                                    pack: 'end'
                                },
                                items: [{
                                        xtype: 'checkbox',
                                        fieldLabel: i18n.expiration_date,
                                        bind: {
                                            value: '{certificateOfDisabilityExpirationEnabled}',
                                            disabled: '{!certificateOfDisabilityEnabled}'
                                        },
                                        handler: 'onExpirationDateChange'
                                    }, {
                                        xtype: 'datefield',
                                        flex: 1,
                                        reference: 'certificateOfDisabilityExpirationDate',
                                        name: 'certificateOfDisabilityExpiration',
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
                                xtype: 'fieldset',
                                title: i18n.patient_address,
                                defaults: {
                                    flex: 1,
                                    margin: 5
                                },
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                items: [{
                                        xtype: 'textfield',
                                        id: 'country',
                                        name: 'country',
                                        fieldLabel: i18n.address_country,
                                        allowBlank: false,
                                        bind: '{address.country}'
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
                                        name: 'street',
                                        fieldLabel: i18n.address_street,
                                        allowBlank: false,
                                        bind: '{address.street}'
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
                                        xtype: 'textfield',
                                        name: 'voivodship',
                                        fieldLabel: i18n.address_voivodship,
                                        allowBlank: false,
                                        bind: '{address.voivodship}'
                                    }, {
                                        xtype: 'textfield',
                                        name: 'county',
                                        fieldLabel: i18n.address_county,
                                        allowBlank: false,
                                        bind: '{address.county}'
                                    }]
                            }, {
                                xtype: 'fieldset',
                                title: i18n.patient_correspondence_address,
                                checkboxToggle: true,
                                bind: {
                                    collapsed: '{!correspondenceAddressEnabled}'
                                },
                                defaults: {
                                    flex: 1,
                                    margin: 5
                                },
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                listeners: {
                                    collapse: 'onCorrespondenceAddressCollapsed',
                                    expand: 'onCorrespondenceAddressExpanded'
                                },
                                items: [{
                                        xtype: 'textfield',
                                        name: 'country',
                                        fieldLabel: i18n.address_country,
                                        bind: {
                                            value: '{correspondenceAddress.country}'
                                        }
                                    }, {
                                        xtype: 'form',
                                        defaults: {
                                            margin: 5,
                                            flex: 1
                                        },
                                        layout: {
                                            type: 'hbox',
                                            align: 'stretch'
                                        },
                                        items: [{
                                                xtype: 'textfield',
                                                name: 'zipcode',
                                                fieldLabel: i18n.address_zipcode,
                                                bind: {
                                                    value: '{correspondenceAddress.zipCode}'
                                                }
                                            }, {
                                                xtype: 'textfield',
                                                name: 'city',
                                                fieldLabel: i18n.address_city,
                                                bind: {
                                                    value: '{correspondenceAddress.city}'
                                                }
                                            }]
                                    }, {
                                        xtype: 'textfield',
                                        name: 'street',
                                        fieldLabel: i18n.address_street,
                                        bind: {
                                            value: '{correspondenceAddress.street}'
                                        }
                                    }, {
                                        xtype: 'form',
                                        defaults: {
                                            margin: 5,
                                            flex: 1
                                        },
                                        layout: {
                                            type: 'hbox',
                                            align: 'stretch'
                                        },
                                        items: [{
                                                xtype: 'textfield',
                                                name: 'house',
                                                fieldLabel: i18n.address_house_no,
                                                bind: {
                                                    value: '{correspondenceAddress.house}'
                                                }
                                            }, {
                                                xtype: 'textfield',
                                                name: 'flat',
                                                fieldLabel: i18n.address_flat_no,
                                                bind: {
                                                    value: '{correspondenceAddress.flat}'
                                                }
                                            }]
                                    }, {
                                        xtype: 'textfield',
                                        name: 'voivodship',
                                        fieldLabel: i18n.address_voivodship,
                                        bind: {
                                            value: '{correspondenceAddress.voivodship}'
                                        }
                                    }, {
                                        xtype: 'textfield',
                                        name: 'county',
                                        fieldLabel: i18n.address_county,
                                        bind: {
                                            value: '{correspondenceAddress.county}'
                                        }
                                    }]
                            }]
                    }]
            }, {
                title: i18n.diagnosis,
                defaults: {
                    margin: 5,
                    flex: 0
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                        layout: 'hbox',
                        items: [{
                                xtype: 'splitter',
                                flex: 1,
                                align: 'stretch'
                            }, {
                                xtype: 'splitbutton',
                                name: 'copytemplate',
                                reference: 'copyDiagnosisTemplateBtn',
                                align: 'right',
                                text: i18n.patient_diagnosis_copy_template
                            }]
                    }, {
                        xtype: 'fieldset',
                        items: [{
                                xtype: 'textfield',
                                name: 'diagnosisName',
                                fieldLabel: i18n.diagnosis_name,
                                allowBlank: false,
                                bind: '{selectedObject.diagnosis.diagnosisName}'
                            }, {
                                xtype: 'textfield',
                                name: 'diagnosisEnglishName',
                                fieldLabel: i18n.diagnosis_english_name,
                                allowBlank: false,
                                bind: '{selectedObject.diagnosis.diagnosisEnglishName}'
                            }, {
                                xtype: 'textfield',
                                name: 'icd10',
                                fieldLabel: i18n.diagnosis_icd10,
                                allowBlank: false,
                                bind: '{selectedObject.diagnosis.icd10}'
                            }]
                    }]

            }, {
                title: i18n.patient_notes,
                defaults: {
                    margin: 5,
                    flex: 1
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [{
                                xtype: 'splitter',
                                flex: 1,
                                align: 'stretch'
                            }, {
                                xtype: 'button',
                                name: 'notes_refresh',
                                align: 'right',
                                text: i18n.refresh,
                                handler: function () {
                                    logService.debug("refresh");
                                }
                            }]
                    }]
            }]
    },
    isDirty: function () {
        return false;
    }
});