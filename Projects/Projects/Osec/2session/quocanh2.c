/****************************************
 * Proxy HTTP 1.1
 * Done by Quoc Anh LE 
 * Finished August 18, 2008
 * Reported on Wednesday, August 27, 2008
 *
 * Fonctions:
 * - Proxy Server
 * - Chain of proxy
 * - Filter by dynamic moduls
 * - Proxy-Cache
 *
 * example: man select_tut 2
 *
 *        ls -als core
 * debug: gdb ./proxy ./core
 *        valgrind --tool=memcheck --leak-check=yes -v ./proxy
 *
 ****************************************/
#include <stdio.h>      /* for printf() */
#include <stdlib.h>     /* for atoi() and exit() */
#include <string.h>     /* for memset() */
#include <sys/ioctl.h>
#include <net/if.h>
#include <netdb.h>
#include <arpa/inet.h>   /* for sockaddr_in and inet_ntoa() */
#include <netinet/in.h>
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/socket.h>  /* for socket(), bind(), and connect() */
#include <time.h>

#include <dlfcn.h>

/* According to POSIX.1-2001 */
#include <sys/select.h>

/* According to earlier standards */
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#define _XOPEN_SOURCE 600
#include <sys/select.h>

/* Defines */

#define MAX_CONNECTION 250 /* Maximum of request */
#define BUF_SIZE 102400    /* Maximum of packet */

#undef max
#define max(x,y) ((x) > (y) ? (x) : (y))

/* Global variables */
struct connection
{
  int client;
  int server;

  char cBuf[BUF_SIZE+1];
  char sBuf[BUF_SIZE+1];

  int cBuf_avail, cBuf_written;
  int sBuf_avail, sBuf_written;
};

char *msg_busy= 
  "<html><head><title>Error</title></head><body>Sorry, this server is too busy. Try again later</body></html>";

char *msg_interdict = 
  "<html><head><title>Error</title></head><body>Sorry, this site or this navigator is not allowed.</body></html>";

char *msg_bad_request = "<html><head><title>Error</title></head><body>BAD REQUEST</body></html>";

char *msg_not_found = "<html><head><title>Error</title></head><body>NOT FOUND</body></html>";


int PROXY_PORT = 3005; /* Proxy server port */
int SERVER_PORT= 80;   /* Remote server port */

int PORT2 = 0;
char PROXY2[255];

struct connection lstCon[MAX_CONNECTION];
time_t start_time;  
int ProxySock;      
fd_set rd, wr;
int running;

/* Protypes */

int create_socket_proxy();
int create_socket_http(char *hostname, int port);
int check_data();
int filter_and_check(char *buf);

void initialisation(int argc, char *argv[]);
void loop();
void accept_new_connection();
void close_all();
void close_connection(int i);
void shut_sock(int sock);
void process_data();
void connection_remote_server(int index);
void get_host_name(char* host_name, char* buffer);
void set_non_blocking(int sock);

int (*function_filter)(char*, char*, char*);
void *module_filter;

/* Start here */

int main(int argc, char *argv[])
{
  initialisation(argc,argv);
  ProxySock=create_socket_proxy();
  loop();
  close_all();
  return 0;
}

/*****************************************
***            INITIALISATION
*
* Description: Initialise variables
* Arguments  : Arguments from command line
* Returns    : None.
* Note       : cmd: :/> make run 3005 80 
***
******************************************/

void initialisation(int argc, char *argv[])
{
  int k;

  time(&start_time);

  running = 1;
  
  if(argv[1] != NULL)
    PROXY_PORT = atoi(argv[1]); /* First arg: local port */

  printf("PROXY PORT = %d, SERVER PORT = %d\n",PROXY_PORT, SERVER_PORT);

  if(argc > 2)
    {
      PORT2 = atoi(argv[2]); /* Second arg: remote proxy port */
      strcpy(PROXY2,argv[3]); /* Third arg: remote proxy address */

      printf("Remote proxy port = %d, IP = %s\n",PORT2,PROXY2);
    }




  for(k=0;k<MAX_CONNECTION;k++)
    {
      lstCon[k].client = 0;
      lstCon[k].server = 0;
      lstCon[k].cBuf_avail = 0;
      lstCon[k].cBuf_written = 0;
      lstCon[k].sBuf_avail = 0;
      lstCon[k].sBuf_written = 0;
    }
}

/*****************************************
***            CREATE A PROXY SOCKET
*
* Description: Create a socket non blocking for proxy server
* Arguments  : Port
* Returns    : Descriptor if succes or exit programme if failed
* Note       :  
***
******************************************/

int create_socket_proxy()
{
  int sock;
  int    rc, on=1;
  struct sockaddr_in serveraddr;
  
  /*
   * AF_INET address family sockets can be either 
   * connection-oriented (type SOCK_STREAM) or they 
   * can be connectionless (type SOCK_DGRAM). 
   * Connection-oriented AF_INET sockets use TCP as
   * the transport protocol. Connectionless AF_INET 
   * sockets use UDP as the transport protocol. When 
   * you create an AF_INET domain socket, you specify 
   * AF_INET for the address family in the socket program.
   * AF_INET sockets can also use a type of SOCK_RAW. 
   * If this type is set, the application connects 
   * directly to the IP layer and does not use either 
   * the TCP or UDP transports.
  */


  /*
   * The socket() function returns a socket descriptor 
   * representing an endpoint.  The statement also
   * identifies that the INET (Internet Protocol) address
   * family with the TCP transport (SOCK_STREAM) will be
   * used for this socket.
   */      
  sock = socket(AF_INET, SOCK_STREAM,0);
  if(sock < 0)
    {
      perror("socket() failed");
      shut_sock(sock);
      exit(EXIT_FAILURE);

    }
  else
    printf("Proxy is created!\n");

  /*
   * Set socket non block
   */
  set_non_blocking(sock);


  /*
   * The setsockopt() fonction is used to allow the local
   * address to be reused when the server is restarted
   * before the required wait time expires.
   */
  rc = setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, 
		  (char *)&on, sizeof(on));
  if(rc < 0)
    {
      perror("setsockopt(SO_REUSEADDR) failed");
      shut_sock(sock);
      exit(EXIT_FAILURE);

    }

  /*
   * After the socket descriptor is created, a bind()
   * function gets a unique name for the socket. 
   * s_addr is set to zero, which allows connections
   * to be established from any client that specifies 
   * port PROXY_PORT
   */
  memset(&serveraddr, 0, sizeof(struct sockaddr_in));
  /* Internet address family */
  serveraddr.sin_family      = AF_INET;           
  /* Local port */
  serveraddr.sin_port        = htons(PROXY_PORT); 
  /* Any incoming interface */
  serveraddr.sin_addr.s_addr = htonl(INADDR_ANY); 

  /* Bind to the local address */
  rc = bind(sock, (struct sockaddr *)&serveraddr,
	    sizeof(serveraddr));
  if(rc < 0)
    {
      perror("bind() failed");
      shut_sock(sock);
      exit(EXIT_FAILURE);
    }

  /*
   * The listen() function allows the server to accept
   * incoming client connections. 
   * The backlog means that system will queue
   * CONNECTION_QUEUE_SIZE incoming connection requests
   * before the system starts rejecting the incoming 
   * requests.
   */
  rc = listen(sock, MAX_CONNECTION);
  if(rc < 0)
    {
      perror("listen() failed");
      shut_sock(sock);
      exit(EXIT_FAILURE);
    }
  else
    printf("Ready for connection from client\n");


  return sock;
}


/*****************************************
***            LOOP
*
* Description: Repeat checking and processing the data until keypressed (Enter)
* Arguments  : None
* Returns    : None
* Note       :  
***
******************************************/


void loop()
{

  while(running) 
    {
      if(check_data() > 0)
	process_data();      

    }

} 


/*****************************************
***            CHECK DATA
*
* Description: Using one only command Select() to check available data 
*              for reading and writting
* Arguments  : None
* Returns    : Positive if data is ready for processing
* Note       : Keypress is also checked 
***
******************************************/


int check_data()
{
  struct timeval tv;
  int retval;
  int maxDes;
  time_t ctime;
  int t,k;
   
  /* First put together fd_set for select(), which will
   * consist of the sock veriable in case a new connection
   * is coming in, plus all the sockets we have already
   * accepted
   */

  /* FD_ZERO() clears out the fd_set called socks, so that
   * it doesn't contain any file descriptor
   */

  FD_ZERO(&rd);
  FD_ZERO(&wr);

  /* FD_SET adds the file descriptor "sock" to the fd_set,
   * so that select() will return if a connection comes in
   * on that socket (which means you have to do accept(),
   * etc.
   */

  FD_SET(STDIN_FILENO, &rd); /* check keyboard */

  FD_SET(ProxySock, &rd);    /* check new connection */

  /* wait 0.01s */
  tv.tv_sec  = 0;
  tv.tv_usec = 10000;

  maxDes = ProxySock;

  /* Loops through all the possible connections and adds
   * those sockets to the fd_set
   */
  for(k=0;k<MAX_CONNECTION;k++)    
    {      
      if(lstCon[k].client > 0 && 
	 lstCon[k].cBuf_avail < BUF_SIZE)
	{
	  FD_SET(lstCon[k].client, &rd);	
	  maxDes = max(maxDes,lstCon[k].client);
	}	
      
      if(lstCon[k].server > 0 &&
	 lstCon[k].sBuf_avail < BUF_SIZE)
	  {
	    FD_SET(lstCon[k].server,&rd);	    
	    maxDes = max(maxDes,lstCon[k].server);
	  }

      if(lstCon[k].client > 0 &&
	 lstCon[k].sBuf_avail - lstCon[k].sBuf_written > 0)
	{
	  FD_SET(lstCon[k].client, &wr);
	  maxDes = max(maxDes, lstCon[k].client);
	}

      if(lstCon[k].server > 0 &&
	 lstCon[k].cBuf_avail - lstCon[k].cBuf_written > 0)
	{
	  FD_SET(lstCon[k].server, &wr);
	  maxDes = max(maxDes, lstCon[k].server);
	}

      }
  
  retval = select(maxDes+1,&rd,&wr,NULL, &tv);

  if((retval == -1) && (errno != EINTR))
      return -1;
  else if(retval < 0)
    {
      perror("select() failed");
      close_all();
      exit(EXIT_FAILURE);
    }
  else if(retval == 0)
    {
      time(&ctime);
      t = ctime-start_time;     
      if(t%5 == 0)
	{
	  printf(".");
	  fflush(stdout);
	  start_time = ctime+1;
	}
      return -1;
    }
  
  return 1;
}


/*****************************************
***            PROCESS DATA
*
* Description: if keypress then exit
*              if new connection then create new connection
               if data is ready for reading then read data
               if data is ready for writting then write data
* Arguments  : 
* Returns    : None
* Note       :  
***
******************************************/

void process_data()
{
  int r,k;
  

  if(FD_ISSET(0,&rd)) /* Enter keypress */
    {      
      running = 0;
      getchar();
      return;
    }

  if(FD_ISSET(ProxySock,&rd)) /* New connection */
    {
      accept_new_connection();
      return;
    }
 
    for(k = 0;k<MAX_CONNECTION;k++)
      {
	if(lstCon[k].client > 0)
	  if(FD_ISSET(lstCon[k].client, &rd))
	    {
	      r = read(lstCon[k].client, 
		       lstCon[k].cBuf + lstCon[k].cBuf_avail,
		       BUF_SIZE - lstCon[k].cBuf_avail);
	      if(r < 1)
		{
		  printf("read(client)\n");
		  close_connection(k);
		}
	      else
		{
		  lstCon[k].cBuf_avail = lstCon[k].cBuf_avail + r;
	
		  if(lstCon[k].server == 0 || 
		     (lstCon[k].cBuf[0] == 'G'))
		    connection_remote_server(k);
		}
	    }

	if(lstCon[k].server > 0)
	  if(FD_ISSET(lstCon[k].server, &rd))
	    {
	      r = read(lstCon[k].server, 
		       lstCon[k].sBuf + lstCon[k].sBuf_avail,
		       BUF_SIZE - lstCon[k].sBuf_avail);
	      if(r < 1)
		{
		  printf("read(server)\n");
		  close_connection(k);
		}
	      else
		lstCon[k].sBuf_avail = lstCon[k].sBuf_avail + r;
	    }

	if(lstCon[k].client > 0)
	  if(FD_ISSET(lstCon[k].client, &wr))
	    {
	      r = write(lstCon[k].client,
		lstCon[k].sBuf + lstCon[k].sBuf_written,
		lstCon[k].sBuf_avail - lstCon[k].sBuf_written);

	      if(r < 1)
		{
		  printf("write(client)\n");
		  close_connection(k);
		}
	      else
		lstCon[k].sBuf_written=lstCon[k].sBuf_written+r;
	    }

	if(lstCon[k].server > 0)
	  if(FD_ISSET(lstCon[k].server, &wr))
	    {
	      r = write(lstCon[k].server,
		lstCon[k].cBuf + lstCon[k].cBuf_written,
		lstCon[k].cBuf_avail - lstCon[k].cBuf_written);

	      if(r < 1)
		{
		  printf("write(server)\n");
		  close_connection(k);
		}
	      else
		{
		printf("Send request to server:%d [%s]\n",
		       lstCon[k].cBuf_avail-lstCon[k].cBuf_written,
		       lstCon[k].cBuf+lstCon[k].cBuf_written);

		lstCon[k].cBuf_written=lstCon[k].cBuf_written+r;
		}
	    }

	/* check if write data has caught read data */
	if(lstCon[k].cBuf_written >= lstCon[k].cBuf_avail)
	  lstCon[k].cBuf_written = lstCon[k].cBuf_avail = 0;

	if(lstCon[k].sBuf_written >= lstCon[k].sBuf_avail)
	  lstCon[k].sBuf_written = lstCon[k].sBuf_avail = 0;

	/* if client has closed the connection,
	 * the server must be closed
	 */
	if(lstCon[k].client <= 0)
	  close_connection(k);
       }   

}


/*****************************************
***            ACCEPT A NEW CONNECTION
*
* Description: Create new connection for new request
* Arguments  : None
* Returns    : None
* Note       :  
***
******************************************/

void accept_new_connection()
{
  int cSock;                /* Socket descriptor for client */
  struct sockaddr_in cAddr; /* Client address */
  unsigned int cLen;        /* Length of address structure */
  int k;

  cLen = sizeof(cAddr);
    
  if ((cSock = accept(ProxySock,(struct sockaddr *) &cAddr, 
           &cLen)) < 0)
    {
      perror("accept() failed");      
      close_all();
      exit(EXIT_FAILURE);
    }

  printf("Handling client %s\n",inet_ntoa(cAddr.sin_addr));

  /*
   * Set socket non block
   */
  set_non_blocking(cSock);

  for(k=0;(k<MAX_CONNECTION)&&(cSock != -1);k++)
    if(lstCon[k].client == 0)
    {
      lstCon[k].client = cSock;
      printf("\nConnection accepted: FD=%d;Slot=%d \n",
	     cSock,k);
      
      cSock = -1;
    }

  
  if(cSock != -1)
    {
      
      printf("No room left for new client.\n");
      send(cSock,msg_busy,strlen(msg_busy),0);
	      
      printf("send back:%d = %s\n",strlen(msg_busy),msg_busy);
      shut_sock(cSock);
    }
  

}


/*****************************************
***            FILTER
*
* Description: Load dynamique module and
               check acceptation conditions for a request
* Arguments  : A request
* Returns    : 0 if request is ok and otherwise
* Note       :  
***
******************************************/


int filter(char *userAgent, char *command, char *hostname)
{
  int ret = 0;

  /* Load modul here */
  module_filter = dlopen("filter.so", RTLD_LAZY);

  if(!module_filter)
    {
      fprintf(stderr,"couldn't open filter.so: %s\n",dlerror());
      return 0;
    }

  /* Find the address of function */
  *(void **)(&function_filter)=dlsym(module_filter,"check_filter");

  if(dlerror() != NULL)
    {
      perror("Couldn't find function check_filter");
      return 0;
    }

  ret = (*function_filter)(userAgent,command,hostname);

  printf("Bo loc tra ve gia tri = %d\n",ret);

  if(dlclose(module_filter) != 0) 
    perror("dlclose() failed");

  return ret;
}


/*****************************************
***            GET INFORMATIONS ABOUT REQUEST
*
* Description: Get hostname, agent, ...
* Arguments  : Port
* Returns    : Descriptor if succes or exit programme if failed
* Note       :  
***
******************************************/


void get_host_name(char* host_name, char* buffer)
{
    char *line;
    int i;
    i = 0;
    line = strstr(buffer, "Host: ");
    line = line+6;
    while(*line && *line != '\r'){
	host_name[i++] = *line++;
    }
    host_name[i] = 0;    
}

void get_user_agent(char* userAgent, char* buffer)
{
    char *line;
    int i;
    i = 0;
    line = strstr(buffer, "User-Agent: ");
    line = line+strlen("User-Agent: ");
    while(*line && *line != '\r'){
	userAgent[i++] = *line++;
    }
    userAgent[i] = 0;      
}

/*****************************************
***            CREAT HTTP SOCKET
*
* Description: 
* Arguments  : hostname, (Port is a global var)
* Returns    : 
* Note       :  
***
******************************************/


int create_socket_http(char *hostname, int port)
{
  int sock;
  struct sockaddr_in address;
  struct hostent *hp;


  if(hostname == NULL) return -1;
  
  hp = gethostbyname(hostname);

  if(hp == NULL) return -1;

  sock = socket(AF_INET, SOCK_STREAM,0);

  if(sock <= 0)
    {
      perror("create_socket_http() failed");     
      shut_sock(sock);
      return -1;
    }

  address.sin_family = AF_INET;
  address.sin_port   = htons(port);
  memcpy(&address.sin_addr.s_addr,hp->h_addr,hp->h_length); 

  printf("creat http socket: %s (%s) on %d",
	 hostname,inet_ntoa(address.sin_addr),port);

  if(connect(sock,(struct sockaddr*)&address,
	     sizeof(struct sockaddr_in)) < 0)
    {
      perror("Connect to http server failed");
      shut_sock(sock);
      return -1;
    }
  else
      printf(" connected\n");

  set_non_blocking(sock);


  return sock;
}

/*****************************************
***            CHECK BAD REQUEST
*
* Description: 
* Arguments  : request
* Returns    : 
* Note       :  
***
******************************************/
 
int check_bad_request(char *httpRequest,
		      char *command,
		      char *userAgent,
		      char *hostname)
{ 
  int i = 0;
  size_t size = strlen(httpRequest);
 
  if(size <= 0)
    return 1;

  if ((httpRequest[0] != 'G') || 
      (httpRequest[1] != 'E') || 
      (httpRequest[2] != 'T'))
    return 1;


  /* commmand */
  while(httpRequest[i] != '\r' && httpRequest[i] != '\n')
    {
      command[i] = httpRequest[i];
      i++;
      if(i >= size)
	return 1;
    }

  command[i] = '0';

  get_host_name(hostname,httpRequest);

  get_user_agent(userAgent,httpRequest);

  if((hostname == NULL)||(userAgent == NULL))
    return 1;
  
  return 0;
}


/***************************************/
void connection_remote_server(int index)
{
  int httpSock=0;

  char command[2048];
  char userAgent[1024];
  char hostname[1024];

  if(check_bad_request(lstCon[index].cBuf,
		       command,userAgent,hostname)==1)
    {
      send(lstCon[index].client,msg_bad_request,
	   strlen(msg_bad_request),0);
      close_connection(index);
      return;	
    }

  printf("Hostname: %s\n",hostname);
  printf("URL: %s\n",command);
  printf("User agent: %s\n",userAgent);

  if(filter(userAgent,command,hostname)==1)
    {
      send(lstCon[index].client,msg_interdict,
	   strlen(msg_interdict),0);
      close_connection(index);
      return;
    }

  if(PORT2 == 0) /* No remote proxy */
    httpSock = create_socket_http(hostname,SERVER_PORT);
  else
    httpSock = create_socket_http(PROXY2,PORT2);

  if(httpSock > 0)         
      lstCon[index].server = httpSock;
  else
      lstCon[index].server = 0; 
}

/****************************************/
void set_non_blocking(int sock)
{
  /*
   * Under Linux, select() may report a socket file descriptor
   * as "ready for reading",  while nevertheless a subsequent
   * read blocks.  This could for example happen when data has
   * arrived but  upon  examination  has  wrong checksum and is
   * discarded.  There may be other circumstances in which a
   * file descriptor is spuriously reported as ready.  
   * Thus it may be  safer to use O_NONBLOCK on sockets that 
   * should not block.
   */
	int opts;

	opts = fcntl(sock,F_GETFL);
	if (opts < 0) {
		perror("fcntl(F_GETFL)");
		exit(EXIT_FAILURE);
	}
	opts = (opts | O_NONBLOCK);
	if (fcntl(sock,F_SETFL,opts) < 0) {
		perror("fcntl(F_SETFL)");
		exit(EXIT_FAILURE);
	}
	return;
}

/*****************************************/
void shut_sock(int sock)
{
  if(sock >=0)
    {
      shutdown(sock,SHUT_RDWR);
      close(sock);      
    }
}

/*****************************************/
void close_all()
{
  int k;

  shut_sock(ProxySock);
  
  for(k=0;k<MAX_CONNECTION;k++)
    close_connection(k);
}

/*****************************************/
void close_connection(int i)
{
  if(lstCon[i].client != 0)
    {
      /* Disables further send and receive operations */
      shut_sock(lstCon[i].client);
      lstCon[i].client = 0;

      printf("\nClose connection %d\n",i);
    }
  if(lstCon[i].server != 0)
    {
      shut_sock(lstCon[i].server);
      lstCon[i].server = 0;
    } 

  lstCon[i].cBuf_avail = lstCon[i].cBuf_written = 0;
  lstCon[i].sBuf_avail = lstCon[i].sBuf_written = 0;
  
}

