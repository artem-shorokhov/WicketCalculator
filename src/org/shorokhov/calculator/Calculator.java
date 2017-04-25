package org.shorokhov.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;


public class Calculator extends WebPage {

	private static final long serialVersionUID = 1L;
	private static final int PRECISION = 10;

	public Calculator() {
 	
    	final TextField<String> expressionField = new TextField<String>("expression", Model.of(""));
		final TextArea<String> historyField = new TextArea<String>("history",  Model.of(""));
		
		Form<?> form = new Form<Object>("form") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				
				String expression = (String) expressionField.getModelObject();
				String history = (String) historyField.getModelObject();
				String result = "";
				
				if (expression == null) {
					if (history == null) {
						history = "";
					} 
				} else {
					try {
						result = evaluate(expression);
					} catch(IllegalArgumentException e) {
						result = "NaN";
					}
					if (history == null) {
						history = String.format("%s%n= %s", expression, result);
					} else {
						history = String.format("%s\n%s\n= %s", history, expression, result);
					}
				}
				
				expressionField.setModel(Model.of(result));
				historyField.setModel(Model.of(history));
			}
		};
		
		Button clear = new Button("clearButton") {

			private static final long serialVersionUID = 1L;

			public void onSubmit() {
            	expressionField.setModel(Model.of(""));
            }
        };
        
        clear.setDefaultFormProcessing(false);
        form.add(clear);
		
		form.add(expressionField);
		form.add(historyField);
		add(form);
    }
    
    private String evaluate(String expression) {
    	
    	String[] arguments = expression.split(" ");
    	BigDecimal result = new BigDecimal(arguments[0]);
    	
    	for (int i = 1; i + 1 < arguments.length; i += 2) {
    		
    		BigDecimal argument;
    		
    		try {
    			argument= new BigDecimal(arguments[i + 1]);
    		} catch(NumberFormatException e) {
    			throw new IllegalArgumentException();
    		}
    		
    		switch(arguments[i]) {
	    		case ("+"):
	    			result = result.add(argument);
	    			break;
	    		case ("-"):
	    			result = result.subtract(argument);
	    			break;
	    		case ("*"):
	    			result = result.multiply(argument);
	    			break;
	    		case ("/"):
	    			result = result.divide(argument, PRECISION, RoundingMode.HALF_UP);
	    			break;    		
	    		default:
	    			throw new IllegalArgumentException();
    		}
    	}
    	    	
    	return result.toString();
    }
}

