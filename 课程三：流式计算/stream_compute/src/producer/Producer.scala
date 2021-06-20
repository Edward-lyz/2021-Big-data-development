package producer

import java.util.Properties
import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.nlpcn.commons.lang.util.IOUtil

object Producer {
  //s3参数
  val accessKey = "88939849D3327875CFFC"
  val secretKey = "W0U1RkE4NzdFQkMzNDE3NjFBMDU1RjY3QzZEODM0"
  val endpoint = "scuts3.depts.bingosoft.net:29997"
  val bucket = "edwards"
  //要读取的文件
  val key = "hotel_stay.csv"

  //kafka参数
  val topic = "edward_hotel_topic"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  /**
   * 从s3中读取文件内容
   *
   * @return s3的文件内容
   */
  def readFile(): String = {
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val clientConfig = new ClientConfiguration()
    clientConfig.setProtocol(Protocol.HTTP)
    val amazonS3 = new AmazonS3Client(credentials, clientConfig)
    amazonS3.setEndpoint(endpoint)
    val s3Object = amazonS3.getObject(bucket, key)
    IOUtil.getContent(s3Object.getObjectContent, "UTF-8")
  }

  /**
   * 把数据写入到kafka中
   *
   * @param s3Content 要写入的内容
   */
  def produceToKafka(s3Content: String): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val dataArr = s3Content.split("\n")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }

  def main(args: Array[String]): Unit = {
    val s3Content = readFile()
    produceToKafka(s3Content)
  }
}
