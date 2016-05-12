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

/* global Ext */

Ext.define('Patients.view.user.UserList', {
    extend: 'Ext.grid.Panel',
    xtype: 'userlist',

    requires: [
        'Patients.model.UserViewModel',
        'Patients.store.User',
	'Patients.plugin.Clearable'
    ],
    controller: 'userlist',
    reference: 'userListGrid',

    title: 'User List',

    listeners: {
        selectionchange: 'onSelectionChange',
//        select: 'onItemSelected'
        rowdblclick: 'onRowDblClick'
    },
    viewModel: { type: 'user' },
    store: { type: 'user' },

    columns: [
//        { text: 'Id',          dataIndex: 'id' },
        { text: 'Login',       dataIndex: 'loginName' },
        { text: 'First Name',  dataIndex: 'firstName' },
        { text: 'Last Name',   dataIndex: 'lastName' },
        { text: 'e-mail',      dataIndex: 'email', flex: 1 },
	{
            text: 'Authorities',
            xtype: 'templatecolumn',
            tpl: '<tpl for="authorities"><span class="label label-info">{.}</span>&nbsp;</tpl>',
            flex: 1,
            dataIndex: 'authorities'
        }],
    tbar: [{
        text: 'Add',
        tooltip: 'Add a new user',
        iconCls: 'x-fa fa-plus',
        handler: 'onAdd'
    },{
        text: 'Edit',
        reference: 'editUserButton',
        tooltip: 'Edit selected user\'s data',
        disabled: true,
        handler: 'onEdit'
    },{
        text: 'Remove',
        reference: 'removeUserButton',
        tooltip: 'Remove selected user',
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
        { xtype: 'pagingtoolbar', store: { type: 'user' } , dock: 'bottom', displayInfo: true }
    ]
});
