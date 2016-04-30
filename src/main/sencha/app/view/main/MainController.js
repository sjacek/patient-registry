/* 
 * Copyright (C) 2015 jsztajnke
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

/**
 * This class is the controller for the main view for the application. It is specified as
 * the "controller" of the Main view class.
 */
Ext.define('Patients.view.main.MainController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.main',

    init: function() {
        this.listen({
            controller: {
                '*': {
                    createTab: 'onCreateTab'
                }
            }
        });
    },

    createTab: function (id, cfg) {
        var tabs = this.lookupReference('main'),
            tab = tabs.items.getByKey(id);

        if (!tab) {
            cfg.itemId = id;
            cfg.closable = true;
            tab = tabs.add(cfg);
//            tab.setTitle(id);
        }

        tabs.setActiveTab(tab);
    },

    onCreateTab: function(title, cfg) {
        this.createTab(title, cfg);
    },
    
    onSelect: function (view, record, index, eOpts) {
        var data = record['data'];

        this.createTab(data['title'], {
            xtype: data['xtype'],
            session: true,
            viewModel: {
                data: {
//                    theTicket: rec
                }
            }
        });
    },

    onSearchClick: function (view, rowIdx, colIdx, item, e, rec) {
        this.createTab('project', rec, {
            xtype: 'ticketsearch',
            listeners: {
                viewticket: 'onViewTicket'
            },
            viewModel: {
                data: {
                    theProject: rec
                }
            }
        });
    },

// -------------------------------------------------------------
    onItemSelected: function (sender, record) {
        Ext.Msg.confirm('Confirm', 'Are you sure?', 'onConfirm', this);
    },

    onConfirm: function (choice) {
        if (choice === 'yes') {
            //
        }
    },

    onClickButton: function () {
        // Remove the localStorage key/value
        localStorage.removeItem('PatientsLoggedIn');

        // Remove Main View
        this.getView().destroy();

        // Add the Login Window
        Ext.create({
            xtype: 'login'
        });
    } /*,

    onAddButton: function() {
        Ext.create({ xtype: 'patient' });
    },
    
    onEditButton: function() {
        Ext.create({ xtype: 'patient' });
    },
    
    onRemoveButton: function() {
        Ext.create({ xtype: 'patient' });
    }*/
    
});
