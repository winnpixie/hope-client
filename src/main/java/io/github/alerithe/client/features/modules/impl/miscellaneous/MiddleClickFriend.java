package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventInput;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.entity.Entity;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", new String[]{"mcf"}, Type.MISCELLANEOUS);
    }

    @Register
    private void onMiddleClick(EventInput.MiddleClick event) {
        if (Wrapper.getGame().objectMouseOver == null) return;
        if (Wrapper.getGame().objectMouseOver.entityHit == null) return;

        Entity entity = Wrapper.getGame().objectMouseOver.entityHit;
        Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
        if (friend != null) {
            Client.FRIEND_MANAGER.remove(friend);
            return;
        }

        Client.FRIEND_MANAGER.add(new Friend(entity.getName(), entity.getName()));
    }
}
