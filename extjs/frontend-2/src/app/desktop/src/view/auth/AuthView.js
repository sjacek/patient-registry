Ext.define('PatientsApp.view.auth.AuthView',{
	xtype: 'authview',
	cls: 'authview',
	controller: {type: 'authviewcontroller'},
	viewModel: {type: 'authviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">authview</div>'
});
