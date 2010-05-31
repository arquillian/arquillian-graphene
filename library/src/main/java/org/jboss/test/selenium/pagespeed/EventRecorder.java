/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.pagespeed;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.internal.Contextual;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * The EventRecorder controller responsible for controlling state of Firebug window, marking the events and flushing
 * data to output file.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * 
 */
public class EventRecorder implements Contextual {

    static final long DATA_TIMEOUT = 5000L;

    boolean enabled = true;
    Boolean extensionsInstalled = null;
    final JavaScript isExtensionsInstalled = new JavaScript("eventRecorder.isExtensionInstalled()");
    final JavaScript openFirebug = new JavaScript("eventRecorder.open()");
    final JavaScript closeFirebug = new JavaScript("eventRecorder.close()");
    final JavaScript stopProfiler = new JavaScript("eventRecorder.stopProfiler()");
    final JavaScript markEvent = new JavaScript("eventRecorder.markEvent('{0}')");
    final JavaScript flushEvents = new JavaScript("eventRecorder.flushEvents()");
    final JavaScript isDataAvailable = new JavaScript("eventRecorder.isDataAvailable()");
    final JavaScript getData = new JavaScript("eventRecorder.getData()");

    File outputDir;

    /**
     * Constructs EventRecorder with given output directory
     * 
     * @param outputDir
     *            the directory for output of recorded events
     */
    public EventRecorder(File outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * Returns true if EventRecorder is enabled and it's browser extensions is installed
     * 
     * @return true if EventRecorder is enabled and it's browser extensions is installed
     */
    public boolean isEnabled() {
        return enabled && isExtensionInstalled();
    }

    /**
     * By setting the enabled flag to boolean value, it can enable/disable event recoding.
     * 
     * @param enabled
     *            if true, the event recorder will be enabled; otherwise will be the event recorder disabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks that EventRecorder PageSpeed's extension is installed in the page.
     * 
     * @return true if extensions is installed; false otherwise
     */
    public boolean isExtensionInstalled() {
        if (extensionsInstalled == null) {
            extensionsInstalled = Boolean.valueOf(getSelenium().getEval(isExtensionsInstalled));
        }
        return extensionsInstalled;
    }

    /**
     * <p>
     * Opens the EventRecorder.
     * </p>
     * 
     * <p>
     * Actually opens Firebug window on Activity panel.
     * </p>
     */
    public void open() {
        if (!isEnabled()) {
            return;
        }
        getSelenium().getEval(openFirebug);
    }

    /**
     * <p>
     * Closes the EventRecorder.
     * </p>
     * 
     * <p>
     * Actually closes Firebug panel.
     * </p>
     */
    public void close() {
        if (!isEnabled()) {
            return;
        }
        getSelenium().getEval(closeFirebug);
    }

    /**
     * <p>
     * Stops the PageSpeed's profiler.
     * </p>
     */
    public void stopProfiler() {
        if (!isEnabled()) {
            return;
        }
        getSelenium().getEval(stopProfiler);
    }

    /**
     * Mark the event with given title on timeline
     * 
     * @param title
     *            the title for marked time frame
     */
    public void markEvent(String title) {
        if (!isEnabled()) {
            return;
        }
        getSelenium().getEval(markEvent.parametrize(title));
    }

    /**
     * <p>
     * Flush the events from EventRecorder to file given by descriptor (with .json extension).
     * </p>
     * 
     * <p>
     * The file will be stored in directory specified in constructor.
     * </p>
     * 
     * @param descriptor
     *            the filename pattern for output file (without .json extensions)
     */
    public void flushEvents(String descriptor) {
        if (!isEnabled()) {
            return;
        }

        getSelenium().getEval(flushEvents);
        getSelenium().waitForCondition(isDataAvailable, DATA_TIMEOUT);
        String jsonData = getSelenium().getEval(getData);

        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File outputFile = new File(outputDir, descriptor + ".json");

        FileWriter output = null;
        try {
            output = new FileWriter(outputFile);
            IOUtils.write(jsonData, output);
        } catch (IOException e) {
            throw new IllegalStateException(format("EventRecorder was unable to write data to '{0}'", outputFile
                .getAbsolutePath()), e);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * Obtains the AjaxSelenium object from current context.
     * 
     * @return the AjaxSelenium object from current context.
     */
    private AjaxSelenium getSelenium() {
        return AjaxSelenium.getCurrentContext(this);
    }
}
