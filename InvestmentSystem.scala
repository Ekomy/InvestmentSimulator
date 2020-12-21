package demo
import scala.collection.mutable
import scala.io.StdIn.readLine

class InvestmentSystem extends Serializable{

  private var Funds:mutable.Map[String, Double] = mutable.Map(
    "RB" -> 100.0,
    "OB" -> 100.0,
    "SW" -> 100.0,
    "BA" -> 100.0,
    "AC" -> 100.0,
  )
  private var initial: Boolean = true
  private var initialTime: Long = System.currentTimeMillis()
  private var milliSecondsPassed: Long = 0

  def Buy(fund: String, unit: Int, wallet: Wallet): Wallet = {
    var price : Double = Funds.getOrElse(fund, 0.0) * unit
    price = (price*100).asInstanceOf[Int].asInstanceOf[Double]/100

    val balance: Double = wallet.getBalance()
    if (balance >= price) {
      wallet.setFund(fund, wallet.getFunds().getOrElse(fund, 0) + unit)
      wallet.setBalance(balance - price)
      return wallet
    }
    println("Insufficient balance")
    return wallet
  }

  def Sell(fund: String, unit: Int, wallet: Wallet): Wallet = {
    var price : Double = Funds.getOrElse(fund, 0.0) * unit
    price = (price*100).asInstanceOf[Int].asInstanceOf[Double]/100

    val balance: Double = wallet.getBalance()
    if (wallet.getFunds().getOrElse(fund, 0) >= unit) {
      wallet.setFund(fund, wallet.getFunds().getOrElse(fund, 0) - unit)
      wallet.setBalance(balance + price)
      return wallet
    }
    println("Insufficient fund: " + fund)
    return wallet
  }

  def ViewOptions (wallet: Wallet): Unit = {
    var total: Double = 0
    var price: Double = 0
    println("Balance: " + wallet.getBalance())
    wallet.getFunds().foreach{
      case (k, v) => {
        price = getPrice(k, v)
        price = (price*100).asInstanceOf[Int].asInstanceOf[Double]/100
        total += price
        println("Fund " + k + " : Amount = " + v + "\tPrice: " + price )
      }
    }
    total += wallet.getBalance()
    total = (total*100).asInstanceOf[Int].asInstanceOf[Double]/100

    println("Total: " + total)

  }

  def getPrice(key: String, count: Int): Double ={
    return Funds.getOrElse(key, 0.0) * count
  }

  def Valuation(count: Long): Unit ={
    var c = count
    if (c > 0){
      var newRP: Double = Funds.getOrElse("RP", 100)
      newRP += Math.random()*0.45-0.05
      var newOB: Double = Funds.getOrElse("OB", 100)
      newOB += Math.random()*0.75-0.15
      var newSW: Double = Funds.getOrElse("SW", 100)
      newSW += Math.random()*1.5-0.85
      var newBA: Double = Funds.getOrElse("BA", 100)
      newBA += Math.random()*1.7-0.8
      var newAC: Double = Funds.getOrElse("AC", 100)
      newAC += Math.random()*2.1-1

      Funds.update("RP", if(newRP <= 0) 0 else newRP)
      Funds.update("OB", if(newOB <= 0) 0 else newOB)
      Funds.update("SW", if(newSW <= 0) 0 else newSW)
      Funds.update("BA", if(newBA <= 0) 0 else newBA)
      Funds.update("AC", if(newAC <= 0) 0 else newAC)
      c -= 1
      Valuation(c)
    }
  }

  def isAllDigits(x: String) = x forall Character.isDigit

  def run(wallet: Wallet): Unit ={
    milliSecondsPassed += (System.currentTimeMillis() - initialTime)
    val minutes = milliSecondsPassed/600000
    if (minutes > 0){
      initialTime = System.currentTimeMillis()
      milliSecondsPassed = 0
      Valuation(minutes)
    }
    if(initial){
      println("Action options: ")
      println("Buy  - (eg. B AC 4)")
      println("Sell - (eg. S RB 2): ")
      println("View - To view balance options")
      println("Exit - Exit the application")
      initial = false
    }
    print(">>> ")

    val line = readLine()
    val tokens = line.split(" ")

    if (tokens(0) == "View"){
      ViewOptions(wallet)
    }else if (tokens(0) == "B"){
      if (Funds.contains(tokens(1)) && isAllDigits(tokens(2))){
        Buy(tokens(1), tokens(2).toInt, wallet)
      }
    }else if (tokens(0) == "S"){
      if (Funds.contains(tokens(1)) && isAllDigits(tokens(2))){
        Sell(tokens(1), tokens(2).toInt, wallet)
      }
    }else if (tokens(0) == "Exit"){
      initial = true
      return
    }
    run(wallet)
  }

}