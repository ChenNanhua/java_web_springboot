$(document).ready(function() {
    
    "use strict";
    
    
    // Datatables
    
    $('#example').dataTable();
    $('#example1').dataTable();
    $('#example2').dataTable();
    $('#example3').dataTable();
    $('#example4').dataTable();
    $('#example5').dataTable();
    $('#example6').dataTable();
    $('#example7').dataTable();
    $('#example8').dataTable();
    $('#example9').dataTable();

    // Order by the grouping
    $.fn.isValid = function(){
        return this[0].checkValidity()
    }

    $('.date-picker').datepicker({
        orientation: "top auto",
        autoclose: true
    });
});