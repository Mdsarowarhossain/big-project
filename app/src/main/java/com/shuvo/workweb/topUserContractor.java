package com.shuvo.workweb;

public class topUserContractor {
	String Name, Earnings, Refer;

	public topUserContractor(String Name, String Earnings, String withdraw, String Refer) {
		this.Name = Name;
		int w = Integer.parseInt(withdraw);
		w = w * 100;
		int po = Integer.parseInt(Earnings);

		int p = w + po;
		this.Earnings = p + "";
		this.Refer = Refer;

	}
}