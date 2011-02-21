/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logging;

import java.text.MessageFormat;

final class Log4jLogger extends Logger {

    private static final long serialVersionUID = -5446154366955151335L;

    private final org.apache.log4j.Logger logger;

    Log4jLogger(final String name) {
        super(name);
        logger = org.apache.log4j.Logger.getLogger(name);
    }

    public boolean isEnabled(final Level level) {
        final org.apache.log4j.Level l = translate(level);
        return logger.isEnabledFor(l) && l.isGreaterOrEqual(logger.getEffectiveLevel());
    }

    protected void doLog(final Level level, final String loggerClassName, final Object message, final Object[] parameters, final Throwable thrown) {
        logger.log(loggerClassName, translate(level), parameters == null || parameters.length == 0 ? message : MessageFormat.format(String.valueOf(message), parameters), thrown);
    }

    protected void doLogf(final Level level, final String loggerClassName, final String format, final Object[] parameters, final Throwable thrown) {
        logger.log(loggerClassName, translate(level), parameters == null ? String.format(format) : String.format(format, parameters), thrown);
    }

    private static org.apache.log4j.Level translate(final Level level) {
        if (level != null) switch (level) {
            case FATAL: return org.apache.log4j.Level.FATAL;
            case ERROR: return org.apache.log4j.Level.ERROR;
            case WARN:  return org.apache.log4j.Level.WARN;
            case INFO:  return org.apache.log4j.Level.INFO;
            case DEBUG: return org.apache.log4j.Level.DEBUG;
            case TRACE: return org.apache.log4j.Level.TRACE;
        }
        return org.apache.log4j.Level.ALL;
    }
}
