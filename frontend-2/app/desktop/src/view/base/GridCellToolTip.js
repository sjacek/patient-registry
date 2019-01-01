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

Ext.define('Patients.view.base.GridCellToolTip', {
    extend: 'Ext.tip.ToolTip',
    delegate: '.x-grid-cell',
    width: 200,
    padding: 0,
    listeners: {
        beforeshow: function (tip) {
            var div = this.triggerElement.firstChild;
            if (div.scrollWidth <= div.clientWidth) {
                return false;
            }
            tip.setWidth(div.scrollWidth > 220 ? 220 : div.scrollWidth);
            tip.update(div.textContent);
        }
    }
});
