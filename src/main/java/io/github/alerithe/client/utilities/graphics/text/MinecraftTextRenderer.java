package io.github.alerithe.client.utilities.graphics.text;

import io.github.alerithe.client.utilities.GameHelper;

public class MinecraftTextRenderer implements TextRenderer {
    @Override
    public float getFontHeight() {
        return GameHelper.getGame().fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public float getStringWidth(String text) {
        return GameHelper.getGame().fontRendererObj.getStringWidth(text);
    }

    @Override
    public void drawString(String text, float x, float y, int color, boolean shadow) {
        GameHelper.getGame().fontRendererObj.drawString(text, x, y, color, shadow);
    }
}
