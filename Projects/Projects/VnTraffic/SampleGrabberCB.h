// SampleGrabberCB.h: interface for the CSampleGrabberCB class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SAMPLEGRABBERCB_H__37D6E5B8_6389_4A82_8419_E8EB7933FB8C__INCLUDED_)
#define AFX_SAMPLEGRABBERCB_H__37D6E5B8_6389_4A82_8419_E8EB7933FB8C__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


#include "Vehicles.h"
#include "common.h"

class CSampleGrabberCB : public ISampleGrabberCB  
{
public:
	CVehicles Vehicle[50];
    CVehicles Vehicle_new[50];
    BYTE vehicle_count_new;
	BYTE vehicle_count;
	BYTE distance(BYTE x,BYTE y,BYTE z, BYTE a,BYTE b,BYTE c);
	void get_color(BYTE *pBuffer, long x,long y, BYTE *r, BYTE *g, BYTE *b);
	void set_color(BYTE *pBuffer, long x, long y, BYTE r, BYTE g, BYTE b);
	double motobike_speed;
	double car_speed;
	DWORD start_time;
	long motobike_number;
	long car_number;
	void Show_Result(DWORD time_total, long car_number, long motobike_number, double car_speed, double motobike_speed);
	STDMETHODIMP FrameDifferencing(double SampleTime, BYTE *pBuffer, long BufferSize);
	BYTE* Subtraction(BYTE *pBuffer1, BYTE *pBuffer2);
	STDMETHOD(MethodUsingReferenceImage)(double SampleTime, BYTE *pBuffer, long BufferSize);
	int y0;
	int x0;


    int pattern[350][250];
    int stack_x[50000];
    int stack_y[50000]; 

	BYTE *temp3;
	BYTE *temp2;
	BYTE *temp1;
	BITMAPINFO BitmapInfo;
	HWND g_hwnd;
	STDMETHOD(BufferCB)( double SampleTime, BYTE * pBuffer, long BufferSize );
	long height;
	long width;
	CSampleGrabberCB();
	virtual ~CSampleGrabberCB();
    // Fake out any COM ref counting
    //
    STDMETHODIMP_(ULONG) AddRef() { return 2; }
    STDMETHODIMP_(ULONG) Release() { return 1; }

    // Fake out any COM QI'ing
    //
    STDMETHODIMP QueryInterface(REFIID riid, void ** ppv)
    {
        CheckPointer(ppv,E_POINTER);
        
        if( riid == IID_ISampleGrabberCB || riid == IID_IUnknown ) 
        {
            *ppv = (void *) static_cast<ISampleGrabberCB*> ( this );
            return NOERROR;
        }    

        return E_NOINTERFACE;
    }


    // We don't implement this one
    //
    STDMETHODIMP SampleCB( double SampleTime, IMediaSample * pSample )
    {
        return 0;
    }

private:
	void draw_rectangle(BYTE *pBuffer,int x1,int y1,int w,int h,byte r,byte g,byte b);
	void draw_line(BYTE *pBuffer,int x1,int y1,int x2,int y2,byte r,byte g,byte b);
};

#endif // !defined(AFX_SAMPLEGRABBERCB_H__37D6E5B8_6389_4A82_8419_E8EB7933FB8C__INCLUDED_)
