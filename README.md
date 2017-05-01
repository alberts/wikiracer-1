# wikiracer

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

