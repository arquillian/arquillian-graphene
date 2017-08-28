package org.jboss.arquillian.graphene.assertions;

public class CouldNotVerifyException extends RuntimeException {

    public CouldNotVerifyException(){
        super();
    }

    public CouldNotVerifyException(String message){
        super(message);
    }
}
