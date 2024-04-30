package io.github.alerithe.client.utilities;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TTSManager {
    private final List<String> speechQueue = new CopyOnWriteArrayList<>();

    private Voice voice;
    private Thread speechThread;
    private boolean running;

    public void load() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        voice.setRate(130F);
        voice.setPitch(60F);
        (speechThread = new Thread(() -> {
            running = true;

            while (running) {
                if (!speechQueue.isEmpty()) {
                    voice.speak(speechQueue.get(0));
                    speechQueue.remove(0);
                }
            }
        })).start();
    }

    public String getCurrentMessage() {
        return speechQueue.isEmpty() ? "" : speechQueue.get(0);
    }

    public void queueText(String message) {
        speechQueue.add(message);
    }

    public void unload() {
        running = false;

        try {
            speechThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        voice.deallocate();
    }
}
