function adlistshow(AdList, AdName, CheckShow, ShowType, Interface, LimitWidth, LimitHeight, Extra){
	this.myid=AdName;
	this.checkshow=CheckShow;
	this.limitwidth=LimitWidth;
	this.limitheight=LimitHeight;
	this.showtype=ShowType;
	this.face=Interface;
	this.delay=15000;
	if (typeof(Extra) == 'undefined') {this.extra='';} else {this.extra=Extra;}
	switch (ShowType) {
		case 0:	case 4:	case 5: case 6: this.readtype=0; break;
		default: this.readtype=1; break;
	}
	var arTemp = new Array();
	var i, j, k, no, showcount;
	i=0;j=-1;k=0;no=-1;showcount=1;
	if (this.readtype==0){
		for (i=0,j=0;i<AdList.length;i++){
			if (this.checkShowThis(AdList[i])){
				arTemp[j]=AdList[i];
				j++;
			}
		}
	}
	else{
		for (i=0;i<AdList.length;i++){
			if (AdList[i][5]!=no){
				no=AdList[i][5];
				if (showcount>0) j++;
				showcount=0;
				k=0;
			}
			if (this.checkShowThis(AdList[i])){
				if (k==0) arTemp[j] = new Array();
				arTemp[j][k] = AdList[i];
				showcount++;
				k++;
			}
		}
	}
	this.itemcount=j+1;
	this.listvalue=arTemp;
	this.initialize();
}

adlistshow.prototype.initialize=function(){
	switch (this.showtype) {
		case 0: this.displayrotatorbanner(); break;
		case 1: case 7: this.displaylistbanner(); break;
		case 2: this.displayfloatbanner(0); break;
		case 3: this.displayfloatbanner(1); break;
		case 4: this.displayfloatbanner(2); break;
		case 5: this.displaypopupbanner(false); break;
		case 6: this.displaypopupbanner(true); break;
		default: break;
	}
}

adlistshow.prototype.checkLastObj=function(index){
	if (this.readtype==0){
		return true;
	}
	else {
		if (this.listvalue.length==index+1){
			return true;
		}
		else {
			return false;
		}
	}
}

adlistshow.prototype.displayrotatorbanner=function(){
	document.write('<table ', (this.limitwidth>0)?'width='.concat(this.limitwidth):'', ' cellspacing=0 cellpadding=0 border=0>');
	this.writerotatorbanner(this.listvalue,0);
	document.write('</table>');
}

adlistshow.prototype.displaylistbanner=function(){
	if (this.face==0){
		document.write('<table ', (this.limitwidth>0)?'width='.concat(this.limitwidth):'', ' cellspacing=0 cellpadding=0 border=0 align=center>');
	}
	for (var i=0;i<this.listvalue.length;i++){
		if (this.listvalue[i][0][5]<100){
			this.writerotatorbanner(this.listvalue[i],i,this.checkLastObj(i));
		}
		else {
			this.writerandombanner(this.listvalue[i],this.checkLastObj(i));
		}
	}
	if (this.face==0){
		document.write('</table>');
	}
}

adlistshow.prototype.displayfloatbanner=function(type){
	if (type==2){
		document.write('<DIV id="floatdiv',this.myid,'" style="position:absolute;overflow:hidden;left:-200;',(this.limitheight>0)?'height:'.concat(this.limitheight):'180',';',(this.limitwidth>0)?'width:'.concat(this.limitwidth):'110',';">');
		this.writebottomupbanner(this.listvalue);
	}
	else{
		document.write('<DIV id="floatdiv',this.myid,'" style="position:absolute;left:-200;',(this.limitwidth>0)?'width:'.concat(this.limitwidth):'110',';">');
		if (RelatedFolder==1000 && type==1) document.writeln('<iframe width="120" height="600" noresize scrolling=No frameborder=0 marginheight=0 marginwidth=0 src="http://rotator.adjuggler.com/servlet/ajrotator/196652/0/vh?z=newmediaww&dim=193431"><script language=JavaScript src="http://rotator.adjuggler.com/servlet/ajrotator/196652/0/vj?z=newmediaww&dim=193431&abr=$scriptiniframe"></script><noscript><a href="http://rotator.adjuggler.com/servlet/ajrotator/196652/0/cc?z=newmediaww"><img src="http://rotator.adjuggler.com/servlet/ajrotator/196652/0/vc?z=newmediaww&dim=193431&abr=$imginiframe" width="120" height="600" border="0"></a></noscript></iframe>');
		this.displaylistbanner();
	}
	document.write('</DIV>');
	FloatTopDiv('floatdiv'.concat(this.myid),type);
}

adlistshow.prototype.displaypopupbanner=function(type){
	vIndex = this.listvalue[0][2].lastIndexOf(',');
	vID = this.listvalue[0][2].substr(vIndex + 2);
	sLink = buildLink(vID,this.listvalue[0][1]);
	var arrPara = this.listvalue[0][2].split(",");	
	openPopup(this.listvalue[0][0], sLink, 'Advertisment', arrPara[2], arrPara[9], arrPara[8], screen.height - arrPara[9] - 80, (screen.width - 770)/2 + 410 - 5, type);
}

adlistshow.prototype.writerotatorbanner=function(obj,index,lastobj){
	obj.sort(randOrd);
	if (this.face==1) {
		document.write('<tr><td id="', this.myid, '_', index, '" style="padding:4px;">');
	}
	else {
		document.write('<tr><td id="', this.myid, '_', index, '">');
	}
	document.write(buildhtml(obj[0],this.limitwidth,this.limitheight));
	if (obj.length>1){
		var idtemp = ''.concat(this.myid).concat('_').concat(index);
		var delaytime = this.delay + Math.round(Math.random()*10000);
		var limitwidth = this.limitwidth;
		var limitheight = this.limitheight;
		setTimeout(function(){changebanner(obj,0,idtemp,delaytime,limitwidth,limitheight)}, delaytime);
	}
	document.write('</td></tr>');
	if (this.face==0){
		document.write('<tr><td height="3"></td></tr>');
	}
	if (this.face==1 && (!lastobj)) {
		document.write('<tr><td height="1" bgcolor="#808080"></td></tr>');
	}
}

adlistshow.prototype.writerandombanner=function(obj,lastobj){
	obj.sort(randOrd);
	for (var i=0;i<obj.length;i++){
		if (this.face==1) {
			document.write('<tr><td style="padding:4px;">');
		}
		else{
			document.write('<tr><td>');
		}
		document.write(buildhtml(obj[i],this.limitwidth,this.limitheight));
		document.write('</td></tr>');
		if (this.face==0){
			document.write('<tr><td height="3"></td></tr>');
		}
		if (this.face==1 && (i<obj.length-1)) {
			document.write('<tr><td height="1" bgcolor="#808080"></td></tr>');
		}
	}
}

adlistshow.prototype.writebottomupbanner=function(obj){
	obj.sort(randOrd);
	document.write('<DIV id="subdiv',this.myid,'" style="position:absolute;',(this.limitheight>0)?'top:'.concat(this.limitheight):'180',';',(this.limitwidth>0)?'width:'.concat(this.limitwidth):'115',';',(this.limitheight>0)?'height:'.concat(this.limitheight):'180',';">');
	document.write(buildhtml(obj[0],this.limitwidth,this.limitheight));
	var idtemp = 'subdiv'.concat(this.myid);
	var delaytime = this.delay + Math.round(Math.random()*10000);
	var limitwidth = this.limitwidth;
	var limitheight = this.limitheight;
	var delaytime=this.delay + Math.round(Math.random()*10000);
	setTimeout(function(){startbottomupbanner(obj,0,idtemp,delaytime,limitwidth,limitheight)}, 500);
	document.write('</DIV>');
}

function changebanner(obj,index,bannerid,delaytime,limitwidth,limitheight){
	if (index==obj.length-1) {index=0;} else {index++;}
	document.getElementById(bannerid).innerHTML=buildhtml(obj[index],limitwidth,limitheight);
	setTimeout(function(){changebanner(obj,index,bannerid,delaytime,limitwidth,limitheight)},delaytime);
}

function startbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight){
	var ftlObj = document.getElementById(bannerid)
	if (!ftlObj) return;
	var pY = parseInt(ftlObj.style.top);
	pY = pY - Math.round(parseInt(ftlObj.style.top)/8) - 1;
	if (pY>0){
		ftlObj.style.top = pY;
		setTimeout(function(){startbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight)},10);
	}
	else{
		ftlObj.style.top = 0;
		setTimeout(function(){endbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight)},delaytime);		
	}
}

function endbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight){
	var ftlObj = document.getElementById(bannerid)
	if (!ftlObj) return;
	var pY = parseInt(ftlObj.style.top);
	pY = pY + Math.round((limitheight-parseInt(ftlObj.style.top))/8) + 1;
	if (pY<limitheight){
		ftlObj.style.top = pY;
		setTimeout(function(){endbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight)},10);
	}
	else{
		ftlObj.style.top = limitheight;
		if (index==obj.length-1) {index=0;} else {index++;}
		document.getElementById(bannerid).innerHTML=buildhtml(obj[index],limitwidth,limitheight);
		setTimeout(function(){startbottomupbanner(obj,index,bannerid,delaytime,limitwidth,limitheight)},5000);		
	}
}

function buildhtml(obj,limitwidth,limitheight){
	if (!obj) {return ''}
	var sTemp = '';
	var sImageLink = (Left(obj[0],7).toLowerCase() == 'http://')?obj[0]:PageHost.concat(obj[0]);
	var imagewidth = (limitwidth<obj[3] && limitwidth>0)?limitwidth:obj[3];
	var imageheight = (limitheight<obj[4] && limitheight>0)?limitheight:obj[4];
	if (Right(obj[0],4).toLowerCase() == '.swf'){
		sTemp = sTemp.concat('<object classid="clsid:D27CDB6E-AE6D-11CF-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0" border="0" width=').concat(imagewidth).concat(' height=').concat(imageheight).concat('>');
		sTemp = sTemp.concat('<param name="movie" value="').concat(sImageLink).concat('?link=').concat(escape(buildLink(obj[2].substring(obj[2].lastIndexOf(',')+2),obj[1]))).concat('">');
		sTemp = sTemp.concat('<param name="quality" value="High">');
		sTemp = sTemp.concat('<embed src="').concat(sImageLink).concat('?link=').concat(escape(buildLink(obj[2].substring(obj[2].lastIndexOf(',')+2),obj[1]))).concat('" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width=').concat(imagewidth).concat(' height=').concat(imageheight).concat('>');
		sTemp = sTemp.concat('</object>');
	}
	else{
		if (obj[1] != '') {
			sTemp = '<a href="'.concat(obj[1]).concat('" onClick="return openMeExt(this, ').concat(obj[2]).concat(', 1)"><img src="').concat(sImageLink).concat('" border=0 width=').concat(imagewidth).concat(' height=').concat(imageheight).concat('></a></td>'); 
		}
		else {
			sTemp = '<img src="'.concat(sImageLink).concat('" border=0 width=').concat(imagewidth).concat(' height=').concat(imageheight).concat('></td>');
		}
	}
	return sTemp;
}

adlistshow.prototype.checkShowThis=function(obj){
	var checkShow=false;

	var iCheck = obj[6];
	
	if (iCheck == 3 && this.checkshow < 4) checkShow=true;
	else if (iCheck == this.checkshow || iCheck == 0) checkShow=true;
	
	//Truong hop dac biet Son nguyen chi quang cao o muc thue va cho thue nha
	if (RelatedFolder==9998)
		if (obj[1].indexOf('http://www.sonnguyenvn.com')>=0)
		{
			if (window.location.href.indexOf("c=12")>0 || window.location.href.indexOf("c=14")>0)
				{return true} else {return false}
		}

	if (checkShow && this.showtype==7){
		checkShow=false;
		if (typeof(dtSubjectDate) == 'undefined') return;
		var dtFromDate = new Date(obj[7]);
		var dtToDate = new Date(obj[8]);
		var iFromDiff = (dtSubjectDate.getTime()-dtFromDate.getTime())/1000;
		var iToDiff   = (dtToDate.getTime()-dtSubjectDate.getTime())/1000;
		if (iFromDiff<5*86400 && iFromDiff>=0 && iToDiff>5*86400 && iToDiff>=0){
			checkShow=true;
		}
	}
	else{
		return checkShow;
	}
	return checkShow;
}

//function randOrd(){ return (Math.round(Math.random())-0.5); } 
function randOrd(){ return (Math.random()*10000)-5000; } 

function openPopup(vImage, vLink, vTitle, vScrollbars, vWidth, vHeight, vTop, vLeft, vHide)
{
	if (typeof(vHide) == 'undefined'){
		vHide = false;
	}

	if (vHide){
		var pw = open('/Library/PopupUnder.Asp?vImage='.concat(escape(vImage)).concat('&vLink=').concat(escape(vLink)).concat('&vTitle=').concat(escape(vTitle)), 'Popup_'.concat(ReplaceAll(vTitle, ' ', '_')), 'status=no,resizable=no,scrollbars='.concat(vScrollbars).concat(',toolbar=no,location=no,fullscreen=no,titlebar=no,height=').concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',').concat('top=').concat(0).concat(',').concat('left=').concat(0));
		var LinkStr = vLink;
		window.focus();
		return pw;
	}
	else{
		return open('/Library/Popup.Asp?vImage='.concat(escape(vImage)).concat('&vLink=').concat(escape(vLink)).concat('&vTitle=').concat(escape(vTitle)), 'Popup_'.concat(ReplaceAll(vTitle, ' ', '_')), 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',').concat('top=').concat(vTop).concat(',').concat('left=').concat(vLeft));
	}
}

function openMeExt(vLink, vStatus, vResizeable, vScrollbars, vToolbar, vLocation, vFullscreen, vTitlebar, vCentered, vHeight, vWidth, vTop, vLeft, vID, vCounter)
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
	winDef = winDef.concat('height=').concat(vHeight-140).concat(',');
	winDef = winDef.concat('width=').concat(vWidth).concat(',');

	if (vCentered){
		winDef = winDef.concat('top=').concat((screen.height - vHeight)/2).concat(',');
		winDef = winDef.concat('left=').concat((screen.width - vWidth)/2);
	}
	else{
		winDef = winDef.concat('top=').concat(vTop).concat(',');
		winDef = winDef.concat('left=').concat(vLeft);
	}

	if (typeof(vCounter) == 'undefined'){
		vCounter = 0;
	}

	if (typeof(vID) == 'undefined')	{
		vID = 0;
	}
	
	if (vCounter){
		sLink = buildLink(vID,sLink);
	}

	open(sLink, '_blank', winDef);

	if (typeof(vLink.href) != 'undefined')	{
		return false;
	}
}

function buildLink(vID, vLink){
	return 'http://srv.ngoisao.net/Counter/?n='.concat(vID).concat('&u=').concat(escape(vLink)).concat('&r=').concat(Math.random());
}

function FloatTopDiv(divid,type){
	var startX, startY;
	if (type==1) {startX = document.body.clientWidth - 115;} else {startX = 0;}
	if (type==2) {startY = document.body.clientHeight;} else {startY = 0;}
	if (document.body.clientWidth < 980) {startX = -115};

	window.stayFloat=function(ftlObj,type)
	{
		var startX, startY;
		var ns = (navigator.appName.indexOf("Netscape") != -1);
		if (type==1) {startX = document.body.clientWidth - 115;} else {startX = 0;}
		if (document.body.clientWidth < 980) {
			ftlObj.style.display = 'none';
		} 
		else {
			ftlObj.style.display = '';
			
			if (document.documentElement && document.documentElement.scrollTop)
				var pY = ns ? pageYOffset : document.documentElement.scrollTop;
			else if (document.body)
				var pY = ns ? pageYOffset : document.body.scrollTop;

			if (type==2){
				startY = document.body.clientHeight-183;
			}
			else{
				if (document.body.scrollTop > 71){startY = 3} else {startY = 71};
			}
			ftlObj.y += (pY + startY - ftlObj.y)/8;
			ftlObj.style.left=startX;
			ftlObj.style.top=ftlObj.y;
		}
		setTimeout(function(){stayFloat(ftlObj,type)}, 15);
	}

	var ftlObj = document.getElementById?document.getElementById(divid):document.all?d.all[divid]:document.layers[divid];
	if(!ftlObj) return;
	ftlObj.x = startX;
	ftlObj.y = startY;
	stayFloat(ftlObj,type);
}

function Left(str, n){
	if (n <= 0)
	    return "";
	else if (n > String(str).length)
	    return str;
	else
	    return String(str).substring(0,n);
}

function Right(str, n){
    if (n <= 0)
       return "";
    else if (n > String(str).length)
       return str;
    else {
       var iLen = String(str).length;
       return String(str).substring(iLen, iLen - n);
    }
}

//===================================================================================================================
//ngocta Wed, 25/10/2006 15:37:25
var redirectServerURL='http://www.fpt.vn';
var clickThroughURL='http://www.yahoo.com.vn';	browser = new BrowserSniffer("Windows-Ie,Windows-Firefox,Mac-Firefox");
var img300x100="";//strExpandImage;
var swf300x250="";//strExpandFlash;
var swf300x100="";//strExpandFlash;
var propertyLocation ="UK";
var customSurveyLink="";
var adtest = false;
var key1='LOCATION_CODE'+'hp_'+'CLIENT_NAME'+'_'+'FORMAT_CODE';
var useLocationBasedVariables = false;
var txt1='';
var ad='bt1';
var survey='http://www.fpt.vn';
var surveyLnkTxt = "";//"None";
var repeatLnkTxt = "";//"Replay Ad";
var closeLnkTxt  = "";//"Close Ad";
var ht2x;		// HTML tag img when clicked CloseAd()
var ht1=''; 	// HTML big flash when mouse over image done
var CurrImg=''; // Current Image after setInterval()
var ClosingInProgress=false; // Close Done ?
var swf1 = swf300x250;
var swf3 = swf300x100;
var img1 = img300x100;
var r0 = redirectServerURL;
var url = clickThroughURL;
//////////////////
var ph=0;
var nv=0,fv=0,ie=0,done=0,cap=0,lan=0,auto=0,stay=0;
var red=r0.substring(0,r0.length-5);
var expires=2*24*3600*1000;
var domain='.yahoo.com';
var times=999;
var img1u=url;
var img2=img1;
var img2u=url;
var txt1u=url;
var swf1u=url;
var swf3u=url;
var bg='white';
var clip=300;
var dial= 0 ,nbv= 5 ;
var g,a,p,m,v;
var cook;
var tab1='';//<div id="adeast" style="text-align:center;">';
var tab2='';//</div>';
var lnk1='';//'<a hr'+'ef="javascript:replayAd();">'+repeatLnkTxt+'</a>';
var lnk4='';//'<a hr'+'ef="javascript:None()">'+surveyLnkTxt+' </a>';
var lnk5='';//'<a hr'+'ef="javascript:closeThisAd();">'+closeLnkTxt+'</a>';
var lnk7='';//'<a hr'+'ef="'+jp(4,'txt1',txt1u)+'">'+txt1+'</a>';
var lnk9='';//'<a hr'+'ef="'+jp(1,'img1',img1u)+'"><img src="'+img1+'" width=728 height=60 border=0></a>';
var ht3='';
var ht5='';//tab1+lnk7+'  '+lnk5+' - '+lnk4+tab2;
var ht6='';//tab1+lnk7+'  '+lnk1+' - '+lnk4+tab2;
var ht7='';//tab1+lnk7+tab2;
var intIndex=0;
var m_arrRefExpand=new Array();
var m_intInterval=0;
var m_intAdID=0;
var m_strRefURL='';

function BrowserSniffer(targettedBrowsers) {
	var browsersSupported = new Array("Firefox","Safari","Ie","Mac"); 
	for (var k=0; k < browsersSupported.length; k++) eval("this.is" + browsersSupported[k] + "=false;this.is" + browsersSupported[k] + "Targetted=false;"); 
	var browsersTargetted = targettedBrowsers.replace(/\s/g,"").split(",");this.isAdPlayable=false;this.agent=navigator.userAgent.toLowerCase();
	for (var i=0; i < browsersTargetted.length; i++) {
		var browsersTargettedInfo = browsersTargetted[i].replace(/\s/g,"").split("-");
		var targettedPlatform=browsersTargettedInfo[0];
		var targettedAgent=browsersTargettedInfo[1];
		//alert(targettedPlatform + " - " + this.agent + " - " + targettedAgent);
		if ( (this.agent.indexOf(targettedAgent.toLowerCase()) != -1) && (this.agent.indexOf(targettedPlatform.toLowerCase()) != -1) ){
			var thisVar = targettedAgent.substring(0,1).toUpperCase()+targettedAgent.substring(1,targettedAgent.length);
			if ( (this.agent.indexOf(targettedAgent.toLowerCase())!=-1) ) {this.isAdPlayable = true;eval("this.is"+thisVar+"=true;");break;}
		}
	}
	this._checkOnFlash = function() { 
		if (navigator.plugins && navigator.plugins.length) { 
			if ( (navigator.plugins["Shockwave Flash"]) || (navigator.plugins["Shockwave Flash 2.0"]) ) {var y;
				if (navigator.plugins["Shockwave Flash"]) y=this.isFlashVersion=navigator.plugins["Shockwave Flash"].description;
				if (navigator.plugins["Shockwave Flash 2.0"]) y=this.isFlashVersion=navigator.plugins["Shockwave Flash 2.0"].description;
				this.isFlashVersion=y.charAt(y.indexOf('.')-1);return true; 
			} else if (navigator.mimeTypes && navigator.mimeTypes.length) { 
				var x = navigator.mimeTypes['application/x-shockwave-flash']; if (x && x.enabledPlugin) return true; else return false; 
			} else return false; 
		} else if (this.isIe) { 
			document.write('<script language=vbscript>\nfunction vbflash()\non error resume next\ntest = IsObject(CreateObject("ShockwaveFlash.ShockwaveFlash." & 6 ))\nif test then\nvbflash = 1\nelse\nvbflash = 0\nend if\nend function\n<\/script>'); return vbflash();
		} else return false; 
	} 
	if (this.isAdPlayable) {this.isFlash = this._checkOnFlash();this.isAdPlayable = this.isFlash;}
}



function ShowExpandBanner(strExpandImage, strExpandFlash, strReplayAd, strCloseAd, strNone)	
{
	img300x100	 = strExpandImage;
	swf300x250	 = strExpandFlash;
	swf300x100	 = strExpandFlash;
	surveyLnkTxt = strNone;//"None";
	repeatLnkTxt = strReplayAd;//"Replay Ad";
	closeLnkTxt  = strCloseAd;//"Close Ad";
	
	img2=strExpandImage;
	swf1=strExpandFlash;
	swf3=strExpandFlash;
	CurrImg=strExpandImage;
	//alert(strExpandImage+','+strExpandFlash+','+ strReplayAd+','+ strCloseAd+','+ strNone)
	//////////////////////////////////////
	
	g=navigator;
	a=g.userAgent;
	p=g.appVersion;
	m=p.indexOf('MSIE');
	if (m!=-1 && a.indexOf('Win')!=-1) {
		v=parseFloat(p.substring(m+4));
		if (v>=nbv) {
			ie=v;
		}
	}
	if (browser.isAdPlayable) cap=1;
	lan=1;
	cook=getCook(key1);
	if (!cook) {
		setCook(key1,1);
		cook=getCook(key1);
		if (!cook) {
			done=1;
		} else {
			nv=1;
		}
	} else {
		cook++;
		if (cook%times) done=1;
		setCook(key1,cook);
		nv=cook;
		if (nv>9) nv=9;
	}
	if (cap&&!done) auto=0;
	tab1='';//<div id="adeast" style="text-align:center;">';
	tab2='';//</div>';
	lnk1='<a hr'+'ef="javascript:replayAd();">'+repeatLnkTxt+'</a>';
	lnk4='<a hr'+'ef="javascript:None()">'+surveyLnkTxt+' </a>';
	lnk5='<a hr'+'ef="javascript:closeThisAd();">'+closeLnkTxt+'</a>';
	lnk7='<a hr'+'ef="'+jp(4,'txt1',txt1u)+'">'+txt1+'</a>';
	lnk9='<a hr'+'ef="'+jp(1,'img1',img1u)+'"><img src="'+img1+'" width=728 height=60 border=0></a>';
	//var ht1='';
	ht1 += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width=728 height=120>';
	ht1 +=		'<param name=movie value="'+swf1+'?link='+GetSwfURL(m_intAdID, m_strRefURL)+'">';
	ht1 +=		'<param name=quality value=autohigh>';
	ht1 +=		'<param name=loop value=false>';
	ht1 +=		'<param name="allowScriptAccess" value="always" />';
	ht1 +=		'<EMBED ';
	ht1 +=			'src=' + swf1 +'?link='+GetSwfURL(m_intAdID, m_strRefURL)+ ' ';
	ht1 +=			'quality=high ';
	ht1 +=			'width=728 ';
	ht1 +=			'height=120 ';
	ht1 +=			'type="application/x-shockwave-flash" ';
	ht1 +=			'pluginspage="http://www.macromedia.com/go/getflashplayer" ';
	ht1 +=			'allowScriptAccess="always" ';
	ht1 +=		'></EMBED>';
	ht1 += '</object>';
	ht2x='<a hr'+'ef="'+r0+url+'" target="_blank"><img onmouseover="replayAd()" src="'+img2+'" width=728 height=60 border=0></a>';
	ht3='';
	ht3 += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width=728 height=120>';
	ht3 +=		'<param name=movie value="'+swf3+'?link='+GetSwfURL(m_intAdID, m_strRefURL)+'">';
	ht3 +=		'<param name=quality value=autohigh>';
	ht3 +=		'<param name=loop value=false>';
	ht3 +=		'<param name="allowScriptAccess" value="always" />';
	ht3 +=		'<EMBED ';
	ht3 +=			'src=' + swf3 +'?link='+GetSwfURL(m_intAdID, m_strRefURL)+ ' ';
	ht3 +=			'quality=high ';
	ht3 +=			'width=728 ';
	ht3 +=			'height=60 ';
	ht3 +=			'type="application/x-shockwave-flash" ';
	ht3 +=			'pluginspage="http://www.macromedia.com/go/getflashplayer" ';
	ht3 +=			'allowScriptAccess="always" ';
	ht3 +=		'></EMBED>';
	ht3 += '</object>';
	ht5=tab1+lnk7+'  '+lnk5+' - '+lnk4+tab2;
	ht6=tab1+lnk7+'  '+lnk1+' - '+lnk4+tab2;
	ht7=tab1+lnk7+tab2;
	if (adtest) {
		auto = true; 
	}
	if (cap) {
		if (auto) {
			flashload();
			temp = ('<div id=addiv style="position:relative;height:120;width:728">'+ht1+'</div><div id=lnkdiv style="text-align:center">'+ht5+'</div>');
			//temp = ('<div id=addiv style="position:relative;height:300;width:728;z-index:300">'+ht1+ht5+'</div>');

			document.write(temp);
		} else {
			temp = ('<div id=addiv style="position:relative;height:60;width:728">'+ht3+'</div><div id=lnkdiv style="text-align:center">'+ht6+'</div>');
			document.write(temp);
		}
	} else {
		document.write(lnk9+ht7);
	}
	// part 3 - FAD bits - place in page footer
	if (cap) {
		if (lan) {
			tt(1,'load_cap_lan');
		} else {
			tt(2,'load_cap_dial');
		}
	} else {
		tt(3,'load_nocap');
	}


	document.getElementById("addiv").style.backgroundColor='';
	document.getElementById("addiv").innerHTML=ht2x;
	ph = 120;	
	//alert(document.getElementById("addiv").innerHTML);
	//alert(ht1);
	//alert(ht3);
}
	
function closeThisAd() {ph=120;tt(12,'user_close');cleanUp2(0);}
function err(a,b,c) {var img=new Image;img.src='http://srd.yahoo.com/'+ad+'-err/'+escape(a)+','+escape(b)+','+escape(c)+'/*1';return true;}
function tt(n,s,u) {var img=new Image;img.src='http://s'+red.substring(7,red.length)+'N='+n+(cap+1)+(lan+1)+nv+'/id='+s+'/fv='+fv+'/'+Math.random()+'/*'+(u?u:'1');}
function jp0(r,s,u) {return red+'R='+r+(cap+1)+(lan+1)+nv+'/id='+s+(cap?'_cap':'_nocap')+(lan?'_lan':'_dial')+'/*'+u;}
function jp(r,s,u) {return jp0(r,s,u);}
function setCook(c,t) {var d=new Date;d.setTime(d.getTime()+expires);document.cookie=c+'=t='+t+'; expires='+d.toGMTString()+'; domain='+domain+'; path=/';}
function getCook(c) {var a,s,e,v;a=' '+document.cookie+';';s=a.indexOf(' '+c+'=');if (s==-1) return 0;s+=c.length+2;e=a.indexOf(';',s);v=a.substring(s,e).split('=');return v[1];}
function flashload() {var i= new Image;var a=Math.random();i.src="%rd%/%play%/id=adplay/"+a+"/*http://sg.yimg.com/i/sg/adv/test/space.gif";}
function flashreplay() {var i= new Image;var a=Math.random();i.src="%rd%/%play%/id=adreplay/"+a+"/*http://sg.yimg.com/i/sg/adv/test/space.gif";}
function openAd() {ph=60;tt(11,'user_play');stay=1;cleanUp2(1);}
function replayAd() {if (ClosingInProgress) return;ClosingInProgress=true;ph=60;flashreplay();tt(12,'user_replay');stay=1;cleanUp2(1);}
function closeAd() {ph=120;tt(12,'user_close');cleanUp2(0);}
function cleanUp2(open){
	if (open) {
		document.getElementById("addiv").style.backgroundColor=bg;
		document.getElementById("addiv").innerHTML='';
		ph = 60;
		moveIt(open);
	} else {
		document.getElementById("addiv").style.backgroundColor='';
		document.getElementById("addiv").innerHTML=ht2x;ph = 120;
		moveIt(open);
	}
}
function moveIt(open) {
	if (open){
		if (ph<120) {
				document.getElementById("addiv").style.pixelHeight=(ph+5);
				if (browser.isFirefox) document.getElementById("addiv").style.height=(ph+5)+"px";
				ph = ph+5;
				document.getElementById("addiv").style.clip='rect(0 728 '+clip+' 0)';
				clip+=5;
				setTimeout('moveIt(1)',1);
			} else {
				document.getElementById("addiv").innerHTML=ht1;
				//alert(ht1);
				document.getElementById("lnkdiv").innerHTML=ht5;
			}
		} else {
			if (ph>60) {
				document.getElementById("addiv").style.pixelHeight=(ph-5);
				if (browser.isFirefox) document.getElementById("addiv").style.height=(ph-5)+"px";
				ph = ph-5;
				document.getElementById("addiv").style.clip='rect(0 728 '+clip+' 0)';
				clip-=5;
				if (browser.isFirefox) document.getElementById("addiv").style.opacity= (Math.round((300-ph)/1.5))/100;
				document.getElementById("addiv").style.filter='alpha(opacity='+(Math.round((300-ph)/1.5))+')';
				setTimeout('moveIt(0)',1);
			} else {
				document.getElementById("addiv").style.filter='alpha(opacity=100)';
				document.getElementById("lnkdiv").innerHTML=ht6;
				//alert(ht6);
				if (ht2x!='')
				{
					document.getElementById("addiv").innerHTML='<img onmouseover="replayAd()" src="'+CurrImg+'" width=728 height=60 border=0>';
					//alert('collapsed');
					ClosingInProgress=false;
				}
			}
		}
	}

function swfAction(a) {
	if (a=='done') {
		tt(13,'done');
		cleanUp2(0);
	} else if (a=='click1') {
		location.href=r0+url;
	} else if (a=='click2') {
		location.href=r0+url;
	} else {
		location.href=r0+url;
	}
}

function MainWork(ImgFile, SwfFile, r)
{

	swf1 = SwfFile;
	swf3 = SwfFile;
	img1 = ImgFile;
	img2 = ImgFile;
	ht1='';
	CurrImg=ImgFile;
	
	var ph=0;
	var nv=0,fv=0,ie=0,done=0,cap=0,lan=0,auto=0,stay=0;
	var red=r0.substring(0,r0.length-5);
	var expires=2*24*3600*1000;
	var domain='.yahoo.com';
	var times=999;
	var img1u=url;
	var img2=img1;
	var img2u=url;
	var txt1u=url;
	var swf1u=url;
	var swf3u=url;
	var bg='white';
	var clip=300;
	var dial= 0 ,nbv= 5 ;
	var g,a,p,m,v;
	g=navigator;
	a=g.userAgent;
	p=g.appVersion;
	m=p.indexOf('MSIE');
	if (m!=-1 && a.indexOf('Win')!=-1) {
		v=parseFloat(p.substring(m+4));
		if (v>=nbv) {
			ie=v;
		}
	}
	if (browser.isAdPlayable) cap=1;
	lan=1;
	var cook=getCook(key1);
	if (!cook) {
		setCook(key1,1);
		cook=getCook(key1);
		if (!cook) {
			done=1;
		} else {
			nv=1;
		}
	} else {
		cook++;
		if (cook%times) done=1;
		setCook(key1,cook);
		nv=cook;
		if (nv>9) nv=9;
	}
	if (cap&&!done) auto=0;
	var tab1='';//<div id="adeast" style="text-align:center;">';
	var tab2='';//</div>';
	var lnk1='<a hr'+'ef="javascript:replayAd();">'+repeatLnkTxt+'</a>';
	var lnk4='<a hr'+'ef="javascript:None()">'+surveyLnkTxt+' </a>';
	var lnk5='<a hr'+'ef="javascript:closeThisAd();">'+closeLnkTxt+'</a>';
	var lnk7='<a hr'+'ef="'+jp(4,'txt1',txt1u)+'">'+txt1+'</a>';
	var lnk9='<a hr'+'ef="'+jp(1,'img1',img1u)+'"><img src="'+img1+'" width=728 height=60 border=0></a>';
	//var ht1='';
	ht1 += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width=728 height=120>';
	ht1 +=		'<param name=movie value="'+swf1+'?link='+GetSwfURL(m_intAdID, m_strRefURL)+'">';
	ht1 +=		'<param name=quality value=autohigh>';
	ht1 +=		'<param name=loop value=false>';
	ht1 +=		'<param name="allowScriptAccess" value="always" />';
	ht1 +=		'<EMBED ';
	ht1 +=			'src=' + swf1 + '?link=' +GetSwfURL(m_intAdID, m_strRefURL)+' ';
	ht1 +=			'quality=high ';
	ht1 +=			'width=728 ';
	ht1 +=			'height=120 ';
	ht1 +=			'type="application/x-shockwave-flash" ';
	ht1 +=			'pluginspage="http://www.macromedia.com/go/getflashplayer" ';
	ht1 +=			'allowScriptAccess="always" ';
	ht1 +=		'></EMBED>';
	ht1 += '</object>';
	ht2x ='<a hr'+'ef="'+r0+url+'" target="_blank"><img onmouseover="replayAd()" src="'+img2+'" width=728 height=60 border=0></a>';
	var ht3='';
	ht3 += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width=728 height=120>';
	ht3 +=		'<param name=movie value="'+swf3+'?link='+GetSwfURL(m_intAdID, m_strRefURL)+'">';
	ht3 +=		'<param name=quality value=autohigh>';
	ht3 +=		'<param name=loop value=false>';
	ht3 +=		'<param name="allowScriptAccess" value="always" />';
	ht3 +=		'<EMBED ';
	ht3 +=			'src=' + swf3 +'?link='+GetSwfURL(m_intAdID, m_strRefURL)+ ' ';
	ht3 +=			'quality=high ';
	ht3 +=			'width=728 ';
	ht3 +=			'height=60 ';
	ht3 +=			'type="application/x-shockwave-flash" ';
	ht3 +=			'pluginspage="http://www.macromedia.com/go/getflashplayer" ';
	ht3 +=			'allowScriptAccess="always" ';
	ht3 +=		'></EMBED>';
	ht3 += '</object>';
	var ht5=tab1+lnk7+'  '+lnk5+' - '+lnk4+tab2;
	var ht6=tab1+lnk7+'  '+lnk1+' - '+lnk4+tab2;
	var ht7=tab1+lnk7+tab2;
	if (adtest) {
		auto = true; 
	}
	if (cap) {
		if (auto) {
			flashload();
			temp = ('<div id=addiv style="position:relative;height:120;width:728">'+ht1+'</div><div id=lnkdiv style="text-align:center">'+ht5+'</div>');			
			document.getElementById('adeast').innerHTML=temp;
		} else {
			temp = ('<div id=addiv style="position:relative;height:60;width:728">'+ht3+'</div><div id=lnkdiv style="text-align:center">'+ht6+'</div>');
			document.getElementById('adeast').innerHTML=temp;
		}
	} else {
		document.write(lnk9+ht7);
	}
	// part 3 - FAD bits - place in page footer
	if (cap) {
		if (lan) {
			tt(1,'load_cap_lan');
		} else {
			tt(2,'load_cap_dial');
		}
	} else {
		tt(3,'load_nocap');
	}
}		
		
function None()
{
	document.getElementById('addiv').style.display='none';
	document.getElementById('lnkdiv').style.display='none';	
}

function ChangeExpandBanner()
{	
	intIndex++;
	if (intIndex>=m_arrRefExpand.length) intIndex=0;
	
	ClosingInProgress=false;
	m_intRefURL = m_arrRefExpand[intIndex][2];
	m_intAdID   = m_arrRefExpand[intIndex][3];	
	MainWork(m_arrRefExpand[intIndex][0], m_arrRefExpand[intIndex][1], Math.round(Math.random()*10000));
	document.getElementById("addiv").innerHTML=ht2x;		
	setTimeout("ChangeExpandBanner()",m_intInterval);	
}

function InitializeExpandBanner(arrRefExpand, strReplayAd, strCloseAd, strNone, intInterval)	
{
	m_arrRefExpand	= arrRefExpand;
	m_intInterval	= intInterval;
	
	if (m_arrRefExpand.length>0)
	{			
		m_strRefURL = m_arrRefExpand[0][2];
		m_intAdID   = m_arrRefExpand[0][3];
		ShowExpandBanner(m_arrRefExpand[0][0], m_arrRefExpand[0][1], strReplayAd, strCloseAd, strNone)	
		if (arrRefExpand.length>1)
			setTimeout("ChangeExpandBanner()",m_intInterval);	
	}
	else
	{
		document.getElementById('adeast').style.display = 'none';
	}
}
function GetSwfURL(intAdID, strURL)
{
	var strReturn=''
	if (Right(strURL,1)!="/")
		strURL+="/";
	if (Left(strURL,7)!="http://")
		strURL="http://"+strURL;
	strReturn="http://srv.ngoisao.net/Counter/?n="+intAdID+escape("&u=")+escape(strURL)
	return strReturn;
}


