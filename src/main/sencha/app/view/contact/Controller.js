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

Ext.define('Patients.view.contact.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        formClassName: 'Patients.view.contact.Form',
        objectName: i18n.contact,
        objectNamePlural: i18n.contacts
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('method'), function() {
            this.getView().deselectAll();
            this.onGridRefresh();
            Patients.Util.successToast(i18n.destroysuccessful);
        }, null, this);
//        this.onGridRefresh();
    },
    onCancelClick: function() {
        this.getView().destroy();        
    }

});