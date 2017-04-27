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
/* global Ext, logService */

Ext.define("Patients.ErrorLogger", {
    singleton: true,
    // Log only one error per page visit
    maxNbrLogs: 1,
    nbrErrorsLogged: 0,
    constructor: function () {
        window.onerror = Ext.Function.bind(this.onError, this);
    },
    onError: function (message, file, line, column, errorObj) {
        if (!logService) {
            return;
        }

        var win = window, d = document;

        if (!message || message.match('chrome://') || message.match('Script error')) {
            return;
        }

        if (this.nbrErrorsLogged < this.maxNbrLogs && message && (line || file)) {
            this.nbrErrorsLogged++;

            var windowWidth = win.innerWidth
                    || d.documentElement.clientWidth
                    || d.body.clientWidth, windowHeight = win.innerHeight
                    || d.documentElement.clientHeight
                    || d.body.clientHeight;

            var crashData = {
                msg: message,
                url: file,
                line: line,
                href: win.location.href,
                windowWidth: windowWidth,
                windowHeight: windowHeight,
                extVersion: Ext.versions && Ext.versions.extjs && Ext.versions.extjs.version,
                localDate: new Date().toString(),
                column: column || '',
                stack: (errorObj && errorObj.stack) || ''
            };

            logService.logClientCrash(crashData);
        }
    }
});