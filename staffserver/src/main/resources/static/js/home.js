$(document).ready(function(){
    $("#search").on("keyup", function() {
        let value = $(this).val().toLowerCase();
        $("#staffList tr").filter(function() {
            let row = $(this).text().toLowerCase();
            let visible = row.indexOf(value) > -1 || row.indexOf("check in time") > -1;
            $(this).toggle(visible)
        });
    });
});