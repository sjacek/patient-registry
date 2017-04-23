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

Ext.define('Patients.view.user.Grid', {
    extend: 'Ext.grid.Panel',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.user.Grid',
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
        title: i18n.user_users,
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
                tooltip: i18n.user_create_tooltip,
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
            stateId: 'view.user.Grid.id',
            hidden: true
        }, {
            text: i18n.user_email,
            dataIndex: 'email',
            flex: 1,
            stateId: 'view.user.Grid.email'
        }, {
            text: i18n.user_fullname,
            xtype: 'templatecolumn',
            tpl: '{lastName} {firstName}',
            flex: 1,
            stateId: 'view.user.Grid.fullName'
        }, {
            text: i18n.user_authorities,
            xtype: 'templatecolumn',
            tpl: '<tpl for="authorities"><span class="label label-info">{.}</span>&nbsp;</tpl>',
            flex: 1,
            stateId: 'view.user.Grid.authorities'
        }, {
            xtype: 'datecolumn',
            format: 'Y-m-d H:i:s',
            text: i18n.user_lastaccess,
            dataIndex: 'lastAccess',
            width: 170,
            stateId: 'view.user.Grid.lastAccess'
        }, {
            text: i18n.user_enabled,
            dataIndex: 'enabled',
            width: 85,
            align: 'center',
            defaultRenderer: function (value) {
                if (value === true) {
                    return '<span class="label label-success">' + i18n.yes + '</span>';
                }
                return '<span class="label label-error">' + i18n.no + '</span>';
            },
            stateId: 'view.user.Grid.enabled'
        }, {
            text: i18n.user_locked,
            dataIndex: 'lockedOutUntil',
            width: 95,
            align: 'center',
            defaultRenderer: function (value) {
                if (value) {
                    return '<span class="label label-error">' + i18n.yes + '</span>';
                }
                return '<span class="label label-success">' + i18n.no + '</span>';
            },
            stateId: 'view.user.Grid.lockedOutUntil'
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.user_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.user_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});