/* 
 * Copyright (C) 2016 jsztajnke
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

Ext.define('Patients.view.user.UserListController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.userlist',

//    onItemSelected: function() {
//        var grid = this.lookupReference('patientListGrid'),
//            model = grid.getSelectionModel();
//        console.log(model);
//    },
    
    onSelectionChange: function(model /*, selected, eOpts*/) {
        var count = model.getCount();
        if (count > 0) {
            this.lookupReference('editUserButton').enable();
            this.lookupReference('removeUserButton').enable();
        }
        else {
            this.lookupReference('editUserButton').disable();
            this.lookupReference('removeUserButton').disable();
        }
    },

    onRowDblClick: function(view, record/*, tr, rowIndex, e, eOpts*/) {
        var user = view.getStore().findRecord("id", record.data.id);
        this.fireEvent('createTab', 'User - ' + user.loginName, {
            xtype: 'user',
            session: true,
            viewModel: {
                data: {
                    theUser: user
                }
            }
        });
    },

    onAdd: function() {
        this.fireEvent('createTab', 'User - New', {
            xtype: 'user',
            session: true
        });
    },

    onEdit: function() {
        var controller = this,
            store = this.getView().getStore();
        this.getView().getSelection().forEach(function(record) {
            var user = store.findRecord("id", record.data.id);
            controller.fireEvent('createTab', 'User - ' + user.loginName, {
                xtype: 'user',
                session: true,
                viewModel: {
                    data: {
                        theUser: user
                    }
                }
            });
        });
    },
    
    onRemove: function() {
        var grid = this.lookupReference('userListGrid'),
            model = grid.getSelectionModel(),
            user = model.getSelection();
        console.log(user);
        grid.getStore().remove(user);
    },

    onFilter: function(tf) {
    	var value = tf.getValue();
	var store = this.getStore(this.getObjectStoreName());
	if (value) {
            this.getViewModel().set('filter', value);
            store.filter('filter', value);
	}
	else {
            this.getViewModel().set('filter', null);
            store.removeFilter('filter');
	}
    }
});
