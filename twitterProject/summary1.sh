#!/bin/bash
#
#
export PATH="/opt/anaconda3/bin:/opt/anaconda3/condabin:/Library/Frameworks/Python.framework/Versions/3.10/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/tarunkumar/sbt/bin:/Users/tarunkumar/kafka_2.13-3.0.0/bin"
#
cd /Users/tarunkumar/SparkScalaCourse/twitterProject
sbt "runMain summary"
#
