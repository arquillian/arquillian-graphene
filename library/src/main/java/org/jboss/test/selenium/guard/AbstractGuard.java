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
package org.jboss.test.selenium.guard;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * AbstractGuard encapsulates common functionality for commands which should be executed before (resp. after) the
 * command.
 * </p>
 * 
 * <p>
 * Provide us with the information, if the particular command can be executed, based on set of the commands, which are
 * associated for guarding by implementation guard.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractGuard implements Guard {

    protected static final Set<String> INTERACTIVE_COMMANDS = initInteractiveCommands();

    /**
     * Returns true whenever the given command is in the set of the commands allowed to be guarded by this guard.
     */
    public boolean isGuarding(String command) {
        return getGuardedCommands().contains(command);
    }

    /**
     * <p>
     * Point for extension: define here what commands should be guarded.
     * </p>
     * 
     * <p>
     * For better performance use cached value of SortedSet.
     * </p>
     * 
     * <p>
     * For ensuring consistency, use unmodifiable set as a result.
     * </p>
     * 
     * @return the set of commands, which should be guarded by this guard.
     */
    protected abstract Set<String> getGuardedCommands();

    /**
     * Inits the set of interactive commands.
     * 
     * @return the set of the interactive selenium commands
     */
    public static Set<String> initInteractiveCommands() {
        return Collections.unmodifiableSortedSet(new TreeSet<String>(Arrays.asList(new String[] {"click",
            "doubleClick", "contextMenu", "clickAt", "doubleClickAt", "contextMenuAt", "fireEvent", "focus",
            "keyPress", "shiftKeyDown", "shiftKeyUp", "metaKeyDown", "metaKeyUp", "altKeyDown", "altKeyUp",
            "controlKeyDown", "controlKeyUp", "keyDown", "keyUp", "mouseOver", "mouseOut", "mouseDown",
            "mouseDownRight", "mouseDownRightAt", "mouseUp", "mouseUpRight", "mouseUpAt", "mouseUpRightAt",
            "mouseMove", "mouseMoveAt", "typeKeys", "check", "uncheck", "select", "addSelection", "removeSelection",
            "removeAllSelections", "submit", "getEval", "dragdrop", "dragAndDrop", "dragAndDropToObject",
            "keyDownNative", "keyUpNative", "keyPressNative" })));
    }
}
