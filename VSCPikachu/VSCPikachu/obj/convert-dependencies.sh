#!/bin/sh
# AUTO-GENERATED FILE, DO NOT EDIT!
if [ -f $1.org ]; then
  sed -e 's!^C:/c/lib!/usr/lib!ig;s! C:/c/lib! /usr/lib!ig;s!^C:/c/bin!/usr/bin!ig;s! C:/c/bin! /usr/bin!ig;s!^C:/c/!/!ig;s! C:/c/! /!ig;s!^E:!/cygdrive/e!ig;s! E:! /cygdrive/e!ig;s!^D:!/cygdrive/d!ig;s! D:! /cygdrive/d!ig;s!^C:!/cygdrive/c!ig;s! C:! /cygdrive/c!ig;' $1.org > $1 && rm -f $1.org
fi
