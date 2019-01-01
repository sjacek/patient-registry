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
/* global Ext */

/**
 * This is the base class for all Authentication related Form dialogs. It optionally
 * enables autoComplete support to any child textfield so that browsers or their plugins
 * may restore/persist username, password and other attributes to/from such forms.
 */
Ext.define('Patients.view.auth.Dialog', {
    extend: 'Ext.form.Panel',
    controller: {
        xclass: 'Patients.view.auth.AuthController'
    },
    /*
     * Seek out the first enabled, focusable, empty textfield when the form is focused
     */
    defaultFocus: 'textfield:focusable:not([hidden]):not([disabled]):not([value])',
    /**
     * @cfg {Boolean} [autoComplete=false] Enables browser (or Password Managers) support
     *      for autoCompletion of User Id and password.
     */
    autoComplete: false,
    initComponent: function () {
        var me = this, listen;

        if (me.autoComplete) {
            // Use standard FORM tag for detection by browser or password tools
            me.autoEl = Ext.applyIf(me.autoEl || {}, {
                tag: 'form',
                name: 'authdialog',
                method: 'post'
            });
        }

        me.addCls('auth-dialog');
        me.callParent();

        if (me.autoComplete) {
            listen = {
                afterrender: 'doAutoComplete',
                scope: me,
                single: true
            };

            Ext.each(me.query('textfield'), function (field) {
                field.on(listen);
            });
        }
    },
    doAutoComplete: function (target) {
        if (target.inputEl && target.autoComplete !== false) {
            target.inputEl.set({
                autocomplete: 'on'
            });
        }
    }
});
