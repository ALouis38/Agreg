package org.news.agreg;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.log.LogService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




@Component(name = "DataFinderRSSComponent")
@Provides(specifications = SearchInfoItf.class)
@Instantiate(name = "DataFinderTwitterComponentInstance")
public class DataFinder implements SearchInfoItf{

	@Requires(optional = false, id = "logger")
	private LogService log;
	@Property(name=SearchInfoItf.PROP_TYPE,value=SearchInfoItf.TYPE_DATAFINDER)
	private String type;
	public Map<URL, String> search(String searchquery) {
		DocumentBuilder builder;
		String[] listeMotsCles = searchquery.split(" ");
		HashMap<URL, String> res = new HashMap<URL, String>();
		
		System.out.println("début recherche...");
		try {
			//parser le XML en objet
			URL url = new URL("file:///home/a/amorel/m1info/tagl/axfKhsqe");
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(url.openStream());
			System.out.println("parse doc...");
			
			NodeList nodesTitle;
			NodeList nodesDescription;
			NodeList nodesLink;
			
			//On récupère chaque noeud "title" du flux
			nodesTitle = doc.getElementsByTagName("title");
			
			//On récupère chaque noeud "description" du flux
			nodesDescription = doc.getElementsByTagName("description");
			
			//On récupère tous les noeuds "link" du flux
			nodesLink = doc.getElementsByTagName("link");
			System.out.println("recherche..");
			for (int i = 0; i < nodesTitle.getLength(); i++) {
				Node n = nodesTitle.item(i);
				String contenu = n.getFirstChild().getTextContent();
				
				Node n2 = nodesDescription.item(i);
				String contenu2 = n2.getFirstChild().getTextContent();
				
				for (int j = 0; j < listeMotsCles.length; j++) {
					if (contenu.contains(listeMotsCles[j])||contenu2.contains(listeMotsCles[j])) {
						String parsedLink = nodesLink.item(i).getFirstChild().getTextContent();
						URL link = new URL(parsedLink);
						res.put(link, "Titre : "+contenu+"\nDescription : "+contenu2);
						
						System.out.println("URL = "+link+"String = "+res.get(link));
					}
				}
				
				
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	   
	}
	
	 @Bind(id = "logger")
	 private void bindLogger(LogService log) {
	 }
	 @Unbind(id = "logger")
	 private void unbindLogger(LogService log) {
	 }
	 @Validate
	 public void validate() {
	 log.log(LogService.LOG_INFO, "SampleProviderComponent start");
	 }
	 @Invalidate
	 public void invalidate() {
	 log.log(LogService.LOG_INFO, "SampleProviderComponent stop");
	 }

}
