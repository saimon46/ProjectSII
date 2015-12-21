import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
	
    final String CONSUMER_KEY = "WCADpVfbO6csxBLTlKK9SWTqW";
    final String CONSUMER_KEY_SECRET = "fjokyct4BQoUQYCDOEq1eyCZZ1Bqx5UTY7GacKn1kHYJfA0Vv7";
    final String ACCESS_TOKEN = "4482711635-W4kukDswSRXCkBarjh8fz7wFW3ZoRvyf1ASml2F";
    final String ACCESS_TOKEN_SECRET = "k69lmJJCGIbs3QwA0opYYZDRUJLnzonXhr6YTmZRCVZK4";
    
    void run() throws FileNotFoundException, IOException, TwitterException, InterruptedException {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		EntityManager em = emf.createEntityManager();

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
        boolean a = true;
        
        while(a){
	        QueryResult result = twitter.search(query);
	        int j=1;
	        for (Status status : result.getTweets()) {
	        	EntityTransaction tx = em.getTransaction();
	    		tx.begin();
	    		
	        	URLEntity[] urls = status.getURLEntities();
	        	
	        	if(UrlUtility.containsUrlSpotify(urls)){
	        		String[] ids = UrlUtility.getIdsFromUrls(urls);
	        		String idString = "";
	        		
	        		for(String id:ids){
	        			idString += id + " - ";
	        		}
	        		
	        		User user = new User();
	        		user.setIdTwitter(status.getUser().getId());
	        		user.setName(status.getUser().getScreenName());
	        		
		        	String str = i + "." + j + "..:" + status.getCreatedAt().toString() + " @" + status.getUser().getScreenName() + ":" + idString;
		        	
		        	out.write(str+"\n");
		        	out.flush();
		            System.out.println(str);
		            j++;
	        	}
	        }
	        i++;
	        Thread.sleep(20000);
        }
        
        out.close();
        em.close();
		emf.close();
    }
}
