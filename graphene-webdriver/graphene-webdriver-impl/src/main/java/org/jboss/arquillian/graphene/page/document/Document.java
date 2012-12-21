package org.jboss.arquillian.graphene.page.document;

import org.jboss.arquillian.graphene.javascript.JavaScript;

/**
 * The JavaScript document object and its methods
 *
 * @author Lukas Fryc
 */
@JavaScript("document")
public interface Document {

    String getReadyState();
}
