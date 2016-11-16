/* global Ext */

Ext.define("Patients.model.PatientPlus", {
    extend: "Patients.model.Patient",
    uses: ['Patients.model.Contact'],

    hasMany: {
        model: 'Patients.model.Contact',
        name: 'contacts'
    }
});
