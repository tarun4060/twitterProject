#!/bin/bash
#
#
export PATH="/opt/anaconda3/bin:/opt/anaconda3/condabin:/Library/Frameworks/Python.framework/Versions/3.10/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/tarunkumar/sbt/bin:/Users/tarunkumar/kafka_2.13-3.0.0/bin"
#
cd /Users/tarunkumar/SparkScalaCourse/twitterProject
sbt "runMain kafkaProducer"
#
export AWS_ACCESS_KEY_ID=AKIA2Y27EQSBJES7FI4Y
export AWS_SECRET_ACCESS_KEY=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
#
/Users/tarunkumar/kafka_2.13-3.0.0/bin/connect-standalone.sh /Users/tarunkumar/kafka_2.13-3.0.0/config/connect-standalone.properties /Users/tarunkumar/kafka_2.13-3.0.0/connector/confluentinc-kafka-connect-s3-10.0.4/etc/quickstart-s3.properties
