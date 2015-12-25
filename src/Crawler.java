import java.io.FileNotFoundException;
import java.io.IOException;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;

import twitter4j.TwitterException;

public class Crawler {

	public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException, WebApiException {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		//EntityManager em = emf.createEntityManager();
		
		//System.out.println("Creazione db completata!");
		//TwitterCrawler crawler = new TwitterCrawler();
		//crawler.run();
		
		Api api = Api.DEFAULT_API; 

		// Create a request object for the type of request you want to make
		com.wrapper.spotify.models.Track track = api.getTrack("0lSS0Sg2yX9oCYdTSNvju7").build().get();
		System.out.println(track.getPopularity());
	}
}