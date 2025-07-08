onload = function(){
let dd = document.getElementById("dateDebut");
let df = document.getElementById("dateFin");
let date = new Date();
date = dateFormat(date.getFullYear(), date.getMonth() + 1, date.getDate());
dd.min = date;
df.min = max(dd.value, date);
dd.max = df.value;
dd.onchange = function(){
    df.min = max(dd.value, date);
};
df.onchange = function(){
    dd.max = df.value;
};

function max(date1, date2){
    return (date1>date2)? date1 : date2;
};
function dateFormat(year, month, day){
    if(year<10) year = "0" + year;
    if(month<10) month = "0" + month;
    if(day<10) day = "0" + day;
    return year + "-" + month + "-" + day;
};
};