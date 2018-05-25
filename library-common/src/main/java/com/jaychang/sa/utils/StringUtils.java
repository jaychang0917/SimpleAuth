package com.jaychang.sa.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

  private StringUtils() {
  }

  public static boolean isBlank(String text) {
    return text.trim().length() == 0;
  }

  public static boolean isEmpty(String text) {
    return text == null || text.length() == 0;
  }

  public static String reverse(String source) {
    return new StringBuilder(source).reverse().toString();
  }

  public static String replaceLast(String source, String pattern, String to) {
    int index = source.lastIndexOf(pattern);
    if (index >= 0) {
      source = new StringBuilder(source).replace(index, index + pattern.length(), to).toString();
    }

    return source;
  }

  public static List<Integer> indexesOf(String src, String target) {
    List<Integer> positions = new ArrayList<>();
    for (int index = src.indexOf(target);
         index >= 0;
         index = src.indexOf(target, index + 1)) {
      positions.add(index);
    }
    return positions;
  }

  public static String capitalize(final String str, final char... delimiters) {
    final int delimLen = delimiters == null ? -1 : delimiters.length;
    if (StringUtils.isEmpty(str) || delimLen == 0) {
      return str;
    }
    final char[] buffer = str.toCharArray();
    boolean capitalizeNext = true;
    for (int i = 0; i < buffer.length; i++) {
      final char ch = buffer[i];
      if (isDelimiter(ch, delimiters)) {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        buffer[i] = Character.toTitleCase(ch);
        capitalizeNext = false;
      }
    }
    return new String(buffer);
  }

  public static String capitalize(final String str) {
    return capitalize(str, null);
  }

  public static String capitalizeFully(String str, final char... delimiters) {
    final int delimLen = delimiters == null ? -1 : delimiters.length;
    if (StringUtils.isEmpty(str) || delimLen == 0) {
      return str;
    }
    str = str.toLowerCase();
    return capitalize(str, delimiters);
  }

  public static String capitalizeFully(String str) {
    return capitalizeFully(str, null);
  }

  public static String uncapitalize(final String str, final char... delimiters) {
    final int delimLen = delimiters == null ? -1 : delimiters.length;
    if (StringUtils.isEmpty(str) || delimLen == 0) {
      return str;
    }
    final char[] buffer = str.toCharArray();
    boolean uncapitalizeNext = true;
    for (int i = 0; i < buffer.length; i++) {
      final char ch = buffer[i];
      if (isDelimiter(ch, delimiters)) {
        uncapitalizeNext = true;
      } else if (uncapitalizeNext) {
        buffer[i] = Character.toLowerCase(ch);
        uncapitalizeNext = false;
      }
    }
    return new String(buffer);
  }

  public static String uncapitalize(final String str) {
    return uncapitalize(str, null);
  }

  private static boolean isDelimiter(final char ch, final char[] delimiters) {
    if (delimiters == null) {
      return Character.isWhitespace(ch);
    }
    for (final char delimiter : delimiters) {
      if (ch == delimiter) {
        return true;
      }
    }
    return false;
  }

  public static String withSuffix(long count) {
    if (count < 1000) return "" + count;
    int exp = (int) (Math.log(count) / Math.log(1000));
    return String.format("%.1f %c",
      count / Math.pow(1000, exp),
      "kMGTPE".charAt(exp-1));
  }

}
