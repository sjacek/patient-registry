Ext.define("Patients.model.Organization",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct" ],
  identifier : "uuid",
  fields : [ {
    name : "name",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "code",
    type : "string"
  }, {
    name : "parentId",
    type : "string"
  }, "parent", {
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
      read : "organizationService.read",
      create : "organizationService.update",
      update : "organizationService.update",
      destroy : "organizationService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});