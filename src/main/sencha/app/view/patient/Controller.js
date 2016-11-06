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

Ext.define('Patients.view.patient.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        formClassName: 'Patients.view.patient.Form',
        objectName: i18n.patient,
        objectNamePlural: i18n.patients
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('id'), null, function () {
            Patients.Util.errorToast(i18n.user_lastadmin_error);
        }, this);
    },
/*
    onSaveClick: function() {
        var form = this.lookupReference('form');
        
        if (form.isValid()) {
            var patient = this.getViewModel().get('thePatient');

            Ext.Msg.wait('Saving', 'Saving patient...');
            patient.save({
                scope: this,
		success: function(record, operation) {
                    Ext.toast({
                        title: 'Save',
                        html: 'Patient saved successfully',
                        align: 't',
                        bodyPadding: 10
                    });
		},
                callback: function() {
                    Ext.Msg.hide();
                }
            });
        }
    },
*/
    onCancelClick: function() {
        this.getView().destroy();        
    }

});