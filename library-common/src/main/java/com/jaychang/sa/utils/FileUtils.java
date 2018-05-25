package com.jaychang.sa.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtils {

  private FileUtils() {
  }

  public static boolean exist(File file) {
    return file != null && file.exists();
  }

  public static boolean isFile(File file) {
    return exist(file) && file.isFile();
  }

  public static boolean isDirectory(File file) {
    return exist(file) && file.isDirectory();
  }

  public static boolean createFile(File file) {
    if (file == null) {
      return false;
    }

    try {
      return file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void writeStreamToFile(InputStream input, File outputFile) {
    OutputStream out = null;

    try {
      out = new FileOutputStream(outputFile);
      byte[] buf = new byte[1024];
      int len;
      while ((len = input.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void writeTextToFile(String text, File file) {
    try {
      FileWriter writer = new FileWriter(file, false);
      writer.write(text);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void appendTextToFile(String text, File file) {
    try {
      FileWriter writer = new FileWriter(file, true);
      writer.write(text);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean createDir(File dir) {
    return dir.mkdirs();
  }

  public static void copyFile(File src, File dst) {
    try {
      FileInputStream inStream = new FileInputStream(src);
      FileOutputStream outStream = new FileOutputStream(dst);
      FileChannel inChannel = inStream.getChannel();
      FileChannel outChannel = outStream.getChannel();
      inChannel.transferTo(0, inChannel.size(), outChannel);
      inStream.close();
      outStream.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static boolean rename(File file, String newName) {
    if (file == null || newName == null) {
      return false;
    }
    if (!file.exists()) {
      return false;
    }
    if (StringUtils.isBlank(newName)) {
      return false;
    }
    if (newName.equals(file.getName())) {
      return true;
    }

    File newFile = new File(file.getParent() + File.separator + newName);
    return !newFile.exists() && file.renameTo(newFile);
  }

  public static boolean deleteDir(File dir) {
    if (!exist(dir)) {
      return true;
    }
    if (!isDirectory(dir)) {
      return false;
    }

    File[] files = dir.listFiles();

    if (files == null || files.length <= 0) {
      return false;
    }

    for (File file : files) {
      if (file.isFile() && !deleteFile(file)) {
        return false;
      } else if (file.isDirectory() && !deleteDir(file)) {
        return false;
      }
    }

    return dir.delete();
  }

  public static boolean deleteFile(File file) {
    if (!exist(file)) {
      return true;
    }
    if (!isFile(file)) {
      return false;
    }

    return file.delete();
  }

  public static boolean delete(File fileOrDir) {
    if (isDirectory(fileOrDir)) {
      return deleteDir(fileOrDir);
    }

    return deleteFile(fileOrDir);
  }

  public static void closeSilently(Closeable... closeables) {
    if (closeables == null) {
      return;
    }

    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static String getFileName(File file) {
    return file.getName();
  }

  public static String getFileNameNoExtension(File file) {
    return file.getName().replaceFirst("[.][^.]+$", "");
  }

  public static String getFileExtension(File file) {
    String filePath = file.getPath();
    int lastPoi = filePath.lastIndexOf('.');
    int lastSep = filePath.lastIndexOf(File.separator);
    if (lastPoi == -1 || lastSep >= lastPoi) {
      return "";
    }
    return filePath.substring(lastPoi + 1);
  }

}