package io.github.alerithe.client.events.game;

import io.github.alerithe.events.CancellableEvent;
import net.minecraft.block.Block;
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

    public static class Environment extends EventSlowdown {
        private final Block block;

        public Environment(Block block) {
            this.block = block;
        }

        public Block getBlock() {
            return block;
        }
    }
}
