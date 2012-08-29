package org.jboss.arquillian.graphene.spi.components.validation;

import java.util.List;

import org.jboss.arquillian.graphene.spi.components.common.Component;

/**
 * Validation component represents set of inputs which are validated somehow. That is, there are some constraints, which have to
 * be fulfilled in order to be submitted, like various patterns etc.
 * 
 * @author jhuska
 * 
 */
public interface ValidationComponent extends Component {

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
