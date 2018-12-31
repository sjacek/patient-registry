Ext.define("Patients.model.AddressPl",
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
    name : "street",
    type : "string"
  }, {
    name : "house",
    type : "string"
  }, {
    name : "flat",
    type : "string"
  }, {
    name : "postOffice",
    type : "string"
  }, {
    name : "county",
    type : "string"
  }, {
    name : "voivodship",
    type : "string"
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