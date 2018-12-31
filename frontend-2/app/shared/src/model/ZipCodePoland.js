Ext.define("Patients.model.ZipCodePoland",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct" ],
  identifier : "uuid",
  fields : [ {
    name : "zipCode",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "postOffice",
    type : "string"
  }, {
    name : "city",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "voivodship",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "street",
    type : "string"
  }, {
    name : "county",
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
      read : "zipCodePolandService.read",
      create : "zipCodePolandService.update",
      update : "zipCodePolandService.update",
      destroy : "zipCodePolandService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});