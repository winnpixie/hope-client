package io.github.alerithe.client.events;

import io.github.alerithe.events.CancellableEvent;
import net.minecraft.item.ItemStack;

public class EventSlowdown extends CancellableEvent {
    public static class Item extends EventSlowdown {
        private final ItemStack itemStack;

        public Item(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public static class Block extends EventSlowdown {
        private final Block block;

        public Block(Block block) {
            this.block = block;
        }

        public Block getBlock() {
            return block;
        }
    }
}
