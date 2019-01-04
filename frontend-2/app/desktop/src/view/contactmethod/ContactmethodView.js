Ext.define('PatientRegistry.view.contactmethod.ContactmethodView',{
	xtype: 'contactmethodview',
	cls: 'contactmethodview',
	controller: {type: 'contactmethodviewcontroller'},
	viewModel: {type: 'contactmethodviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">contactmethodview</div>'
});
