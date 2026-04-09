package io.github.alerithe.client.features.friends;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;

import java.io.IOException;
import java.nio.file.Files;

public class FriendManager extends FeatureManager<Friend> {
    @Override
    public void load() {
        setDataPath(Client.DATA_PATH.resolve("friends.properties"));

        try {
            Files.readAllLines(getDataPath()).forEach(line -> {
                String[] data = line.split(":");
                add(new Friend(data[0], data[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        StringBuilder builder = new StringBuilder();
        getElements().forEach(friend -> builder.append(friend.getName()).append(':')
                .append(friend.getAliases()[0]).append('\n'));

        try {
            Files.write(getDataPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
