Ext.define('PatientRegistry.view.orphadata.OrphadataView',{
	xtype: 'orphadataview',
	cls: 'orphadataview',
	controller: {type: 'orphadataviewcontroller'},
	viewModel: {type: 'orphadataviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">orphadataview</div>'
});
