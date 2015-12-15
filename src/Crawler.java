import java.io.FileNotFoundException;
import java.io.IOException;

import twitter4j.TwitterException;

public class Crawler {

	public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException {
		
		TwitterCrawler crawler = new TwitterCrawler();
		crawler.run();
		
	}

}
