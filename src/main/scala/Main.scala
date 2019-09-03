package org.mackler.cvičenie

import com.google.cloud.texttospeech.v1._
import com.google.protobuf.ByteString
import java.io.{ FileOutputStream, OutputStream }

/** A simple example of using the Google text-to-speech service */

object Main extends App {
  val testText = """<speak>
                      <audio src="https://storage.googleapis.com/org_mackler_sounds/rollout24k.mp3">
                        nefunguje
                        <break time="3s"/>
                      </audio>
                      Ahoj svet!
                    </speak>"""
  val textToSpeechClient: TextToSpeechClient = TextToSpeechClient.create()

  // TODO: If there are exceptions in both the try-block and the
  // finally-block, then the latter will “overthrow” the former and
  // that exception will be lost.
  // Search StackOverflow for "scala try-with-resources" for
  // workarounds.

  try {
    // Set the text input to be synthesized
    val input: SynthesisInput = SynthesisInput.newBuilder().
                                setSsml(testText).
                                build();

    // Build the voice request, select the language code ("sk-SK") and the ssml voice gender
    // ("neutral")
    val voice: VoiceSelectionParams = VoiceSelectionParams.newBuilder().
                                      setLanguageCode("sk-SK").
                                      setSsmlGender(SsmlVoiceGender.NEUTRAL).
                                      build()
    
    // Select the type of audio file to be returned
    val audioConfig: AudioConfig = AudioConfig.newBuilder().
                                   setAudioEncoding(AudioEncoding.MP3).
                                   build()

    // Perform the text-to-speech request on the text input with the selected voice parameters and
    // audio file type
    val response: SynthesizeSpeechResponse =
      textToSpeechClient.synthesizeSpeech(input, voice, audioConfig)

    // Get the audio contents from the response
    val audioContents: ByteString = response.getAudioContent()

    // Write the response to the output file.
    val outputFilename = "output.mp3"
    val out: OutputStream = new FileOutputStream(outputFilename)
    // TODO: see comment above regarding try-with-resources in Scala.
    try {
      out.write(audioContents.toByteArray())
      println(s"Audio content written to file $outputFilename")
    } finally {
      out.close()
    }

  } catch {
    case t: Throwable => println(s"Fail: ${t.getMessage}")
  } finally {
    textToSpeechClient.close()
  }

  println()
}
