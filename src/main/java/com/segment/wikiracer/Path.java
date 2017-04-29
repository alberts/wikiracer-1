package com.segment.wikiracer;

import java.util.LinkedList;
import java.util.List;

/**
 * Data structure to represent a list of nodes in order to traverse from start to finish
 */
public class Path {

    private List<String> nodes = new LinkedList<>();

    Path() {
    }

    Path(List<String> nodes) {
        this.nodes.addAll(nodes);
    }

    void push(String item) {
        nodes.add(item);
    }

    List<String> getNodes() {
        return nodes;
    }

    protected void print() {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.size() == 1 || i == nodes.size() - 1) {
                System.out.println(nodes.get(nodes.size() - 1));
            } else {
                System.out.print(nodes.get(i) + " --> ");
            }
        }
    }

}
