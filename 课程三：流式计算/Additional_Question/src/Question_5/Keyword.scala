package Question_5

import java.io.{BufferedReader, FileReader}
import Q5._
object Keyword extends  App{
  val File: String = "/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/Additional_Question/src/Question_5/key.txt"
  var line: String = ""
  var keyword:String =""
  try{
    val br=new BufferedReader (new FileReader (File) )
    try while ( {
      line = br.readLine;
      line != null
    }){
      keyword=br.readLine()
      Q5.user=keyword
      Q5.main(args: Array[String])
    }
  }
}
