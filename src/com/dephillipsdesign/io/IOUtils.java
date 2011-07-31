package com.dephillipsdesign.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class IOUtils {

  private static final int BUFFER_SIZE = 1024 * 8;

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
      Log.d(IOUtils.class.getName(), "Total bytes written: " + cumulativeBytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
