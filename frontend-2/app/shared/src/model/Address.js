Ext.define("Patients.model.Address",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "country",
    type : "string",
    defaultValue : "Pl",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "zipCode",
    type : "string"
  }, {
    name : "city",
    type : "string"
  }, {
    name : "address1",
    type : "string"
  }, {
    name : "address2",
    type : "string"
  } ]
});