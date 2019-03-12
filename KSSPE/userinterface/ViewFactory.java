package userinterface;

import controller.Transaction;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, Transaction t)
	{
		if(viewName.equals("LoginView") == true)
		{
			return new LoginView(t);
		}
		else if(viewName.equals("ReceptionistView") == true)
		{
			return new ReceptionistView(t);
		}
		else if(viewName.equals("AddWorkerView") == true)
		{
			return new AddWorkerView(t);
		}
		else if(viewName.equals("AddBorrowerView") == true)
		{
			return new AddBorrowerView(t);
		}
		else if(viewName.equals("AddCategoryView") == true)
		{
			return new AddCategoryView(t);
		}
		else if(viewName.equals("SearchWorkerView") == true)
		{
			return new SearchWorkerView(t);
		}
		else if(viewName.equals("SearchCategoryView") == true)
		{
			return new SearchCategoryView(t);
		}
		else if(viewName.equals("WorkerCollectionView") == true)
		{
			return new WorkerCollectionView(t);
		}
		else if(viewName.equals("CategoryCollectionView") == true)
		{
			return new CategoryCollectionView(t);
		}
		else if(viewName.equals("UpdateWorkerView") == true)
		{
			return new UpdateWorkerView(t);
		}
		else if(viewName.equals("UpdateCategoryView") == true)
		{
			return new UpdateCategoryView(t);
		}
		else
			return null;
	}
}
