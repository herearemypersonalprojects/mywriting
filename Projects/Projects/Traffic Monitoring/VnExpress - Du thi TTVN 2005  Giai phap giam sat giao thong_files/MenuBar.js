function DisplayMenuBar()
{
	var FolderDef=(GetPostVariable('v', '')=='');
	var HasItem=0;

	function InitMenu()
	{
		if (!HasItem)
		{
			document.writeln('<table border=0 width="100%" cellpadding=0 cellspacing=0 bgcolor="#ffffff">');
			HasItem=1;
		}
	}

	function AddLineSpace(height)
	{
		document.writeln('<tr><td class=BreakLine height=', (typeof(height)=='undefined' ? 1 : height), '></td></tr>');
	}

	function AddLevel1(Item, Link, TrId, Color)
	{
		var SLink;
		InitMenu();

		if (typeof(Color)=='undefined')
			Color = '#003366';

		if (Link.charAt(0) == '/')
		{
			Link = PageHost.concat(Link);
		}

		if ((p=Link.lastIndexOf('/?'))==-1)
		{
			SLink = ((Link.charAt(Link.length - 1) == '/') ? Link.substr(0, Link.length - 1) : Link).toLowerCase();
		}
		else
		{
			SLink = Link.substr(0, p).toLowerCase();
		}
		
		
		document.writeln('<tr bgcolor="', ((VirtualFolder.substr(0, SLink.length)==SLink) ? '#C80000' : Color), '" Id="',(TrId),'">');		
		
		document.writeln('<td height=16 nowrap colspan=2>');
		
		if (Item=='RSS')
		{
			Item = '<IMG src="/RSS/rss.gif" border=0 style="padding-top:3;">';
		}

		if (VirtualFolder==SLink && FolderDef)
		{
			document.write('<div class=MenuSelected>', Item, '</div>');
		}
		else
		{
			document.write('<a href="', Link, '" class=Menu>', Item, '</a>');
		}

		document.writeln('</td></tr>');
		AddLineSpace();
	}

	function AddLevel2(Item, Link, Parent, Flag)
	{
		var SLink, Spl;
		InitMenu();
		document.getElementById(Parent).bgColor="#C80000";
		if (Link.charAt(0) == '/')
		{
			Link = PageHost.concat(Link);
		}

		if ((p=Link.lastIndexOf('/?'))==-1)
		{
			SLink = ((Link.charAt(Link.length - 1) == '/') ? Link.substr(0, Link.length - 1) : Link).toLowerCase();
		}
		else
		{
			SLink = Link.substr(0, p).toLowerCase();
		}

		document.writeln('<tr bgcolor="#CCCCCC">');

		if (Flag==1)
		{
			document.write('<td height=16 nowrap>');
		}
		else
		{			
			document.write('<td height=16 nowrap colspan=2>');
		}

		if (Flag==1)
		{
			document.write('<div class=SubMenu>', Item, '</div>');
		}
		else
		{
			if (Link=='http://www.evan.com.vn' || Link=='http://dothi.net' || Link=='/Vietnam/Motor-Show/'){
				document.write('<a href="', Link, '" class=SubMenu target="_blank">', Item, '</a>');
			}
			else {
				document.write('<a href="', Link, '" class=SubMenu>', Item, '</a>');
			}
		}

		document.write('</td>');		

		if (Flag==1)
		{
			document.write('<td align=right><img src="/Images/Mark.gif" border=0></td>');
		}
		else
		{
			document.write('<td align=right></td>');
		}

		document.writeln('</tr>');
		AddLineSpace();
	}

	AddLevel1('Trang nh&#7845;t', '/Vietnam/Home/','mnu_1');
	AddLevel1('X&#227; h&#7897;i', '/Vietnam/Xa-hoi/','mnu_18');
	AddLevel1('Th&#7871; gi&#7899;i', '/Vietnam/The-gioi/','mnu_2');
	AddLevel1('Kinh doanh', '/Vietnam/Kinh-doanh/','mnu_3');
	AddLevel1('V&#259;n h&#243;a', '/Vietnam/Van-hoa/','mnu_51');
	AddLevel1('Th&#7875; thao', '/Vietnam/The-thao/','mnu_9');
	AddLevel1('Ph&#225;p lu&#7853;t', '/Vietnam/Phap-luat/','mnu_47');
	AddLevel1('&#272;&#7901;i s&#7889;ng', '/Vietnam/Doi-song/','mnu_110');
	AddLevel1('Khoa h&#7885;c', '/Vietnam/Khoa-hoc/','mnu_83');
	AddLevel1('Vi t&#237;nh', '/Vietnam/Vi-tinh/','mnu_89');
	AddLevel2('S&#7843;n ph&#7849;m m&#7899;i', '/Vietnam/Vi-tinh/San-pham-moi/','mnu_89', 1);
	AddLevel2('Kinh nghi&#7879;m', '/Vietnam/Vi-tinh/Kinh-nghiem/','mnu_89', 0);
	AddLevel2('H&#7887;i &#273;&#225;p', '/Vietnam/Vi-tinh/Hoi-dap/','mnu_89', 0);
	AddLevel2('Hacker & Virus', '/Vietnam/Vi-tinh/Hacker-Virus/','mnu_89', 0);
	AddLevel2('Gi&#7843;i tr&#237;', '/Vietnam/Vi-tinh/Giai-tri/','mnu_89', 0);
	AddLevel1('&#212;t&#244; - Xe m&#225;y', '/Vietnam/Oto-Xe-may/','mnu_38');
	AddLevel1('B&#7841;n &#273;&#7885;c vi&#7871;t', '/Vietnam/Ban-doc-viet/','mnu_109');
	AddLevel1('T&#226;m s&#7921;', '/Vietnam/Ban-doc-viet/Tam-su/','mnu_127');
	AddLevel1('Rao v&#7863;t', '/User/Rao-vat/','mnu_9998');
	AddLevel1('C&#432;&#7901;i', '/Vietnam/Cuoi/','mnu_105');
	document.writeln('<tr bgcolor="#003366"><td colspan=2>');
	ShowSearch();
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://ngoisao.net" target="_blank" hidefocus><img src="/Images/Left_Ngoisao.gif" border=0></a></td>');
	document.writeln('<td><a href="http://ngoisao.net" target="_blank" class="Menu" style="Font-size:10pt">Ng&#244;i sao</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://sohoa.net" target="_blank" hidefocus><img src="/Images/Left_Sohoa.gif" border=0></a></td>');
	document.writeln('<td><a href="http://sohoa.net" class="Menu" target="_blank" style="Font-size:10pt">S&#7889; ho&#225;</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://gamethu.net" target="_blank" hidefocus><img src="/Images/Left_Gamethu.gif" border=0></a></td>');
	document.writeln('<td><a href="http://gamethu.net" class="Menu" target="_blank" style="Font-size:10pt">Game th&#7911;</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://phimanh.net" target="_blank" hidefocus><img src="/Images/Left_Phimanh.gif" border=0></a></td>');
	document.writeln('<td><a href="http://phimanh.net" class="Menu" target="_blank" style="Font-size:10pt">Phim &#7843;nh</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://dothi.net" target="_blank" hidefocus><img src="/Images/Left_Dothi.gif" border=0></a></td>');
	document.writeln('<td><a href="http://dothi.net" class="Menu" target="_blank" style="Font-size:10pt">&#272;&#244; th&#7883;</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	document.writeln('<Table border=0 cellspacing=0 cellpadding=0><tr><td><a href="http://vieclam.vnexpress.net" target="_blank" hidefocus><img src="/Images/Left_VNW.gif" border=0></a></td>');
	document.writeln('<td><a href="http://vieclam.vnexpress.net" class="Menu" target="_blank" style="Font-size:10pt">Vi&#7879;c l&#224;m</a></td>');
	document.writeln('</tr></Table>');
	document.writeln('</td></tr>');
	AddLineSpace();
	ShowMenuAd();
	AddLevel1('RSS', '/Rss/', '#1E5C99');
	AddLevel1('Font Vietnam', 'JavaScript:SetFont()', '#1E5C99');
	AddLevel1('Li&#234;n h&#7879; qu&#7843;ng c&#225;o', '/Advertising/', '#1E5C99');
	AddLevel1('G&#7917;i t&#242;a so&#7841;n', '/ContactUs/?d=vitinh@vnexpress.net', '#1E5C99');

	if (!HasItem)
		return;

	AddLineSpace();

/*	document.writeln('<tr bgcolor="#1E5C99"><td colspan=2>');
	ShowSearch();
	document.writeln('</td></tr>');
*/	document.writeln('</table>');
}

DisplayMenuBar();