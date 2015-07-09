// SampleGrabberCB.cpp: implementation of the CSampleGrabberCB class.
//Written by Le Quoc Anh - Vien Ten Lua
//Address: 6 Hoang Sam, Cau Giay, Ha Noi
//Vinaphone: 0912643289

//////////////////////////////////////////////////////////////////////

#include "SampleGrabberCB.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
BYTE digital[]={124,198,206,222,246,230,124,0,48,112,48,48,48,48,
252,0,120,204,12,56,96,204,252,0,120,204,12,56,12,204,120,0,28,60,
108,204,254,12,30,0,252,192,248,12,12,24,120,0,56,88,192,248,204,
204,120,0,252,204,12,24,48,48,48,0,120,204,204,120,204,204,120,
0,120,204,204,124,12,24,112,0};
#define SizeHeight   7;
BYTE BaseSize[8][8];
BYTE ZoomSize[];

void WriteDigital(LPTSTR filename, int x, int y, int size, BYTE r, BYTE g, BYTE b)//320x240
{
    int i,j;
    BYTE BitStr[8];
    BYTE Pos;
    char Letter[255];
    char TempSt[255];
    int SizeZ, X1, X2, Y1, Y2;
    BYTE Tem, Dodai;
// kiem tra xem can phai tang chieu nao cua chu
Dodai = strlen(St);
Temp = (Size - 8);
SizeZ = SizeHeight + (Temp) \ 2;
//khai bao lai mang kich thuoc cua font chu moi
ReDim Zoomsize(0 To SizeZ, 0 To (SizeZ + 1) * Dodai - 1) As Byte
TempSt = St
Object.ScaleMode = 3 ' dat che do pixel
X1 = 0
X2 = SizeZ
Y1 = 0
Y2 = SizeZ
While St <> ""
    Letter = Left(St, 1)
    Pos = (Asc(Letter) - 48) * 8
    ' ve tung chu 1 len ma tran kich thuoc co so
    For i = 0 To 7
         ConvertByteToBit Digital(Pos + i), BitStr
         ' ve xau bit len tren doi tuong can ve
         For j = 7 To 0 Step -1
            BaseSize(i, 7 - j) = BitStr(j)
         Next j
    Next i
    ' phong to chu theo kich thuoc chuan
     Recstretch 0, 0, 7, 7, X1, Y1, X2, Y2, Zoomsize
    St = Mid(St, 2)
    X1 = X2 + 1
    X2 = X1 + SizeZ
Wend
' Ve ra doi tuong can thiet
Temp = (SizeZ + 1) * Dodai - 1
For i = 0 To SizeZ
 For j = 0 To Temp
  If Zoomsize(i, j) = 1 Then
    Object.PSet (X + j, Y + i), Color
  End If
 Next j
Next i

}

CSampleGrabberCB::CSampleGrabberCB()
{
temp1=NULL;
temp2=NULL;
temp3=NULL;

motobike_speed=0;
car_speed=0;
start_time=0;
motobike_number=0;
car_number=0;

vehicle_count=0;
}

CSampleGrabberCB::~CSampleGrabberCB()
{
SafeDelete(temp1);
SafeDelete(temp2);
SafeDelete(temp3);
}

STDMETHODIMP CSampleGrabberCB::BufferCB(double SampleTime, BYTE *pBuffer, long BufferSize)
{  
//    return FrameDifferencing(SampleTime,pBuffer,BufferSize);
    int i,j;
    BYTE r1,g1,b1,r2,g2,b2,r,g,b;
    BYTE bb=20;

    //Initialize memory templates
    if(temp1==NULL)
	{
		temp1=new BYTE[BufferSize];
		memcpy(temp1,pBuffer,BufferSize);
		x0=5;
		y0=35;
		return 0;
	}
    if(temp2==NULL)
	{
		temp2=new BYTE[BufferSize];
		memcpy(temp2,pBuffer,BufferSize);
		return 0;
	}

	if(temp3==NULL)
	{
		temp3=new BYTE[BufferSize];
		memcpy(temp3,pBuffer,BufferSize);
        for(i=0;i<BitmapInfo.bmiHeader.biWidth;i++)
            for(j=0;j<BitmapInfo.bmiHeader.biHeight;j++)
                set_color(temp3,i,j,175,255,255);       
	}


//Edge detection

    FillMemory(pattern,sizeof(pattern),-1);
    for(i=x0;i<BitmapInfo.bmiHeader.biWidth-x0;i++)
        for(j=y0;j<BitmapInfo.bmiHeader.biHeight/2-23;j++)
        {
            get_color(pBuffer,i,j,&r,&g,&b);
            get_color(pBuffer,i+1,j,&r1,&g1,&b1);
            get_color(pBuffer,i,j+1,&r2,&g2,&b2);
     
    if((sqrt((r-r1)*(r-r1)+(g-g1)*(g-g1)+(b-b1)*(b-b1))>=bb)||
       (sqrt((r-r2)*(r-r2)+(g-g2)*(g-g2)+(b-b2)*(b-b2))>=bb))
    {
 //       set_color(pBuffer,i,j,255,255,255);     
        pattern[i][j]=1;
        pattern[i][j+1]=1;
        pattern[i+1][j]=1;
        pattern[i][j-1]=1;
        pattern[i-1][j]=1;
    }
    else    
    {
    //    set_color(pBuffer,i,j,0,0,0);    
        pattern[i][j]=0;
    }

        }

//Filling areas without objects
i=0;
j=0;
int tox[4]={0,0,-1,1};
int toy[4]={1,-1,0,0};
int k,x,y;
int dem=1;

int x1,y1,x2,y2,nho;
bool ok;
//initialize stack
for(k=40;k<60;k++)
if(pattern[35][k]==0)
{
    stack_x[dem]=35;
    stack_y[dem]=k;
    pattern[35][k]=3;
    break;
}
//start filling
while(dem>0)
{  
    i=stack_x[dem];
    j=stack_y[dem];
    dem=dem-1;
        for(k=0;k<4;k++)
        {
            x=i+tox[k];
            y=j+toy[k];
            if(pattern[x][y]==0)
            {
                dem++;
                stack_x[dem]=x;
                stack_y[dem]=y;                
                pattern[x][y]=3; 
            }
        }
}


//Using frame difference to contribute blobs
for(i=x0;i<BitmapInfo.bmiHeader.biWidth-x0;i++)
for(j=y0;j<BitmapInfo.bmiHeader.biHeight/2-23;j++)
{
    get_color(pBuffer,i,j,&r,&g,&b);
    get_color(temp1,i,j,&r1,&g1,&b1);
    get_color(temp2,i,j,&r2,&g2,&b2);
    
    x=distance(r,g,b,r1,g1,b1);
    y=distance(r1,g1,b1,r2,g2,b2);
    
    if((x>bb)&&(y>bb))
    {
//        set_color(temp3,i,j,0,0,0);
        pattern[i][j]=0;

    }
//    else
//        set_color(temp3,i,j,175,255,255);       
}
memcpy(temp2,temp1,BufferSize);
memcpy(temp1,pBuffer,BufferSize);

//Show black blobs on bottom lelf of screen

for(i=x0;i<BitmapInfo.bmiHeader.biWidth-x0;i++)
for(j=y0;j<BitmapInfo.bmiHeader.biHeight/2-23;j++)
   if((pattern[i][j]!=3))
   {
       //set_color(temp3,i,j,0,0,0);
       get_color(pBuffer,i,j,&r,&g,&b);
       set_color(temp3,i,j,r,g,b);
   }

   else
       set_color(temp3,i,j,175,255,255);       




//Determine each black blob in image and recognize it
for(i=x0;i<BitmapInfo.bmiHeader.biWidth-x0;i++)
{
for(j=y0;j<BitmapInfo.bmiHeader.biHeight/2-23;j++)
if((pattern[i][j]!=3))//(=0 || = 1)
{
      x1=x2=i;
      y1=y2=j;                                              
      //Using filling algorithm to measure areas
      dem=1;
      stack_x[dem]=i;
      stack_y[dem]=j;
      
      while(dem>0)
      {                      
         nho=dem;
         dem=dem-1;
         
         x1=x1>stack_x[nho]?stack_x[nho]:x1;
         y1=y1>stack_y[nho]?stack_y[nho]:y1;
         x2=x2<stack_x[nho]?stack_x[nho]:x2;
         y2=y2<stack_y[nho]?stack_y[nho]:y2;
         for(k=0;k<4;k++)
         {
            x=stack_x[nho]+tox[k];
            y=stack_y[nho]+toy[k];
            if((pattern[x][y]!=3)&&(pattern[x][y]!=-1))
               {
                  dem++; 
                  stack_x[dem]=x;
                  stack_y[dem]=y;                
                  pattern[x][y]=3; 
                }
            }
      }

    //Recognize it
    nho=0;
    if(y2-y1>bus_width-10)                                
        if(x2-x1>bus_length-10)
            nho=1;                  
        else                         
            nho=0;                                    
    else
        if((y2-y1>bike_width+10)&&(y2-y1<car_width+10))                                                   
            if(x2-x1>car_length-10)
                nho=2;
            else 
                nho=0;                                    
        else                
            if((y2-y1>bike_width*1/2)&&(x2-x1>bike_length*1/2)&&(y2-y1<=bike_width+5)&&(x2-x1<bike_length+5))
                nho=3;           

   //Draw rectangle around it
          if(nho==1)
               draw_rectangle(pBuffer,x1,y1,x2-x1,y2-y1,255,0,255);
          else
               if(nho==2)               
                   draw_rectangle(pBuffer,x1,y1,x2-x1,y2-y1,0,255,0);
               else
                   if(nho==3)                   
                       draw_rectangle(pBuffer,x1,y1,x2-x1,y2-y1,0,0,255);                        
//test   
if(nho>0)
{
    vehicle_count_new++;
    Vehicle_new[vehicle_count_new].set_values(x1,y1,x2,y2,nho);
}
    for(x=x1;x<x2;x++)
    for(y=y1;y<y2;y++)
       pattern[x][y]=3;    

    ok=false;    
    if(nho>0)        
    {

    }//end if nho>0

}//end for
}//end for

//Initialize members of list as invisible in current frame
 for(k=1;k<=vehicle_count;k++)
     Vehicle[k].visibled=false;

//Tracking vehicles
for(i=1;i<=vehicle_count_new;i++)
{
    ok=true;
    for(j=1;j<=vehicle_count;j++)
        if(Vehicle_new[i].match(Vehicle[j].x1,Vehicle[j].y1,Vehicle[j].x2,Vehicle[j].y2, Vehicle[j].type ))
        {
            ok=false;
            Vehicle[j].set_values(Vehicle_new[i].x1,Vehicle_new[i].y1,Vehicle_new[i].x2,Vehicle_new[i].y2,Vehicle_new[i].type );            
            Vehicle[j].visibled=true;
            break;
        }
    if(ok)
    {
        if(Vehicle_new[i].x_center<bus_length)
        {
        vehicle_count++;        
        Vehicle[vehicle_count].set_values(Vehicle_new[i].x1,Vehicle_new[i].y1,Vehicle_new[i].x2,Vehicle_new[i].y2,Vehicle_new[i].type );
        Vehicle[vehicle_count].visibled=true;
        Vehicle[vehicle_count].set_time(timeGetTime());
        if(Vehicle_new[i].type==3)
            motobike_number++;
        else
            car_number++;        
        }        
    }    
//    else
//            draw_rectangle(temp3,Vehicle_new[i].x_center,Vehicle_new[i].y_center,2,2,255,0,0);            
        
}
vehicle_count_new=0;
//If a vehicle isn't visible in the current frame then it is removed from list
double tam;
j=vehicle_count;
for(k=1;k<=vehicle_count;k++)
if((Vehicle[k].visibled==false)&&(Vehicle[k].x_center>=bus_length))
{
  //Remove it and measure its speed
    if(Vehicle[k].type==3)
    {
        tam=Vehicle[k].x_center*3600*30/((timeGetTime()-Vehicle[k].start_time)*BitmapInfo.bmiHeader.biWidth );
        motobike_speed=(motobike_speed*(motobike_number-1)+tam)/motobike_number;
    }
    else
    {
        tam=Vehicle[k].x_center*3600*23/((timeGetTime()-Vehicle[k].start_time)*BitmapInfo.bmiHeader.biWidth );
        car_speed=(car_speed*(car_number-1)+tam)/car_number;
    }

   j=j-1;
    for(i=k;i<=vehicle_count;i++)
    {
        Vehicle[i]=Vehicle[i+1];
    }
    
}
vehicle_count=j;

Show_Result(timeGetTime()-start_time,car_number,motobike_number,car_speed,motobike_speed);

		HDRAWDIB hd=DrawDibOpen(); 
		HDC  hdc=GetDC(g_hwnd);
		DrawDibDraw(hd,hdc,40,285,320,240,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);		
		DrawDibClose(hd);		
	    ReleaseDC(g_hwnd,hdc);

return 0;
}

void CSampleGrabberCB::draw_line(BYTE *pBuffer, int x1, int y1, int x2, int y2, byte r, byte g, byte b)
{
	int k;
	if(y1==y2)
	{
	for(k=x1;k<x2;k++)
	{
			*(pBuffer+k*3+0+y1*width*3)=(unsigned char)r;
			*(pBuffer+k*3+1+y1*width*3)=(unsigned char)g;
			*(pBuffer+k*3+2+y1*width*3)=(unsigned char)b;
	}
	}
	else		
		{		
		for(k=y1;k<y2;k++)
		{
			*(pBuffer+x1*3+0+k*width*3)=(unsigned char)r;
			*(pBuffer+x1*3+1+k*width*3)=(unsigned char)g;			
			*(pBuffer+x1*3+2+k*width*3)=(unsigned char)b;
		}
		}
}

void CSampleGrabberCB::draw_rectangle(BYTE *pBuffer, int x1, int y1, int w, int h, byte r, byte g, byte b)
{	
int x2=x1+w;
int y2=y1+h;
//x2=x2>width?width:x2;
//y2=y2>height?height:y2;

draw_line(pBuffer,x1,y1,x1,y2,r,g,b);
draw_line(pBuffer,x1,y2,x2,y2,r,g,b);
draw_line(pBuffer,x1,y1,x2,y1,r,g,b);
draw_line(pBuffer,x2,y1,x2,y2,r,g,b);
}

STDMETHODIMP CSampleGrabberCB::MethodUsingReferenceImage(double SampleTime, BYTE *pBuffer, long BufferSize)
{    
    //At the first time, we save this frame as the reference image, then leave the method
    if(temp1==NULL)
    {
		temp1=new BYTE[BufferSize];
		memcpy(temp1,pBuffer,BufferSize);
        x0=5;
		y0=30;
		return 0;
    }
    if(temp3==NULL)
    {
        temp3=new BYTE[BufferSize];
        memcpy(temp3,pBuffer,BufferSize);
    }
    if(temp2==NULL)
    {
        temp2=new BYTE[BufferSize];
        memcpy(temp2,pBuffer,BufferSize);
    }
    //Since the second time, we absolute subtract the current frame from the reference image,
    //then the end result of subtraction is the moving objects.
    	
    BYTE pattern[350][250];
    BYTE update[350][250];

    int x=0;
    int y=0;
    int aa=20;
    int i=0;
    int j=0;
    int nx=0;
    int ny=0;
    BYTE mau1r,mau2r,mau1g,mau2g,mau1b,mau2b;

    FillMemory(update,sizeof(update),0);
    FillMemory(pattern,sizeof(pattern),0);
	for(x=x0;x<width-x0;x++)
		for(y=y0;y<height/2;y++)
		{
			mau1r=*(pBuffer+x*3+0+y*width*3);
			mau1g=*(pBuffer+x*3+1+y*width*3);
			mau1b=*(pBuffer+x*3+2+y*width*3);

			mau2r=*(temp1+x*3+0+y*width*3);
			mau2g=*(temp1+x*3+1+y*width*3);
			mau2b=*(temp1+x*3+2+y*width*3);

            mau1r=(BYTE)sqrt((mau1r-mau2r)*(mau1r-mau2r)+(mau1g-mau2g)*(mau1g-mau2g)+(mau1b-mau2b)*(mau1b-mau2b));
            if(mau1r>50)
            {
                pattern[x][y]=1;

            }            
        }

draw_rectangle(pBuffer,x0,y0,width/2,height/2-20,225,0,125);

	for(i=x0;i<width-x0;i++)
		for(j=y0;j<height/2;j++)
            if(pattern[i][j]==1)
        {

                   *(temp3+i*3+0+j*width*3)=255;
			       *(temp3+i*3+1+j*width*3)=255;
			       *(temp3+i*3+2+j*width*3)=255;

            }
            else
            {
                   *(temp3+i*3+0+j*width*3)=0;
			       *(temp3+i*3+1+j*width*3)=0;
			       *(temp3+i*3+2+j*width*3)=0;

            }
        		//Hien thi len man hinh(587,480)
		HDRAWDIB hd=DrawDibOpen(); 
		HDC  hdc=GetDC(g_hwnd);
		//DrawDibDraw(hd,hdc,80  ,0,640 ,480,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);
		DrawDibDraw(hd,hdc,40,285,320,240,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);		
		DrawDibClose(hd);	
        return 0;
    //memcpy(temp2,temp1,BufferSize);
    //memcpy(temp1,pBuffer,BufferSize);


	//for(x=x0;x<310+x0;x++)
	x=x0;
	while(x<width-x0-30)
	{		
		//for(y=y0;y<125+y0;y++)		
		y=y0;
		while(y<125+y0)
		{
			if(pattern[x][y]>40)
			{
//			    fprintf(f," x=%d",x);//; y=%d; value=%d",x,y,pattern[x][y]);
				//draw_rectangle(pBuffer,x,y,25,16,0,0,255);
				
					aa=pattern[x][y];
					nx=x;
					ny=y;
					for(i=x;i<x+25;i++)
						for(j=y;j<y+16;j++)
							if(aa<pattern[i][j])
							{
								aa=pattern[i][j];
								nx=i;
								ny=j;
							}			
                            else
                                pattern[i][j]=0;
					for(i=x;i<(nx+30>width-30?width-30:nx+30);i++)
					    for(j=(y-16<0?y-5:y-16);j<y+16;j++)
                        {
						    pattern[i][j]=0;
			       
                        }

				draw_rectangle(temp3,nx,ny,30,16,0,0,255);		
                for(i=nx;i<nx+30;i++)
                    for(j=ny;j<ny+16;j++)
                    {
                    //*(temp1+i*3+0+j*width*3)=*(temp2+i*3+0+j*width*3);;
			        //*(temp1+i*3+1+j*width*3)=*(temp2+i*3+1+j*width*3);;
			        //*(temp1+i*3+2+j*width*3)=*(temp2+i*3+2+j*width*3);;
                        update[i][j]=1;
                    }
				y=ny+20;
			}	
			y++;
		}
	//		fprintf(f,"\n");
			x++;
	}
//		fclose(f);
	for(x=x0;x<width-x0;x++)
		for(y=y0;y<height/2-10;y++)
            if(update[x][y]==0)
		{
                   *(temp1+i*3+0+j*width*3)=*(pBuffer+i*3+0+j*width*3);
			       *(temp1+i*3+1+j*width*3)=*(pBuffer+i*3+1+j*width*3);
			       *(temp1+i*3+2+j*width*3)=*(pBuffer+i*3+2+j*width*3);

        }
            else
            {
                   *(temp1+i*3+0+j*width*3)=0;
			       *(temp1+i*3+1+j*width*3)=0;
			       *(temp1+i*3+2+j*width*3)=0;

            }
        		//Hien thi len man hinh(587,480)
//		HDRAWDIB hd=DrawDibOpen(); 
//		HDC  hdc=GetDC(g_hwnd);
		//DrawDibDraw(hd,hdc,80  ,0,640 ,480,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);
//		DrawDibDraw(hd,hdc,40,285,320,240,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);		
//		DrawDibClose(hd);		

return 0;
}

BYTE* CSampleGrabberCB::Subtraction(BYTE *pBuffer1, BYTE *pBuffer2)
{
int i,j;
for(i=0;i<BitmapInfo.bmiHeader.biWidth;i++)
for(j=0;j<BitmapInfo.bmiHeader.biHeight;j++)
{
    *(pBuffer1+i*3+0+j*BitmapInfo.bmiHeader.biWidth *3)=abs((unsigned char)*(pBuffer1+i*3+0+j*BitmapInfo.bmiHeader.biWidth *3)-*(pBuffer2+i*3+0+j*BitmapInfo.bmiHeader.biWidth *3));
     *(pBuffer1+i*3+1+j*BitmapInfo.bmiHeader.biWidth *3)=abs((unsigned char)*(pBuffer1+i*3+1+j*BitmapInfo.bmiHeader.biWidth *3)-*(pBuffer2+i*3+1+j*BitmapInfo.bmiHeader.biWidth *3));
      *(pBuffer1+i*3+2+j*BitmapInfo.bmiHeader.biWidth *3)=abs((unsigned char)*(pBuffer1+i*3+2+j*BitmapInfo.bmiHeader.biWidth *3)-*(pBuffer2+i*3+2+j*BitmapInfo.bmiHeader.biWidth *3));
}
return pBuffer1;
}



STDMETHODIMP CSampleGrabberCB::FrameDifferencing(double SampleTime, BYTE *pBuffer, long BufferSize)
{
int pattern[350][250];
HDRAWDIB hd;
HDC  hdc;
BYTE mau;
BYTE mau1r,mau2r,mau3r,mau1g,mau2g,mau3g,mau1b,mau2b,mau3b;
int x,y,i,j,nx,ny;
int aa=20;
	if(temp1==NULL)
	{
		temp1=new BYTE[BufferSize];
		memcpy(temp1,pBuffer,BufferSize);
		x0=5;
		y0=10;
		return 0;
	}
	if(temp2==NULL)
	{
		temp2=new BYTE[BufferSize];
		memcpy(temp2,pBuffer,BufferSize);
		return 0;
	}
	
	if(temp3==NULL)
	{
		temp3=new BYTE[BufferSize];
		memcpy(temp3,pBuffer,BufferSize);
	}



	//Dung thuat toan three difference-frames tach loc doi tuong chuyen dong
	FillMemory(pattern,sizeof(pattern),0);
	for(x=x0;x<width-x0;x++)
		for(y=y0;y<height/2-10;y++)
		{
            get_color(pBuffer,x,y,&mau1r,&mau1g,&mau1b);
            get_color(temp1,x,y,&mau2r,&mau2g,&mau2b);
            get_color(temp2,x,y,&mau3r,&mau3g,&mau3b);
            i=distance(mau1r,mau1g,mau1b,mau2r,mau2g,mau2b);
            j=distance(mau2r,mau2g,mau2b,mau3r,mau3g,mau3b);
            
            if((i>aa)&&(j>aa))
            {
                mau=255;
    		for(i=(x-25>x0)?x-25:x0;i<x;i++)
	    		for(j=(y-16>y0)?y-16:y0;j<y;j++)
		    		pattern[i][j]++;
//			*(temp3+x*3+0+y*width*3)=*(pBuffer+x*3+0+y*width*3);//(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;
//			*(temp3+x*3+1+y*width*3)=*(pBuffer+x*3+1+y*width*3);//(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;
//			*(temp3+x*3+2+y*width*3)=*(pBuffer+x*3+2+y*width*3);//(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;

            }            
            else 
            {
                mau=0;
            }
			*(temp3+x*3+0+y*width*3)=(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;
			*(temp3+x*3+1+y*width*3)=(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;
			*(temp3+x*3+2+y*width*3)=(unsigned char)mau;//0.299*r + 0.587*g + 0.114* b;

            
	//0.299*R + 0.587*G + 0.114* B

		}

	memcpy(temp2,temp1,BufferSize);
    memcpy(temp1,pBuffer,BufferSize);
//
    BYTE r1,g1,b1,r2,g2,b2,r,g,b;
	for(i=x0;i<width-x0;i++)
		for(j=y0;j<height/2-10;j++)
        {
            get_color(pBuffer,i,j,&r,&g,&b);
            get_color(pBuffer,i+1,j,&r1,&g1,&b1);
            get_color(pBuffer,i,j+1,&r2,&g2,&b2);
     
    if((sqrt((r-r1)*(r-r1)+(g-g1)*(g-g1)+(b-b1)*(b-b1))>=aa)||
       (sqrt((r-r2)*(r-r2)+(g-g2)*(g-g2)+(b-b2)*(b-b2))>=aa))
    {
        set_color(pBuffer,i,j,255,255,255);     
    }
    else    
        set_color(pBuffer,i,j,0,0,0);    
        }


	//for(x=x0;x<310+x0;x++)
	x=x0;
	while(x<width-x0-30)
	{		
		//for(y=y0;y<125+y0;y++)		
		y=y0;
		while(y<125+y0)
		{
			if(pattern[x][y]>100)
			{
//			    fprintf(f," x=%d",x);//; y=%d; value=%d",x,y,pattern[x][y]);
				//draw_rectangle(pBuffer,x,y,25,16,0,0,255);
				
					aa=pattern[x][y];
					nx=x;
					ny=y;
					for(i=x;i<x+25;i++)
						for(j=y;j<y+16;j++)
							if(aa<pattern[i][j])
							{
								aa=pattern[i][j];
								nx=i;
								ny=j;
							}								
							for(i=x;i<(nx+30>width-30?width-30:nx+30);i++)
					for(j=(y-16<0?y-5:y-16);j<y+16;j++)
						pattern[i][j]=0;

				draw_rectangle(pBuffer,nx,ny,30,16,0,0,255);				
				y=ny+20;
			}	
			y++;
		}
	//		fprintf(f,"\n");
			x++;
	}
//		fclose(f);

		//Hien thi len man hinh(587,480)
		hd=DrawDibOpen(); 
		hdc=GetDC(g_hwnd);
		//DrawDibDraw(hd,hdc,80  ,0,640 ,480,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);
		DrawDibDraw(hd,hdc,40,285,320,240,&BitmapInfo.bmiHeader,temp3,0,0,width,height,DDF_NOTKEYFRAME);		
		DrawDibClose(hd);		
	    ReleaseDC(g_hwnd,hdc);
//40,15,320,240)

		 //Hien thi ket qua
//		RECT rec;
//		GetClientRect(g_hwnd,&rec);	
//		char d[30];			
//		sprintf(d,"Sè l­îng xe m¸y :    ");
//		TextOut(hdc,rec.left+150 ,rec.bottom-70,d,strlen(d) );		
//		sprintf(d,"Sè l­îng « t«        :     ");
//		TextOut(hdc,rec.left+150 ,rec.bottom-50,d,strlen(d) );
//		ReleaseDC(g_hwnd,hdc);
	return 0;
}

void CSampleGrabberCB::Show_Result(DWORD time_total, long car_number, long motobike_number, double car_speed, double motobike_speed)
{
 HDC hdc=GetDC(g_hwnd); 
 RECT rc;
 GetClientRect(g_hwnd, &rc);
SetBkColor(hdc,RGB(225,225, 155));
HFONT font=CreateFont(
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
    char d[50];
    sprintf(d,"Tæng thêi gian: %d phót, %d gi©y    ",time_total/60000,(time_total % 60000)/1000);
 TextOut(hdc, rc.right/2,67,d,strlen(d));
 sprintf(d,"Sè l­îng xe « t«: %d",car_number);
 TextOut(hdc, rc.right/2,107,d,strlen(d)); 
 sprintf(d,"Sè l­îng xe m¸y: %d",motobike_number);
 TextOut(hdc, rc.right/2,147,d,strlen(d));
 sprintf(d,"VËn tèc trung b×nh « t«: %4.2f km/h",car_speed);
 TextOut(hdc, rc.right/2,187,d,strlen(d));
 sprintf(d,"VËn tèc trung b×nh xe m¸y: %4.2f km/h",motobike_speed);
 TextOut(hdc, rc.right/2,227,d,strlen(d));

DeleteObject(font);
 ReleaseDC(g_hwnd,hdc);

}

void CSampleGrabberCB::set_color(BYTE *pBuffer,long x, long y, BYTE r, BYTE g, BYTE b)
{
			*(pBuffer+x*3+0+y*width*3)=r;
			*(pBuffer+x*3+1+y*width*3)=g;
			*(pBuffer+x*3+2+y*width*3)=b;
}

void CSampleGrabberCB::get_color(BYTE *pBuffer, long x, long y, BYTE *r, BYTE *g, BYTE *b)
{
			*r=*(pBuffer+x*3+0+y*width*3);
			*g=*(pBuffer+x*3+1+y*width*3);
			*b=*(pBuffer+x*3+2+y*width*3);
}

BYTE CSampleGrabberCB::distance(BYTE x, BYTE y, BYTE z, BYTE a, BYTE b, BYTE c)
{
return (BYTE)sqrt((x-a)*(x-a)+(y-b)*(y-b)+(z-c)*(z-c));
}


