package Ozeki.Libs.Rest.Results.MessageSendResult

import scala.collection.mutable.ArrayBuffer

case class MessageSendResults(TotalCount : Int = 0, SuccessCount : Int = 0, FailedCount : Int = 0, Results : ArrayBuffer[MessageSendResult] = new ArrayBuffer[MessageSendResult]())
{
  def add(result : MessageSendResult): Unit = {
    Results += result
  }
  override def toString: String = {
    return "Success: %s. Success: %s. Failed: %s.".format(TotalCount, SuccessCount, FailedCount)
  }
}
