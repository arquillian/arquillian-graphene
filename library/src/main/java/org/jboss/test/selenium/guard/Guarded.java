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

/**
 * Class implementing {@link Guarded} specified, that executes commands, which can be guarded by some {@link Guard}
 * implementation.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface Guarded {

    /**
     * <p>
     * Registers guard, which should guard execution of command executed by this object.
     * </p>
     * 
     * <p>
     * Overwrites a guard, which was specified before, and has same sub-interface as given guard.
     * </p>
     * 
     * <p>
     * For example:
     * </p>
     * 
     * <pre>
     * <tt><b>interface ExtendedGuard</b> extends {@link Guard}</tt>
     * 
     * <tt><b>MyGuard</b> implements ExtendedGuard</tt>
     * 
     * <tt><b>MyOtherGuard</b> implements ExtendedGuard</tt>
     * 
     * <tt>Guard guard1 = new MyGuard();</tt>
     * 
     * <tt>Guard guard2 = new MyOtherGuard();</tt>
     * </pre>
     * 
     * <p>
     * <b><tt>guard2</tt></b> will then overwrite already registered <b><tt>guard1</tt></b>.
     * </p>
     * 
     * <p>
     * This will satisfy, that guards of same type will be overriden.
     * </p>
     * 
     * @param guard
     *            to register
     */
    void registerGuard(Guard guard);

    /**
     * Unregisters guard, which should guard execution of command executed by this object.
     * 
     * @param guard
     *            to unregister
     */
    void unregisterGuard(Guard guard);

    /**
     * Unregisters all the guards of given type.
     * 
     * @param type
     *            type of guards to unregister
     */
    void unregisterGuards(Class<? extends Guard> type);
}