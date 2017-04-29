package com.segment.wikiracer;

import com.segment.wikiracer.util.LinkHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static String domain;
    private static boolean verbosity = false;

    //List of supported arguments
    private enum Argument {
        START, FINISH, DOMAIN, THREAD, VERBOSE, HELP
    }

    private static final int DEFAULT_NUM_THREADS = 100;
    private static ExecutorService executor;

    private static Map<Argument, String> argsMap = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("Starting at: " + System.currentTimeMillis());

        parseArguments(args);

        setDomain();

        setThreadPool();

        setVerbosity();

        Traverser traverser = new Traverser(argsMap.get(Argument.FINISH), domain, executor, verbosity);
        traverser.traverse(LinkHelper.wikify(argsMap.get(Argument.START), domain), argsMap.get(Argument.START), new Path());

        executor.shutdown();
        while (!executor.isTerminated()) {

        }
        System.out.println("Can not find a path to the end!");
        System.out.println("Finish at: " + System.currentTimeMillis());

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
            throw new IllegalArgumentException("Missing required start and end arguments! Try '-h' for help");
        }
    }

    private static void setVerbosity() {
        verbosity = Boolean.valueOf(argsMap.get(Argument.VERBOSE));
    }

    private static void setThreadPool() {

        int threads = argsMap.get(Argument.THREAD) == null || argsMap.get(Argument.THREAD).isEmpty() ? DEFAULT_NUM_THREADS : Integer.valueOf(argsMap.get(Argument.THREAD));
        executor = Executors.newFixedThreadPool(threads);
    }

    private static void setDomain() {

        //Set default domain to wiki
        domain = argsMap.get(Argument.DOMAIN);
        domain = domain == null || domain.isEmpty() ? "https://en.wikipedia.org/wiki/" : domain;
    }
}
