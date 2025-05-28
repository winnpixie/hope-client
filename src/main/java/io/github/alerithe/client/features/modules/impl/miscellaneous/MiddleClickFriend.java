package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventInput;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.entity.Entity;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", new String[]{"mcf"}, Type.MISCELLANEOUS);
    }

    @Subscribe
    private void onMiddleClick(EventInput.MiddleClick event) {
        if (GameHelper.getGame().objectMouseOver == null) return;
        if (GameHelper.getGame().objectMouseOver.entityHit == null) return;

        Entity entity = GameHelper.getGame().objectMouseOver.entityHit;
        Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
        if (friend != null) {
            Client.FRIEND_MANAGER.remove(friend);
            return;
        }

        Client.FRIEND_MANAGER.add(new Friend(entity.getName(), entity.getName()));
    }
}
