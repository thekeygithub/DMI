function getFormatDate(str){ 
	var day=new Date(eval(str))
	var Year = 0; 
	var Month = 0; 
	var Day = 0; 
	var CurrentDate = ""; 
	//初始化时间 
	//Year= day.getYear();//有火狐下2008年显示108的bug 
	Year= day.getFullYear();//ie火狐下都可以 
	Month= day.getMonth()+1; 
	Day = day.getDate(); 
	//Hour = day.getHours(); 
	// Minute = day.getMinutes(); 
	// Second = day.getSeconds(); 
	CurrentDate += Year + "-"; 
	if (Month >= 10 ) 
	{ 
	CurrentDate += Month + "-"; 
	} 
	else 
	{ 
	CurrentDate += "0" + Month + "-"; 
	} 
	if (Day >= 10 ) 
	{ 
	CurrentDate += Day ; 
	} 
	else 
	{ 
	CurrentDate += "0" + Day ; 
	} 
	return CurrentDate; 
    } 
    
    
/**
格式化时间 
yyyy-MM-dd hh:mm:ss
*/
function getdate(time){
	var data = new Date();
	data.setTime(time); 
	var month = data.getMonth()+1;
	var date = data.getDate();
	var hour = data.getHours();
	var min = data.getMinutes();
	var sec = data.getSeconds();
	if(month<10) month="0"+month;
	if(date<10) date="0"+date;
	if(hour<10) hour="0"+hour;
	if(min<10) min="0"+min;
	if(sec<10) sec="0"+sec;
	var time = ""+data.getFullYear()+"-"+month+"-"+date+" "+hour+":"+min+":"+sec;
	return time;
}


        
/**
格式化时间 
yyyy-MM-dd hh:mm
*/
function getdateNoSec(time){
	var data = new Date();
	data.setTime(time); 
	var month = data.getMonth()+1;
	var date = data.getDate();
	var hour = data.getHours();
	var min = data.getMinutes();
	var sec = data.getSeconds();
	if(month<10) month="0"+month;
	if(date<10) date="0"+date;
	if(hour<10) hour="0"+hour;
	if(min<10) min="0"+min;
	if(sec<10) sec="0"+sec;
	
	var time = ""+data.getFullYear()+"-"+month+"-"+date+" "+hour+":"+min;
	
	return time;
}
