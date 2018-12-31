Ext.define("Patients.model.Project",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct", "Ext.data.validator.Presence" ],
  identifier : "uuid",
  fields : [ {
    name : "name",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "start",
    type : "date",
    dateFormat : "c",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "end",
    type : "date",
    dateFormat : "c",
    validators : [ {
      type : "presence"
    } ]
  }, "organization", {
    name : "organizationId",
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
      read : "projectService.read",
      create : "projectService.update",
      update : "projectService.update",
      destroy : "projectService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});