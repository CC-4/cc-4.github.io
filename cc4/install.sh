#!/bin/sh


USERDIR=$(pwd)

cd /usr/class/cs143/bin

rm -f coolc-rv.jar coolc-rv jupitercl
wget https://github.com/CC-4/PA4/releases/download/v1.0/coolc-rv.jar
wget https://github.com/CC-4/PA4/releases/download/v1.0/coolc-rv
wget https://github.com/CC-4/PA4/releases/download/v1.0/jupitercl

chmod +x coolc-rv
chmod +x jupitercl

cd /usr/class/cs143/lib

rm -f runtime.s
wget https://raw.githubusercontent.com/CC-4/runtime/master/runtime.s

cd "$USERDIR"
