package Ozeki.Libs.Rest.Results.MessageSendResult

import Ozeki.Libs.Rest.Message

case class MessageSendResult(Message : Message, Status : DeliveryStatus.DeliveryStatus = DeliveryStatus.Failed, ResponseMsg : String = "") {

  override def toString: String = {
    return "%s, %s".format(Status, Message)
  }
}
