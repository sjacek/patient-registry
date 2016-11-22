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

Ext.define('Patients.store.ProjectStatus', {
    extend: 'Ext.data.Store',
    storeId: 'projectStatus',
    fields: ['value', 'text'],
    data: [{
            value: Patients.constant.PatientStatus.NEW,
            text: i18n.patient_status_new
        }, {
            value: Patients.constant.PatientStatus.DECLARED,
            text: i18n.patient_status_declared
        }, {
            value: Patients.constant.PatientStatus.CONFIRMED,
            text: i18n.patient_status_confirmed
        }, {
            value: Patients.constant.PatientStatus.REGISTERED,
            text: i18n.patient_status_registered
        }, {
            value: Patients.constant.PatientStatus.UNREGISTERED,
            text: i18n.patient_status_unregistered
        }, {
            value: Patients.constant.PatientStatus.DEAD,
            text: i18n.patient_status_dead
        }]
});