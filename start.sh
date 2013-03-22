#!/bin/bash

VCLUSTER_LIB=/home/rsyoung/workspace/vcluster/lib
CLASSPATH=$CLASSPATH:$VCLUSTER_LIB/axis.jar:$VCLUSTER_LIB/jaxb-impl.jar:$VCLUSTER_LIB/resolver.jar:$VCLUSTER_LIB/serializer.jar:$VCLUSTER_LIB/xercesImpl.jar:$VCLUSTER_LIB/xercesSamples.jar:$VCLUSTER_LIB/xml-apis.jar
export CLASSPATH 

java ./bin/vcluster.ui.UIMain
