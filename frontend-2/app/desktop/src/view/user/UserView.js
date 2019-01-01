Ext.define('PatientRegistry.view.user.UserView',{
	xtype: 'userview',
	cls: 'userview',
	controller: {type: 'userviewcontroller'},
	viewModel: {type: 'userviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">userview</div>'
});
