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

Ext.define('Patients.view.zipcodepl.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        objectName: i18n.zipcodepoland,
        objectNamePlural: i18n.zipcodespoland
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('zipCode'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
//        this.onGridRefresh();
    },
    onCancelClick: function () {
        this.getView().destroy();
    },
    onEdit: function () {
        var rowedit = this.lookup('gridPanel').findPlugin('rowediting');
        if (!rowedit.editing) {
            var store = this.getStore(this.getObjectStoreName()),
                record = store.getModel().create();
            this.getViewModel().set(this.getSelectedObjectName(), record);
            store.add(record);
            rowedit.startEdit(record);
        }
    }
});