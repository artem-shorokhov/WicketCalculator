package org.shorokhov.calculator;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Common Apache Wicket loader class.
 * 
 * @author Artem Shorokhov
 *
 */
public class CalculatorApplication extends WebApplication {
	
    public CalculatorApplication() {}

    /**
     * @return {@code Calculator.class} as homepage processor. 
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return Calculator.class;
    }
}