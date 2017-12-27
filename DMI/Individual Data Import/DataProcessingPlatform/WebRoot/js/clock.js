//定义一个tick函数，以获取系统的时间
function tick(){
var clock_1 = document.getElementById('my_clock') ;
if(!clock_1){
	document.write("<div id='my_clock'></div>") ;
}

var year,month,day,hours,minutes,seconds;
var intYear,intMonth,intDay,intHours,intMinutes,intSeconds;
var today;
today = new Date();
intYear=today.getFullYear();
intMonth=today.getMonth()+1;
intDay=today.getDate();
intHours=today.getHours();
var week_cn = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六") ;
intMinutes=today.getMinutes();
intSeconds=today.getSeconds();
//获取系统时间的小时数
if(intHours < 10){
hours="0"+intHours+":";
}else{
hours=intHours+":";
}
//获取系统时间的分数
if(intMinutes<10){
minutes="0"+intMinutes+":";
}else{
minutes=intMinutes+":";
}
//获取系统时间的秒数
if(intSeconds<10){
seconds="0"+intSeconds+" ";
}else{
seconds=intSeconds+" ";
}

timeString=""+intYear+"年"+intMonth+"月"+intDay+"日"+" "+week_cn[today.getDay()]+" "+hours+minutes+seconds+"";
$("#my_clock").html(timeString);
//每隔0.1秒钟执行一次tick函数
window.setTimeout("tick()",100);
}