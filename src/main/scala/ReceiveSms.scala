import Ozeki.Libs.Rest.{ Configuration, MessageApi }


object ReceiveSms  {
  def main(args: Array[String]): Unit = {

    val configuration = Configuration(
      Username = "http_user",
      Password = "qwe123",
      ApiUrl = "http://127.0.0.1:9509/api"
    )

    val api : MessageApi = MessageApi(configuration)

    val result = api.DownloadIncoming()

    println(result)

    for (message <- result.Messages) {
      println(message)
    }
  }
}