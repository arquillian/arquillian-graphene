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
package org.jboss.arquillian.ajocado.actions;

import static org.jboss.arquillian.ajocado.waiting.Wait.waitSelenium;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.geometry.Dimension;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;

/**
 * <p>
 * Provides item dragging capability to Selenium tests.
 * </p>
 * 
 * <p>
 * Methods can be call in following serie: start, mouseOut, move, enter, drop.
 * </p>
 * 
 * <p>
 * If we are calling preceding phase (e.g. move, when drag was already called), IllegalStateException is thrown.
 * </p>
 * 
 * <p>
 * If we are calling following phase (e.g. drop, when no action was called), all phases preceding requested phase will
 * be done before requested phase can be done.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Drag {

    /** The Constant NUMBER_OF_STEPS. */
    private static final int NUMBER_OF_STEPS = 5;

    /** The Constant FIRST_STEP. */
    private static final int FIRST_STEP = 2;

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    /** The point. */
    private Point currentDelta;

    // specifies phase in which is dragging state
    /** The current phase. */
    private Phase currentPhase;

    /** The item to drag. */
    private ElementLocator<?> itemToDrag;

    /** The drag indicator. */
    private ElementLocator<?> dragIndicator;

    /** The drop target. */
    private ElementLocator<?> dropTarget;

    private Point startMove;

    private Point currentPosition;

    private Point endPosition;

    private Point overallMove;

    private Point reposition = new Point(0, 0);

    private Point movement = new Point(0, 0);

    private int numberOfSteps = NUMBER_OF_STEPS;

    /** The wait. */
    private final SeleniumWaiting wait = waitSelenium.timeout(10);

    /**
     * Initiates Drag object, handled by given Selenium instance, instructing drag of itemToDrag to dropTarget object.
     * 
     * @param itemToDrag
     *            item to drag
     * @param dropTarget
     *            target of item dragging
     */
    public Drag(ElementLocator<?> itemToDrag, ElementLocator<?> dropTarget) {
        this.currentPhase = Phase.INITIAL;
        this.itemToDrag = itemToDrag;
        this.dropTarget = dropTarget;
        setDropTarget(dropTarget);
    }

    public void setDropTarget(ElementLocator<?> dropTarget) {
        switch (currentPhase) {
            case INITIAL:
                startMove = new Point(selenium.getElementWidth(itemToDrag) / 2,
                    selenium.getElementHeight(itemToDrag) / 2);
                currentPosition = getCenterOfElement(itemToDrag);
                endPosition = getCenterOfElement(dropTarget);
                overallMove = endPosition.substract(currentPosition);
                return;
            case DROP:
                throw new IllegalStateException("draggable was already dropped");
            case ENTER:
                selenium.mouseOut(this.dropTarget);
                break;
            default:
        }
        if (Phase.MOUSE_OUT.before(currentPhase)) {
            currentPhase = Phase.MOUSE_OUT;
        }
        endPosition = getCenterOfElement(dropTarget);
        overallMove = endPosition.substract(currentPosition);
        reposition = currentDelta.substract(startMove);
        movement = new Point(0, 0);
        this.dropTarget = dropTarget;
    }

    public void setDragIndicator(ElementLocator<?> dragIndicator) {
        this.dragIndicator = dragIndicator;
    }

    public void setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    /**
     * Starts first phase of dragging.
     * 
     * Simulate left mouse button pressing and small initial movement.
     */
    public void start() {
        processUntilPhase(Phase.START);
    }

    /**
     * Starts second phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this phase.
     * 
     * Simulate movement of mouse cursor out of the item that ve want to drag.
     */
    public void mouseOut() {
        processUntilPhase(Phase.MOUSE_OUT);
    }

    /**
     * Starts third phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this phase.
     * 
     * Simulate movement of mouse cursor near the target item.
     */
    public void move() {
        processUntilPhase(Phase.MOVE);
    }

    /**
     * Starts fourth phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this phase.
     */
    public void enter() {
        processUntilPhase(Phase.ENTER);
    }

    /**
     * Last phase of dragging.
     * 
     * If there is some unfinished preceding phases, it will be done before this phase.
     * 
     * Drop the item to target.
     */
    public void drop() {
        processUntilPhase(Phase.DROP);
    }

    /**
     * Holds whole process of dragging serialized to one switch condition.
     * 
     * If some phase is called by its number, it will be recognized, that is possible to process this step.
     * 
     * Internally is used counter 'phase' which will be decreased when passed to a new phase. Switch condition breaks
     * when will finished in requesting phase.
     */
    private void processUntilPhase(Phase requestedPhase) {
        if (requestedPhase.before(currentPhase)) {
            throw new IllegalArgumentException("Requested phase (" + requestedPhase + ") is before current phase ("
                + currentPhase + ")");
        }

        while ((currentPhase.before(requestedPhase))) {
            currentPhase = currentPhase.next();
            executePhase(currentPhase);
        }
    }

    /**
     * Executes the instructions for given phase.
     * 
     * @param phase
     *            the phase what should be executed
     */
    private void executePhase(Phase phase) {
        switch (phase) {
            case START:
                currentDelta = startMove.add(new Point((overallMove.getX() < 0) ? FIRST_STEP : -FIRST_STEP,
                    (overallMove.getY() < 0) ? FIRST_STEP : -FIRST_STEP));
                selenium.mouseDownAt(itemToDrag, new Point(0, 0));
                selenium.mouseMoveAt(itemToDrag, new Point(2, 2));
                if (dragIndicator != null && selenium.isElementPresent(dragIndicator)) {
                    startMove = startMove.substract(getCentral(dragIndicator));
                }
                selenium.mouseMoveAt(itemToDrag, startMove);
                break;
            case MOUSE_OUT:
                selenium.mouseOut(itemToDrag);
                break;
            case MOVE:
                for (int i = 0; i < numberOfSteps; i++) {
                    Point oldMovement = movement;
                    movement = new Point(overallMove.getX() * i / numberOfSteps, overallMove.getY() * i / numberOfSteps);
                    Point movementDelta = movement.substract(oldMovement);

                    currentDelta = reposition.add(startMove).add(movement);
                    selenium.mouseMoveAt(itemToDrag, currentDelta);
                    currentPosition = currentPosition.add(movementDelta);
                    wait.waitForTimeout();
                }
                break;
            case ENTER:
                currentDelta = reposition.add(startMove).add(overallMove);
                currentPosition = endPosition;
                selenium.mouseMoveAt(itemToDrag, currentDelta);
                selenium.mouseOverAt(dropTarget, currentDelta);
                break;
            case DROP:
                selenium.mouseUp(dropTarget);
                break;
            default:
        }
    }

    /**
     * Enumeration of phases supported by this {@link Drag} object.
     */
    public enum Phase {

        INITIAL, START, MOUSE_OUT, MOVE, ENTER, DROP;

        /**
         * Compares given phase to this phase.
         * 
         * @param phase
         *            the phase
         * @return true, if given phase is before this phase; else otherwise
         */
        boolean before(Phase phase) {
            return this.compareTo(phase) < 0;
        }

        /**
         * Return next phase in order after this phase.
         * 
         * @return the next phase in oder after this phase
         */
        Phase next() {
            boolean returnNextPhase = false;
            for (Phase phase : Phase.values()) {
                if (returnNextPhase) {
                    return phase;
                }
                if (this == phase) {
                    returnNextPhase = true;
                }
            }
            return null;
        }
    }

    private Point getCentral(ElementLocator<?> element) {
        Dimension dimension = selenium.getElementDimension(element);
        Point central = new Point(dimension.getWidth() / 2, dimension.getHeight() / 2);
        return central;
    }

    private Point getCenterOfElement(ElementLocator<?> element) {
        return selenium.getElementPosition(element).add(getCentral(element));
    }
}
