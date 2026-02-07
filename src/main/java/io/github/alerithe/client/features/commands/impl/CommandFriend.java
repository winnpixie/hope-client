package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;

public class CommandFriend extends Command {
    private static final int COMMANDS_PER_PAGE = 7;

    public CommandFriend() {
        super("friends", new String[]{"friend", "fr", "f"}, "<add|remove|rename|get|list> <name|page> [alias]");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            listFriends(1);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add": {
                if (Client.FRIEND_MANAGER.find(args[1]) != null) {
                    GameHelper.printChatMessage(String.format("\247cFriend '%s' already exists.", args[1]));
                    break;
                }

                Friend friend = new Friend(args[1], args.length > 2 ? args[2] : args[1]);
                Client.FRIEND_MANAGER.add(friend);
                GameHelper.printChatMessage(String.format("Added friend '%s' under the alias '%s'.",
                        friend.getName(), friend.getAliases()[0]));
                break;
            }
            case "remove": {
                Friend friend = Client.FRIEND_MANAGER.find(args[1]);
                if (friend == null) {
                    GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
                    break;
                }

                Client.FRIEND_MANAGER.remove(friend);
                GameHelper.printChatMessage(String.format("Removed friend '%s'.", friend.getName()));
                break;
            }
            case "rename": {
                Friend friend = Client.FRIEND_MANAGER.find(args[1]);
                if (friend == null) {
                    GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
                    break;
                }

                if (args.length < 3) {
                    GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
                    break;
                }

                String newAlias = args[2];
                friend.getAliases()[0] = newAlias;
                GameHelper.printChatMessage(String.format("'%s' is now known as '%s'.", friend.getName(),
                        newAlias));
                break;
            }
            case "get": {
                Friend friend = Client.FRIEND_MANAGER.find(args[1]);
                if (friend == null) {
                    GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
                    break;
                }

                GameHelper.printChatMessage(String.format("'%s' is known as '%s'.", friend.getName(),
                        friend.getAliases()[0]));
                break;
            }
            case "list": {
                if (!MathHelper.isInt(args[1])) {
                    GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
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

    private void listFriends(int page) {
        int pageCount = (Client.FRIEND_MANAGER.getChildren().size() - 1) / COMMANDS_PER_PAGE; // 7 FRIENDS PER PAGE

        GameHelper.printChatMessage(String.format("\247eFriends (Page %d/%d)", page, pageCount + 1));
        GameHelper.printChatMessage("\2477Name (Alias)");
        for (int i = 0; i < COMMANDS_PER_PAGE; i++) {
            int idx = i + ((page - 1) * COMMANDS_PER_PAGE);
            if (idx > Client.FRIEND_MANAGER.getChildren().size() - 1) break;

            Friend friend = Client.FRIEND_MANAGER.getChildren().get(idx);
            GameHelper.printChatMessage(String.format("%s (%s)", friend.getName(), friend.getAliases()[0]));
        }
    }
}
