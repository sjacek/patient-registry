Ext.define('Patients.store.Authority', {
	extend: 'Ext.data.Store',
	storeId: 'authority',
	data: [
		{ value: Patients.constant.Authority.ADMIN },
		{ value: Patients.constant.Authority.EMPLOYEE },
		{ value: Patients.constant.Authority.USER }
	]
});