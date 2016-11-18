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
Ext.define('Patients.overrides.AccessKeyConfig', {
    override: 'Ext.Component',

    config: {
        accessKey: null
    },
    hookAfterRender: function () {
        delete this.afterRender;
        this.afterRender();

        this.updateAccessKey(this.getAccessKey());
    },
    updateAccessKey: function (key) {
        if (this.rendered) {
            var focusEl = this.getFocusEl();
            focusEl.dom.accessKey = key;
        } else {
            this.afterRender = this.hookAfterRender;
        }
    }
});