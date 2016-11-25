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

Ext.define('Patients.view.addressdictionary.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'addresslist',
    reference: 'gridPanel',
    requires: ['Patients.plugin.Clearable', 'Ext.grid.plugin.RowEditing'],
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
    header: {
        title: i18n.address_dictionary,
        defaults: {
            xtype: 'button',
            margin: '0 0 0 5'
        },
        items: [{
                text: i18n.create,
                tooltip: i18n.contact_create_tooltip,
                iconCls: 'x-fa fa-plus',
                ui: 'soft-green',
                handler: 'newObject'
            }]
    },
    listeners: {
        canceledit: 'back',
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
            stateId: 'view.addressdictionary.Grid.id',
            hidden: true
        }, {
            text: i18n.address_country,
            dataIndex: 'country',
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
            stateId: 'view.addressdictionary.Grid.method'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.addressdictionary.Grid.version',
            hidden: true
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.contact_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.contact_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
