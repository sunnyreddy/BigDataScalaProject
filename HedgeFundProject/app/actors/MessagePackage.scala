package actors

case class UserRegisterMessage(username: String, password: String, name: String, email: String, portfolioID: String, availableFund: Float)
case object RegisterSuccessMessage
case class UserConnected()
case class StartWorking(userName: String)

case class PurchaseMessage(stockCode: String, quantity: Double)
case object PurchaseSuccessMessage

case class Login(name: String, password: String)
case class LoginSuccessMessage(userName: String)



