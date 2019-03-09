package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class CategoryTableModel
{
	private final SimpleStringProperty barcodePrefix;
	private final SimpleStringProperty name;
	private final SimpleStringProperty status;

	//----------------------------------------------------------------------------
	public CategoryTableModel(Vector<String> atData)
	{
		barcodePrefix =  new SimpleStringProperty(atData.elementAt(0));
		name =  new SimpleStringProperty(atData.elementAt(1));
		status =  new SimpleStringProperty(atData.elementAt(2));
	}

	//----------------------------------------------------------------------------
	public String getBarcodePrefix() {
		return barcodePrefix.get();
	}

	//----------------------------------------------------------------------------
	public void setBarcodePrefix(String pref) {
		barcodePrefix.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getName() {
		return name.get();
	}

	//----------------------------------------------------------------------------
	public void setName(String desc) {
		name.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getStatus() {
		return status.get();
	}

	//----------------------------------------------------------------------------
	public void setStatus(String stat)
	{
		status.set(stat);
	}
}
