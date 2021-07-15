package Ozeki.Libs.Rest.Results.MessageReceiveResult

import Ozeki.Libs.Rest.Message

import scala.collection.mutable.ArrayBuffer

case class MessageReceiveResult(Folder : String, Limit : String, Messages : ArrayBuffer[Message] = ArrayBuffer[Message]())
{
  def addMessage(Message: Message) : Unit = {
    Messages += Message
  }

  override def toString: String = {
    "Message count: %d.".format(Messages.length)
  }
}
