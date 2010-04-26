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
package org.jboss.test.selenium.framework;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jboss.test.selenium.guard.Guard;
import org.jboss.test.selenium.guard.Guarded;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * CommandProcessor executing commands inside guarded block.
 * </p>
 * 
 * <p>
 * Before (resp. after) the command will be executed, each guard registered in this command processor will be executed.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class GuardedCommandProcessor extends ProxyCommandProcessor implements Guarded {

    /**
     * Commands, which should be guarded - it means commands that do some interaction on client side and can call the
     * request
     */
    private static final String[] GUARDED_COMMANDS = initGuardedCommands();

    /** The block which will be executed before command will have turn */
    private GuardBlock beforeCommand = new GuardBlock() {
        public void executeGuard(Guard guard) {
            guard.doBeforeCommand();
        }
    };

    /** The block which will be executed after the command execution */
    private GuardBlock afterCommand = new GuardBlock() {
        public void executeGuard(Guard guard) {
            guard.doAfterCommand();
        }
    };

    /** The registered guards. */
    private Map<Class<? extends Guard>, Guard> registeredGuards;
    
    /**
     * Instantiates a new guarded command processor as the proxy for given commandProcessor
     *
     * @param commandProcessor the command processor to be used to proxying
     */
    public GuardedCommandProcessor(CommandProcessor commandProcessor) {
        super(commandProcessor);

        registeredGuards = new HashMap<Class<? extends Guard>, Guard>();
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.framework.ProxyCommandProcessor#doCommand(java.lang.String, java.lang.String[])
     */
    @Override
    public String doCommand(String command, String[] args) {

        if (Arrays.binarySearch(GUARDED_COMMANDS, command) < 0) {
            return commandProcessor.doCommand(command, args);
        }

        executeGuard(beforeCommand);
        String result = commandProcessor.doCommand(command, args);
        executeGuard(afterCommand);

        return result;
    }

    /**
     * Executes the guard block with each registered guard.
     * 
     * This will cause run of {@link Guard#doBeforeCommand()} and {@link Guard#doAfterCommand()}.
     *
     * @param guardBlock the specific guard block, to execute before or after command execution
     */
    private void executeGuard(GuardBlock guardBlock) {
        for (Guard guard : registeredGuards.values()) {
            guardBlock.executeGuard(guard);
        }
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.guard.Guarded#registerGuard(org.jboss.test.selenium.guard.Guard)
     */
    public void registerGuard(Guard guard) {
        registeredGuards.put(resolveInterface(guard), guard);
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.guard.Guarded#unregisterGuard(org.jboss.test.selenium.guard.Guard)
     */
    public void unregisterGuard(Guard guard) {
        Class<? extends Guard> classToRemove = null;

        for (Map.Entry<Class<? extends Guard>, Guard> entry : registeredGuards.entrySet()) {
            if (entry.getValue().equals(guard)) {
                classToRemove = entry.getKey();
            }
        }

        if (classToRemove == null) {
            throw new NoSuchElementException("there is no such guard defined");
        }

        registeredGuards.remove(classToRemove);
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.guard.Guarded#unregisterGuards(java.lang.Class)
     */
    public void unregisterGuards(Class<? extends Guard> type) {
        registeredGuards.remove(type);
    }

    /**
     * Interface for executing the specific command on the guard.
     */
    private interface GuardBlock {
        
        /**
         * Execute guard.
         *
         * @param guard the guard
         */
        void executeGuard(Guard guard);
    }

    /**
     * Utility method for resolving the nearest type to Guard for given guard type.
     *
     * @param guard the guard
     * @return the the nearest type to Guard for given guard type
     */
    private Class<? extends Guard> resolveInterface(Guard guard) {
        return guard.getClass().asSubclass(Guard.class);
    }

    /**
     * Creates immutable copy of this command processor
     *
     * @return the guarded command processor
     */
    protected GuardedCommandProcessor immutableCopy() {
        GuardedCommandProcessor copy = new GuardedCommandProcessor(this.commandProcessor);
        copy.registeredGuards.putAll(this.registeredGuards);
        return copy;
    }

    /**
     * Inits the guarded commands.
     *
     * @return the string[] of the guarded commands
     */
    public static String[] initGuardedCommands() {
        String[] guardedCommands = new String[] {"click", "doubleClick", "contextMenu", "clickAt", "doubleClickAt",
            "contextMenuAt", "fireEvent", "focus", "keyPress", "shiftKeyDown", "shiftKeyUp", "metaKeyDown",
            "metaKeyUp", "altKeyDown", "altKeyUp", "controlKeyDown", "controlKeyUp", "keyDown", "keyUp", "mouseOver",
            "mouseOut", "mouseDown", "mouseDownRight", "mouseDownRightAt", "mouseUp", "mouseUpRight", "mouseUpAt",
            "mouseUpRightAt", "mouseMove", "mouseMoveAt", "typeKeys", "check", "uncheck", "select", "addSelection",
            "removeSelection", "removeAllSelections", "submit", "getEval", "dragdrop", "dragAndDrop",
            "dragAndDropToObject", "keyDownNative", "keyUpNative", "keyPressNative"};
        Arrays.sort(guardedCommands);
        return guardedCommands;
    }

}
