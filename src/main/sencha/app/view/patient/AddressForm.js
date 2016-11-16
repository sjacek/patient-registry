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

Ext.define('Patients.view.patient.AddressForm', {
    extend: 'Ext.form.Panel',
    xtype: 'address',
    alias: 'form.address',
    cls: 'shadow',
    bodyPadding: 20,
    modelValidation: true,
    fieldDefaults: {
        labelAlign: 'right',
        align: 'stretch'
    },
    config: {
        addressLocal: null
    },
    items: [{
            xtype: 'form',
            layout: 'vbox',
            items: [{
                    xtype: 'textfield',
                    name: 'street',
                    fieldLabel: i18n.address_street,
                    allowBlank: false,
                    bind: '{this.getAddressLocal().street}'
                }, {
                    layout: 'hbox',
                    items: [{
                            xtype: 'textfield',
                            name: 'house',
                            fieldLabel: i18n.address_house_no,
                            allowBlank: false,
                            bind: '{address.house}'
                        }, {
                            xtype: 'textfield',
                            name: 'flat',
                            fieldLabel: i18n.address_flat_no,
                            allowBlank: true,
                            bind: '{address.flat}'
                        }]
                }, {
                    layout: 'hbox',
                    items: [{
                            xtype: 'textfield',
                            name: 'zipcode',
                            fieldLabel: i18n.address_zipcode,
                            allowBlank: false,
                            bind: '{this.addressLocal.zipCode}'
                        }, {
                            xtype: 'textfield',
                            name: 'city',
                            fieldLabel: i18n.address_city,
                            allowBlank: false,
                            bind: '{addressLocal.city}'
                        }]
                }, {
                    xtype: 'textfield',
                    name: 'county',
                    fieldLabel: i18n.address_county,
                    allowBlank: false,
                    bind: '{address.county}'
                }, {
                    xtype: 'textfield',
                    name: 'voivodship',
                    fieldLabel: i18n.address_voivodship,
                    allowBlank: false,
                    bind: '{address.voivodship}'
                }, {
                    xtype: 'textfield',
                    name: 'country',
                    fieldLabel: i18n.address_country,
                    allowBlank: false,
                    bind: '{address.country}'
                }]
        }]
    ,
    setAddressLocal: function (address) {
        this.config.addressLocal = address;
    }
});
