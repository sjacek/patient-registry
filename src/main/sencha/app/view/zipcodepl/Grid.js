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

Ext.define('Patients.view.zipcodepl.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'zipcodepllist',
    reference: 'gridPanel',
    requires: ['Patients.plugin.Clearable', 'Ext.grid.plugin.RowEditing'],
    stateful: true,
    stateId: 'view.zipcodepl.Grid',
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
        title: i18n.zipcodepl_dictionary,
        defaults: {
            xtype: 'button',
            margin: '0 0 0 5'
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
                tooltip: i18n.zipcodepl_create_tooltip,
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
            stateId: 'view.zipcodepl.Grid.id',
            hidden: true
        }, {
            text: i18n.zipcodepl_zipCode,
            dataIndex: 'zipCode',
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
            stateId: 'view.zipcodepl.Grid.zipCode'
        }, {
            text: i18n.zipcodepl_postOffice,
            dataIndex: 'postOffice',
            editor: {
                completeOnEnter: false,
                // If the editor config contains a field property, then
                // the editor config is used to create the <a href='Ext.grid.CellEditor.html'>Ext.grid.CellEditor</a>
                // and the field property is used to create the editing input field.
                field: {
                    xtype: 'textfield'
                }
            },
            flex: 1,
            stateId: 'view.zipcodepl.Grid.postOffice'
        }, {
            text: i18n.zipcodepl_city,
            dataIndex: 'city',
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
            stateId: 'view.zipcodepl.Grid.city'
        }, {
            text: i18n.zipcodepl_voivodship,
            dataIndex: 'voivodship',
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
            stateId: 'view.zipcodepl.Grid.voivodship'
        }, {
            text: i18n.zipcodepl_street,
            dataIndex: 'street',
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
            stateId: 'view.zipcodepl.Grid.street'
        }, {
            text: i18n.zipcodepl_county,
            dataIndex: 'county',
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
            stateId: 'view.zipcodepl.Grid.county'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.zipcodepl.Grid.version',
            hidden: true
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.zipcodepl_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.zipcodepl_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
