package io.github.alerithe.client.features.friends;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.GameHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FriendManager extends FeatureManager<Friend> {
    @Override
    public void load() {
        setConfigurationFile(new File(Client.DATA_DIR, "friends.txt"));

        try {
            Files.readAllLines(getConfigurationFile().toPath()).forEach(line -> {
                String[] data = line.split(":");
                add(new Friend(data[0], data[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client.COMMAND_MANAGER.add(new Command("friends", new String[]{"friend", "f"},
                "<add/remove/rename/get/list> <name/page> [alias]") {
            private void listFriends(int page) {
                final int PER_PAGE = 7;
                int pageCount = (getChildren().size() - 1) / PER_PAGE; // 7 FRIENDS PER PAGE

                GameHelper.printChatMessage(String.format("\247eFriends (Page %d/%d)", page, pageCount + 1));
                GameHelper.printChatMessage("\2477Name (Alias)");
                for (int i = 0; i < PER_PAGE; i++) {
                    int idx = i + ((page - 1) * PER_PAGE);
                    if (idx > getChildren().size() - 1) break;

                    Friend friend = getChildren().get(idx);
                    GameHelper.printChatMessage(String.format("%s (%s)", friend.getName(), friend.getAliases()[0]));
                }
            }

            @Override
            public void execute(String[] args) {
                if (args.length < 2) {
                    listFriends(1);
                    return;
                }

                switch (args[0].toLowerCase()) {
                    case "add": {
                        if (find(args[1]) != null) {
                            GameHelper.printChatMessage(String.format("\247cFriend '%s' already exists.", args[1]));
                            break;
                        }

                        Friend friend = new Friend(args[1], args.length > 2 ? args[2] : args[1]);
                        add(friend);
                        GameHelper.printChatMessage(String.format("Added friend '%s' under the alias '%s'.",
                                friend.getName(), friend.getAliases()[0]));
                        break;
                    }
                    case "remove": {
                        Friend friend = find(args[1]);
                        if (friend == null) {
                            GameHelper.printChatMessage(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        remove(friend);
                        GameHelper.printChatMessage(String.format("Removed friend '%s'.", friend.getName()));
                        break;
                    }
                    case "rename": {
                        Friend friend = find(args[1]);
                        if (friend == null) {
                            GameHelper.printChatMessage(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        if (args.length < 3) {
                            GameHelper.printChatMessage("\247cNot enough arguments.");
                            break;
                        }

                        friend.setAliases(new String[]{args[2]});
                        GameHelper.printChatMessage(String.format("'%s' is now known as '%s'.", friend.getName(),
                                friend.getAliases()[0]));
                        break;
                    }
                    case "get": {
                        Friend friend = find(args[1]);
                        if (friend == null) {
                            GameHelper.printChatMessage(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        GameHelper.printChatMessage(String.format("'%s' is known as '%s'.", friend.getName(),
                                friend.getAliases()[0]));
                        break;
                    }
                    case "list": {
                        if (!MathHelper.isInt(args[1])) {
                            GameHelper.printChatMessage("\247cInvalid Argument Type.");
                            break;
                        }

                        listFriends(Integer.parseInt(args[1]));
                        break;
                    }
                    default:
                        GameHelper.printChatMessage(ErrorMessages.INVALID_ARG);
                        break;
                }
            }
        });
    }

    @Override
    public void save() {
        StringBuilder builder = new StringBuilder();
        getChildren().forEach(friend -> builder.append(friend.getName()).append(':')
                .append(friend.getAliases()[0]).append('\n'));

        try {
            Files.write(getConfigurationFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
