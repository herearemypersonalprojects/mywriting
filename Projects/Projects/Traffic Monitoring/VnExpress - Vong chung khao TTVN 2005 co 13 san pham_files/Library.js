var fBrw=(navigator.userAgent.indexOf('MSIE')!= -1 && navigator.userAgent.indexOf('Windows')!= -1);
var fDSp=(typeof(document.getElementById)!='undefined');
var RefBanner = new Array();
var RefAdLogo = new Array();
var RefAdLBox = new Array();
var RefAdLBar = new Array();
var RefColumn = new Array();
var RefAdLeft = new Array();
var RefURight = new Array();
var RefUColum = new Array();
var RefDRight = new Array();
var RefDiLeft = new Array();
var RefFlLeft = new Array();
var RefAtDate = new Array();
var RefAtQuan = new Array();
var RefAPopup = new Array();
var RefUPopup = new Array();
var RefNormal = new Array();
var RefExpand = new Array();
var RefSlfExp = new Array();
var RefTStory = new Array();

var LastChild = 0;
var LComplete = 0;

var strDomain = 'vnexpress.net'
var sLocation = location.href;

var pos = sLocation.indexOf("vnexpress.net")
//if (pos < 1) location.href='http://vnexpress.net';
if (typeof(PageHost) == 'undefined') var PageHost = '';

if (typeof(SkipTopWindow) == 'undefined')
{
	if (window.parent!=window)
	{	
		alert('This website violate "The VnExpress.net © Copyright Notice".\r\nClick OK to Access VnExpress.net!');
		window.open(location.href, '_top', '');
	}
}

var objXML;
var aFlag=true;

function loadXMLDoc(url, callbackFunction, desc, QUERY_STRING) 
{
	aFlag=false;
	if(desc) window.status = desc;
    // branch for native XMLHttpRequest object
    if (window.XMLHttpRequest) {
        objXML = new XMLHttpRequest();
		objXML.onreadystatechange =	function()
									{
										// only if req shows "complete"
										if (objXML.readyState == 4) {
											eval(callbackFunction);
										}
									}
		if(QUERY_STRING)
		{
		    objXML.open("POST", url, true);
			objXML.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			objXML.send(QUERY_STRING);
		}
        else
        {
	        objXML.open("GET", url, true);
	        objXML.send(null);
	    }
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        objXML = new ActiveXObject("Microsoft.XMLHTTP");
        if (objXML) {
        	objXML.onreadystatechange =	function()
										{
											// only if req shows "complete"
											if (objXML.readyState == 4) {
												eval(callbackFunction);
											}
										}
			if(QUERY_STRING)
			{
			    objXML.open("POST", url, true);
				objXML.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				objXML.send(QUERY_STRING);
			}
			else
			{
			    objXML.open("GET", url, true);
			    objXML.send(null);
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
		document.writeln('<tr><td width=1 class=Symbol><b>&#169;&nbsp;</b></td><td class=Copyright nowrap><b><font color=#000000>Copyright 1997-2002 VnExpress.net</font></b>, All rights reserved. <a href="', PageHost, '/ContactUs/?d=webmaster@VnExpress.net">Contact us</a><td></tr>');
		document.writeln('<tr><td height=1 colspan=2 class=BreakLine>&nbsp;</td></tr>');
		document.writeln('<tr><td width=1 class=Symbol><b>&#174;&nbsp;</b></td><td class=Copyright nowrap>Y&#234;u c&#7847;u m&#7885;i t&#7893; ch&#7913;c khi s&#7917; d&#7909;ng th&#244;ng tin c&#7911;a VnExpress.net ph&#7843;i ghi r&#245; ngu&#7891;n tin.</td></tr>');
	}
	else
	{
		document.writeln('<tr><td width=1 class=Symbol><b>&#169;</b></td><td class=Copyright nowrap><b><font color=#000000>Copyright 1997-2002 VnExpress.net</font></b>, All rights reserved.<td></tr>');
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

	//document.writeln('<table width="100%" border=0 cellspacing=0 cellpadding=0 id="tIDM_', Name, '"><tr><td><div class=BreakLine id="IDM_', Name, '">');
	document.writeln('<table width="100%" border=0 cellspacing=0 cellpadding=0><tr><td id="IDM_', Name, '">');
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
		document.writeln('</table></td></tr></table></td></tr></table>');
	}
	else
	{
		document.writeln('</td></tr></table></td></tr></table>');
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
	
	if (MItem.style.display!='')
	{
		//MItem.setAttribute('style','display:""');
		MItem.style.display='';
		Image.src='/Images/min.gif';
	}
	else
	{
		//MItem.setAttribute('style','display:none');
		MItem.style.display='none';
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
	
	if (RelatedFolder < 1000 )
	{
		location.replace(CurrentFolder.concat('/Default.Asp?d=').concat(escape(LastDate)));
	}
	else
	{
		location.href = SetParameter(location.href, 'd', escape(LastDate));
	}	
	
}

function ShowNextFolderItem(LastDate)
{
	if (RelatedFolder < 1000 )
	{
		location.href = SetParameter(location.href + 'Default.Asp', 'd', escape(LastDate));
	}
	else
	{
		location.href = SetParameter(location.href, 'd', escape(LastDate));
	}	
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

function openAdWord(id, c)
{
	var url = 'http://srv.vnexpress.net/User/Rao-vat/Counter/?id=' + id + '&c=' + c + '&r=' + Math.random();
	location.href = url;
	return false;
}

function openImage(vLink, vHeight, vWidth)
{
	var sLink = (typeof(vLink.href) == 'undefined') ? vLink : vLink.href;

	if (sLink == '')
	{
		return false;
	}

	winDef = 'status=no,resizable=no,scrollbars=no,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',');
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

	if (RelatedFolder!=1000){
		document.writeln('<table height=30 cellspacing=0 cellpadding=0 border=0 align=center>');
		document.writeln('<form method="POST" name="Search" onSubmit="return SearchMe(document.Search.TSearch.value, 1)">');
		document.writeln('<tr>');
		document.writeln('<td><div><input type="text" name="TSearch" size=9 value="Search" class=SearchBox onfocus="SearchOnFocus(this)" onkeyup="initTyper(this)" onblur="SearchOnBlur(this)"></div></td>');
		document.writeln('<td class=BreakLine width=3>&nbsp;</td>');
		document.writeln('<td><a href="javascript:SearchMe(document.Search.TSearch.value, 1)"><img src="/Images/Go.gif" border=0></a></td>');
		document.writeln('</tr>');
		document.writeln('</form>');
		document.writeln('</table>');
	}
	else{
		document.writeln('<table height=30 cellspacing=0 cellpadding=0 border=0 align=center>');
		document.writeln('<tr>');
		document.writeln('<td bgcolor=white colspan=3>');
		document.writeln('<img src="/Images/Gate.gif" border=0>');
		document.writeln('</td>');
		document.writeln('</tr>');
		document.writeln('<form method="get" name="GateSearch" action="http://search.gate.vn">');
		document.writeln('<tr>');
		document.writeln('<td align=center>');
		document.writeln('<input type="text" name="p" value size="25" class="SearchBox">');
		document.writeln('<input type="hidden" name="ei" value="ei - UTF-8">');
		document.writeln('<input type="hidden" name="fr" value="cb-gatevn">');
		document.writeln('<input type="hidden" name="meta" value="web*vs=vnexpress.net">');
		document.writeln('</td>');
		document.writeln('<td class=BreakLine width=3>&nbsp;</td>');
		document.writeln('<td><input type="image" src="/Images/Go.gif" onclick="GateSearch.submit();"></td>');
		document.writeln('</tr>');
		document.writeln('</form>');
		document.writeln('</table>');
	}
	
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
			alert('Hay chon mot trong cac muc truoc khi bieu quyet.');
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
	var sheight = (j * 40) + 50;
	if (sheight < 250){
		sheight = 250;
	}
	open('', sform.name, 'scrollbars=yes,resizeable=no,locationbar=no,width=500,height='+sheight+',left='.concat((screen.width - 500)/2).concat(',top=').concat((screen.height - 250)/2));
	sform.faction.value = saction;
	sform.action = 'http://srv.vnexpress.net/User/Vote/Default.Asp' ;
	sform.submit();
}

function AddVote(SubjectID, PageID, VoteID, Align, VoteTitle, Color, BgColor, Width, NumItem, ItemArray, Description, Column)
{
	if (RelatedFolder!=500){
		document.writeln('<table width="', Width, '" border=0 cellspacing=0 cellpadding=2 bgcolor="', BgColor, '"', (Align=='') ? '' : ' align='.concat(Align), '>');
		if (VoteTitle!='')
		{
			document.writeln('<tr><td><p class=BoxTitle style="margin-left: 3; color: ', Color, '">', VoteTitle, '</p></td></tr>');
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
		document.writeln('<table border=0 cellpadding=0 cellspacing=0 width="100%" bgcolor="#FFFFFF">');

		document.writeln('<input type="hidden" name="fsubjectid" value=', SubjectID, '>');
		document.writeln('<input type="hidden" name="fpageid" value=', PageID, '>');
		document.writeln('<input type="hidden" name="ffolder" value="', CurrentFolder, '">');
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
		document.writeln('<tr><td width="100%" height=32>&nbsp;<input type="button" value="Bi&#7875;u quy&#7871;t" name="Vote" class=VoteButton style="width: 60" onClick="SubmitVote(this.form, 0)">&nbsp;<input type="button" value="K&#7871;t qu&#7843;" name="View" class=VoteButton style="width: 60" onClick="SubmitVote(this.form, 1)"></td></tr>');

		document.writeln('</table>');
		document.writeln('</td>');
		document.writeln('</form>');
		document.writeln('</tr>');
		document.writeln('</table>');
		document.writeln('<table height=2 cellspacing=0 cellpadding=0 border=0><tr><td class=BreakLine>&nbsp;</td></tr></table>');
	}
	else{
		document.writeln('<table width="90%" border=0 cellspacing=0 cellpadding=0 align=center bgcolor=white>');
		document.writeln('<tr><td><img src="/Images/Motor-show/VoteTopLeft.gif"></td><td width=100%></td><td><img src="/Images/Motor-show/VoteTopRight.gif"></td></tr>');
		if (VoteTitle!='')
		{
			document.writeln('<tr><td></td><td><p class=BoxTitle style="margin-left: 3; color: #113868">', VoteTitle, '</p></td><td></td></tr>');
		}

		if (typeof(Description)=='undefined')
		{
			Description = '';
		}

		if (typeof(Column)=='undefined')
		{
			Column = 1;
		}

		document.writeln('<tr><td></td>');
		document.writeln('<form method="POST" target="Frm_', VoteID, '" name="Frm_', VoteID, '">');
		document.writeln('<td>');
		document.writeln('<table border=0 cellpadding=0 cellspacing=0 width="100%" bgcolor="#FFFFFF">');

		document.writeln('<input type="hidden" name="fsubjectid" value=', SubjectID, '>');
		document.writeln('<input type="hidden" name="fpageid" value=', PageID, '>');
		document.writeln('<input type="hidden" name="ffolder" value="', CurrentFolder, '">');
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
		document.writeln('<tr><td width="100%" height=32>&nbsp;<input type="button" value="Bi&#7875;u quy&#7871;t" name="Vote" class=VoteButton style="width: 60" onClick="SubmitVote(this.form, 0)">&nbsp;<input type="button" value="K&#7871;t qu&#7843;" name="View" class=VoteButton style="width: 60" onClick="SubmitVote(this.form, 1)"></td></tr>');

		document.writeln('</table>');
		document.writeln('</td>');
		document.writeln('</form>');
		document.writeln('<td></td></tr>');
		document.writeln('<tr><td><img src="/Images/Motor-show/VoteBottomLeft.gif"></td><td width=100%></td><td><img src="/Images/Motor-show/VoteBottomRight.gif"></td></tr>');
		document.writeln('</table>');
		document.writeln('<table height=10 cellspacing=0 cellpadding=0 border=0><tr><td class=BreakLine>&nbsp;</td></tr></table>');
	}
}

function Showsurvey(){
	vWH = 500;
	vWW = 440;
	vWN = 'ContactUs';
	winDef = 'status=no,resizable=no,scrollbars=yes,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vWH).concat(',').concat('width=').concat(vWW).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vWH)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWW)/2);
	newwin = open('/Customize/ADSLSurvey/', vWN, winDef);
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

function PrintSubject()
{
	w=open(location.href.concat('?q=1'), '_blank', '');
	return false;
	w.document.writeln('<html>');
	w.document.writeln('<head>');
	w.document.writeln('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">');
	w.document.writeln('<link rel="stylesheet" href="http://vnexpress.net/Resource/Default.css" type="text/css">');
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
	openMeExt('http://srv.vnexpress.net/User/EmailSubject/?u='.concat(escape(location.href)), 0, 0, 0, 0, 0, 0, 1, 1, 515, 480, 0, 0, '', 0);
	return false;
}

function ShowPopupAd()
{
	if (RefAPopup.length==0) return;
	var alPopupBanner = new adlistshow(RefAPopup,'PopupBanner',0,5,0,0,0);
}

function ShowPopupUnder()
{
	if (RefUPopup.length==0) return;
	//if(VirtualFolder != CurrentFolder) return;
	var alPopUnderBanner = new adlistshow(RefUPopup,'PopUnderBanner',0,6,0,0,0);
}

function AddLineSpace(height)
{
	document.writeln('<tr><td class=BreakLine height=', (typeof(height)=='undefined' ? 1 : height), '></td></tr>');
}

function ShowMenuAd(vAd)
{
	if (typeof(RefAdMenu) == 'undefined') return;
	if (RefAdMenu.length==0) return;
	if (typeof(vAd) == 'undefined') vAd = 0;
	var vCheckShow = 0;
	
	//var alMenuBanner = new adlistshow(RefAdMenu,'MenuBanner',vAd,1,0,130,180);
}

function ShowFooterAd(vAd)
{
	if (typeof(RefFooter) == 'undefined') return;
	if (typeof(vAd) == 'undefined') vAd = 0;
	var alFooterBanner = new adlistshow(RefFooter,'FooterBanner',vAd,0,0,770,150);
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
function DisplayBanner(rbn,vAd)
{
	if (RefBanner.length==0)
	{
		document.write('<table cellspacing=0 cellpadding=1 border=0 width=468 height=60 bgcolor="#c0c0c0"><tr><td><table cellspacing=0 cellpadding=0 border=0 width="466" height="58"><tr><td bgcolor="#ffffff" align=center class=LeadFront><a href="http://vnexpress.net/Advertising" class=AdTop>D&#224;nh cho Qu&#7843;ng c&#225;o</a><br>&#272;i&#7879;n tho&#7841;i: 090 436 1114  (HN) / 090 810 7277  (HCM)<br><span class=Time>(Th&#244;ng tin <b>Rao v&#7863;t</b> vui l&#242;ng kh&#244;ng li&#234;n h&#7879; theo s&#7889; n&#224;y)</span></td></tr></table></td></tr></table>');
		return;
	}
	var alTopBanner = new adlistshow(RefBanner,'TopBanner',vAd,0,0,468,60);
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
	document.writeln('<td><img src="/Images/Advertising.gif" border=0></td>');
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


function ShowAdLogoLeft(vAd)
{
	if (typeof(vAd) == 'undefined') vAd = 0;
	if (RefAdLeft.length==0){
		document.write('<table width=130 cellspacing=0 cellpadding=0 border=0 bgcolor="#808080">');
		document.write('<tr>');
		document.write('<td valign=top>');
		document.write('<table cellspacing=1 cellpadding=4 border=0 width="100%">');
		document.write('<tr><td height=60 align=center bgcolor="#ffffff"><a href="http://vnexpress.net/Advertising" class=AdTitle>D&#224;nh cho <BR>Qu&#7843;ng c&#225;o</a></td></tr>');
		document.write('</table>');	
		document.write('</td>');
		document.write('</tr>');
		document.write('</table>');
		return;
	}
	var alLeftBanner = new adlistshow(RefAdLeft,'LeftLogo',vAd,1,0,130,180);
}

function ShowDivLogoLeft()
{
	//if (RefDiLeft.length==0) return;
	if (typeof(vAd) == 'undefined') vAd = 0;
	var slBottomUp = new adlistshow(RefDiLeft,'LeftBottomUp',vAd,4,0,100,180);
}

function ShowDivLogoRight()
{
	if (typeof(vAd) == 'undefined') vAd = 0;
	//if (RefDRight.length==0) return;
	var alRightFloatBanner = new adlistshow(RefDRight,'RightFloatLogo',vAd,3,0,115,500,'showtest');
}

function ShowDivLogoLeftFloat(){
	if (typeof(vAd) == 'undefined') vAd = 0;
	//if (RefFlLeft.length==0) return;
	var alLeftFloatBanner = new adlistshow(RefFlLeft,'LeftFloatLogo',vAd,2,0,100,210 );
}

function ShowAdLogoRight(vAd)
{
	if (bSTS==true && RefTStory.length!=0){
		var alBigRight = new adlistshow(RefTStory,'RightBigLogo',vAd,1,0,210,600);
	}
	else{
		var alBigRight = new adlistshow(RefAdLBox,'RightBigLogo',vAd,1,0,210,130);
		var alNormal = new adlistshow(RefNormal,'RightNormal',vAd,1,0,180,150,false);
		
		if (RefAdLogo.length!=0){
			document.write('<table border="1" width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#808080">');
			document.write('	<tr>');
			document.write('		<td valign=top>');
			document.write('		<table border="0" width="136" cellspacing="0" cellpadding="0">');
			var alSmallRight = new adlistshow(RefAdLogo,'RightSmallLogo',vAd,1,1,130,100);
			document.write('		</table>');
			document.write('		</td>');
			document.write('		<td width="100%" align="center" valign="top" style="padding-top:4px;" >');
			if (RefColumn.length==0){
				document.write('<table border="0" cellpadding="0" cellspacing="0"><tr><td width=60 height=60 align=center bgcolor="#CC0000"><a href="/Advertising" class=PnDTitle>D&#224;nh<BR>cho<BR>Qu&#7843;ng<BR>c&#225;o</a></td></tr></table>');
			}
			else{
				var alColumnRight = new adlistshow(RefColumn,'RightColumnLogo',vAd,1,0,60,300);
			}
			document.write('		</td>');
			document.write('	</tr>');
			document.write('</table>');
		}

		/*
		if (RefURight.length!=0){
			document.write('<table border="1" width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#808080">');
			document.write('	<tr>');
			document.write('		<td valign=top>');
			document.write('		<table border="0" width="136" cellspacing="0" cellpadding="0">');
			var alUnderRight = new adlistshow(RefURight,'UnderRightLogo',vAd,1,0,130,100);
			document.write('		</table>');
			document.write('		</td>');
			document.write('		<td width="100%" align="center" valign="top" style="border-right: 1px solid #808080; border-top: 1px solid #808080; border-bottom: 1px solid #808080; padding-top:4px;" >');
			if (RefColumn.length==0){
				document.write('<table border="0" cellpadding="0" cellspacing="0"><tr><td width=60 height=60 align=center bgcolor="#CC0000"><a href="/Advertising" class=PnDTitle>D&#224;nh<BR>cho<BR>Qu&#7843;ng<BR>c&#225;o</a></td></tr></table>');
			}
			else{
				var alUnderRightColumn = new adlistshow(RefUColum,'UnderRightColumn',vAd,1,0,60,300);
			}
			document.write('		</td>');
			document.write('	</tr>');
			document.write('</table>');
		}
		*/
		var alUnderRight = new adlistshow(RefURight,'UnderRightLogo',vAd,1,0,210,150);
		
		if (RefAdLBox.length+RefNormal.length+RefAdLogo.length+RefURight.length==0){
			document.write('<table width=210 cellspacing=0 cellpadding=0 border=0 bgcolor="#808080">');
			document.write('<tr>');
			document.write('<td valign=top>');
			document.write('<table cellspacing=1 cellpadding=4 border=0 width="100%">');
			document.write('<tr><td height=64 align=center bgcolor="#ffffff"><a href="/Advertising" class=TopStory>D&#224;nh cho Qu&#7843;ng c&#225;o</a></td></tr>');
			document.write('</table>');	
			document.write('</td>');
			document.write('</tr>');
			document.write('</table>');
		}
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









function ShowTopicJS(vFolderID, vItemCount, vType1, vType2, vCustomTitle, vPageCheck, vObjectID, vShowHeader, vSubjectID){
	var sId, sTitle, sType, sPath, iCount=0, iMaxItem=10;
	var arItem = new Array();
	var sLink = '/XML/'.concat(parseInt(vFolderID/100)).concat('/').concat(vFolderID).concat('.xml');
	var sHTML;
	
	AjaxRequest.get(
		{
	    'url':sLink
	    ,'onSuccess':function(req){
	    	if (req.responseXML.getElementsByTagName('Topic').length>0){
	    		with(req.responseXML.getElementsByTagName('Topic').item(0)){
					sId   = getNodeValue(getElementsByTagName('ID'));
					sTitle= getNodeValue(getElementsByTagName('Title'));
					sDate = getNodeValue(getElementsByTagName('ShowDate'));
					sType = toUpper(getNodeValue(getElementsByTagName('Type')));
					sPath = getNodeValue(getElementsByTagName('Path'));
	    		}
	    	}
	    	var iCount=0;
	    	for (var i=0;i<req.responseXML.getElementsByTagName('Subject').length;i++){
	    		with(req.responseXML.getElementsByTagName('Subject').item(i)){
	    			if (iCount<iMaxItem && iCount<vItemCount){
		    			var sTemp  = getNodeValue(getElementsByTagName('ID'));
						var sCheck = getNodeValue(getElementsByTagName('CheckShow'));
		    			if (sTemp!='' && displayid(sTemp) && sTemp!=vSubjectID && (vPageCheck==sCheck || sCheck==0)){
			    			arItem[iCount] = new Array(10);
							arItem[iCount][0] = getNodeValue(getElementsByTagName('Type'));
							arItem[iCount][1] = getNodeValue(getElementsByTagName('Title'));
							arItem[iCount][2] = getNodeValue(getElementsByTagName('Path'));							
							arItem[iCount][3] = getNodeValue(getElementsByTagName('Date'));
							iCount++;
						}
					}
	    		}
	    	}
			
			gmobj(vObjectID).innerHTML = GetTopicHTML(sId,sTitle,sDate,sPath,sType,sPath,arItem,vType1,vType2,vCustomTitle,vShowHeader);
	    }
	    ,'onError':function(req){gmobj(vObjectID).innerHTML=req.statusText;}
		}
	)
}



function GetTopicHTML(sId,sTitle,sDate,sPath,sType,sPath,arItem,vType1,vType2,vCustomTitle,vShowHeader){
	var sHTML = '';
	var iCount=0, i=0;
	var sTdBullet='', sTableTopicTitle='';
	var sTopicTitle='Theo d&#242;ng s&#7921; ki&#7879;n'
	
	switch(vType2)
	{
	case 0: // ' 0 Default Title	
		sTableTopicTitle += '<table border="0" cellpadding="0" cellspacing="0" width="100%">';
		sTableTopicTitle += '<tr><td class="OtherTitle" height="40" valign="middle">'+sTopicTitle+':</td></tr>';
		sTableTopicTitle += '</table>';
		sTdBullet='';
		break;
	case 1: // ' 1 User Defined Title
		sTableTopicTitle += '<table border="0" cellpadding="0" cellspacing="0" width="100%">';
		sTableTopicTitle += '<tr><td class="OtherTitle" height="40" valign="middle">'+vCustomTitle+':</td></tr>';
		sTableTopicTitle += '</table>';
		sTdBullet='';
		break;
	case 2: // ' 2 No Title
		sTrTopicTitle='';
		sTdBullet='';
		break;
	case 3: // ' 3 List Item Only
		sTrTopicTitle='';
		sTdBullet='<td width="7" valign="top" class="Normal">&#9642;</td>'
		break;
	}
	
	if (vShowHeader==0) sTableTopicTitle='';

	switch(vType1)		
	{
	case 1: // ' Topic
		sHTML += sTableTopicTitle;
		sHTML += '<table border="0" cellpadding="2" cellspacing="0" width="96%">';
		sHTML += '<tr><td><a href="/Topic/?ID='+sId+'" class="TopicTitle">'+sTitle+'</a><span class="ShowDate">&nbsp;('+sDate+')</span></td></tr>';
		sHTML += '</table>';
		break;
	case 2: // ' Subject
		sHTML += sTableTopicTitle;
		sHTML += '<table border="0" cellpadding="2" cellspacing="0" width="96%">';
		while (i<arItem.length)
		{
			sHTML += '<tr>'+sTdBullet+'<td><a class="Other" href="'+arItem[i][2]+'">'+arItem[i][1]+'</a><span class="ShowDate">&nbsp;('+arItem[i][3]+')</span></td></tr>';
			i++;
		}
		//sHTML += '<tr><td colspan="2" align="right"><a href="/Topic/?ID='+sId+'" class="Other">Xem ti&#7871;p</a></td></tr>';
		sHTML += '</table>';
		break;
	case 3: // ' Topic + Subject
		sHTML += sTableTopicTitle;
		sHTML += '<table border="0" cellpadding="2" cellspacing="0" width="96%">';
		sHTML += '<tr><td><a href="/Topic/?ID='+sId+'" class="TopicTitle">'+sTitle+'</a><span class="ShowDate">&nbsp;('+sDate+')</span></td></tr>';
		while (i<arItem.length)
		{
			sHTML += '<tr>'+sTdBullet+'<td><a class="Other" href="'+arItem[i][2]+'">'+arItem[i][1]+'</a><span class="ShowDate">&nbsp;('+arItem[i][3]+')</span></td></tr>';
			i++;
		}
		sHTML += '</table>';
		break;
	case 4: // ' Subject (before)
		sHTML += sTableTopicTitle;
		sHTML += '<table border="0" cellpadding="2" cellspacing="0" width="96%">';
		while (i<arItem.length)
		{
			sHTML += '<tr>'+sTdBullet+'<td><a class="Other" href="'+arItem[i][2]+'">'+arItem[i][1]+'</a><span class="ShowDate">&nbsp;('+arItem[i][3]+')</span></td></tr>';
			i++;
		}
		sHTML += '<tr><td align="right"><a class="Other" href="/Topic/?ID='+sId+'">Xem ti&#7871;p</a></td></tr>';
		sHTML += '</table>';
		break;
	case 5: // ' Topic + Subject (before)
		sHTML += sTableTopicTitle;
		sHTML += '<table border="0" cellpadding="2" cellspacing="0" width="96%">';
		sHTML += '<tr><td><a href="/Topic/?ID='+sId+'" class="TopicTitle">'+sTitle+'</a><span class="ShowDate">&nbsp;('+sDate+')</span></td></tr>';
		while (i<arItem.length)
		{
			sHTML += '<tr>'+sTdBullet+'<td><a class="Other" href="'+arItem[i][2]+'">'+arItem[i][1]+'</a><span class="ShowDate">&nbsp;('+arItem[i][3]+')</span></td></tr>';
			i++;
		}
		sHTML += '<tr><td align="right"><a class="Other" href="/Topic/?ID='+sId+'">Xem ti&#7871;p</a></td></tr>';
		sHTML += '</table>';
		break;
	}
	//if (sId==4953)	alert(sHTML);
	return sHTML;
}