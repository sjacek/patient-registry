/* 
 * Copyright (C) 2016 Jacek Sztajnke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* global Ext, i18n */

Ext.define('Patients.view.auth.PwResetReq', {
	extend: 'Patients.view.base.LockingWindow',

	title: '<i class="x-fa fa-rocket"></i> ' + i18n.app_name + ': ' + i18n.auth_pwresetreq,

	defaultFocus: 'form',

	items: [ {
		xclass: 'Patients.view.auth.Dialog',
		listeners: {
			beforerender: 'onResetRequestBeforeRender'
		},
		width: 455,
		defaultButton: 'resetPassword',
		autoComplete: true,
		bodyPadding: '20 20',
		layout: {
			type: 'vbox',
			align: 'stretch'
		},

		defaults: {
			margin: '5 0'
		},

		api: {
			submit: 'securityService.resetRequest'
		},
		paramsAsHash: true,

		cls: 'auth-dialog-login',
		items: [ {
			xtype: 'label',
			cls: 'lock-screen-top-label',
			text: i18n.auth_pwresetreq_info
		}, {
			xtype: 'textfield',
			cls: 'auth-textbox',
			height: 55,
			name: 'email',
			hideLabel: true,
			allowBlank: false,
			emptyText: 'user@example.com',
			vtype: 'email',
			triggers: {
				glyphed: {
					cls: 'trigger-glyph-noop auth-email-trigger'
				}
			}
		}, {
			xtype: 'button',
			reference: 'resetPassword',
			scale: 'large',
			ui: 'soft-blue',
			formBind: true,
			iconAlign: 'right',
			iconCls: 'x-fa fa-angle-right',
			text: i18n.auth_pwresetreq,
			listeners: {
				click: 'onResetRequestClick'
			}
		}, {
			xtype: 'component',
			html: '<div style="text-align:right">' + '<a href="#auth.signin" class="link-forgot-password">' + i18n.auth_backtosignin + '</a></div>'
		} ]
	} ]
});
