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

/**
 * This view is an example list of people.
 */
Ext.define('Patients.view.patient.PatientList', {
    extend: 'Ext.grid.Panel',
    xtype: 'patientlist',

    requires: [
        'Patients.model.PatientViewModel',
        'Patients.store.Patients'
    ],
    controller: 'patientlist',
    reference: 'patientListGrid',

    title: 'Patient List',

    listeners: {
        selectionchange: 'onSelectionChange',
//        select: 'onItemSelected'
        rowdblclick: 'onRowDblClick'
    },
    viewModel: { type: 'patient' },
//    bind: '{patients}',
    store: { type: 'patients' },

    columns: [
        { text: 'Id',          dataIndex: 'id' },
        { text: 'First Name',  dataIndex: 'firstName' },
        { text: 'Second Name', dataIndex: 'secondName' },
        { text: 'Last Name',   dataIndex: 'lastName' },
        { text: 'PESEL',       dataIndex: 'pesel', flex: 1 }
    ],
    tbar: ['->', {
            text: 'Add',
            tooltip: 'Add a new patient',
            handler: 'onAdd'
        },{
            text: 'Edit',
            reference: 'editPatientButton',
            tooltip: 'Edit selected patient\'s data',
            disabled: true,
            handler: 'onEdit'
        },{
            text: 'Remove',
            reference: 'removePatientButton',
            tooltip: 'Remove selected patient',
            disabled: true,
            handler: 'onRemove'
    }],
    dockedItems: [
        { xtype: 'pagingtoolbar', store: { type: 'patients' } , dock: 'bottom', displayInfo: true }
    ]
});
