import os
os.environ['PYSPARK_SUBMIT_ARGS'] = '--jars ~/spark-streaming-kafka-assembly_2.10-1.6.1.jar pyspark-shell'
import pyspark
from pyspark.streaming.kafka import KafkaUtils
from pyspark.streaming import StreamingContext
sc.stop()
sc = pyspark.SparkContext()
ssc = StreamingContext(sc,1)

#Broker is left blank to protect my Google Cloud Instance IP Address
broker = ""
directKafkaStream = KafkaUtils.createDirectStream(ssc, ["bitcoin"],
                        {"metadata.broker.list": broker})
directKafkaStream.pprint()
sc.start()
