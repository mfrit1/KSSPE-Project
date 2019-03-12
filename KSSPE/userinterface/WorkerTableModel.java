package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class WorkerTableModel
{
	private final SimpleStringProperty bannerId;
	private final SimpleStringProperty credential;
	private final SimpleStringProperty password;
	private final SimpleStringProperty status;
	private final SimpleStringProperty dateAdded;
	private final SimpleStringProperty dateLastUpdated;


	//----------------------------------------------------------------------------
	public WorkerTableModel(Vector<String> atData)
	{
		bannerId =  new SimpleStringProperty(atData.elementAt(0));
		credential =  new SimpleStringProperty(atData.elementAt(1));
		password =  new SimpleStringProperty(atData.elementAt(2));
		status =  new SimpleStringProperty(atData.elementAt(3));
		dateAdded =  new SimpleStringProperty(atData.elementAt(4));
		dateLastUpdated =  new SimpleStringProperty(atData.elementAt(5));
	}

	//----------------------------------------------------------------------------
	public String getBannerId() {
		return bannerId.get();
	}

	//----------------------------------------------------------------------------
	public String getCredential() {
		return credential.get();
	}

	//----------------------------------------------------------------------------
	public String getPassword() {
		return password.get();
	}

	//----------------------------------------------------------------------------
	public String getStatus() {
		return status.get();
	}

	//----------------------------------------------------------------------------
	public String getDateAdded() {
		return dateAdded.get();
	}

	//----------------------------------------------------------------------------
	public String getDateLastUpdated() {
		return dateLastUpdated.get();
	}

	//----------------------------------------------------------------------------
	public void setCredential(String cred) {
		credential.set(cred);
	}

	//----------------------------------------------------------------------------
	public void setStatus(String stat)
	{
		status.set(stat);
	}

	//----------------------------------------------------------------------------
	public void setDateLastUpdated(String date)
	{
		dateLastUpdated.set(date);
	}
}
