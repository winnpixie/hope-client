package io.github.alerithe.client.events;

import io.github.alerithe.events.CancellableEvent;

public class EventUpdate extends CancellableEvent {
    public static class Pre extends EventUpdate {
        private double y;
        private float yaw;
        private float pitch;
        private boolean onGround;

        public Pre(double y, float yaw, float pitch, boolean onGround) {
            this.y = y;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public boolean isOnGround() {
            return onGround;
        }

        public void setOnGround(boolean onGround) {
            this.onGround = onGround;
        }
    }

    public static class Post {
    }
}
