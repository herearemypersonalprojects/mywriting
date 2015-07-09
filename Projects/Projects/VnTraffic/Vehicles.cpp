// Vehicles.cpp: implementation of the CVehicles class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

//////////////////////////////////////////////////////////////////////

#include "Vehicles.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CVehicles::CVehicles()
{

}

CVehicles::~CVehicles()
{

}

bool CVehicles::match(int xx1, int yy1, int xx2, int yy2, BYTE kieu)
{
if(kieu!=type)
return false;
int i=(xx1+xx2)/2;
int j=(yy1+yy2)/2;


if(((x_center-i)*(x_center-i)+(y_center-j)*(y_center-j)<bike_length*bike_length))
    return true;
return false;
}

void CVehicles::set_values(int xx1, int yy1, int xx2, int yy2, BYTE kieu)
{
x1=xx1;
y1=yy1;
x2=xx2;
y2=yy2;
type=kieu;

x_center=(x1+x2)/2;
y_center=(y1+y2)/2;
}



void CVehicles::set_time(DWORD time)
{
start_time=time;
}
