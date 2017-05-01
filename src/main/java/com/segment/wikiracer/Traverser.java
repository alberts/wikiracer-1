package com.segment.wikiracer;

import com.segment.wikiracer.util.LinkHelper;
import com.segment.wikiracer.util.PageParser;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Class to traverse the links
 */
public class Traverser {
    private String end;
    private String domain;
    private ExecutorService executor;
    private boolean verbosity;
    private Date startTime;

    Traverser(String end, String domain, Date startTime, ExecutorService executor, boolean verbosity) {
        this.end = end;
        this.domain = domain;
        this.executor = executor;
        this.verbosity = verbosity;
        this.startTime = startTime;
        System.out.println("Starting at: " + startTime.toString());
    }

    void traverse(String startLink, String start, Path path) {
        path.push(start);

        PageParser parser = new PageParser(startLink, verbosity);
        try {
            Map<String, String> links = parser.parseLinks();
            if (links.keySet().contains(LinkHelper.normalize(end))) {
                path.push(end);
                System.out.printf("Winner [%s] across the finish line on %s, took %.3f seconds to traverse the path [%s]%n", Thread.currentThread().getName(), new Date().toString(), (float) (new Date().getTime() - startTime.getTime()) / 1000, path.toString());
                System.exit(0);
            }
            for (String title : links.keySet()) {
                Runnable task = new TraverseTask(title, links.get(title), new Path(path.getNodes()));
                executor.execute(task);

            }
        } catch (Exception e) {
            if (verbosity){
                e.printStackTrace();
                System.out.println(e.getStackTrace());
            }
        }
    }

    class TraverseTask implements Runnable {

        String title;
        String link;
        Path path;

        TraverseTask(String title, String link, Path path) {
            this.link = link;
            this.path = path;
            this.title = title;
        }

        @Override
        public void run() {
            traverse(link, title, path);
        }

    }

}

