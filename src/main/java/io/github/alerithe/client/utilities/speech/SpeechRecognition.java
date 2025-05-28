package io.github.alerithe.client.utilities.speech;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

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
                if (result == null) continue;
                String hypothesis = result.getHypothesis().trim();
                if (hypothesis.isEmpty()) continue;

                System.out.format("Speech Hypothesis: %s\n", result.getHypothesis());

                if (Minecraft.getInstance().world == null) continue;

                String[] args = hypothesis.split(" ");
                Command command = Client.COMMAND_MANAGER.find(args[0]);
                if (command != null) command.execute(args.length > 1 ? Arrays.copyOfRange(args, 1, args.length)
                        : new String[0]);
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
