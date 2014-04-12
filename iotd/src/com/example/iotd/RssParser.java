package com.example.iotd;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssParser extends DefaultHandler {
	private RssItem item;
	private List<RssItem> list = new ArrayList<RssItem>();
	private StringBuffer chars;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		//super.startElement(uri, localName, qName, attributes);
		if(localName.equals("item")){
			item = new RssItem();
			list.add(item);
		} else if (item != null) {
			if(localName.equalsIgnoreCase("enclosure")){
				item.setImagemUrl(attributes.getValue("url"));
			}
		}
		chars = new StringBuffer();
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		//super.characters(ch, start, length);
		chars.append(new String(ch,start,length));
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		//super.endElement(uri, localName, qName);
		if(item != null){
			if(localName.equalsIgnoreCase("item")){
				item = null;
			} else if (localName.equalsIgnoreCase("title")){
				item.setTitulo(chars.toString());
			} else if (localName.equalsIgnoreCase("description")){
				item.setDescricao(chars.toString());
			} else if (localName.equalsIgnoreCase("pubDate")){
				item.setDate(chars.toString());
			}
		}
		
	}
	
	public RssItem getFirstItem(){
		return list.get(0);
		
	}
	
	
}
