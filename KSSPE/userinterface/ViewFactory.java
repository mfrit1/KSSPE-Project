package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("ReceptionistView") == true)
		{
			return new ReceptionistView(model);
		} 
		else
			return null;
	}
}
