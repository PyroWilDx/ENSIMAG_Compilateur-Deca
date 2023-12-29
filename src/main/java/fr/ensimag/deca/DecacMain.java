package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;
import java.util.concurrent.*;
/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl47
 * @date 01/01/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            throw new UnsupportedOperationException("decac -b not yet implemented");
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("Usage : ./decac <Source File> -Options");
            // TODO (Better Message)
        }
        if (options.getParallel()) {
            ExecutorService executorService = Executors.newFixedThreadPool(options.getSourceFiles().size());
            for (File source : options.getSourceFiles()) {
                executorService.execute(() -> {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    compiler.compile();
                });
            }
            executorService.shutdown();

            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO (Test Parallèle)
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
