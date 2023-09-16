
//var xmlhttp = new XMLHttpRequest();
//var url = "http://localhost:8080/api/contribution/total-savings";
//xmlhttp.open("GET", url, true);
//xmlhttp.send();
//xmlhttp.onreadystatechange = function(){
//    if(this.readyState == 4 && this.status ==200){
//        var data = JSON.parse(this.responseText);
//        console.log(data);
//        $("#member-table").DataTable({
//            "data": data,
//            columns: [
//                { "data": 'id' },
//                { "data": 'firstName' },
//                { "data": 'midName' },
//                { "data": 'email' },
//                { "data": 'idNo' },
//                { "data": 'phone' },
//                { "data": 'totalSavings' }
//            ]
//        });
//    }
//}



//Function for toggling sidebar
function showSideBar() {
    var x = document.getElementById("sidebar-container");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}



//function for adding contribution
$(document).ready(function (){
    $("#contrib-form").submit(function(event){
        event.preventDefault();
        if($("#id").val()==""){
            alert("Enter ID");
        }
        else if($("#type").val()==""){
            alert("Choose type");
        }
        else if($("#amount").val()==""){
            alert ("Enter amount");
        }
        else{
            var originalButtonText = $("#submitBtn").html();
            $("#submitBtn").html('<div class="spinner-border" role="status" id="spinner">' +
                          '<span class="visually-hidden"></span>' +
                          '</div> Please wait...');

            let formData = {
                memberId: $("#id").val(),
                contributionType:$("#type").val(),
                amount:$("#amount").val()
            }
            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "/save-contributions",
                data : JSON.stringify(formData),
                dataType : 'json',
                success: function(result){
                    if(result.status == "success"){
                        $("#submitBtn").html(originalButtonText);
                        $("#results").html('<div style="background-color:#7FA7B0; color:white;padding:20px 0px; text-align:center; width:100%;"><p>Contribution added successfully</p></div>');
                        $("#results").fadeToggle(3000);
                        $("#contrib-form")[0].reset();
                    }
                    else{
                        $("#submitBtn").html(originalButtonText);
                        $("#results").html('<div style="background-color:#7FA7B0; color:white;padding:20px 0px; text-align:center; width:100%;">Failed</p></div>');
                        $("#results").fadeToggle(3000);
                        $("#contrib-form")[0].reset();
                    }


                },
                error: function (xhr, status, error) {
                    console.log(error)
                    console.log(status)
                    $("#results").html('<div style="background-color:#7FA7B0; color:white;padding:20px 0px; text-align:center; width:100%;"><p>Error</p></div>');
                    $("#results").fadeToggle(3000);
                    $("#sub-form")[0].reset();
                    $("#submitBtn").html(originalButtonText);
                    $("#contrib-form")[0].reset();
                    // Log the error response for inspection
                    console.log(xhr.responseText);
                    //close activity indicator here
                }

            });
        }
    });
});

//formating memember contributions table as datable
$(document).ready( function () {
    new DataTable("#contributions-table", {
        processing:true,
        pagingType:'simple_numbers'
    });
});

//member table as datatable
$(document).ready( function () {
//    var table = $("#member-table").DataTable(
//
//    );

    new DataTable("#member-table", {
        processing: true,
        pagingType: "simple_numbers"
    });
});

$(document).ready(function(){
    new DataTable("#member-contributions-table", {
        processing:true,
        pagingType:'simple_numbers'
    });
});

$(document).ready(function(){
    new DataTable("#member-guarantee-table", {
        processing:true,
        pagingType:'simple_numbers'
    });
});

$(document).ready( function () {
    var table = $("#members_table").DataTable({
    });
});

$(document).ready( function () {
    var table = $("#guarantors-table").DataTable({

    });

});

$(document).ready(function(){
    var table = $("#repayments-table").DataTable({});
});

$(document).ready( function () {
    var table = $("#loans-table").DataTable({
    });

});