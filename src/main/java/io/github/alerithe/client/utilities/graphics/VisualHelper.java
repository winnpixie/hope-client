package io.github.alerithe.client.utilities.graphics;

import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.graphics.drawing.GraphicsDevice;
import io.github.alerithe.client.utilities.graphics.drawing.MinecraftGraphicsDevice;
import io.github.alerithe.client.utilities.graphics.text.GLAWTTextRenderer;
import io.github.alerithe.client.utilities.graphics.text.MinecraftTextRenderer;
import io.github.alerithe.client.utilities.graphics.text.TextRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VisualHelper {
    // Optimizations for grabbing a ScaledResolution instance
    private static int previousWidth = -1;
    private static int previousHeight = -1;
    private static int previousScale = -1;
    private static ScaledResolution scaledResolution;

    // World to Screen projections
    private static final FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);
    private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);

    // View checking
    private static final Frustum frustum = new Frustum();

    // Fonts
    public static final TextRenderer MC_FONT = new MinecraftTextRenderer();
    public static final TextRenderer HELVETICA = new GLAWTTextRenderer(new Font("Helvetica", Font.PLAIN, 18));

    // Graphics Device
    public static final GraphicsDevice MC_GFX = new MinecraftGraphicsDevice();

    private VisualHelper() {
    }

    public static ScaledResolution getDisplay() {
        if (scaledResolution == null
                || previousWidth != Display.getWidth()
                || previousHeight != Display.getHeight()
                || previousScale != GameHelper.getGame().gameSettings.guiScale) {
            previousWidth = Display.getWidth();
            previousHeight = Display.getHeight();
            previousScale = GameHelper.getGame().gameSettings.guiScale;
            scaledResolution = new ScaledResolution(GameHelper.getGame());
        }

        return scaledResolution;
    }

    public static float[] project(float x, float y, float z) {
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        if (!GLU.gluProject(x, y, z, modelViewMatrix, projectionMatrix, viewport, windowPosition)) {
            return new float[0];
        }

        float scale = getDisplay().getScaleFactor();
        return new float[]{
                windowPosition.get(0) / scale,
                (Display.getHeight() - windowPosition.get(1)) / scale,
                windowPosition.get(2)
        };
    }

    public static boolean isInView(Entity entity) {
        return entity.ignoreFrustumCheck || isInView(entity.getEntityBoundingBox());
    }

    public static boolean isInView(AxisAlignedBB aabb) {
        Entity current = GameHelper.getGame().getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);

        return frustum.isBoundingBoxInFrustum(aabb);
    }

    public static int[] toRGBAIntArray(int argb, boolean hasAlpha) {
        return new int[]{
                (argb >> 16) & 255,                      // R
                (argb >> 8) & 255,                       // G
                argb & 255,                              // B
                !hasAlpha ? 255 : ((argb >> 24) & 255)   // A
        };
    }

    public static float[] toRGBAFloatArray(int argb, boolean hasAlpha) {
        int[] rgba = toRGBAIntArray(argb, hasAlpha);

        return new float[]{
                rgba[0] / 255f,
                rgba[1] / 255f,
                rgba[2] / 255f,
                rgba[3] / 255f,
        };
    }

    public static int toARGBInt(int red, int green, int blue) {
        return toARGBInt(red, green, blue, 255);
    }

    public static int toARGBInt(int red, int green, int blue, int alpha) {
        return (
                ((alpha & 255) << 24)
                        | ((red & 255) << 16)
                        | ((green & 255) << 8)
                        | (blue & 255)
        );
    }

    public static int toARGBInt(float red, float green, float blue) {
        return toARGBInt(red, green, blue, 1f);
    }

    public static int toARGBInt(float red, float green, float blue, float alpha) {
        return toARGBInt(
                (int) (red * 255f),
                (int) (green * 255f),
                (int) (blue * 255f),
                (int) (alpha * 255f)
        );
    }
}
