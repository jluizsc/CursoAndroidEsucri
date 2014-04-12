package com.example.iotd;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class RssService extends AsyncTask<RssParser, Void, RssItem>{
	private MainActivity activity;
	private ProgressDialog progress;
	
	public RssService(MainActivity activity){
		this.activity = activity;
	}
	
	@Override
	protected RssItem doInBackground(RssParser... params) {
		// TODO Auto-generated method stub
		// return null;
		RssParser parser = params[0];
		RssItem item = null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(parser);
			//local
			//xr.parse(new InputSource(activity.getAssets().open("iotd.xml")));
			URL url = new URL("http://www.nasa.gov/rss/dyn/image_of_the_day.rss");
			xr.parse(new InputSource(url.openConnection().getInputStream()));
			
			
			item = parser.getFirstItem();
			if(item != null){
				//item.setImagem(BitmapFactory.decodeResource(activity.getResources(),R.drawable.iotd));
				item.setImagem(getBitmap(item.getImagemUrl()));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return item;
	}
	
	private Bitmap getBitmap(String imagemUrl){
		Bitmap bitmap = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(imagemUrl).openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
			input.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(RssItem result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		activity.displayData(result);
		progress.dismiss();
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//super.onPreExecute();
		progress = new ProgressDialog(activity);
		progress.setIndeterminate(true);
		progress.setMessage("Carregando Dados");
		progress.show();
	}
	
}
