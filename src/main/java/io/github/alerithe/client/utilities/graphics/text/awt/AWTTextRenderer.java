package io.github.alerithe.client.utilities.graphics.text.awt;

import io.github.alerithe.client.utilities.graphics.text.TextRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class AWTTextRenderer implements TextRenderer {
    private static final int TEX_COLOR_BUFFER = GL15.glGenBuffers();
    private static final ByteBuffer VBO = ByteBuffer.allocateDirect(4 * 2 * 4) // V2F
            .order(ByteOrder.nativeOrder());

    private final Map<Integer, BakedImage> bakery = new WeakHashMap<>();

    private final Font font;
    private final Graphics2D context;

    static {
        ByteBuffer texColorBuffer = ByteBuffer.allocateDirect(((4 * 2) + 4) * 4) // T2F_C4UB
                .order(ByteOrder.nativeOrder());
        texColorBuffer
                .putFloat(0f).putFloat(0f)
                .put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255)
                .putFloat(0f).putFloat(1f)
                .put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255)
                .putFloat(1f).putFloat(1f)
                .put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255)
                .putFloat(1f).putFloat(0f)
                .put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255);
        texColorBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEX_COLOR_BUFFER);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texColorBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public AWTTextRenderer(Font font) {
        this.font = font;
        this.context = AWTHelper.TEMPLATE.createGraphics();

        context.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        context.setFont(font);
    }

    @Override
    public float getFontHeight() {
        return (float) font.getStringBounds(" ", context.getFontRenderContext()).getHeight() / 2f;
    }

    @Override
    public float getStringWidth(String text) {
        return (float) font.getStringBounds(stripControlCodes(text), context.getFontRenderContext()).getWidth() / 2f;
    }

    @Override
    public void drawString(String text, float x, float y, int baseColor, boolean hasShadow) {
        BakedImage baked = bakery.computeIfAbsent(Objects.hash(text, baseColor, hasShadow),
                k -> ImageBakery.bake(font, context, text, baseColor, hasShadow));

        // downscale = free AA :)
        float width = baked.width / 2f;
        float height = baked.height / 2f;

        VBO
                .putFloat(x).putFloat(y)
                .putFloat(x).putFloat(y + height)
                .putFloat(x + width).putFloat(y + height)
                .putFloat(x + width).putFloat(y);
        VBO.flip();

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEX_COLOR_BUFFER);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 12, 0L);

        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 12, 8L);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, VBO);
        VBO.clear();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.bindTexture(baked.textureId);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 4);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GlStateManager.disableBlend();
    }

    private static String stripControlCodes(String str) {
        int length = str.length();
        char[] stripped = new char[length];

        int len = 0;
        boolean ctrl = false;
        for (int i = 0; i < length; i++) {
            if (ctrl) {
                ctrl = false;
                continue;
            }

            char ch = str.charAt(i);
            if (ch == '\u00A7' && i + 1 < length) {
                char c = Character.toLowerCase(str.charAt(i + 1));
                if ((c >= 'a' && c <= 'f')
                        || (c >= '0' && c <= '9')
                        || (c >= 'k' && c <= 'o')
                        || c == 'r') {
                    ctrl = true;
                    continue;
                }
            }

            stripped[len++] = ch;
        }

        return new String(stripped, 0, len);
    }
}
