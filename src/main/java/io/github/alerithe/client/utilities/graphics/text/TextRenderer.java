package io.github.alerithe.client.utilities.graphics.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
        return wrapTokens((start, length) -> text.substring(start, start + length),
                text.length(), maxWidth);
    }

    default List<String> wrapStringPerWord(String text, float maxWidth) {
        String[] words = text.split(" ");

        return wrapTokens((start, length) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(words[start + i]).append(' ');
            }

            return builder.toString();
        }, words.length, maxWidth);
    }

    default List<String> wrapTokens(BiFunction<Integer, Integer, String> supplier, int count, float maxWidth) {
        List<String> wraps = new ArrayList<>();

        int start = 0;
        int length = count;

        while (count > 0) {
            String sub = supplier.apply(start, length);

            if (getStringWidth(sub) > maxWidth) {
                length--;

                // Force to avoid infinite loop
                if (length == 0) {
                    wraps.add(sub);

                    count--;
                    start++;
                    length = count;
                }
            } else {
                wraps.add(sub);

                count -= length;
                start += length;
                length = count;
            }
        }

        return wraps;
    }
}
