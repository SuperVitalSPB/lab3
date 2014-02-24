package ru.supervital.lab3;

import java.util.ArrayList;


public class Rate {
//    public ImageView imageView;
	public String Code;
    public double Rate;
    public String Nominal;
    public String Name;
    public ArrayList<CurrDynam> Dynam;
    public String ID;
    public String NumCode;
    
    
    
    public Rate(String Code, double Rate, String Nominal, String Name){
    	super();
    	this.Code = Code;
    	this.Rate = Rate;
    	this.Nominal = Nominal;
    	this.Name = Name;    	
    }
    
    
}


