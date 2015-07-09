// Application.cpp: implementation of the CApplication class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289
//////////////////////////////////////////////////////////////////////

#include "Application.h"
//-----------------------------------------------------------------------------
// Global access to the app (needed for the global WndProc())
//-----------------------------------------------------------------------------
static CApplication* g_app = NULL;
//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CApplication::CApplication()
{
	g_app=this;
	g_hinstance=NULL;
	g_hwnd=NULL;
	g_class_name="classname";
	g_window_title="Gi¸m s¸t giao th«ng b»ng hÖ thèng xö lý ¶nh";

	pCaptureG=NULL;
	pGB=NULL ;
	pMC=NULL;
	pVW=NULL;
	pME=NULL;
	pMP=NULL;

	pGrabberF = NULL;
	pGrabber=NULL;

	pSrc=NULL;
	NullRenderFilter=NULL;
	pAudioDecoder=NULL;
	pAudioRender=NULL;
	pStreamSplitter=NULL;
	pMediaFilter=0;
	pAviDecompressor=NULL;
	pVideoRender=NULL;

}

CApplication::~CApplication()
{
kill_dshow();
kill_window();
g_hwnd=NULL;
g_hinstance=NULL;
CoUninitialize();
}

HRESULT CApplication::Create(HINSTANCE hinstance)
{
if(SUCCEEDED(init_window()))
{
//Show_Result(0,0,0,0,0);
}
else
	return E_FAIL;
return S_OK;
}

int CApplication::Run()
{
	MSG msg;
	PeekMessage(&msg, NULL, 0U, 0U, PM_NOREMOVE);
	HACCEL         ghAccel ;
   ghAccel = LoadAccelerators(g_hinstance, g_class_name) ;
   //	m_dwStartTime = timeGetTime();
       // Initialize COM
    if(FAILED(CoInitialize(NULL)))
        return E_FAIL;

    // All set; get and process messages
    while (GetMessage(&msg, NULL, 0, 0)>0) 
	{

        if (! TranslateAccelerator(g_hwnd, ghAccel, &msg)) 
		{
            TranslateMessage(&msg) ;
            DispatchMessage(&msg) ;
        }
			// for now test if user is hitting ESC and send WM_CLOSE
	if (KEY_DOWN(VK_ESCAPE))
		SendMessage(g_hwnd,WM_CLOSE,0,0);	
    }
return msg.wParam;
}

//Ham xu ly thong diep
LRESULT CALLBACK wnd_proc( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam )
{
    return g_app->msg_proc( hWnd, uMsg, wParam, lParam );
}

HRESULT CApplication::init_window()
{
WNDCLASS window_class;

InitCommonControls();

   window_class.style          =0;// CS_OWNDC | CS_HREDRAW | CS_VREDRAW;
   window_class.cbClsExtra     = 0;
   window_class.cbWndExtra     = 0;
   window_class.hInstance      = g_hinstance;
   window_class.hIcon			= LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON1));
   window_class.hCursor        = LoadCursor(GetModuleHandle(NULL), MAKEINTRESOURCE(IDC_CURSOR1));
   window_class.hbrBackground  = (HBRUSH)COLOR_HIGHLIGHT;//(HBRUSH)GetStockObject(WHITE_BRUSH);
   window_class.lpszMenuName   = MAKEINTRESOURCE(IDR_MENU1);;
   window_class.lpszClassName  = g_class_name;
   //Here we provide our default window handler, all windows messages
   //will be sent to this function.
   window_class.lpfnWndProc    = wnd_proc;
  


   //Register the class with windows
   
   if(FAILED(RegisterClass(&window_class)))
   {      
	   MSG("RegisterClass function error!");
      return E_FAIL;
   }
     //Here we actually create the window.  For more detail on the various
   //parameters please refer to the Win32 documentation.
  g_hwnd=CreateWindow(g_class_name, //name of our registered class
                              g_window_title, //Window name/title
                            WS_OVERLAPPEDWINDOW,      //Style flags  WS_POPUP|WS_VISIBLE|
                              0,          //X position
                              0,          //Y position
                              GetSystemMetrics(SM_CXSCREEN),//width of window
                              GetSystemMetrics(SM_CYSCREEN),//height of window
                              GetDesktopWindow(),       //Parent window
                              NULL,       //Menu
                              g_hinstance, //application instance handle
                              NULL);      //pointer to window-creation data

   if(!g_hwnd){
      MSG("CreateWindow function error");
      kill_window();
      return E_FAIL;
   }
	  callback.g_hwnd=g_hwnd;
      ShowWindow( g_hwnd, SW_MAXIMIZE);
      UpdateWindow( g_hwnd );
	  //Hide the mouse in full-screen
     //ShowCursor(FALSE);
return S_OK;
}

void CApplication::kill_window()
{
  //Test if our window is valid
   if(g_hwnd)
   {
//      if(!DestroyWindow(g_hwnd)){
//         //We failed to destroy our window, this shouldn't ever happen
//         MSG("Destroy Window Failed");
//      }else
	   DestroyWindow(g_hwnd);
	  {
         MSG msg;
         //Clean up any pending messages
         while(PeekMessage(&msg, NULL, 0, 0,PM_REMOVE))
		 {
            DispatchMessage(&msg);
         }
      }
      //Set our window handle to NULL just to be safe
      g_hwnd=NULL;
   }

   //Unregister our window, if we had opened multiple windows using this
   //class, we would have to close all of them before we unregistered the class.
   if(!UnregisterClass(g_class_name,g_hinstance))
   {
      MSG("Unregister Failed");
   }

}

LRESULT CApplication::msg_proc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
switch(uMsg)
	{
	case WM_P_GRAPHNOTIFY:
		{
		  long evCode, param1, param2;

			HRESULT hr;

			while (hr = pME->GetEvent(&evCode, &param1, &param2, 0), SUCCEEDED(hr))
			{
				hr = pME->FreeEventParams(evCode, param1, param2);

				if ((EC_COMPLETE == evCode) || (EC_USERABORT == evCode))
				{
						pVW->put_Visible(OAFALSE);
						pVW->put_Owner(NULL);
					pMP->put_CurrentPosition(0);   // reset to beginning
						MSG("Finished");
				break;
				}
			}
		}break;

	case WM_CREATE:
		{			
		}break;

	case WM_SIZE:
		{

		}		
		break;
	case WM_MOUSEMOVE:
		{
		}break;
    case WM_PAINT:
        {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hWnd, &ps);
        EndPaint(hWnd, &ps);
            break ;
        }
	case WM_COMMAND:		
		menu_proc(wParam);
		break;
//	case WM_CLOSE:
//		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, uMsg, wParam, lParam);
	}
return 0;
}

void CApplication::menu_proc(WPARAM wParam)
{
			switch(LOWORD(wParam))
			{
			//menu he thong
			case ID_HETHONG_NOIKETFILENGUON:
				{		

					OPENFILENAME ofn;
					char szFileName[MAX_PATH] = "";
		
					ZeroMemory(&ofn, sizeof(ofn));

					ofn.lStructSize = sizeof(OPENFILENAME);
					ofn.hwndOwner =g_hwnd;
					ofn.lpstrFilter = "Movie Files (*.mpg)\0*.mpg\0All Files (*.*)\0*.*\0";
					ofn.lpstrFile = szFileName;
					ofn.nMaxFile = MAX_PATH;
					ofn.Flags = OFN_EXPLORER | OFN_FILEMUSTEXIST | OFN_HIDEREADONLY;
					ofn.lpstrDefExt = "mpg";					
					
					if(GetOpenFileName(&ofn))
					{                                                
                        
                       FileName=new CHAR[strlen(ofn.lpstrFile )+1];	
                       strcpy(FileName,ofn.lpstrFile);
                       
		                //MultiByteToWideChar(CP_ACP, 0,  filename , -1, MediaFile, strlen(filename)+1);
						connect_file(ofn.lpstrFile);

					}			
				}break;
			case ID_HETHONG_NOIKETCAMERA:
				{
					connect_camera();
				}break;
			case ID_HETHONG_GHIKETQUA:
				{
                    break;
					OPENFILENAME ofn;
					char szFileName[MAX_PATH] = "";
		
					ZeroMemory(&ofn, sizeof(ofn));

					ofn.lStructSize = sizeof(OPENFILENAME);
					ofn.hwndOwner =g_hwnd;
					ofn.lpstrFilter = "Movie Files (*.mpg)\0*.mpg\0All Files (*.*)\0*.*\0";
					ofn.lpstrFile = szFileName;
					ofn.nMaxFile = MAX_PATH;
					ofn.Flags = OFN_EXPLORER | OFN_FILEMUSTEXIST | OFN_HIDEREADONLY;
					ofn.lpstrDefExt = "mpg";
					
					
					if(GetOpenFileName(&ofn))
					{
		WCHAR *MediaFile=new WCHAR[strlen(ofn.lpstrFile)+1];
	
		MultiByteToWideChar(CP_ACP, 0,  ofn.lpstrFile , -1, MediaFile, strlen(ofn.lpstrFile )+1);

			//This creates the filter graph manager
		if(FAILED(CoCreateInstance(CLSID_FilterGraph, NULL, CLSCTX_INPROC_SERVER,          
			IID_IGraphBuilder, (void **)&pGB)))
		{
			MSG("Error on create GraphBuilder");
			return ;
		}
// Create the Sample Grabber.

	HRESULT hr = CoCreateInstance(CLSID_SampleGrabber, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pGrabberF);
	if (FAILED(hr))
	{
    // Return an error.
	MSG("Error on create pGrabberF");
	return;
	}
hr = pGB->AddFilter(pGrabberF, L"Sample Grabber");
if (FAILED(hr))
{
    // Return an error.
	MSG("Error AddFilter pGrabberF");
}

pGrabberF->QueryInterface(IID_ISampleGrabber, (void**)&pGrabber);

AM_MEDIA_TYPE mt;
ZeroMemory(&mt, sizeof(AM_MEDIA_TYPE));
mt.majortype = MEDIATYPE_Video;
mt.subtype = MEDIASUBTYPE_RGB24;
mt.formattype = FORMAT_VideoInfo; 
hr = pGrabber->SetMediaType(&mt);
	if (FAILED(hr))
	{
		MSG("Error on SetMediaType");
		return;
	}
	hr = pGB->RenderFile(MediaFile,NULL);
	if (FAILED(hr))
	{
		MSG("error on RenderFile");
		return;
	}

						pGB->QueryInterface(IID_IMediaControl, (void **)&pMC);
						pGB->QueryInterface(IID_IVideoWindow, (void **)&pVW);
						pGB->QueryInterface(IID_IMediaEvent,	(void **)&pME);
						pGB->QueryInterface(IID_IMediaPosition, (void**)&pMP);

		if (pMC == NULL || pMP== NULL )
		{
			MSG("Error on create pMC or pMP");
			return;
		}

			// Set up one-shot mode.
	hr = pGrabber->SetBufferSamples(TRUE);
	if (FAILED(hr))
	{
		MSG("error setbuffersamples");
		return;
	}
	hr = pGrabber->SetOneShot(TRUE);
	if (FAILED(hr))
		return;

	//typedef double REFTIME;
	REFTIME duration;
	pMP->get_Duration(&duration);
	int NumSecs = int(duration/10000000);
	
	REFTIME rtStart = 1 * 10000000;
	if (NumSecs < 1)
		rtStart = 0;
	REFTIME rtStop = rtStart; 

	hr=pMP->put_CurrentPosition(rtStart);
	hr=pMP->put_StopTime(rtStop);
	
	hr=pVW->put_AutoShow(OAFALSE);

	hr=pMC->Run();
	
//    // Set the graph clock.
//    IMediaFilter *pMediaFilter = 0;
//    pGB->QueryInterface(IID_IMediaFilter, (void**)&pMediaFilter);
//	IReferenceClock *pClock = 0;
//    pMediaFilter->SetSyncSource(pClock);
//    pClock->Release();
//    pMediaFilter->Release();


	long evCode;
	hr=pME->WaitForCompletion(INFINITE, &evCode);
//	hr = pSeek->SetPositions(&rtStart, AM_SEEKING_AbsolutePositioning, 
//							 &rtStop, AM_SEEKING_AbsolutePositioning);
	if (FAILED(hr))
		return;
	AM_MEDIA_TYPE MediaType;
	ZeroMemory(&MediaType,sizeof(MediaType));
	hr = pGrabber->GetConnectedMediaType(&MediaType); 
	if (FAILED(hr))
		return;

		// Get a pointer to the video header.
	VIDEOINFOHEADER *pVideoHeader = (VIDEOINFOHEADER*)MediaType.pbFormat;
	if (pVideoHeader == NULL)
	{
		MSG("VIDEOINFORHEADER");
		return;
	}

	// The video header contains the bitmap information. 
	// Copy it into a BITMAPINFO structure.
	BITMAPINFO BitmapInfo;
	ZeroMemory(&BitmapInfo, sizeof(BitmapInfo));
	CopyMemory(&BitmapInfo.bmiHeader, &(pVideoHeader->bmiHeader), sizeof(BITMAPINFOHEADER));

	// Create a DIB from the bitmap header, and get a pointer to the buffer.
	long size = 0;
	hr = pGrabber->GetCurrentBuffer(&size, NULL);
	void *buffer = new char[size];
	
	//HBITMAP hBitmap = ::CreateDIBSection(0, &BitmapInfo, DIB_RGB_COLORS, &buffer, NULL, 0);
	//GdiFlush();
	// Copy the image into the buffer.
	hr = pGrabber->GetCurrentBuffer(&size, (long *)buffer);
	if (FAILED(hr))
		return ;
	long Width = pVideoHeader->bmiHeader.biWidth;
	long Height = pVideoHeader->bmiHeader.biHeight;
	
	HDC hdc=GetDC(g_hwnd);
	SetDIBitsToDevice(
    hdc, 0, 0, 
   Width,
   Height,
    0, 0, 
    0,
   Height,
    buffer,
    &BitmapInfo,
    DIB_RGB_COLORS
);
ReleaseDC(g_hwnd,hdc);
// Free the format block when you are done:
FreeMediaType(MediaType);

					}
				}break;
			case ID_HETHONG_THOAT:
				{
					SendMessage(g_hwnd,WM_CLOSE,0,0);	
				}break;
			//menu qua trinh xu ly
			case ID_QUATRINHXULY_BATDAU:
				{
					if((pMC==NULL)||(pMP==NULL)||(pGB==NULL))
					{
						MSG("Phai ket noi du lieu tai menu File!");
						return;
					}                 
//						pMC->Stop();
//						pMP->put_CurrentPosition(0);
//						pMC->Run();
   //                 
                        pMC->Stop;
                        callback.car_number=0;
                        callback.motobike_number=0;
                        callback.car_speed=0;
                        callback.motobike_speed=0;
                         callback.start_time=timeGetTime();
                        
                         connect_file(FileName);  

				}break;
			case ID_QUATRINHXULY_TAMDUNG:
				{
					if((pMC==NULL)||(pMP==NULL)||(pGB==NULL))
					{
						MSG("Phai ket noi du lieu tai menu File!");
						return;
					}
					pMC->Pause();	
				}break;
			case ID_QUATRINHXULY_KETTHUC:
				{	
					if((pMC==NULL)||(pMP==NULL)||(pGB==NULL))
					{
						MSG("Phai ket noi du lieu tai menu File!");
						return;
					}					
						if(FAILED(pMC->Stop()))
						{
							MSG("Can't play this file")	;
							return;
						}
						pMP->put_CurrentPosition(0);   // return to beginning
				}break;
			//menu ket qua
			case ID_KETQUA_TACHDOITUONG:
				{
					
				}break;
			case ID_KETQUA_DEMSODOITUONG:
				{
					
				}break;
			case ID_KETQUA_BAMDOITUONG:
				{
					
				}break;
			case ID_KETQUA_KETQUATOANBO:
				{
					
				}break;
			//menu huong dan su dung
			case ID_HUONGDANSUDUNG_TACGIA:
				{
					MessageBox(g_hwnd,"Mäi chi tiÕt xin liªn hÖ: Lª Quèc Anh, Phßng thÝ nghiÖm M« h×nh ho¸-M« pháng, ViÖn Tªn Löa","Mäi chi tiÕt xin liªn hÖ: Lª Quèc Anh, ViÖn Tªn Löa",MB_OK);
				}break;
			case ID_HUONGDANSUDUNG_TAILIEUHUONGDAN:
				{
						// TODO: Add your command handler code here
	DWORD data=0;			   	
	::WinHelp(g_hwnd,"mophong_muctieu.hlp",HELP_CONTENTS,data );
				}break;
			case ID_HUONGDANSUDUNG_CACTHUATTOANCHINH:
				{
					
				}break;
			}
}

void CApplication::kill_dshow()
{	

	SafeRelease(pGrabberF);
	SafeRelease(pGrabber);
	SafeRelease(pSrc);
	SafeRelease(NullRenderFilter);
	SafeRelease(pMediaFilter);
	SafeRelease(pAudioDecoder);
	SafeRelease(pAudioRender);
	SafeRelease(pStreamSplitter);
	SafeRelease(pAviDecompressor);
	SafeRelease(pVideoRender);
	//Stop previewing data
	if(pMC)
		pMC->StopWhenReady();
	//Stop receiving events
//	if(pME)
//		pME->SetNotifyWindow(NULL,WM_P_GRAPHNOTIFY WM_APP,0);
	//Relinquish ownership (IMPORTANT!) of the video window.
	//Failing to call put_Owner can lead to assert failures within
	//the video renderer, as it still assumes that it has a valid
	//parent window.
	if(pVW)
	{
		pVW->put_Visible(OAFALSE);
		pVW->put_Owner(NULL);
	}

//#ifdef REGISTER_FILTERGRAPH
//	//Remove filter graph from the running object table
//	if(g_dwGraphRegister)
//		RemoveGraphFromRot(g_dwGraphRegister);
//#endif

	SafeRelease(pCaptureG);
	SafeRelease(pGB);
	SafeRelease(pMC);
	SafeRelease(pVW);
	SafeRelease(pME);
	SafeRelease(pMP);

}

HRESULT CApplication::init_dshow()
{
	


	HRESULT hr=S_OK;
			//This creates the filter graph manager
		if(FAILED(CoCreateInstance(CLSID_FilterGraph, NULL, CLSCTX_INPROC_SERVER,          
			IID_IGraphBuilder, (void **)&pGB)))
		{
			MSG("Error on create GraphBuilder");
			kill_dshow();
			return E_FAIL ;
		}
		//Create the capture graph builder  
    hr=CoCreateInstance (CLSID_CaptureGraphBuilder2 , NULL, CLSCTX_INPROC,
                           IID_ICaptureGraphBuilder2, (void **) &pCaptureG);
    if (FAILED(hr))
        return hr;

// Create the Sample Grabber.

	hr = CoCreateInstance(CLSID_SampleGrabber, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pGrabberF);
	if (FAILED(hr))
	{
    // Return an error.
	MSG("Error on create pGrabberF");
	kill_dshow();
	return E_FAIL;
	}
hr = pGB->AddFilter(pGrabberF, L"Sample Grabber");
if (FAILED(hr))
{
    // Return an error.
	MSG("Error AddFilter pGrabberF");
	kill_dshow();
	return E_FAIL;
}

hr=pGrabberF->QueryInterface(IID_ISampleGrabber, (void**)&pGrabber);

AM_MEDIA_TYPE mt;
ZeroMemory(&mt, sizeof(AM_MEDIA_TYPE));
mt.majortype = MEDIATYPE_Video;
mt.subtype = MEDIASUBTYPE_RGB24;
mt.formattype = FORMAT_VideoInfo; 
hr = pGrabber->SetMediaType(&mt);
	if (FAILED(hr))
	{
		MSG("Error on SetMediaType");
		kill_dshow();
		return E_FAIL;
	}

						pGB->QueryInterface(IID_IMediaControl, (void **)&pMC);
						pGB->QueryInterface(IID_IVideoWindow, (void **)&pVW);
						pGB->QueryInterface(IID_IMediaEvent,	(void **)&pME);
						pGB->QueryInterface(IID_IMediaPosition, (void**)&pMP);
						pGB->QueryInterface(IID_IMediaFilter, (void**)&pMediaFilter);
    	

		if (pMC == NULL || pMP== NULL || pVW==NULL || pME==NULL || pMediaFilter==NULL)
		{
			MSG("Error on create pMC or pMP");
			kill_dshow();
			return E_FAIL;
		}
//		pMediaFilter->SetSyncSource(NULL); //De tang toc do

		hr = pGrabber->SetBufferSamples(FALSE);
	if (FAILED(hr))
	{
		MSG("error setbuffersamples");
		kill_dshow();
		return E_FAIL;
	}
    // Only grab one at a time, stop stream after
    // grabbing one sample
    //
//    hr = pGrabber->SetOneShot( TRUE );
//	if (FAILED(hr))
//	{
//		MSG("coloi");
//		kill_dshow();
//		return E_FAIL;
//	}


	  hr = pGrabber->SetCallback( &callback, 1 );



return hr;
}

HRESULT CApplication::get_bitmap(LPTSTR pszFileName, BITMAPINFOHEADER **ppbmih)
{
return S_OK;
}

HRESULT CApplication::ConnectFilters(IGraphBuilder *pGraph, IBaseFilter *pSrc, IBaseFilter *pDest)
{
    if ((pGraph == NULL) || (pSrc == NULL) || (pDest == NULL))
    {
        return E_POINTER;
    }

    // Find an output pin on the first filter.
    IPin *pOut = 0;
    HRESULT hr = GetUnconnectedPin(pSrc, PINDIR_OUTPUT, &pOut);
    if (FAILED(hr)) 
    {
        return hr;
    }
    hr = ConnectFilters(pGraph, pOut, pDest);
    pOut->Release();
    return hr;

}

HRESULT CApplication::GetUnconnectedPin(IBaseFilter *pFilter, PIN_DIRECTION PinDir, IPin **ppPin)
{
    *ppPin = 0;
    IEnumPins *pEnum = 0;
    IPin *pPin = 0;
    HRESULT hr = pFilter->EnumPins(&pEnum);
    if (FAILED(hr))
    {
        return hr;
    }
    while (pEnum->Next(1, &pPin, NULL) == S_OK)
    {
        PIN_DIRECTION ThisPinDir;
        pPin->QueryDirection(&ThisPinDir);
        if (ThisPinDir == PinDir)
        {
            IPin *pTmp = 0;
            hr = pPin->ConnectedTo(&pTmp);
            if (SUCCEEDED(hr))  // Already connected, not the pin we want.
            {
                pTmp->Release();
            }
            else  // Unconnected, this is the pin we want.
            {
                pEnum->Release();
                *ppPin = pPin;
                return S_OK;
            }
        }
        pPin->Release();
    }

    pEnum->Release();
    // Did not find a matching pin.
    return E_FAIL;

}

HRESULT CApplication::ConnectFilters(IGraphBuilder *pGraph, IPin *pOut, IBaseFilter *pDest)
{
    if ((pGraph == NULL) || (pOut == NULL) || (pDest == NULL))
    {
        return E_POINTER;
    }
#ifdef debug
        PIN_DIRECTION PinDir;
        pOut->QueryDirection(&PinDir);
        _ASSERTE(PinDir == PINDIR_OUTPUT);
#endif

    // Find an input pin on the downstream filter.
    IPin *pIn = 0;
    HRESULT hr = GetUnconnectedPin(pDest, PINDIR_INPUT, &pIn);
    if (FAILED(hr))
    {
        return hr;
    }
    // Try to connect them.
    hr = pGraph->Connect(pOut, pIn);
    pIn->Release();
    return hr;

}

void CApplication::FreeMediaType(AM_MEDIA_TYPE &mt)
{
    if (mt.cbFormat != 0)
    {
        CoTaskMemFree((PVOID)mt.pbFormat);
        mt.cbFormat = 0;
        mt.pbFormat = NULL;
    }
    if (mt.pUnk != NULL)
    {
        // Unecessary because pUnk should not be used, but safest.
        mt.pUnk->Release();
        mt.pUnk = NULL;
    }
}

void CApplication::connect_file(LPTSTR filename)
{
		kill_dshow();        
		if FAILED(init_dshow())
		{
			MSG("Loi khoi tao DirectShow!");
			return;
		}
		WCHAR *MediaFile=new WCHAR[strlen(filename)+1];
	
		MultiByteToWideChar(CP_ACP, 0,  filename , -1, MediaFile, strlen(filename)+1);
	
	HRESULT hr = pGB->AddSourceFilter(MediaFile, L"Source", &pSrc);
	if (FAILED(hr))
	{
	    // Return an error code.+
		MSG("ERROR");
		kill_dshow();
		return;
	}

	//hr = ConnectFilters(pGB, pSrc, pGrabberF);
	//Create Stream Spilitter
	hr = CoCreateInstance(CLSID_MPEG1Splitter, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pStreamSplitter);
	if (FAILED(hr))
	{
    // Return an error.
	MSG("Error on create Stream Splitter Filter");
	kill_dshow();
	return;
	}
hr = pGB->AddFilter(pStreamSplitter, L"MPEG-1 Stream Splitter");
if (FAILED(hr))
{
    // Return an error.
	MSG("Error Add Sream Splitter Filter");
	kill_dshow();
	return;
}
hr = ConnectFilters(pGB, pSrc, pStreamSplitter);
hr = ConnectFilters(pGB, pStreamSplitter, pGrabberF);
	//Add Video Render filter to our graph
	if(FAILED(CoCreateInstance(CLSID_VideoRenderer, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pVideoRender)))	
	{
    // Return an error.
	MSG("Error on create Video Render Filter");
	kill_dshow();
	return;
	}
if(FAILED(pGB->AddFilter(pVideoRender, L"Video Renderer")))
{
    // Return an error.
	MSG("Error Add Video Render Filter");
	kill_dshow();
	return;
}

//	//create Null Render Filter
//	hr = CoCreateInstance(CLSID_NullRenderer, NULL, CLSCTX_INPROC_SERVER,
//   IID_IBaseFilter, (void**)&NullRenderFilter);
//	if (FAILED(hr))
//	{
//    // Return an error.
//	MSG("Error on create Null Render Filter");
//	kill_dshow();
//	return;
//	}
//hr = pGB->AddFilter(NullRenderFilter, L"Null Renderer");
//if (FAILED(hr))
//{
//    // Return an error.
//	MSG("Error Add Null Render Filter");
//	kill_dshow();
//	return;
//}
hr = ConnectFilters(pGB, pGrabberF, pVideoRender);
//Create Audio Decoder Filter
	hr = CoCreateInstance(CLSID_CMpegAudioCodec, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pAudioDecoder);
	if (FAILED(hr))
	{
    // Return an error.
	MSG("Error on create Audio Decoder Filter");
	kill_dshow();
	return;
	}
hr = pGB->AddFilter(pAudioDecoder, L"Audio Decoder");
if (FAILED(hr))
{
    // Return an error.
	MSG("Error Add Audio Decoder Filter");
	kill_dshow();
	return;
}
hr = ConnectFilters(pGB, pStreamSplitter, pAudioDecoder);
//Create Audio Render
	hr = CoCreateInstance(CLSID_DSoundRender, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pAudioRender);
	if (FAILED(hr))
	{
    // Return an error.
	MSG("Error on create Audio Render Filter");
	kill_dshow();
	return;
	}
hr = pGB->AddFilter(pAudioRender, L"DirectSound Renderer");
if (FAILED(hr))
{
    // Return an error.
	MSG("Error Add Audio Render Filter");
	kill_dshow();
	return;
}
hr = ConnectFilters(pGB, pAudioDecoder, pAudioRender);

	AM_MEDIA_TYPE MediaType;
    hr = pGrabber->GetConnectedMediaType( &MediaType);

    VIDEOINFOHEADER * vih = (VIDEOINFOHEADER*) MediaType.pbFormat;
    callback.width  = vih->bmiHeader.biWidth;
    callback.height = vih->bmiHeader.biHeight;

    //Show image width & hight in title of windows
//	TCHAR szFilename[MAX_PATH];
//     wsprintf(szFilename,"Width=%d, Height=%d",callback.width,callback.height );
//	  SetWindowText(g_hwnd,szFilename);

	//init bitmapinfo to preview on screen
	ZeroMemory(&callback.BitmapInfo, sizeof(callback.BitmapInfo));
	CopyMemory(&callback.BitmapInfo.bmiHeader, &(vih->bmiHeader), sizeof(BITMAPINFOHEADER));

    FreeMediaType( MediaType);
    
//	hr=pVW->put_AutoShow(OAFALSE);	
	   // Set the video window to be a child of the main window
    if(FAILED(pVW->put_Owner((OAHWND)g_hwnd)))    
        return;
    
    // Set video window style
    if(FAILED(pVW->put_WindowStyle(WS_CHILD | WS_CLIPCHILDREN)))    
        return;


	pVW->put_AutoShow(OATRUE);	
// Make the video window visible, now that it is properly positioned
    if(FAILED(pVW->put_Visible(OATRUE)))    
        return ;
   
		
	hr=pMC->Run();
		
	//long evCode;
	//hr=pME->WaitForCompletion(INFINITE, &evCode);
	if (FAILED(hr))
	{
		MSG("co loi MC-Run()");
		kill_dshow();
		return;
	}
		SafeDelete(MediaFile);
ResizeVideoWindow();
Show_Result(0,0,0,0,0);
}

void CApplication::connect_camera()
{
		kill_dshow();
		if (FAILED(init_dshow()))
		{
			MSG("Sai init_dshow()");
			return;		
		}
		//Attach the filter graph to the capture graph
		if(FAILED(pCaptureG->SetFiltergraph(pGB)))
			return;

		//Use the system device enumerator and class enumerator to find
		//a video capture/preview device, such as a desktop USB video camera
		if(FAILED(FindCaptureDevice(&pSrc)))
			return;
		//Add Capture filter to our graph
		if(FAILED(pGB->AddFilter(pSrc,L"Video Capture")))
		{
			MSG("The capture device is being used by another application");
			kill_dshow();
			return;
		}
		//Add AVI Decompressor filter to our graph	
	if(FAILED(CoCreateInstance(CLSID_AVIDec, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pAviDecompressor)))	
	{
    // Return an error.
	MSG("Error on create Avi Decompressor Filter");
	kill_dshow();
	return;
	}
if(FAILED(pGB->AddFilter(pAviDecompressor, L"AVI Decompressor")))
{
    // Return an error.
	MSG("Error Add Avi Decompressor Filter");
	kill_dshow();
	return;
}
		//Connect Source to Grabber
	  ConnectFilters(pGB, pSrc, pAviDecompressor);

	//Add Video Render filter to our graph
	if(FAILED(CoCreateInstance(CLSID_VideoRenderer, NULL, CLSCTX_INPROC_SERVER,
    IID_IBaseFilter, (void**)&pVideoRender)))	
	{
    // Return an error.
	MSG("Error on create Video Render Filter");
	kill_dshow();
	return;
	}
if(FAILED(pGB->AddFilter(pVideoRender, L"Video Renderer")))
{
    // Return an error.
	MSG("Error Add Video Render Filter");
	kill_dshow();
	return;
}
ConnectFilters(pGB, pAviDecompressor, pGrabberF);
ConnectFilters(pGB, pGrabberF, pVideoRender);
//Them Audio capture filter
		//Use the system device enumerator and class enumerator to find
		//a audio capture device, such as a USB Audio Device
//		if(FAILED(FindAudioDevice(&pAudioDecoder)))
//			return;
//		//Add Capture filter to our graph
//        
//		if(FAILED(pGB->AddFilter(pSrc,L"Audio Capture")))
//		{
//			MSG("The capture device is being used by another application");
//			kill_dshow();
//			return;
//		}


	AM_MEDIA_TYPE MediaType;
    pGrabber->GetConnectedMediaType( &MediaType);

    VIDEOINFOHEADER * vih = (VIDEOINFOHEADER*) MediaType.pbFormat;
    callback.width  = vih->bmiHeader.biWidth;
    callback.height = vih->bmiHeader.biHeight;
//     TCHAR szFilename[MAX_PATH];
//      wsprintf(szFilename,"Width=%d, Height=%d",callback.width,callback.height );
//	  SetWindowText(g_hwnd,szFilename);

	//init bitmapinfo to preview on screen
	ZeroMemory(&callback.BitmapInfo, sizeof(callback.BitmapInfo));
	CopyMemory(&callback.BitmapInfo.bmiHeader, &(vih->bmiHeader), sizeof(BITMAPINFOHEADER));

    FreeMediaType( MediaType);
    
	   // Set the video window to be a child of the main window
    if(FAILED(pVW->put_Owner((OAHWND)g_hwnd)))    
        return;
    
    // Set video window style
    if(FAILED(pVW->put_WindowStyle(WS_CHILD | WS_CLIPCHILDREN)))    
        return;

	pVW->put_AutoShow(OATRUE);	
// Make the video window visible, now that it is properly positioned
    if(FAILED(pVW->put_Visible(OATRUE)))    
        return ;

	pMC->Run();
	ResizeVideoWindow();
    Show_Result(0,0,0,0,0);
}


HRESULT CApplication::FindCaptureDevice(IBaseFilter **ppSrcFilter)
{
    HRESULT hr;
    IBaseFilter * pSrc = NULL;
    CComPtr <IMoniker> pMoniker =NULL;
    ULONG cFetched;

    if (!ppSrcFilter)
        return E_POINTER;
   
    // Create the system device enumerator
    CComPtr <ICreateDevEnum> pDevEnum =NULL;

    hr = CoCreateInstance (CLSID_SystemDeviceEnum, NULL, CLSCTX_INPROC,
                           IID_ICreateDevEnum, (void **) &pDevEnum);
    if (FAILED(hr))
    {        
			MSG("Couldn't create system enumerator!");
        return hr;
    }

    // Create an enumerator for the video capture devices
    CComPtr <IEnumMoniker> pClassEnum = NULL;

    hr = pDevEnum->CreateClassEnumerator (CLSID_VideoInputDeviceCategory, &pClassEnum, 0);
    if (FAILED(hr))
    {
        MSG("Couldn't create class enumerator! ");
        return hr;
    }

    // If there are no enumerators for the requested type, then 
    // CreateClassEnumerator will succeed, but pClassEnum will be NULL.
    if (pClassEnum == NULL)
    {
        MSG("No video capture device was detected.");
        return E_FAIL;
    }

    // Use the first video capture device on the device list.
    // Note that if the Next() call succeeds but there are no monikers,
    // it will return S_FALSE (which is not a failure).  Therefore, we
    // check that the return code is S_OK instead of using SUCCEEDED() macro.
    if (S_OK == (pClassEnum->Next (1, &pMoniker, &cFetched)))
    {
        // Bind Moniker to a filter object
        hr = pMoniker->BindToObject(0,0,IID_IBaseFilter, (void**)&pSrc);
        if (FAILED(hr))
        {
            MSG("Couldn't bind moniker to filter object!");
            return hr;
        }
    }
    else
    {
        MSG("Unable to access video capture device!");   
        return E_FAIL;
    }

    // Copy the found filter pointer to the output parameter.
    // Do NOT Release() the reference, since it will still be used
    // by the calling function.
    *ppSrcFilter = pSrc;

    return hr;
}

HRESULT CApplication::FindAudioDevice(IBaseFilter **ppAudioSource)
{
return S_OK;
}

void CApplication::ResizeVideoWindow()
{
    // Resize the video preview window to match owner window size
    if (pVW)
    {
       // RECT rc;
        
        // Make the preview video fill our window
    //    GetClientRect(g_hwnd, &rc);
        //pVW->SetWindowPosition(100, 0, rc.right-200, rc.bottom-100);		
		pVW->SetWindowPosition(40,15,320,240);
HDC hdc=GetDC(g_hwnd);
     // Create a green pen.
 HPEN hpen = CreatePen(PS_SOLID, 10, RGB(0,0, 155));
     // Create a red brush.
 HBRUSH  hbrush = CreateSolidBrush(RGB(255,255, 255));
 RECT rc;
 GetClientRect(g_hwnd, &rc);
 
 SelectObject(hdc, hpen);
SelectObject(hdc, hbrush);
 Rectangle(hdc, 35,10, 366,260);

 Rectangle(hdc, 35,280, 366,260+270);

 //SetTextColor(hdc,RGB(223,232,23));
 //TextOut(hdc,rc.right/2+242,20,"KÕt qu¶",7);
 
 DeleteObject(hpen);
 DeleteObject(hbrush);
 ReleaseDC(g_hwnd,hdc);

    }
}





void CApplication::Show_Result(long time_total, long car_number, long motobike_number, double car_speed, double motobike_speed)
{
 HDC hdc=GetDC(g_hwnd);
     // Create a red pen.
 HPEN hpen = CreatePen(PS_SOLID, 5, RGB(0,0, 155));
     // Create a yellow brush.
 HBRUSH  hbrush = CreateSolidBrush(RGB(225,225, 155));
 RECT rc;
 GetClientRect(g_hwnd, &rc);
 HFONT font=CreateFont(42, 
	 // nHeight
   0,                         // nWidth
   0,                         // nEscapement
   0,                         // nOrientation
   FW_NORMAL,                 // nWeight
   FALSE,                     // bItalic
   FALSE,                     // bUnderline
   0,                         // cStrikeOut
   ANSI_CHARSET,              // nCharSet
   OUT_DEFAULT_PRECIS,        // nOutPrecision
   CLIP_DEFAULT_PRECIS,       // nClipPrecision
   DEFAULT_QUALITY,           // nQuality
   DEFAULT_PITCH | FF_SWISS,  // nPitchAndFamily
   _T(".VN3DH"));                 // lpszFacename
 
 SelectObject(hdc, hpen);
SelectObject(hdc, hbrush);
 Rectangle(hdc, rc.right/2-15,7, rc.right-10,rc.bottom-30);

 SelectObject(hdc,font);
 SetTextColor(hdc,RGB(223,0,0));
 SetBkColor(hdc,RGB(225,225, 155));
TextOut(hdc,rc.right/2+142,20,"KÕt qu¶",7);
font=CreateFont(
	24, 					 // nHeight
   0,                         // nWidth
   0,                         // nEscapement
   0,                         // nOrientation
   FW_NORMAL,                 // nWeight
   FALSE,                     // bItalic
   FALSE,                     // bUnderline
   0,                         // cStrikeOut
   ANSI_CHARSET,              // nCharSet
   OUT_DEFAULT_PRECIS,        // nOutPrecision
   CLIP_DEFAULT_PRECIS,       // nClipPrecision
   DEFAULT_QUALITY,           // nQuality
   DEFAULT_PITCH | FF_SWISS,  // nPitchAndFamily
   _T(".VnTime"));                 // lpszFacename
 SelectObject(hdc,font);
 SetTextColor(hdc,RGB(0,0,0));
 SelectObject(hdc,hpen);
    char d[50];
    sprintf(d,"Tæng thêi gian:                                     ");
 TextOut(hdc, rc.right/2,67,d,strlen(d));
 sprintf(d,"Sè l­îng xe « t«:                                    ");
 TextOut(hdc, rc.right/2,107,d,strlen(d)); 
 sprintf(d,"Sè l­îng xe m¸y:                                   ");
 TextOut(hdc, rc.right/2,147,d,strlen(d));
 sprintf(d,"VËn tèc trung b×nh cña « t«:                 ");
 TextOut(hdc, rc.right/2,187,d,strlen(d));
 sprintf(d,"VËn tèc trung b×nh cña xe m¸y:           ");
 TextOut(hdc, rc.right/2,227,d,strlen(d));

DeleteObject(font);

 DeleteObject(hpen);
 DeleteObject(hbrush);
 ReleaseDC(g_hwnd,hdc);


callback.start_time=timeGetTime();
callback.car_number=0;
callback.car_speed=0;
callback.motobike_number=0;
callback.motobike_speed=0;

}
