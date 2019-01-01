Ext.define('PatientRegistry.view.main.MainViewModel', {
	extend: 'Ext.app.ViewModel',
	alias: 'viewmodel.mainviewmodel',
	data: {
		name: 'PatientRegistry',
		navCollapsed:       false,
		navview_max_width:    300,
		navview_min_width:     44,
		topview_height:        75,
		bottomview_height:     50,
		headerview_height:     50,
		footerview_height:     50,
		detailCollapsed:     true,
		detailview_width:      10,
		detailview_max_width: 300,
		detailview_min_width:   0,

	},
	formulas: {
		navview_width: function(get) {
			return get('navCollapsed') ? get('navview_min_width') : get('navview_max_width');
		},
		detailview_width: function(get) {
			return get('detailCollapsed') ? get('detailview_min_width') : get('detailview_max_width');
		}
	},
	stores: {
		menu: {
			"type": "tree",
			"root": {
				"expanded": true,
				"children": [
					{ "text": "Userconfig", "iconCls": "x-fa fa-cog", "xtype": "userconfigview", "leaf": true },
					{ "text": "User", "iconCls": "x-fa fa-cog", "xtype": "userview", "leaf": true },
					{ "text": "Employee", "iconCls": "x-fa fa-cog", "xtype": "employeeview", "leaf": true },
					{ "text": "Organization", "iconCls": "x-fa fa-cog", "xtype": "organizationview", "leaf": true },
					{ "text": "Orphadata", "iconCls": "x-fa fa-cog", "xtype": "orphadataview", "leaf": true },
					{ "text": "Patient", "iconCls": "x-fa fa-cog", "xtype": "patientview", "leaf": true },
					{ "text": "Project", "iconCls": "x-fa fa-cog", "xtype": "projectview", "leaf": true },
					{ "text": "Diagnosis", "iconCls": "x-fa fa-cog", "xtype": "diagnosisview", "leaf": true },
					{ "text": "Countrydictionary", "iconCls": "x-fa fa-cog", "xtype": "countrydictionaryview", "leaf": true },
					{ "text": "Contactmethod", "iconCls": "x-fa fa-cog", "xtype": "contactmethodview", "leaf": true },
					{ "text": "Zipcodepl", "iconCls": "x-fa fa-cog", "xtype": "zipcodeplview", "leaf": true },
          { "text": "Home", "iconCls": "x-fa fa-home", "xtype": "homeview", "leaf": true },
					{ "text": "Personnel", "iconCls": "x-fa fa-table", "xtype": "personnelview","leaf": true },
					//add new items on the next line (from sencha-node generate viewpackage)

				]
			}
		}
	}
});
