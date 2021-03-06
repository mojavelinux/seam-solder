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

import java.util.Map;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

final class Log4jLoggerProvider implements LoggerProvider {

    public Logger getLogger(final String name) {
        return new Log4jLogger(name);
    }

    public Object getMdc(String key) {
        return MDC.get(key);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMdcMap() {
        return MDC.getContext();
    }

    public Object putMdc(String key, Object val) {
        try {
            return MDC.get(key);
        } finally {
            MDC.put(key, val);
        }
    }

    public void removeMdc(String key) {
        MDC.remove(key);
    }

    public void clearNdc() {
        NDC.clear();
    }

    public String getNdc() {
        return NDC.get();
    }

    public int getNdcDepth() {
        return NDC.getDepth();
    }

    public String peekNdc() {
        return NDC.peek();
    }

    public String popNdc() {
        return NDC.pop();
    }

    public void pushNdc(String message) {
        NDC.push(message);
    }

    public void setNdcMaxDepth(int maxDepth) {
        NDC.setMaxDepth(maxDepth);
    }
}
