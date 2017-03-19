/* 
 * Copyright (C) 2017 Jacek Sztajnke
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

Ext.define('Patients.view.organization.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        formClassName: 'Patients.view.organization.Form',
        objectName: i18n.organization,
        objectNamePlural: i18n.organization
    },
    onDelete: function (grid, rowIndex, colIndex) {
        this.setSelectedObject(grid.getStore().getAt(rowIndex));
        this.eraseObject(this.getSelectedObject().get('name'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
    },
    onEdit: function (view, row, col, action, ev, record) {
        this.setSelectedObject(record);
        this.createSubobjects();
        this.edit();
    },
    createSubobjects: function () {
    },
    edit: function () {
        this.getView().add({xclass: this.getFormClassName()});
        var formPanel = this.getView().getLayout().next();

        Ext.defer(function () {
            formPanel.isValid();
        }, 1);
    },
    onCancelClick: function () {
        this.getView().destroy();
    }

});