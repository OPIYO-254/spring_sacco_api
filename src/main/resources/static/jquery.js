
$(document).ready(function(){
    $("#repayment-form").submit(function(event){
        event.preventDefault();
        if($("#amount").val()==""){
            $("#msg").html("<p style='color:red; font-size:10px;'>Please enter amount</p>").
            $("#msg").fadeToggle(3000);
            console.log('error');
        }
        else{
            var originalButtonText = $("#submitBtn").html();
            $("#submitBtn").html('<div class="spinner-border" role="status" id="spinner">' +
                  '<span class="visually-hidden"></span><div>');
            let formData = {
                loanId:$("#loanId").val(),
                amount:$("#amount").val()
            }
            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "api/repayment/add",
                data : JSON.stringify(formData),
                dataType : 'json',
                success: function(result){
                    if(result.status == "success"){
                        $("#submitBtn").html(originalButtonText);
                        $("#msg").html('<div style="color:green;"><p>'+status.message+'</p></div>');
                        $("#repayment-form")[0].reset();
                        $("#repayModal").modal("hide");
                        location.reload();
                    }
                    else{
                        $("#submitBtn").html(originalButtonText);
                        $("#msg").html('<div style="color:red;"><p>+status.message+</p></div>');
                        $("#repayment-form")[0].reset();
                        $("#repayModal").modal("hide");
                        location.reload();
                    }
                },
                error:function (xhr, status, error) {
                    console.log(error);
                    $("#repayment-form")[0].reset();
                    $("#repayModal").modal("hide");
                    location.reload();
                }
            });
        }
    });
});


//adding contribution through modal class
$(document).ready(function (){
        $("#contribution-form").submit(function(event){
            event.preventDefault();
            if($("#type").val()==""){
                $("#type-msg").html("<p style='color:red; font-size:10px;'>Please select loan type</p>").
                $("#type=msg").fadeToggle(3000);
            }
            else if($("#amount").val()==""){
                $("#amt-msg").html("<p style='color:red; font-size:10px;'>Please enter amount</p>").
                $("#amt-msg").fadeToggle(3000);
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
                            $("#contribution-form")[0].reset();
                            $("#repayModal").modal("hide");
                            location.reload();
                        }
                        else{
                            $("#submitBtn").html(originalButtonText);
                            $("#results").html('<div style="background-color:#7FA7B0; color:white;padding:20px 0px; text-align:center; width:100%;">Failed</p></div>');
                            $("#results").fadeToggle(3000);
                            $("#contribution-form")[0].reset();
                            $("#repayModal").modal("hide");
                            location.reload();
                        }


                    },
                    error: function (xhr, status, error) {
                        console.log(error)
                        console.log(status)
                        $("#results").html('<div style="background-color:#7FA7B0; color:white;padding:20px 0px; text-align:center; width:100%;"><p>Error</p></div>');
                        $("#results").fadeToggle(3000);
                        $("#contribution-form")[0].reset();
                        $("#submitBtn").html(originalButtonText);
                        $("#repayModal").modal("hide");
                        location.reload();
                        // Log the error response for inspection
                        console.log(xhr.responseText);
                        //close activity indicator here
                    }

                });
            }
        });
    });