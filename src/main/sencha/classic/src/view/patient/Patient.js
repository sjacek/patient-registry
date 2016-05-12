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

Ext.define('Patients.view.patient.Patient', {
    extend: 'Ext.panel.Panel',
    xtype: 'patient',

    requires: [
        'Patients.view.patient.PatientController',
        'Ext.form.Panel'
    ],

    controller: 'patient',
    viewModel: {
        type: 'patient'
    },

    bind: {
        title: 'Patient - {thePatient.firstName} {thePatient.lastName}'
    },
    
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    
    componentCls: 'patient-detail',
    bodyPadding: 20,
    
    closable: true,
    autoShow: true,
    grid: null,
    items: {
        xtype: 'form',
        reference: 'form',
        items: [{
                xtype: 'textfield',
                name: 'firstName',
                fieldLabel: 'First Name',
                allowBlank: false,
                bind: '{thePatient.firstName}',
                publishes: ['value']
            }, {
                xtype: 'textfield',
                name: 'secondName',
                fieldLabel: 'Second Name',
                allowBlank: true,
                bind: '{thePatient.secondName}',
                publishes: ['value']

            }, {
                xtype: 'textfield',
                name: 'lastName',
                fieldLabel: 'Last Name',
                allowBlank: false,
                bind: '{thePatient.lastName}',
                publishes: ['value']

            }, {
                xtype: 'textfield',
                name: 'pesel',
                fieldLabel: 'PESEL',
                allowBlank: true,
                bind: '{thePatient.pesel}',
                publishes: ['value']

            }],
        tbar: [{
                text: 'Reset',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            }, {
                text: 'Save',
        	iconCls: 'x-fa fa-floppy-o',
                formBind: true,
                listeners: {
                    click: 'onSaveClick'
                }
            }]
    }
});
