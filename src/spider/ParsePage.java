/**
 * Kevin Smith
 * 11/17/2016
 * ParsePage.java
 * This class parses a page looking for links to add to a links queue and search terms to count and report.
 */
package spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class retrieves a line of text from the page queue and process it looking for links to add the the link queue
 * and search terms to count and report.
 */
public class ParsePage extends Thread
{
    private LinkedHashSet<String> searchWords;

    private Pages pageQueue;
    private Links linkQueue;

    private volatile boolean running = true;

    /**
     *
     * @param pageQueue Requires a Page object, representing a page queue.
     * @param linkQueue Requires a Link object, representing a link queue.
     * @param searchWords Requires a linked hash set, representing the searched terms.
     */
    public ParsePage(Pages pageQueue, Links linkQueue, LinkedHashSet<String> searchWords)
    {
        this.pageQueue = pageQueue;
        this.linkQueue = linkQueue;
        this.searchWords = searchWords;
    }

    /**
     * Terminates thread loop
     */
    public void terminate()
    {
        running = false;
    }

    /**
     * This method represents a thread operation, this operation includes retrieving a page from the page queue
     * and checking it for occurrences of links and search terms. This method will add found links to the link
     * queue and count search term occurrences and reports them to the queue. As well as reporting that it has
     * parsed a page.
     */
    @Override
    public void run()
    {
        while(running)
        {
            //get page from queue
            String stringPageThread = pageQueue.retrievePageFromQueue();

            //search for links
            Pattern pattern = Pattern.compile("<a.*?href=\"(http://www.*?)\"");
            Matcher matcher = pattern.matcher(stringPageThread);

            while (matcher.find())
            {
                String stringURL = matcher.group(1);

                //create an null instance if try fails.
                URL url = null;

                try
                {
                    url = new URL(stringURL);
                }
                catch (MalformedURLException e)
                {
                    pageQueue.setTotalFailedDownload();
                    System.out.println("Malformed URL Exception: " + e.getMessage());
                }

                if(!linkQueue.visitedURLContains(url))
                {
                    linkQueue.addLinkToQueue(url);
                }
            }

            for (String searchTerm : searchWords)
            {
                Pattern keyword = Pattern.compile("\\s" + searchTerm + "\\s");
                Matcher keywordMatcher = keyword.matcher(stringPageThread);

                int keyWordCount = 0;

                while (keywordMatcher.find())
                {
                    String foundKeyword = keywordMatcher.group(0);

                    if(foundKeyword.equals(" " + searchTerm + " "))
                    {
                        keyWordCount++;
                    }
                }

                pageQueue.setKeyWordCount(searchTerm, keyWordCount);
            }

            pageQueue.setTotalPagesParsed(1);

        }
    }
}
