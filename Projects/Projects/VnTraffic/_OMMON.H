//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

#if !defined(AFX_COMMON_H__A942B98E_02CA_4521_A196_1354DBE3C536__INCLUDED_)
#define AFX_COMMON_H__A942B98E_02CA_4521_A196_1354DBE3C536__INCLUDED_

#include <windows.h>
#include <streams.h>
#include <stdio.h>
#include <atlbase.h>
#include <qedit.h>
#include <math.h>
#include <vfw.h>

#define SafeRelease(pInterface) if(pInterface != NULL) {pInterface->Release(); pInterface=NULL;}
#define SafeDelete(pObject) if(pObject != NULL) {delete pObject; pObject=NULL;}
#define MSG(msg) MessageBox( NULL, msg, "Application Message", MB_OK )

#define bus_length 140
#define bus_width    50

#define bike_length 30
#define bike_width   16

#define car_length  70
#define car_width    35
#endif