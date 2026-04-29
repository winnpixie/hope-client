package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BreadCrumbs extends Module {
    private final BooleanProperty testDepth = new BooleanProperty("DepthTest", new String[]{"depth"}, true);

    private final List<double[]> positions = new ArrayList<>();

    private float baseHue;

    public BreadCrumbs() {
        super("BreadCrumbs", new String[0], Type.VISUAL);

        getPropertyManager().add(testDepth);
    }

    @Override
    public void onDisable() {
        positions.clear();
    }

    @Subscribe
    private void onPostUpdate(EventUpdate.Post event) {
        if (!EntityHelper.getUser().hasMoved()) {
            return;
        }

        positions.add(new double[]{
                EntityHelper.getUser().posX, EntityHelper.getUser().posY + 0.5, EntityHelper.getUser().posZ,
        });
    }

    @Subscribe
    public void onWorldDraw(EventDraw.World event) {
        int count = positions.size();
        if (count < 2) {
            return;
        }

        baseHue = (baseHue + (0.5f * event.getPartialTicks())) % 360;
        float hue = baseHue;

        boolean noDepth = !testDepth.getValue();
        if (noDepth) {
            GlStateManager.disableDepth();
        }

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2f);

        ByteBuffer vbo = BufferUtils.createByteBuffer((4 + (4 * 3)) * count);
        for (double[] position : positions) {
            int color = Color.HSBtoRGB((hue % 360) / 360f, 1f, 1f);
            hue = (hue + 2f) % 360;

            int[] channels = VisualHelper.toRGBAIntArray(color, false);
            double x = position[0] - GameHelper.getGame().getRenderManager().viewerPosX;
            double y = position[1] - GameHelper.getGame().getRenderManager().viewerPosY;
            double z = position[2] - GameHelper.getGame().getRenderManager().viewerPosZ;

            // Color
            vbo
                    .put((byte) channels[0])
                    .put((byte) channels[1])
                    .put((byte) channels[2])
                    .put((byte) channels[3]);

            // Vertex
            vbo
                    .putFloat((float) x)
                    .putFloat((float) y)
                    .putFloat((float) z);
        }

        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        vbo.flip();
        GL11.glInterleavedArrays(GL11.GL_C4UB_V3F, 16, vbo);

        GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, count);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        if (noDepth) {
            GlStateManager.enableDepth();
        }
    }
}
