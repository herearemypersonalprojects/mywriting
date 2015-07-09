	function mOvr(src) {
		if (!src.contains(event.fromElement)) {
			src.style.cursor = 'hand';
			src.style.backgroundColor = '#4477FF';
		}

	}

	function mOut(src) {
		if (!src.contains(event.toElement)) {
			src.style.cursor = 'default';
			src.style.backgroundColor = '#7e9db7';
		}
	}

	function mClk(src) {
		if(event.srcElement.tagName=='TD'){
			src.children.tags('A')[0].click();
		}
	}

	function mOvr1(src) {
		if (!src.contains(event.fromElement)) {
			src.style.cursor = 'hand';
			src.style.backgroundColor = '#4477FF';
		}

	}

	function mOut1(src) {
		if (!src.contains(event.toElement)) {
			src.style.cursor = 'default';
			src.style.backgroundColor = '#2B6EBB';
		}
	}

	function mClk1(src) {
    
		if(event.srcElement.tagName=='TD'){
			src.children.tags('A')[0].click();
		}
	}

	function mOvr_Cal(src) {
		if (!src.contains(event.fromElement)) {
			src.style.cursor = 'hand';
			src.style.backgroundColor = '#F7E8B9';
		}

	}

	function mOut_Cal(src) {
		if (!src.contains(event.toElement)) {
			src.style.cursor = 'default';
			src.style.backgroundColor = '#F4D15F';
        }
	}

	function mClk_Cal(src) {
		if(event.srcElement.tagName=='TD'){
			src.children.tags('A')[0].click();
		}
	}


	function check_searchform(str)
	{
		if (str.value=='')
		{
			alert("Nhap vao noi dung tim kiem");
			str.focus;
			return false;
		}
		return true;
	}
  
  //for the weather box
  var LastChild;
  if (typeof(PageHost) == 'undefined')
  {
    var PageHost = '';
  }
  function AddHeader(Name, Header, Buttons, Symbol, AddChildTable)
  {
    document.writeln('<table width="100%" border=0 cellspacing=0 cellpadding=1 bgcolor="#1E5C99"><tr><td class="bg3">');
    document.writeln('<div class="weather" id="IDM_', Name, '">');
    if (typeof(AddChildTable)=='undefined')
    {
      document.writeln('<table align=center width="100%" cellspacing=0 cellpadding=0 border=1 >');
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
    document.writeln('<tr><td colspan="2" class="weather" align="center">Theo vnexpress</td></tr>');
    if (LastChild)
    {
      document.writeln('</table></div></td></tr></table>');
    }
    else
    {
      document.writeln('</div></td></tr></table>');
    }
  }
  
  ///////////////////////////////////////////////////////////////////////////////////////////
  function checkValidateADSForm()
  {
    var msg = "";		
    if(document.ADVPOST.apst_advcatfk.value == -1 )
  	  msg += "- Chọn mục tin.\n";
  	
  	if(document.ADVPOST.apst_title.value == "")
  		msg += "- Tựa đề.\n";
    if(document.ADVPOST.apst_content.value == "")
  		msg += "- Nội dung.\n";
    if(document.ADVPOST.apst_contact.value == "")
  		msg += "- Liên hệ.\n";
        
    if(document.ADVPOST.apst_sell.value == -1)
  	{
      msg += "- Cần.\n";
  	}

    if(msg != "")
    {
      msg = "Các mục tin dưới đây không hợp lệ:\n" + msg;
      alert(msg);
      return false;
    }
    
  	return true;
  }
  
  function SaveADS()
  {
    if(checkValidateADSForm())
    {
      document.ADVPOST.submit();
    }    
  }
