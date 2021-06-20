package consumer

import java.util.{Properties, UUID}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010

import java.io.{File, FileOutputStream, PrintStream}

object Consumer {
  /**
   * 输入的主题名称
   */
  val inputTopic = "edward_hotel_topic"
  /**
   * kafka地址
   */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
    inputKafkaStream.map(x => println(x))
    env.execute()
    val f=new File("/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/stream_compute/result.csv")
    //f.createNewFile()
    val fileOutputStream = new FileOutputStream(f)
    val printStream = new PrintStream(fileOutputStream)
    System.setOut(printStream)
    println("全部数据已经写入本地csv中！")
  }
}
