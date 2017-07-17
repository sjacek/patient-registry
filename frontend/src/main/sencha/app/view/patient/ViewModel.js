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
        certificateOfDisabilityExpirationEnabled: false,
        isOrganizationEmpty: false
    },
    stores: {
        objects: {
            type: 'patient',
            listeners: {
                load: 'onObjectStoreLoad'
            }
        },
        diagnosis: { type: 'diagnosis' }
    },
    formulas : {
        // isOrganizationEmpty: {
        //     bind: {
        //         organizationId: '{Patients.app.globals.organizationId}'
        //     },
        //     get: function(data) {
        //         logService.debug('isOrganizationEmpty.get ' + data);
        //         // logService.debug('updateOrganizationId2 ' + ());
        //         return data === undefined || data === null || data === '';
        //     }
        // }
    }
});
