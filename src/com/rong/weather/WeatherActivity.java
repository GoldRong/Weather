package com.rong.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.rong.utils.WeatherSaxTools;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;

public class WeatherActivity extends Activity implements OnClickListener {
	private HashMap<String, String> hashMap;
	private Button layout_weather_b;
	private String url;
	private String jsonResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weather);
		layout_weather_b = (Button) findViewById(R.id.layout_weather_b);
		layout_weather_b.setOnClickListener(this);
		new SaxXMLAsync().execute();
	}

	@Override
	public void onClick(View v) {
		new GetWeatherJsonAsync().execute();
	}

	// 获取json
	class GetWeatherJsonAsync extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			StringBuilder sb = new StringBuilder();
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				} else {
					Log.e("error", "Failed to download file");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("json",""+result);
			jsonResult=result;
			try{
				JSONObject jsonObject=new JSONObject(result);
				JSONObject json=jsonObject.getJSONObject("weatherinfo");
				Log.i("city", json.getString("city"));
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	// 解析xml
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
				hashMap = new HashMap<String, String>();
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
			url = "http://m.weather.com.cn/data/" + hashMap.get("成都")+ ".html";
			Log.i("url", ""+url);
		}
	}
}
