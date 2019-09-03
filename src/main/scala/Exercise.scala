package org.mackler.cvičenie

import scala.io.Source
import javax.sound.sampled.AudioFileFormat.Type._
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

import javax.sound.sampled.AudioInputStream
  def write(name: String, audioContent: AudioInputStream) {
    val outputFilename = name + ".wav"
//    val out: OutputStream = new FileOutputStream(outputFilename)
    try {
      AudioSystem.write(audioContent,
//        WAVE,
//javax.sound.sampled.AudioSystem.getAudioFileTypes.apply(0),
        javax.sound.sampled.AudioFileFormat.Type.WAVE,
        new File(outputFilename))
      println(s"wrote $outputFilename")
    } catch {
      case t: Throwable => System.err.println(s"error writing $name: ${t.getMessage}")
    } finally {
      audioContent.close()
    }
  }

}
