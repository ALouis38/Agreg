package org.news.agreg;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@Component(name = "DataFinderTwitterComponent")
@Provides(specifications = SearchInfoItf.class)
@Instantiate(name = "DataFinderTwitterComponentInstance")
public class DataFinder implements SearchInfoItf{

	@Requires(optional = false, id = "logger")
	private LogService log;
	@Property(name=SearchInfoItf.PROP_TYPE,value=SearchInfoItf.TYPE_DATAFINDER)
	private String type;
	public Map<URL, String> search(String searchquery) {

	    HashMap<URL, String> resultats= new HashMap<URL, String>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("5a9yPLyipqQqsfnUeUpPx87Bx")
		  .setOAuthConsumerSecret("D29CiRDM29ut0OjxH51yhmMLFumCTqNQMOKVvCRZjKTIycbJ18")
		  .setOAuthAccessToken("3224315956-O80DV3jHCpUikoIws4ASQndGtjzJHy4w5tglFpb")
		  .setOAuthAccessTokenSecret("FZJ0aLPIBTwBuayHhpBwE7jBpeK7iuBvABJMNFOMNXP5E");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		String s = new String("lemonde" );
	    twitter4j.Query query = new  twitter4j.Query(s);
	    
	    QueryResult result = null;
	    try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			e.printStackTrace();	
		}
	    for (Status status : result.getTweets()) {
	        String text = status.getText();
	        if(text.indexOf(searchquery)!=	-1){
	        	 try {
	        		 String url= "https://twitter.com/" + status.getUser().getScreenName() 
	        		     + "/status/" + status.getId();
	        		 
	        		 resultats.put(new URL(url) , status.getText());
	        		 System.out.println(url);
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    return resultats;
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
