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

Ext.define('Patients.view.patient.PatientListController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.patientlist',

    requires: [
        'Patients.store.Patient'
    ],

    onSelectionChange: function(model /*, selected, eOpts*/) {
        var count = model.getCount();
        if (count > 0) {
            this.lookupReference('editPatientButton').enable();
            this.lookupReference('removePatientButton').enable();
        }
        else {
            this.lookupReference('editPatientButton').disable();
            this.lookupReference('removePatientButton').disable();
        }
    },

    onRowDblClick: function(view, record/*, tr, rowIndex, e, eOpts*/) {
        var patient = view.getStore().findRecord("id", record.data.id);
        this.fireEvent('createTab', 'Patient - ' + patient.firstName + ' ' + patient.lastName, {
            xtype: 'patient',
            session: true,
            viewModel: {
                data: {
                    thePatient: patient
                }
            }
        });
    },

    onAdd: function() {
	var patient = this.getView().getStore().getModel().create();

        this.fireEvent('createTab', 'Patient - New', {
            xtype: 'patient',
            session: true,
            viewModel: {
                data: {
                    thePatient: patient
                }
            }
        });
    },

    onEdit: function() {
        var controller = this,
            store = this.getView().getStore();
        this.getView().getSelection().forEach(function(record) {
            var patient = store.findRecord("id", record.data.id);
            controller.fireEvent('createTab', 'Patient - ' + patient.firstName + ' ' + patient.lastName, {
                xtype: 'patient',
                session: true,
                viewModel: {
                    data: {
                        thePatient: patient
                    }
                }
            });
        });
    },
    
    onRemove: function() {
        var store = this.getView().getStore();
        this.getView().getSelection().forEach(function(record) {
            var patient = store.findRecord("id", record.data.id);
            Ext.Msg.wait('Removing', 'Removing patient...');
            patient.erase({
                scope: this,
                failure: function(record, operation) {
                    Ext.toast({
                        title: 'Remove',
                        html: 'Unable to remove patient',
                        align: 't',
                        bodyPadding: 10
                    });
                },
		success: function(record, operation) {
                    Ext.toast({
                        title: 'Remove',
                        html: 'Patient removed successfully',
                        align: 't',
                        bodyPadding: 10
                    });
		},
                callback: function() {
                    Ext.Msg.hide();
//                    store.destroy(user);
                }
            });
        });
    },

    onFilter: function(tf) {
    	var value = tf.getValue();
	var store = this.getStore(this.getObjectStoreName());
	if (value) {
            this.getViewModel().set('filter', value);
            store.filter('filter', value);
	}
	else {
            this.getViewModel().set('filter', null);
            store.removeFilter('filter');
	}
    }
});
