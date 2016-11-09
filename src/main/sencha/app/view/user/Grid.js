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
//            text: i18n.user_fullname,
//            dataIndex: '{fullName}',
//            flex: 1,
//            stateId: 'view.user.Grid.fullName'
//        }, {
            text: i18n.user_lastname,
            dataIndex: 'lastName',
            flex: 1,
            stateId: 'view.user.Grid.lastName'
        }, {
            text: i18n.user_firstname,
            dataIndex: 'firstName',
            flex: 1,
            stateId: 'view.user.Grid.firstName'
        }, {
            xtype: 'templatecolumn',
            tpl: '<tpl for="authorities"><span class="label label-info">{.}</span>&nbsp;</tpl>',
            text: i18n.user_authorities,
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
        }],
    dockedItems: [{
            xtype: 'toolbar',
            dock: 'top',
            items: [{
                    text: i18n.create,
                    iconCls: 'x-fa fa-plus',
                    handler: 'newObject'
                }, {
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
                }]
        }, {
            xtype: 'toolbar',
            dock: 'bottom',
            padding: 0,
            items: [{
                    iconCls: 'x-fa fa-refresh',
                    handler: 'onGridRefresh',
                    cls: 'no-bg-button',
                    tooltip: i18n.refresh
                }, {
                    xtype: 'tbtext',
                    bind: {
                        text: '{totalCount}'
                    }
                }]
        }]

});