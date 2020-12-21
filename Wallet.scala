package demo
import scala.collection.mutable

class Wallet extends Serializable{

  private var Balance : Double = 1000
  private var Funds: mutable.Map[String, Int] = mutable.Map(
    "RB" -> 0,
    "OB" -> 0,
    "SW" -> 0,
    "BA" -> 0,
    "AC" -> 0,
  )

  def getFunds(): mutable.Map[String, Int] ={
    return Funds
  }

  def getBalance(): Double = {
    return Balance
  }

  def setFund(fund: String, count: Int) {
    Funds.update(fund, count)
  }

  def setFunds(funds: mutable.Map[String, Int]) {
    Funds = funds
  }

  def setBalance(balance: Double) {
    var b = (balance*100).asInstanceOf[Int].asInstanceOf[Double]/100
    Balance = b
  }
}
