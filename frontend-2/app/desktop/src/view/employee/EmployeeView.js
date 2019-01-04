Ext.define('PatientRegistry.view.employee.EmployeeView',{
	xtype: 'employeeview',
	cls: 'employeeview',
	controller: {type: 'employeeviewcontroller'},
	viewModel: {type: 'employeeviewmodel'},
	requires: [],
	extend: 'Ext.panel.Panel',

	html: '<div style="font-size:24px;">employeeview</div>'
});
