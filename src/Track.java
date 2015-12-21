import javax.persistence.*;

@Entity
@NamedQuery(name = "findAllTrack", query = "SELECT t FROM Track t")
@Table(name="tbl_track")
public class Track {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	private String idSpotify;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String author;
	
	@Column
	private String album;
	
	@Column
	private String url;
	
	public Track(){
	}
	
	public Track(String idSpotify, String name, String author, String album){
		this.idSpotify = idSpotify;
		this.name = name;
		this.author = author;
		this.album = album;
		this.url = "https://play.spotify.com/track/" + idSpotify;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdSpotify() {
		return idSpotify;
	}

	public void setIdSpotify(String idSpotify) {
		this.idSpotify = idSpotify;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
