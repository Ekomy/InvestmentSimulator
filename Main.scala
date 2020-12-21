package demo
import demo.InvestmentSystem
import demo.Wallet
import sun.nio.cs.UTF_8
import java.io.{BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream, File, FileWriter, ObjectInputStream, ObjectOutputStream}
import java.util.Base64
import scala.collection.mutable

object Main {

  def serialise(value: Any, value2: Any): Unit = {
    {
      val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(stream)
      oos.writeObject(value)
      oos.close
      val result = new String(Base64.getEncoder().encode(stream.toByteArray))

      val file = new File("inv.txt")
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(result)
      bw.close()
    }
    {
      val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(stream)
      oos.writeObject(value2)
      oos.close
      val result = new String(Base64.getEncoder().encode(stream.toByteArray))

      val file = new File("wal.txt")
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(result)
      bw.close()
    }
  }

  def deserialise(): (InvestmentSystem, Wallet) = {

    val file = new File("inv.txt")
    val file2 = new File("wal.txt")

    if (!file.exists() || !file2.exists()) {
      var Funds: mutable.Map[String, Int] = mutable.Map(
        "RB" -> 0,
        "OB" -> 0,
        "SW" -> 0,
        "BA" -> 0,
        "AC" -> 0,
      )
      var wal = new Wallet
      wal.setFunds(Funds)
      return (new InvestmentSystem, wal)
    }

    var str: String = ""
    scala.io.Source.fromFile("inv.txt").getLines().foreach{ line =>
      str += line
    }
    val bytes = Base64.getDecoder().decode(str.getBytes())
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close

    var str2: String = ""
    scala.io.Source.fromFile("wal.txt").getLines().foreach{ line =>
      str2 += line
    }
    val bytes2 = Base64.getDecoder().decode(str2.getBytes())
    val ois2 = new ObjectInputStream(new ByteArrayInputStream(bytes2))
    val value2 = ois2.readObject
    ois2.close

    (value.asInstanceOf[InvestmentSystem], value2.asInstanceOf[Wallet])
  }

  def main(args: Array[String]): Unit = {
    var (system, wallet) = deserialise()
    system.run(wallet)
    serialise(system, wallet)
  }

}
