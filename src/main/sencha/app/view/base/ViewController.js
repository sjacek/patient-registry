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
/* global Ext, i18n, logService */

Ext.define('Patients.view.base.ViewController', {
    extend: 'Ext.app.ViewController',
    requires: 'Patients.view.base.GridCellToolTip',
    config: {
        objectStoreName: 'objects',
        selectedObjectName: 'selectedObject',
        formClassName: null,
        objectName: 'Object',
        objectNamePlural: 'Objects'
    },
    onBaseAfterRender: function (cmp) {
        cmp.tip = Ext.create('Patients.view.base.GridCellToolTip', {
            target: cmp.getView().getEl()
        });
    },
    onObjectStoreLoad: function (store) {
        var total = store.getTotalCount();
        this.getViewModel().set('totalCount', Ext.util.Format.plural(total, this.getObjectName(), this.getObjectNamePlural()));
    },
    getSelectedObject: function () {
        return this.getViewModel().get(this.getSelectedObjectName());
    },
    onGridRefresh: function () {
        this.getViewModel().set(this.getSelectedObjectName(), null);
        this.getStore(this.getObjectStoreName()).reload();
    },
    newObject: function () {
        var model = this.getStore(this.getObjectStoreName()).getModel();
        this.getViewModel().set(this.getSelectedObjectName(), model.create());
        this.edit();
    },
    onFilter: function (tf) {
        var value = tf.getValue();
        var store = this.getStore(this.getObjectStoreName());
        if (value) {
            this.getViewModel().set('filter', value);
            store.filter('filter', value);
        } else {
            this.getViewModel().set('filter', null);
            store.removeFilter('filter');
        }
    },
    onItemdblclick: function (store, record) {
        this.getViewModel().set(this.getSelectedObjectName(), record);
        this.edit();
    },
    onEdit: function() {
        this.getViewModel().set(this.getSelectedObjectName(), this.getSelectedObject());
        this.edit();
    },
    edit: function () {
        this.getView().add({
            xclass: this.getFormClassName()
        });

        var formPanel = this.getView().getLayout().next();

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    },
    back: function () {
        var selectedObject = this.getSelectedObject();
        if (selectedObject) {
            selectedObject.reject();
        }

        this.getView().getLayout().prev();
        this.getView().getLayout().getNext().destroy();
    },
    save: function (callback) {
        var form = this.lookup('editPanel').getForm();
        if (form.isValid()) {
            this.getView().mask(i18n.saving);

            var selectedObject = this.getSelectedObject();
            this.preSave(selectedObject);
            selectedObject.save({
                scope: this,
                success: function (record, operation) {
                    this.afterSuccessfulSave();
                    Patients.Util.successToast(i18n.savesuccessful);
                    this.back();
                    if (Ext.isFunction(callback)) {
                        callback.call(this);
                    }
                },
                failure: function (record, operation) {
                    Patients.Util.errorToast(i18n.inputcontainserrors);
                    var validations = operation.getResponse().result.validations;
                    Patients.Util.markInvalidFields(form, validations);
                },
                callback: function (record, operation, success) {
                    this.getView().unmask();
                }
            });

        }
    },
    preSave: Ext.emptyFn,
    afterSuccessfulSave: function () {
        this.getStore(this.getObjectStoreName()).reload();
    },
    eraseObject: function (errormsg, successCallback, failureCallback, scope) {
        var selectedObject = this.getSelectedObject();
        if (!selectedObject) {
            return;
        }

        Ext.Msg.confirm(i18n.attention, Ext.String.format(i18n.destroyConfirmMsg, errormsg), function (choice) {
            if (choice === 'yes') {
                selectedObject.erase({
                    success: function (record, operation) {
                        if (successCallback) {
                            successCallback.call(scope);
                        } else {
                            this.onGridRefresh();
                            Patients.Util.successToast(i18n.destroysuccessful);
                        }
                    },
                    failure: function (record, operation) {
                        if (failureCallback) {
                            failureCallback.call(scope);
                        } else {
                            Patients.Util.errorToast(i18n.servererror);
                        }
                    },
                    callback: function (records, operation, success) {
                        this.back();
                    },
                    scope: this
                });
            }
        }, this);
    }
});