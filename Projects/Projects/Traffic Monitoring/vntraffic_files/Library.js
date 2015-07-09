var fBrw=(navigator.userAgent.indexOf('MSIE')!= -1 && navigator.userAgent.indexOf('Windows')!= -1);
var fDSp=(typeof(document.getElementById)!='undefined');
var RefBanner = new Array();
var RefAdLogo = new Array();
var RefAdLBox = new Array();
var RefAdLBar = new Array();
var RefColumn = new Array();
var RefAdLeft = new Array();
var RefSmallB = new Array();
var RefAdMenu = new Array();
var RefDRight = new Array();
var RefDiLeft = new Array();
var RefFooter = new Array();

var RefAPopup = new Array();
var RefUPopup = new Array();
var RefExpand = new Array();
var RefAtDate = new Array();
var RefAtQuan = new Array();

var RefAdStay = 0;
var SkpFolder = true;
var CurBanner = 0;
var CurColumn = 0;
var CurSmallB = 0;
var CurAdMenu = 0;
var CurAdLeft = 0;
var CurFooter = 0;

var StAdRight = new Array();
var EnAdRight = new Array();
var CurARight = new Array();
var ARightLnk = new Array();

var BannerLnk = 0;
var SmallBLnk = 0;
var AdMenuLnk = 0;
var ColumnLnk = 0;
var AdLeftLnk = 0;
var FooterLnk = 0;

var LastChild = 0;

var LComplete = 0;
var sDomain	  = 'Ngoisao.net'; 
var iBuonchuyenItem = 0;

if (typeof(PageHost) == 'undefined')
{
	var PageHost = '';
}

if (typeof(SkipTopWindow) == 'undefined')
{
	if (window.parent!=window)
	{	
		alert('This website violate "The '+sDomain+' © Copyright Notice".\r\nClick OK to Access '+sDomain+'!');
		window.open(location.href, '_top', '');
	}
}

function loadRelatedXMLDoc(url, callbackFunction, desc, QUERY_STRING) {
    // branch for native XMLHttpRequest object
    if (window.XMLHttpRequest) {
        objRelated = new XMLHttpRequest();
		objRelated.onreadystatechange =	function(){
										// only if req shows "complete"
										if (objRelated.readyState == 4) {
											eval(callbackFunction);
										}
									}
		if(QUERY_STRING){
		    objRelated.open("POST", url, true);
			objRelated.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			objRelated.send(QUERY_STRING);
		}
        else{
	        objRelated.open("GET", url, true);
	        objRelated.send(null);
	    }
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        objRelated = new ActiveXObject("Microsoft.XMLHTTP");
        if (objRelated) {
        	objRelated.onreadystatechange =	function(){
											// only if req shows "complete"
											if (objRelated.readyState == 4) {
												eval(callbackFunction);
											}
										}
			if(QUERY_STRING){
			    objRelated.open("POST", url, true);
				objRelated.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				objRelated.send(QUERY_STRING);
			}
			else{
			    objRelated.open("GET", url, true);
			    objRelated.send(null);
			}
        }
    }
}

function setCookie(Name, Path, Expires, Value)
{
	var cstr = Name.concat('=').concat(Value);
	
	if (Path=='')
		path='/';

	cstr=cstr.concat(';path=').concat(Path);


	if (Expires=='')
		Expires=(new Date(2020, 11, 14)).toGMTString();

	document.cookie=cstr.concat(';expires=').concat(Expires);
}

function getCookie(Name, Default)
{
	var cookie = document.cookie;
	var ir = 0, ie = 0, sf = '', i = 0, j = 0;
	Name = Name.toLowerCase();

	if (typeof(Default) == 'undefined')
		Default = '';

	if (cookie.length == 0)
		return Default;

	if ((ir = Name.indexOf('.')) == -1)
	{
		if (cookie.substr(0, Name.length + 1).toLowerCase() == Name.concat('='))
		{
			if ((ie = cookie.indexOf(';')) != -1)
			{
				cookie = cookie.substr(0, ie);
			}
		}
		else
		{
			if ((ie = cookie.toLowerCase().indexOf('; '.concat(Name).concat('='))) == -1)
				return Default;

			cookie = cookie.substr(ie + 2);

			if ((ie = cookie.indexOf(';')) != -1)
			{
				cookie = cookie.substr(0, ie);
			}
		}
		sf = ';';
	}
	else
	{
		if ((i=cookie.toLowerCase().indexOf(Name.concat('='))) != -1)
		{
			if ((j = cookie.indexOf(';', i)) > i + Name.length + 1)
			{
				return ReplaceAll(unescape(cookie.substr(i + Name.length + 1, j - i - Name.length - 1)), '+', ' ');
			}
			else
			{
				j = cookie.length;
				return ReplaceAll(unescape(cookie.substr(i + Name.length + 1, j - i - Name.length - 1)), '+', ' ');
			}
		}

		var Root = Name.substr(0, ir);
		Name = Name.substr(ir + 1);

		if (cookie.substr(0, Root.length + 1).toLowerCase() == Root.concat('='))
		{
			if ((ie = cookie.indexOf(';')) != -1)
			{
				cookie = cookie.substr(0, ie);
			}
		}
		else
		{
			if ((ie = cookie.toLowerCase().indexOf('; '.concat(Root).concat('='))) == -1)
				return Default;

			cookie = cookie.substr(ie + 2);

			if ((ie = cookie.indexOf(';')) != -1)
			{
				cookie = cookie.substr(0, ie);
			}
		}

		cookie = cookie.substr(Root.length + 1);
		sf = '&';
	}

	if (cookie.substr(0, Name.length + 1).toLowerCase() == Name.concat('='))
	{
		ir = Name.length + 1;
	}
	else
	{
		if ((ir = cookie.toLowerCase().indexOf('&'.concat(Name).concat('='))) == -1)
			return Default;

		ir+=Name.length + 2;
	}

	if ((ie=cookie.indexOf(sf, ir)) == -1)
	{
		return ReplaceAll(unescape(cookie.substr(ir)), '+', ' ');
	}
	else
	{
		return ReplaceAll(unescape(cookie.substring(ir, ie)), '+', ' ');
	}
}

function ReplaceChar(iStr)
{
	var	r1=/&/g;
	var	r2=/ /g;
	var	r3=/"/g;

	iStr	=iStr.replace(r1, '%26');
	iStr	=iStr.replace(r2, '%20');
	iStr	=iStr.replace(r3, '%22');

	return iStr;
}

function CharReplace(iStr)
{
	var	r1=/%26/g;
	var	r2=/%20/g;
	var	r3=/%22/g;

	iStr	=iStr.replace(r1, '&');
	iStr	=iStr.replace(r2, ' ');
	iStr	=iStr.replace(r3, '"');

	return iStr;
}

function GetPostVariable(vName, vDef)
{
	var	str=location.href;
	var	pos=str.indexOf('?'.concat(vName).concat('='));

	if (pos==-1)
	{
		pos=str.indexOf('&'.concat(vName).concat('='));
		if (pos==-1) return vDef;
	}
	
	str=str.substring(pos + vName.length + 2);
	pos=str.indexOf('&');

	if (pos==-1)
	{
		pos=str.length;
	}	

	if (pos > 0)
	{
		str=str.substring(0, pos);
	}

	return (typeof(vDef)=='number') ? parseInt(str) : CharReplace(str);
}

function GoNothing()
{
}

function doStarTeenVote(iid)
{
	var vWH = 170;
	var vWW = 330;
	var vWN = 'StarTeenVote';
	winDef = 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vWH).concat(',').concat('width=').concat(vWW).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vWH)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWW)/2);
	newwin = open(('http://srv.ngoisao.net/user/starteen/vote/?iid=').concat(iid), vWN, winDef);
	newwin.focus();
}

function showStarTeenVote(iPoint)
{
	var rPoint = Math.floor(iPoint);
	var uPoint = Math.round((iPoint - rPoint) * 100);
	var iShown = 0;
	
	document.write('<Table border=0 cellspacing=0 cellpadding=0 width=\"100%\" align=center>');
	document.write('<tr><td align=right>');
	for (i = 1; i < rPoint + 1; i++)
	{
		document.write('<img src=\"/Images/Vote/Star06.gif\"  border=0>');
		iShown = i;
	}
	
	if (uPoint > 0 && uPoint < 26) 
	{
		document.write('<img src=\"/Images/Vote/Star02.gif\"  border=0>');	
		iShown++;
	}	
	if (uPoint > 25 && uPoint < 51) 
	{
		document.write('<img src=\"/Images/Vote/Star03.gif\"  border=0>');	
		iShown++;
	}
	
	if (uPoint > 50 && uPoint < 76) 
	{
		document.write('<img src=\"/Images/Vote/Star04.gif\"  border=0>');	
		iShown++;		
	}
	if (uPoint > 75 && uPoint < 100) 
	{
		document.write('<img src=\"/Images/Vote/Star05.gif\"  border=0>');	
		iShown++;		
	}
	
	for (i = iShown; i < 5; i++)
	{
		document.write('<img src=\"/Images/Vote/Star01.gif\"  border=0>');	
	}
	document.write('</td>');
	document.write('<td width=5 style=\"padding-left:2; padding-right:10;\" class=\"Time\" align=center>');
	document.write('(' + iPoint + ')');	
	document.write('</td></tr>');		
	document.write('</Table>');
}

function doMissVote(iid)
{
	var vWH = 130;
	var vWW = 250;
	var vWN = 'StarTeenVote';
	winDef = 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vWH).concat(',').concat('width=').concat(vWW).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vWH)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWW)/2);
	newwin = open(('http://srv.ngoisao.net/user/MissVote/?iid=').concat(iid), vWN, winDef);
	newwin.focus();
}

function AddBreakSpace(Pixel)
{
	if (typeof(Pixel)=='undefined')
	{
		Pixel=3;
	}
	document.writeln('<table cellspacing=0 cellpadding=0 border=0 width="100%" bgcolor="#FFFFFF"><tr><td height=', Pixel, '><img src="/Images/White.gif" border=0 height=1 width=1></td></tr></table>');
}

function DisplayCopyright(showType)
{
	if (typeof(showType)=='undefined')
	{
		showType=1;
	}

	document.writeln('<table cellspacing=0 cellpadding=0 border=0>');
	if (showType)
	{
		document.writeln('<tr><td width=1 class=Symbol><b>&#169;&nbsp;</b></td><td class=Copyright nowrap><b><font color=#000000>Copyright 1997-2002 '+sDomain+'</font></b>, All rights reserved. <a href="', PageHost, '/ContactUs/?d=webmaster@'+sDomain+'">Contact us</a><td></tr>');
		document.writeln('<tr><td height=1 colspan=2 class=BreakLine>&nbsp;</td></tr>');
		document.writeln('<tr><td width=1 class=Symbol><b>&#174;&nbsp;</b></td><td class=Copyright nowrap>Y&#234;u c&#7847;u m&#7885;i t&#7893; ch&#7913;c khi s&#7917; d&#7909;ng th&#244;ng tin c&#7911;a '+sDomain+' ph&#7843;i ghi r&#245; ngu&#7891;n tin.</td></tr>');
	}
	else
	{
		document.writeln('<tr><td width=1 class=Symbol><b>&#169;</b></td><td class=Copyright nowrap><b><font color=#000000>Copyright 1997-2002 '+sDomain+'</font></b>, All rights reserved.<td></tr>');
	}
	document.writeln('</table>');
}

function AddHeader(Name, Header, Buttons, Symbol, AddChildTable)
{
	document.writeln('<table width="100%" border=0 cellspacing=0 cellpadding=1 bgcolor="#1E5C99"><tr><td>');

	if (Header!='')
	{
		document.writeln('<table width="100%" border=0 cellspacing=0 cellpadding=0>');
		document.writeln('<tr>');

		if (typeof(Symbol)!='undefined')
		{
			document.writeln('<td height=16 class=BoxHeader><img src="', Symbol, '" border=0></td>');
		}

		document.writeln('<td height=16 width="100%" align=left class=BoxHeader>&nbsp;', Header, '</td>');

		if ((Buttons & 1) && fDSp)
		{
			document.write('<td width=15 align=right>');
			document.write('<a href="JavaScript:ItemMinimize(\x27', Name, '\x27)">');
			document.write('<img src="/Images/min.gif" name="IDI_', Name, '" border=0 alt="Minimize | Maximize">');
			document.write('</a></td>');
		}

		document.writeln('</tr></table>');
	}

	document.writeln('<div class=BreakLine id="IDM_', Name, '">');
	if (typeof(AddChildTable)=='undefined')
	{
		document.writeln('<table align=center width="100%" cellspacing=0 cellpadding=0 border=1>');
		LastChild = 1;
	}
	else
	{
		LastChild = 0;
	}
	return true;
}

function AddFooter()
{
	if (LastChild)
	{
		document.writeln('</table></div></td></tr></table>');
	}
	else
	{
		document.writeln('</div></td></tr></table>');
	}
}

function ItemMinimize(Name)
{
	if (!fDSp)
	{
		return;
	}

	var MItem=document.getElementById('IDM_'.concat(Name));
	var Image=document.getElementById('IDI_'.concat(Name));
	
	if (MItem.innerHTML=='')
	{
		MItem.innerHTML = MItem.abbr;
		MItem.abbr = '';
		Image.src='/Images/min.gif';
	}
	else
	{
		MItem.abbr = MItem.innerHTML;
		MItem.innerHTML = '';
		Image.src='/Images/max.gif';
	}
}

function SetParameter(pFile, pName, pVal)
{
	if ((cPost=pFile.indexOf('&'.concat(pName).concat('=')))==-1)
		cPost=pFile.indexOf('?'.concat(pName).concat('='));

	if (cPost >= 0)
	{
		if ((pPost=pFile.indexOf('&', cPost + 1))==-1)
		{
			pFile=pFile.substring(0, cPost + pName.length + 2).concat(pVal);
		}
		else
		{
			pFile=pFile.substring(0, cPost + pName.length + 2).concat(pVal).concat(pFile.substr(pPost));
		}
	}
	else
	{
		if (pFile.indexOf('?')==-1)
		{
			pFile=pFile.concat('?').concat(pName).concat('=').concat(pVal);
		}
		else
		{
			pFile=pFile.concat('&').concat(pName).concat('=').concat(pVal);
		}
	}

	return pFile;
}

function ReverseFolderByDate()
{
	Ryear = document.Reverse.fYear.options[document.Reverse.fYear.selectedIndex].value;
	Rmonth = document.Reverse.fMonth.options[document.Reverse.fMonth.selectedIndex].value;
	Rday = document.Reverse.fDay.options[document.Reverse.fDay.selectedIndex].value;

	for (; Rday > 0; Rday--)
	{
		Rdate = new Date(Ryear, Rmonth - 1, Rday);
		if (Rdate.getDate() == Rday)
		{
			break;
		}
	}

	LastDate = Ryear.concat('/').concat(Rmonth).concat('/').concat(Rday).concat(' 23:59:59');
	location.replace(CurrentFolder.concat('/?d=').concat(escape(LastDate)));
}

function ShowNextFolderItem(LastDate)
{
	location.href = SetParameter(location.href, 'd', escape(LastDate));
}

function UnderConst()
{
	alert('Sorry!\nThis Page is under construction!\nPlease try latter!');
}

function openMe(url, inNew, winDef)
{
	if (url == '')
		return;

	if (typeof(inNew)=='undefined')
		inNew = 0;

	if (typeof(winDef)=='undefined')
		winDef = 'scrollbars=yes,status=yes,toolbar=yes,location=yes,menubar=yes,resizable=yes,height=300,width=400,top='.concat((screen.height - 400)/2).concat(',left=0');

	if (inNew)
	{
		open(url, 'Advertising', winDef);
	}
	else
	{
		location.href = url;
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

	if (typeof(vCounter) == 'undefined')
	{
		vCounter = 0;
	}

	if (typeof(vID) == 'undefined')
	{
		vID = 0;
	}
	
	if (vCounter)
	{
		sLink = 'http://srv.'+sDomain+'/Counter/?n='.concat(vID).concat('&u=').concat(escape(sLink)).concat('&r=').concat(Math.random());
	}

	open(sLink, '_blank', winDef);

	if (typeof(vLink.href) != 'undefined')
	{
		return false;
	}
}


function openPlayer(vLink, vHeight, vWidth)
{
	var sLink = (typeof(vLink.href) == 'undefined') ? vLink : vLink.href;

	if (sLink == '') return false;
	
	winDef = '';
	winDef = 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=no,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vHeight)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWidth)/2);
	open(vLink, 'NhacsoListen', winDef);

	if (typeof(vLink.href) != 'undefined')
	{
		return false;
	}
}

function openImage(vLink, vHeight, vWidth, vSbar)
{
	var sLink = (typeof(vLink.href) == 'undefined') ? vLink : vLink.href;
	var sSbar = (typeof(vSbar) == 'undefined') ? "no" : vSbar;
	if (sLink == '')
	{
		return false;
	}

	winDef = 'status=no,resizable=no,scrollbars=' + sSbar + ',toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vHeight)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWidth)/2);
	newwin = open('', '_blank', winDef);

	newwin.document.writeln('<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">');
	newwin.document.writeln('<a href="" onClick="window.close(); return false;"><img src="', sLink, '" alt="', (fBrw) ? '&#272;&#243;ng l&#7841;i' : 'Dong lai', '" border=0></a>');
	newwin.document.writeln('</body>');

	if (typeof(vLink.href) != 'undefined')
	{
		return false;
	}
}


function SetFont()
{
	if (fBrw)
	{
		rs=window.showModalDialog('/SetFont.htm', '', 'dialogHeight:215px;dialogWidth:385px;status:no;help:no');
		if (typeof(rs)!='undefined')
		{
			if (rs)
			{
				location.reload(true);
			}
		}
	}
	else
	{
		open('/SetFont.htm', 'SetFont', 'toolbar=no,height=185,width=370,top='.concat((screen.height - 185)/2).concat(',left=').concat((screen.width - 370)/2));
	}
}

function PageSet(vPage)
{
	location.replace(SetParameter(location.href, 'p', vPage));
}


function ShowHeartBanner(sType, vAd)
{
	// if (RefColumn.length==0)
	// if (RefSmallB.length==0)
	// if (RefAdMenu.length==0)
	// if (RefAdLeft.length==0)
	if (sType==1)
		if (RefColumn.length>0)
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;">');
			var alRefColumn = new adlistshow(RefColumn,'HeartBanner1',0,  0 ,0,420,58);
			document.writeln('</td></tr></Table>');
		}
		else
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;"><img src="/Images/Blank.gif" border=0></td></tr></Table>');
			return;
		}
	
	if (sType==2)
		if (RefSmallB.length>0)
		{	
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;">');
			var alRefSmallB = new adlistshow(RefSmallB,'HeartBanner2',0,  0 ,0,420,58);
			document.writeln('</td></tr></Table>');
		}
		else
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;"><img src="/Images/Blank.gif" border=0></td></tr></Table>');
			return;
		}
		
	if (sType==3)
		if (RefAdMenu.length>0)
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;">');
			var alRefAdMenu = new adlistshow(RefAdMenu,'HeartBanner3',0,  0 ,0,420,58);
			document.writeln('</td></tr></Table>');
		}
		else
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;"><img src="/Images/Blank.gif" border=0></td></tr></Table>');
			return;
		}
	
	if (sType==4)
		if (RefAdLeft.length>0 && sType==4)
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;">');
			var alRefAdLeft = new adlistshow(RefAdLeft,'HeartBanner4',0,  0 ,0,420,58);
			document.writeln('</td></tr></Table>');
		}
		else
		{
			document.writeln('<Table cellspacing=0 cellpadding=0 border=0 align=center><tr><td style="padding-bottom:5; padding-top:5;"><img src="/Images/Blank.gif" border=0></td></tr></Table>');
			return;
		}
}


function ShowAdBox()
{
	if (RefAdLBox.length==0)
		return;

	document.writeln('<table width="100%" cellspacing=0 cellpadding=0 border=0 bgcolor=red>');
	document.writeln('<tr>');
	document.writeln('<td width=190>');
	if (RefAdLBox[0][1] != '')
	{
		document.writeln('<a href="javascript:openMe(\'', RefAdLBox[0][1], '\', ', RefAdLBox[0][2], ')"><img src="', RefAdLBox[0][0], '" border=0></a>');
	}
	else
	{
		document.writeln('<img src="', RefAdLBox[0][0], '" border=0>');
	}
	document.writeln('</td>');
	document.writeln('<td width=1 bgcolor="#FFFFFF"><img src="/Images/white.gif" border=0></td>');
	document.writeln('<td><a href="/Advertising/"><img src="/Images/Advertising.gif" border=0></a></td>');
	document.writeln('</tr>');
	document.writeln('</table>');
}


function ShowAdLogoNew(sType)
{
	if (typeof(sType)=='undefined')
		sType = 2;

	switch (sType)
	{
	case 1:
		ShowAdLogoLeft();
		break;
	case 2:
		ShowAdLogoRight();
		break;
	}
}

function ShowAdLogoLeft()
{
	if (RefAdLeft.length==0)
	{
		document.write('<table width=130 cellspacing=0 cellpadding=0 border=0 bgcolor="#808080">');
		document.write('<tr>');
		document.write('<td valign=top>');
		document.write('<table cellspacing=1 cellpadding=4 border=0 width="100%">');

		document.write('<tr><td height=60 align=center bgcolor="#ffffff"><a href="http://'+sDomain+'/Advertising" class=AdTitle>D&#224;nh cho <BR>Qu&#7843;ng c&#225;o</a></td></tr>');
		
		document.write('</table>');	
		document.write('</td>');
		document.write('</tr>');
		document.write('</table>');
		
		return;
	}

	document.writeln('<table cellspacing=0 cellpadding=0 border=0>');

	for (i=0; i < RefAdLeft.length; i++)
	{
		if (i > 0)
		{
			document.writeln('<tr><td height=2><img src="/Images/white.gif" border=0 height=1 width=1></td></tr>');
		}		

		document.writeln('<tr><td>');

		w = 180;
		h = RefAdLeft[i][4];

		if (RefAdLeft[i][1] != '')
		{
			document.writeln('<a href="', RefAdLeft[i][1], '" onClick="return openMeExt(this, ', RefAdLeft[i][2], ', 1)"><img align=center src="', PageHost.concat(RefAdLeft[i][0]), '" width=', w, ' height=', h, ' border=0></a>');
		}
		else
		{
			document.writeln('<img align=center src="', PageHost.concat(RefAdLeft[i][0]), '" width=', w, ' height=', h, ' border=0>');
		}
		document.writeln('</td></tr>');
	}

	document.writeln('</table>');
}

function ShowDivLogoLeft()
{
	if (RefDiLeft.length==0) return;
	var alLeftFloatBanner = new adlistshow(RefDiLeft,'LeftFloatLogo',0,2,0,110,500);
}

function ShowDivLogoRight()
{
	if (RefDRight.length==0) return;	
	var alRightFloatBanner = new adlistshow(RefDRight,'RightFloatLogo',0,3,0,110,500);
}

function ShowAdLogoRight(vAd)
{
	document.writeln('<table cellspacing=0 cellpadding=0 border=0>');
	document.writeln('<tr><td><a href="/Advertising/"><img src="/Images/Advertisment.gif" border="0"></a></td></tr>');
	document.writeln('</table>');

	if (RefAdLBox.length==0 && RefAdLogo.length==0)
	{
		document.writeln('<table cellspacing=1 cellpadding=1 border=0 width="160" bgcolor=\"#163FA0\">');
		document.write('<tr><td align=center height=64 bgcolor="#FFFFFF"><a href="http://'+sDomain+'/Advertising" class=clsAdvertising>&nbsp;</a></td></tr>');
		document.write('<tr><td height=5 bgcolor=#163FA0></td></tr>');							
		document.writeln('</table>');
		document.writeln('<table cellspacing=0 cellpadding=0 border=0 width="100%"><tr><td height=5></td></tr></table>');
		return;
	}	
	
	if (RefAdLBox.length>0 || RefAdLogo.length>0)	
	{
		document.writeln('<table cellspacing=0 cellpadding=0 border=0 width="160" bgcolor=\"#163FA0\">');	
		document.writeln('<tr><td height=3 bgcolor=#DAE2EF></td></tr>');
	}
	
	if (RefAdLBox.length>0)
	{
		document.writeln('<tr><td bgcolor=#DAE2EF>');
		var alBigIcon = new adlistshow(RefAdLBox,'BigIcon',0,1,0,130,130);
		document.writeln('</td></tr>');
	}	
	if (RefAdLogo.length>0)
	{
		document.writeln('<tr><td bgcolor=#DAE2EF>');
		var alSmallIcon = new adlistshow(RefAdLogo,'SmallIcon',0,1,0,130,60);	
		document.writeln('</td></tr>');
	}
	if (RefAdLBox.length>0 || RefAdLogo.length>0)	
	{
		document.write('<tr><td height=5 bgcolor=#163FA0></td></tr>');
		document.write('<tr><td height=5 bgcolor=#FFFFFF></td></tr>');
	}	
	document.writeln('</table>');
}

function UnicodeSet(iStr)
{
	for (i=0, oStr=''; i < iStr.length; i++)
	{
		switch ((j=iStr.charCodeAt(i)))
		{
		case 34:
			oStr=oStr.concat('&quot;');
			break;
		case 38:
			oStr=oStr.concat('&amp;');
			break;
		case 39:
			oStr = oStr.concat('&#39;');
			break;
		case 60:
			oStr = oStr.concat('&lt;');
			break;
		case 62:
			oStr = oStr.concat('&gt;');
			break;
		default:
			if (j < 32 || j > 127 || j==34 || j==39)
			{
				oStr=oStr.concat('&#').concat(j).concat(';');
			}
			else
			{
				oStr=oStr.concat(iStr.charAt(i)); 
			}
			break;
		}
	}
	
	return oStr;
}

function UnicodeGet(iStr)
{
	for (i=0, oStr=''; i < iStr.length; )
	{
		if (iStr.charCodeAt(i)==38)
		{
			if (iStr.charCodeAt(i + 1)==35)
			{
				p=iStr.indexOf(';', i  + 2);
				if (p!=-1)
				{
					if (p - i <= 7)
					{
						if (isFinite(iStr.substr(i + 2, p - i - 2)))
						{
							oStr = oStr.concat(String.fromCharCode(iStr.substr(i + 2, p - i - 2)));
							i = p + 1;
							continue;
						}
					}
				}
			}
			else
			{
				p=iStr.indexOf(';', i  + 1);
				if (p!=-1)
				{
					switch (iStr.substr(i + 1, p - i - 1))
					{
					case 'amp':
						oStr = oStr.concat('&');
						i = p + 1;
						break;
					case 'quot':
						oStr = oStr.concat('"');
						i = p + 1;
						break;
					case 'lt':
						oStr = oStr.concat('<');
						i = p + 1;
						break;
					case 'gt':
						oStr = oStr.concat('>');
						i = p + 1;
						break;
					}
				}
			}
		}
	
	
		oStr=oStr.concat(iStr.charAt(i));
		i++;
	}
	
	return oStr;
}

function SearchMe(s, a)
{
	while (s.length > 0 && s.charAt(0) <= ' ')
	{
		s = s.substr(1);
	}

	while ((i=s.length) > 0 && s.charAt(i - 1) <= ' ')
	{
		s = s.substr(0, i - 1);
	}

	if (s=='')
	{
		document.Search.TSearch.value = s;
		return false;
	}
	
	f = GetPostVariable('r', RelatedFolder);
	s = escape(UnicodeSet(s));
	r = '/Search/?p=1&r='.concat(f).concat('&a=').concat(a).concat('&s=').concat(s);

	if (location.pathname.toLowerCase()=='/search/')
	{
		location.replace(r);
	}
	else
	{
		location.href=r;
	}
	return false;
}

function SearchOnFocus(field)
{
	if(field.value=='Search')
	{
		field.value = '';
	}
}

function SearchOnBlur(field)
{
	if(field.value=='')
	{
		field.value='Search';
	}
}

function ShowSearch()
{
	if ((s=GetPostVariable('s', ''))!='')
	{
		s = unescape(s);
	}

	s=UnicodeGet(s);

	document.writeln('<table height=20 cellspacing=0 cellpadding=0 border=0 align=center width="100%">');
	document.writeln('<form method="POST" name="Search" onSubmit="return SearchMe(document.Search.TSearch.value, 1)">');
	document.writeln('<tr>');
	document.writeln('<td align=right valign=top><div><input type="text" name="TSearch" size=9 value="Search" class=SearchBox onfocus="SearchOnFocus(this)" onkeyup="initTyper(this)" onblur="SearchOnBlur(this)"></div></td>');
	document.writeln('<td class=BreakLine width=3>&nbsp;</td>');
	document.writeln('<td valign=top><a href="javascript:SearchMe(document.Search.TSearch.value, 1)"><img src="/Images/Go.gif" border=0></a></td>');
	document.writeln('<td class=BreakLine width=3>&nbsp;</td>');
	document.writeln('</tr>');
	document.writeln('</form>');
	document.writeln('</table>');

	if (s!='')
	{
		document.Search.TSearch.value = s;
	}
}

function CheckThisVote(field)
{
	form = field.form;
	if (field.checked)
	{
		form.fvotefor.value = field.value;
	}
	else
	{
		form.fvotefor.value = '';
		return;
	}

	for (i=0; i < form.elements.length - 2; i++)
	{
		if(form.elements[i].type=='checkbox')
			if (form.elements[i] != field)
				if (form.elements[i].checked)
					form.elements[i].checked = false;
	}
}

function SubmitVote(sform, saction)
{
	if (saction==0)
	{
		if (sform.fvotefor.value=='')
		{
			alert('Hay chon mot trong cac muc truoc khi bieu quyet');
			return;
		}
	}

	var form = sform;
	var j = 0
	for (i=0; i < form.elements.length - 2; i++)
		{
			if(form.elements[i].type=='checkbox'){
				j = j + 1
			}
		}
	var sheight = (j * 40) + 80;
	if (sheight < 250){
		sheight = 250;
	}
	open('', sform.name, 'scrollbars=yes,resizeable=no,locationbar=no,width=550,height='+sheight+',left='.concat((screen.width - 500)/2).concat(',top=').concat((screen.height - 250)/2));
	sform.faction.value = saction;
	sform.action = 'http://srv.'+sDomain+'/User/Vote/Default.Asp';
	sform.submit();
}

function AddVote(SubjectID, PageID, VoteID, Align, VoteTitle, Color, BgColor, Width, NumItem, ItemArray, Description, Column)
{
	document.writeln('<table width="', Width, '" border=0 cellspacing=0 cellpadding=1 bgcolor="#B3B3B3" ', (Align=='') ? '' : ' align='.concat(Align), '>');
	if (VoteTitle!='')
	{
		document.writeln('<tr><td><Table bgcolor=',BgColor,' cellspacing=0 cellpadding=3 width="100%"><tr><td><p class=BoxTitle style="margin-left: 3; color: ', Color, '">', VoteTitle, '</p></td></tr></Table></td></tr>');
	}

	if (typeof(Description)=='undefined')
	{
		Description = '';
	}

	if (typeof(Column)=='undefined')
	{
		Column = 1;
	}

	document.writeln('<tr>');
	document.writeln('<form method="POST" target="Frm_', VoteID, '" name="Frm_', VoteID, '">');
	document.writeln('<td>');
	document.writeln('<table border=0 cellpadding=0 cellspacing=0 width="100%" bgcolor="#E6DECD">');

	document.writeln('<input type="hidden" name="fsubjectid" value=', SubjectID, '>');
	document.writeln('<input type="hidden" name="fpageid" value=', PageID, '>');
	document.writeln('<input type="hidden" name="fvoteid" value=', VoteID, '>');
	document.writeln('<input type="hidden" name="fvotetitle" value="', ReplaceAll(VoteTitle, '"', '&quot;'), '">');
	document.writeln('<input type="hidden" name="fvotefor" value="">');
	document.writeln('<input type="hidden" name="faction" value="0">');
	document.writeln('<input type="hidden" name="fDescription" value="', ReplaceAll(Description, '"', '&quot;'), '">');

	document.writeln('<input type="hidden" name="fnumitem" value=', NumItem, '>');
	document.writeln('<tr><td width="100%" height=5><img src="/Images/White.gif" border=0></td></tr>');
    
	document.writeln('<tr><td>');
	document.writeln('<table width="100%" cellspacing=0 cellpadding=0 border=0>');

	var i, j, k;

	for (i=0; i < NumItem; )
	{
		document.writeln('<tr>');
		
		for (j=0; j < Column && i < NumItem; j++, i++)
		{
			document.writeln('<input type="hidden" name="fT_', i, '" value="', ReplaceAll(ItemArray[i][0], '"', '&quot;'), '">');
			document.writeln('<input type="hidden" name="fI_', i, '" value="', ItemArray[i][1], '">');
			document.writeln('<input type="hidden" name="fN_', i, '" value="', ItemArray[i][2], '">');
			document.writeln('<td valign=top width=20 align=right><input type="checkbox" name="fC_', i, '" value=', ItemArray[i][2], ' class=VoteField onClick="CheckThisVote(this)"></td>');
			if (i + 1 < NumItem || Column==1)
			{
				document.writeln('<td><p  class=VoteItem>', ItemArray[i][0], '</p></td>');
			}
			else
			{
				document.writeln('<td colspan=', (Column - j - 1)*2,'><p  class=VoteItem>', ItemArray[i][0], '</p></td>');
			}
		}		

		document.writeln('</tr>');
	}

	document.writeln('</table>');
	document.writeln('</td></tr>');

	document.writeln('<tr><td width="100%" class=BreakLine height=4>&nbsp;</td></tr>');
	document.writeln('<tr><td width="100%" class=BreakLine height=1 bgcolor="', BgColor, '"></td></tr>');
	document.writeln('<tr><td width="100%" height=40 bgcolor="#F2EDDD">&nbsp;<input type="button" hidefocus value="Bi&#7875;u quy&#7871;t" name="Vote" class=VoteButton style="width: 70" onClick="SubmitVote(this.form, 0)">&nbsp;<input type="button" value="Xem k&#7871;t qu&#7843;" name="View" class=VoteButton style="width: 70" hidefocus onClick="SubmitVote(this.form, 1)"></td></tr>');
	document.writeln('<tr><td><Table bgcolor=',BgColor,' cellspacing=0 cellpadding=3 width="100%"><tr><td style="margin-left: 3;" class="Time">N&#7871;u b&#7841;n c&#243; l&#7921;a ch&#7885;n kh&#225;c, c&#243; th&#7875; chia s&#7867; <a a href="/ContactUs/?d=webmaster@ngoisao.net" class=Time style="color: ', Color, '"><b>t&#7841;i &#273;&#226;y</b></a></td></tr></Table></td></tr>');		
	document.writeln('</table>');
	document.writeln('</td>');
	document.writeln('</form>');
	document.writeln('</tr>');
	document.writeln('</table>');
}

function ShowExpand(sobj1, sobj2)
{
	sobj1.style.display = 'none';
	sobj2.style.display = '';
}

function SetSelectValue(Field, iStr)
{
	if (iStr=='')
	{
		iStr=' ';
	}

	for (i=0; i < Field.options.length; i++)
	{
		if (Field.options[i].value==iStr)
		{
			Field.selectedIndex=i;
			return;
		}
	}
}

function LTrim(iStr)
{
	while (iStr.charCodeAt(0) <= 32)
	{
		iStr=iStr.substr(1);
	}
	return iStr;
}

function RTrim(iStr)
{
	while (iStr.charCodeAt(iStr.length - 1) <= 32)
	{
		iStr=iStr.substr(0, iStr.length - 1);
	}
	return iStr;
}

function Trim(iStr)
{
	while (iStr.charCodeAt(0) <= 32)
	{
		iStr=iStr.substr(1);
	}

	while (iStr.charCodeAt(iStr.length - 1) <= 32)
	{
		iStr=iStr.substr(0, iStr.length - 1);
	}

	return iStr;
}


function Left(str, n)
{
	if (n <= 0)
	    return "";
	else if (n > String(str).length)
	    return str;
	else
	    return String(str).substring(0,n);
}


function Right(str, n)
{
    if (n <= 0)
       return "";
    else if (n > String(str).length)
       return str;
    else {
       var iLen = String(str).length;
       return String(str).substring(iLen, iLen - n);
    }
}


function CheckEmailAddress(Email)
{
	Email = Trim(Email);

	while (Email != '')
	{
		c = Email.charAt(0);	
		if (c==' ' || c=='<' || c==39 || c==':' || c=='.')
		{
			Email = Email.substr(1);
		}
		else
		{
			break;
		}
	}

	i = Email.indexOf('>');
	if (i==-1)
	{
		while (Email != '')
		{
			c = Email.charAt(Email.length - 1);
			if (c==' ' || c==39 || c=='.')
			{
				Email = Email.substr(0, Email.length - 1);
			}
			else
			{
				break;
			}
		}
	}
	else
	{
		Email = Email.substr(0, i);
	}

	if (Email.length > 96)
		return '';

	i = Email.lastIndexOf('@');
	j = Email.lastIndexOf('.');
	if (i < j)
		i = j;

	switch (Email.length - i - 1)
	{
	case 2:
		break;
	case 3:
		switch (Email.substr(i))
		{
		case '.com':
		case '.net':
		case '.org':
		case '.edu':
		case '.mil':
		case '.gov':
		case '.biz':
		case '.pro':
		case '.int':
			break;
		default:
			return '';
		}
		break;
	default:
		switch (Email.substr(i))
		{
		case '.name':
		case '.info':
			break;
		default:
			return '';
		}
		break;
	}

	Email = Email.toLowerCase();

	if (Email == '')
		return '';

	if (Email.indexOf(' ') != -1)
		return '';

	if (Email.indexOf('..') != -1)
		return '';

	if (Email.indexOf('.@') != -1)
		return '';

	if (Email.indexOf('@.') != -1)
		return '';

	if (Email.indexOf(':') != -1)
		return '';

	for (i=0; i < Email.length; i++)
	{
		c = Email.charAt(i);

		if (c >= '0' && c <= '9')
			continue;
		
		if (c >= 'a' && c <= 'z')
			continue;
		
		if ('`~!#$%^&*-_+=?/\\|@.'.indexOf(c) != -1)
			continue;

		return '';
	}

	if ((i=Email.indexOf('@'))==-1)
		return '';

	if (Email.substr(i + 1).indexOf('@')!=-1)
		return '';

	if (Email.charAt(0)=='.' || Email.charAt(Email.length - 1)=='.')
		return '';

	return Email;
}

function ShowAdWordByCate(Field)
{
	location.replace(SetParameter('/User/Rao-vat/Source/List.Asp', 'c', Field.options[Field.selectedIndex].value));
}

function ReplaceAll(iStr, v1, v2)
{
	var i = 0, oStr = '', j = v1.length;

	while (i < iStr.length)
	{
		if (iStr.substr(i, j) == v1)
		{
			oStr+=v2;
			i+=j
		}
		else
		{
			oStr+=iStr.charAt(i);
			i++;
		}
	}

	return oStr;
}

function TrimAndRDS(iStr)
{
	function IsHyperLink(iStr)
	{
		var i = 0, c = ' ';

		if (iStr.charAt(0) == '.')
			return false;

		for (i=0; i < iStr.length; i++)
		{
			c = iStr.charAt(i).toLowerCase();
			if (c >= '0' && c <= '9')
				continue;
		
			if (c >= 'a' && c <= 'z')
				continue;
		
			if ('@_-&.?#+-/:'.indexOf(c) != -1)
				continue;

			return false;
		}
	
		return true;
	}

	function GetLastBreak(iStr, s)
	{
		var f = new Array('(', ')', '<', '>', ' ', '\r', '\n', '\t', ',', ';', '!'), p = 0, i = 0, r = -1;
	
		for (i = 0; i < f.length; i++)
			if ((p = iStr.lastIndexOf(f[i], s)) != -1)
				if (r == -1 || p > r)
					r = p;
		return r;
	}

	function GetNextBreak(iStr, s)
	{
		var f = new Array('(', ')', '<', '>', ' ', '\r', '\n', '\t', ',', ';', '!'), p = 0, i = 0, r = -1;
	
		for (i = 0; i < f.length; i++)
			if ((p = iStr.indexOf(f[i], s)) != -1)
				if (r == -1 || p < r)
					r = p;
		return r;
	}

	function CheckDotAfter(iStr)
	{
		var p0 = 0, p1 = 0, p2 = 0, p3 = 0;

		while ((p1 = iStr.indexOf('.', p0)) != -1)
		{
			if (iStr.charAt(p1 - 1) == ' ')
			{
				iStr = iStr.substr(0, p1 - 1).concat(iStr.substr(p1));
				p0 = p1;
			}
			else
			{
				p0 = p1 + 1;
			}

			if (iStr.charAt(p0) != ' ')
			{
				if ((p3 = GetLastBreak(iStr, p0)) == -1)
				{
					p3 = p0;
				}
				else
				{
					p3 = p3 + 1;
				}
		
				if ((p2 = GetNextBreak(iStr, p3)) == -1)
				{
					if (IsHyperLink(iStr.substr(p3)))
					{
						iStr = iStr.substr(0, p3).concat(iStr.substr(p3).toLowerCase())
						break;
					}
					else
					{
						if (iStr.charAt(p0) < '0' || iStr.charAt(p0) > '9')
						{
							iStr = iStr.substr(0, p0).concat(' ').concat(iStr.substr(p0, 1).toUpperCase()).concat(iStr.substr(p0 + 1));
							p0++;
						}
					}
				}
				else
				{
					if (IsHyperLink(iStr.substring(p3, p2)))
					{
						iStr = iStr.substr(0, p3).concat(iStr.substring(p3, p2).toLowerCase()).concat(iStr.substr(p2));
						p0 = p2 + 1;
					}
					else
					{
						if (iStr.charAt(p0) < '0' || iStr.charAt(p0) > '9')
						{
							iStr = iStr.substr(0, p0).concat(' ').concat(iStr.substr(p0, 1).toUpperCase()).concat(iStr.substr(p0 + 1));
							p0++;
						}
					}
				}
			}
			else
			{
				iStr = iStr.substr(0, p0 + 1).concat(iStr.substr(p0 + 1, 1).toUpperCase()).concat(iStr.substr(p0 + 2));
			}
		}	

		return iStr;
	}

	function CheckCharAfter(iStr, iChar, iUp)
	{
		var p0 = 0, p1 = 0;

		while ((p1 = iStr.indexOf(iChar, p0)) != -1)
		{
			if (iStr.charAt(p1 - 1) == ' ')
			{
				iStr = iStr.substr(0, p1 - 1).concat(iStr.substr(p1));
				p0 = p1;
			}
			else
			{
				p0 = p1 + 1;
			}

			if (iStr.charAt(p0) != ' ')
			{
				if (iStr.charAt(p0) < '0' || iStr.charAt(p0) > '9')
				{
					if (iUp)
					{
						iStr = iStr.substr(0, p0).concat(' ').concat(iStr.substr(p0, 1).toUpperCase()).concat(iStr.substr(p0 + 1));
					}
					else
					{
						iStr = iStr.substr(0, p0).concat(' ').concat(iStr.substr(p0));
					}
					p0++;
				}
			}
			else
			{
				if (iUp)
				{
					iStr = iStr.substr(0, p0 + 1).concat(iStr.substr(p0 + 1, 1).toUpperCase()).concat(iStr.substr(p0 + 2));
				}
			}
		}

		return iStr;
	}

	function CheckScope(iStr, s1, s2)
	{
		var p0 = 0, p1 = 0;

		for (p0 = 0; (p1 = iStr.indexOf(s1, p0)) != -1; )
		{
			if (iStr.charAt(p1 + 1) == ' ')
				iStr = iStr.substr(0, p1 + 1).concat(iStr.substr(p1 + 2));

			if (p1 > 0)
				if (iStr.charAt(p1 - 1) != ' ')
				{
					iStr = iStr.substr(0, p1).concat(' ').concat(iStr.substr(p1));
					p1++;
				}
			
			p0 = p1 + 1;
		}

		for (p0 = 0; (p1 = iStr.indexOf(s2, p0)) != -1; )
		{
			var SkipChar = ':,.;!?'.concat(s2);

			if (p1 > 0)
				if (iStr.charAt(p1 - 1) == ' ')
				{
					iStr = iStr.substr(0, p1 - 1).concat(iStr.substr(p1));
					p1--;
				}

			if (iStr.charAt(p1 + 1) != ' ' && SkipChar.indexOf(iStr.charAt(p1 + 1)) == -1)
				iStr = iStr.substr(0, p1 + 1).concat(' ').concat(iStr.substr(p1 + 1));

			p0 = p1 + 1;
		}		

		return iStr;
	}
	
	iStr = ReplaceAll(iStr, '  ', ' ');
	iStr = ReplaceAll(iStr, ' \r\n', '\r\n');
	iStr = ReplaceAll(iStr, '\r\n ', '\r\n');

	iStr = CheckCharAfter(iStr, ',', false);
	iStr = CheckCharAfter(iStr, ':', false);
	iStr = CheckCharAfter(iStr, ';', false);
	iStr = CheckCharAfter(iStr, '?', true);
	iStr = CheckCharAfter(iStr, '!', true);

	iStr = CheckScope(iStr, '(', ')');
	iStr = CheckScope(iStr, '[', ']');

	iStr = ReplaceAll(iStr, 'http: //', 'http://');
	iStr = CheckDotAfter(iStr);

	iStr = ReplaceAll(iStr, ', \r\n', ',\r\n');
	iStr = ReplaceAll(iStr, ': \r\n', ':\r\n');
	iStr = ReplaceAll(iStr, '; \r\n', ';\r\n');
	iStr = ReplaceAll(iStr, '? \r\n', '!\r\n');
	iStr = ReplaceAll(iStr, '! \r\n', '!\r\n');
	iStr = ReplaceAll(iStr, '. \r\n', '.\r\n');


	if (iStr.charAt(0) == ' ')
		iStr = iStr.substr(1);

	if (iStr.charAt(iStr.length - 1) == ' ')
		iStr = iStr.substr(0, iStr.length - 1);

	return iStr.substr(0, 1).toUpperCase().concat(iStr.substr(1));
}

function dw(wstr)
{
	document.writeln(unescape(wstr));
}

function ShowFooterAd()
{
	if (RefAdLogo.length==0)
	{
		return;
	}

	CurAdLogo=RefAdStay + (Math.floor(Math.random()*12311) % (RefAdLogo.length - RefAdStay));

	document.writeln('<table width="100%" cellspacing=0 cellpadding=0 border=0 bgcolor="#c0c0c0">');
	document.writeln('<tr><td align=center>');
	document.writeln('<table width="100%" cellspacing=1 cellpadding=4 border=0>');
	document.writeln('<tr>');	

	var AdPost = new Array(new Array(0, RefAdStay), new Array(CurAdLogo, RefAdLogo.length), new Array(RefAdStay, CurAdLogo));

	for (k=0, c=0; c < 4 && k < 3; k++)
	{
		for (i=AdPost[k][0]; c < 4 && i < AdPost[k][1]; i++, c++)
		{
			document.writeln('<td height=64 align=center valign=middle align=right bgcolor="#FFFFFF">');
			if (RefAdLogo[i][1] != '')
			{
				document.writeln('<a href="', RefAdLogo[i][1], '"><img src="', PageHost.concat(RefAdLogo[i][0]), '" border=0 width=130 height=60></a>');
			}
			else
			{
				document.writeln('<img src="', PageHost.concat(RefAdLogo[i][0]), '" border=0 width=130 height=60>');
			}
			document.writeln('</td>');
		}
	}

	document.writeln('</tr>');
	document.writeln('</table>');	
	document.writeln('</td></tr>');
	document.writeln('</table>');
}

function PrintSubject()
{
	w=open(location.href.concat('?q=1'), '_blank', '');
	return false;
	w.document.writeln('<html>');
	w.document.writeln('<head>');
	w.document.writeln('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">');
	w.document.writeln('<link rel="stylesheet" href="http://'+sDomain+'/Resource/Default.css" type="text/css">');
	w.document.writeln('</head>');
	w.document.writeln('<body topmargin=3 leftmargin=0 marginheight=3 marginwidth=0>');
	w.document.writeln('<table cellspacing=0 cellpadding=0 border=0 width=420 align=center>');
	w.document.writeln('<tr>');
	w.document.writeln('<td>');
	w.document.writeln(document.getElementById('CContainer').innerHTML);
	w.document.writeln('</td>');
	w.document.writeln('</tr>');
	w.document.writeln('</table>');
	w.document.writeln('</body>');
	w.document.writeln('</html>');
	w.document.title = document.title;
	return false;
}

function EmailSubject(PageID)
{
	openMeExt('http://srv.'+sDomain+'/User/EmailSubject/?u='.concat(escape(location.href)), 0, 0, 0, 0, 0, 0, 1, 1, 400, 480, 0, 0, '', 0);
	return false;
}

function openPopup(vImage, vLink, vTitle, vHeight, vWidth, vTop, vLeft, vHide)
{
	if (typeof(vHide) == 'undefined')
	{
		vHide = 0;
	}

	if (vHide)
	{
		var pw = open('/Library/Popup.Asp?vImage='.concat(escape(vImage)).concat('&vLink=').concat(escape(vLink)).concat('&vTitle=').concat(escape(vTitle)), 'Popup_'.concat(ReplaceAll(vTitle, ' ', '_')), 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',').concat('top=').concat(screen.height).concat(',').concat('left=').concat(screen.width));
		//window.focus();
		pw.moveTo(vLeft, vTop);
		return pw;
	}
	else
	{
		return open('/Library/Popup.Asp?vImage='.concat(escape(vImage)).concat('&vLink=').concat(escape(vLink)).concat('&vTitle=').concat(escape(vTitle)), 'Popup_'.concat(ReplaceAll(vTitle, ' ', '_')), 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',').concat('top=').concat(vTop).concat(',').concat('left=').concat(vLeft));
	}
}

function ShowPopupAd()
{
	if (RefAPopup.length==0) return;
	var alPopupBanner = new adlistshow(RefAPopup,'PopupBanner',0,5,0,0,0);
}

function ShowPopupUnder()
{
	if (RefUPopup.length==0) return;	
	var alPopUnderBanner = new adlistshow(RefUPopup,'PopUnderBanner',0,6,0,0,0);
}

function AddLineSpace(height)
{
	document.writeln('<tr><td class=BreakLine height=', (typeof(height)=='undefined' ? 1 : height), '></td></tr>');
}

function ShowMenuAd()
{
	var i;
	if (typeof(RefAdMenu) == 'undefined')
		return;

	if (RefAdMenu.length==0)
		return;
	
	for (i=0; i < RefAdMenu.length; i++)
	{
		w = 130;
		h = RefAdMenu[i][4];
		document.writeln('<tr>');
		document.writeln('<td colspan=2>');
		document.writeln('<a href="', RefAdMenu[i][1], '" onClick="return openMeExt(this, ', RefAdMenu[i][2], ', 1)"><img align=center src="', PageHost.concat(RefAdMenu[i][0]), '" width=', w, ' height=', h, ' border=0></a>');
		document.writeln('</td></tr>');
		AddLineSpace();	
	}
}

function ShowFooterAd()
{
	if (typeof(RefFooter) == 'undefined') return;
	if (typeof(vAd) == 'undefined') vAd = 0;
	var alFooterBanner = new adlistshow(RefFooter,'FooterBanner',vAd,0,0,770,150);

}

function DisplayBanner(rbn,vAd)
{
	if (RefBanner.length==0)
	{
		document.write('<table cellspacing=0 cellpadding=1 border=0 width=468 height=60 bgcolor="#c0c0c0"><tr><td><table cellspacing=0 cellpadding=0 border=0 width="466" height="58"><tr><td bgcolor="#ffffff" align=center class=LeadFront><a href="http://vnexpress.net/Advertising" class=AdTop>D&#224;nh cho Qu&#7843;ng c&#225;o</a><br>&#272;i&#7879;n tho&#7841;i: 091 244 9324  (HN) / 090 810 7277  (HCM)<br><span class=Time></span></td></tr></table></td></tr></table>');
		return;
	}
	var alTopBanner = new adlistshow(RefBanner,'TopBanner',vAd,0,0,468,60);

}

function ShowArticleLogoDate()
{
	if (RefAtDate.length==0) return;
	if (typeof(vAd) == 'undefined') vAd = 0;
	var alArticleDateBanner = new adlistshow(RefAtDate,'ArticleDateBanner',vAd,1,0,400,150);
}

function ShowArticleLogoQuantity()
{
	if (RefAtQuan.length==0) return;
	var alArticleQuantityBanner = new adlistshow(RefAtQuan,'ArticleQuantityBanner',vAd,7,0,400,150);
}