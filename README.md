This application generates audio flashcards for learning a foreign
language.  It uses Google's text-to-speech service.  Put the text for the cards in
the `src/main/resources` directory.  The application reads the files as
tab-separated-values, one card per line, ignoring blank lines and lines beginning with
`#`.

Each line has three fields: the first field is the prompt, the second
the question, the third the answer.  The generated sound file will
play the prompt, followed by a pause, followed by the question,
followed by the answer repeated twice, with pauses before and after
each.

Pass the names of the text files to be read, excluding the directory name and `.tsv`
file extension, as command-line arguments.

You must set the shell variable `GOOGLE_APPLICATION_CREDENTIALS`, na príklad:

    export GOOGLE_APPLICATION_CREDENTIALS="/usr/local/src/cvičenia/google-service-key-dc5ea44d6f84.json"

See:
* https://cloud.google.com/text-to-speech/docs/quickstart-client-libraries
* https://developers.google.com/resources/api-libraries/documentation/texttospeech/v1beta1/java/latest
