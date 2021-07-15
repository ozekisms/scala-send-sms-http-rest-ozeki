package Ozeki.Libs.Rest

import Ozeki.Libs.Rest.Results.MessageDeleteResult.MessageDeleteResult
import Ozeki.Libs.Rest.Results.MessageMarkResult.MessageMarkResult
import Ozeki.Libs.Rest.Results.MessageReceiveResult.MessageReceiveResult
import Ozeki.Libs.Rest.Results.MessageSendResult._

import java.util.Base64
import java.net.URI
import java.net.http._
import Ozeki.Libs.Rest.org.json.{JSONArray, JSONObject}

case class MessageApi (_configuration : Configuration)
{
  def createAuthorizationHeader(Username : String, Password : String) : String = {
    val usernamePassword = "%s:%s".format(Username, Password).getBytes()
    val usernamePasswordEncoded = Base64.getEncoder.encodeToString(usernamePassword)
    "Basic %s".format(usernamePasswordEncoded)
  }

  def createRequestBody(Message: Message) : String = {
    val json : JSONObject = new JSONObject()
    val messagesArray = new JSONArray()
    messagesArray.put(Message.getJSON)
    json.put("messages", messagesArray)
    json.toString()
  }

  def createRequestBody(Messages: List[Message]) : String = {
    val json : JSONObject = new JSONObject()
    val messagesArray = new JSONArray()
    for (message <- Messages) {
      messagesArray.put(message.getJSON)
    }
    json.put("messages", messagesArray)
    json.toString()
  }

  def createRequestBodyToManipulate(Folder : String, Message: Message) : String = {
    val json : JSONObject = new JSONObject()
    val messagesArray = new JSONArray()
    messagesArray.put(Message.ID)
    json.put("folder", Folder)
    json.put("message_ids", messagesArray)
    json.toString()
  }

  def createRequestBodyToManipulate(Folder : String, Messages: List[Message]) : String = {
    val json : JSONObject = new JSONObject()
    val messagesArray = new JSONArray()
    for (message <- Messages) {
      messagesArray.put(message.ID)
    }
    json.put("folder", Folder)
    json.put("message_ids", messagesArray)
    json.toString()
  }

  def createUriToSendSms(url : String): String = {
    val uri : String = url.split("\\?")(0)
    "%s?action=sendmsg".format(uri)
  }

  def createUriToDeleteSms(url : String): String = {
    val uri : String = url.split("\\?")(0)
    "%s?action=deletemsg".format(uri)
  }

  def createUriToMarkSms(Url : String): String = {
    val uri : String = Url.split("\\?")(0)
    "%s?action=markmsg".format(uri)
  }

  def createUriToReceiveSms(Url : String, Folder : String): String = {
    val Uri : String = Url.split("\\?")(0)
    "%s?action=receivemsg&folder=%s".format(Uri, Folder)
  }

  def Send(Message: Message) : MessageSendResult = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBody(Message)
    getResponseSend(doRequestPost(createUriToSendSms(_configuration.ApiUrl), requestBody, authorizationHeader)).Results(0)
  }

  def Send(Messages : List[Message]) : MessageSendResults = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBody(Messages)
    getResponseSend(doRequestPost(createUriToSendSms(_configuration.ApiUrl), requestBody, authorizationHeader))
  }

  def Delete(Folder : String, Message: Message) : Boolean = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBodyToManipulate(Folder, Message)
    val result = getResponseDelete(doRequestPost(createUriToDeleteSms(_configuration.ApiUrl), requestBody, authorizationHeader), List(Message))
    if (result.MessageIdsRemoveSucceeded.length == 1 && result.MessageIdsRemoveFailed.isEmpty) { true } else { false }
  }

  def Delete(Folder : String, Messages: List[Message]) : MessageDeleteResult = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBodyToManipulate(Folder, Messages)
    getResponseDelete(doRequestPost(createUriToDeleteSms(_configuration.ApiUrl), requestBody, authorizationHeader), Messages)
  }


  def Mark(Folder : String, Message: Message) : MessageMarkResult = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBodyToManipulate(Folder, Message)
    getResponseMark(doRequestPost(createUriToMarkSms(_configuration.ApiUrl), requestBody, authorizationHeader), List(Message))
  }

  def Mark(Folder : String, Messages: List[Message]) : MessageMarkResult = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    val requestBody = createRequestBodyToManipulate(Folder, Messages)
    getResponseMark(doRequestPost(createUriToMarkSms(_configuration.ApiUrl), requestBody, authorizationHeader), Messages)
  }

  def DownloadIncoming() : MessageReceiveResult = {
    val authorizationHeader = createAuthorizationHeader(_configuration.Username, _configuration.Password)
    getResponseReceive(doRequestGet(createUriToReceiveSms(_configuration.ApiUrl, Folder.Inbox), authorizationHeader))
  }

  def getResponseSend(response : JSONObject) : MessageSendResults = {
    val data = response.getJSONObject("data")
    val results = MessageSendResults(data.getInt("total_count"),data.getInt("success_count"), data.getInt("failed_count") )
    val messages = data.getJSONArray("messages")
    for (i <- 0 until messages.length) {
      val message = messages.getJSONObject(i)
      val msg = Message().parse(message)
      if (message.getString("status") == "SUCCESS") {
        results.Results += MessageSendResult(msg, DeliveryStatus.Success)
      } else {
        results.Results += MessageSendResult(msg, DeliveryStatus.Failed, message.getString("status"))
      }
    }
    results
  }

  def getResponseDelete(Response : JSONObject, Messages : List[Message]) : MessageDeleteResult = {
    val result = MessageDeleteResult(Response.getJSONObject("data").getString("folder"))
    val MessageIDs = Response.getJSONObject("data").getJSONArray("message_ids")
    for (message <- Messages) {
      var success = false
      for (i <- 0 until MessageIDs.length) { if (MessageIDs.get(i) == message.ID) { success = true } }
      if (success) { result.addIdRemoveSucceeded(message.ID) } else { result.addIdRemoveFailed(message.ID) }
    }
    result
  }

  def getResponseMark(Response : JSONObject, Messages : List[Message]) : MessageMarkResult = {
    val result = MessageMarkResult(Response.getJSONObject("data").getString("folder"))
    val MessageIDs = Response.getJSONObject("data").getJSONArray("message_ids")
    for (message <- Messages) {
      var success = false
      for (i <- 0 until MessageIDs.length) { if (MessageIDs.get(i) == message.ID) { success = true } }
      if (success) { result.addIdMarkSucceeded(message.ID) } else { result.addIdMarkFailed(message.ID) }
    }
    result
  }

  def getResponseReceive(Response : JSONObject) : MessageReceiveResult = {
    val result = MessageReceiveResult(Response.getJSONObject("data").getString("folder"),
      Response.getJSONObject("data").getString("limit"))
    val messages = Response.getJSONObject("data").getJSONArray("data")
    for (i <- 0 until messages.length) {
      result.addMessage(Message().parse(messages.getJSONObject(i)))
    }
    Delete(Folder.Inbox, result.Messages.toList)
    result
  }

  def doRequestPost(url : String, requestBody : String, authorizationHeader : String) : JSONObject = {
    try {
        val client: HttpClient = HttpClient.newHttpClient
        val request: HttpRequest = HttpRequest.newBuilder
          .uri(new URI(url))
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .headers("Authorization", authorizationHeader, "Content-Type", "application/json", "Accept", "application/json")
          .build
        val response: HttpResponse[String] = client.send(request, HttpResponse.BodyHandlers.ofString)
        return new JSONObject(response.body)
    } catch {
      case e: Exception => println(e)
    }
    new JSONObject()
  }

  def doRequestGet(url : String, authorizationHeader : String) : JSONObject = {
    try {
      val client = HttpClient.newHttpClient
      val request = HttpRequest.newBuilder.uri(new URI(url)).GET.headers("Authorization", authorizationHeader).build
      val response = client.send(request, HttpResponse.BodyHandlers.ofString)
      return new JSONObject(response.body)
    } catch {
        case e: Exception => println(e)
    }
    new JSONObject()
  }
}