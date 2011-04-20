package net.sprd.groovy.amazonas

import java.nio.channels.FileChannel
import java.nio.charset.Charset

class GeneralIO {
  public static copy(FileInputStream inputStream, File outputFile) throws IOException {
    FileOutputStream outputStream = null
    try {
      outputStream = new FileOutputStream(outputFile)
      copy(inputStream, outputStream)
    }
    finally {
      if (outputStream != null) {
        try {
          outputStream.close()
        } catch (IOException ex) {
        }
      }
    }
  }

  public static copy(File inputFile, FileOutputStream outputStream) throws IOException {
    FileInputStream inputStream = null
    try {
      inputStream = new FileInputStream(inputFile)
      copy(inputStream, outputStream)
    } catch (Exception ex) {
      ex.printStackTrace()
    }
    finally {
      if (inputStream != null) {
        try {
          inputStream.close()
        } catch (IOException ex) {
        }
      }
    }
  }

  public static copy(FileInputStream inputStream, FileOutputStream outputStream) throws IOException {

    FileChannel inChannel = null
    FileChannel outChannel = null

    try {
      inChannel = inputStream.getChannel();
      outChannel = outputStream.getChannel();

      inChannel.transferTo(0, inChannel.size(),
              outChannel);
    }
    finally {
      try {
        if (inChannel != null) inChannel.close();
        if (outChannel != null) outChannel.close();
      } catch (IOException ex) {
      }
    }
  }

  public static getExtension(String fileName) {
    fileName?.substring(fileName?.lastIndexOf('.') + 1)
  }

  public static getFileFilter(String extension) {
    new FileFilter() {
      boolean accept(File pathname) {
        return pathname.getName().endsWith(extension)
      }
    }
  }
}
