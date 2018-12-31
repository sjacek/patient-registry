Ext.define("Patients.model.orphadata.Disorder",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct", "Ext.data.validator.Presence" ],
  identifier : "uuid",
  fields : [ {
    name : "orphaNumber",
    type : "integer",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "typeOrphaNumber",
    type : "integer",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "orphaId",
    type : "integer",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "name",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "type",
    type : "string"
  }, {
    name : "expertLink",
    type : "string"
  }, {
    name : "icd10",
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
    directFn : "disorderService.read",
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});