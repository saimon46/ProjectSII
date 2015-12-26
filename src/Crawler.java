import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.SimpleArtist;

import twitter4j.TwitterException;

public class Crawler {

	public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException, WebApiException {
		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		//EntityManager em = emf.createEntityManager();
		
		//System.out.println("Creazione db completata!");
		TwitterCrawler crawler = new TwitterCrawler();
		crawler.run(); 

		// Create a request object for the type of request you want to make
		// Create an API instance. The default instance connects to https://api.spotify.com/.
		//Api spotify = Api.DEFAULT_API;

		// Create a request object for the type of request you want to make
		//com.wrapper.spotify.models.Track request = spotify.getTrack("1GLmaPfulP0BrfijohQpN5").build().get();
		//String array = request.getExternalUrls().get("spotify");
		
		//System.out.println(array);
	}
}