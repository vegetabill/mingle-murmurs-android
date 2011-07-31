package com.dephillipsdesign.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class IOUtils {

  private static final int BUFFER_SIZE = 1024 * 8;

  /**
   * Writes a stream into a file and then closes the stream
   * 
   * @param inputStream
   * @param output
   */
  public static void writeStream(InputStream inputStream, File output) {
    OutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(output);
      copyStream(inputStream, outputStream);
    } catch (FileNotFoundException e) {
      Log.e(IOUtils.class.getName(), "Unable to write inputStream to " + output);
    }
  }

  public static InputStream openInputStream(File file) {
    return openInputStream(file.getAbsoluteFile());
  }

  /**
   * Opens a new input stream on the file. Clients are responsible for closing
   * it.
   * 
   * @param filename
   * @return
   */
  public static InputStream openInputStream(String filename) {
    try {
      return new BufferedInputStream(new FileInputStream(filename), BUFFER_SIZE);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Copies all contents of one stream into another, then closes both
   * 
   * @param inputStream
   * @param outputStream
   */
  public static void copyStream(InputStream inputStream, OutputStream outputStream) {

    inputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
    outputStream = new BufferedOutputStream(outputStream, BUFFER_SIZE);

    long cumulativeBytes = 0;
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead = 0;

    try {
      while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
        cumulativeBytes += bytesRead;
      }
      Log.d(IOUtils.class.getName(), "Total bytes copied: " + cumulativeBytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        Log.e(IOUtils.class.getName(), "Unable to close InputStream", e);
      }
      try {
        outputStream.close();
      } catch (IOException e) {
        Log.e(IOUtils.class.getName(), "Unable to close OutputStream", e);
      }
    }

  }

}
