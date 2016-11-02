Ext.define("Patients.model.Patient",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Negative", "Ext.data.proxy.Direct" ],
  identifier : "negative",
  fields : [ {
    name : "firstName",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "secondName",
    type : "string"
  }, {
    name : "lastName",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "pesel",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "id",
    type : "string",
    allowNull : true,
    convert : null
  } ],
  proxy : {
    type : "direct",
    api : {
      read : "patientService.read",
      create : "patientService.update",
      update : "patientService.update"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});