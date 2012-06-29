package org.jboss.arquillian.graphene.spi.components.numberSpinner;

import org.jboss.arquillian.graphene.spi.components.common.Component;

public interface NumberSpinnerComponent extends Component {
	
	void increase();
	
	void decrease();
	
	void fillInTheInput(Integer integer);
	
	Integer getValue();
}
