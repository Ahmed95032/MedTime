onload = function(){
let cld = document.getElementById("table");
let timeContainer = document.getElementById("timeContainer");
let inptDate = document.getElementById("date");
let inptTime = document.getElementById("time");
let p = document.getElementById("p");
let btnPrev = document.getElementById("prevCld");
let btnNext = document.getElementById("nextCld");
let s = byte(medcin.jourDeTravaille);
let jourDeTravaille = [];
for(let i = 0; i<s.length && i<7; i++)
    if(s[i]=='1')
        jourDeTravaille.push(i);
function byte(n){
    let s = "";
    while(n>0){
        s += Math.floor(n%2);
        n = Math.floor(n/2);
    };
    return s;
};
function getDaysInMounth(year, month){
    return new Date(year, month, 0).getDate();
};
function getNumDay(year, month, day){
    // date.getDay() = [dimanche = 0, lundi = 1, mardi = 2, mercredi = 3, jeudi = 4, vendredi = 5, sammedi = 6];
    let numDay = new Date(year, month - 1, day).getDay() - 1;
    return (numDay<0)? 6 : numDay;
};
function dateToNumbers(sdate){
    return [+sdate.substring(0, 4), +sdate.substring(5, 7), +sdate.substring(8, 10)];
};
function timeToNumbers(stime){ // [h, m]
    return [+stime.substring(0, 2), +stime.substring(3, 5)];
};
let today = {
    year: 0,
    month: 0,
    day: 0,
    load(){
        let date = new Date();
        this.day = date.getDate();
        this.month = date.getMonth() + 1;
        this.year = date.getFullYear();
    },
};
let chosenDate = {
    year: "aaaa",
    month: "mm",
    day: "jj",
    hour: "--",
    minute: "--",
    load(year, month, day, hour, minute){
        if(year<today.year || (year==today.year && (month<today.month || (month==today.month && day<=today.day))))
            return;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        selectedDay.load(year, month, day, hour, minute);
    },
};
let selectedDay = {
    year: "aaaa",
    month: "mm",
    day: "jj",
    hour: "--",
    minute: "--",
    load(year, month, day, hour, minute){
        this.selectDate(year, month, day);
        this.selectTime(hour, minute);
    },
    addMonth(n = 1){
        this.month += n;
        if(this.month>12){
            this.month = 1;
            this.year++;
        }else if(this.month<=0){
            this.month = 12;
            this.year--;
        };
    },
    selectDate(year, month, day){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = "--";
        this.minute = "--";
        this.fillDateInputValue();
    },
    selectTime(hour, minute){
        this.hour = hour;
        this.minute = minute;
        this.fillTimeInputValue();
    },
    fillDateInputValue(){
        let year = ((this.year<10)? "0" : "") + this.year;
        let month = ((this.month<10)? "0" : "") + this.month;
        let day = ((this.day<10)? "0" : "") + this.day;
        inptDate.value = year + "-" + month + "-" + day;
    },
    fillTimeInputValue(){
        let hour = ((this.hour<10)? "0" : "") + this.hour;
        let minute = ((this.minute<10)? "0" : "") + this.minute;
        inptTime.value = hour + ":" + minute;
    },
};
let calendrier = {
    currentDate : {
        year: 0,
        month: 0,
        index: 0,
        addMonth(n = 1){
            this.month += n;
            if(this.month>12){
                this.month = 1;
                this.year++;
            }else if(this.month<=0){
                this.month = 12;
                this.year--;
            };
        },
    },
    lastDate : {
        year: 0,
        month: 0,
        index: 0,
        addMonth(n = 1){
            this.month += n;
            if(this.month>12){
                this.month = 1;
                this.year++;
            }else if(this.month<=0){
                this.month = 12;
                this.year--;
            };
        },
    },
    load(){
        this.currentDate.year = this.lastDate.year = today.year;
        this.currentDate.month = this.lastDate.month = today.month;
        this.fillParagraphe();
        for(let i = 0; i<2; i++)
            this.addTable();
    },
    addTable(){
        this.lastDate.index++;
        // cld.innerHTML += `
        // <div class="days" id="l${this.lastDate.index}">
        //     <h3>Lundi</h3>
        //     <h3>Mardi</h3>
        //     <h3>Mercredi</h3>
        //     <h3>Jeudi</h3>
        //     <h3>Vendredi</h3>
        //     <h3>Samedi</h3>
        //     <h3">Dimanche</h3>
        // </div>`
        cld.innerHTML += `
        <div class="days" id="l${this.lastDate.index}">
            <h3 class="${(jourDeTravaille.includes(0))? '' : 'disabled'}">Lun</h3>
            <h3 class="${(jourDeTravaille.includes(1))? '' : 'disabled'}">Mar</h3>
            <h3 class="${(jourDeTravaille.includes(2))? '' : 'disabled'}">Mer</h3>
            <h3 class="${(jourDeTravaille.includes(3))? '' : 'disabled'}">Jeu</h3>
            <h3 class="${(jourDeTravaille.includes(4))? '' : 'disabled'}">Ven</h3>
            <h3 class="${(jourDeTravaille.includes(5))? '' : 'disabled'}">Sam</h3>
            <h3 class="dim ${(jourDeTravaille.includes(6))? '' : 'disabled'}">Dim</h3>
        </div>`
        // ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"];
        let numDay = getNumDay(this.lastDate.year, this.lastDate.month, 1);
        let m = this.lastDate.month;
        let y = this.lastDate.year;
        this.lastDate.addMonth(-1);
        let nbDays = getDaysInMounth(this.lastDate.year, this.lastDate.month);
        let l = document.getElementById(`l${this.lastDate.index}`);
        for(let i = numDay - 1; i>=0; i--){
            l.innerHTML += `<p class="prev y${this.lastDate.year}m${this.lastDate.month}d${nbDays - i}
            ${(m==today.month && y==today.year)? "" : "ac"}">${nbDays - i}</p>`;
        };
        this.lastDate.addMonth();
        nbDays = getDaysInMounth(this.lastDate.year, this.lastDate.month);
        for(let i = 1; i<=nbDays; i++){
            let s = ((i<=today.day && m==today.month && y==today.year) || (!jourDeTravaille.includes(getNumDay(y, m, i)) && 
            (i!=chosenDate.day || m!=chosenDate.month || y!=chosenDate.year)))? "dis" : "actif";
            let sm = ((m<10)? "0" : "") + m;
            let sd = ((i<10)? "0" : "") + i;
            let selected = (this.lastDate.year==selectedDay.year && this.lastDate.month==selectedDay.month && i==selectedDay.day)? "selected" : "";
            l.innerHTML += `<button type="button" class="${s} ${selected}" id="y${y}m${m}d${i}" value="${y}-${sm}-${sd}">${sd}</button>`;
            if(selected=="selected")
                divTime.clear();
        };
        this.lastDate.addMonth();
        numDay = getNumDay(this.lastDate.year, this.lastDate.month, 1);
        for(let i = numDay + 1; i<=7; i++){ 
            let d = i - numDay;
            let sd = ((d<10)? "0" : "") + d;
            l.innerHTML += `<p class="next y${this.lastDate.year}m${this.lastDate.month}d${d} ac">${sd}</p>`;
        };
        this.addDis();
        this.addClickEventToBtn();
    },
    addDis(){
        for(let i = 0; i<allVacances.length; i++){
            if(allVacances[i]==null || allVacances[i].dateDebut==null || allVacances[i].dateFin==null || 
                allVacances[i].dateDebut>allVacances[i].dateFin)
                continue;
            let dateDebut = dateToNumbers(allVacances[i].dateDebut);
            let dateFin = dateToNumbers(allVacances[i].dateFin);
            let y = dateDebut[0], m = dateDebut[1], d = dateDebut[2];
            let btn = document.getElementById(`y${dateFin[0]}m${dateFin[1]}d${dateFin[2]}`);
            let numberDays = getDaysInMounth(y, m);
            if(btn!=null){
                btn.classList.remove("actif");
                btn.classList.add("dis");
            };
            while(y!=dateFin[0] || m!=dateFin[1] || d!=dateFin[2]){
                btn = document.getElementById(`y${y}m${m}d${d}`);
                if(btn!=null){
                    btn.classList.remove("actif");
                    btn.classList.add("dis");
                };
                d++;
                if(d>numberDays){
                    d = 1;
                    m++;
                    if(m>12){
                        m = 1;
                        y++;
                    };
                    numberDays = getDaysInMounth(y, m);
                };
            };
        };
        this.lastDate.addMonth(-1);
        let numberDays = getDaysInMounth(this.lastDate.year, this.lastDate.month);
        let days = [];
        let time = [];
        for(let d = 1; d<=numberDays; d++){
            days[d] = [];
        }
        // remplire variable time.
        if(divTime.heureDebut<divTime.heureFin){
            for(let i = divTime.heureDebut; i<=divTime.heureFin - divTime.duree; i+=divTime.duree){
                time.push([i, true]);
            };
        }else{
            for(let i = 0; i<=divTime.heureFin - divTime.duree; i+=divTime.duree){
                time.push([i, true]);
            };
            for(let i = divTime.heureDebut; i<=1440 - divTime.duree; i+=divTime.duree){
                time.push([i, true]);
            };
        };
        
        for(let i = 0; i<rendezVousList.length; i++){
            if(rendezVousList[i]==null || rendezVousList[i].date==null || rendezVousList[i].time==null)
                continue;
            let date = dateToNumbers(rendezVousList[i].date); //[year, month, day];
            if(date[0]==this.lastDate.year && date[1]==this.lastDate.month)
                days[date[2]].push(rendezVousList[i]);
        };
        for(let i = 1; i<days.length; i++){
            for(let j = 0; j<days[i].length; j++){
                let timeRendezVous = timeToNumbers(days[i][j].time); //[heur, minute];
                timeRendezVous = timeRendezVous[0]*60 + timeRendezVous[1];
                let dureeS = timeToNumbers(days[i][j].dureeS);
                dureeS = dureeS[0]*60 + dureeS[1];
                for(let k = 0; k<time.length; k++)
                    if(time[k][1] && timeRendezVous!=selectedDay.hour*60 + selectedDay.minute && (timeRendezVous==time[k][0] || 
                    (timeRendezVous<time[k][0] + divTime.duree && timeRendezVous + dureeS>time[k][0])))
                        time[k][1] = false;
            };
            let test = true;
            for(let j = 0; j<time.length; j++)
                if(time[j][1]){
                    test = false;
                    break;
                };
            if(test){
                let btn = document.getElementById(`y${this.lastDate.year}m${this.lastDate.month}d${i}`);
                if(btn!=null){
                    btn.classList.remove("actif");
                    btn.classList.add("dis");
                };
            };
            for(let j = 0; j<time.length; j++)
                time[j][1] = true;
        };
        this.lastDate.addMonth();
    },
    addClickEventToBtn(){
        let T = document.getElementsByClassName("prev");
        for(let i = 0; i<T.length; i++)
            T[i].onclick = function(){
                let btn = document.getElementById(this.classList[1]);
                if(!calendrier.prev() || btn==null || btn.classList[0]=="dis") return;
                T = document.getElementsByClassName("actif");
                for(let i = 0; i<T.length; i++)
                    T[i].classList.remove('selected');
                btn.classList.add("selected");
                selectedDay.selectDate(+btn.value.substring(0, 4), +btn.value.substring(5, 7), +btn.value.substring(8, 10));
                divTime.clear();
                inptTime.value = "";
            };

        T = document.getElementsByClassName("actif");
        for(let i = 0; i<T.length; i++)
            T[i].onclick = function(){
                T = document.getElementsByClassName("actif");
                for(let i = 0; i<T.length; i++)
                    T[i].classList.remove('selected');
                this.classList.add('selected');
                selectedDay.selectDate(+this.value.substring(0, 4), +this.value.substring(5, 7), +this.value.substring(8, 10));
                divTime.clear();
                inptTime.value = "";
            };

        T = document.getElementsByClassName("next");
        for(let i = 0; i<T.length; i++)
            T[i].onclick = function(){
                let btn = document.getElementById(this.classList[1]);
                if(!calendrier.next() || btn==null || btn.classList[0]=="dis") return;
                T = document.getElementsByClassName("actif");
                for(let i = 0; i<T.length; i++)
                    T[i].classList.remove('selected');
                btn.classList.add("selected");
                selectedDay.selectDate(+btn.value.substring(0, 4), +btn.value.substring(5, 7), +btn.value.substring(8, 10));
                divTime.clear();
                inptTime.value = "";
            };
    },
    next(){
        this.currentDate.index++;
        this.currentDate.addMonth();
        if(this.currentDate.index + 2>=this.lastDate.index){
            // this.currentDate.index--
            // btnNext.classList.add('dis');
            // return false;
            this.addTable();
        };
        cld.style.marginLeft = -this.currentDate.index*100 + "%";
        btnPrev.classList.remove('dis');
        this.fillParagraphe();
        return true;
    },
    prev(){
        if(this.currentDate.index<=0)
            return false;
        this.currentDate.index--;
        this.currentDate.addMonth(-1);
        cld.style.marginLeft = -this.currentDate.index*100 + "%";
        if(this.currentDate.index<=0)
            btnPrev.classList.add('dis');
        // btnNext.classList.remove('dis');
        this.fillParagraphe();
        return true;
    },
    fillParagraphe(){
        let year = ((this.currentDate.year<10)? "0" : "") + this.currentDate.year;
        let month = ((this.currentDate.month<10)? "0" : "") + this.currentDate.month;
        p.innerHTML = year + "/" + month;
    },
};
let divTime = {
    heureDebut : +medcin.heurDebut.substring(0, 2)*60 + +medcin.heurDebut.substring(3, 5),
    heureFin : +medcin.heurFin.substring(0, 2)*60 + +medcin.heurFin.substring(3, 5),
    duree : Math.max(+medcin.dureeM.substring(0, 2)*60 + +medcin.dureeM.substring(3, 5), 5),
    load(){
        this.createBouttons();
    },
    createBouttons(){
        timeContainer.innerHTML = "";
        if(this.heureDebut<this.heureFin){
            for(let i = this.heureDebut; i<=this.heureFin - this.duree; i+=this.duree){
                let time = this.calculeTime(i);
                let h = ((time[0]<10)? "0" : "") + time[0];
                let m = ((time[1]<10)? "0" : "") + time[1];
                let s = h + ":" + m;
                timeContainer.innerHTML += `
                    <button type="button" class="timeBtn timeActif" id="h${time[0]}m${time[1]}" value="${s}">${s}</button>`;
            };
        }else{
            for(let i = 0; i<=this.heureFin - this.duree; i+=this.duree){
                let time = this.calculeTime(i);
                let h = ((time[0]<10)? "0" : "") + time[0];
                let m = ((time[1]<10)? "0" : "") + time[1];
                let s = h + ":" + m;
                timeContainer.innerHTML += `
                    <button type="button" class="timeBtn timeActif" id="h${time[0]}m${time[1]}" value="${s}">${s}</button>`;
            };
            for(let i = this.heureDebut; i<=1440 - this.duree; i+=this.duree){
                let time = this.calculeTime(i);
                let h = ((time[0]<10)? "0" : "") + time[0];
                let m = ((time[1]<10)? "0" : "") + time[1];
                let s = h + ":" + m;
                timeContainer.innerHTML += `
                    <button type="button" class="timeBtn timeActif" id="h${time[0]}m${time[1]}" value="${s}">${s}</button>`;
            };
        };
        if(!isNaN(+selectedDay.minute) && !isNaN(+selectedDay.hour))
            this.clear();
    },
    setDisable(){
        // set the disable btn;
        let T = document.getElementsByClassName("timeBtn");
        for(let i = 0; i<T.length; i++){
            T[i].classList.remove('selected');
            T[i].classList.remove('timeDis');
            T[i].classList.add('timeActif');
            T[i].onclick = function(){};
        };
        for(let i = 0; i<rendezVousList.length; i++){
            if(rendezVousList[i]==null || rendezVousList[i].date==null || rendezVousList[i].time==null)
                continue;
            let date = dateToNumbers(rendezVousList[i].date); //[year, month, day];
            if(date[0]==selectedDay.year && date[1]==selectedDay.month && date[2]==selectedDay.day){
                let T = document.getElementsByClassName("timeActif");
                let timeRendezVous = timeToNumbers(rendezVousList[i].time); //[heur, minute];
                timeRendezVous = timeRendezVous[0]*60 + timeRendezVous[1];
                let dureeS = timeToNumbers(rendezVousList[i].dureeS);
                dureeS = dureeS[0]*60 + dureeS[1];
                for(let j = 0; j<T.length; j++){
                    let time = timeToNumbers(T[j].value);
                    time = time[0]*60 + time[1];
                    if(timeRendezVous==time || (timeRendezVous<time + this.duree && timeRendezVous + dureeS>time)){
                        if(timeRendezVous==chosenDate.hour*60 + chosenDate.minute){
                            if(timeRendezVous==selectedDay.hour*60 + selectedDay.minute)
                                T[j].classList.add('selected');
                        }else{
                            T[j].classList.add('timeDis');
                            T[j].classList.remove('timeActif');
                            j--
                        };
                    };
                };
            };
        };
    },
    setEvent(){
        let T = document.getElementsByClassName("timeActif");
        for(let i = 0; i<T.length; i++)
            T[i].onclick = function(){
                let T = document.getElementsByClassName("timeActif");
                for(let i = 0; i<T.length; i++)
                    T[i].classList.remove('selected');
                this.classList.add('selected');
                selectedDay.selectTime(+this.value.substring(0, 2), +this.value.substring(3, 5));
            };
    },
    calculeTime(minute){
        return [Math.floor(minute/60), minute%60];
    },
    timeFormat(heur, minute){
        heur = ((heur<10)? "0" : "") + heur;
        minute = ((minute<10)? "0" : "") + minute;
        return heur + ":" + minute;
    },
    clear(){
        this.setDisable();
        this.setEvent();
    },
};
function load(){
    inptDate.value = "";
    inptTime.value = "";
    today.load();
	if(selectedRendezVous.date!=null && selectedRendezVous.time!=null && selectedRendezVous.role!=null && 
    selectedRendezVous.role.normalize()=="prêt"){
        let T = selectedRendezVous.date.split("-");
        chosenDate.load(+T[0], +T[1], +T[2], +selectedRendezVous.time.substring(0, 2), +selectedRendezVous.time.substring(3, 5));
    };
    calendrier.load();
    divTime.load();
};
load();
btnPrev.onclick = function(){
    calendrier.prev();
};
btnNext.onclick = function(){
    calendrier.next();
};
};