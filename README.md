# Scala send SMS with Ozeki SMS Gateway


### How to use the Ozeki.Libs.Rest library

In order to send sms messages using scala, you have to import the contents of the Ozeki.Libs.Rest library into your project (you also have to include the library in your project).

```scala
import Ozeki.Libs.Rest.{ Configuration, Message, MessageApi }
```

To send an SMS message, first of all you need to create a configuration.

```scala
val configuration = Configuration(
 Username = "http_user",
 Password = "qwe123",
 ApiUrl = "http://127.0.0.1:9509/api"
)
```

To create a message, you will use the Message class of the Ozeki.Libs.Rest library:

```scala
val msg = Message(
 ToAddress = "+36201111111",
 Text = "Helllo world!"
)
```

To send your previously created SMS message you need to create a MessageApi instance:

```scala
val api = MessageApi(configuration)
```

After these setps, you can sedn your SMS by calling the Send() method of the MessageApi class:

```scala
val result = api.Send(msg)
```

This method will return with a MessageSendResult object, which we can save and print to the console.

```scala
println(result)
```
