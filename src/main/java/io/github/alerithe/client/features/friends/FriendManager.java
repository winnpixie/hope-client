package io.github.alerithe.client.features.friends;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FriendManager extends FeatureManager<Friend> {
    @Override
    public void load() {
        setConfigFile(new File(Client.DATA_DIR, "friends.txt"));

        try {
            Files.readAllLines(getConfigFile().toPath()).forEach(line -> {
                String[] data = line.split(":");
                add(new Friend(data[0], data[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client.COMMAND_MANAGER.add(new Command("friends", new String[]{"friend", "f"},
                "<add/remove/rename/get/list> <name/page> [alias]") {
            private void listFriends(int page) {
                int maxPages = (getElements().size() - 1) / 7;
                int thePage = page;
                if (thePage < 1) {
                    thePage = 1;
                }
                if (thePage > maxPages + 1) {
                    thePage = maxPages + 1;
                }
                thePage -= 1;

                int endIndex = Math.min((thePage + 1) * 7, getElements().size());
                Wrapper.printChat(String.format("\247eFriends (Page %d/%d)", thePage + 1, maxPages + 1));
                Wrapper.printChat("\2477Name (Alias)");
                for (int i = page * 7; i < endIndex; ++i) {
                    Friend friend = getElements().get(i);
                    Wrapper.printChat(String.format("%s (%s)", friend.getName(), friend.getAliases()[0]));
                }
            }

            @Override
            public void execute(String[] args) {
                if (args.length < 2) {
                    listFriends(0);
                    return;
                }

                switch (args[0].toLowerCase()) {
                    case "add": {
                        if (get(args[1]) != null) {
                            Wrapper.printChat(String.format("\247cFriend '%s' already exists.", args[1]));
                            break;
                        }

                        Friend friend = new Friend(args[1], args.length > 2 ? args[2] : args[1]);
                        add(friend);
                        Wrapper.printChat(String.format("Added friend '%s' under the alias '%s'.",
                                friend.getName(), friend.getAliases()[0]));
                        break;
                    }
                    case "remove": {
                        Friend friend = get(args[1]);
                        if (friend == null) {
                            Wrapper.printChat(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        remove(friend);
                        Wrapper.printChat(String.format("Removed friend '%s'.", friend.getName()));
                        break;
                    }
                    case "rename": {
                        Friend friend = get(args[1]);
                        if (friend == null) {
                            Wrapper.printChat(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        if (args.length < 3) {
                            Wrapper.printChat("\247cNot enough arguments.");
                            break;
                        }

                        friend.setAliases(new String[]{args[2]});
                        Wrapper.printChat(String.format("'%s' is now known as '%s'.", friend.getName(),
                                friend.getAliases()[0]));
                        break;
                    }
                    case "get": {
                        Friend friend = get(args[1]);
                        if (friend == null) {
                            Wrapper.printChat(String.format("\247cNo such friend '%s'.", args[1]));
                            break;
                        }

                        Wrapper.printChat(String.format("'%s' is known as '%s'.", friend.getName(),
                                friend.getAliases()[0]));
                        break;
                    }
                    case "list": {
                        if (!MathHelper.isInt(args[1])) {
                            Wrapper.printChat("\247cInvalid Argument Type.");
                            break;
                        }

                        listFriends(Integer.parseInt(args[1]));
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void save() {
        StringBuilder builder = new StringBuilder();
        getElements().forEach(friend -> builder.append(friend.getName()).append(':')
                .append(friend.getAliases()[0]).append('\n'));

        try {
            Files.write(getConfigFile().toPath(), builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
