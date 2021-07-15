import Ozeki.Libs.Rest.{Configuration, Message, MessageApi}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object SendScheduledSms  {
  def main(args: Array[String]): Unit = {

    val DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val configuration = Configuration(
      Username = "http_user",
      Password = "qwe123",
      ApiUrl = "http://127.0.0.1:9509/api"
    )

    val msg = Message(
      ToAddress = "+36201111111",
      Text = "Hello world!",
      TimeToSend = LocalDateTime.parse("2021-07-15 16:30:00", DateFormat)

    )

    val api = MessageApi(configuration)

    val result = api.Send(msg)

    println(result)
  }
}
