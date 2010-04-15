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

import java.util.*;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.test.selenium.guard.Guard;
import org.jboss.test.selenium.guard.Guarded;

import com.thoughtworks.selenium.CommandProcessor;

public class GuardedCommandProcessor extends ProxyCommandProcessor implements Guarded {

    private static String[] GUARDED_COMMANDS = initGuardedCommands();

    public GuardedCommandProcessor(CommandProcessor commandProcessor) {
        super(commandProcessor);

        registeredGuards = newMap();
        guardOnce = newMap();
    }

    public Map<Class<? extends Guard>, Guard> registeredGuards;
    public Map<Class<? extends Guard>, Guard> guardOnce;

    @Override
    public String doCommand(String command, String[] args) {

        if (Arrays.binarySearch(GUARDED_COMMANDS, command) < 0) {
            return commandProcessor.doCommand(command, args);
        }
            
        executeGuard(beforeCommand);

        try {
            String result = commandProcessor.doCommand(command, args);

            executeGuard(afterCommand);

            return result;
        } finally {
            guardOnce.clear();
            guardOnce = newMap();
        }
    }

    private void executeGuard(GuardedCommand guardedCommand) {
        for (Map.Entry<Class<? extends Guard>, Guard> entry : registeredGuards.entrySet()) {
            if (!guardOnce.containsKey(entry.getKey())) {
                guardedCommand.executeGuard(entry.getValue());
            }
        }

        for (Guard guard : guardOnce.values()) {
            guardedCommand.executeGuard(guard);
        }
    }

    public void registerGuard(Guard guard) {
        registeredGuards.put(resolveInterface(guard), guard);
    }

    public void unregisterGuard(Guard guard) {
        registeredGuards.remove(resolveInterface(guard));
    }

    public <T extends Guard> void guardOnce(Guard guard) {
        guardOnce.put(resolveInterface(guard), guard);
    }

    private GuardedCommand beforeCommand = new GuardedCommand() {
        public void executeGuard(Guard guard) {
            guard.doBeforeCommand();
        }
    };

    private GuardedCommand afterCommand = new GuardedCommand() {
        public void executeGuard(Guard guard) {
            guard.doAfterCommand();
        }
    };

    private Class<? extends Guard> resolveInterface(Guard guard) {
        return guard.getClass().asSubclass(Guard.class);
    }

    private static Map<Class<? extends Guard>, Guard> newMap() {
        return new HashMap<Class<? extends Guard>, Guard>();
    }

    private interface GuardedCommand {
        public void executeGuard(Guard guard);
    }

    protected GuardedCommandProcessor immutableCopy() {
        GuardedCommandProcessor copy = new GuardedCommandProcessor(this.commandProcessor);
        copy.registeredGuards.putAll(this.guardOnce);
        copy.guardOnce.putAll(this.guardOnce);
        return copy;
    }

    public static String[] initGuardedCommands() {
        String[] guardedCommands = new String[] { "click", "doubleClick", "contextMenu", "clickAt", "doubleClickAt",
            "contextMenuAt", "fireEvent", "focus", "keyPress", "shiftKeyDown", "shiftKeyUp", "metaKeyDown",
            "metaKeyUp", "altKeyDown", "altKeyUp", "controlKeyDown", "controlKeyUp", "keyDown", "keyUp", "mouseOver",
            "mouseOut", "mouseDown", "mouseDownRight", "mouseDownRightAt", "mouseUp", "mouseUpRight", "mouseUpAt",
            "mouseUpRightAt", "mouseMove", "mouseMoveAt", "typeKeys", "check", "uncheck", "select", "addSelection",
            "removeSelection", "removeAllSelections", "submit", "getEval", "dragdrop", "dragAndDrop",
            "dragAndDropToObject", "keyDownNative", "keyUpNative", "keyPressNative" };
        Arrays.sort(guardedCommands);
        return guardedCommands;
    }
}
