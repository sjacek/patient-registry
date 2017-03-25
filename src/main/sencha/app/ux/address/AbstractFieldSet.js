/* 
 * Copyright (C) 2017 Jacek Sztajnke
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
/* global Ext, i18n, logService */

Ext.define('Patients.ux.address.AbstractFieldSet', {
    extend: 'Ext.form.FieldSet',
    xtype: 'abstractaddress.form',
    alias: 'widget.abstractaddress',
    cls: 'shadow',
    modelValidation: true,
    config: {
        correspondence: null
    },
    initComponent: function () {
        this.callParent(arguments);
        logService.debug("************************ initComponent " + this.xtype);
        logService.debug("config.correspondence:" + this.getCorrespondence());
//        if (this.getAddress() !== null && this.getAddress() !== undefined)
//            logService.debug("country:" + this.getAddress().street);
        logService.debug(Object.keys(this.getConfig()));
        logService.debug(this.items.length);
        var combo = this.query('#country')[0];
//        if (this.getCorrespondence()) {
//            var stuff = {
//                bind: {
//                    value: '{selectedObject.correspondenceAddress.country}'
//                }
//            };
//            Ext.apply(combo, stuff);
//        } else {
//            var stuff = {
//                bind: {
//                    value: '{selectedObject.address.country}'
//                }
//            };
//            Ext.apply(combo, stuff);
//        }
        logService.debug('viewModel: ' + Object.keys(this.viewModel));
//        var country = this.getSelectedObject();
//        logService.debug('country: ' + country);
//                        this.addAdressDetails(newValue, this.getCorrespondence());
    },
    addAdressDetails: function (newValue, correspondence) {
        var address = this.getAddress();
        logService.debug("addAdressDetails address:" + address);
        if (newValue === "PL" && !correspondence) {
            logService.debug("PL && !correspondence");
            this.add({
                itemId: 'address',
                xtype: 'address_pl',
                bind: {address: '{address}'}
            });
        } else if (newValue === "PL" && correspondence) {
            logService.debug("PL && correspondence");
            this.add({
                itemId: 'address',
                xtype: 'address_pl',
                bind: {address: '{address}'}
            });
        } else if (!correspondence) {
            logService.debug("!correspondence");
            this.add({
                itemId: 'address',
                xtype: 'address_general',
                bind: {address: '{address}'}
            });
        } else {
            logService.debug("correspondence");
            this.add({
                itemId: 'address',
                xtype: 'address_general',
                bind: {address: '{address}'}
            });
        }
        logService.debug("addAdressDetails end");
    },
    viewModel: {
        data: {
            address: null
//            correspondence: null
        }
//        formulas: {
//            address: {
//                get: function (data) {
//                    logService.debug("address get" + Object.keys(this));
//                    var selectedObject = this.viewModel.set('selectedObject');
//                    return (data.correspondence ? selectedObject.correspondenceAddress : selectedObject.address);
//                }
//            }
//        }
    },
//    function() {
//        return this.getCorrespondence();
//    },
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
//    bind: {
//            collapsed: '{correspondenceAddressAndDisabled}'
//    },
    margin: 0,
    items: [{
            xtype: 'combo',
            itemId: 'country',
            name: 'country',
            fieldLabel: i18n.address_country,
            store: {type: 'countryDictionary'},
            allowBlank: false,
            bind: {
                value: '{address.country}'
            },
            valueField: 'code',
            displayField: 'countryEn',
            forceSelection: true,
            editable: false,
            listeners: {
                render: function() {
                    logService.debug("combo render");
                },
                change: function (combo, newValue, oldValue, eOpts) {
                    var parent = combo.up(),
                            correspondence = parent.getCorrespondence();
                    logService.debug(parent.xtype);
//                    logService.debug(this.parentForm);
                    logService.debug("oldValue:" + oldValue + "; newValue:" + newValue);
                    if (newValue !== oldValue) {
                        var oldAddressPanel = parent.getComponent('address');
                        if (oldAddressPanel !== undefined) {
                            logService.debug("oldAddressPanel: " + oldAddressPanel.xtype);
                            parent.remove('address');
                        }
                        parent.addAdressDetails(newValue, correspondence);
                    }
                }
            }
        }],
    listeners: {
        collapse: function () {
            this.allowBlank(this.items, true, this.allowBlank);
//            this.parentFormValid();
        },
        expand: function () {
            this.allowBlank(this.items, false, this.allowBlank);
//            this.parentFormValid();
        }
    },
    allowBlank: function (items, on, func) {
        items.each(function (item) {
            if (item.items !== undefined) {
                func(item.items, on, func);
            }
            if (item.allowBlank !== undefined) {
                if (item.name !== 'flat' && item.name !== 'postOffice') {
                    Ext.apply(item, {allowBlank: on}, {});
                    item.fireEvent('dirtychange'); // TODO: not working
                }
            }
        });
    },
    updateBind: function () {
        var combo = this.query('#country')[0];
        Ext.apply(combo, {
            bind: {
                value: '{address.country}'
            }
        });
//        if (this.getCorrespondence()) {
//            Ext.apply(combo, {
//                bind: {
//                    value: '{selectedObject.correspondenceAddress.country}'
//                }
//            });
//        } else {
//            Ext.apply(combo, {
//                bind: {
//                    value: '{selectedObject.address.country}'
//                }
//            });
//        }
    },
    setAddress: function (value) {
        logService.debug("setAddress address:" + Object.keys(value));
        logService.debug("setAddress country:" + Object.keys(value.data));
//        this.address = value;
//        logService.debug("config.correspondence:" + this.getCorrespondence());
        this.viewModel.set('address', value.data);
        logService.debug(this.viewModel.get('address').country);
//        updateBind();
        var combo = this.query('#country')[0];
        Ext.apply(combo, {
            bind: {
                value: '{address.country}'
            }
        });
    },
    getAddress: function () {
//        if (this.getCorrespondence())
//            return this.viewModel.get('selectedObject.correspondenceAddress');
//        else
//            return this.viewModel.get('selectedObject.address');
        return this.viewModel.get('address');
    }
});
