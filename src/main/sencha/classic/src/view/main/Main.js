/* global Ext */

/**
 * This class is the main view for the application. It is specified in app.js as the
 * "mainView" property. That setting automatically applies the "viewport"
 * plugin causing this view to become the body element (i.e., the viewport).
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('Patients.view.main.Main', {
    extend: 'Ext.container.Viewport',
    xtype: 'app-main',
    requires: [
        'Ext.plugin.Viewport',
        'Ext.window.MessageBox',
        'Patients.view.main.MainController',
        'Patients.view.main.MainModel',
        'Patients.view.patient.PatientList',
        'Patients.view.user.UserList'
    ],
    controller: 'main',
    viewModel: {
        type: 'main'
    },
    layout: 'border',
//    plugins: 'viewport',
//    ui: 'navigation',
//    tabBarHeaderPosition: 1,
/*    titleRotation: 0,
    tabRotation: 0,
    header: {
        layout: {
            align: 'stretchmax'
        },
        title: {
            bind: {
                text: '{name}'
            },
            flex: 0
        },
        iconCls: 'fa-th-list',
        items: [{
                xtype: 'button',
                text: 'Logout',
                margin: '10 0',
                handler: 'onClickButton'
            }]
    },
    tabBar: {
        flex: 1,
        layout: {
            align: 'stretch',
            overflowHandler: 'none'
        }
    },
    responsiveConfig: {
        tall: {
            headerPosition: 'top'
        },
        wide: {
            headerPosition: 'left'
        }
    },
    defaults: {
        bodyPadding: 20,
        tabConfig: {
            plugins: 'responsive',
            responsiveConfig: {
                wide: {
                    iconAlign: 'left',
                    textAlign: 'left'
                },
                tall: {
                    iconAlign: 'top',
                    textAlign: 'center',
                    width: 120
                }
            }
        }
    },
*/
/*    items: [{
            title: 'Issuer',
            iconCls: 'fa-home',
            // The following grid shares a store with the classic version's grid as well!
            items: [{
                    xtype: 'issuerslist'
                }]
        }, {
            title: 'Patients',
            iconCls: 'fa-users',
            // The following grid shares a store with the classic version's grid as well!
            items: [{
                    xtype: 'patientlist'
                }]
        }, {
            title: 'Home',
            iconCls: 'fa-home',
            // The following grid shares a store with the classic version's grid as well!
            items: [{
                    xtype: 'mainlist'
                }]
        }, {
            title: 'Users',
            iconCls: 'fa-user',
            bind: {
                html: '{loremIpsum}'
            }
        }, {
            title: 'Groups',
            iconCls: 'fa-users',
            bind: {
                html: '{loremIpsum}'
            }
        }, {
            title: 'Settings',
            iconCls: 'fa-cog',
            bind: {
                html: '{loremIpsum}'
            }
        }] */
    items: [{
        xtype: 'container',
        id: 'app-header',
        region: 'north',
        height: 52,
        layout: {
            type: 'hbox',
            align: 'middle'
        },

        items: [{
            xtype: 'component',
            id: 'app-header-logo'
        },{
            xtype: 'component',
            cls: 'app-header-text',
//            bind: '{currentOrg.name}',
            bind: 'PPMD',
            flex: 1
        },{
            xtype: 'component',
            id: 'app-header-username',
            cls: 'app-header-text',
            bind: '{currentUser.name}',
            listeners: {
                click: 'onClickUserName',
                element: 'el'
            },
            margin: '0 10 0 0'
        }]
    }, {
        region: 'west',
        xtype: 'grid',
        reference: 'navigation',
        title: 'Navigation',
        width: 250,
        split: true,
        collapsible: true,
        selModel: {
            listeners: {
                select: 'onSelect'
            }
        },
        bind: {
            store: {
                fields: ['title'],
                data: {
                    items: [
                        { title: 'Issuers',  xtype: 'issuerslist' },
                        { title: 'Users',    xtype: 'userlist' },
                        { title: 'Patients', xtype: 'patientlist' }
                ]},
                proxy: {
                    type: 'memory',
                    reader: { type: 'json', rootProperty: 'items' }
                }
            },
            // Bind the project for the current user as the default selection (single).
            selection: {
//                bindTo: '{currentUser.project}',
                single: true
            }
        },
        hideHeaders: true,
        columns: [{
            text: 'Title', dataIndex: 'title', flex: 1
        }, {
            xtype: 'actioncolumn',
            width: 20,
            handler: 'onSearchClick',
            stopSelection: false,
            items: [{
                tooltip: 'Search tickets',
                iconCls: 'search'
            }]
        }]
    }, {
        xtype: 'tabpanel',
        region: 'center',
        flex: 1,
        reference: 'main',
        items: [/*{
            xtype: 'app-dashboard',
            title: 'Dashboard',
            listeners: {
                viewticket: 'onViewTicket',
                edituser: 'onEditUser'
            }
        }*/]
    }]
});
