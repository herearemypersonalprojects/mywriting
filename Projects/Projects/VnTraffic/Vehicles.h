// Vehicles.h: interface for the CVehicles class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

//////////////////////////////////////////////////////////////////////

#if !defined(AFX_VEHICLES_H__A942B98E_02CA_4521_A196_1354DBE3C536__INCLUDED_)
#define AFX_VEHICLES_H__A942B98E_02CA_4521_A196_1354DBE3C536__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
//#include <windows.h>
//#include <stdio.h>
//#include <atlbase.h>
//#include <qedit.h>
//#include <math.h>
//#include <vfw.h>
#include "common.h"

class CVehicles  
{
public:
	double speed;
	bool visibled;
	void set_time(DWORD time);
	BYTE type; //1: xe may, 2: car, 3: bus
	void set_values(int xx1, int yy1, int xx2, int yy2, BYTE kieu);
	int y2;
	int y1;
	int x2;
	int x1;
    int x_center;
    int y_center;
	DWORD start_time;
	bool match(int xx1, int yy1, int xx2, int yy2, BYTE kieu);
	CVehicles();
	virtual ~CVehicles();

};

#endif // !defined(AFX_VEHICLES_H__A942B98E_02CA_4521_A196_1354DBE3C536__INCLUDED_)
