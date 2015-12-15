import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

class TwitterCrawler {
    
    final String CONSUMER_KEY = "zaFZcEP7i70wjxX8oJxZoGmJv";
    final String CONSUMER_KEY_SECRET = "Ee4DtLckHTHQebNhk0vNJCfyY33ucJo3JSZBHbJAFxB3bBioIJ";
    final String ACCESS_TOKEN = "4482711635-W4kukDswSRXCkBarjh8fz7wFW3ZoRvyf1ASml2F";
    final String ACCESS_TOKEN_SECRET = "k69lmJJCGIbs3QwA0opYYZDRUJLnzonXhr6YTmZRCVZK4";
    Status status;
    
    void run() throws FileNotFoundException, IOException, TwitterException, InterruptedException {
    	ConfigurationBuilder builder = new ConfigurationBuilder();
    	builder.setOAuthConsumerKey(CONSUMER_KEY);
    	builder.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
    	builder.setOAuthAccessToken(ACCESS_TOKEN);
    	builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
    	Configuration configuration = builder.build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        
        FileWriter fOutstream = new FileWriter("tweets.txt");
        BufferedWriter out = new BufferedWriter(fOutstream);

        Query query = new Query("spotify");
        query.setResultType(ResultType.recent);
        
        int i=1;
        
        while(true){
	        QueryResult result = twitter.search(query);
	        int j=1;
	        for (Status status : result.getTweets()) {
	        	URLEntity[] urls = status.getURLEntities();
	        	
	        	String urlsString = "";
	        	for(URLEntity url: urls){
	        		url.getURL();
	        		urlsString+= url.getExpandedURL() + " - ";
	        	}
	        	
	        	String str = i + "." + j + "..:" + status.getCreatedAt().toString() + " @" + status.getUser().getScreenName() + ":" + urlsString;
	        	
	        	fOutstream.write(str+"\n");
	            System.out.println(str);
	            j++;
	        }
	        i++;
	        Thread.sleep(20000);
        }
        
        //out.close();
    } 
}
