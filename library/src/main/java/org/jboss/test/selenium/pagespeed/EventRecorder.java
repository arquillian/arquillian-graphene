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

    final JavaScript openFirebug = JavaScript.fromResource("javascript/eventrecorder-control/open-firebug.js");
    final JavaScript markEvent = JavaScript.fromResource("javascript/eventrecorder-control/mark-event.js");
    final JavaScript flushEvents = JavaScript.fromResource("javascript/eventrecorder-control/flush-events.js");
    final JavaScript waitForData = JavaScript.fromResource("javascript/eventrecorder-control/wait-for-data.js");
    final JavaScript getData = JavaScript.fromResource("javascript/eventrecorder-control/get-data.js");

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
     * <p>
     * Opens the EventRecorder.
     * </p>
     * 
     * <p>
     * Actually opens Firebug window on Activity panel.
     * </p>
     */
    public void open() {
        getSelenium().getEval(openFirebug);
    }

    /**
     * Mark the event with given title on timeline
     * 
     * @param title
     *            the title for marked time frame
     */
    public void markEvent(String title) {
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
        getSelenium().getEval(flushEvents);
        getSelenium().waitForCondition(waitForData, DATA_TIMEOUT);
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
