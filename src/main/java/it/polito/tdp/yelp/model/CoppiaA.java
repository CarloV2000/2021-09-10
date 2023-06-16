package it.polito.tdp.yelp.model;

public class CoppiaA {

	private Business b1;
	private Business b2;
	private Double distanzaKM;
	
	public CoppiaA(Business b1, Business b2, Double distanzaKM) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.distanzaKM = distanzaKM;
	}

	public Business getB1() {
		return b1;
	}

	public void setB1(Business b1) {
		this.b1 = b1;
	}

	public Business getB2() {
		return b2;
	}

	public void setB2(Business b2) {
		this.b2 = b2;
	}

	public Double getDistanzaKM() {
		return distanzaKM;
	}

	public void setDistanzaKM(Double distanzaKM) {
		this.distanzaKM = distanzaKM;
	}

	@Override
	public String toString() {
		return this.b1+"<--->"+this.b2+" ("+this.distanzaKM+") \n";
	}
	
	
}
