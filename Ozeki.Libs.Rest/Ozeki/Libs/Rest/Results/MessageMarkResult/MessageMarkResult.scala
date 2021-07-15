package Ozeki.Libs.Rest.Results.MessageMarkResult

import scala.collection.mutable.ArrayBuffer

case class MessageMarkResult(Folder : String, MessageIdsMarkSucceeded : ArrayBuffer[String] = ArrayBuffer[String](),
                          MessageIdsMarkFailed : ArrayBuffer[String] = ArrayBuffer[String]()
)
{
  def addIdMarkSucceeded(ID : String): Unit = {
    MessageIdsMarkSucceeded += ID
  }

  def addIdMarkFailed(ID : String): Unit = {
    MessageIdsMarkFailed += ID
  }

  override def toString: String = {
    "Total: %d. Success: %d. Failed: %d.".format((MessageIdsMarkSucceeded.length + MessageIdsMarkFailed.length),
      MessageIdsMarkSucceeded.length, MessageIdsMarkFailed.length)
  }
}
