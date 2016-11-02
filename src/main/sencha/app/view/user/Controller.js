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
/* global Ext, i18n, securityService, userService */

Ext.define('Patients.view.user.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        formClassName: 'Patients.view.user.Form',
        objectName: i18n.user,
        objectNamePlural: i18n.users
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('email'), null, function () {
            Patients.Util.errorToast(i18n.user_lastadmin_error);
        }, this);
    },
    switchTo: function () {
        var selectedObject = this.getSelectedObject();
        if (selectedObject) {
            securityService.switchUser(selectedObject.getId(), function (authUser) {
                if (authUser) {
                    Patients.app.authUser = authUser;
                    var currentLocation = window.location.toString();
                    var hashPos = currentLocation.indexOf("#");
                    if (hashPos > 0) {
                        currentLocation = currentLocation.substring(0, hashPos) + '#auth.signin';
                    } else {
                        currentLocation += '#auth.signin';
                    }
                    window.location.replace(currentLocation);
                    window.location.reload();
                }
            }, this);
        }
    },
    unlock: function () {
        var selectedObject = this.getSelectedObject();
        if (selectedObject) {
            userService.unlock(selectedObject.getId(), function () {
                Patients.Util.successToast(i18n.user_unlocked);
                selectedObject.set('lockedOutUntil', null, {
                    commit: true
                });
                this.back();
            }, this);
        }
    },
    disableTwoFactorAuth: function () {
        var selectedObject = this.getSelectedObject();
        if (selectedObject) {
            userService.disableTwoFactorAuth(selectedObject.getId(), function () {
                Patients.Util.successToast(i18n.user_unlocked);
                selectedObject.set('twoFactorAuth', false, {
                    commit: true
                });
                this.back();
            }, this);
        }
    },
    sendPwResetReq: function () {
        this.save(function () {
            var selectedObject = this.getSelectedObject();
            if (selectedObject) {
                userService.sendPassordResetEmail(selectedObject.getId(), function () {
                    Patients.Util.successToast(i18n.user_sent_pwresetreq);
                });
            }
        });
    }

});