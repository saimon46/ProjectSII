import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@NamedQuery(name = "findAllUsers", query = "SELECT u FROM User u")
@Table(name="tbl_user")
public class User {	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Column(unique=true)
	private long idTwitter;
	
	@Column(nullable = false, unique=true)
	private String name;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Track> tracks;
	
	public User(){
		this.tracks = new ArrayList<Track>();
	}
	
	public User(String nome){
		this.name = nome;
		this.tracks = new ArrayList<Track>();
	}
	
	public boolean addTrack(Track track){
		if(!this.tracks.contains(track)){
			this.tracks.add(track);
			track.setUser(this);
			return true;
		}
		return false;
	}
	
	public Track hasTrack(String idSpotify){
		Track track = null;
		
		for(Track trackIterator:tracks)
			if(trackIterator.getIdSpotify().equals(idSpotify))
				track = trackIterator;
					
		return track;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getIdTwitter() {
		return idTwitter;
	}

	public void setIdTwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
}
