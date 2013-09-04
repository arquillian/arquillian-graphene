package org.jboss.arquillian.graphene.condition;

import org.openqa.selenium.support.ui.ExpectedCondition;

public interface StringConditionFactory<T extends StringConditionFactory> extends BooleanConditionFactory<T> {

    ExpectedCondition<Boolean> contains(String expected);

    ExpectedCondition<Boolean> equalTo(String expected);

}
