Ext.define("Patients.model.Patient",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct", "Ext.data.validator.Presence" ],
  identifier : "uuid",
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
    name : "gender",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "status",
    defaultValue : "NEW",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "ward",
    type : "boolean"
  }, {
    name : "pesel",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "birthday",
    type : "date",
    dateFormat : "c",
    validators : [ {
      type : "presence"
    } ]
  }, "address", "correspondenceAddress", {
    name : "organizationId",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "disabilityLevel",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "certificateOfDisabilityIssue",
    type : "date",
    dateFormat : "c"
  }, {
    name : "certificateOfDisabilityIssuingUnit",
    type : "string"
  }, {
    name : "certificateOfDisabilityExpiration",
    type : "date",
    dateFormat : "c"
  }, "diagnosis", {
    name : "id",
    type : "string",
    allowNull : true,
    convert : null
  }, {
    name : "version",
    type : "integer"
  } ],
  proxy : {
    type : "direct",
    api : {
      read : "patientService.read",
      create : "patientService.update",
      update : "patientService.update",
      destroy : "patientService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});