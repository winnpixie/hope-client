package io.github.alerithe.client.utilities.speech;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class SpeechRecognition {
    private final Configuration configuration = new Configuration();

    private LiveSpeechRecognizer recognizer;
    private Thread commandThread;
    private boolean running;

    public void load() {
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            this.recognizer = new LiveSpeechRecognizer(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        (commandThread = new Thread(() -> {
            running = true;

            recognizer.startRecognition(true);
            while (running) {
                SpeechResult result = recognizer.getResult();
                if (result != null) System.out.format("Hypothesis: %s\n", result.getHypothesis());
            }
        })).start();
    }

    public void unload() {
        running = false;

        try {
            if (commandThread != null) commandThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (recognizer != null) recognizer.stopRecognition();
        }
    }
}
