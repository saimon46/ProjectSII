import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.SimpleArtist;

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
    Api spotify = Api.DEFAULT_API;
    
    void run() throws FileNotFoundException, IOException, TwitterException, InterruptedException, WebApiException {
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
	        for (Status status:result.getTweets()) {
	        	EntityTransaction tx = em.getTransaction();
	    		tx.begin();
	        	URLEntity[] urls = status.getURLEntities();
	        	
	        	ArrayList<String> ids = (ArrayList<String>) UrlUtility.getIdsFromUrls(urls);
	        	//***se restituisce NULL l'utente non ha pubblicato link Spotify e deve essere scartato***
	        	
	        	if(ids != null){
	        		//***estraggo dal database l'utente e se esiste non lo reinserisco ma lo utilizzo***
	        		javax.persistence.Query qUser = em.createQuery("SELECT u FROM User u WHERE u.name = :name");
	        		qUser.setParameter("name", status.getUser().getScreenName());
	        		List<User> users = (List<User>) qUser.getResultList();
	        		User user = null;
	        		
	        		String idString = "";
	        		
	        		if(users.isEmpty()){
	        			long userIdTwitter = status.getUser().getId();
	        			String userName = status.getUser().getScreenName();
	        			user = new User(userIdTwitter, userName);
	        			
	        			for(String id:ids){
	        				javax.persistence.Query qTrack = em.createQuery("SELECT t FROM Track t WHERE t.idSpotify = :id");
	    	        		qTrack.setParameter("id", id);
	    	        		List<Track> tracks = (List<Track>) qTrack.getResultList();
	    	        		Track track = null;
	        				
	        				if(tracks.isEmpty()){
	        					com.wrapper.spotify.models.Track trackSpotify = spotify.getTrack(id).build().get();
	        					String trackName = trackSpotify.getName();
	        					String trackAlbum = trackSpotify.getAlbum().getName();
	        					double trackPopularity = trackSpotify.getPopularity()/100.0;
	        					String trackAuthors = "";
	        					
	        					List<SimpleArtist> artistList = trackSpotify.getArtists();
	        					for(SimpleArtist artist:artistList)
	        						trackAuthors += artist.getName() + ", ";
	        					trackAuthors = trackAuthors.substring(0, trackAuthors.length()-2);
	        					
	        					track = new Track(id, trackName, trackAuthors, trackAlbum, trackPopularity);
	        				}else{
	        					System.out.println("-----------------------doppione------------------------");
	        					track = tracks.get(0);
	        				}
	        				
	        				Tweet tweet = new Tweet();
	        				tweet.setTrack(track);
	        				user.addTweet(tweet);
	        				em.persist(tweet);
	        				em.persist(track);
		        			
		        			idString += id + " - ";
		        		}
	        		}else{
	        			user = users.get(0);
	        			
	        			for(String id:ids){
	        				Tweet tweet = user.hasTrackInTweet(id);
	        				
	        				if(tweet != null){
	        					tweet.incrementCount();
	        				}else{
	        					javax.persistence.Query qTrack = em.createQuery("SELECT t FROM Track t WHERE t.idSpotify = :id");
		    	        		qTrack.setParameter("id", id);
		    	        		List<Track> tracks = (List<Track>) qTrack.getResultList();
		    	        		Track track = null;
		        				
		        				if(tracks.isEmpty()){
		        					com.wrapper.spotify.models.Track trackSpotify = spotify.getTrack(id).build().get();
		        					String trackName = trackSpotify.getName();
		        					String trackAlbum = trackSpotify.getAlbum().getName();
		        					double trackPopularity = trackSpotify.getPopularity()/100.0;
		        					String trackAuthors = "";
		        					
		        					List<SimpleArtist> artistList = trackSpotify.getArtists();
		        					for(SimpleArtist artist:artistList)
		        						trackAuthors += artist.getName() + ", ";
		        					trackAuthors = trackAuthors.substring(0, trackAuthors.length()-2);
		        					
		        					track = new Track(id, trackName, trackAuthors, trackAlbum, trackPopularity);
		        				}else{
		        					System.out.println("-----------------------doppione------------------------");
		        					track = tracks.get(0);
		        				}
		        				
		        				tweet = new Tweet();
		        				tweet.setTrack(track);
			        			user.addTweet(tweet);
			        			em.persist(tweet);
		        				em.persist(track);
	        				}
	        				
		        			idString += id + " - ";
		        		}
	        		}
	        		
	        		em.persist(user);
        		
		        	String str = i + "." + j + "..:" + status.getCreatedAt().toString() + " @" + status.getUser().getScreenName() + ":" + idString;
		        	
		        	out.write(str+"\n");
		        	out.flush();
		            System.out.println(str);
		            j++;
	        	}
	        	
	        	tx.commit();
	        }
	        i++;
	        Thread.sleep(20000);
        }
        
        out.close();
        em.close();
		emf.close();
    }
}
