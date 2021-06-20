package Question_5
import java.util
import java.util.{Properties, UUID}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema

object Q5 {
  //需要监控的人名
  var user = "安国栋"
  val inputTopics: util.ArrayList[String] = new util.ArrayList[String]() {
    {
      add("edward_ticket_topic") //车票购买记录主题
      add("edward_hotel_topic") //酒店入住信息主题
      add("edward_monitor_topic") //监控系统数据主题
    }
  }
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[ObjectNode](inputTopics,
      new JSONKeyValueDeserializationSchema(true), kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
    inputKafkaStream.filter(x => x.get("value").get("username").asText("").equals(user)).map(x => {
      (x.get("metadata").get("topic").asText("") match {
        case "edward_monitor_topic"
        => x.get("value").get("found_time")
        case _ => x.get("value").get("buy_time")
      }, x)
    }).print()
    env.execute()
  }
}
