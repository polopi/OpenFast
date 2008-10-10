package org.openfast.examples;

import java.io.File;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class OpenFastExample {
    protected static CommandLine parseCommandLine(String[] args, Options options) {
        try {
            BasicParser parser = new BasicParser();
            return parser.parse(options, args);
        } catch (ParseException e) {
            displayHelp(options);
            return null;
        }
    }

    protected static void displayHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("perf", options);
        System.exit(1);
    }

    protected static int getInteger(CommandLine cl, String option) {
        try {
            return Integer.parseInt(cl.getOptionValue(option));
        } catch (NumberFormatException e) {
            System.out.println("The required parameter \"" + option + "\" must be an integer.");
            System.exit(1);
            return 0;
        }
    }

    protected static File getFile(CommandLine cl, String option) {
        Assert.assertTrue(cl.hasOption(option), "The required parameter \"" + option + "\" is missing.");
        File file = new File(cl.getOptionValue(option));
        return file;
    }
}
