Ext.define('PatientRegistry.view.organization.OrganizationView',{
	xtype: 'organizationview',
	cls: 'organizationview',
	controller: {type: 'organizationviewcontroller'},
	viewModel: {type: 'organizationviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">organizationview</div>'
});
