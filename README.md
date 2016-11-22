# web_crawler
Web crawler, developed with Java, starts with a seed URL.

Program will create multiple threads that download html pages starting with a seed URL. Parsing the page looking for links that the program adds to a queue for processing by other parsing threads. This project consists of two queues, a link queue and a page gueue and two processing classes. The link processing class downloads the html from a given web page and adds the contents to a page queue for later processing by the page processing class. This class retrieves the page from the page queue looks for links and adds them to the link queue and also searches the page text for search terms provided by the user via the java console application. This program tracks all search terms as well as the number of occurances.

The program allows a user to start as many threads as they wish, to get a better concept of a producer / consumer relationship, from an application in action.
