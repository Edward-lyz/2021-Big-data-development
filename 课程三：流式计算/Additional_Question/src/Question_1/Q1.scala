package Question_1


import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**统计一分钟内有多少含b词组出现
 * author 李琰朕
 * Date 2021/06/17
 */

object Q1 {
  val target="b"
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    val text = env.socketTextStream("localhost", 9999)
    val counts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }.map {
      ("带有b的单词60秒内出现次数：",_,1)
    } .keyBy(0)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(60)))
      .sum(2)

    counts.print();

    env.execute("Window Stream WordCount")
  }
}

