package com.formobile.projectlivestream.utils;

import org.jsoup.nodes.Element;

public class JSoupUtils {

	public static String[] splitBrContents(Element element){
		String[] parts = element.html().split("<br />"); // Jsoup transforms <br> to <br />.
		
		return parts;
	}
	
}
