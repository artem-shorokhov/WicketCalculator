package org.shorokhov.calculator;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class CalculatorApplication extends WebApplication {
	
    public CalculatorApplication() {
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return Calculator.class;
    }
}