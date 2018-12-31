Ext.define("Patients.model.Diagnosis",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct" ],
  identifier : "uuid",
  fields : [ {
    name : "diagnosisName",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "diagnosisEnglishName",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "icd10",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "description",
    type : "string"
  }, {
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
      read : "diagnosisService.read",
      create : "diagnosisService.update",
      update : "diagnosisService.update",
      destroy : "diagnosisService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});