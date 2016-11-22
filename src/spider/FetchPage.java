/**
 * Kevin Smith
 * 11/17/2016
 * FetchPage.java
 * This class fetches pages from the web, and processes the html into a single line of text. The page is located
 * via a link provided by the link queue.
 */
package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class pulls links from the link queue and downloads the html text and parses it into a single line of text
 * and places that text onto the page queue for later processing.
 */
public class FetchPage extends Thread
{
    private Pages pageQueue;
    private Links linkQueue;

    //thread running loop conditional
    private volatile boolean running = true;

    /**
     *
     * @param pageQueue Requires a Page object representing the page queue.
     * @param linkQueue Requires a Link object representing the list queue.
     */
    public FetchPage(Pages pageQueue, Links linkQueue)
    {
        this.pageQueue = pageQueue;
        this.linkQueue = linkQueue;
    }

    /**
     * Terminates thread loop
     */
    public void terminate()
    {
        running = false;
    }

    /**
     * This method represents a thread operation, the operation of this thread includes retrieving a link from the
     * link queue. Downloading the html text of a given html page located at a given url provided by the link queue.
     * parses this html text line by line into a single line of text and reports if a link was not downloaded. This
     * method also reports that it has processed a link.
     */
    @Override
    public void run()
    {
        while (running)
        {
            URL url = linkQueue.retrieveLinkFromQueue();

            try
            {
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                BufferedReader download = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                if(download != null)
                {
                    String input_line;

                    String page = "";

                    while((input_line = download.readLine()) != null)
                    {
                        page += input_line;
                    }

                    download.close();

                    pageQueue.addPageToQueue(page);
                }
                else
                {
                    pageQueue.setTotalFailedDownload();
                }

                linkQueue.setTotalLinksFetched(1);
            }
            catch (UnknownHostException e)
            {
                pageQueue.setTotalFailedDownload();
                System.out.println("Unknown Host Exception: " + e.getMessage());
            }
            catch (IOException e)
            {
                pageQueue.setTotalFailedDownload();
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
