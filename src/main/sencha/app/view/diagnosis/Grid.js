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

Ext.define('Patients.view.diagnosis.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'diagnosislist',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.diagnosis.Grid',
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
            stateId: 'view.diagnosis.Grid.id',
            hidden: true
        }, {
            text: i18n.diagnosis_name,
            dataIndex: 'diagnosisName',
            flex: 1,
            stateId: 'view.diagnosis.Grid.diagnosisName'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.diagnosis.Grid.version'
        }, {
            xtype: 'actioncolumn',
            width: 30,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.diagnosis_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.patient_delete_diagnosis,
                    handler: 'onDelete'
                }]
        }],
    tbar: [{
            text: i18n.create,
            tooltip: i18n.diagnosis_create_tooltip,
            iconCls: 'x-fa fa-plus',
            ui: 'soft-green',
            handler: 'newObject'
        }, {
            text: 'Edit',
            reference: 'editDiagnosisButton',
            tooltip: i18n.diagnosis_edit_tooltip,
            iconCls: 'x-fa fa-edit',
            handler: 'onEdit',
            ui: 'soft-green',
            bind: {
                disabled: '{!selectedObject}'
            }
        }, {
            text: i18n.destroy,
            reference: 'removeDiagnosisButton',
            tooltip: i18n.diagnosis_destroy_tooltip,
            iconCls: 'x-fa fa-trash-o',
            handler: 'erase',
            ui: 'soft-red',
            bind: {
                disabled: '{!selectedObject}'
            }
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
