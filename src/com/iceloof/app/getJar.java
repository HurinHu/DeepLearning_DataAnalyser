package com.iceloof.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class getJar {

	public static void main(String[] args) {
		get("https://mvnrepository.com/artifact/org.deeplearning4j");
		get("https://mvnrepository.com/artifact/org.deeplearning4j?p=2");
		get("https://mvnrepository.com/artifact/org.deeplearning4j?p=3");
		get("https://mvnrepository.com/artifact/org.deeplearning4j?p=4");
		get("https://mvnrepository.com/artifact/org.deeplearning4j?p=5");
       
	}
	
	public static void get(String url){
		File directory = new File("dl4j");
        if (! directory.exists()){
            directory.mkdir();
        }
		HttpClient client = HttpClientBuilder.create().build();
	    HttpClientContext context = HttpClientContext.create();
	    HttpGet request = new HttpGet(url);
      //System.out.println(url);
          try {
          HttpResponse response = client.execute(request, context);
          //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          StringBuffer result = new StringBuffer();
          String line = "";
          while ((line = rd.readLine()) != null) {
              result.append(line);
          }
          String rtn = result.toString();
          Document document = Jsoup.parseBodyFragment(rtn);
          Elements elements = document.getElementsByClass("im-subtitle");
          for(int i=0;i<elements.size();i++){
        	  getNext("https://mvnrepository.com/artifact/org.deeplearning4j/"+elements.get(i).getElementsByTag("a").html());
          }
          HttpClientUtils.closeQuietly(response);
      } catch (Exception ex) {
          //System.out.println("Exception"+symbol);
          //System.out.println(ex);
      } finally {
      	request.releaseConnection();
      }
	}
	
	public static void getNext(String url){
		HttpClient client = HttpClientBuilder.create().build();
	    HttpClientContext context = HttpClientContext.create();
	    HttpGet request = new HttpGet(url);
      //System.out.println(url);
          try {
          HttpResponse response = client.execute(request, context);
          //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          StringBuffer result = new StringBuffer();
          String line = "";
          while ((line = rd.readLine()) != null) {
              result.append(line);
          }
          String rtn = result.toString();
          Document document = Jsoup.parseBodyFragment(rtn);
          Elements elements = document.getElementsByTag("table").get(1).getElementsByTag("td");
          String link = "";
          if(elements.get(1).getElementsByTag("a").attr("href").equals("/repos/central")){
        	  link = "https://mvnrepository.com/artifact/org.deeplearning4j/"+elements.get(0).getElementsByTag("a").attr("href");
          }else{
        	  link = "https://mvnrepository.com/artifact/org.deeplearning4j/"+elements.get(1).getElementsByTag("a").attr("href");
          }
          mydownload(link);
          HttpClientUtils.closeQuietly(response);
      } catch (Exception ex) {
          //System.out.println("Exception"+symbol);
          //System.out.println(ex);
      } finally {
      	request.releaseConnection();
      }
	}
	
	public static void mydownload(String url){
		HttpClient client = HttpClientBuilder.create().build();
	    HttpClientContext context = HttpClientContext.create();
	    HttpGet request = new HttpGet(url);
      //System.out.println(url);
          try {
          HttpResponse response = client.execute(request, context);
          //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
          StringBuffer result = new StringBuffer();
          String line = "";
          while ((line = rd.readLine()) != null) {
              result.append(line);
          }
          String rtn = result.toString();
          Document document = Jsoup.parseBodyFragment(rtn);
          Elements elements = document.getElementsByTag("table").get(0).getElementsByTag("td");
          String link = elements.get(2).getElementsByTag("a").get(0).attr("href");
          if(link.endsWith(".jar")){
        	  System.out.println(link);
          }else{
        	  link = elements.get(2).getElementsByTag("a").get(1).attr("href");
        	  if(link.endsWith(".jar")){
            	  System.out.println(link);
              }else{
            	  link = elements.get(2).getElementsByTag("a").get(2).attr("href");
            	  if(link.endsWith(".jar")){
                	  System.out.println(link);
            	  }
              }
          }
          String[] name = link.split("/");
          saveUrl(name[name.length-1],link);
          HttpClientUtils.closeQuietly(response);
      } catch (Exception ex) {
          //System.out.println("Exception"+symbol);
          //System.out.println(ex);
      } finally {
      	request.releaseConnection();
      }
	}
	
	public static void saveUrl(final String filename, final String urlString)
	        throws MalformedURLException, IOException {
	    BufferedInputStream in = null;
	    FileOutputStream fout = null;
	    try {
	        in = new BufferedInputStream(new URL(urlString).openStream());
	        fout = new FileOutputStream("dl4j/"+filename);
	        System.out.println(filename);
	        final byte data[] = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	    } finally {
	        if (in != null) {
	            in.close();
	        }
	        if (fout != null) {
	            fout.close();
	        }
	    }
	}
}
