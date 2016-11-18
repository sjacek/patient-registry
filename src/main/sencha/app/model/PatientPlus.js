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

Ext.define("Patients.model.PatientPlus", {
    extend: "Patients.model.Patient",
    requires: ['Patients.model.Contact', 'Patients.model.Address'],

    hasOne: [{
            model: 'Patients.model.Address',
            name: 'address'
        }, {
            model: 'Patients.model.Address',
            name: 'correspondenceAddress'
        }],

    hasMany: {
        model: 'Patients.model.Contact',
        name: 'contacts'
    },

    save: function (failure, success, callback) {
        logService.debug('PatientPlus.save contacts:' + this.contacts().getCount());
        logService.debug('PatientPlus.save address:' + this.address !== undefined);
        logService.debug('PatientPlus.save correspondenceAddress:' + this.correspondenceAddress !== undefined);

        this.superclass.save.call(this, failure, success, callback);
    }
});
