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

Ext.define('Patients.view.auth.AuthController', {
    extend: 'Ext.app.ViewController',
    // <debug>
    onLoginAsAdminButtonClick: function () {
        var form = this.getView().getForm();
        form.setValues({
            username: 'admin@starter.com',
            password: 'admin'
        });
        this.onLoginButtonClick();
    },
    onLoginAsUserButtonClick: function () {
        var form = this.getView().getForm();
        form.setValues({
            username: 'user@starter.com',
            password: 'user'
        });
        this.onLoginButtonClick();
    },
    // </debug>

    onLoginButtonClick: function () {
        var me = this;
        var form = me.getView().getForm();

        Patients.Util.getCsrfToken().then(function () {
            form.submit({
                clientValidation: true,
                success: function (form, action) {
                    var authUser = action.result.authUser;

                    if (authUser) {
                        Ext.Ajax.setDefaultHeaders({
                            'X-CSRF-TOKEN': authUser.csrf
                        });

                        me.fireEvent('signedin', this, authUser);
                    }
                },
                failure: function (form, action) {
                    Patients.Util.errorToast(i18n.auth_signin_failed);
                }
            });
        });
    },
    onForgotPasswordAfterRender: function (link) {
        link.el.on('click', function () {
            var val = link.up('form').down('textfield[name=username]').getValue();
            if (val) {
                sessionStorage.patients_login = val;
            } else {
                sessionStorage.removeItem('patients_login');
            }
        });
    },
    onLoginResumeButtonClick: function () {
        var me = this;
        var form = this.getView().getForm();

        form.submit({
            clientValidation: true,
            success: function (form, action) {
                me.getView().up('window').destroy();
            },
            failure: function (form, action) {
                Patients.Util.errorToast(i18n.auth_locked_resume_failed);
            }
        });
    },
    onResetRequestClick: function () {
        var me = this;
        var form = me.getView().getForm();

        Patients.Util.getCsrfToken().then(function () {
            form.submit({
                clientValidation: true,
                success: function (form, action) {
                    me.redirectTo('auth.pwresetconfirm');
                }
            });
        });
    },
    onResetClick: function () {

        var token = Patients.app.pwResetToken;

        var me = this;
        var form = this.getView().getForm();

        Patients.Util.getCsrfToken().then(function () {
            form.submit({
                clientValidation: true,
                params: {
                    token: token
                },
                success: function (form, action) {
                    delete Patients.app.pwResetToken;
                    var authUser = action.result.authUser;

                    if (authUser) {
                        me.fireEvent('signedin', this, authUser);
                    }
                },
                failure: function (form, action) {
                    me.redirectTo('auth.signin', true);
                }
            });
        });
    },
    onPasswordRender: function (field) {
        field.capsWarningTooltip = Ext.create('Ext.tip.ToolTip', {
            target: field.bodyEl,
            anchor: 'top',
            width: 305,
            html: '<div style="font-weight: bold;">' + i18n.auth_capslockwarning_title + '</div><br>' + '<div>' + i18n.auth_capslockwarning_line1 + '</div><br>' + '<div>' + i18n.auth_capslockwarning_line2 + '</div>'
        });
        field.capsWarningTooltip.disable();
    },
    onPasswordKeypress: function (field, e) {
        var charCode = e.getCharCode();
        if ((e.shiftKey && charCode >= 97 && charCode <= 122) || (!e.shiftKey && charCode >= 65 && charCode <= 90)) {
            field.capsWarningTooltip.enable();
            field.capsWarningTooltip.show();
        } else {
            if (field.capsWarningTooltip.isHidden() === false) {
                field.capsWarningTooltip.disable();
                field.capsWarningTooltip.hide();
            }
        }
    },
    onPasswordBlur: function (field) {
        if (field.capsWarningTooltip.isHidden() === false) {
            field.capsWarningTooltip.hide();
        }
    }

});
