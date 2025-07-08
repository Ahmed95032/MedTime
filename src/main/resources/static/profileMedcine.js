onload = function(){
function byte(n){
    let s = "";
    while(n>0){
        s += Math.floor(n%2);
        n = Math.floor(n/2);
    };
    return s;
};
let ul = document.getElementById("days");
let days = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"];
let s = byte(jourDeTravaille);
ul.innerHTML = "";
for(let i = 0; i<7 && i<s.length; i++)
    if(s[i]=='1')
        ul.innerHTML += `<li style="text-transform: capitalize;">${days[i]}</li>`;
if(ul.innerHTML=="")
    ul.innerHTML = `<li>aucun jour de travaille </li>`;
}