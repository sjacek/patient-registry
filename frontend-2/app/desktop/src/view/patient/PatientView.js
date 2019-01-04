Ext.define('PatientRegistry.view.patient.PatientView',{
	xtype: 'patientview',
	cls: 'patientview',
	controller: {type: 'patientviewcontroller'},
	viewModel: {type: 'patientviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">patientview</div>'
});
