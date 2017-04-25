package org.shorokhov.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Class represents calculator implementation using Apache Wicket.
 * 
 * It takes {@code String} expression as an input and calculates it.
 * Expression should consist of decimal numbers and operations
 * separated by spaces. Calculation is performed consequently.
 * In example: 2 + 2 * 2 would be calculated as 8.
 * If expression contains unrecognizable arguments and/or actions,
 * {@code NaN} would be shown as answer. Also it keeps track of
 * expresion history.
 * 
 * @author Artem Shorokhov
 */
@SuppressWarnings("serial")
public class Calculator extends WebPage {

	private static final int PRECISION = 10;

	/**
	 * Default constructor which is responsible for the form content.
	 */
	public Calculator() {

		// binding form elements 
    	final TextField<String> expressionField = new TextField<String>("expression", Model.of(""));
		final TextArea<String> historyField = new TextArea<String>("history",  Model.of(""));
		
		Form<?> form = new Form<Object>("form") {
			
			// "="-button action
			@Override
			public void onSubmit() {
				
				String expression = (String) expressionField.getModelObject();
				String history = (String) historyField.getModelObject();
				String result = "";
				
				// if-else blocks to handle corner-cases for history and input fields
				if (expression == null) {
					if (history == null) {
						history = "";
					} 
				} else {
					try {
						result = calculate(expression);
					} catch(IllegalArgumentException e) {
						result = e.getMessage();
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
		
		form.add(expressionField);
		form.add(historyField);

		Button clear = new Button("clearButton") {

			@Override
			public void onSubmit() {
            	expressionField.setModel(Model.of(""));
            }
        };
        
        clear.setDefaultFormProcessing(false);
        form.add(clear);
	
		add(form);
    }
  
	/**
	 * Method takes {@code String} expression as an input and calculates it.
	 * Expression should consist of decimal numbers and operations
	 * separated by spaces. Calculation is performed consequently.
	 * In example: 2 + 2 * 2 would be calculated as 8.
	 * 
	 * @param expression {@code String} representation of expression obtained from input field.
	 * @return {@code String} representation of expression calculation.
	 * @throws IllegalArgumentException if expression contains unrecognizable arguments and/or actions.
	 */
    private String calculate(String expression) {
    	
    	String[] arguments = expression.split(" ");
    	BigDecimal result = new BigDecimal(arguments[0]);
    	
    	for (int i = 1; i + 1 < arguments.length; i += 2) {
    		
    		BigDecimal argument;
    		
    		try {
    			argument = new BigDecimal(arguments[i + 1]);
    		} catch(NumberFormatException e) {
    			throw new IllegalArgumentException(String.format("NaN (argument '%s' is not a decimal number).", arguments[i + 1]));
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
	        		try {
	        			result = result.divide(argument, PRECISION, RoundingMode.HALF_UP);
	        		} catch(ArithmeticException e) {
	        			throw new IllegalArgumentException("NaN (division by 0).");
	        		}
	    			break;
	    		case ("mod"):
	    			result = result.remainder(argument);
	    			break;
	    		case ("pow"):
	    			try {
	    				result = result.pow(Integer.parseInt(arguments[i + 1]));
	    			} catch(NumberFormatException e) {
	        			throw new IllegalArgumentException(String.format("NaN (power '%s' is not an int).", arguments[i + 1]));
	        		}
	    			break;
	    		default:
	    			throw new IllegalArgumentException(String.format("NaN (operator '%s' is not supported).", arguments[i]));
    		}
    	}

    	// make output readable
    	DecimalFormat df = new DecimalFormat();
    	df.setMaximumFractionDigits(PRECISION);
    	df.setMinimumFractionDigits(0);
    	df.setGroupingUsed(false); 
    	
    	return df.format(result);
    }
}

