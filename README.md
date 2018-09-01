# web_crawler
Web crawler, developed with Java, starts with a keyword and seed URL.

This was a challenging application, it took a keyword and seed URL that a user entered via the Java console. The application would count each occurance of the keyword on that page and start a new thread for each new URL found, that in turn repeats the process. The application required both a consumer/producer pattern as well as a consideration of data corruption issues that can occur across multiple threads.
