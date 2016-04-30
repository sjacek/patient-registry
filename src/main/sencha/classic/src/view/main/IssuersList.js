/* global Ext */

/**
 * This view is an example list of people.
 */
Ext.define('Patients.view.main.IssuersList', {
    extend: 'Ext.grid.Panel',
    xtype: 'issuerslist',
    
    requires: [
        'Patients.store.Issuers'
    ],

    title: 'Issuers List',
    store: {type: 'issuers'},
//    plugins: [ 'cellediting' ],
    columns: [{
            text: "Id", dataIndex: 'id', sortable: true//,
//            editor: {
//                allowblank: false, type: 'string'
//            }
        },{
            text: "Ticker", dataIndex: 'ticker', sortable: true//,
//            editor: {
//                allowblank: false, type: 'string'
//            }
        },{
            text: "Issuer Name", dataIndex: 'issuerName', sortable: true//,
//            editor: {
//                allowblank: false, type: 'string'
//            }
        },{
            text: "Issuer Type", dataIndex: 'issuerType', sortable: true, width: 75//,
//            editor: {
//                allowblank: false, type: 'string'
//            }
        },{
            text: "Country", dataIndex: 'country', sortable: true, width: 75//,
//            editor: {
//                allowblank: false, type: 'string'
//            }
        }
    ],
    dockedItems: [
        {xtype: 'pagingtoolbar', store: {type: 'issuers'}, dock: 'bottom', displayInfo: true}
    ]
});
