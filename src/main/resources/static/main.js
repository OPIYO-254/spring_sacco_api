
//Function for toggling sidebar
function showSideBar() {
    var x = document.getElementById("sidebar-container");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}


function showMenu() {
    var x = document.getElementById("navbarSupportedContent");
    var nav = document.getElementById("navbar");

    if (x.style.display === "none") {
        x.style.display = "block";
        nav.style.height= "300px";

    } else {
        x.style.display = "none";
        nav.style.height= "80px";
    }
}




















