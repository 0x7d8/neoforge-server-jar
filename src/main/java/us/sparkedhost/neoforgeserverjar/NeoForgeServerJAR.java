package us.sparkedhost.neoforgeserverjar;

import us.sparkedhost.neoforgeserverjar.server.ServerBootstrap;
import us.sparkedhost.neoforgeserverjar.utils.ErrorReporter;

import java.io.*;
import java.lang.management.ManagementFactory;

public class NeoForgeServerJAR {
    public static void main(final String[] args) {
        // Determine unix_args.txt location
        String directoryPath = "libraries/net/minecraftforge/forge";
        String forgeVersion = null;
        File directory = new File(directoryPath);
        File[] filesAndDirs = directory.listFiles();

        if (filesAndDirs == null) {
            ErrorReporter.error("08", true);
        }

        assert filesAndDirs != null;
        for (File fileOrDir : filesAndDirs) {
            if (fileOrDir.isDirectory()) {
                forgeVersion = fileOrDir.getName();
            }
        }

        if(forgeVersion == null) {
            ErrorReporter.error("09", true);
        }

        // Server startup
        String[] vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
        String[] cmd = new String[vmArgs.length + 2];
        cmd[0] = "java";

        System.arraycopy(vmArgs, 0, cmd, 1, vmArgs.length);

        boolean windows = System.getProperty("os.name").startsWith("Windows");
        cmd[1 + vmArgs.length] = "@libraries/net/minecraftforge/forge/" + forgeVersion + "/" + (windows ? "win" : "unix") + "_args.txt";
        String cmdStr = String.join(" ", cmd);

        try {
            new ServerBootstrap().startServer(cmd);
        } catch (ServerBootstrap.ServerStartupException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
