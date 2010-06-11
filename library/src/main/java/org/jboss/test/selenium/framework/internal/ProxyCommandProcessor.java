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
package org.jboss.test.selenium.framework.internal;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * Abstract proxy for implementing command processor proxy.
 * </p>
 * 
 * <p>
 * For implementation it is enough to overwrite {@link ProxyCommandProcessor#doCommand(String, String[])}
 * </p>
 * 
 * <p>
 * Takes a CommandProcessor as target for proxied commands.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class ProxyCommandProcessor implements CommandProcessor {

    protected CommandProcessor commandProcessor;

    public ProxyCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public abstract String doCommand(String command, String[] args);

    public boolean getBoolean(String string, String[] strings) {
        return commandProcessor.getBoolean(string, strings);
    }

    public boolean[] getBooleanArray(String string, String[] strings) {
        return commandProcessor.getBooleanArray(string, strings);
    }

    public Number getNumber(String string, String[] strings) {
        return commandProcessor.getNumber(string, strings);
    }

    public Number[] getNumberArray(String string, String[] strings) {
        return commandProcessor.getNumberArray(string, strings);
    }

    public String getRemoteControlServerLocation() {
        return commandProcessor.getRemoteControlServerLocation();
    }

    public String getString(String string, String[] strings) {
        return commandProcessor.getString(string, strings);
    }

    public String[] getStringArray(String string, String[] strings) {
        return commandProcessor.getStringArray(string, strings);
    }

    public void setExtensionJs(String extensionJs) {
        commandProcessor.setExtensionJs(extensionJs);
    }

    public void start() {
        commandProcessor.start();
    }

    public void start(String optionsString) {
        commandProcessor.start(optionsString);
    }

    public void start(Object optionsObject) {
        commandProcessor.start(optionsObject);
    }

    public void stop() {
        commandProcessor.stop();
    }
}
