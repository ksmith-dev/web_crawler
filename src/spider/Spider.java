/**
 * Kevin Smith
 * 11/17/2016
 * Spider.java
 * This class serves as the driver class, and manages console interactions. As well as interactions between other classes
 * such as Queue, FetchPage, ParsePage, Links, Pages.
 */
package spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class serves as the driver class, and manages console interactions. As well as interactions between other classes
 * such as Queue, FetchPage, ParsePage, Links, Pages.
 */
public class Spider
{
    private static String currentSearchTerm;
    private static int producerThreadCount = 0;
    private static int consumerThreadCount = 0;
    private static int consoleNumberOfConsumerThreads = 0;
    private static int consoleNumberOfProducerThreads = 0;
    private static LinkedHashSet<String> searchTerms = new LinkedHashSet<String>();


    private static Pages pageQueue = new Pages();
    private static Links linksQueue = new Links();

    /**
     *
     * @param args args args command-line arguments
     */
    public static void main(String[] args)
    {
        //open with menu
        printMenu();
    }

    private static void printMenu()
    {
        System.out.println("1.  Add seed URL");
        System.out.println("2.  Add consumer");
        System.out.println("3.  Add producer");
        System.out.println("4.  Add keyword search");
        System.out.println("5.  Print stats");

        //instantiate a scanner object
        Scanner console = new Scanner(System.in);

        try
        {
            //store scanner object
            int userInput = console.nextInt();

            switch (userInput)
            {
                case 1 : addSeedURL();
                    break;
                case 2 : addConsumerThreads();
                    break;
                case 3 : addProducerThreads();
                    break;
                case 4 : addKeywordSearch();
                    break;
                case 5 : printStats();
                    break;
                default :
                    System.out.println("Your input was not recognized, please try again");

                    printMenu();

                    break;
            }
        }
        catch(InputMismatchException e)
        {
            System.out.println("Input Mismatch Exception: " + e.getMessage());
        }
    }

    private static void addSeedURL()
    {
        if(linksQueue.getSeedURL() != null)
        {
            System.out.println("You have already entered a seed url.");
            System.out.println("Please try another menu option.");

            printMenu();
        }
        else
        {
            Scanner seedScan = new Scanner(System.in);

            System.out.println("Please enter a seed url: ");
            System.out.println("example(www.cnn.com)");

            try
            {
                String url = "http://" + seedScan.nextLine();

                try
                {
                    URL seed = new URL(url);

                    linksQueue.setSeed(seed);

                    linksQueue.addLinkToQueue(seed);
                }
                catch (MalformedURLException e)
                {
                    System.out.println("Malformed URL Exception: " + e.getMessage());
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println("Input Mismatch Exception: " + e.getMessage());
                printMenu();
            }
            printMenu();
        }
    }

    private static void addConsumerThreads()
    {
        if(linksQueue.getSeedURL() == null)
        {
            System.out.println("You must first addToQueue a seed URL");
            System.out.println("Please select the Add seed url option from the menu.");
            printMenu();
        }

        System.out.println("How many consumer treads would you like to start?");

        Scanner console = new Scanner(System.in);

        try
        {
            consoleNumberOfConsumerThreads = console.nextInt();
        }
        catch (InputMismatchException e)
        {
            System.out.println("Input Mismatch Exception: " + e.getMessage());
        }

        for(int i = 0; i< consoleNumberOfConsumerThreads; i++)
        {
            ParsePage parsePageThread = new ParsePage(pageQueue, linksQueue, searchTerms);

            consumerThreadCount++;

            parsePageThread.start();
        }

        printMenu();
    }

    private static void addProducerThreads()
    {
        if(linksQueue.getSeedURL() == null)
        {
            System.out.println("You must first addToQueue a seed URL");
            System.out.println("Please select the Add seed url option from the menu.");

            printMenu();
        }

        System.out.println("How many producer treads would you like to start?");

        Scanner console = new Scanner(System.in);

        try
        {
            consoleNumberOfProducerThreads = console.nextInt();
        }
        catch (InputMismatchException e)
        {
            System.out.println("Input Mismatch Exception: " + e.getMessage());
        }

        for(int i = 0; i< consoleNumberOfProducerThreads; i++)
        {
            FetchPage producerThread = new FetchPage(pageQueue, linksQueue);

            producerThreadCount++;

            producerThread.start();
        }

        printMenu();
    }

    private static void addKeywordSearch()
    {
        System.out.println("Please enter a search term: ");

        Scanner console = new Scanner(System.in);

        try
        {
            String userInput = console.nextLine();

            currentSearchTerm = userInput;

            searchTerms.add(currentSearchTerm);

            pageQueue.addNewSearchTerm(currentSearchTerm);

            printMenu();
        }
        catch (InputMismatchException e)
        {
            System.out.println("Input Mismatch Exception: " + e.getMessage());

            System.out.println("Please try again,");

            printMenu();
        }
    }

    private static void printStats()
    {
        if(pageQueue.getTotalPagesParsed() == 0 && linksQueue.getTotalLinksFetched() == 0)
        {
            System.out.println("");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Before you see accurate results from the print status menu option,");
            System.out.println("you must first add consumer and add producer threads using the menu option 2 and 3.");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("");

            printMenu();
        }
        else if (linksQueue.getTotalLinksFetched() == 0)
        {
            System.out.println("");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Before you see accurate results from the print status menu option,");
            System.out.println("you must first add a consumer thread using the menu option 2.");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("");

            printMenu();
        }
        else if(pageQueue.getTotalPagesParsed() == 0)
        {
            System.out.println("");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Before you see accurate results from the print status menu option,");
            System.out.println("you must first add a producer thread using the menu option 3.");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("");

            printMenu();
        }
        else
        {
            for (String search : searchTerms)
            {
                if(!pageQueue.hasThreadsSearching(search))
                {
                    System.out.println("");
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("Your search for (" + search + ") will not return results until you star up new threads.");
                    System.out.println("The result will continue to show as \"0\" until you start new threads");
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("");
                }
                System.out.println("Search term (" + search + ") was found [" + pageQueue.getKeyWordValue(search) + "] times.");
            }
            System.out.println("Total pages processed: " + pageQueue.getTotalPagesParsed());
            System.out.println("Failed downloads: " + pageQueue.getTotalFailedDownloads());
            System.out.println("Producers: " + producerThreadCount);
            System.out.println("Consumers: " + consumerThreadCount);
            System.out.println("Total in page queue: " + pageQueue.totalInQueue());
            System.out.println("Total in link queue: " + linksQueue.totalInQueue());

            int urlsVisitedCount = 0;

            LinkedHashSet urlFound = linksQueue.getUrlsVisited();
            for (Object urls : urlFound)
            {
                urlsVisitedCount++;
            }

            System.out.println("Number of URL's found: " + urlsVisitedCount);

            printMenu();
        }
    }
}
