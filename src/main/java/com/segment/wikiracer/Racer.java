package com.segment.wikiracer;

import com.segment.wikiracer.model.Link;
import com.segment.wikiracer.model.PageParser;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
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
    private Set<Link> visitedLinks = new ConcurrentSkipListSet<>();

    Racer(String end, int parsingThreads, boolean verbosity) {
        this.end = end;
        this.parsingExecutor = Executors.newFixedThreadPool(parsingThreads);
        this.verbosity = verbosity;
        this.startTime = new Date();
        System.out.println("Starting at: " + startTime.toString());
    }

    void race(Link startLink) {

        //Do the first path and get all the depth one links and start from there
        PageParser parser = new PageParser(startLink);
        printVerboseMessage(String.format("[%s]: Parsing %s%n", Thread.currentThread().getName(), startLink.getURL()));

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

        if(!visitedLinks.contains(startLink)){
            visitedLinks.add(startLink);
        }else{
            return;
        }
        if(startLink.getPath().size() > 4) {
            return;
        }
        PageParser parser = new PageParser(startLink);
        printVerboseMessage(String.format("[%s]: Parsing %s%n", Thread.currentThread().getName(), startLink.getURL()));
        try {
            Map<String, Link> links = parser.parseLinks();
            for(String title : links.keySet()){
                if(title.equalsIgnoreCase(end)){
                    found(links.get(title));
                }else{
                    Runnable task = new ParseTask(links.get(title));
                    parsingExecutor.execute(task);
                }
            }
        } catch (Exception e) {
            printVerboseMessage(e.getLocalizedMessage());
        }
    }

    private void found(Link endLink) {
        System.out.printf("Winner [%s] across the finish line on %s, took %.3f seconds to traverse the path %s%n", Thread.currentThread().getName(), new Date().toString(), (float) (new Date().getTime() - startTime.getTime()) / 1000, endLink.getPath());
        System.exit(0);
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

