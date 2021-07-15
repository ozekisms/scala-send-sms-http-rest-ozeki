import Ozeki.Libs.Rest.{ Configuration, Message, MessageApi }


object SendSms  {
  def main(args: Array[String]): Unit = {
    
    val configuration = Configuration(
      Username = "http_user",
      Password = "qwe123",
      ApiUrl = "http://127.0.0.1:9509/api"
    )

    val msg = Message(
      ToAddress = "+36201111111",
      Text = "Hello world!"
    )

    val api = MessageApi(configuration)

    val result = api.Send(msg)

    println(result)
  }
}