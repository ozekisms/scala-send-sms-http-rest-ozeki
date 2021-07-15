package Ozeki.Libs.Rest.Results.MessageDeleteResult

import scala.collection.mutable.ArrayBuffer

case class MessageDeleteResult(Folder : String, MessageIdsRemoveSucceeded : ArrayBuffer[String] = ArrayBuffer[String](),
                          MessageIdsRemoveFailed : ArrayBuffer[String] = ArrayBuffer[String]()
)
{
  def addIdRemoveSucceeded(ID : String): Unit = {
    MessageIdsRemoveSucceeded += ID
  }

  def addIdRemoveFailed(ID : String): Unit = {
    MessageIdsRemoveFailed += ID
  }

  override def toString: String = {
    "Total: %d. Success: %d. Failed: %d.".format((MessageIdsRemoveSucceeded.length + MessageIdsRemoveFailed.length),
      MessageIdsRemoveSucceeded.length, MessageIdsRemoveFailed.length)
  }
}
