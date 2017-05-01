package com.segment.wikiracer.model;

/**
 * Representation class of a HREF link
 */
public class Link {
    String title;
    String url;
    Link parent;

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
        int result = 31 * (url != null ? url.hashCode() : 0);
        return result;
    }
}
