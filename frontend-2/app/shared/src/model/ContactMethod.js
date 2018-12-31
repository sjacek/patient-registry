Ext.define("Patients.model.ContactMethod",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct" ],
  identifier : "uuid",
  fields : [ {
    name : "locale",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "method",
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
      read : "contactService.read",
      create : "contactService.update",
      update : "contactService.update",
      destroy : "contactService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});