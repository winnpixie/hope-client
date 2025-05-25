package io.github.alerithe.client.utilities.speech;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextToSpeech {
    private final List<String> messageQueue = new CopyOnWriteArrayList<>();

    private Voice voice;
    private Thread narrationThread;
    private boolean running;

    public void load() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();

        // Theoretically, this is the most "average" man voice.
        voice.setRate(150f);
        voice.setPitch(130f);
        voice.setPitchRange(50f);

        (narrationThread = new Thread(() -> {
            running = true;

            while (running) {
                if (messageQueue.isEmpty()) continue;

                voice.speak(messageQueue.get(0));
                messageQueue.remove(0);
            }
        })).start();
    }

    public String getActiveSpeech() {
        return messageQueue.isEmpty() ? "" : messageQueue.get(0);
    }

    public void narrate(String message) {
        messageQueue.add(message);
    }

    public void unload() {
        running = false;

        try {
            if (narrationThread != null) narrationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            voice.deallocate();
        }
    }
}
