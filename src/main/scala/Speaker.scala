package org.mackler.cvičenie

import com.google.cloud.texttospeech.v1._
import com.google.protobuf.ByteString

object Speaker {

  val textToSpeechClient: TextToSpeechClient = TextToSpeechClient.create()

  val voice: VoiceSelectionParams = VoiceSelectionParams.newBuilder().
                                    setLanguageCode("sk-SK").
                                    setSsmlGender(SsmlVoiceGender.NEUTRAL).
                                    setName("sk-SK-Wavenet-A").
                                    build()

    
  val audioConfig: AudioConfig = AudioConfig.newBuilder().
                                 setAudioEncoding(AudioEncoding.LINEAR16).
                                 build()

  /** Convert the given SSML String to speech audio. */
  def textToSpeech(ssml: String): Array[Byte] = {
    val input: SynthesisInput = SynthesisInput.newBuilder().
                                setSsml(ssml).
                                build()

    val response: SynthesizeSpeechResponse =
      textToSpeechClient.synthesizeSpeech(input, voice, audioConfig)

    val audioContents: ByteString = response.getAudioContent()

    audioContents.toByteArray()
  }

  def closeClient() {
    textToSpeechClient.close()
  }

}
