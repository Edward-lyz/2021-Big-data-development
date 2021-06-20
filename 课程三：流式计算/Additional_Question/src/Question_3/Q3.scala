package Question_3

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 *通过Maxwell来将SQL的每一次更新写入Kafka队列
 * author 李琰朕
 * date 2021/06/18
 */

object Q3 extends App {
    val p=Runtime.getRuntime.exec("/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/Additional_Question/src/Question_3/run.sh")
    val in = new BufferedInputStream(p.getInputStream())
    val br = new BufferedReader(new InputStreamReader(in))
    var lineStr = ""
    var result=""
    while ( {
        lineStr = br.readLine; lineStr != null
    }) {
        result = lineStr
        println(result)
    }
    br.close
    in.close()
    p.destroy()

}
