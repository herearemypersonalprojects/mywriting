#!/bin/sh
echo "Cela tuera tous les processus contenant le mot java... Etes-vous sur ? (Controle-C pour annuler)"
read pause
for PID in `ps -u sysdis7 | grep 'java' | awk '{print $1}'`; do
echo $PID @killing;
kill -9 $PID;
done
echo "JVM's killed..."
