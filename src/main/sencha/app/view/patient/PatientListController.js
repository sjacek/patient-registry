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

//    onItemSelected: function() {
//        var grid = this.lookupReference('patientListGrid'),
//            model = grid.getSelectionModel();
//        console.log(model);
//    },
    
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
        this.fireEvent('createTab', 'Patient - New', {
            xtype: 'patient',
            session: true
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
        var grid = this.lookupReference('patientListGrid'),
            model = grid.getSelectionModel(),
            patient = model.getSelection();
        console.log(patient);
        grid.getStore().remove(patient);
    }

});
