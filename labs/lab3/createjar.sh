#!/bin/bash
jarfile=lab3
rm $jarfile.jar -f
jar cvf $jarfile.jar $jarfile *.class