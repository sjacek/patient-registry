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

Ext.define('Patients.view.patient.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'patientlist',
    requires: ['Patients.plugin.Clearable'],
    stateful: true,
    stateId: 'view.patient.Grid',
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
        title: i18n.patient_patients,
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
                tooltip: i18n.patient_create_tooltip,
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
            stateId: 'view.patient.Grid.id',
            hidden: true
        }, {
            text: i18n.patient_fullname,
            xtype: 'templatecolumn',
            tpl: '{firstName} {lastName}',
            flex: 1,
            stateId: 'view.patient.Grid.fullName'
        }, {
            text: i18n.patient_pesel,
            dataIndex: 'pesel',
            flex: 1,
            stateId: 'view.patient.Grid.pesel'
        }, {
            xtype: 'datecolumn',
            text: i18n.patient_birthday,
            dataIndex: 'birthday',
            flex: 1,
            format: 'Y-m-d',
//            format: Ext.Date.patterns.ISO8601Short,
            stateId: 'view.patient.Grid.birthday',
            hidden: true
        }, {
            text: i18n.patient_address,
            dataIndex: 'address',
            flex: 1,
            renderer: function(value, meta, record) {
                var address = record._address.data;
                switch (address.country) {
                case "PL":
                    var ret = address.zipCode + " ";
                    if (address.postOffice !== undefined && address.postOffice !== "")
                        ret += address.postOffice + ",";
                    if (address.city !== address.postOffice && address.city !== "")
                        ret += address.city + ",";                        
                    if (address.street !== undefined && address.street !== "")
                        ret += address.street;
                    if (address.house !== undefined && address.house !== "") {
                        ret += address.house;
                        if (address.flat !== undefined && address.flat !== "")
                            ret += "/" + address.flat;
                    }
                    return ret;
                default:
                    return "ble";                
                }
            },
            stateId: 'view.patient.Grid.address'
        }, {
            text: i18n.patient_status,
            dataIndex: 'status',
            renderer: function (value) {
                if (value === undefined || value === null)
                    return 'undefined';
                var store = Ext.create('Patients.store.PatientStatus');
                var record = store.findRecord('value', value);
                if (record === undefined || record === null)
                    return 'not found';
                return record.get('text');
            },
            flex: 1,
            stateId: 'view.patient.Grid.status'
        }, {
            text: i18n.version,
            dataIndex: 'version',
            flex: 1,
            stateId: 'view.patient.Grid.version',
            hidden: true
        }, {
            xtype: 'actioncolumn',
            width: 50,
            items: [{
                    iconCls: 'x-fa fa-edit',
                    tooltip: i18n.patient_edit_tooltip,
                    handler: 'onEdit'
                }, {
                    iconCls: 'x-fa fa-times',
                    tooltip: i18n.patient_delete_tooltip,
                    handler: 'onDelete'
                }]
        }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
