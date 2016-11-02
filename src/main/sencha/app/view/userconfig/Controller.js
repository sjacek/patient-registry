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
/* global Ext, i18n, userConfigService */

Ext.define('Patients.view.userconfig.Controller', {
    extend: 'Ext.app.ViewController',
    initViewModel: function () {
        var userSettings = new Patients.model.UserSettings();
        userSettings.load({
            scope: this,
            success: function (record, operation) {
                this.getViewModel().set('user', userSettings);
            }
        });
    },
    save: function () {
        this.getView().mask(i18n.saving);

        var userSettings = this.getViewModel().get('user');

        // FIX: Somehow the field value of newPassword is not bound to the model
        var form = this.getView().down('form').getForm();
        userSettings.set('newPassword', form.findField('newPassword').getValue());
        // FIX: END

        userSettings.save({
            scope: this,
            success: function (record, operation) {
                Patients.Util.successToast(i18n.savesuccessful);
            },
            failure: function (record, operation) {
                Patients.Util.errorToast(i18n.inputcontainserrors);
                var form = this.lookup('userConfigForm').getForm();
                var validations = operation.getResponse().result.validations;
                Patients.Util.markInvalidFields(form, validations);
            },
            callback: function (record, operation, success) {
                this.getView().unmask();
            }
        });

    },
    clearState: function () {
        localStorage.removeItem("patients_navigation_micro");

        var store = Ext.state.Manager.getProvider().store;
        store.clear();
    },
    destroyPersistentLogin: function (view, rowIndex, colIndex, item, e, record, row) {
        record.erase({
            callback: function () {
                Patients.Util.successToast(i18n.destroysuccessful);
            }
        });
    },
    enable2f: function () {
        var vm = this.getViewModel();
        userConfigService.enable2f(function (secret) {
            Patients.Util.successToast(i18n.userconfig_2fa_enabled);
            vm.set('secret', secret);
            vm.set('user.twoFactorAuth', true);
            this.lookup('twoFactorPanel').add({
                xtype: 'image',
                src: 'qr',
                width: 200,
                height: 200,
                alt: secret
            });
        }, this);
    },
    disable2f: function () {
        var vm = this.getViewModel();
        userConfigService.disable2f(function () {
            Patients.Util.successToast(i18n.userconfig_2fa_disabled);
            vm.set('user.twoFactorAuth', false);
            vm.set('secret', null);
            var qrimage = this.lookup('twoFactorPanel').down('image');
            if (qrimage) {
                qrimage.destroy();
            }
        }, this);
    }

});