package io.github.alerithe.client.utilities.graphics.text;

import java.util.ArrayList;
import java.util.List;

public interface TextRenderer {
    float getFontHeight();

    float getStringWidth(String text);

    default void drawString(String text, float x, float y, int color) {
        drawString(text, x, y, color, false);
    }

    default void drawStringWithShadow(String text, float x, float y, int color) {
        drawString(text, x, y, color, true);
    }

    void drawString(String text, float x, float y, int color, boolean shadow);

    default List<String> wrapStringPerCharacter(String text, float maxWidth) {
        List<String> wrapped = new ArrayList<>();

        int remaining = text.length();
        int start = 0;
        int length = text.length();

        while (remaining > 0) {
            String sub = text.substring(start, start + length);

            if (getStringWidth(sub) > maxWidth) {
                length--;

                // Force add to avoid infinite loop
                if (length == 0) {
                    wrapped.add(sub);

                    remaining--;
                    start++;
                    length = remaining;
                }
            } else {
                wrapped.add(sub);

                remaining -= sub.length();
                start += sub.length();
                length = remaining;
            }
        }

        return wrapped;
    }

    default List<String> wrapStringPerWord(String text, float maxWidth) {
        List<String> wrapped = new ArrayList<>();
        String[] words = text.split(" ");

        int remaining = words.length;
        int start = 0;
        int length = words.length;

        while (remaining > 0) {
            StringBuilder builder = new StringBuilder();

            int count = 0;
            for (int i = 0; i < length; i++) {
                builder.append(words[start + i]).append(' ');
                count++;
            }
            String sub = builder.toString();

            if (getStringWidth(sub) > maxWidth) {
                length--;

                // Force add to avoid infinite loop
                if (length == 0) {
                    wrapped.add(sub);

                    remaining--;
                    start++;
                    length = remaining;
                }
            } else {
                wrapped.add(sub);

                remaining -= count;
                start += count;
                length = remaining;
            }
        }

        return wrapped;
    }
}
