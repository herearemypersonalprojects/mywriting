// Application.h: interface for the CApplication class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

//////////////////////////////////////////////////////////////////////

#if !defined(AFX_APPLICATION_H__B8199318_63CF_4B65_ABEB_0D81607D0B40__INCLUDED_)
#define AFX_APPLICATION_H__B8199318_63CF_4B65_ABEB_0D81607D0B40__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <windows.h>
#include <commctrl.h>
#include <stdio.h>
#include <dshow.h>
#include <qedit.h>
#include <vfw.h>
#include "resource.h"
#include "SampleGrabberCB.h"


#define SafeRelease(pInterface) if(pInterface != NULL) {pInterface->Release(); pInterface=NULL;}
#define SafeDelete(pObject) if(pObject != NULL) {delete pObject; pObject=NULL;}
#define MSG(msg) MessageBox( NULL, msg, "Application Message", MB_OK )
// these read the keyboard asynchronously
#define KEY_DOWN(vk_code) ((GetAsyncKeyState(vk_code) & 0x8000) ? 1 : 0)
#define KEY_UP(vk_code)   ((GetAsyncKeyState(vk_code) & 0x8000) ? 0 : 1)

#define pi 3.141592653589793

#define WM_P_GRAPHNOTIFY WM_APP + 1

class CApplication  
{
public:
	void Show_Result(long time_total, long car_number, long motobike_number, double car_speed, double motobike_speed);
	LRESULT msg_proc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
	int Run();
	HRESULT Create(HINSTANCE hinstance);
	CApplication();
	virtual ~CApplication();

private:
	void ResizeVideoWindow(void);
	HRESULT FindAudioDevice(IBaseFilter **ppAudioSource);
	HRESULT FindCaptureDevice(IBaseFilter ** ppSrcFilter);
	void connect_camera();
	void connect_file(LPTSTR filename);
	int HEIGHT;
	int WIDTH;
	void FreeMediaType(AM_MEDIA_TYPE& mt);
	HRESULT ConnectFilters(IGraphBuilder *pGraph,IPin *pOut,IBaseFilter *pDest);
	HRESULT GetUnconnectedPin(IBaseFilter *pFilter,PIN_DIRECTION PinDir,IPin **ppPin);
	HRESULT ConnectFilters(IGraphBuilder *pGraph,  IBaseFilter *pSrc, IBaseFilter *pDest);
	HRESULT get_bitmap(LPTSTR pszFileName, BITMAPINFOHEADER** ppbmih);
	HRESULT init_dshow();
	void kill_dshow();
	void menu_proc(WPARAM wParam);
	void kill_window();
	HRESULT init_window();
	HWND g_hwnd;
	HINSTANCE g_hinstance;
	LPSTR g_class_name;
	LPSTR g_window_title;

    LPTSTR FileName;

	IGraphBuilder *pGB;
	ICaptureGraphBuilder2 *pCaptureG;
	IMediaControl *pMC;
	IVideoWindow *pVW;
	IMediaEventEx *pME;
	IMediaPosition *pMP;

	IBaseFilter *pGrabberF;
	ISampleGrabber *pGrabber;

	CSampleGrabberCB callback;
	IBaseFilter *pSrc;
	IBaseFilter *NullRenderFilter;
	IBaseFilter  *pAudioDecoder;
	IBaseFilter  *pAudioRender;
	IBaseFilter  *pStreamSplitter;
	IBaseFilter  *pAviDecompressor;
	IBaseFilter  *pVideoRender;
	IMediaFilter *pMediaFilter;//want the graph the run as quickly as possible, set the reference clock to NULL


};

#endif // !defined(AFX_APPLICATION_H__B8199318_63CF_4B65_ABEB_0D81607D0B40__INCLUDED_)
