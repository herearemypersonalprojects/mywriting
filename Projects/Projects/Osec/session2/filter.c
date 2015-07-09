#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define TAG_NAV "<Navigators>"
#define TAG_KEY "<Keywords>"
#define TAG_DOM "<Domains>"

int get_word(char* line, char* word, int pos, char* tag)
{
  int i=pos;
  int j =0;
  int len = strlen(line) - strlen(tag);

  while(line[i] == ' ' && i < len) i++;

  while(line[i] != ' ' && i < len) word[j++]=line[i++];
  
  word[j]= 0;

  if(i < len)
    return i;

  return 0;
}


int check_filter(char* userAgent, char* command, char *hostname)
{
  char navigator[255];
  char keyword[255];
  char domain[255];
  int n;

  /* read configuration file */
  FILE *f;
  char* line = NULL;
  ssize_t read;
  size_t len = 0;
  

  f = fopen("filter.txt","r"); 
  if(f == NULL)
    {
      perror("fopen() failed");
      return 0;
    }

  while((read = getline(&line, &len, f)) != -1)
    {
      printf("Retrived line of length %zu: \n",read);
      printf("%s",line);

      if(strstr((char*)line,TAG_NAV) != NULL)   /* Block IE */
	{
	  n = strlen(TAG_NAV);
	  while((n=get_word(line,navigator,n,TAG_NAV)) > 0)
	    {
	      printf("%s %d\n",navigator,n);
	      if ( strstr( (char*) userAgent,navigator) != NULL)
		return 1;
	    }  
	}
      else if(strstr((char*)line,TAG_KEY) != NULL) /* keyword */
	{
	  n = strlen(TAG_KEY);
	  while((n=get_word(line,keyword,n,TAG_KEY)) > 0)
	    {
	      printf("%s %d\n",keyword,n);
	      if ( strstr( (char*) command,keyword) != NULL)
		return 1;
	    }  
	}
      else if(strstr((char*)line,TAG_DOM) != NULL) /* domain */
	{
	  n = strlen(TAG_DOM);
	  while((n=get_word(line,domain,n,TAG_DOM)) > 0)
	    {
	      printf("%s %d\n",domain,n);
	      if ( strstr( (char*) command,domain) != NULL)
		return 1;
	    }  
	}

    }

  if(line) 
    free(line);
  fclose(f);


 

  return 0;
}
