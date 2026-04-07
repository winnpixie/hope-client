package io.github.alerithe.client.utilities;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SpeechEngine {
    private final Deque<String> speechQueue = new ConcurrentLinkedDeque<>();

    private Voice voice;
    private boolean talking;

    public void init() {
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        voice.setVolume(1f);
        voice.setRate(150f);
        voice.setPitch(69f);

        talk();
    }

    private void talk() {
        talking = true;

        new Thread(() -> {
            while (talking) {
                if (speechQueue.isEmpty()) continue;

                voice.speak(speechQueue.remove());
            }

            voice.deallocate();
        }).start();
    }

    public void cleanUp() {
        talking = false;
    }

    public void queue(String statement) {
        speechQueue.add(statement);
    }
}
