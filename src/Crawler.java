import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import twitter4j.TwitterException;

public class Crawler {

	public static void main(String[] args) throws FileNotFoundException, IOException, TwitterException, InterruptedException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("projectSII");
		EntityManager em = emf.createEntityManager();
		
		System.out.println("Creazione db completata!");
		//TwitterCrawler crawler = new TwitterCrawler();
		//crawler.run();
		
		/*User pippo = new User();
		pippo.setName("pippo2312");
		pippo.setIdTwitter(21);
		Track canzone = new Track();
		canzone.setAuthor("Antonacci");
		canzone.setIdSpotify("sdgisdghfgifuogfgs");
		canzone.setName("Piccolo");
		canzone.setUrl("rfsdfsd");
		pippo.addTrack(canzone);
		
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(pippo);
		em.persist(canzone);
		tx.commit();
		*/
		EntityTransaction tx1 = em.getTransaction();
		tx1.begin();
		
		User users = (User)em.createQuery("SELECT c FROM User c WHERE c.name = 'pippo'").getSingleResult();
		
		Track track = new Track();
		track.setAuthor("Antonacci ciccooooo");
		track.setIdSpotify("sdgisdghfgifuefsdfgogfgs");
		track.setName("Piccolo2323");
		track.setUrl("rfsdffdgdsgsdgssd");
		users.addTrack(track);
		
		System.out.println(users.getName() + users.getId());
		em.persist(track);
		
		tx1.commit();
		
		

		em.close();
		emf.close();
	}
}