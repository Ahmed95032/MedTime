document.addEventListener("DOMContentLoaded", function(){
let btn = document.getElementById("pass");
let passdiv = document.getElementById("passdiv");
let timer = 20;
let passwd = {
    input : document.getElementById("password"),
    hidden : true,
};
let eye = {
    svg : document.getElementById("svg"),
    iris : {
        element : document.getElementById("eye"),
        x : 0,
        y : 0,
        rad : 0,
        maxRad : 18,
        load(){
            this.x = parseFloat(this.element.getAttribute("cx"));
            this.y = parseFloat(this.element.getAttribute("cy"));
            this.rad = parseFloat(this.element.getAttribute("r"));
        },
    },
    pupil : {
        element : document.getElementById("pupil"),
        x : 0,
        y : 0,
        rad : 0,
        maxRad : 17,
        load(){
            this.x = parseFloat(this.element.getAttribute("cx"));
            this.y = parseFloat(this.element.getAttribute("cy"));
            this.rad = parseFloat(this.element.getAttribute("r"));
        },
    },
    eyelid : {
        element : document.getElementById("eyelid"),
        eyelid2 : document.getElementById("eyelid2"),
        d : "",
        distance : 0,
        varE : 0, 
        start : 0, is : 0, fs : 0,
        end : 0, ie : 0, fe : 0,
        blink : false,
        time : 250,
        i : 0,
        delay : 10000,
        load(){
            this.d = this.element.getAttribute("d");
            let n = "", test = false, t = 0, nb = 0;
            for(let i=0; i<this.d.length; i++){
                if("-.0123456789".includes(this.d[i])){
                    if(test){
                        if(n==""){
                            if(nb==0) this.is = i;
                            else this.ie = i;
                        };
                        n += this.d[i];
                    };
                }else{
                    if(n!=""){
                        if(t>=2){
                            if(nb==0){
                                this.start = +n;
                                this.fs = i;
                                test = false;
                                t = 0;
                                nb++;
                            }else{
                                this.end = +n;
                                this.fe = i;
                                break;
                            };
                        }else if(test){
                            t++;
                        };
                        n = "";
                    };
                    if(this.d[i]=="Q"){
                        test = true;
                        t = 1;
                    };
                };
            };
            this.varE = this.start;
            this.distance = this.end - this.start;
        },
    },
    close(){
        if(this.eyelid.blink || !passwd.hidden){
            this.eyelid.varE += this.eyelid.distance*timer/this.eyelid.time;
            if(this.eyelid.varE>=this.eyelid.end){
                this.eyelid.varE = this.eyelid.end;
                this.eyelid.blink = false;
            };
            this.eyelid.element.setAttribute("d", this.eyelid.d.slice(0, this.eyelid.is) + this.eyelid.varE + this.eyelid.d.slice(this.eyelid.fs, this.eyelid.d.length));
            this.eyelid.eyelid2.setAttribute("d", this.eyelid.d.slice(0, this.eyelid.is) + this.eyelid.varE + this.eyelid.d.slice(this.eyelid.fs, this.eyelid.d.length));
        };
    },
    open(){
        if(passwd.hidden && !this.eyelid.blink){
            this.eyelid.varE -= this.eyelid.distance*timer/this.eyelid.time;
            if(this.eyelid.varE<=this.eyelid.start){
                this.eyelid.varE = this.eyelid.start;
            };
            this.eyelid.element.setAttribute("d", this.eyelid.d.slice(0, this.eyelid.is) + this.eyelid.varE + this.eyelid.d.slice(this.eyelid.fs, this.eyelid.d.length));
            this.eyelid.eyelid2.setAttribute("d", this.eyelid.d.slice(0, this.eyelid.is) + this.eyelid.varE + this.eyelid.d.slice(this.eyelid.fs, this.eyelid.d.length));
        };
    },
    load(){
        this.iris.load();
        this.pupil.load();
        this.eyelid.load();
    },
};
function focus(){
    passdiv.style.outlineWidth = "1px";
};
function blur(){
    passdiv.style.outlineWidth = "0";
};
function load(){
    eye.load();
    blur();
};
load();
onmouseenter = onmousemove = function(event){
    const rect = eye.svg.getBoundingClientRect();
    let x = event.clientX - (rect.left + rect.width/2 + window.scrollX), y = event.clientY - (rect.top + rect.height/2 + window.scrollY);
    let z = Math.sqrt(x**2 + y**2);
    let rad = eye.iris.maxRad - eye.iris.rad;
    let ix = x, iy = y;
    if(z>rad){
        ix *= rad/z;
        iy *= rad/z;
    };
    eye.iris.element.setAttribute("cx", ix + eye.iris.x);
    eye.iris.element.setAttribute("cy", iy + eye.iris.y);
    rad = eye.pupil.maxRad - eye.pupil.rad;
    ix = x; iy = y;
    if(z>rad){
        ix *= rad/z;
        iy *= rad/z;
    };
    eye.pupil.element.setAttribute("cx", ix + eye.pupil.x);
    eye.pupil.element.setAttribute("cy", iy + eye.pupil.y);
};
btn.onclick = function(){
    passwd.hidden = !passwd.hidden;
    passwd.input.focus();
    if(passwd.hidden)
        passwd.input.type = "password";
    else passwd.input.type = "text";
};
passwd.input.onfocus = function(){focus();};
passwd.input.onblur = function(){blur();};
let interval = setInterval(function(){
    eye.close();
    eye.open();
    if(passwd.hidden){
        eye.eyelid.i += timer;
        if(eye.eyelid.i>=eye.eyelid.delay){
            eye.eyelid.i %= eye.eyelid.delay;
            eye.eyelid.blink = true;
        };
    }else eye.eyelid.i = 0;
}, timer);
});