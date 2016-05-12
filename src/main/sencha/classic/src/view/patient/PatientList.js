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

Ext.define('Patients.view.patient.PatientList', {
    extend: 'Ext.grid.Panel',
    xtype: 'patientlist',

    requires: [
        'Patients.model.PatientViewModel',
        'Patients.store.Patient',
	'Patients.plugin.Clearable'
    ],
    controller: 'patientlist',
    reference: 'patientListGrid',

    title: 'Patient List',

    listeners: {
        selectionchange: 'onSelectionChange',
        rowdblclick: 'onRowDblClick'
    },
    viewModel: { type: 'patient' },
    store: { type: 'patient' },

    columns: [
//        { text: 'Id',          dataIndex: 'id' },
        { text: 'First Name',  dataIndex: 'firstName' },
        { text: 'Second Name', dataIndex: 'secondName' },
        { text: 'Last Name',   dataIndex: 'lastName' },
        { text: 'PESEL',       dataIndex: 'pesel', flex: 1 }
    ],
    tbar: [{
            text: 'Add',
            tooltip: 'Add a new patient',
            iconCls: 'x-fa fa-plus',
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
            iconCls: 'x-fa fa-trash-o',
            disabled: true,
            handler: 'onRemove'
        },
    '->',
    {
	emptyText: "Filter",
	xtype: 'textfield',
	width: 250,
	plugins: [ {
            ptype: 'clearable'
	} ],
	listeners: {
            change: {
		fn: 'onFilter',
		buffer: 500
            }
	}
    }],
    dockedItems: [
        { xtype: 'pagingtoolbar', store: { type: 'patient' } , dock: 'bottom', displayInfo: true }
    ]
});
