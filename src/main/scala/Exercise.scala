package org.mackler.cvičenie

import javax.sound.sampled.AudioInputStream
import ws.schild.jave.{ Encoder, AudioAttributes, EncodingAttributes, MultimediaObject }

import scala.io.Source
import scala.util.{ Failure, Success, Try }

import java.io.{ File, FileOutputStream, OutputStream }
import java.util.regex.{ Matcher, Pattern }
import javax.sound.sampled.AudioFileFormat.Type.WAVE
import javax.sound.sampled.AudioSystem

object Exercise {

  case class Item(question: Vector[String], answer: String)

  val sentence = Pattern.compile("""\s*(.+?([.?]|$))\s*""")

  /** Read multi-part items from a file of punctuation-separated lines,
    * ignoring lines that are either empty or begin with a '#' character.
    * The answer is the last sentence on the line.  The question is a Vector of
    * all the sentences except the last. */
  def read(name: String): Try[Generator] = Try {
    Source.fromResource(name + ".txt").getLines.toSeq filter { l =>
      l.length > 0 && l(0) != '#'
    } map { line =>
      val matcher = sentence matcher line

      @annotation.tailrec
      def iter(acc: Vector[String] = Vector.empty[String]): Item = {
        if (!matcher.find())
          Item(acc dropRight 1, acc.last)
        else
          iter(acc :+ (matcher group 1))
      }

      iter()
    }
  } match {
    case f:Failure[Seq[Item]] => Option(f.exception.getMessage) match {
      case Some(e) => Failure[Generator](f.exception)
      case None => Failure(new java.io.FileNotFoundException(s"failed to read exercise ${name}"))
    }
    case s:Success[Seq[item]] => s map (new Generator(name, _))
  }

  /** Write the given audio content as an MP3 file,
    * named based on the exercise name and item number, and answer text. */
  def write(exerciseName: String, itemNumber: String, answerText: String, audioContent: AudioInputStream) {
    val outputFilenameWav = answerText + ".wav"
    val outputFilenameMp3 = (if (itemNumber.length > 0) itemNumber + " - " else "") + answerText + ".mp3"

    val outputDirectory = new File("output/" + exerciseName)
    if (!outputDirectory.exists) {
      outputDirectory.mkdirs()
      println(s"created directory ${outputDirectory}")
    }

    val wavFile = new File(outputDirectory, outputFilenameWav)
    val mp3File = new File(outputDirectory, outputFilenameMp3)

    try {
      AudioSystem.write(audioContent,
        javax.sound.sampled.AudioFileFormat.Type.WAVE,
        wavFile
      )

      // convert WAV file to MP3file
      val audio = new AudioAttributes()
      audio.setCodec("libmp3lame")
      audio.setBitRate(32000);
      audio.setChannels(1)
      audio.setSamplingRate(24000);
      
      val attrs = new EncodingAttributes()
      attrs.setFormat("mp3");
      attrs.setAudioAttributes(audio)

      // encode and save mp3 file
      val encoder = new Encoder()
      encoder.encode(new MultimediaObject(wavFile), mp3File, attrs);

      wavFile.delete()

      println(s"wrote $mp3File")
    } catch {
      case t: Throwable => System.err.println(s"error writing $answerText: ${t.getMessage}")
    } finally {
      audioContent.close()
    }
  }

}
