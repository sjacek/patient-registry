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

Ext.define('Patients.view.project.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'projectlist',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.project.Grid',
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
    header: {
        title: i18n.project_projects,
        defaults: {
            xtype: 'button',
            margin: '0 0 0 2'
        },
        items: [{
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
            }, {
                text: i18n.create,
                tooltip: i18n.project_create_tooltip,
                iconCls: 'x-fa fa-plus',
                ui: 'soft-green',
                handler: 'newObject'
            }]
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
            stateId: 'view.project.Grid.id',
            hidden: true
        }, {
            text: i18n.project_name,
            dataIndex: 'name',
            flex: 1,
            stateId: 'view.project.Grid.Name'
        }, {
            text: i18n.project_pesel,
            dataIndex: 'pesel',
            flex: 1,
            stateId: 'view.project.Grid.pesel'
        }, {
            xtype: 'datecolumn',
            text: i18n.project_start_date,
            dataIndex: 'start',
            flex: 1,
            format: 'Y-m-d',
//            format: Ext.Date.patterns.ISO8601Short,
            stateId: 'view.project.Grid.start'
        }, {
            xtype: 'datecolumn',
            text: i18n.project_end_date,
            dataIndex: 'end',
            flex: 1,
            format: 'Y-m-d',
//            format: Ext.Date.patterns.ISO8601Short,
            stateId: 'view.project.Grid.end'
        }, {
            text: i18n.project_status,
            dataIndex: 'status',
            renderer: function (value) {
                if (value === undefined || value === null)
                    return 'undefined';
                var store = Ext.create('Patients.store.ProjectStatus');
                var record = store.findRecord('value', value);
                if (record === undefined || record === null)
                    return 'not found';
                return record.get('text');
            },
            flex: 1,
            stateId: 'view.project.Grid.status'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.project.Grid.version',
            hidden: true
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.project_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.project_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
