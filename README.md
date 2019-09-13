This application generates audio flashcards for learning a foreign
language.  It uses Google's text-to-speech service.  Put the text for the cards under
the `src/main/resources` directory, in files named with `.txt` suffix.
The application reads the files, one card per line, ignoring blank lines and
lines beginning with`#`.

Each line is read as a sequence of sentences: all sentences but the last become the question;
the last sentence becomes the answer.
The generated sound file will play the question sentences, separated by pauses,
followed by the answer repeated twice, once slowly, with longer pauses before and after
each repetition.

Pass the names of the text files to be read, excluding the `.txt`
file extension, as command-line arguments.  Audio files will be placed in a directory named
`output`, created if necessary, in a directory structure matching that in which the
input files are located.

To emphasise parts of the card, use single or double asterisks around the text to be
emphasized.  Emphases are removed from the second repetition of the answer.

# Using Google's Text-To-Speech Service

Using this application requires a Google Cloud account.

You must set the shell variable `GOOGLE_APPLICATION_CREDENTIALS`, na príklad:

    export GOOGLE_APPLICATION_CREDENTIALS="/usr/local/src/cvičenia/google-service-key-dc5ea44d6f84.json"

See:
* https://cloud.google.com/text-to-speech/docs/quickstart-client-libraries
* https://developers.google.com/resources/api-libraries/documentation/texttospeech/v1beta1/java/latest
