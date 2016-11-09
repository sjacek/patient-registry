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
        itemdblclick: 'onItemdblclick',
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
            stateId: 'view.patient.Grid.id',
            hidden: true
        }, {
            text: i18n.patient_fullname,
            xtype: 'templatecolumn',
            tpl: '{lastName} {firstName}',
            flex: 1,
            stateId: 'view.patient.Grid.fullName'
        }, {
            text: i18n.patient_pesel,
            dataIndex: 'pesel',
            flex: 1,
            stateId: 'view.patient.Grid.pesel'
        }, {
            xtype: 'datecolumn',
            text: i18n.patient_birthday,
            dataIndex: 'birthday',
            flex: 1,
            format: 'Y-m-d',
//            format: Ext.Date.patterns.ISO8601Short,
            stateId: 'view.patient.Grid.birthday',
            hidden: true
        }],
    tbar: [{
            text: i18n.create,
            tooltip: i18n.patient_create_tooltip,
            iconCls: 'x-fa fa-plus',
            ui: 'soft-green',
            handler: 'newObject'
        }, {
            text: 'Edit',
            reference: 'editPatientButton',
            tooltip: i18n.patient_edit_tooltip,
            iconCls: 'x-fa fa-edit',
            handler: 'onEdit',
            ui: 'soft-green',
            bind: {
                disabled: '{!selectedObject}'
            }
        }, {
            text: i18n.destroy,
            reference: 'removePatientButton',
            tooltip: i18n.patient_destroy_tooltip,
            iconCls: 'x-fa fa-trash-o',
            handler: 'erase',
            ui: 'soft-red',
            bind: {
                disabled: '{!selectedObject}'
            }
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
//        {xtype: 'pagingtoolbar', store: {type: 'patient'}, dock: 'bottom', displayInfo: true}
        {xtype: 'pagingtoolbar', bind: { store: '{objects}' }, dock: 'bottom', displayInfo: true}
    ]
});
