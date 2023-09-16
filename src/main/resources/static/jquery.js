
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