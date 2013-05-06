package edu.strathmore.egeza.models;

public class ModelClients {

	private String clientId,fname,lname,nationalId;
	private  int counter=0;
	private boolean selected;
	
	public ModelClients(String id,String fname,String lname, String natId) {
		this.fname = fname;
		this.lname=lname;
		this.nationalId = natId;
		this.clientId = id;
	}
	
	public void setClientId(String id) {
		this.clientId=id;
	}
	public String getClientId() {
		return clientId;
	}
	
	public void setFirstName(String fname) {
		this.fname=fname;
	}
	public String getFirstName() {
		return fname;
	}
	
	public void setLastName(String lname) {
		this.lname=lname;
	}
	public String getLastName() {
		return lname;
	}
	
	public void setNationalId(String natid) {
		this.nationalId=natid;
	}
	public String getNationalId() {
		return nationalId;
	}

}
