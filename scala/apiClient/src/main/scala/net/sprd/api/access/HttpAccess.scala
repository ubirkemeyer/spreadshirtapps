package net.sprd.api.access

import scala.collection._
import scala.xml.XML
import java.net._
import java.io._
import java.util.zip.GZIPInputStream
import java.security.MessageDigest
import _root_.net.sprd.api.ApiClient

object HttpAccess {
  
  val urlWithParameterRegex = """(http.+)([^\?]+)\?(.+)""".r
  
  val sha1Encoder = MessageDigest.getInstance("SHA-1")
  
  def get(url: String, params : Map[String,String] = Map.empty, apiKey: Boolean = false, sessionId: String = null) = {
    val paramUrl = createUrl(url, params, "GET", apiKey, sessionId)
    //connect with gzip
    val connection = new URL(paramUrl).openConnection()
    connection.setRequestProperty("Accept-Encoding", "gzip")
    connection.connect()
    connection.getHeaderField("Content-Encoding") match {
      case "gzip" => new GZIPInputStream(connection.getInputStream)
      case _ => connection.getInputStream
    }
  }
  
  def loadXml(url: String, params : Map[String,String] = Map.empty, apiKey: Boolean = false, sessionId: String = null) = {
    val stream = get(url,params,apiKey,sessionId)
    val xml = XML.load(stream)
    stream.close
    xml
  }
  
  def createLocation(url: String, content: String, apiKey: Boolean = false, apiKeyGet: Boolean = false, sessionId: String = null) = {
    val postResult = post(url, content, apiKey, sessionId)
    if (postResult._2 == 200 || postResult._2 == 201) {
      val stream = HttpAccess.get(postResult._1.getHeaderField("Location"), Map.empty, apiKeyGet, sessionId)
      val xml = XML.load(stream)
      stream.close
      xml
    }
    else {
      println(postResult._2)
      val connection = postResult._1
      val errorStream = connection.getHeaderField("Content-Encoding") match {
        case "gzip" => new GZIPInputStream(connection.getErrorStream)
        case _ => connection.getErrorStream
      }
      val errorReader = new BufferedReader(new InputStreamReader(errorStream))
      var line = errorReader.readLine
      while (line!=null) {
        println(line)
        line = errorReader.readLine
      }
      null
    }
  }
  
  def post(url: String, content: String, apiKey: Boolean = false, sessionId: String = null, method: String = "POST") = {
    val paramUrl = createUrl(url, Map.empty, method, apiKey, sessionId)
    //connect with gzip
    val connection = new URL(paramUrl).openConnection().asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("POST");

    //connection.setRequestProperty("Accept-Encoding", "gzip")
    connection.setDoOutput(true)
    connection.setUseCaches(false)
    connection.setDoInput(true)
    connection.setConnectTimeout(10000)
    val out =new OutputStreamWriter(connection.getOutputStream(),"iso-8859-1")
    out.write("""<?xml version="1.0" encoding="iso-8859-1" standalone="yes"?>""")
    out.write(content)
    out.flush()
    out.close()
    (connection, connection.getResponseCode(), ()=>
    connection.getHeaderField("Content-Encoding") match {
      case "gzip" => new GZIPInputStream(connection.getInputStream)
      case _ => connection.getInputStream
    })
  }
  
  def put(url: String, content: String, apiKey: Boolean = false, sessionId: String = null) = {
    val newUrl = url match {
      case urlWithParameterRegex(domain,link,params) => url+"&method=put"
      case _ => url+"?method=put"
    }
    post(newUrl, content, apiKey, sessionId,"PUT")
  }
  
  def urlParamsString(params:Map[String,String]) = 
    params map (a => a._1+"="+URLEncoder.encode(a._2)) reduceLeft (_+"&"+_)
  
  protected def createUrl(url: String, params : Map[String,String] = Map.empty, method: String, apiKey: Boolean = false, sessionId: String = null) = {
    //extend apiKey params, if necessary
    var newParams = getUrlParams(url, params, method, apiKey, sessionId)
    //create url with params
    val newUrl = if (newParams.size > 0) {
      val getParams = urlParamsString(newParams)
      url match {
        case urlWithParameterRegex(domain,link,params) => url+"&"+getParams
        case _ => url+"?"+getParams
      }
    }
    else url
    println(newUrl)
    newUrl
  }
  
  protected def getUrlParams(url: String, params : Map[String,String] = Map.empty, method: String, apiKey: Boolean = false, sessionId: String = null) = {
    //extend apiKey params, if necessary
    var newParams = 
      if (apiKey) {
        getApiKeyParams(method,url) ++ params
      }
      else
        params;
    if (sessionId != null) {
      newParams = newParams + ("sessionId" -> sessionId)
    }
    newParams
  }
  
  protected def getApiKeyParams(method:String, url:String) : Map[String,String] = {
    val (rawUrl, givenParam) = 
      url match {
        case urlWithParameterRegex(domain,link,params) => (domain+link, params)
        case _ => (url, null)
      }
    val time = (System.currentTimeMillis-60000).toString
    val apiKey = ApiClient.apiKey
    val sig = encodeSha1(method + " "+rawUrl+" "+time+" "+ApiClient.secret)
    return Map(("apiKey" -> apiKey), ("sig" -> sig), ("time" -> time))
  }
  
  def encodeSha1(str:String) = {
    val mySha1Encoder = sha1Encoder.clone.asInstanceOf[MessageDigest]
    mySha1Encoder.update(str.getBytes)
    val bytes = mySha1Encoder.digest
    convertToHex(bytes)
  }
  
  protected def convertToHex(bytes:Array[Byte]) = 
    bytes map (a =>if (a<0) (a+256) & 0xff  else a.toInt & 0xff) map (a=> if (a<16) "0"+ Integer.toHexString(a) else Integer.toHexString(a)) reduceLeft (_+_)

}
