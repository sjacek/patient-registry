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

Ext.define('Patients.view.contactmethod.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'contactmethodlist',
    reference: 'gridPanel',
    requires: ['Patients.plugin.Clearable', 'Ext.grid.plugin.RowEditing' ],
    stateful: true,
    stateId: 'view.contactmethod.Grid',
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
        canceledit:'back',
        afterRender: 'onBaseAfterRender'
    },
    cls: 'shadow',
    plugins: {
        ptype: 'rowediting',
        clicksToEdit: 2
    },
    viewConfig: {
        preserveScrollOnRefresh: true
    },
    selModel: 'rowmodel',
    columns: [{
            text: i18n.id,
            dataIndex: 'id',
            flex: 0,
            stateId: 'view.contactmethod.Grid.id',
            hidden: true
        }, {
            text: i18n.contact_method,
            dataIndex: 'method',
            editor: {
                completeOnEnter: false,

                // If the editor config contains a field property, then
                // the editor config is used to create the <a href='Ext.grid.CellEditor.html'>Ext.grid.CellEditor</a>
                // and the field property is used to create the editing input field.
                field: {
                    xtype: 'textfield',
                    allowBlank: false
                }
            },
            flex: 1,
            stateId: 'view.contactmethod.Grid.method'
        }, {
            text: i18n.contact_description,
            dataIndex: 'description',
            editor: 'textareafield',
            flex: 1,
            stateId: 'view.contactmethod.Grid.description'
        }],
    tbar: [{
            text: i18n.create,
            tooltip: i18n.contact_create_tooltip,
            iconCls: 'x-fa fa-plus',
            ui: 'soft-green',
            handler: 'onEdit'
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
