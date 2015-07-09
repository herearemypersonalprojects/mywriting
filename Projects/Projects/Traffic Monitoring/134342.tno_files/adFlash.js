var gBannerHeight = 160;
var preSmaller = new smallAD('gifDove.gif',57,36,'#');

function ChangeBanner(arBanner, strLnk, strImg)
{
	if (arBanner.length==0)
		return;
	CurBanner++;
	if (CurBanner >= arBanner.length)
	{
		CurBanner=0;
	}
	document.links[BannerLnk].href= arBanner[CurBanner][1];
	document.images[strImg].src = arBanner[CurBanner][0];
	document.images[strImg].width = arBanner[CurBanner][2];
	document.images[strImg].height = arBanner[CurBanner][3];
}
function DisplayBanner(strAr, arBanner, strLnk, strImg,strClickTarget)
{
	CurBanner=Math.floor(Math.random()*12321) % arBanner.length;
	BannerLnk=document.links.length;
	document.write('<a name="',strLnk,'" href="', arBanner[CurBanner][1], '" target="',strClickTarget,'"><img name="',strImg,'" src="', arBanner[CurBanner][0], '" width=', arBanner[CurBanner][2], ' height=',arBanner[CurBanner][3], ' border=0></a>');
	setInterval('ChangeBanner('+strAr+', \''+strLnk+'\', \''+strImg+'\')', 7000);
} 

function smallAD (src,w,h,hyperLnk) {
	this.src = src;
	this.w = w;
	this.h = h;
	this.hyperLnk = hyperLnk
} 

function showDiv () {
	if 	(document.all('smallerAD').style.display == '')	
		document.all('smallerAD').style.display = 'none';	
	window.setTimeout (hideDiv, transflashshowtime);
	document.all('swfTest').width = flashWidth;
	document.all('swfTest').height = flashHeight ;
	document.all('swfTest').movie = gAdPath + aFlashFileName;
	var windowX = (window.screen.width - flashWidth)/2;
	cHeight = document.body.clientHeight; 
	var windowY = document.body.scrollTop + gBannerHeight + (cHeight - flashHeight) / 2;	
	document.all('divAD').style.top = windowY;
	document.all('divAD').style.left = windowX;	
	document.all('divAD').style.display = '';			
}

function showSmallDiv () {
	var leftPos = 0;
	if (window.screen.width==800)
		leftPos= 200;
	else leftPos = 300;
	cHeight = document.body.clientHeight; 
	var topPos = document.body.scrollTop + 15;		
	document.all('smallerLink').href = preSmaller.hyperLnk;
	document.all('smallerImg').src = gAdPath + preSmaller.src;
	document.all('smallerAD').style.top = topPos;
	document.all('smallerAD').style.left = leftPos;
	document.all('smallerAD').style.display = '';	
}

function setVar (_flashWidth, _flashHeight, _transflashshowtime, _aFlashFileName)
{
	flashWidth = _flashWidth;
	flashHeight = _flashHeight;
	transflashshowtime = _transflashshowtime;
	aFlashFileName = _aFlashFileName;
}

function startAD(){
	window.setTimeout (showDiv, transflashshowtime);	
}

function hideDiv (){
	document.all('divAD').style.display = 'none';
//	window.setTimeout (showDiv, transflashshowtime);		
}

