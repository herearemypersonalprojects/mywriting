//Copyright (c) QuanTriMang.com
//Written By Sonnguyen ALI - sonnguyen_ali@yahoo.com
var theobjects = document.getElementsByTagName("object");
for (var i = 0; i < theobjects.length; i++) {
	theobjects[i].outerHTML = theobjects[i].outerHTML;
}