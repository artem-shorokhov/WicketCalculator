package org.shorokhov.calculator;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

@SuppressWarnings("hiding")
public class CalculatorForm<Object> extends Form<Object> {
	
	private static final long serialVersionUID = 1L;

	private String expression;
	private String history;
	
	public CalculatorForm(String name) {
		
        super(name);
        

        
        
        
        add(new Button("evaluateButton") {
        	
			private static final long serialVersionUID = 1L;

			public void onSubmit() {
				
		        final TextField<String> expressionField = new TextField<String>("expression", Model.of(""));
				final TextArea<String> historyField = new TextArea<String>("history",  Model.of(""));
		        
		        expression = (String) expressionField.getModelObject();
				history = (String) historyField.getModelObject();
				
				String result;
				
				try {
					result = evaluate(expression);
				} catch(IllegalArgumentException e) {
					result = "NaN";
				}
				
				if (expression == null) {
					if (history == null) {
						history = "";
					} 
				} else {
					if (history == null) {
						history = String.format("%s%n= %s", expression, result);
					} else {
						history = String.format("%s\n%s\n= %s", history, expression, result);
					}
				}
				
				expressionField.setModel(Model.of(result));
				historyField.setModel(Model.of(history));
            }
        });
        
        Button clear = new Button("clearButton") {

			private static final long serialVersionUID = 1L;

			public void onSubmit() {
                
            }
        };
        clear.setDefaultFormProcessing(false);
        add(clear);
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
	    			result = result.divide(argument);
	    			break;    		
	    		default:
	    			throw new IllegalArgumentException();
    		}
    	}
    	    	
    	return result.toString();
    }

}
