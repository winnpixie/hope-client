package io.github.alerithe.client.utilities.graphics.text;

import net.minecraft.client.Minecraft;

public class MinecraftTextRenderer implements TextRenderer {
    @Override
    public float getFontHeight() {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    @Override
    public float getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    @Override
    public void drawString(String text, float x, float y, int color, boolean shadow) {
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color, shadow);
    }
}
