package com.vv.carmensandiego;

import java.util.ArrayList;

public class UtilityClassSuspects {

  private static UtilityClassSuspects instance;

  private ArrayList<Suspects> list;

  public ArrayList<Suspects> getList() {
    return list;
  }

  public void setList(ArrayList<Suspects> list) {
    this.list = list;
  }

  private UtilityClassSuspects(){}

  public static UtilityClassSuspects getInstance(){
    if(instance == null){
      instance = new UtilityClassSuspects();
    }
    return instance;
  }

}
