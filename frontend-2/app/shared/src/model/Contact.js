Ext.define("Patients.model.Contact",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "method",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "contact",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  } ]
});