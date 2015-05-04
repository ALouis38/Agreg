package org.news.agreg;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class Test {
	public static void main(String[] args) {
		DataFinder testImpl = new DataFinder();
		HashMap<URL, String> res = (HashMap<URL, String>)testImpl.search("princesse");
		System.out.println("Resultat : ");
		for(Entry<URL, String> s: res.entrySet()) {
			System.out.println(s.getKey()+"\n"+s.getValue()+"\n");
		}
	}
}
