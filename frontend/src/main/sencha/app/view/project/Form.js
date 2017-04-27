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

Ext.define('Patients.view.project.Form', {
    extend: 'Ext.form.Panel',
    requires: ['Ext.form.field.Date'],
    reference: 'editPanel',
    xtype: 'project.form',
    header: {
        title: i18n.project,
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
                fieldDefaults: {
                    labelAlign: 'right'
                },
                tabConfig: {
                    title: i18n.project_data,
                    tootip: i18n.project_data_tooltip
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
                                        fieldLabel: i18n.project_name,
                                        allowBlank: false,
                                        bind: '{selectedObject.name}'
                                    }, {
                                        xtype: 'datefield',
                                        name: 'start_date',
                                        fieldLabel: i18n.project_start_date,
                                        allowBlank: false,
                                        bind: '{selectedObject.start}'
                                    }, {
                                        xtype: 'datefield',
                                        name: 'end_date',
                                        fieldLabel: i18n.project_end_date,
                                        allowBlank: false,
                                        bind: '{selectedObject.end}'
                                    }]
                            }]
                    }]
            }, {
                title: i18n.project_employees,
                defaults: {
                    margin: 5,
                    flex: 0
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    }]
            }, {
                title: i18n.project_participants,
                defaults: {
                    margin: 5,
                    flex: 1
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    }]
            }]
    }
});