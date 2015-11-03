#!/bin/bash

# compile or run project, assume gnu.getopt.jar in current directory
case "$1" in
	-c|"-C")
        javac -cp "gnu.getopt.jar:." RSA.java 
        ;;

	-r|"-R")
        java -cp "gnu.getopt.jar:." RSA $2 $3 $4 $5 $6 $7
        ;;

	-cr|"-CR")
        javac -cp "gnu.getopt.jar:." RSA.java &&
		java -cp "gnu.getopt.jar:." RSA $2 $3 $4 $5 $6 $7
        ;;
    *)
        java -cp "gnu.getopt.jar:." RSA $1 $2 $3 $4 $5 $6
esac

