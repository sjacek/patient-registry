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

Ext.define('Patients.ux.AddressFieldSet', {
    extend: 'Ext.form.FieldSet',
    xtype: 'address',
    alias: 'widget.address',
    cls: 'shadow',
    modelValidation: true,
    viewModel: {
        data: {
            theAddress: null
        }
    },
    fieldDefaults: {
        labelAlign: 'right',
        align: 'stretch'
    },
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    defaults: {
        margin: 1,
        flex: 1
    },
    margin: 0,
    items: [{
            xtype: 'combo',
            name: 'country',
            fieldLabel: i18n.address_country,
            store: { type: 'addressDictionary'},
            allowBlank: false,
            bind: {
                value: '{theAddress.country}'
            },
            valueField: 'country',
            displayField: 'country',
            forceSelection: true,
            editable: false
        }, {
            defaults: {
                margin: 1,
                flex: 1
            },
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                    xtype: 'textfield',
                    name: 'zipcode',
                    fieldLabel: i18n.address_zipcode,
                    allowBlank: false,
                    bind: '{theAddress.zipCode}'
                }, {
                    xtype: 'textfield',
                    name: 'city',
                    fieldLabel: i18n.address_city,
                    allowBlank: false,
                    bind: '{theAddress.city}'
                }]
        }, {
            xtype: 'textfield',
            name: 'street',
            fieldLabel: i18n.address_street,
            allowBlank: false,
            bind: '{theAddress.street}'
        }, {
            defaults: {
                margin: 1,
                flex: 1
            },
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                    xtype: 'textfield',
                    name: 'house',
                    fieldLabel: i18n.address_house_no,
                    allowBlank: false,
                    bind: '{theAddress.house}'
                }, {
                    xtype: 'textfield',
                    name: 'flat',
                    fieldLabel: i18n.address_flat_no,
                    allowBlank: true,
                    bind: '{theAddress.flat}'
                }]
        }, {
            xtype: 'textfield',
            name: 'voivodship',
            fieldLabel: i18n.address_postoffice,
            bind: '{theAddress.postoffice}'
        }, {
            xtype: 'textfield',
            name: 'voivodship',
            fieldLabel: i18n.address_voivodship,
            allowBlank: false,
            bind: '{theAddress.voivodship}'
        }, {
            xtype: 'textfield',
            name: 'county',
            fieldLabel: i18n.address_county,
            allowBlank: false,
            bind: '{theAddress.county}'
        }],
    setAddress: function (address) {
        this.viewModel.set('theAddress', address);
    }
});
