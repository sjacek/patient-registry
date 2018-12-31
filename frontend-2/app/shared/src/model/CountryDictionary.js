Ext.define("Patients.model.CountryDictionary",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.identifier.Uuid", "Ext.data.proxy.Direct" ],
  identifier : "uuid",
  fields : [ {
    name : "code",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "countryEn",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "countryPl",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "countryDe",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
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
      read : "countryDictionaryService.read",
      create : "countryDictionaryService.update",
      update : "countryDictionaryService.update",
      destroy : "countryDictionaryService.destroy"
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});