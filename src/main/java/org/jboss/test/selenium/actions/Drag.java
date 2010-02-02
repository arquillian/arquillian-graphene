/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/

package org.jboss.test.selenium.actions;

import org.jboss.test.selenium.waiting.Wait;
import org.jboss.test.selenium.waiting.Wait.Waiting;

import com.thoughtworks.selenium.Selenium;

/**
 * Provides item dragging capability to Selenium tests.
 * 
 * Methods can be call in following serie: start, mouseOut, move, enter, drop.
 * 
 * If we are calling preceding phase (e.g. move, when drag was already called),
 * IllegalStateException is thrown.
 * 
 * If we are calling following phase (e.g. drop, when no action was called), all
 * phases preceding requested phase will be done before requested phase can be
 * done.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Drag {
    // specifies phase in which is dragging state
    private int phase;
    private Selenium selenium;
    private String item;
    private String target;
    private int x;
    private int y;
    private final int STEPS = 5;
    private final int FIRST_STEP = 2;
    private final Waiting wait = Wait.timeout(10);

    /**
     * @param selenium
     *            initialized and started Selenium instance
     * @param item
     *            item to drag
     * @param target
     *            target of item dragging
     */
    public Drag(Selenium selenium, String item, String target) {
        this.phase = 0;
        this.selenium = selenium;
        this.item = item;
        this.target = target;
        x = selenium.getElementPositionLeft(target).intValue() - selenium.getElementPositionLeft(item).intValue();
        y = selenium.getElementPositionTop(target).intValue() - selenium.getElementPositionTop(item).intValue();
    }

    /**
     * Starts first phase of dragging.
     * 
     * Simulate left mouse button pressing and small initial movement.
     */
    public void start() {
        process(0);
    }

    /**
     * Starts second phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this
     * phase.
     * 
     * Simulate movement of mouse cursor out of the item that ve want to drag.
     */
    public void mouseOut() {
        process(1);
    }

    /**
     * Starts third phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this
     * phase.
     * 
     * Simulate movement of mouse cursor near the target item.
     */
    public void move() {
        process(2);
    }

    /**
     * Starts fourth phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this
     * phase.
     */
    public void enter() {
        process(3);
    }

    /**
     * Last phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this
     * phase.
     * 
     * Drop the item to target.
     */
    public void drop() {
        process(4);
    }

    /**
     * Holds whole process of dragging serialized to one switch condition.
     * 
     * If some phase is called by its number, it will be recognized, that is
     * possible to process this step.
     * 
     * Internally is used counter 'phase' which will be decreased when passed to
     * a new phase. Switch condition breaks when will finished in requesting
     * phase.
     */
    private void process(int request) {
        if (request < phase) {
            throw new RuntimeException();
        }

        switch (phase) {
        case 0: // START

            selenium.mouseDown(item);
            String moveCoords = coords((x < 0) ? FIRST_STEP : -FIRST_STEP, (y < 0) ? FIRST_STEP : -FIRST_STEP);
            selenium.mouseMoveAt(item, moveCoords);

            if (request < ++phase)
                break;
        case 1: // MOUSE OUT

            selenium.mouseOut(item);

            if (request < ++phase)
                break;
        case 2: // MOVE

            for (int i = 0; i < STEPS; i++) {
                selenium.mouseMoveAt(item, coords(x * i / STEPS, y * i / STEPS));
                wait.waitForTimeout();
            }

            if (request < ++phase)
                break;
        case 3: // ENTER

            selenium.mouseMoveAt(item, coords(x, y));
            selenium.mouseOver(target);

            if (request < ++phase)
                break;
        case 4: // DROP

            selenium.mouseUp(target);

            if (request < ++phase)
                break;
        }
    }

    /**
     * Returns string with given coordinates by pattern "x,y" (e.g. "-30,100").
     */
    private String coords(int x, int y) {
        return String.format("%d,%d", x, y);
    }

}
