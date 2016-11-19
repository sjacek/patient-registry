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

Ext.define('Patients.store.DisabilityLevel', {
    extend: 'Ext.data.Store',
    storeId: 'disabilityLevel',
    fields: ['value', 'text'],
    data: [{
            value: Patients.constant.DisabilityLevel.NO_CERTIFICATE,
            text: i18n.patient_disabilitylevel_no_cerificate
        }, {
            value: Patients.constant.DisabilityLevel.LIGHTWIEGHT,
            text: i18n.patient_disabilitylevel_lightweight
        }, {
            value: Patients.constant.DisabilityLevel.MODERATE,
            text: i18n.patient_disabilitylevel_moderate
        }, {
            value: Patients.constant.DisabilityLevel.SEVERE,
            text: i18n.patient_disabilitylevel_severe
        }]
});