package ru.ifmo.backend_2021.pseudodb

import ru.ifmo.backend_2021
import ru.ifmo.backend_2021.Message

import java.io.File
import scala.io.Source

object PseudoDB {
  def apply(filename: String, clean: Boolean): PseudoDB = {
    val db = new PseudoDB(filename)
    if (clean) db.clear()
    db
  }
}

class PseudoDB(filename: String) extends MessageDB {

  lazy val defaultMessages =
    List(
      Message(
        1,
        "ventus976",
        "I don't particularly care which interaction they pick so long as it's consistent.",
        None
      ),
      Message(
        2,
        "XimbalaHu3",
        "Exactly, both is fine but do pick one.",
        None
      ),
      Message(
        3,
        "XimbalaHu3",
        "Worse it!",
        Option(2)
      ),
      Message(
        4,
        "thresh",
        "Test Message!",
        None
      ),
      Message(
        5,
        "thresh",
        "Test Message!",
        None
      ),
      Message(
        6,
        "thresh",
        "You can always trust Braum!",
        None
      )
    )

  def clear(): Unit =
    new File(filename).delete()

  def createIfNotExists(): Unit = {
    val file = new File(filename)
    if (!file.exists()) {
      file.createNewFile()
      FileUtils.withFileWriter(filename)(
        _.write(defaultMessages.map(_.toFile).mkString("\n"))
      )
    }
  }

  def getMessages: List[Message] = synchronized {
    createIfNotExists()
    FileUtils.withFileReader[List[Message]](filename)(_.map(Message(_)))
  }

  def addMessage(message: Message): Unit = synchronized {
    createIfNotExists()
    val source = Source.fromFile(filename)
    val result = FileUtils.withFileReader[List[String]](filename)(
      identity
    ) :+ message.toFile
    FileUtils.withFileWriter(filename)(_.write(result.mkString("\n")))
    source.close()
  }
}
