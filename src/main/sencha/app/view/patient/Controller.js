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
//    onObjectStoreLoad: function (store) {
//        this.superclass.onObjectStoreLoad.call(this, store);
//        this.getViewModel().set('address', this.getSelectedObject().get('address'));
//        this.getViewModel().set('correspondenceAddress', this.getSelectedObject().get('correspondenceAddress'));
//    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();

        Ext.defer(function () {
            logService.debug('edit 5');
            formPanel.isValid();
            logService.debug('edit 6');
        }, 1);
        logService.debug('edit 7');
    },
//    save: function (callback) {
//        this.superclass.save.call(this, callback);
//    },
    onCancelClick: function () {
        this.getView().destroy();
    }

});