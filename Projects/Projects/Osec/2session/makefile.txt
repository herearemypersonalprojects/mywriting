export LD_LIBRARY_PATH=.:/usr/local/lib:/opt/lib
CC = gcc
CFLAGS = -ansi -pedantic -Wall -Werror 
EXEC = proxy
DEBUG = -ggdb

all: 
	$(CC) -c -fPIC filter.c
	$(CC) -shared -fPIC -o filter.so filter.o	
	
	$(CC) $(CFLAGS) -o $(EXEC) quocanh2.c -ldl

	./$(EXEC)
run:
	./$(EXEC)

debug:
	$(CC) $(CFLAGS) $(DEBUG) -o $(EXEC) quocanh2.c


clean:
	if [ -e $(EXEC) ];	then rm $(EXEC); fi
	rm filter.so
	rm filter.o
