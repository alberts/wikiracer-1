package com.segment.wikiracer.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Representation class of a HREF link node on a link graph
 */
public class Link implements Comparable<Link>{
    private String title;
    private String url;
    private Queue<String> path = new ConcurrentLinkedQueue<>();

    public Link(String title, String url, Link link) {
        this.title = title;
        this.url = url;
        if(link != null) {
            path.addAll(link.getPath());
        }
        path.add(title);
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        return title != null ? title.equalsIgnoreCase(link.title) || url.equalsIgnoreCase(link.url) : link.title == null;
    }

    @Override
    public int hashCode() {
        return  31 * (title != null ? title.hashCode() : 0);
    }

    public Queue<String> getPath() {
        return path;
    }

    @Override
    public int compareTo(Link o) {
        return this.equals(o) ? 0 : -1;
    }
}
