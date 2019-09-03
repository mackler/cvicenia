package org.mackler.cvičenie

import javax.sound.sampled.AudioInputStream
import ws.schild.jave.{ Encoder, AudioAttributes, EncodingAttributes, MultimediaObject }

import scala.io.Source
import javax.sound.sampled.AudioFileFormat.Type.WAVE
import javax.sound.sampled.AudioSystem
import java.io.{ File, FileOutputStream, OutputStream }

object Exercise {

  case class Item(prompt: String, question: String, answer: String)

  def read(name: String): Seq[Item] = {
    Source.fromResource(name + ".tsv").getLines.toSeq.filter(_(0) != '#').
      map { i =>
        val elems = i.split('\t')
        Item(elems(0), elems(1), elems(2))
      }
  }

  def write(name: String, audioContent: AudioInputStream) {
    val outputFilenameWav = name + ".wav"
    val wavFile = new File(outputFilenameWav)
    val outputFilenameMp3 = name + ".mp3"
    val mp3File = new File(outputFilenameMp3)

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
      case t: Throwable => System.err.println(s"error writing $name: ${t.getMessage}")
    } finally {
      audioContent.close()
    }
  }

}
