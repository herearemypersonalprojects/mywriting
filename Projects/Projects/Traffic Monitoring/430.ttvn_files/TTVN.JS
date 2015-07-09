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

function navBar( tableCellRef, hoverFlag, navStyle ) {
	if ( hoverFlag ) {
		switch ( navStyle ) {
			case 1:
//This defines rollover color
				tableCellRef.style.backgroundColor = '#407080';
				if ( tableCellRef.getElementsByTagName( 'a' ) ) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = 'white';
				}								
				break;
			default:
//tableCellRef.style.backgroundColor = '#ccc';
				if ( tableCellRef.getElementsByTagName ( 'a' )) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = '#E5E3DA';
				}
		}
	} else {
		switch ( navStyle ) {
			case 1:
//This defines return color
				tableCellRef.style.backgroundColor = '#E5E3DA';
				if ( tableCellRef.getElementsByTagName( 'a' ) ) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = 'black';
				}								
				break;
			default:
//tableCellRef.style.backgroundColor = '#ddd';
				if ( tableCellRef.getElementsByTagName ( 'a' )) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = '#E5E3DA';
				}
		}
	}
}

function TopNavBar( tableCellRef, hoverFlag, navStyle ) {
	if ( hoverFlag ) {
		switch ( navStyle ) {
			case 1:
//This defines rollover color
				tableCellRef.style.backgroundColor = '#0000AF';
				break;
			default:
//tableCellRef.style.backgroundColor = '#ccc';
				if ( tableCellRef.getElementsByTagName( 'a' ) ) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = '#0000AF';
				}
		}
	} else {
		switch ( navStyle ) {
			case 1:
//This defines return color
				tableCellRef.style.backgroundColor = '#5454E5';
				break;
			default:
//tableCellRef.style.backgroundColor = '#ddd';
				if ( tableCellRef.getElementsByTagName( 'a' ) ) {
					tableCellRef.getElementsByTagName( 'a' )[0].style.color = '#5454E5';
				}
		}
	}
}

// This script is broken by Kyo @ CoffeeStation. For TTVN.net only. Do not distribute.
var img=new Array()
img[0]='<img src="images/toplinkimg1.jpg" height="188" width="160" hspace="5">'
img[1]='<img src="images/toplinkimg2.jpg" height="188" width="160" hspace="5">'
img[2]='<img src="images/toplinkimg3.jpg" height="188" width="160" hspace="5">'
img[3]='<img src="images/toplinkimg4.jpg" height="188" width="160" hspace="5">'
img[4]='<img src="images/toplinkimg5.jpg" height="188" width="160" hspace="5">'

function gen(){
window.location.reload()
}
function gen2(){
if (document.layers){
appear()
setTimeout("window.onresize=gen",450)
}
}

function changeimg(whichcontent){

if (document.all||document.getElementById){
cross_el=document.getElementById? document.getElementById("descriptions"):document.all.descriptions
cross_el.innerHTML=''+whichcontent+''
}
else if (document.layers){
document.d1.document.d2.document.write(''+whichcontent+'')
document.d1.document.d2.document.close()
}

}

function appear(){
document.d1.visibility='show'
}
window.onload=gen2;

//Kyo's comment: Rolling func.
function high(which2){
theobject=which2
highlighting=setInterval("highlightit(theobject)",50)
}
function low(which2){
clearInterval(highlighting)
if (which2.style.MozOpacity)
which2.style.MozOpacity=0.3
else if (which2.filters)
which2.filters.alpha.opacity=75
}

function highlightit(cur2){
if (cur2.style.MozOpacity<1)
cur2.style.MozOpacity=parseFloat(cur2.style.MozOpacity)+0.1
else if (cur2.filters&&cur2.filters.alpha.opacity<100)
cur2.filters.alpha.opacity+=25
else if (window.highlighting)
clearInterval(highlighting)
}

// Topnavigation_rollvereffects
function newImage(arg) {
	if (document.images) {
		rslt = new Image();
		rslt.src = arg;
		return rslt;
	}
}
function changeImages() {
	if (document.images && (preloadFlag == true)) {
		for (var i=0; i<changeImages.arguments.length; i+=2) {
			document[changeImages.arguments[i]].src = changeImages.arguments[i+1];
		}
	}
}

var preloadFlag = false;
function preloadImages() {
	if (document.images) {
		over_docbailuu = newImage(/*URL*/'/images/docbailuu_2.gif');	
		preloadFlag = true;
	}
}

// End of topnavigation effects bar.