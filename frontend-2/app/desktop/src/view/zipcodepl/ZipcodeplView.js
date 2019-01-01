Ext.define('PatientRegistry.view.zipcodepl.ZipcodeplView',{
	xtype: 'zipcodeplview',
	cls: 'zipcodeplview',
	controller: {type: 'zipcodeplviewcontroller'},
	viewModel: {type: 'zipcodeplviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">zipcodeplview</div>'
});
