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

Ext.define('Patients.view.organization.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'organizationlist',
    stateful: true,
    stateId: 'view.organization.Grid',
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
        title: i18n.organization_organizations,
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
                tooltip: i18n.organization_create_tooltip,
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
            stateId: 'view.organization.Grid.id',
            hidden: true
        }, {
            text: i18n.organization_name,
            dataIndex: 'name',
            flex: 1,
            stateId: 'view.organization.Grid.Name'
        }, {
            text: i18n.organization_code,
            dataIndex: 'code',
            flex: 1,
            stateId: 'view.organization.Grid.code'
        }, {
            text: i18n.organization_parent_id,
            dataIndex: 'parentId',
            flex: 1,
            stateId: 'view.organization.Grid.parentId',
            hidden: true
        }, {
            text: i18n.organization_parent,
            dataIndex: 'parent',            
            renderer: function(value, meta, record) {
                if (record._parent === undefined)
                    return "";
                return record._parent.data.name;
            },
            flex: 1,
            stateId: 'view.organization.Grid.parent'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.organization.Grid.version',
            hidden: true
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.organization_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.organization_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
