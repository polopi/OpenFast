package org.openfast.logging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Loggers {
    private Map<Class<?>, Map<String, FastMessageLogger>> loggers = Collections.emptyMap();
    
    public FastMessageLogger getLogger(Class<?> clazz, String identifier) {
        if (loggers.isEmpty())
            loggers = new HashMap<>();
        Map<String, FastMessageLogger> loggerIdentifierMap = loggers.get(clazz);
        if (loggerIdentifierMap.isEmpty()) {
            loggerIdentifierMap = new HashMap<>();
            loggers.put(clazz, loggerIdentifierMap);
        }
        return null;
    }
}
