Ext.define("Patients.model.info.OrphadataInfo",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct", "Ext.data.identifier.Uuid" ],
  identifier : "uuid",
  fields : [ {
    name : "date",
    type : "date"
  }, {
    name : "version",
    type : "string"
  }, {
    name : "copyright",
    type : "string"
  } ],
  proxy : {
    type : "direct",
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});