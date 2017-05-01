package com.segment.wikiracer;

import com.segment.wikiracer.model.Link;
import com.segment.wikiracer.model.PageParser;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to traverse the links
 */
public class Racer {
    private String end;
    private ExecutorService parsingExecutor;
    private Date startTime;
    private boolean verbosity;

    Racer(String end, int parsingThreads, boolean verbosity) {
        this.end = end;
        this.parsingExecutor = Executors.newFixedThreadPool(parsingThreads);
        this.verbosity = verbosity;
        this.startTime = new Date();
        System.out.println("Starting at: " + startTime.toString());
    }

    void race(Link startLink) {

        parse(startLink);

        while (!parsingExecutor.isTerminated()) {

        }
        System.out.printf("Finish at %s but no path found", new Date().toString());

    }

    private void printVerboseMessage(String message) {
        if (verbosity) {
            System.out.println(message);
        }
    }

    private void parse(Link startLink) {

        PageParser parser = new PageParser(startLink);
        printVerboseMessage(String.format("[%s]: Parsing %s%n", Thread.currentThread().getName(), startLink.getURL()));
        try {
            Map<String, Link> links = parser.parseLinks();
            if (links.containsKey(end)) {
                StringBuilder sb = new StringBuilder();
                printPath(links.get(end), sb);
                System.out.printf("Winner [%s] across the finish line on %s, took %.3f seconds to traverse the path [%s]%n", Thread.currentThread().getName(), new Date().toString(), (float) (new Date().getTime() - startTime.getTime()) / 1000, sb.toString());
                System.exit(0);
            } else {
                for (Link link : links.values()) {
                    Runnable task = new ParseTask(link);
                    parsingExecutor.execute(task);
                }
            }
        } catch (Exception e) {
            printVerboseMessage(e.getLocalizedMessage());
        }
    }

    private void printPath(Link link, StringBuilder sb) {
        if (link.getParent() != null) {
            sb.insert(0, " --> " + link.getTitle());
            printPath(link.getParent(), sb);
        } else {
            sb.insert(0, link.getTitle());
        }
    }

    class ParseTask implements Runnable {

        private Link link;

        ParseTask(Link link) {
            this.link = link;
        }

        @Override
        public void run() {
            parse(link);
        }

    }
}

