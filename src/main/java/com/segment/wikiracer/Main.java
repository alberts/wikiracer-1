package com.segment.wikiracer;

import com.segment.wikiracer.model.Link;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static String domain;
    private static boolean verbosity = false;
    private static final int DEFAULT_NUM_THREADS = 30;
    private static Map<Argument, String> argsMap = new HashMap<>();

    //List of supported arguments
    private enum Argument {
        START, FINISH, DOMAIN, THREAD, VERBOSE, HELP
    }

    public static void main(String[] args) {

        parseArguments(args);

        setDomain();

        setVerbosity();

        Racer racer = new Racer(argsMap.get(Argument.FINISH), getThreads(), verbosity);
        Link startLink = new Link(argsMap.get(Argument.START), wikify(argsMap.get(Argument.START), domain), null);
        racer.race(startLink);
    }

    private static void parseArguments(String[] args) {
        // Arguments has to come in pairs and has at least 2 pairs (start and end)
        if (args.length % 2 != 0 || args.length < 4) {
            throw new IllegalArgumentException("Invalid number of arguments! Try '-h' for help");
        }

        //TODO: Assuming the arguments are passed in the format of "-s abc -e cde -d abc" etc. not the most flexible
        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "-s":
                case "--start":
                    argsMap.put(Argument.START, args[++i]);
                    break;
                case "-f":
                case "--finish":
                    argsMap.put(Argument.FINISH, args[++i]);
                    break;
                case "-d":
                case "--domain":
                    argsMap.put(Argument.DOMAIN, args[++i]);
                    break;
                case "-t":
                case "--thread":
                    argsMap.put(Argument.THREAD, args[++i]);
                    break;
                case "-v":
                case "--verbose":
                    argsMap.put(Argument.VERBOSE, args[++i]);
                    break;
                case "-h":
                case "--help":
                    argsMap.put(Argument.HELP, args[++i]);
                    break;
                default:
                    throw new IllegalArgumentException("Argument not supported! Try '-h' for help");
            }
        }

        if (argsMap.get(Argument.START) == null || argsMap.get(Argument.FINISH) == null || argsMap.get(Argument.START).isEmpty() || argsMap.get(Argument.FINISH).isEmpty()) {
            throw new IllegalArgumentException("Missing required start and finish arguments! Try '-h' for help");
        }
    }

    private static void setVerbosity() {
        verbosity = Boolean.valueOf(argsMap.get(Argument.VERBOSE));
    }

    private static int getThreads() {

        return argsMap.get(Argument.THREAD) == null || argsMap.get(Argument.THREAD).isEmpty() ? DEFAULT_NUM_THREADS : Integer.valueOf(argsMap.get(Argument.THREAD));
    }

    private static void setDomain() {

        //Set default domain to wiki
        domain = argsMap.get(Argument.DOMAIN);
        domain = domain == null || domain.isEmpty() ? "https://en.wikipedia.org/wiki/" : domain;
    }

    private static String wikify(String pageTitle, String domain) {
        return domain + pageTitle.replace(" ", "_");
    }

}
