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

Ext.define('Patients.view.patient.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'patientlist',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.patient.Grid',
    height: 100,
    beforeLayout: function () {
        this.height = Ext.Element.getViewportHeight() - 60;
        this.callParent(arguments);
    },
    autoLoad: true,
    bind: {
        store: '{objects}',
        selection: '{selectedObject}'
    },
    listeners: {
        itemclick: 'onItemclick',
        afterRender: 'onBaseAfterRender'
    },
    cls: 'shadow',
    viewConfig: {
        preserveScrollOnRefresh: true
    },
    columns: [{
            text: i18n.id,
            dataIndex: 'id',
            flex: 0,
            stateId: 'view.patient.Grid.id'
        }, {
            text: i18n.patient_firstname,
            dataIndex: 'firstName',
            flex: 1,
            stateId: 'view.patient.Grid.firstName'
        }, {
            text: i18n.patient_lastname,
            dataIndex: 'lastName',
            flex: 1,
            stateId: 'view.patient.Grid.lastName'
        }, {
            text: i18n.patient_pesel,
            dataIndex: 'pesel',
            flex: 1,
            stateId: 'view.patient.Grid.pesel'
        }],
    tbar: [{
            text: i18n.create,
            tooltip: i18n.patient_create_tooltip,
            iconCls: 'x-fa fa-plus',
            handler: 'newObject'
        }, {
            text: 'Edit',
            reference: 'editPatientButton',
            tooltip: i18n.patient_edit_tooltip,
            disabled: true,
            handler: 'onEdit'
        }, {
            text: i18n.destroy,
            reference: 'removePatientButton',
            tooltip: i18n.patient_destroy_tooltip,
            iconCls: 'x-fa fa-trash-o',
            disabled: true,
            handler: 'onRemove'
        },
        '->',
        {
            emptyText: i18n.filter,
            xtype: 'textfield',
            width: 250,
            plugins: [{
                    ptype: 'clearable'
                }],
            listeners: {
                change: {
                    fn: 'onFilter',
                    buffer: 500
                }
            }
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', store: {type: 'patient'}, dock: 'bottom', displayInfo: true}
    ]
});
