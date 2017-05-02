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
    onDelete: function (grid, rowIndex, colIndex) {
        this.setSelectedObject(grid.getStore().getAt(rowIndex));
        this.eraseObject(this.getSelectedObject().get('firstName') + " " + this.getSelectedObject().get('lastName'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
    },
    onEdit: function (view, row, col, action, ev, record) {
        this.setSelectedObject(record);
        this.createSubobjects();
        this.edit();
    },
    createMenuDiagnosis: function () {
        var menu = Ext.create('Ext.menu.Menu'),
                me = this,
                store = Ext.getStore('diagnosis');
        store.each(function (record) {
            menu.add({
                text: record.get('diagnosisName'),
                handler: function () {
                    Ext.Msg.confirm(i18n.attention, Ext.String.format(i18n.patient_change_diagnosis_confirm_msg, record.get('diagnosisName')), function (choice) {
                        if (choice === 'yes') {
                            me.getSelectedObject().setDiagnosis(record);
                        }
                    });
                }
            });
        });
        var copytemplate = this.lookup('copyDiagnosisTemplateBtn');
        if (copytemplate !== null) {
            copytemplate.setMenu(menu);
        }
    },
    createSubobjects: function () {
        var viewModel = this.getViewModel(),
                selectedObject = this.getSelectedObject();
        if (!selectedObject.getAddress()) {
            selectedObject.setAddress(Ext.create('Patients.model.Address'));
        }

        var correspondenceAddress = selectedObject.getCorrespondenceAddress();
                correspondenceAddressEnabled = (correspondenceAddress !== null && correspondenceAddress.phantom === false);
        if (!correspondenceAddress) {
            selectedObject.setCorrespondenceAddress(Ext.create('Patients.model.Address'));
        }
        viewModel.set('correspondenceAddressEnabled', correspondenceAddressEnabled);
        
        var certificateOfDisabilityEnabled = (selectedObject.get('disabilityLevel') !== 'NO_CERTIFICATE');
        viewModel.set('certificateOfDisabilityEnabled', certificateOfDisabilityEnabled);
        if (!selectedObject.getDiagnosis()) {
            selectedObject.setDiagnosis(Ext.create('Patients.model.Diagnosis'));
        }

        var expirationData = selectedObject.get('certificateOfDisabilityExpiration');
        var enabled = expirationData !== undefined && expirationData !== null;
        viewModel.set('certificateOfDisabilityExpirationEnabled', viewModel.get('certificateOfDisabilityEnabled') && enabled);
    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();
        this.createMenuDiagnosis();

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    },
    save: function (callback) {
        var viewModel = this.getViewModel(), selectedObject = this.getSelectedObject();

        var correspondence_address = this.lookup('correspondenceAddress');
        if (correspondence_address.collapsed) {
            selectedObject.getCorrespondenceAddress().destroy();
            delete selectedObject.getCorrespondenceAddress();
        }
        if (!viewModel.get('certificateOfDisabilityExpirationEnabled')) {
            delete selectedObject.get('certificateOfDisabilityExpiration');
            selectedObject.set('certificateOfDisabilityExpiration', null);
        }

        this.callParent(arguments);
    },
    onCancelClick: function () {
        this.getView().destroy();
    },
    onContactsNewClick: function () {
        var store = this.getSelectedObject().contacts();
        store.insert(0, {method: '', contact: ''})[0];
    },
    onContactDeleteClick: function (view, row, col, action, ev, record) {
        var store = record.store;
        store.remove(record);
    },
    onDisabilityLevelSelect: function (combo, value, opts) {
        var viewModel = this.getViewModel();
        var certificateOfDisabilityEnabled = value.data.value.localeCompare('NO_CERTIFICATE') !== 0;
        viewModel.set('certificateOfDisabilityEnabled', certificateOfDisabilityEnabled);
        viewModel.set('certificateOfDisabilityExpirationEnabled', certificateOfDisabilityEnabled && viewModel.get('certificateOfDisabilityExpirationEnabled'));
        this.lookup('editPanel').isValid();
    },
    onExpirationDateChange: function (newValue) {
        var viewModel = this.getViewModel();
        viewModel.set('certificateOfDisabilityExpirationEnabled', viewModel.get('certificateOfDisabilityEnabled') && newValue.value);
        var certificateOfDisabilityExpirationDate = this.lookup('certificateOfDisabilityExpirationDate');
        Ext.apply(certificateOfDisabilityExpirationDate, {allowBlank: !viewModel.get('certificateOfDisabilityExpirationEnabled')}, {});
        this.lookup('editPanel').isValid();
    },
    validateContacts: function () {
        var grid = this.lookup('contactsGrid');
        var ret = true;
        grid.getStore().each(function (record) {
            if (record.get('method').value.lenght === 0 || record.get('contact').value.lenght === 0) {
                ret = false;
            }
        });
        return ret;
    },
    printReport: function (format) {
        var url = serverUrl + '/export/patients.' + format;
        var textFilter = this.lookup('textFilter');
        url += '?filter=' + textFilter.getValue();

        var grid = this.lookup('patientGrid');
        var sorter = grid.getStore().sorters.getAt(0);

        var sort = 'Id';
        var dir = 'ASC';
        if (typeof sorter != 'undefined') {
            var sort = sorter.property;
            var dir = sorter.direction;
        }
        url += '&sort=' + sort + '&dir=' + dir;

        Ext.Ajax.request({
            url: url,
            method: 'GET',
            autoAbort: false,
            success: function(result) {
                if (result.status == 204) {
                    Ext.Msg.alert('Empty Report', 'There is no data');
                }
                else if(result.status == 200) {
                    Ext.DomHelper.append(Ext.getBody(), {
                        tag:          'iframe',
                        frameBorder:  0,
                        width:        0,
                        height:       0,
                        css:          'display:none;visibility:hidden;height:0px;',
                        src:          url
                    });
                }
            },
            failure: function() {
                // failure action
            }
        });
    },
    printReportPdf: function () { this.printReport('pdf'); },
    printReportCsv: function () { this.printReport('csv'); },
    printReportXls: function () { this.printReport('xls'); }
});