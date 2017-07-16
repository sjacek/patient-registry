/*
 * Copyright (C) 2017 Jacek Sztajnke
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

/**
 * A generic observable object, useful for passing context between ViewControllers.
 *
 * @private
 */
Ext.define('Patients.ux.Mediator', {
    singleton: true,
    mixins: ['Ext.mixin.Observable'],

    constructor: function (config) {
        this.mixins.observable.constructor.call(this, config);
    }
});
