Ext.define('PatientRegistry.view.userconfig.UserconfigView',{
	xtype: 'userconfigview',
	cls: 'userconfigview',
	controller: {type: 'userconfigviewcontroller'},
	viewModel: {type: 'userconfigviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">userconfigview</div>'
});
