package com.segment.wikiracer.model;

/**
 * Representation class of a HREF link node on a link graph
 */
public class Link{
    private String title;
    private String url;
    private Link parent;

    public Link(String title, String url, Link link) {
        this.title = title;
        this.url = url;
        this.parent = link;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }

    public Link getParent() {
        return parent;
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
        return  31 * (url != null ? url.hashCode() : 0);
    }

}
