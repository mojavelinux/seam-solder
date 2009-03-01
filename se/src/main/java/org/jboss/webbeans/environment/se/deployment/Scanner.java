/**
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.webbeans.environment.se.deployment;

import java.io.File;

/**
 * The Scanner is used to find resources to be processed by Seam
 *
 * The processing is done by {@link DeploymentHandler}s
 *
 * @author Pete Muir
 *
 */
public interface Scanner
{
    /**
     * Recursively scan directories, skipping directories in the exclusion list.
     *
     * @param directories An array of the roots of the directory trees to scan
     */
    public void scanDirectories( File[] directories );

    /**
     * Recursively scan directories, skipping directories in the exclusion list.
     *
     * @param directories An array of the roots of the directory trees to scan
     * @param excludedDirectories Directories to skip over during the recursive scan
     */
    public void scanDirectories( File[] directories, File[] excludedDirectories );

    /**
     * Scan for structures which contain any of the given resources in their root
     *
     * @param resources The resources to scan for
     */
    public void scanResources( String[] resources );

    /**
     * Get the deployment strategy this scanner is used by
     */
    public DeploymentStrategy getDeploymentStrategy(  );

    public long getTimestamp(  );
}