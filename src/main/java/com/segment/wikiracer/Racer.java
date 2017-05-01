package com.segment.wikiracer;

import com.segment.wikiracer.model.Link;
import com.segment.wikiracer.model.PageParser;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Class to traverse the links
 */
public class Racer {
    private String end;
    private ExecutorService parsingExecutor;
    private ExecutorService traverseExecutor;
    private Date startTime;
    private boolean verbosity;
    private Map<Link, Boolean> allLinks = new ConcurrentHashMap<>();

    Racer(String end, int parsingThreads, boolean verbosity) {
        this.end = end;
        this.parsingExecutor = Executors.newFixedThreadPool(parsingThreads);
        this.traverseExecutor = Executors.newFixedThreadPool(1);
        this.verbosity = verbosity;
        this.startTime = new Date();
        System.out.println("Starting at: " + startTime.toString());
    }

    void race(Link startLink) {
        PageParser parser = new PageParser(startLink);
        printVerboseMessage(String.format("[%s]: Parsing %s%n", Thread.currentThread().getName(), startLink.getURL()));

        //Mark current link as visited
        allLinks.put(startLink, true);
        try {
            for (Link link : parser.parseLinks()) {
                if (!allLinks.containsKey(link)) {
                    allLinks.put(link, false);
                }
            }
        } catch (IOException e) {
            printVerboseMessage(e.getLocalizedMessage());
        }

        Runnable traverseTask = new TraverseTask();
        traverseExecutor.execute(traverseTask);

        while (allLinks.containsValue(false)) {
            for (Link link : allLinks.keySet()) {
                //Parse the link and fill up the not visited links for searching
                if (!allLinks.get(link)) {
                    ParsingTask parseTask = new ParsingTask(link);
                    parsingExecutor.execute(parseTask);
                }
            }
        }

        while (!parsingExecutor.isTerminated() || !traverseExecutor.isTerminated()) {

        }
        System.out.printf("Finish at %s but no path found", new Date().toString());

    }

    private void printVerboseMessage(String message) {
        if (verbosity) {
            System.out.println(message);
        }
    }

    //Responsible for traverse the graph to see if the finish has returned yet
    class TraverseTask implements Runnable {

        @Override
        public void run() {

            while (true) {
                for (Link link : allLinks.keySet()) {
                    if (link.getTitle().equalsIgnoreCase(end)) {
                        StringBuilder sb = new StringBuilder();
                        printPath(link, sb);
                        System.out.printf("Winner [%s] across the finish line on %s, took %.3f seconds to traverse the path [%s]", Thread.currentThread().getName(), new Date().toString(), (float) (new Date().getTime() - startTime.getTime()) / 1000, sb.toString());
                        System.exit(0);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    printVerboseMessage(e.getLocalizedMessage());

                }
            }
        }

        private void printPath(Link link, StringBuilder sb) {
            if (link.getParent() != null) {
                sb.insert(0, " --> " + link.getTitle());
                printPath(link.getParent(), sb);
            } else {
                sb.insert(0,  link.getTitle());
            }
        }

    }

    //Responsible for parsing the http response and report back all the links
    class ParsingTask implements Runnable {

        private final Link link;

        ParsingTask(Link link) {
            this.link = new Link(link.getTitle(), link.getURL(), link.getParent());
        }

        @Override
        public void run() {
            PageParser parser = new PageParser(link);
            printVerboseMessage(String.format("[%s]: Parsing %s%n", Thread.currentThread().getName(), link.getURL()));

            //Mark current link as visited
            printVerboseMessage(String.valueOf(allLinks.size()));
            allLinks.put(link, true);
            try {
                for (Link link : parser.parseLinks()) {
                    if (!allLinks.containsKey(link)) {
                        allLinks.put(link, false);
                    }
                }
            } catch (RejectedExecutionException ex) {
                if (!parsingExecutor.isShutdown() && !parsingExecutor.isTerminated()) {
                    System.out.println(ex.getLocalizedMessage());
                }
            } catch (IOException e) {
               printVerboseMessage(e.getLocalizedMessage());
            }

        }

    }


}

