import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.StringTokenizer;

import twitter4j.URLEntity;
 
/**
 * @author Abhijit Ghosh
 * @version 1.0
 */
public class UrlUtility {	
    public static String expandUrl(String shortenedUrl) throws IOException {
        URL url = new URL(shortenedUrl);
        // open connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); 
        
        // stop following browser redirect
        httpURLConnection.setInstanceFollowRedirects(false);
         
        // extract location header containing the actual destination URL
        String expandedURL = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();
         
        return expandedURL;
    }
    
    public static boolean containsUrlSpotify(URLEntity[] urls) throws IOException {
    	boolean ctr = false;
    	
    	for(URLEntity url: urls){
    		String urlString = url.getExpandedURL();
    		
    		if(urlString.contains(".spotify.com/track")){
    			ctr = true;
    			break;
    		}
    			
    		if(urlString.contains("spoti.fi/")) {
				if(UrlUtility.expandUrl(urlString).contains(".spotify.com/track")){
					ctr = true;
					break;
				}
			}
    	}
    	return ctr;
    }
    
    public static String[] getIdsFromUrls(URLEntity[] urls) throws IOException{
    	String[] ids = new String[urls.length];
    	int i = 0;
    	
    	for(URLEntity url: urls){
    		String urlString = url.getExpandedURL();
    		
    		if(urlString.contains("spotify.com")){
    			ids[i] = getSingleIdFromUrl(urlString);
    			i++;
    		}
    			
    		if(urlString.contains("spoti.fi")) {
    			ids[i] = getSingleIdFromUrl(UrlUtility.expandUrl(urlString));
    			i++;
			}
    	}
    	if(ids.length == 0)
    		return null;
    	return ids;
    }
    
    private static String getSingleIdFromUrl(String url){
    	StringTokenizer urlStringToken = new StringTokenizer(url,"/");
		
		String token = "";
		
		while(urlStringToken.hasMoreTokens())
			token = urlStringToken.nextToken();
		return token;
    }
}