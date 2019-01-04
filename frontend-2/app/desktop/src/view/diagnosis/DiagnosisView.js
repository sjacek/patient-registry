Ext.define('PatientRegistry.view.diagnosis.DiagnosisView',{
	xtype: 'diagnosisview',
	cls: 'diagnosisview',
	controller: {type: 'diagnosisviewcontroller'},
	viewModel: {type: 'diagnosisviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">diagnosisview</div>'
});
