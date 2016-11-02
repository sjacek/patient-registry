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
/* global Ext */

Ext.define('Patients.plugin.Clearable', {
    extend: 'Ext.plugin.Abstract',
    alias: 'plugin.clearable',
    config: {
        toggleEvent: 'change',
        weight: -100
    },
    init: function (field) {
        var plugin = this;
        var toggleEvent = field.clearableEvent || plugin.getToggleEvent();
        var weight = plugin.getWeight();

        // <debug>
        if (!field.isXType('textfield')) {
            Ext.Error.raise({
                msg: 'Ext.form.field.plugin.Clearable: This plugin is intended for usage with textfield-derived components',
                field: field,
                fieldXtypes: field.getXTypes()
            });
        }
        // </debug>

        field.setTriggers(Ext.applyIf(field.getTriggers(), {
            clear: {
                cls: Ext.baseCSSPrefix + 'form-clear-trigger',
                weight: weight,
                handler: function () {
                    if (Ext.isFunction(field.clearValue)) {
                        field.clearValue();
                    } else {
                        field.setValue(null);
                    }
                    field.getTrigger('clear').hide();
                },
                hidden: !field.getValue()
            }
        }));

        field.on('render', function () {
            var listeners = {
                destroyable: true
            };

            listeners[toggleEvent] = function (field) {
                var fieldValue = field.getValue();
                var hasValue = false;

                switch (field.getXType()) {
                    case 'numberfield':
                        hasValue = fieldValue !== null;
                        break;
                    default:
                        hasValue = fieldValue;
                }

                field.getTrigger('clear')[hasValue ? 'show' : 'hide']();
            };

            field.clearableListeners = field.on(listeners);

        }, field, {
            single: true
        });

        Ext.Function.interceptAfter(field, 'setReadOnly', plugin.syncClearTriggerVisibility, plugin);
    },
    destroy: function () {
        var field = this.getCmp();
        if (field.clearableListeners) {
            field.clearableListeners.destroy();
        }
    },
    /**
     * Considers all conditions to set trigger visibility. Can be overridden to influence
     * when trigger is made visible.
     */
    syncClearTriggerVisibility: function () {
        var field = this.getCmp();
        var value = field.getValue();
        var clearTrigger = field.getTrigger('clear');
        var isReadOnly = field.readOnly;

        clearTrigger[value && !isReadOnly ? 'show' : 'hide']();
    }
});