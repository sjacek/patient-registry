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

Ext.define('Patients.ux.address.FieldSet', {
    extend: 'Ext.form.FieldSet',
    xtype: 'address.form',
    alias: 'widget.address',
    cls: 'shadow',
    modelValidation: true,

//    config: {
//            correspondence: null
//    },
    viewModel: {
        data: {
            correspondence: null
        }
//        formulas: {
//            address: {
//                get: function (data) {
//                    return (data.correspondence ? data.selectedObject.correspondence_address : data.selectedObject.address);
//                }
//            }
//        }
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
//    bind: {
//            collapsed: '{correspondenceAddressAndDisabled}'
//    },
    margin: 0,
    items: [{
            xtype: 'combo',
            name: 'country',
            fieldLabel: i18n.address_country,
            store: {type: 'countryDictionary'},
            allowBlank: false,
            bind: {
                value: '{theAddress.country}'
            },
            valueField: 'code',
            displayField: 'country',
            forceSelection: true,
            editable: false,
            listeners: {
                change: function (combo, newValue, oldValue, eOpts) {
                    var parent = combo.up(),
                            correspondence = parent.viewModel.get('correspondence');
                    logService.debug(parent.xtype);
//                    logService.debug(this.parentForm);
                    logService.debug("oldValue:" + oldValue + "; newValue:" + newValue);
                    if (newValue !== oldValue) {
                        var oldAddressPanel = parent.getComponent('address');
                        if (oldAddressPanel !== undefined) {
                            logService.debug("oldAddressPanel: " + oldAddressPanel.xtype);
                            parent.remove('address');
                        }
                        if (newValue === "PL" && !correspondence) {
                            logService.debug("PL && !correspondence");
                            parent.add({
                                itemId: 'address',
                                xtype: 'address_pl',
                                bind: {address: '{selectedObject.address}'}
                            });
                        } else if (newValue === "PL" && correspondence) {
                            logService.debug("PL && correspondence");
                            parent.add({
                                itemId: 'address',
                                xtype: 'address_pl',
                                bind: {address: '{selectedObject.correspondenceAddress}'}
                            });
                        } else if (!correspondence) {
                            logService.debug("!correspondence");
                            parent.add({
                                itemId: 'address',
                                xtype: 'address_general',
                                bind: {address: '{selectedObject.address}'}
                            });
                        } else {
                            logService.debug("correspondence");
                            parent.add({
                                itemId: 'address',
                                xtype: 'address_general',
                                bind: {address: '{selectedObject.correspondenceAddress}'}
                            });
                        }
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
//    parentFormValid: function () {
//        var viewModel = this.viewModel;
//        if (viewModel === undefined) {
//            return;
//        }
//        var parentForm = this.viewModel.get('parentForm');
//        if (parentForm === undefined || parentForm === null) {
//            return;
//        }
//
//        Ext.defer(function () {
//            parentForm.isValid();
//        }, 1);
//    },
    setCorrespondence: function (value) {
        logService.debug("correspondence:" + value);
        this.viewModel.set('correspondence', value);
    }

});
