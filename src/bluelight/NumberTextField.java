package bluelight;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }
    
    public Integer getValue() {
    	try {
    		return Integer.parseInt(getText());
    	} catch (NumberFormatException ex) {
    		return null;
    	}
    }
    
    public void setValue(int val) {
    	this.setText(Integer.toString(val));
    }

    private boolean validate(String text)
    {
        return ("".equals(text) || text.matches("[0-9]"));
    }
}
