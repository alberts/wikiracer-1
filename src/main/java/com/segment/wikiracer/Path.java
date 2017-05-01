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

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.size() == 1 || i == nodes.size() - 1) {
                output.append(nodes.get(nodes.size() - 1));
            } else {
                output.append(nodes.get(i)).append(" --> ");
            }
        }
        return output.toString();
    }

}
