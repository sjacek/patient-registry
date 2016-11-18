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

Ext.define('Patients.view.patient.Controller', {
    extend: 'Patients.view.base.ViewController',
    requires: ['Patients.model.Address'],
    config: {
        formClassName: 'Patients.view.patient.Form',
        objectName: i18n.patient,
        objectNamePlural: i18n.patients,
        correspondenceAddressEnabled: false
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('firstName') + " " + this.getSelectedObject().get('lastName'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
    },
    onObjectStoreLoad: function (store) {
        this.superclass.onObjectStoreLoad.call(this, store);
    },
    createSubobjects: function () {
        var selectedObject = this.getSelectedObject();
        if (!selectedObject.address) {
            selectedObject.address = Ext.create('Patients.model.Address');
        }
        this.correspondenceAddressEnabled = (selectedObject.correspondenceAddress !== undefined);
        if (!this.correspondenceAddressEnabled) {
            selectedObject.correspondenceAddress = Ext.create('Patients.model.Address');
        }
        logService.debug('createSubobjects ' + selectedObject.contacts());
    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();

        var selectedObject = this.getSelectedObject(),
                store = selectedObject.contacts();
        logService.debug('edit 3 ' + store.getCount());

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    },
    save: function (callback) {
        var selectedObject = this.getSelectedObject(),
                store = selectedObject.contacts();
        logService.debug('save ' + store.getCount());
////        logService.debug('save ' + this.getSelectedObject().contacts[0]);
////        logService.debug('save ' + this.getSelectedObject().contacts[1]);
////        logService.debug('save ' + this.getSelectedObject().contacts[2]);
//        
        this.superclass.save.call(this, callback);
    },
    onCancelClick: function () {
        this.getView().destroy();
    },
    onContactsNewClick: function () {
        var store = this.getSelectedObject().contacts();
        logService.debug('save ' + store.getCount());
        store.insert(0, {method: 'xxx', contact: 'zzz'})[0];
        logService.debug('save ' + store.getCount());
    },
    onContactDeleteClick: function (view, row, col, action, ev, record) {
        var store = record.store;
        store.remove(record);
    }

});