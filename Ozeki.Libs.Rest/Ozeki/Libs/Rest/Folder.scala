package Ozeki.Libs.Rest


object Folder extends Enumeration {
  type Folder = Value
  val Inbox  = "inbox"
  val Outbox = "outbox"
  val Sent = "sent"
  val NotSent = "notsent"
  val Deleted = "deleted"
}
