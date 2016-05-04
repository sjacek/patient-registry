/* 
 * Copyright (C) 2016 jsztajnke
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

Ext.define('Patients.view.user.UserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.user',

    requires: [
        'Patients.window.Toast'
    ],

    onSaveClick: function() {
        var form = this.lookupReference('form');
        
        if (form.isValid()) {
            var user = this.getViewModel().get('theUser');

            Ext.Msg.wait('Saving', 'Saving user...');
            user.save({
                scope: this,
                callback: this.onComplete
            });
        }
    },
    
    onComplete: function() {
        Ext.Msg.hide();
        Patients.toast({
            title: 'Save',
            html: 'User saved successfully',
            align: 't',
            bodyPadding: 10
        });
    },

    onCancelClick: function() {
        this.getView().destroy();        
    }
});
