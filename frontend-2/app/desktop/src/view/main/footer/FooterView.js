Ext.define('PatientRegistry.view.main.footer.FooterView', {
	extend: 'Ext.Toolbar',
	xtype: 'footerview',
	cls: 'footerview',

	items: [
		{ 
			xtype: 'container',
			cls: 'footerviewtext',
      html: 'Ext JS version: ' + Ext.versions.extjs.version
			//bind: { html: '{name} footer' }
		}
	]
});
