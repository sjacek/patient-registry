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

Ext.define('Patients.view.contact.Controller', {
    extend: 'Patients.view.base.ViewController',
    config: {
        objectName: i18n.contact,
        objectNamePlural: i18n.contacts
    },
    erase: function () {
        this.eraseObject(this.getSelectedObject().get('method'), function () {
            Patients.Util.successToast(i18n.destroysuccessful);
            this.onGridRefresh();
        }, null, this);
//        this.onGridRefresh();
    },
    onCancelClick: function () {
        this.getView().destroy();
    },
    onEdit: function () {
//        this.newObject();
//        logService.debug("onEdit 1");
////        var rec = new Patients.model.Contact({
////            method: '',
////            description: ''
////        });
//        logService.debug("onEdit 2");
//        var grid = this.lookup('gridPanel');
//        logService.debug("onEdit 3");
        var rowedit = this.lookup('gridPanel').findPlugin('rowediting');
        if (!rowedit.editing) {
            var store = this.getStore(this.getObjectStoreName());
            logService.debug("onEdit 1");
            var model = store.getModel();
            logService.debug("onEdit 2 " + store.getTotalCount());
            var rec = model.create();
            this.getViewModel().set(this.getSelectedObjectName(), rec);
            logService.debug("onEdit 3 " + store.getTotalCount());
//            this.getStore(this.getObjectStoreName()).reload();
            store.add(rec);
            logService.debug("onEdit 4 " + store.getTotalCount());
            rowedit.startEdit(rec);
            logService.debug("onEdit 5");
        }
        logService.debug("onEdit 6");
//        this.newObject();
    }
});