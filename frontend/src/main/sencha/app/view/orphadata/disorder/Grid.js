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

Ext.define('Patients.view.orphadata.disorder.Grid', {
    extend: 'Ext.grid.Panel',
    xtype: 'orphadatadisorderlist',
    reference: 'gridPanel',
    requires: ['Ext.grid.plugin.RowEditing'],
    stateful: true,
    stateId: 'view.orphadata.disorder.Grid',
    height: 100,
    beforeLayout: function () {
        this.height = Ext.Element.getViewportHeight() - 60;
        this.callParent(arguments);
    },
    autoLoad: true,
    bind: {
        store: '{objects}',
        selection: '{selectedObject}'
    },
    header: {
        title: i18n.orphadata_disorders,
        defaults: {
            xtype: 'button',
            margin: '0 0 0 5'
        },
        items: [{
            xtype: 'textfield',
            emptyText: i18n.filter,
            reference: 'textFilter',
            width: 250,
            plugins: [{
                ptype: 'clearable'
            }],
            listeners: {
                change: {
                    fn: 'onFilter',
                    buffer: 500
                }
            }
        // }, {
        //     text: i18n.create,
        //     tooltip: i18n.contact_create_tooltip,
        //     iconCls: 'x-fa fa-plus',
        //     ui: 'soft-green',
        //     handler: 'newObject'
        }]
    },
    listeners: {
        canceledit: 'back',
        afterRender: 'onBaseAfterRender'
    },
    cls: 'shadow',
    plugins: {
        ptype: 'rowediting',
        clicksToEdit: 2
    },
    viewConfig: {
        preserveScrollOnRefresh: true
    },
    selModel: 'rowmodel',
    columns: [{
        text: i18n.id,
        dataIndex: 'id',
        flex: 0,
        stateId: 'view.orphadata.disorder.Grid.id',
        hidden: true
    }, {
        text: i18n.orphadata_disorder_orphanumber,
        dataIndex: 'orphaNumber',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.orphaNumber'
    }, {
        text: i18n.orphadata_disorder_name,
        dataIndex: 'name',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.name'
    }, {
        text: i18n.orphadata_disorder_type,
        dataIndex: 'type',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.type'
    }, {
        text: i18n.orphadata_disorder_expert_link,
        dataIndex: 'expertLink',
        renderer : function(ref) {
            return '<a href="' + ref + '" target="_blank">' + ref + '</a>';
        },
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.expertLink'
    }, {
        text: i18n.orphadata_disorder_icd10,
        dataIndex: 'icd10',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.icd10'
    }, {
        text: i18n.orphadata_disorder_synonyms,
        dataIndex: 'synonyms',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.synonyms',
        css: 'white-space:normal;',
        renderer: function (value) {
            var ret = "";
            for (var i = 0, len = value.length; i < len; i++) {
                if (ret.length > 0) ret += "<BR/>";
                ret += value[i];
            }
            return ret;
        }
    }, {
        text: i18n.version,
        dataIndex: 'version',
        flex: 1,
        stateId: 'view.orphadata.disorder.Grid.version',
        hidden: true
    }],
    dockedItems: [
        {xtype: 'pagingtoolbar', bind: {store: '{objects}'}, dock: 'bottom', displayInfo: true}
    ]
});
