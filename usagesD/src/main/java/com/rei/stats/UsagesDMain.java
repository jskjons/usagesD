package com.rei.stats;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class UsagesDMain {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("d",  "data-dir", true, "data directory");
        options.addOption("u",  "udp-port", true, "Listening port for incoming udp packets");
        options.addOption("h",  "http-port", true, "Listening port for incoming http requests");
        CommandLineParser parser = new PosixParser();
        CommandLine cliOpts = null;
        try {
            cliOpts = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Invalid arguments!");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        Path dataDir = Paths.get(cliOpts.getOptionValue("d", "."));
        int udpPort = Integer.parseInt(cliOpts.getOptionValue("u", "9125"));
        int httpPort = Integer.parseInt(cliOpts.getOptionValue("h", "9126"));
        
        try {
            UsagesD usagesD = new UsagesD(dataDir, udpPort, httpPort);
            usagesD.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
