
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
    var bar =document.getElementById("navbar-nav");
    if (x.style.display === "none") {
        x.style.display = "block";
        nav.style.height= "300px";
        nav.style.display="block";
        bar.style.position="relative";
        bar.style.left="2px";
    } else {
        x.style.display = "none";
        nav.style.height= "80px";
    }
}




















