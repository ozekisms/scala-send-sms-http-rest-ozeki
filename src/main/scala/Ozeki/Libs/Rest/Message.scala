package Ozeki.Libs.Rest

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import java.util.UUID
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import Ozeki.Libs.Rest.org.json.{ JSONArray, JSONObject }

case class Message  (
    ID : String = UUID.randomUUID().toString, FromConnection : String = null, FromAddress : String = null, FromStation : String = null,
    ToConnection : String = null, ToAddress : String = null, ToStation : String = null, Text : String = null,
    CreateDate : LocalDateTime = LocalDateTime.now(), ValidUntil : LocalDateTime = LocalDateTime.now().plusDays(7),
    TimeToSend : LocalDateTime = LocalDateTime.MIN, IsSubmitReportRequested : Boolean = true,
    IsDeliveryReportRequested : Boolean = true, IsViewReportRequested : Boolean = true,
    Tags : ArrayBuffer[mutable.Map[String, String]] = ArrayBuffer[mutable.Map[String, String]]()
)
{
  def addTag(name : String, value : String) : Unit  = {
    val map : mutable.Map[String, String] = mutable.Map[String, String]()
    map.addOne("name", name)
    map.addOne("value", value)
    Tags += map
  }

  def getTags : JSONArray = {
    val array = new JSONArray()
    for (tag <- Tags) {
      val jsonTag = new JSONObject()
      jsonTag.put("name", tag("name"))
      jsonTag.put("value", tag("value"))
      array.put(jsonTag)
    }
    array
  }

  def getJSON : JSONObject = {
    val json : JSONObject = new JSONObject()
    val DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    if (ID != "") { json.put("message_id", ID) }
    if (FromConnection != "") { json.put("from_connection", FromConnection) }
    if (FromAddress != "") { json.put("from_address", FromAddress) }
    if (FromStation != "") { json.put("from_station", FromStation) }
    if (ToConnection != "") { json.put("to_connection", ToConnection) }
    if (ToAddress != "") { json.put("to_address", ToAddress) }
    if (ToStation != "") { json.put("to_station", ToStation) }
    if (Text != "") { json.put("text", Text) }
    if (CreateDate.format(DateFormat) != "") { json.put("create_date",
      CreateDate.format(DateFormat).replace(" ", "T")) }
    if (ValidUntil.format(DateFormat) != "") { json.put("valid_until",
      ValidUntil.format(DateFormat).replace(" ", "T")) }
    if (TimeToSend.format(DateFormat) != "") { json.put("time_to_send",
      TimeToSend.format(DateFormat).replace(" ", "T")) }
    if (IsSubmitReportRequested) { json.put("submit_report_requested", true) } else { json.put("submit_report_requested", false) }
    if (IsDeliveryReportRequested) { json.put("delivery_report_requested", true) } else { json.put("delivery_report_requested", false) }
    if (IsViewReportRequested) { json.put("view_report_requested", true) } else { json.put("view_report_requested", false) }
    if (Tags.nonEmpty) { json.put("tags", getTags) }
    json
  }

  def parse(json : JSONObject) : Message = {
    val msg = Message(
      ID = json.getString("message_id"),
      FromConnection = if ( json.has("from_connection") ) { json.getString("from_connection") } else { null },
      FromAddress = if ( json.has("from_address") ) { json.getString("from_address") } else { null },
      FromStation = if ( json.has("from_station") ) { json.getString("from_station") } else { null },
      ToConnection = if ( json.has("to_connection") ) { json.getString("to_connection") } else { null },
      ToAddress = if ( json.has("to_address") ) { json.getString("to_address") } else { null },
      ToStation = if ( json.has("to_station") ) { json.getString("to_station") } else { null },
      Text = if ( json.has("text") ) { json.getString("text") } else { null },
      CreateDate = if ( json.has("create_date") ) { LocalDateTime.parse(json.getString("create_date"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) } else { null },
      ValidUntil = if ( json.has("valid_until") ) { LocalDateTime.parse(json.getString("valid_until"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) } else { null },
      TimeToSend = if ( json.has("time_to_send") ) { LocalDateTime.parse(json.getString("time_to_send"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) } else { null },
    )
    val tags = if ( json.has("tags") ) { json.getJSONArray("tags") } else { null }
    if (tags != null) {
      for (i <- 0 until tags.length) {
        val tag = tags.getJSONObject(i)
        msg.addTag(tag.getString("name"), tag.getString("value"))
      }
    }
    msg
  }

  override def toString : String = {
    "%s->%s '%s'".format(FromAddress, ToAddress, Text)
  }
}


