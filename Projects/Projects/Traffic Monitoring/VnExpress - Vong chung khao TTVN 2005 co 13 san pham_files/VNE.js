//Thu, 13/12/2007 09:57:42

var sLoDID=',';

function gmobj(o){
	if(document.getElementById){
		m=document.getElementById(o);
	}
	else if(document.all){
		m=document.all[o];
	}else if(document.layers){
		m=document[o];
	}
	return m;
}

function getNodeValue(o){
	if (o.item(0)){
		if (o.item(0).firstChild){
			return o.item(0).firstChild.nodeValue;
		}
		else{
			return '';
		}
	}
	else{
		return '';
	}
}


function toUpper(sInput){
	sInput=sInput.toUpperCase()
	var sOutput='',sTemp;
	var i=0, j=0;
	for (var i=0;i<sInput.length;i++){
		if (sInput.charAt(i)+sInput.charAt(i+1)=='&#'){
			sTemp=sInput.substring(i+2,sInput.length);
			j=sTemp.indexOf(';');
			if (j>4){
				sOutput+=sInput.charAt(i);					
			}
			else{
				sTemp=sTemp.substring(0,j)
				switch(sTemp){
					case '225': {sOutput+='&#193;';break;}		//a'
					case '224': {sOutput+='&#192;';break;}		//a`
					case '7843': {sOutput+='&#7842;';break;}	//a?
					case '227': {sOutput+='&#195;';break;}		//a~
					case '7841': {sOutput+='&#7840;';break;}	//a.
					case '226': {sOutput+='&#194;';break;}		//a^
					case '7845': {sOutput+='&#7844;';break;}	//a^'
					case '7847': {sOutput+='&#7846;';break;}	//a^`
					case '7849': {sOutput+='&#7848;';break;}	//a^?
					case '7851': {sOutput+='&#7850;';break;}	//a^~
					case '7853': {sOutput+='&#7852;';break;}	//a^.
					case '259': {sOutput+='&#258;';break;}		//a(
					case '7855': {sOutput+='&#7854;';break;}	//a('
					case '7857': {sOutput+='&#7856;';break;}	//a(`
					case '7859': {sOutput+='&#7858;';break;}	//a(?
					case '7861': {sOutput+='&#7860;';break;}	//a(~
					case '7863': {sOutput+='&#7862;';break;}	//a(.
					case '273': {sOutput+='&#272;';break;}		//dd
					case '233': {sOutput+='&#201;';break;}		//e'
					case '232': {sOutput+='&#200;';break;}		//e`
					case '7867': {sOutput+='&#7866;';break;}	//e?
					case '7869': {sOutput+='&#7868;';break;}	//e~
					case '7865': {sOutput+='&#7864;';break;}	//e.
					case '234': {sOutput+='&#202;';break;}		//e^
					case '7871': {sOutput+='&#7870;';break;}	//e^'
					case '7873': {sOutput+='&#7872;';break;}	//e^`
					case '7875': {sOutput+='&#7874;';break;}	//e^?
					case '7877': {sOutput+='&#7876;';break;}	//e^~
					case '7879': {sOutput+='&#7878;';break;}	//e^.
					case '237': {sOutput+='&#205;';break;}		//i'
					case '236': {sOutput+='&#204;';break;}		//i`
					case '7881': {sOutput+='&#7880;';break;}	//i?
					case '297': {sOutput+='&#296;';break;}		//i~
					case '7883': {sOutput+='&#7882;';break;}	//i.
					case '243': {sOutput+='&#211;';break;}		//o'
					case '242': {sOutput+='&#210;';break;}		//i`
					case '7887': {sOutput+='&#7886;';break;}	//o?
					case '245': {sOutput+='&#213;';break;}		//o~
					case '7885': {sOutput+='&#7884;';break;}	//o.
					case '244': {sOutput+='&#212;';break;}		//o^
					case '7889': {sOutput+='&#7888;';break;}	//o^'
					case '7891': {sOutput+='&#7890;';break;}	//o^`
					case '7893': {sOutput+='&#7892;';break;}	//o^?
					case '7895': {sOutput+='&#7894;';break;}	//o^~
					case '7897': {sOutput+='&#7896;';break;}	//o^.
					case '417': {sOutput+='&#416;';break;}		//o*
					case '7899': {sOutput+='&#7898;';break;}	//o*'
					case '7901': {sOutput+='&#7900;';break;}	//o*`
					case '7903': {sOutput+='&#7902;';break;}	//o*?
					case '7905': {sOutput+='&#7904;';break;}	//o*~
					case '7907': {sOutput+='&#7906;';break;}	//o*.
					case '250': {sOutput+='&#218;';break;}		//u'
					case '249': {sOutput+='&#217;';break;}		//u`
					case '7911': {sOutput+='&#7910;';break;}	//u?
					case '361': {sOutput+='&#360;';break;}		//u~
					case '7909': {sOutput+='&#7908;';break;}	//u.
					case '432': {sOutput+='&#431;';break;}		//u*
					case '7913': {sOutput+='&#7912;';break;}	//u*'
					case '7915': {sOutput+='&#7914;';break;}	//u*`
					case '7917': {sOutput+='&#7916;';break;}	//u*?
					case '7919': {sOutput+='&#7918;';break;}	//u*~
					case '7921': {sOutput+='&#7920;';break;}	//u*.
					case '253': {sOutput+='&#221;';break;}		//y'
					case '7923': {sOutput+='&#7922;';break;}	//y`
					case '7927': {sOutput+='&#7926;';break;}	//y?
					case '7929': {sOutput+='&#7928;';break;}	//y~
					case '7925': {sOutput+='&#7924;';break;}	//y.
					default: {sOutput+='&#'+sTemp+';';break;}
				}
				i+=j+2;
			}
		}
		else{
			sOutput+=sInput.charAt(i);
		}
	}
	return sOutput;
}

function displayid(id){
	if (sLoDID.indexOf(id)<=0){
		sLoDID=sLoDID.concat(id).concat(',');
		return true;
	}
	else{
		return false;
	}
}