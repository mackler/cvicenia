package org.mackler.cvičenie

import javax.sound.sampled.AudioInputStream
import ws.schild.jave.{ Encoder, AudioAttributes, EncodingAttributes, MultimediaObject }

import scala.io.Source
import javax.sound.sampled.AudioFileFormat.Type.WAVE
import javax.sound.sampled.AudioSystem
import java.io.{ File, FileOutputStream, OutputStream }

object Exercise {

  case class Item(prompt: String, question: String, answer: String)

  /** Read three-part items from a tab-separated file, ignoring lines that are
    * either empty or begin with a '#' character. */
  def read(name: String): Seq[Item] = {
    Source.fromResource(name + ".tsv").getLines.toSeq.filter { l =>
      l.length > 0 && l(0) != '#'
    } map { i =>
        val elems = i.split('\t')
        Item(elems(0), elems(1), elems(2))
      }
  }

  /** Write the given audio content as an MP3 file,
    * named based on the exercise name and item number. */
  def write(exerciseName: String, itemName: String, audioContent: AudioInputStream) {
    val outputFilenameWav = itemName + ".wav"
    val outputFilenameMp3 = itemName + ".mp3"

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
      println(s"wrote $outputFilenameWav")

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

    } catch {
      case t: Throwable => System.err.println(s"error writing $itemName: ${t.getMessage}")
    } finally {
      audioContent.close()
    }
  }

}
