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

Ext.define('Patients.view.contact.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'contactlist',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.contact.Grid',
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
            stateId: 'view.contact.Grid.id',
            hidden: true
        }, {
            text: i18n.contact_method,
            dataIndex: 'method',
            flex: 1,
            stateId: 'view.contact.Grid.method'
        }, {
            text: i18n.contact_description,
            dataIndex: 'description',
            flex: 1,
            stateId: 'view.contact.Grid.description'
        }],
    tbar: [{
            text: i18n.create,
            tooltip: i18n.contact_create_tooltip,
            iconCls: 'x-fa fa-plus',
            ui: 'soft-green',
            handler: 'newObject'
        }, {
            text: 'Edit',
            reference: 'editContactButton',
            tooltip: i18n.contact_edit_tooltip,
            iconCls: 'x-fa fa-edit',
            handler: 'onEdit',
            ui: 'soft-green',
            bind: {
                disabled: '{!selectedObject}'
            }
        }, {
            text: i18n.destroy,
            reference: 'removeContactButton',
            tooltip: i18n.contact_destroy_tooltip,
            iconCls: 'x-fa fa-trash-o',
            handler: 'erase',
            ui: 'soft-red',
            bind: {
                disabled: '{!selectedObject}'
            }
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: { store: '{objects}' }, dock: 'bottom', displayInfo: true}
    ]
});
