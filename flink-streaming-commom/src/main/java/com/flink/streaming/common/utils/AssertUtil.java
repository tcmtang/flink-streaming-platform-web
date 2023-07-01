package com.flink.streaming.common.utils;

/**
 * @author tcm
 * @Description:
 * @date 2023-02-08
 */
public class AssertUtil {

    public static void throwExpression(String message) {
        throw new RuntimeException(message);
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throwExpression(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throwExpression(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throwExpression(message);
        }
    }

    public static void hasText(String text, String message) {
        if (!hasText(text)) {
            throwExpression(message);
        }
    }

    private static boolean hasText(String text) {
        return text != null && !text.isEmpty() && text.length() > 0;
    }
}
