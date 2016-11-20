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

Ext.define('Patients.view.patient.ViewModel', {
    extend: 'Ext.app.ViewModel',
    requires: ['Ext.data.BufferedStore'],
    data: {
        selectedObject: null,
        totalCount: null,
        correspondenceAddressEnabled: false,
        certificateOfDisabilityEnabled: false,
        certificateOfDisabilityExpirationEnabled: false
    },
    stores: {
        objects: {
            model: 'Patients.model.PatientPlus',
            autoLoad: false,
            buffered: true,
            remoteSort: true,
            remoteFilter: true,
            sorters: [{
                    property: 'lastName',
                    direction: 'ASC'
                }],
            listeners: {
                load: 'onObjectStoreLoad'
            },
            pageSize: 100,
            leadingBufferZone: 200
        },
        diagnosis: { type: 'diagnosis' }
    },
    formulas: {
        address: {
            bind: {
                bindTo: '{selectedObject.address}',
                deep: true
            },
            get: function (address) {
                return address;
            }
        },
        correspondenceAddress: {
            bind: {
                bindTo: '{selectedObject.correspondenceAddress}',
                deep: true
            },
            get: function (correspondenceAddress) {
                return correspondenceAddress;
            }
        }
//        certificateOfDisabilityEnabled: function(get) {
//            var selectedObject = get('selectedObject');
//            if (selectedObject === null) {
//                return false;
//            }
//            return selectedObject.get('disabilityLevel') !== 'NO_CERTIFICATE';
//        },
//        certificateOfDisabilityExpirationEnabled: function (get) {
//            var a = !get('certificateOfDisability');
//            var ret = !get('certificateOfDisability') && this.certificateOfDisabilityExpiration;
//            return ret;
//        },
    }
});
