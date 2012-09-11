/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.component.object.api.validation;

import java.util.List;

/**
 * Validation component represents set of inputs which are validated somehow. That is, there are some constraints, which have to
 * be fulfilled in order to be submitted, like various patterns etc.
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public interface ValidationComponent {

    boolean isThereAnyErrorMessage();

    /**
     * Returns the list containing all the messages currently on the page.
     * 
     * @return
     */
    List<Message> getAllMessages();

    /**
     * Returns the list containing all the error messages currently on the page.
     * 
     * @return
     */
    List<ErrorMessage> getAllErrorMessages();

    /**
     * Returns the list containing all the info messages currently on the page.
     * 
     * @return
     */
    List<ErrorMessage> getAllInfoMessages();

    /**
     * Determines whether the given message is rendered on the page.
     * 
     * @param msg
     * @return
     */
    boolean isMessageRendered(Message msg);

    /**
     * Determines whether the given static part of the message is rendered on the page.
     * 
     * @param msg
     * @return
     */
    boolean isMessageRendered(StaticMessagePart msg);

    /**
     * Represents the general messages which can appear on the page during validation.
     * 
     * @author jhuska
     * 
     */
    public interface Message {

        StaticMessagePart getStaticMessagePart();

        VariableMessagePart getVariableMessagePart();

        void setVariableMessagePart();

        void setStaticMessagePart();
    }

    /**
     * Represents error message which can appear on the page during validation.
     * 
     * @author jhuska
     * 
     */
    public interface ErrorMessage extends Message {

    }

    /**
     * Represents info message which can appear on the page during validation.
     * 
     * @author jhuska
     * 
     */
    public interface InfoMessage extends Message {

    }

    public interface StaticMessagePart {

        String getValue();
    }

    public interface VariableMessagePart {

        String getValue();
    }
}
