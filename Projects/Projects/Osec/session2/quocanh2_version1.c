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
 *
 *        ls -als core
 * debug: gdb ./proxy ./core
 *        valgrind --tool=memcheck --leak-check=yes -v ./proxy
 *
 ****************************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <time.h>

/* According to POSIX.1-2001 */
#include <sys/select.h>

/* According to earlier standards */
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#define _XOPEN_SOURCE 600
#include <sys/select.h>

/* Defines */
#define PROXY_PORT 3005
#define SERVER_PORT 80
#define MAX_CONNECTION 50
#define REQUEST_SIZE 1024


/* Global variables */
struct connection
{
  int client;
  int server;
  time_t start_time;
  int status; /* 0: new client; 
		 1: data available at client;
		 2: data available at server;
	      */
};

char *msg_busy= 
  "<html><head><title>Error</title></head><body>Sorry, this server is too busy. Try again later</body></html>";

char *msg_interdict = 
  "<html><head><title>Error</title></head><body>Sorry, this site or this navigator is not allowed.</body></html>";



time_t start_time;
struct connection list_connection[MAX_CONNECTION];
int ProxySock;

/* Protypes */

int create_socket_proxy();
int create_socket_http(char *hostname);
int check_data();
int filter_and_check(char *buf);


void debug(char *str);
void initialisation();
void loop();
void accept_new_connection();
void close_all();
void close_connection(int i);
void process_data(int index);
void connection_remote_server(char *buf, int res, int index);
void get_host_name(char* host_name, char* buffer);
void set_non_blocking(int sock);

/* Start here */

int main()
{
  initialisation();
  ProxySock=create_socket_proxy();
  loop();
  close_all();
  return 0;
}

/*****************************************/
void close_all()
{
  int k;

  close(ProxySock);
  
  for(k=0;k<MAX_CONNECTION;k++)
    close_connection(k);
}

/*****************************************/
void close_connection(int i)
{
  if(list_connection[i].client != 0)
    {
      /* Disables further send and receive operations */
      shutdown(list_connection[i].client,SHUT_RDWR);
      close(list_connection[i].client);
      list_connection[i].client = 0;
      list_connection[i].status = 0;

      printf("\nClose connection %d\n",i);
    }
  if(list_connection[i].server != 0)
    {
      shutdown(list_connection[i].server,SHUT_RDWR);
      close(list_connection[i].server);
      list_connection[i].server = 0;
    } 

  
}
/*****************************************/
void initialisation()
{
  int k;

  time(&start_time);

  for(k=0;k<MAX_CONNECTION;k++)
    {
      list_connection[k].client = 0;
      list_connection[k].server = 0;
      list_connection[k].status = 0;
    }
}

/*****************************************/
void loop()
{
  int running = 1;
  int c;

  while(running) 
    {
      c = check_data();
      
      switch(c)
	{	
	case 100:  /* Keypress */
	  running = 0;
	  break;

	case MAX_CONNECTION:	 /* new request */
	  accept_new_connection();
	  break;

	case -1: /* nothing */
	  break;

        default:
	  /* Du lieu den hay du lieu di */
          process_data(c);
	  break;

	}

    }

} 

/*****************************************/
void process_data(int k)
{
  int res;
  char buf[REQUEST_SIZE];
  
  if(list_connection[k].status == 1) /* request from client */
    {
      res = recv(list_connection[k].client,buf, 
	     REQUEST_SIZE,0);
  
      if(res <= 0)
	{
	  close_connection(k);
	}
      else
	{
	  printf("%d bytes read from client:[%.*s]\n",res,res,buf);

	  /* connect to remote server if not yet connected */
	  /* and then send the request to this server */
	  if(list_connection[k].server == 0)
	    connection_remote_server(buf,res,k);
	  else
	    /* send remain of request to remote server */
	    send(list_connection[k].server,buf,res,0);
	}
    }
  else  /* data returned from remote server */
    if(list_connection[k].status == 2)
    {
      res = recv(list_connection[k].server,buf, 
	     REQUEST_SIZE,0);
  
      if(res <= 0)
	{
	  close_connection(k);
	}
      else
	{
	  printf("%d bytes read from server:[%.*s]\n",res,res,buf);

	  /* send data back to client */
	  send(list_connection[k].client,buf,res,0);
	}
      
    }
}

/*************dynamic modul*************/
int filter(char *buf)
{
  /* Load modul here */
  if ( strstr( (char*) buf, "MSIE") != NULL)
    return 0;

  return 0;
}


/****************************************/

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

/***************************************/
int create_socket_http(char *hostname)
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
      return -1;
    }

  address.sin_family = AF_INET;
  address.sin_port   = htons(SERVER_PORT);
  memcpy(&address.sin_addr.s_addr,hp->h_addr,hp->h_length); 

  printf("creat http socket: %s on %d",
	inet_ntoa(address.sin_addr),SERVER_PORT);

  if(connect(sock,(struct sockaddr*)&address,
	     sizeof(struct sockaddr_in)) < 0)
    {
      perror("Connect to http server failed");
      return -1;
    }
  else
    {
      debug(" connected");
    }

  set_non_blocking(sock);


  return sock;
}

/***************************************/
void connection_remote_server(char *buf, int res, int index)
{
  char host_name[REQUEST_SIZE];
  int httpSock;
  
  if(filter(buf)==1)
    {
      send(list_connection[index].client,msg_interdict,
	   strlen(msg_interdict),0);
      close_connection(index);
      return;
    }

  /* get host name */
  get_host_name(host_name,buf);
  
  httpSock = create_socket_http(host_name);

  if(httpSock > 0)
    {
      send(httpSock,buf,res,0);
      list_connection[index].server = httpSock;
      printf("Request sent \n");
    }
  else
    {      
      list_connection[index].server = 0; 
    }
}

/****************************************/
void set_non_blocking(int sock)
{
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
void accept_new_connection()
{
  int cSock;  
  struct sockaddr_in cAddr;
  unsigned int cLen; 
  int k;
  int res;
  char buf[REQUEST_SIZE];

  cLen = sizeof(cAddr);
    
  if ((cSock = accept(ProxySock,(struct sockaddr *) &cAddr, 
           &cLen)) < 0)
    {
      perror("accept() failed");      
      close_all();
      exit(EXIT_FAILURE);
    }

  /*
   * Set socket non block
   */
  set_non_blocking(cSock);

  for(k=0;(k<MAX_CONNECTION)&&(cSock != -1);k++)
    if(list_connection[k].client == 0)
    {
      list_connection[k].client = cSock;
      printf("\nConnection accepted: FD=%d;Slot=%d \n",
	     cSock,k);
      
      cSock = -1;
    }

  
  if(cSock != -1)
    {
      res = recv(cSock,buf,REQUEST_SIZE,0);
  
      if(res > 0)
	{
	  printf("\nNew request:%d = %s\n",res,buf);
	}
      
      printf("No room left for new client.\n");
      send(cSock,msg_busy,strlen(msg_busy),0);
	      
      printf("send back:%d = %s\n",strlen(msg_busy),msg_busy);
      close(cSock);
    }
  

}

/*****************************************/
int check_data()
{
  fd_set sockSet;
  struct timeval tv;
  int retval;
  int maxDescriptor;
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

  FD_ZERO(&sockSet);

  /* FD_SET adds the file descriptor "sock" to the fd_set,
   * so that select() will return if a connection comes in
   * on that socket (which means you have to do accept(),
   * etc.
   */

  FD_SET(STDIN_FILENO, &sockSet); /* check keyboard */

  FD_SET(ProxySock, &sockSet);

  /* wait 2 seconds */
  tv.tv_sec  = 1;
  tv.tv_usec = 0;

  maxDescriptor = ProxySock;

  /* Loops through all the possible connections and adds
   * those sockets to the fd_set
   */
  for(k=0;k<MAX_CONNECTION;k++)
    if(list_connection[k].client != 0)
      {
	FD_SET(list_connection[k].client, &sockSet);

	if(maxDescriptor < list_connection[k].client)
	  maxDescriptor = list_connection[k].client;


	if(list_connection[k].server != 0)
	  {
	    FD_SET(list_connection[k].server,&sockSet);
	    if(maxDescriptor<list_connection[k].server)
	      maxDescriptor = list_connection[k].server;
	  }
      }

  retval = select(maxDescriptor+1,&sockSet,NULL,NULL, &tv);

  if(retval == -1)
    {
      perror("select()failed");
      return -1;
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
  else if(retval > 0)
    {
      if (FD_ISSET(0,&sockSet)) /* check keyboard */
	{
	  debug("Proxy server stopped!");
	  getchar();
	  return 100;
	}


      for(k=0;k<MAX_CONNECTION;k++)
	if(list_connection[k].client != 0)
	  {
	    if(FD_ISSET(list_connection[k].client,&sockSet))
	      {
		list_connection[k].status = 1; /* at client */
		return k;
		break;
	      }
	    if(list_connection[k].server != 0) /* connected */
	    if(FD_ISSET(list_connection[k].server,&sockSet))
	      {
		list_connection[k].status = 2; /* at server */
		return k;
		break;
	      }
	  }
      
      if (FD_ISSET(ProxySock, &sockSet)) /*new connection*/
	{
	  printf("\nNew connection arrive ...\n");
	  return MAX_CONNECTION;
	}

    }

  return -1;
}

/*****************************************/
void debug(char *str)
{
  int DEBUG = 1;

  if(DEBUG)
    printf(str);printf("\n");
}

/*****************************************/
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
      return -1;
    }
  else
    debug("Proxy is created!");

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
      return -1;
    }

  /*
   * After the socket descriptor is created, a bind()
   * function gets a unique name for the socket. 
   * s_addr is set to zero, which allows connections
   * to be established from any client that specifies 
   * port PROXY_PORT
   */
  memset(&serveraddr, 0, sizeof(struct sockaddr_in));
  serveraddr.sin_family      = AF_INET; 
  serveraddr.sin_port        = htons(PROXY_PORT);
  serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);

  rc = bind(sock, (struct sockaddr *)&serveraddr,
	    sizeof(serveraddr));
  if(rc < 0)
    {
      perror("bind() failed");
      return -1;
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
      return -1;
    }
  else
    debug("Ready for connection from client");


  return sock;
}
