/**
 * Kevin Smith
 * 11/17/2016
 * Queue.java
 * This class serves as a queue object, that manages threads pulling or adding to this queue. This class also reports
 * how many items are currently in the queue.
 */
package spider;

import java.util.LinkedList;

/**
 * @author Kevin Smith
 * @version 1.0
 * This class serves as a queue object, that manages threads pulling or adding to this queue. This class also reports
 * how many items are currently in the queue.
 */
public class Queue
{
    private final int QUEUE_MAX = 50000;
    protected LinkedList<String> queue;

    /**
     * Instantiates an empty linked list array that represents a queue
     */
    public Queue()
    {
        //instantiate a new linked list of strings to server as a queue
        queue = new LinkedList<String>();
    }

    /**
     *
     * @param data Receives a string and stored it on a linked list queue.
     */
    public void addToQueue(String data)
    {
        synchronized (queue)
        {
            //check to see if queue has reached it limit
            while (queue.size() >= QUEUE_MAX)
            {
                //try to set queue to wait status
                try
                {
                    queue.wait(); //set thread to waiting state
                }
                catch (InterruptedException e)
                {
                    System.out.println("Interrupted Exception: " + e.getMessage());
                }
            }

            queue.addFirst(data); //addToQueue data to first position in queue
            queue.notify(); //notify thread is ready to accept another entry
        }
    }

    /**
     *
     * @return Returns the last string on the linked list queue.
     */
    public String pullFromQueue()
    {
        synchronized (queue)
        {
            //loop while queue is not empty
            while (queue.size() == 0)
            {
                //try to set queue to wait status
                try
                {
                    queue.wait(); //set thread to waiting state
                }
                catch (InterruptedException e)
                {
                    System.out.println("Interrupted Exception: " + e.getMessage());
                }
            }

            String removed = queue.removeLast(); //remove from end of queue
            queue.notify(); //wake up a thread waiting...
            return removed; //remove from queue
        }
    }

    /**
     *
     * @return Returns an integer representing the number of elements in the queue.
     */
    public int totalInQueue()
    {
        return queue.size();
    }
}
