function LoadSearch() {
	var urlx = document.getElementById("input_keyword").value;
	//return location.href='http://7sac.com/services/setracker.php?page=http://search.thanhnien.com.vn/?keyword='+urlx;
	return location.href='http://search.thanhnien.com.vn/?keyword='+urlx;
}

function whichButton(evt) {
	var key=getEvt(evt);
	var urlx = document.getElementById("input_keyword").value;
	if(key==13) {
		//document.location.href='http://7sac.com/services/setracker.php?page=http://search.thanhnien.com.vn/?keyword='+urlx;
		document.location.href='http://search.thanhnien.com.vn/?keyword='+urlx;
		return false;
	} else {
		//telexingVietUC(el,event);
	}	
}
function mytVUC(txtarea,event) {
	txtarea.vietarea= true;
	txtarea.onkeyup= whichButton;
	if (!supported) return;
	txtarea.onkeypress= vietTyping;
	txtarea.getCurrentWord= getCurrentWord;
	txtarea.replaceWord= replaceWord;
	txtarea.onkeydown= onKeyDown;
	txtarea.onmousedown= onMouseDown;
	txtarea.getValue= function() { return this.value; }
	txtarea.setValue= function(txt) { this.value = txt; }
	if(!theTyper) theTyper = new CVietString("");
}

function readsubmit() {
	document.writeln('<input style="width:90px" type="text" class="box" onblur="if (this.value==\'\') this.value=\'Tìm kiếm\';"  style="color:#939393; font-size:10px; vertical-align:bottom ;" onkeyUp="mytVUC(this,event);" onfocus="if (this.value==\'Tìm kiếm\') this.value=\'\';" value="Tìm kiếm" name="keyword" id="input_keyword">&nbsp;&nbsp;<a href="javascript:LoadSearch()"><img src="http://thanhnien.com.vn/images/search.gif" border="0"></a>');
}
readsubmit();
