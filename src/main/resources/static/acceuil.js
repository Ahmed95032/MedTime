onload = function(){
let T = document.getElementsByClassName("select");
for(let i = 0; i<T.length; i++)
    T[i].onchange = function(){
        this.form.submit();
    };
};