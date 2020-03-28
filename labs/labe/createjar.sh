#!/bin/bash
jarfile=labe
rm $jarfile.jar -f
jar cvf $jarfile.jar $jarfile *.class