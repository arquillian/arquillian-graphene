package org.jboss.arquillian.graphene.spi.components.popup;

import org.jboss.arquillian.graphene.spi.components.common.ComponentsContainer;

/**
 * <p>
 * PopupComponent represents a popup which can be showed and then closed.
 * </p>
 * <p>
 * There are various implementations possible. Like modal popup, popup panel,
 * popup panel which is showed after clicking or hovering on or over someting,
 * and so on.
 * </p>
 * 
 * @author jhuska
 * 
 */
public interface PopupComponent<T> extends ComponentsContainer {

	/**
	 * Returns whether this popup is visible.
	 * @return
	 */
	boolean isVisible();
}
