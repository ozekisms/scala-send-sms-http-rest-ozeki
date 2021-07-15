import Ozeki.Libs.Rest.{ Configuration, Message, MessageApi }


object SendMultipleSms {
  def main(args: Array[String]): Unit = {

    val configuration = Configuration(
      Username = "http_user",
      Password = "qwe123",
      ApiUrl = "http://127.0.0.1:9509/api"
    )

    val msg1 = Message(
      ToAddress = "+36201111111",
      Text = "Hello world 1"
    )

    val msg2 = Message(
      ToAddress = "+36202222222",
      Text = "Hello world 2"
    )

    val msg3 = Message(
      ToAddress = "+36203333333",
      Text = "Hello world 3"
    )

    val api = MessageApi(configuration)

    val result = api.Send(List[Message]( msg1, msg2, msg3 ))

    println(result)
  }
}