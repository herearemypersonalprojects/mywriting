function showDialog(vLink, vWidth, vHeight)
{
return showWindow(vLink, false, true, true, false, false, false, true, true, vWidth, vHeight, 0, 0);
}

function showModalDialog(vLink, vWidth, vHeight)
{
return showWindow(vLink, false, false, false, false, false, false, true, true, vWidth, vHeight, 0, 0);
}

function showWindow(vLink, vStatus, vResizeable, vScrollbars, vToolbar, vLocation, vFullscreen, vTitlebar, vCentered, vWidth, vHeight, vTop, vLeft)
{
var sLink = (typeof(vLink.href) == 'undefined') ? vLink : vLink.href;

winDef = '';
winDef = winDef.concat('status=').concat((vStatus) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('resizable=').concat((vResizeable) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('scrollbars=').concat((vScrollbars) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('toolbar=').concat((vToolbar) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('location=').concat((vLocation) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('fullscreen=').concat((vFullscreen) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('titlebar=').concat((vTitlebar) ? 'yes' : 'no').concat(',');
winDef = winDef.concat('height=').concat(vHeight).concat(',');
winDef = winDef.concat('width=').concat(vWidth).concat(',');

if (vCentered)
{
	winDef = winDef.concat('top=').concat((screen.height - vHeight)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWidth)/2);
}
else
{
	winDef = winDef.concat('top=').concat(vTop).concat(',');
	winDef = winDef.concat('left=').concat(vLeft);
}

open(sLink, '_blank', winDef);

if (typeof(vLink.href) != 'undefined')
{
	return false;
}
}

// this function toggles the status of a list
function toggle(list){
var listElementStyle=document.getElementById(list).style;
if (listElementStyle.display=="none"){
    listElementStyle.display="block";
}else{
    listElementStyle.display="none";
}
}

function writeTime(s)
{
	var mydate;
	if (s)
		mydate = new Date(s);
	else
		mydate = new Date();
	
	var year = mydate.getYear()
	if (year < 1000)
		year += 1900
	var month = mydate.getMonth() + 1
	if (month < 10)
		month = "0" + month
	var day = mydate.getDate()
	if (day < 10)
		day = "0" + day

	var dayw = mydate.getDay()
	
	var hour = mydate.getHours()
	if (hour < 10)
		hour = "0" + hour
	
	var minute=mydate.getMinutes()
	if (minute < 10)
		minute = "0" + minute
	var dayarray=new Array("Ch&#7911; Nh&#7853;t","Th&#7913; Hai","Th&#7913; Ba","Th&#7913; T&#432;","Th&#7913; N&#259;m","Th&#7913; S&#225;u","Th&#7913; B&#7843;y")
	document.write(dayarray[dayw]+", "+day+"/"+month+"/"+year+",&nbsp;"+hour+":"+minute + " GMT+7")
}