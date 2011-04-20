package net.sprd.groovy.amazonas

class AmazonasConfig {
  public static final String KEY_EXPORT_ENCODING = 'ExportEncoding'
  public static final String KEY_AMAZON_FILE_HEADER = 'AmazonFileHeader'
  public static final String KEY_API_LIMIT = 'APILimit'
  public static final String KEY_MAX_FILESIZE_BYTES = 'MaxFilesizeBytes'
  public static final String ENCODING_UTF8 = 'UTF-8'
  public static final String ENCODING_ISO = 'ISO-8859-1'

  String configurationKey
  String configurationValue

  static constraints = {
    configurationKey(blank: false, unique: true)
  }

  public String toString() {
    configurationValue
  }
}
