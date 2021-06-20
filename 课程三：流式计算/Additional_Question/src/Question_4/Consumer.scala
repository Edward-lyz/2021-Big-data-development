package Question_4

import java.util.{Properties, UUID}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import Q4.main

import java.io.{File, FileOutputStream, PrintStream}


/**
 运行consumer将实时csv保存在本地
 author 李琰朕
 date 2021/06/18
 */

object Consumer {
  /**
   * 输入的主题名称
   */
  val inputTopic = "edward_ticket_topic"
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
    inputKafkaStream.map(x=> println(x))
    env.execute()
    val f=new File("/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/Additional_Question/result.csv")
    //f.createNewFile()
    val fileOutputStream = new FileOutputStream(f)
    val printStream = new PrintStream(fileOutputStream)
    System.setOut(printStream);
    // 调用写入SQL的方法将获得的实时数据写入SQL
    Q4.main(args: Array[String])

  }

}
