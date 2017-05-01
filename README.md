# wikiracer

### Usage

1. Checkout the source code into a directory e.g. "/Users/lei.guo/Dropbox/playground/wikiracer"
2. CD into the above directory where you checkout the code
3. Run the pre-built artifact by execute the command below
```$xslt
wikiracer-1.0-SNAPSHOT/bin/wikiracer -s "Mike Tyson" -f "Segment"
```
4. To see more usage, simply type
```$xslt
wikiracer-1.0-SNAPSHOT/bin/wikiracer -h
```


### Initial thoughts
* Since only how fast matters, I'm not going to load the whole graph and search from there
* Multi threads as much as possible so each thread can traverse a link itself

### First Path - Brutal Force
First path is simple, each thread take on a link and start to traverse without knowing others, whoever reach the finish first wins and terminate the program.

Sample output of first path:
```
[HOME]
Winner [ForkJoinPool-1-worker-28] took 34.572 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

Winner [ForkJoinPool-1-worker-8] took 39.751 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

Winner [ForkJoinPool-1-worker-19] took 35.305 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

[AIRPORT] - ALMOST TWICE SLOWER
Winner [pool-1-thread-13] took 57.465 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

Winner [pool-1-thread-4] took 63.035 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

Winner [pool-1-thread-4] took 59.843 seconds to traverse the path [Mike Tyson --> haystacks calhoun --> the acolytes/the apa --> Segment]

```

### Second Path - Divide and Conquer

Create parsing threads to make the http request and report the links back to main thread, main thread
maintain a data structure including the links reported back and keeping track of the ones has been visited.
The main thread will also search the result to decide if the end has been reached, if so, it'll print the path and exit, otherwise, it'll continue
After some examination of the data returning the first path, two things was observed:

Known Issues:
Path info is not kept

Note: Airport WIFI the time might not be as good as on a reliable internet connection
```
Starting at: Sun Apr 30 17:42:25 PDT 2017
Winner [pool-1-thread-1] across the finish line on Sun Apr 30 17:42:46 PDT 2017, took 21.238 seconds to traverse the path [Mike Tyson --> Segment]
```

* There're lots of duplicated links that each thread is try to traverse 
* Different URL pointing to same page causing something issues e.g. https://en.wikipedia.org/wiki/Mike_Tyson#cite_ref-127 and https://en.wikipedia.org/wiki/Mike_Tyson#cite_ref-103
* There're lots of wiki build in links, e.g. template/edit, etc. probably won't lead you to anywhere

### Third Path - Further Optimization (Don't have time )
* Optimize the search of the all the links collected, e.g. each thread is responsible for searching one part of the graph
* Store all the links collected somewhere external (not in memory) and have the program run from different machine to collect and search the end
* The search can be further modified to do breadth search first over depth because most of result can be found at around 3-4 depth so prioritize depth over breadth might help to speed up for most of cases
* Create separate thread pool for parsing and traverse and optimize it to the way one is not too far ahead of another, e.g. do not accumulate too many unvisisted links so the traverse can catch up
* Optimize number of threads for each thread pool so there's not too many pending tasks because number of threads are too small or too many un-used threads in the pool so it consume too much system resources


