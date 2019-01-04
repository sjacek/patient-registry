Ext.define('PatientRegistry.view.project.ProjectView',{
	xtype: 'projectview',
	cls: 'projectview',
	controller: {type: 'projectviewcontroller'},
	viewModel: {type: 'projectviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">projectview</div>'
});
