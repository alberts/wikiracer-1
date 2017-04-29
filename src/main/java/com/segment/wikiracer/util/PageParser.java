package com.segment.wikiracer.util;

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

    private final boolean verbosity;
    private String start;

    //Take the URL and return a map with title as key and links list as the value
    public PageParser(String start, boolean verbosity) {
        this.start = start;
        this.verbosity = verbosity;
    }

    private Map<String, String> links = new HashMap<>();

    public Map<String, String> parseLinks() throws IOException {

        Document doc = Jsoup.connect(start).get();
        Elements hrefs = doc.select("a[href]");

        for (Element ele : hrefs) {
            String title = ele.text().trim().toLowerCase();
            String link = ele.attr("abs:href");
            //For whatever reason, there're a bunch missing the title meaning not clickable. weird
            if (!title.isEmpty() && !link.isEmpty() && link.startsWith("https://en.wikipedia.org/wiki/")) {
                links.put(title, link);
            }
        }

        if (verbosity) {
            for (String title : links.keySet()) {
                System.out.println(title + "[" + links.get(title) + "]");
            }
        }

        return links;
    }

}
