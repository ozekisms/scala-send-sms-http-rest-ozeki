import Ozeki.Libs.Rest.{ Configuration, Message, MessageApi, Folder }


object DeleteSms  {
  def main(args: Array[String]): Unit = {

    val configuration = Configuration(
      Username = "http_user",
      Password = "qwe123",
      ApiUrl = "http://127.0.0.1:9509/api"
    )

    val msg = Message(
      ID = "a1762c9d-c165-434b-8cd5-df895358e870"
    )

    val api = MessageApi(configuration)

    val result = api.Delete(Folder.Inbox, msg)

    println(result)
  }
}