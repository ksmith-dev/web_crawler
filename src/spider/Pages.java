/**
 * Kevin Smith
 * 11/17/2016
 * Pages.java
 * This class stores the total number of failed downloads as well as the number of total pages parsed. This class also
 * stored a search term and search term count, as well as an Hash Map that holds a search term and a boolean representing
 * the fact that the search term is indeed being search by a thread or not.
 */
package spider;

import java.util.HashMap;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class stores the total number of failed downloads as well as the number of total pages parsed. This class also
 * stored a search term and search term count, as well as an Hash Map that holds a search term and a boolean representing
 * the fact that the search term is indeed being search by a thread or not.
 */
public class Pages extends Queue
{
    private int totalFailedDownloads = 0;
    private int totalPagesParsed = 0;

    //keyword search amounts
    private HashMap<String, Integer> searchWordMap = new HashMap<>();
    private HashMap<String, Boolean> threadsSearching = new HashMap<String, Boolean>();

    /**
     * Instantiates an empty page object.
     */
    public Pages()
    {

    }

    /**
     *
     * @param page Requires a string input, representing a full html page.
     */
    public void addPageToQueue(String page)
    {
        addToQueue(page);
    }

    /**
     *
     * @return Returns a string, representing a full html page.
     */
    public String retrievePageFromQueue()
    {
        return pullFromQueue();
    }

    /**
     * Method increments the number of failed downloads by 1
     */
    public void setTotalFailedDownload()
    {
        totalFailedDownloads++;
    }

    /**
     *
     * @return Returns an integer representing the number of failed downloads.
     */
    public int getTotalFailedDownloads()
    {
        return totalFailedDownloads;
    }

    /**
     *
     * @param searchTerm Requires a string, representing the search term from a thread.
     * @param keyWordCount Requires a integer representing a word count from a thread.
     */
    public void setKeyWordCount(String searchTerm, Integer keyWordCount)
    {
        synchronized (threadsSearching)
        {
            threadsSearching.put(searchTerm, true);
        }
        synchronized (searchWordMap)
        {
            if (!searchWordMap.containsKey(searchTerm))
            {
                searchWordMap.put(searchTerm, keyWordCount);
            }
            else
            {
                Integer currentKeyWordCount = searchWordMap.get(searchTerm);

                int intValueOfCurrentKeyWordCount = currentKeyWordCount.intValue();
                int intValueOfKeyWordCount = keyWordCount.intValue();

                Integer total = intValueOfCurrentKeyWordCount + intValueOfKeyWordCount;

                searchWordMap.put(searchTerm, total);
            }
        }
    }

    /**
     *
     * @param totalPagesParsed Requires an integer representing the number of total pages parsed.
     */
    public void setTotalPagesParsed(int totalPagesParsed)
    {
        this.totalPagesParsed += totalPagesParsed;
    }

    /**
     *
     * @return Returns an integer representing the total number of pages
     * that have been parsed by thread, that have reported to the queue.
     */
    public int getTotalPagesParsed()
    {
        return totalPagesParsed;
    }

    /**
     *
     * @param key Receives a string, representing the key, which represents a search term.
     * @return Returns the integer representing the number of times a word has been reported as searched.
     */
    public int getKeyWordValue(String key)
    {
        synchronized (searchWordMap)
        {
            if(!searchWordMap.containsKey(key))
            {
                return 0;
            }
            else
            {
                int value = this.searchWordMap.get(key);

                return value;
            }
        }
    }

    /**
     *
     * @param key Requires a string, representing a search term.
     * @return Returns a boolean, representing a condition of true if the search term has threads reporting they are
     * searching that term.
     */
    public boolean hasThreadsSearching(String key)
    {
        boolean value = threadsSearching.get(key);

        if(value)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @param key Requires a string, representing a search term you wish to add to the queue.
     */
    public void addNewSearchTerm(String key)
    {
        threadsSearching.put(key, false);
    }
}