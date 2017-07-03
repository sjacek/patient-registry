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

Ext.define('Patients.view.main.Main', {
    extend: 'Ext.container.Viewport',
    requires: ['Ext.list.Tree', 'Ext.toolbar.Toolbar'],
    controller: {
        xclass: 'Patients.view.main.MainController'
    },
    viewModel: {
        xclass: 'Patients.view.main.MainModel'
    },
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [{
        xtype: 'toolbar',
        cls: 'app-dash-dash-headerbar shadow',
        height: 44,
        items: [{
            xtype: 'component',
            reference: 'appLogo',
            cls: 'app-logo',
            html: '<div><img src="resources/images/logo.png"/><span>' + i18n.app_name + '</span></div>',
            width: 250
        }, {
            cls: 'no-bg-button',
            iconCls: 'x-fa fa-navicon',
            handler: 'onToggleNavigationSize'
        }, {
//                    xtype: 'tbtext',
//                    cls: 'navigation-title',
//                    bind: {
//                        text: '{navigationTitle}'
//                    }
//                }, {
            xtype: 'tbspacer',
            flex: 1
        }, {
            xtype: 'combobox',
            fieldLabel: i18n.organization,
            // store: '{organizations}',
            bind: {
                store: '{organizations}'
            //     value: '{currentOrganization}'
            },
            // // name: 'code',
            valueField: 'id',
            displayField: 'name',
            // queryMode: 'local',
            forceSelection: true,
            editable: false
        }, {
            cls: 'no-bg-button',
            iconCls: 'x-fa fa-cog',
            href: '#userconfig',
            hrefTarget: '_self',
            bind: {
                text: '{fullName}'
            },
            tooltip: i18n.userconfig
        }, {
            cls: 'no-bg-button',
            iconCls: 'x-fa fa-sign-out',
            handler: 'onLogoutClick',
            tooltip: i18n.auth_signout
        }]
    }, {
        xclass: 'Patients.view.main.MainContainer',
        reference: 'mainContainer',
        flex: 1,
        items: [{
            xtype: 'treelist',
            reference: 'navigationTreeList',
            itemId: 'navigationTreeList',
            ui: 'navigation',
            store: 'navigation',
            width: 250,
            expanderFirst: false,
            expanderOnly: false,
            animation: {
                duration: 100,
                easing: 'ease'
            },
            listeners: {
                selectionchange: 'onNavigationTreeSelectionChange'
            }
        }, {
            xtype: 'container',
            flex: 1,
            reference: 'mainCardPanel',
            padding: 8,
            layout: {
                type: 'card'
            }
        }]
    }]
});
