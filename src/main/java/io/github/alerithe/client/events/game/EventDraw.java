package io.github.alerithe.client.events.game;

import io.github.alerithe.events.CancellableEvent;
import net.minecraft.entity.Entity;

public class EventDraw {
    private final float partialTicks;

    public EventDraw(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public static class Overlay extends EventDraw {
        public Overlay(float partialTicks) {
            super(partialTicks);
        }
    }

    public static class World extends EventDraw {
        public World(float partialTicks) {
            super(partialTicks);
        }
    }

    public static class Tag extends CancellableEvent {
        private Entity entity;

        public Tag(Entity entity) {
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }
    }
}
