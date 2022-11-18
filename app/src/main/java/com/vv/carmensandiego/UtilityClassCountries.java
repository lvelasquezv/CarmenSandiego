package com.vv.carmensandiego;

import java.util.ArrayList;

public class UtilityClassCountries {

  private static UtilityClassCountries instance;

  private ArrayList<Country> list;

  public ArrayList<Country> getList() {
    return list;
  }

  public void setList(ArrayList<Country> list) {
    this.list = list;
  }

  private UtilityClassCountries(){}

  public static UtilityClassCountries getInstance(){
    if(instance == null){
      instance = new UtilityClassCountries();
    }
    return instance;
  }

}
