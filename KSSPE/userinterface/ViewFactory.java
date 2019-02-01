package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("LoginView") == true)
		{
			return new newLoginView(model);
		}
		else if(viewName.equals("ReceptionistView") == true)
		{
			return new ReceptionistView(model);
		}
		else if(viewName.equals("AddArticleTypeView") == true)
		{
			return new AddArticleTypeView(model);
		} 
		else
			return null;
	}
}
