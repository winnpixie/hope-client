package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.MathHelper;

public class F5360 extends Module {
    public float cameraYaw;
    public float cameraPitch;

    public F5360() {
        super("F5360", new String[0], Type.VISUAL);
    }


    public void setCameraAngles(float yaw, float pitch) {
        cameraYaw += yaw * 0.15f;
        cameraPitch -= pitch * 0.15f;
        cameraPitch = MathHelper.clamp(cameraPitch, -90f, 90f);
    }
}
