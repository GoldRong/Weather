package com.rong.utils;

import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WeatherSaxTools extends DefaultHandler {
	private HashMap<String,String> hashMap;
	private boolean isAdd=false;
	private String d_id=null;
	
	public WeatherSaxTools(HashMap<String,String> hashMap){
		this.hashMap=hashMap;
	}
	
	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName=="d"){
			isAdd=true;
			d_id=attributes.getValue("d_id");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(isAdd){
			String str=new String(ch, start, length);
			hashMap.put(str,d_id);
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if(localName=="d"){
			isAdd=false;
			d_id=null;
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
	}

}
