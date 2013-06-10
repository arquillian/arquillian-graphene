package org.jboss.arquillian.graphene.enricher;

import java.util.Comparator;

import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;

public class SearchContextTestEnricherPrecedenceComparator implements Comparator<SearchContextTestEnricher> {

    @Override
    public int compare(SearchContextTestEnricher o1, SearchContextTestEnricher o2) {
        if (o1.getPrecedence() > o2.getPrecedence()) {
            return -1;
        } else if (o1.getPrecedence() < o2.getPrecedence()) {
            return 1;
        } else {
            return 0;
        }
    }

}
