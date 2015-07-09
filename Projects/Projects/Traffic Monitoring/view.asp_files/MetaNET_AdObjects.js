var AD_TYPE_IMAGE=1;
var AD_TYPE_TEXT=2;
var AD_TYPE_FLASH=3;
var AD_COUNT=0;
var AD_EX_COUNT=0;
var AD_SHARING_COUNT=0;
var AD_POSITION_RANDOM_CHANGE=-1;
var AD_POSITION_RANDOM_BLINK=1000;
var AD_POSITION_RANDOM_SHOW=0;
function __getEL(id){
return document.getElementById(id)}
function MetaNET_AdObject(args){
var __rendered=0;
AD_COUNT++;
this.__id=AD_COUNT;
this.Id='MetaNET_ADV_NUMBER_'+AD_COUNT
this.imageUrl=(typeof(args['imageUrl'])=='undefined' || args['imageUrl']==null)? '' : args['imageUrl'];
this.linkUrl=(typeof(args['linkUrl'])=='undefined' || args['linkUrl']==null)? '#' : args['linkUrl'];
this.width=(typeof(args['width'])=='undefined' || args['width']==null)? 0 : args['width'];
this.height=(typeof(args['height'])=='undefined' || args['height']==null)? 0 : args['height'];
this.cssClass=(typeof(args['cssClass'])=='undefined' || args['cssClass']==null)? '' : ' class=\''+args['cssClass']+'\'';
this.style=(typeof(args['style'])=='undefined' || args['style']==null)? '' : ' style=\''+args['style']+'\'';
this.target=(typeof(args['target'])=='undefined' || args['target']==null)? '_blank' : args['target'];
this.wmode=(typeof(args['wmode'])=='undefined' || args['wmode']==null)? 'window' : args['wmode'];
this.__style=(this.style=='')? '' : args['style'];
if((typeof(args['adType'])=='undefined' || args['adType']==null)&&!(typeof(args['imageUrl'])=='undefined' || args['imageUrl']==null)){
var t=new String(this.imageUrl);
t=t.toLowerCase();
if(t.lastIndexOf('.swf')==t.length-4){
this.adType=AD_TYPE_FLASH}
else if((t.lastIndexOf('.gif')==t.length-4)||
(t.lastIndexOf('.jpg')==t.length-4)||
(t.lastIndexOf('.png')==t.length-4)){
this.adType=AD_TYPE_IMAGE}
else{
this.adType=AD_TYPE_TEXT}}
else{
this.adType=args[4]}}
function IS_NO_LINK(url){
return(url==''||url=='#'||url=='javascript:void(0);'||url=='about:blank'||typeof(url)=='undefined')}
function GET_ONE_AD(i){
return document.getElementById('MetaNET_ADV_NUMBER_'+i)}
function GET_ALL_AD(){
var arrADS=new Array()
for(i=1;i<=AD_COUNT;i++){
arrADS[length]=GET_ONE_AD(i)}
return arrADS}
MetaNET_AdObject.prototype={
renderHTML:function(){
var s=""
if(this.adType==AD_TYPE_IMAGE){
if(IS_NO_LINK(this.linkUrl)){
s="<img src='"+this.imageUrl+"' width='"+this.width+"' height='"+this.height+"' border=0>"}
else{
s="<a href = '"+this.linkUrl+"' target='_blank' id='"+this.Id+"' "+this.cssClass+this.style+">"+
"<img src='"+this.imageUrl+"' width='"+this.width+
"' height='"+this.height+"' border=0></a>"}}
else if(this.adType==AD_TYPE_FLASH){
if(IS_NO_LINK(this.linkUrl)){
s="<div style='width:"+this.width+"px; height:"+this.height+"px;' id='"+this.Id+"'>"+
"<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' "+
" codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0' "+
" width="+this.width+" height="+this.height+
" id='"+this.Id+"_FLASH' align='middle'>"+
" <param name='allowScriptAccess' value='sameDomain' />"+
" <param name='movie' value='"+this.imageUrl+"'/>"+
" <param name='wmode' value='"+this.wmode+"' />"+
" <param name='linkUrl' value='"+this.linkUrl+"' />"+
" <param name='quality' value='high' />"+
"<embed src='"+this.imageUrl+"'"+
" quality='high' width="+this.width+
" height="+this.height+
" id='"+this.Id+"_FLASH'"+
" name='"+this.Id+"_FLASH'"+
" align='middle' allowScriptAccess='sameDomain'"+
" type='application/x-shockwave-flash'"+
" wmode='"+this.wmode+"'"+
" linkUrl='"+this.linkUrl+"'"+
" pluginspage='http://www.macromedia.com/go/getflashplayer' />"+
" </object></div>"}
else{
s="<div style='width:"+this.width+"px; height:"+this.height+"px;'>"+
"<div style='position:relative;width:"+this.width+"px; height:"+this.height+"px;' id='"+this.Id+"'>"+
"<a href='"+this.linkUrl+"' target='"+this.target+"'>"+
"<div style='position:absolute; top:0px; left:0px;cursor:pointer;"+
"width:"+this.width+"px; height:"+this.height+
"px;z-index:1500;display:block;background-color:Transparent'></div>"+
"<img style='position:absolute; top:0px; left:0px;cursor:pointer;z-index:1501' "+
"src='/MetaNET_ad/glass.gif' border=0 width="+this.width+" height="+this.height+"/>"+
"</a>"+
"<div style='position:absolute; top:0px; left:0px;cursor:pointer;"+
"width:"+this.width+"px; height:"+this.height+"px;z-index:1000;background-color:transparent;"+
this.__style+"' "+this.cssClass+">"+
"<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' "+
" codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0' "+
" width="+this.width+" height="+this.height+
" id='"+this.Id+"_FLASH' align='middle'>"+
" <param name='allowScriptAccess' value='sameDomain' />"+
" <param name='movie' value='"+this.imageUrl+"'/>"+
" <param name='wmode' value='"+this.wmode+"' />"+
" <param name='linkUrl' value='"+this.linkUrl+"' />"+
" <param name='quality' value='high' />"+
"<embed src='"+this.imageUrl+"'"+
" quality='high' width="+this.width+
" height="+this.height+
" id='"+this.Id+"_FLASH'"+
" name='"+this.Id+"_FLASH'"+
" align='middle' allowScriptAccess='sameDomain'"+
" type='application/x-shockwave-flash'"+
" wmode='"+this.wmode+"'"+
" linkUrl='"+this.linkUrl+"'"+
" pluginspage='http://www.macromedia.com/go/getflashplayer' />"+
" </object></div></div></div>"}}
else{
if(IS_NO_LINK(this.linkUrl)){
s=this.imageUrl}
else{
s="<a href = '"+this.linkUrl+"'  id='"+this.Id+"'  target='"+this.target+"' style='display:block;height:"+this.height+"px;width:"+this.width+"px;border:solid 1px #ccc; foat:left;"+this.__style+"' "+this.cssClass+">"+
this.imageUrl+"</a>"}}
__rendered=1
return s
},
show:function(){
if(__rendered==1){
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='inline'}
else{
alert("[MetaNET_AdObject]:"+'Không th&#7875; hi&#7875;n th&#7883; &#273;&#432;&#7907;c MetaNET_AdObject có ID='+this.Id+' do ch&#432;a &#273;&#432;&#7907;c Render')}
},
hide:function(){
try{
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='none'}
catch(e){}
},
renderIn:function(domId){
__getEL(domId).innerHTML=this.renderHTML()
},
renderOut:function(){
document.write(this.renderHTML())}}
function MetaNET_ExAdObject(adPair){
var __rendered=0
if(adPair.lenght=2){
AD_EX_COUNT++
this.Id='MetaNET_EXADV_NUMBER_'+AD_COUNT
this.collapseAd=adPair[0]
this.expandAd=adPair[1]
this.widthMin=this.collapseAd.width
this.widthMax=this.expandAd.width
this.heightMin=this.collapseAd.height
this.heightMax=this.expandAd.height
this.mode=0
this.__interval=10}
else{
alert('[MetaNET_ExAdObject]:'+'Không th&#7875; t&#7841;o &#273;&#432;&#7907;c Expand Ad v&#7899;i duy nh&#7845;t m&#7897;t ho&#7863;c không có MetaNET_AdObject!')
return}}
MetaNET_ExAdObject.prototype={
renderHTML:function(){
var _width=this.widthMin
var _height=this.heightMin
if(this.mode==1){
_width=this.widthMin
_height=this.heightMax}
var s="<div id='"+this.Id+"'><table><tr><td valign=top id='"+this.Id+"_TD' width="+_width+" height="+_height+">"
if(this.mode==1){
s+=this.expandAd.renderHTML()}
else{
s+=this.collapseAd.renderHTML()}
s+="</td></tr></table>"
if(this.mode==1){
s+="<font face='arial,verdana,tahoma' size=1><div class='exAdBtnBar'><a class='expandAdBtn' id='"+this.Id+"__EXPAND' style='display:none' href='javascript:"+this.Id+"__expand();'>M&#7903; r&#7897;ng</a><a class='collapseAdBtn' id='"+this.Id+"__COLLAPSE' href='javascript:"+this.Id+"__collapse();'>Thu nh&#7887;</a>"}
else{
s+="<font face='arial,verdana,tahoma' size=1><div><a class='expandAdBtn' id='"+this.Id+"__EXPAND' href='javascript:"+this.Id+"__expand();'>M&#7903; r&#7897;ng</a><a class='collapseAdBtn' id='"+this.Id+"__COLLAPSE'  style='display:none' href='javascript:"+this.Id+"__collapse();'>Thu nh&#7887;</a>"}
s+=" | <a class='closeAdBtn' id='"+this.Id+"__CLOSE' href='javascript:"+this.Id+"__close();'>&#272;óng</a></div>"
s+="</div></font>"
s+="<div id='"+this.Id+"_HIDDEN' style='display:none; visiblity:hidden; z-index:-1000;'>"
if(this.mode==1){
s+=this.collapseAd.renderHTML()}
else{
s+=this.expandAd.renderHTML()}
s+="</div>"
var s2=''
s2+="<scr"+"ipt type='text/javascript'>"
s2+="var "+this.Id+"__timeID=0;"
s2+="var "+this.Id+"__ModeID=0;"
s2+="function "+this.Id+"__expand(){"
s2+="var adDOMObj = __getEL('"+this.Id+"_TD');"
s2+="__getEL('"+this.Id+"__EXPAND').style.display='none';"
s2+="__getEL('"+this.Id+"__COLLAPSE').style.display='inline';"
s2+="if(adDOMObj.height < "+this.heightMax+"){"
s2+="    adDOMObj.height = parseInt(adDOMObj.height) + 2;"
s2+="}"
s2+="else if(adDOMObj.height > "+this.heightMax+"){"
s2+="    adDOMObj.height="+this.heightMax+";"
s2+="}"
s2+="if(adDOMObj.width < "+this.widthMax+"){"
s2+="    adDOMObj.width = parseInt(adDOMObj.width) + 2;"
s2+="}"
s2+="else if(adDOMObj.width > "+this.widthMax+"){"
s2+="    adDOMObj.width="+this.widthMax+";"
s2+="}"
s2+="if(adDOMObj.width>="+this.widthMax+" && adDOMObj.height>="+this.heightMax+"){"
s2+="    try{"
s2+="        window.clearTimeout("+this.Id+"__timeID);"
s2+="        var "+this.Id+"__adDOMObj_H = __getEL('"+this.Id+"_HIDDEN');"
s2+="        var __t = adDOMObj.innerHTML;"
s2+="        adDOMObj.innerHTML = "+this.Id+"__adDOMObj_H.innerHTML;"
s2+="        "+this.Id+"__adDOMObj_H.innerHTML = __t;"
s2+="        "+this.Id+"__ModeID=1;"
s2+="        return;"
s2+="    }catch(e){}"
s2+="}else{"
s2+="    "+this.Id+"__timeID = window.setTimeout('"+this.Id+"__expand()',5);"
s2+="}"
s2+="return;"
s2+="}"
s2+="function "+this.Id+"__collapse(){"
s2+="var adDOMObj = __getEL('"+this.Id+"_TD');"
s2+="__getEL('"+this.Id+"__EXPAND').style.display='inline';"
s2+="__getEL('"+this.Id+"__COLLAPSE').style.display='none';"
s2+="if("+this.Id+"__ModeID==1){"
s2+="var "+this.Id+"__adDOMObj_H = __getEL('"+this.Id+"_HIDDEN');"
s2+="var __t = adDOMObj.innerHTML;"
s2+="adDOMObj.innerHTML = "+this.Id+"__adDOMObj_H.innerHTML;"
s2+=""+this.Id+"__adDOMObj_H.innerHTML = __t;"
s2+="}"
s2+=""+this.Id+"__ModeID=0;"
s2+="if(adDOMObj.height > "+this.heightMin+"){"
s2+="    adDOMObj.height = parseInt(adDOMObj.height) - 2;"
s2+="}"
s2+="else if(adDOMObj.height < "+this.heightMin+"){"
s2+="    adDOMObj.height="+this.heightMin+";"
s2+="}"
s2+="if(adDOMObj.width > "+this.widthMin+"){"
s2+="    adDOMObj.width = parseInt(adDOMObj.width) - 2;"
s2+="}"
s2+="else if(adDOMObj.width < "+this.widthMin+"){"
s2+="    adDOMObj.width="+this.widthMin+";"
s2+="}"
s2+="if(adDOMObj.width<="+this.widthMin+" && adDOMObj.height<="+this.heightMin+"){"
s2+="    try{"
s2+="        window.clearTimeout("+this.Id+"__timeID);"
s2+="        return;"
s2+="    }catch(e){}"
s2+="}else{"
s2+="    "+this.Id+"__timeID = window.setTimeout('"+this.Id+"__collapse()',5);"
s2+="}"
s2+="return;"
s2+="}"
s2+="function "+this.Id+"__close(){"
s2+="var adDOMObj = __getEL('"+this.Id+"');"
s2+="adDOMObj.style.display='none';"
s2+="}"
s2+="</scr"+"ipt>"
document.write(s2)
__rendered=1
return s
},
show:function(){
if(__rendered==1){
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='inline'
}else{}
},
hide:function(){
try{
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='none'}
catch(e){}
},
renderIn:function(domId){
__getEL(domId).innerHTML=this.renderHTML()
},
renderOut:function(){
document.write(this.renderHTML())}}
function MetaNET_SharingAdObject(interval,agrs){
var __rendered=0
AD_SHARING_COUNT++
this.Id="MetaNET_SHARING_AD_NUMBER_"+AD_SHARING_COUNT
this.adObjects=new Array()
var __totalTime=0
for(i=0;i<agrs.length;i++){
this.adObjects[i]=agrs[i]}
this.interval=interval
this.length=agrs.length
this.currentAdId=this.length-1}
MetaNET_SharingAdObject.prototype={
renderHTML:function(){
var s=''
if(this.interval>0){
s+="<div id='"+this.Id+"'>"
if(this.length>1){
for(i=0;i<this.length;i++){
s+="<div id='"+this.Id+"_SLIDE_"+i+"' style='display:none;'>"
s+=this.adObjects[i].renderHTML()
s+="</div>"}
s+="<scr"+"ipt type='text/javascript'>"
s+=""+this.Id+"__play();"
s+="</scr"+"ipt>"
var s2=''
s2+="<scr"+"ipt type='text/javascript'>"
s2+="var "+this.Id+"__timeID=0;"
s2+="var "+this.Id+"__index="+this.currentAdId+";"
s2+="var "+this.Id+"__old_index="+this.currentAdId+";"
s2+="function "+this.Id+"__play(){"
s2+=" var adDOMObj = __getEL('"+this.Id+"');"
s2+=" "+this.Id+"__old_index = "+this.Id+"__index;"
s2+=" "+this.Id+"__index++;"
s2+=" if("+this.Id+"__index >= "+this.length+" ) "+this.Id+"__index = 0;"
s2+=" __getEL('"+this.Id+"_SLIDE_' + "+this.Id+"__old_index).style.display='none';"
s2+=" __getEL('"+this.Id+"_SLIDE_' + "+this.Id+"__index).style.display='inline';"
s2+=" "+this.Id+"__timeID=window.setTimeout('"+this.Id+"__play()',"+this.interval+");"
s2+=" return;"
s2+="}"
s2+="</scr"+"ipt>"
document.write(s2)}
s+="</div>"
__rendered=1
return s}
else if(this.interval<0){
s+="<div id='"+this.Id+"'>"
var j=this.currentAdId
for(i=0;i<this.length;i++){
s+=this.adObjects[j].renderHTML()
j++
if(j>=this.length)j=0}
s+="</div>"
__rendered=1
return s}
else{
s+="<div id='"+this.Id+"'>"
s+=this.adObjects[this.currentAdId].renderHTML()
s+="</div>"
__rendered=1
return s}
},
show:function(){
if(__rendered==1){
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='inline'
}else{}
},
hide:function(){
try{
var adDOMObj=__getEL(this.Id)
adDOMObj.style.display='none'}
catch(e){}
},
renderIn:function(domId){
__getEL(domId).innerHTML=this.renderHTML()
},
renderOut:function(){
document.write(this.renderHTML())}}

/*------------------------------------------------------------------------------------*/
function MetaNET_SharingAdObject2(interval,agrs){
    var __rendered = 0;
    //var this.IsEmpty = (agrs.length==0)? true : false;
    AD_SHARING_COUNT++;
    this.Id = "MetaNET_SHARING_AD_NUMBER_" + AD_SHARING_COUNT;
    this.adObjects = new Array();
    var __totalTime = 0;
    for(i = 0; i < agrs.length; i++){
        this.adObjects[i] = agrs[i];
    }
    this.interval = interval;
    this.length = agrs.length;
    this.currentAdId = Math.floor(Math.random()*this.length);
}
/*------------------------------------------------------------------------------------*/
MetaNET_SharingAdObject2.prototype = {
    renderHTML:function() {
        var s = '';
        /*Nếu giá trị interval > 0 & có 2 quảng cáo trở nên thì quảng cáo sẽ nháy*/
        if(this.interval > 0){
            s += "<div id='" + this.Id+ "'>";
            //s += this.adObjects[this.currentAdId].renderHTML();
            
            if(this.length > 1){
                for(i=0;i<this.length;i++){
                    s += "<div id='" + this.Id+ "_SLIDE_" + i + "' style='display:none;'>";
                    s += this.adObjects[i].renderHTML();
                    s += "</div>";
                }
                s+="<scr"+"ipt type='text/javascript'>";
                s+=""+this.Id+"__play();";
                s+="</scr"+"ipt>";
                var s2='';
                s2+="<scr"+"ipt type='text/javascript'>";
                
                s2+="var "+this.Id+"__timeID=0;";
                s2+="var "+this.Id+"__index=" + this.currentAdId + ";";
                s2+="var "+this.Id+"__old_index=" + this.currentAdId + ";";
                s2+="function "+this.Id+"__play(){";
                
                s2+=" var adDOMObj = __getEL('"+this.Id+"');";
                s2+=" "+this.Id+"__old_index = "+this.Id+"__index;";
                s2+=" "+this.Id+"__index++;";
                s2+=" if("+this.Id+"__index >= " + this.length + " ) "+this.Id+"__index = 0;";
                s2+=" __getEL('"+this.Id+"_SLIDE_' + "+this.Id+"__old_index).style.display='none';";
                s2+=" __getEL('"+this.Id+"_SLIDE_' + "+this.Id+"__index).style.display='inline';";
                s2+=" "+this.Id+"__timeID=window.setTimeout('"+this.Id+"__play()'," + this.interval + ");";
                
                s2+=" return;";
                s2+="}";
                
                s2+="</scr"+"ipt>";
                document.write(s2);
            }    
            s += "</div>";
            __rendered=1;
            return s;
        } 
        /*Nếu giá trị interval < 0 thì các quảng cáo sẽ xuất hiện thay đổi vị trí cho nhau*/
        else if (this.interval < 0){
            s += "<div id='" + this.Id+ "'>";
            var j = this.currentAdId;
            for(i=0;i<this.length;i++){
                s += this.adObjects[j].renderHTML();
                j++;
                if(j>=this.length) j=0;
            }
            s += "</div>";
            __rendered=1;
            return s;
        }
        /*Nếu giá trị interval == 0 thì sẽ xuất hiện ngẫu nhiên*/
        else{
            s += "<div id='" + this.Id+ "'>";
            s += this.adObjects[this.currentAdId].renderHTML();
            s += "</div>";
            __rendered=1;
            return s;
        }
    },
    /*Hiển thị quảng cáo*/
    show:function(){
        if(__rendered==1){
            var adDOMObj = __getEL(this.Id);
            adDOMObj.style.display='inline';
        }else{
        }
    },
    /*Ẩn quảng cáo*/
    hide:function(){
        try{
            var adDOMObj = __getEL(this.Id);
            adDOMObj.style.display='none';
        }
        catch(e){
            //alert(e.message);
        }
    },
	renderIn:function(domId){
        __getEL(domId).innerHTML = this.renderHTML();
    },
    renderOut:function(){
        document.write(this.renderHTML());
    }
}
