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

Ext.define('Patients.view.diagnosis.Form', {
    extend: 'Ext.form.Panel',
    reference: 'editPanel',
    xtype: 'diagnosis.form',
    header: {
        title: i18n.diagnosis_dictionary,
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
            layout: 'hbox',
            fieldDefaults: {
            },
            items: [{
                    name: 'id',
                    fieldLabel: i18n.id,
                    bind: '{selectedObject.id}'
                }, {
                    xtype: 'textfield',
                    name: 'version',
                    fieldLabel: i18n.version,
                    bind: '{selectedObject.version}',
                    labelAlign: 'right',
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
                            name: 'diagnosisName',
                            fieldLabel: i18n.diagnosis_name,
                            allowBlank: false,
                            bind: '{selectedObject.diagnosisName}'
                        }, {
                            xtype: 'textfield',
                            name: 'diagnosisEnglishName',
                            fieldLabel: i18n.diagnosis_english_name,
                            allowBlank: false,
                            bind: '{selectedObject.diagnosisEnglishName}'
                        }, {
                            xtype: 'textfield',
                            name: 'icd10',
                            fieldLabel: i18n.diagnosis_icd10,
                            allowBlank: false,
                            bind: '{selectedObject.icd10}'
                        }]
                }]
        }]
});