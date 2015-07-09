//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

#include "Application.h"

CApplication App;
//
void NOP(HINSTANCE p_prev_instance,LPSTR p_cmd_line,int p_show){

   p_prev_instance=p_prev_instance;
   p_cmd_line=p_cmd_line;
   p_show=p_show;
}
//
int APIENTRY WinMain(HINSTANCE p_instance,HINSTANCE p_prev_instance,LPSTR p_cmd_line,int p_show)
	{
	//In win32 the p_prev_instance is ignored
    NOP(p_prev_instance,p_cmd_line,p_show);

    if( SUCCEEDED(App.Create( p_instance) ))
	{		
        return App.Run();
	}
    return 0;
	}
