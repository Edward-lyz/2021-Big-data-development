package Question_2

import java.sql.{Connection, DriverManager, PreparedStatement, SQLXML}
import java.io.{BufferedReader, FileReader, IOException}

/**
 * 将从s3中下载的hotel_stay.csv文件写入本地SQL
 * author 李琰朕
 * Date 2021/06/17
 */

object Q2_2 {
  // 访问本地MySQL服务器，通过3306端口访问mysql数据库
  val url = "jdbc:mysql://localhost:3306/buy_ticket?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&rewriteBatchedStatements=true";
  //驱动名称
  val driver = "com.mysql.cj.jdbc.Driver"

  //用户名
  val username = "root"
  //密码
  val password = "12345678"
  //初始化数据连接
  var connection: Connection = _

  def main(args: Array[String]): Unit= {

    try {
    //注册Driver
    Class.forName (driver)
    //得到连接
    connection = DriverManager.getConnection (url, username, password)
    //删除已有数据
    val delete_sql = "truncate buy_ticket"
    val del_statement = connection.createStatement ()
    val del_res = del_statement.executeUpdate (delete_sql)

    //将从kafka下载到本地的csv数据写入SQL中
    val csvFile: String = "/Volumes/HD/Onedrive/STUDY/大三下/实训/Exercise_3/Additional_Question/buy_ticket.csv"
    var line: String = ""
    val cvsSplitBy: String = ","
    var ID = 1
    try {
    val br = new BufferedReader (new FileReader (csvFile) )
    try while ( {
    line = br.readLine;
    line != null
  }) { // use comma as separator
    var data = new Array[String] (5)
    data = line.split (cvsSplitBy)
    var name = data (0)
    var date = data (1)
    var address = data (2)
    var origin = data (3)
    var destination = data (4)
    //println(name+date+address+origin+destination)
    val sql = "INSERT INTO buy_ticket (name, date,address,origin,destination,ID) VALUES (?,?,?,?,?,?)"
    val prestatement = connection.prepareStatement (sql)
    prestatement.setString (1, name)
    prestatement.setString (2, date)
    prestatement.setString (3, address)
    prestatement.setString (4, origin)
    prestatement.setString (5, destination)
    prestatement.setInt (6, ID)
    prestatement.addBatch ()
    val rs = prestatement.executeUpdate()
    ID = ID + 1
  }
    catch {
    case e: IOException =>
    e.printStackTrace ()
    println ("全部写入完毕！")
  } finally if (br != null) br.close ()
    val select_sql = "select destination,count(1) as number from buy_ticket GROUP BY destination ORDER BY count(1) desc LIMIT 5"
    val query = connection.createStatement ()
    val query_result = query.executeQuery (select_sql)
    println ("排名前五的目的地查询结果如下：")
    while (query_result.next) {
    val name = query_result.getString ("destination")
    val num = query_result.getString ("number")
    //      println(name+"\t"+num)
    println ("目的地名称为%s, 目的地为此的买票记录共有%s条".format (name, num) )
  }
  }
  }
  }
}