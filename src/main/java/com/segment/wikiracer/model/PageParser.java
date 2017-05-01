package com.segment.wikiracer.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This will parse the HTML from the response and find all the links in there
 */
public class PageParser {

    private Link start;

    //Take the URL and return a map with title as key and links list as the value
    public PageParser(Link start) {
        this.start = start;
    }

    private Map<String, Link> links = new HashMap<>();

    public Map<String, Link> parseLinks() throws IOException {
        //Decode before connect
        Document doc = Jsoup.connect(start.getURL()).get();
        Elements hrefs = doc.select("a[href]");

        for (Element ele : hrefs) {
            String title = ele.text().trim().toLowerCase();
            String url = ele.attr("abs:href");

            //For whatever reason, there're a bunch missing the title meaning not clickable and exclude the href referencing itself using "#"
            if (!title.isEmpty() && !url.isEmpty() && url.startsWith("https://en.wikipedia.org/wiki/") && !url.contains("#") && !url.substring(8, url.length()).contains(":")) {
                links.put(title, new Link(title, url, start));
            }
        }

        return links;
    }

}
