package dev.uncandango.atl_agent;

import dev.uncandango.atl_agent.transformer.TransformerDiscovererConstantsTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;


public class ATLAgent {
    public static final Logger LOGGER = LoggerFactory.getLogger("All The Leaks Agent");

    public static void premain(String arg, Instrumentation inst) {
        LOGGER.debug("Starting ATL Agent...");
		inst.addTransformer(new TransformerDiscovererConstantsTransformer());
    }
}
