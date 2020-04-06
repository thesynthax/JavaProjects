var line = document.getElementById("header2").children[0].children[0].children;

document.onmousemove = function(e) {
    var x = e.pageX - window.innerWidth/2;
    var y = e.pageY - window.innerHeight/2;

    /*for (var i = 0; i < line.length; i++) {
        line[i].style.setProperty('--x', x + 'px');
        line[i].style.setProperty('--y', y + 'px');
    }*/

};

