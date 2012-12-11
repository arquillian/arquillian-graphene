package org.jboss.arquillian.graphene.enricher;

import org.jboss.arquillian.graphene.spi.annotations.Page;

public class AbstractTest2<T,E>{

    @Page
    private T page1;
    
    @Page
    private E page2;

    public T getPage1() {
        return page1;
    }

    public void setPage1(T page1) {
        this.page1 = page1;
    }

    public E getPage2() {
        return page2;
    }

    public void setPage2(E page2) {
        this.page2 = page2;
    }
}
