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

Ext.define('Patients.store.PatientStatus', {
    extend: 'Ext.data.Store',
    storeId: 'authority',
    data: [
        {value: Patients.constant.PatientStatus.NONE},
        {value: Patients.constant.PatientStatus.DECLARED},
        {value: Patients.constant.PatientStatus.CONFIRMED},
        {value: Patients.constant.PatientStatus.REGISTERED},
        {value: Patients.constant.PatientStatus.UNREGISTERED},
        {value: Patients.constant.PatientStatus.DEAD}
    ]
});