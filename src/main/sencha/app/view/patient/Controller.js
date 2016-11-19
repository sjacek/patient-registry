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
        objectNamePlural: i18n.patients
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
        if (!selectedObject.getAddress()) {
            selectedObject.setAddress(Ext.create('Patients.model.Address'));
        }

        var correspondenceAddress = selectedObject.getCorrespondenceAddress(),
                correspondenceAddressEnabled = (correspondenceAddress !== null && correspondenceAddress.phantom === false);
        if (!correspondenceAddress) {
            selectedObject.setCorrespondenceAddress(Ext.create('Patients.model.Address'));
        }
        this.getViewModel().set('correspondenceAddressEnabled', correspondenceAddressEnabled);

        var certificateOfDisabilityEnabled = (selectedObject.get('disabilityLevel') !== 'NO_CERTIFICATE');
        this.getViewModel().set('certificateOfDisabilityEnabled', certificateOfDisabilityEnabled);

        var expirationData = selectedObject.get('certificateOfDisabilityExpiration');
        var enabled = expirationData !== undefined && expirationData !== null;
        this.getViewModel().set('certificateOfDisabilityExpirationEnabled', this.getViewModel().get('certificateOfDisabilityEnabled') && enabled);
    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    },
    save: function (callback) {
        if (!this.getViewModel().get('correspondenceAddressEnabled')) {
            this.getSelectedObject().getCorrespondenceAddress().destroy();
            delete this.getSelectedObject().getCorrespondenceAddress();
        }
        if (!this.getViewModel().get('certificateOfDisabilityExpirationEnabled')) {
            delete this.getSelectedObject().get('certificateOfDisabilityExpiration');
            this.getSelectedObject().set('certificateOfDisabilityExpiration', null);
        }

        this.superclass.save.call(this, callback);
    },
    onCancelClick: function () {
        this.getView().destroy();
    },
    onContactsNewClick: function () {
        var store = this.getSelectedObject().contacts();
        store.insert(0, {method: '', contact: ''})[0];
    },
    onContactDeleteClick: function (view, row, col, action, ev, record) {
        var store = record.store;
        store.remove(record);
    },
    onDisabilityLevelSelect: function (combo, value, opts) {
        var certificateOfDisabilityEnabled = value.data.value.localeCompare('NO_CERTIFICATE') !== 0;
        this.getViewModel().set('certificateOfDisabilityEnabled', certificateOfDisabilityEnabled);
        this.getViewModel().set('certificateOfDisabilityExpirationEnabled', certificateOfDisabilityEnabled && this.getViewModel().get('certificateOfDisabilityExpirationEnabled'));
        this.lookup('editPanel').isValid();
    },
    onExpirationDateChange: function(newValue) {
        this.getViewModel().set('certificateOfDisabilityExpirationEnabled', this.getViewModel().get('certificateOfDisabilityEnabled') && newValue.value);
        this.lookup('editPanel').isValid();
    }

});