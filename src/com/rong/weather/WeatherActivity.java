package com.rong.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.rong.utils.WeatherSaxTools;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;

public class WeatherActivity extends Activity {
	private HashMap<String, String> hashMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weather);
		new SaxXMLAsync().execute();
	}

	class SaxXMLAsync extends AsyncTask<String, Void, String> {

		SAXParserFactory factory = null;
		SAXParser saxParser = null;
		XMLReader xmlReader = null;
		WeatherSaxTools tools = null;

		@Override
		protected void onPreExecute() {
			try {
				factory = SAXParserFactory.newInstance();
				saxParser = factory.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				hashMap=new HashMap<String, String>();
				tools = new WeatherSaxTools(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				xmlReader.setContentHandler(tools);
				xmlReader.parse(new InputSource(getResources().openRawResource(R.raw.citycode)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			String url="http://m.weather.com.cn/data/"+hashMap.get("台北")+".html";
		}

	}
}
