
var isIE = (document.getElementById && document.all);

var subjectdesctext = new Array('&#272;i&#7873;n kinh',
'Th&#7875; thao d&#432;&#7899;i n&#432;&#7899;c','B&#7855;n cung','C&#7847;u l&#244;ng','B&#243;ng r&#7893;','Quy&#7873;n anh','Canoeing',
'Xe &#273;&#7841;p','&#272;&#7845;u ki&#7871;m','B&#243;ng &#273;&#225;','Th&#7875; d&#7909;c',
'B&#243;ng n&#233;m','Judo','Karatedo','Ch&#232;o thuy&#7873;n','C&#7847;u m&#226;y','B&#7855;n s&#250;ng','B&#243;ng b&#224;n',
'Taekwondo','Qu&#7847;n v&#7907;t','B&#243;ng chuy&#7873;n','C&#7917; t&#7841;','V&#7853;t','Wushu','Billards & Snooker',
'Th&#7875; d&#7909;c th&#7875; h&#236;nh','C&#7901; vua','L&#7863;n','Pencak Silat','Bi s&#7855;t','&#272;&#225; c&#7847;u',
'&#272;ua thuy&#7873;n truy&#7873;n th&#7889;ng');

function DrawLayer(){
	if (isIE){
		document.write('<div id="subjectdesc"></div>');
	} else
		document.write('&nbsp;');
}

function mouseOver(lObject, s_id)
{
	if (isIE){
		eval('document.' + lObject + '.filters.alpha.opacity=90');
		subjectdesc.innerHTML = '<p align="left"><font style="font-family:Tahoma;font-size:8pt;font-weight:bold;color:#ffffff">&#187;&nbsp;' + subjectdesctext[s_id-1] + '&nbsp;&nbsp;</font></p>';
	}	
}

function mouseOut(lObject)
{
	if (isIE){
		eval('document.' + lObject + '.filters.alpha.opacity=100');
		subjectdesc.innerHTML = '';
	}
}

function En(){
var url = document.URL;   
s = url.split('/');

	document.write('<a href=#');
	for (var i=0; i <= s.length - 1; i++){
		if (i>2){
			document.write(s[i]);
			if (i!=(s.length-1))
				document.write('/');
		}
	}
	document.write('\'><font style="font-family:Verdana;font-size:8pt;font-weight:normal;">&#187;&nbsp;English</font></a>&nbsp;&nbsp;&nbsp;');

}

function openImage(vLink, vWidth, vHeight)
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
	newwin.document.writeln('<a href="" onClick="window.close(); return false;"><img src="', sLink, '" alt="', (isIE) ? '&#272;&#243;ng l&#7841;i' : 'Dong lai', '" border=0></a>');
	newwin.document.writeln('</body>');

	if (typeof(vLink.href) != 'undefined')
	{
		return false;
	}
}

function draw_search_box(){
	if (isIE){
		document.write('<input type="text" name="q" size="12" class="iinput" maxlength="20">');
	} else
		document.write('<input type="text" name="q" size="6" class="iinput" maxlength="20">');
}

function openWindow(vLink, vWidth, vHeight)
{
	var sLink = (typeof(vLink.href) == 'undefined') ? vLink : vLink.href;

	if (sLink == '')
	{
		return false;
	}

	winDef = 'status=no,resizable=no,scrollbars=yes,toolbar=no,location=no,fullscreen=no,titlebar=yes,height='.concat(vHeight).concat(',').concat('width=').concat(vWidth).concat(',');
	winDef = winDef.concat('top=').concat((screen.height - vHeight)/2).concat(',');
	winDef = winDef.concat('left=').concat((screen.width - vWidth)/2);
	window.open(sLink, 'prn', winDef);

	if (typeof(vLink.href) != 'undefined')
	{
		return false;
	}
}