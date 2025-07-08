document.addEventListener("DOMContentLoaded", function(){
function byte(n){
    let s = "";
    while(n>0){
        s += Math.floor(n%2);
        n = Math.floor(n/2);
    };
    return s;
};
let jours = [
    document.getElementById("lundi"),
    document.getElementById("mardi"),
    document.getElementById("mercredi"),
    document.getElementById("jeudi"),
    document.getElementById("vendredi"),
    document.getElementById("samedi"),
    document.getElementById("dimanche")
];

let jourDeTravaille = document.getElementById("jourDeTravaille");
let s = byte(+jourDeTravaille.value);
for(let i = 0; i<7 && i<s.length; i++)
    if(s[i]=='1')
        jours[i].className = "select";
for(let i = 0; i<jours.length; i++)
    jours[i].onclick = function(){
        this.className = (this.className=="dis")? "select" : "dis";
        let n = 0;
        for(let i = 0; i<jours.length; i++)
            if(jours[i].className=="select")
                n += 2**i;
        jourDeTravaille.value = n;
    };
});