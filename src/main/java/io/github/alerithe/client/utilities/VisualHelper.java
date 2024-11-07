package io.github.alerithe.client.utilities;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VisualHelper {
    // Optimizations for grabbing a ScaledResolution instance
    private static int previousWidth = -1;
    private static int previousHeight = -1;
    private static int previousScale = -1;
    private static ScaledResolution scaledResolution;

    // World to Screen projections
    private final static FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
    private final static FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);
    private final static IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private final static FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);

    // View checking
    private final static Frustum frustum = new Frustum();

    public static ScaledResolution getDisplay() {
        if (scaledResolution == null || previousWidth != Display.getWidth() || previousHeight != Display.getHeight()
                || previousScale != Wrapper.getSettings().guiScale) {
            previousWidth = Display.getWidth();
            previousHeight = Display.getHeight();
            previousScale = Wrapper.getSettings().guiScale;
            scaledResolution = new ScaledResolution(Wrapper.getGame());
        }
        return scaledResolution;
    }

    public static float[] project(float x, float y, float z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        if (GLU.gluProject(x, y, z, modelViewMatrix, projectionMatrix, viewport, windowPosition)) {
            float scale = getDisplay().getScaleFactor();

            return new float[]{
                    windowPosition.get(0) / scale,
                    (Display.getHeight() - windowPosition.get(1)) / scale,
                    windowPosition.get(2)
            };
        }
        return null;
    }

    public static boolean isInView(Entity entity) {
        return entity.ignoreFrustumCheck || isInView(entity.getEntityBoundingBox());
    }

    public static boolean isInView(AxisAlignedBB aabb) {
        Entity current = Wrapper.getGame().getRenderViewEntity();

        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(aabb);
    }

    // Gui.drawRect
    public static void drawRect(float x, float y, float right, float bottom, int color) {
        if (x < right) {
            float swp = x;
            x = right;
            right = swp;
        }

        if (y < bottom) {
            float swp = y;
            y = bottom;
            bottom = swp;
        }

        float a = (float) (color >> 24 & 255) / 255F;
        float r = (float) (color >> 16 & 255) / 255F;
        float g = (float) (color >> 8 & 255) / 255F;
        float b = (float) (color & 255) / 255F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRectSized(float x, float y, float width, float height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    public static void drawBorderedRect(float x, float y, float right, float bottom, float lineWidth, int color, int borderColor) {
        drawRect(x, y, right, bottom, color);

        drawRect(x - lineWidth, y - lineWidth, x, bottom + lineWidth, borderColor);
        drawRect(x, y - lineWidth, right, y, borderColor);
        drawRect(right, y - lineWidth, right + lineWidth, bottom + lineWidth, borderColor);
        drawRect(x, bottom, right, bottom + lineWidth, borderColor);
    }

    public static int[] toARGBIntArray(int argb, int alpha) {
        return new int[]{
                (argb >> 16) & 255,
                (argb >> 8) & 255,
                (argb) & 255,
                (alpha == -255 ? 255 : ((argb >> 24) & 255))
        };
    }

    public static float[] toARGBFloatArray(int argb, float alpha) {
        return new float[]{
                ((argb >> 16) & 255) / 255f,
                ((argb >> 8) & 255) / 255f,
                ((argb) & 255) / 255f,
                (alpha == -1f ? 1f : ((argb >> 24) & 255) / 255f),
        };
    }
}
