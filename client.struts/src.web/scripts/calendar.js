var gcGray = "#888888";
var gcToggle = "#FFFFFF";
var gcToggleT = "#076A92";
var gcBG = "#F2F2F2";
var gcBD = "#FFFFFF";
var tableBG = "#F2F2F2";
var tableBD = "#FFFFFF";
var listBG = "#F2F2F2";
var arrowBG = "#F2F2F2";
var arrowBD = "#FFFFFF";
var fontN = "11px Verdana, Arial";
var fontB = "bold 11px Arial";

var gdCtrlFut = new Object();
var goSelectTag = new Array();
var gdCurDate = new Date();
var giYear = gdCurDate.getFullYear();
var giMonth = gdCurDate.getMonth()+1;
var giDay = gdCurDate.getDate();

var timeReq = false;
var secondsReq = false;
var dateFormatYYYYDDMM = false;
var giHours=gdCurDate.getHours();
var giMinutes=gdCurDate.getMinutes();
var giSeconds=gdCurDate.getSeconds();

//
function fSetDate(iYear, iMonth, iDay) {

	VicPopCal.style.visibility = "hidden";
	gdCtrlFut.value = formatDateFuture(iYear, iMonth, iDay);
  	for (i in goSelectTag) {
  		goSelectTag[i].style.visibility = "visible";
  	}
  	goSelectTag.length = 0;
}
//
function formatDateFuture(iYear, iMonth, iDay) {
    if (iMonth == 1 || iMonth == 2 || iMonth == 3 || iMonth == 4 || iMonth == 5 
    		|| iMonth == 6 || iMonth == 7 || iMonth == 8 || iMonth == 9) {
      iMonth= '0'+iMonth;
    }
    if (iDay == 1 || iDay == 2 || iDay == 3 || iDay == 4 || iDay == 5 
    	|| iDay == 6 || iDay == 7 || iDay == 8 || iDay == 9) {
      iDay = '0'+iDay;
    }

	if(timeReq)
	{
		var t = time.value;
		if (t.length == 0) t = "00:00";
		return iDay+"-"+gMonths[eval(iMonth-1)]+"-"+iYear+" "+t;
	} else if (dateFormatYYYYDDMM) {
		return iYear+""+(iMonth)+""+iDay;
	} else {
		var dateFmt = iDay+"-"+gMonths[eval(iMonth-1)]+"-"+iYear;
        var date1 = formatUserFormat(dateFmt);
        return date1;
	}
}
//
function formatUserFormat(date) {
	
	var gMonths = new Array("01","02","03","04","05","06","07","08","09","10","11","12");
	var dd = date.substring(0,2);
	var mm = date.substring(3,5);
	var yy = date.substring(6,10);
	if (mm.indexOf(gMonths[0])!=-1) mm='Jan';
	if (mm.indexOf(gMonths[1])!=-1) mm='Feb';
	if (mm.indexOf(gMonths[2])!=-1) mm='Mar';
	if (mm.indexOf(gMonths[3])!=-1) mm='Apr';
	if (mm.indexOf(gMonths[4])!=-1) mm='May';
	if (mm.indexOf(gMonths[5])!=-1) mm='Jun';
	if (mm.indexOf(gMonths[6])!=-1) mm='Jul';
	if (mm.indexOf(gMonths[7])!=-1) mm='Aug';
	if (mm.indexOf(gMonths[8])!=-1) mm='Sep';
	if (mm.indexOf(gMonths[9])!=-1) mm='Oct';
	if (mm.indexOf(gMonths[10])!=-1) mm='Nov';
	if (mm.indexOf(gMonths[11])!=-1) mm='Dec';
    return dd+'-'+mm+'-'+yy;
}
//
function formatDate(iYear, iMonth, iDay) {
    if (iMonth == 1 || iMonth == 2 || iMonth == 3 || iMonth == 4 || iMonth == 5 
    		|| iMonth == 6 || iMonth == 7 || iMonth == 8 || iMonth == 9) {
      iMonth= '0'+iMonth;
    }
    if (iDay == 1 || iDay == 2 || iDay == 3 || iDay == 4 || iDay == 5 
    	|| iDay == 6 || iDay == 7 || iDay == 8 || iDay == 9) {
      iDay = '0'+iDay;
    }

	if(timeReq)
	{
		var t = time.value;
		if (t.length == 0) t = "00:00";
		return iDay+"-"+gMonths[eval(iMonth-1)]+"-"+iYear+" "+t;
	} else if (dateFormatYYYYDDMM) {
		return iYear+""+(iMonth)+""+iDay;
	} else
		return iDay+"-"+gMonths[eval(iMonth-1)]+"-"+iYear;
}

//
function fSetSelected(aCell) {

	var iOffset = 0;
  	var iYear = parseInt(tbSelYear.value);
  	var iMonth = parseInt(tbSelMonth.value);
	self.event.cancelBubble = true;
  	aCell.bgColor = gcBG;
  	
  	with (aCell.children["cellText"]){
   		var iDay = parseInt(innerText);
	   	if (color==gcGray) iOffset = (Victor<10)?-1:1;
	 	iMonth += iOffset;
 		if (iMonth<1) {
	  		iYear--;
  			iMonth = 12;
 		} else if (iMonth>12){
	  		iYear++;
  			iMonth = 1;
 		}
  	}
  	fSetDate(iYear, iMonth, iDay);
}

//
function Point(iX, iY) {

	this.x = iX;
	this.y = iY;
}

//
function fBuildCal(iYear, iMonth) {

	var aMonth=new Array();
  	for(i=1;i<7;i++) {
   		aMonth[i]=new Array(i);
	}
	var dCalDate=new Date(iYear, iMonth-1, 1);
  	var iDayOfFirst=dCalDate.getDay();
  	var iDaysInMonth=new Date(iYear, iMonth, 0).getDate();
  	var iOffsetLast=new Date(iYear, iMonth-1, 0).getDate()-iDayOfFirst+1;
  	var iDate = 1;
  	var iNext = 1;

  	for (d = 0; d < 7; d++) {
 		aMonth[1][d] = (d<iDayOfFirst)?-(iOffsetLast+d):iDate++;
 	}
  	for (w = 2; w < 7; w++) {
   		for (d = 0; d < 7; d++) {
  			aMonth[w][d] = (iDate<=iDaysInMonth)?iDate++:-(iNext++);
  		}
  	}
  	return aMonth;
}

//
function fDrawCal(iYear, iMonth, iCellHeight, sDateTextSize) {

	var WeekDay = new Array("S","M","T","W","T","F","S");

  	var headingStyle=" bgcolor='"+gcBG+"' bordercolor='"+gcBD+"' valign='middle' align='center' height='"+iCellHeight+"' style='font: "+fontB+";color:#000000'";

  	var valueStyle = " bgcolor='"+gcBG+"' bordercolor='"+gcBD+"' valign='middle' align='center' height='"+iCellHeight+"' style='font: "+fontB+";cursor:hand;'";            
  	with (document) {
 		write("<tr>");
		for(i=0; i<7; i++)
  			write("<td "+headingStyle+" >" + WeekDay[i] + "</td>");
 		write("</tr>");

   		for (w = 1; w < 7; w++) {
  			write("<tr>");
  			for (d = 0; d < 7; d++) {
   				write("<td id=calCell "+valueStyle+"onMouseOver='this.bgColor=gcToggle' onMouseOut='this.bgColor=gcBG' onclick='fSetSelected(this)'>");
   				write("<font id=cellText Victor='Liming Weng'> </font>");
   				write("</td>");
  			}
  			write("</tr>");
 		}
  	}
}

//
function fUpdateCal(iYear, iMonth) {

	myMonth = fBuildCal(iYear, iMonth);
  	var i = 0;
  	for (w = 0; w < 6; w++) {
 		for (d = 0; d < 7; d++) {
  			with (cellText[(7*w)+d]) {
   				Victor = i++;
   				if (myMonth[w+1][d]<0) {
    				color = gcGray;
    				innerText = -myMonth[w+1][d];
   				} else {
    				color = ((d==0))?"red":"black";
    				innerText = myMonth[w+1][d];
   				}
  			}
  		}
  	}
}

//
function fSetYearMon(iYear, iMon) {

	tbSelMonth.options[iMon-1].selected = true;
  	for (i = 0; i < tbSelYear.length; i++) {
 		if (tbSelYear.options[i].value == iYear) tbSelYear.options[i].selected = true;
 	}
  	fUpdateCal(iYear, iMon);
}

//
function fSetTime(iHour, iMinutes) {

	time.value = iHour+":"+iMinutes + ":" + giSeconds;
}

//
function fPrevMonth() {

	var iMon = tbSelMonth.value;
  	var iYear = tbSelYear.value;
  	
  	if (--iMon<1) {
   		iMon = 12;
   		iYear--;
  	}
  	fSetYearMon(iYear, iMon);
}

//
function fNextMonth() {

	var iMon = tbSelMonth.value;
  	var iYear = tbSelYear.value;

 	if (++iMon>12) {
   		iMon = 1;
   		iYear++;
  	}
  	fSetYearMon(iYear, iMon);
}

//
function fToggleTags() {

	with (document.all.tags("SELECT")){
  		for (i=0; i<length; i++) {
   			if ((item(i).Victor!="Won")&&fTagInBound(item(i))){
    			item(i).style.visibility = "hidden";
    			goSelectTag[goSelectTag.length] = item(i);
    		}
   		}
  	}
}

//
function fTagInBound(aTag) {

	with (VicPopCal.style){
   		var l = parseInt(left);
  	 	var t = parseInt(top);
   		var r = l+parseInt(width);
   		var b = t+parseInt(height);
 		var ptLT = fGetXY(aTag);
 		return !((ptLT.x>r)||(ptLT.x+aTag.offsetWidth<l)||(ptLT.y>b)||(ptLT.y+aTag.offsetHeight<t));
  	}
}

//
function fGetXY(aTag) {

	var oTmp = aTag;
  	var pt = new Point(0,0);
  	do {
   		pt.x += oTmp.offsetLeft;
   		pt.y += oTmp.offsetTop;
   		oTmp = oTmp.offsetParent;
  	} while(oTmp.tagName!="BODY");
  	return pt;
}

// Main: popCtrl is the widget beyond which you want this calendar to appear;
//       dateCtrl is the widget into which you want to put the selected date.
// i.e.: <input type="text" name="dc" style="text-align:center" readonly><INPUT type="button" value="V" onclick="fPopCalendar(dc,dc);return false">
function fPopCalendar(popCtrl, dateCtrl) {

	gdCtrlFut = dateCtrl;
	var ppCtrl = popCtrl.value; // DateFormat = 'dd-Mon-yyyy hh:mm'
//	if (ppCtrl.length != 0)
//	{ 
//	  	fSetYearMon(ppCtrl.substr(7,4), getMonth(ppCtrl.substr(3,3)));
//	  	fSetTime(ppCtrl.substr(12,2), ppCtrl.substr(15,2));
//	}
//	else 
//	{
		fSetYearMon(giYear, giMonth);
		fSetTime(giHours, giMinutes);
//	}
  	var point = fGetXY(popCtrl);
  	
	//only when body scroll is on
	point.x = point.x - 220;
	point.y = point.y - 25;
	
  	
  	with (VicPopCal.style) {
   		left = point.x;
 		top = point.y+popCtrl.offsetHeight+1;
 		width = VicPopCal.offsetWidth;
 		height = VicPopCal.offsetHeight;
 		fToggleTags(point);
 		visibility = 'visible';
  	}
  	VicPopCal.focus();
}

function fPopupCalendar(popCtrl, dateCtrl) {

	gdCtrlFut = dateCtrl;
	var ppCtrl = popCtrl.value; // DateFormat = 'dd-Mon-yyyy hh:mm'


//	if (ppCtrl.length != 0)
//	{ 
//	  	fSetYearMon(ppCtrl.substr(7,4), getMonth(ppCtrl.substr(3,3)));
//	  	fSetTime(ppCtrl.substr(12,2), ppCtrl.substr(15,2));
//	}
//	else 
//	{
		fSetYearMon(giYear, giMonth);
		fSetTime(giHours, giMinutes);
//	}
  	var point = fGetXY(popCtrl);

	//only when body scroll is on
	point.x = point.x - 220;
	point.y = point.y - 25;
	  	
  	with (VicPopCal.style) {
   		left = point.x;
 		top = point.y+popCtrl.offsetHeight+1;
 		width = VicPopCal.offsetWidth;
 		height = VicPopCal.offsetHeight;
 		fToggleTags(point);
 		visibility = 'visible';
  	}

  	formatDate(giYear,giMonth,giDay);
  	VicPopCal.focus();
 	
}

function fPopCal(dateCtrl) 
{
	fPopCalendar(dateCtrl, dateCtrl);
}

function fPopCalTime(dateCtrl) 
{
    time.style.display='';
	timeReq = true;
	fPopCalendar(dateCtrl, dateCtrl);
}

function fPopCalTimeSeconds(dateCtrl) 
{
    time.style.display='';
	timeReq = true;
	secondsReq = true;
	fPopCalendar(dateCtrl, dateCtrl);
}

function fPopCalRHS(dateCtrl) 
{
	dateFormatYYYYDDMM = true;
	fPopCalendar(dateCtrl, dateCtrl);
}

function fPopCalFuture(dateCtrl) 
{
    timeReq = false;
	dateFormatYYYYDDMM = false;
//	compareDate = true;
	fPopupCalendar(dateCtrl, dateCtrl);
}


//var gMonths = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
var gMonths = new Array("01","02","03","04","05","06","07","08","09","10","11","12");
//
function getMonth(mon) {
	
	mm = -1;
	if (mon.indexOf(gMonths[0])!=-1) mm=1;
	if (mon.indexOf(gMonths[1])!=-1) mm=2;
	if (mon.indexOf(gMonths[2])!=-1) mm=3;
	if (mon.indexOf(gMonths[3])!=-1) mm=4;
	if (mon.indexOf(gMonths[4])!=-1) mm=5;
	if (mon.indexOf(gMonths[5])!=-1) mm=6;
	if (mon.indexOf(gMonths[6])!=-1) mm=7;
	if (mon.indexOf(gMonths[7])!=-1) mm=8;
	if (mon.indexOf(gMonths[8])!=-1) mm=9;
	if (mon.indexOf(gMonths[9])!=-1) mm=10;
	if (mon.indexOf(gMonths[10])!=-1) mm=11;
	if (mon.indexOf(gMonths[11])!=-1) mm=12;
	return mm;
}

//
function fHideCal() {

	var oE = window.event;
  	if ((oE.clientX>0) && (oE.clientY>0) && (oE.clientX<document.body.clientWidth) 
  			&& (oE.clientY<document.body.clientHeight)) {
 		var oTmp = document.elementFromPoint(oE.clientX,oE.clientY);
 		while ((oTmp.tagName!="BODY") && (oTmp.id!="VicPopCal")) {
  			oTmp = oTmp.offsetParent;
  		}
 		if (oTmp.id=="VicPopCal") return;
  	}
  	VicPopCal.style.visibility = 'hidden';
  	for (i in goSelectTag) {
 		goSelectTag[i].style.visibility = "visible";
 	}
  	goSelectTag.length = 0;
}

with (document) {

	write("<Div id='VicPopCal' onblur='fHideCal()' style='POSITION:absolute;visibility:hidden;border:1px outset;z-index:99;'>");
	write("<table border='0' cellpadding='0' bgcolor='"+tableBG+"'>");
	write("<TR>");
	write("<td valign='middle' align='center'><input type='button' name='PrevMonth' value='<' style='height:17;width:17;border:1px solid "+arrowBD+"; background-color: "+arrowBG+"' onClick='fPrevMonth()' onblur='fHideCal()'>");
	write("&nbsp;<select name='tbSelMonth' onChange='fUpdateCal(tbSelYear.value, tbSelMonth.value)' Victor='Won' onclick='self.event.cancelBubble=true' onblur='fHideCal()' style='background-color: "+listBG+"; font: "+fontN+"; vertical-align:middle;'>");
	
	for (i=0; i<12; i++) {
 		write("<option value='"+(i+1)+"'>"+gMonths[i]+"</option>");
 	}
	write("</SELECT>");
	write("&nbsp;<SELECT name='tbSelYear' onChange='fUpdateCal(tbSelYear.value, tbSelMonth.value)' Victor='Won' onclick='self.event.cancelBubble=true' onblur='fHideCal()' style='background-color: "+listBG+"; font: "+fontN+"; vertical-align:middle;'>");
	for(i=giYear-10;i<giYear+10;i++) {
 		write("<OPTION value='"+(i)+"'> "+(i)+"</OPTION>");
 	}
    write("</SELECT>");
    
	write("&nbsp;<input type='button' name='PrevMonth' value='>' style='height:17;width:17;border:1px solid "+arrowBD+"; background-color: "+arrowBG+"' onclick='fNextMonth()' onblur='fHideCal()'>");
	write("</td>");
	write("</TR><TR>");
	write("<td align='center'>");
	write("<DIV style='background-color:"+tableBD+"'><table width='100%' cellpadding='0' border='0'>");
	fDrawCal(giYear, giMonth, 10, '11');
	write("</table></DIV>");
	write("</td>");
	write("</TR><TR><TD align='center' style='font: "+fontB+"'>");
	write("<B style='cursor:hand' onclick='fSetDate(giYear,giMonth,giDay); self.event.cancelBubble=true' onMouseOver='this.style.color=gcToggleT' onMouseOut='this.style.color=0'>Today: " + giDay +  " " + gMonths[giMonth-1] +  ", "+giYear+"</B>");
	write("&nbsp;<input type='text' name='time' maxlength='8' style='height:17;width:55;border:1px solid #000000;font-family: Verdana, Arial; font-size: 11px;display:none'>");
	write("</TD></TR>");write("</TD></TR>");
	write("</TABLE></Div>");
}