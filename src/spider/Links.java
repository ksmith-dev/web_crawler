/**
 * Kevin Smith
 * 11/17/2016
 * Links.java
 * This class stores string representations of url's that have been visited. This class also stored a number for total
 * links fetched by threads. This class also stores a URL object representing the seed URL.
 */
package spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class stores string representations of url's that have been visited. This class also stored a number for total
 * links fetched by threads. This class also stores a URL object representing the seed URL.
 */
public class Links extends Queue
{
    private int totalLinksFetched = 0;
    private LinkedHashSet<URL> urlsVisited;
    private URL seed;

    /**
     * Instantiates a new linked hash set representing a list for visited urls.
     */
    public Links()
    {
        urlsVisited = new LinkedHashSet<URL>();
    }

    /**
     *
     * @param link Requires a URL object, representing a url to add to this link queue.
     */
    public void addLinkToQueue(URL link)
    {
        //also ass the url to LinkedHashSet urlsVisited
        urlsVisited.add(link);

        String stringURL = link.toString();

        addToQueue(stringURL);
    }

    /**
     *
     * @return Returns a URL object, from this links queue.
     */
    public URL retrieveLinkFromQueue()
    {
        try
        {
            String stringURL = pullFromQueue();

            URL link = new URL(stringURL);

            return link;
        }
        catch (MalformedURLException e)
        {
            System.out.println("Malformed URL Exception: " + e.getMessage());

            return null;
        }
    }

    /**
     *
     * @param url Requires a URL object, to check the visited URL array.
     * @return Returns a boolean, representing the existence of the URL inside the linked hash set.
     */
    public boolean visitedURLContains(URL url)
    {
        return urlsVisited.contains(url);
    }

    /**
     *
     * @return Returns a URL object, representing the see URL.
     */
    public URL getSeedURL()
    {
        return seed;
    }

    /**
     *
     * @param seed Requires a URL object, representing the seed URL.
     */
    public void setSeed(URL seed)
    {
        this.seed = seed;
    }

    /**
     *
     * @return Returns a LinkedHashSet<URL> object representing the urls visited.
     */
    public LinkedHashSet<URL> getUrlsVisited()
    {
        return urlsVisited;
    }

    /**
     *
     * @param totalLinksFetched Requires an integer, representing the total number of links fetched by a particular
     * thread and add this reported total to this queue total.
     */
    public void setTotalLinksFetched(int totalLinksFetched)
    {
        this.totalLinksFetched += totalLinksFetched;
    }

    /**
     *
     * @return Returns an integer, representing the total number of links fetched by threads.
     */
    public int getTotalLinksFetched()
    {
        return totalLinksFetched;
    }
}
