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

Ext.define('Patients.view.diagnosis.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        formClassName: 'Patients.view.diagnosis.Form',
        objectName: i18n.diagnosis,
        objectNamePlural: i18n.diagnosises
    },
    onDelete: function (view, row, col, action, ev, record) {
        var store = record.store;
        store.remove(record);
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('diagnosisName'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
    },
    onObjectStoreLoad: function (store) {
        this.superclass.onObjectStoreLoad.call(this, store);
    },
    onEdit: function(view, row, col, action, ev, record) {
        this.getViewModel().set(this.getSelectedObjectName(), record);
        this.createSubobjects();
        this.edit();
    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    }

});