package com.vv.carmensandiego;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para la creacion de los detectives
 * Cada detective tiene un nombre y un rango guardado en sheredPreferences
 * para ganar el juego debe llegar hasta donde esta el ladron antes que se acabe el tiempo o el
 * dinero
 */

public class Detective {

  public String name;
  public String rank;
  public Integer money;
  public Integer hour = 9;
  public Integer totalHours = 0;
  public String hourToShow = "9";
  public String ampm = "A.M.";
  public String dia = "LUNES";
  public String nivel;
  public List<String> paisesVisitados = new ArrayList<String>();
  public List<Boolean> paisesEnLaRutaLadron = new ArrayList<>();
  public Boolean encontrado = false;
  public String sospechoso = "";

  public Detective() {}

  public void changeTime(Integer hoursToReduce){
    this.hour -= hoursToReduce;
  }
  public void changeMoney(Integer moneyToReduce){
    this.money -= moneyToReduce;
  }

  public String getName() {    return name;  }
  public String getRank() {    return rank;  }
  public Integer getMoney() {    return money;  }
  public Integer getHour() {    return hour;  }
  public String getAMPM() {  return ampm;  }
  public String getDia(){       return dia;   }
  public String getNivel(){   return nivel;   }
  public int getTotalPaisesVisitados() { return paisesVisitados.size(); }
  public String getPaisVisitado(Integer index){ return paisesVisitados.get(index); }
  public List<String> getPaisesVisitados() {return paisesVisitados; }

  public String getTime(){  return dia + " " + hourToShow + " " + ampm; }
  public String getUltimoPaisRutaLadron(){
    int index = 0;
    for(int i = 0; i < paisesEnLaRutaLadron.size(); i++){
      if(paisesEnLaRutaLadron.get(i)){
        index = i;
      }
    }
    return paisesVisitados.get(index);
  }

  public Boolean getEncontrado(){ return encontrado; }
  public Integer getTotalHours(){ return totalHours; }


  public void setName(String name){ this.name = name;   }
  public void setRank(String rank){ this.rank = rank; }
  public void setMoney(Integer money) {  this.money = money; }
  public void setHour(Integer hour) {  this.hour = hour; }
  public void setHourToShow(String hour){ this.hourToShow = hour; }
  public void setAMPM(String ampm){  this.ampm = ampm;  }
  public void setDia(String dia){   this.dia = dia; }
  public void setNivel(String nivel){ this.nivel = nivel; }

  public void addPaisVisitado(String pais){ this.paisesVisitados.add(pais); }
  public void setPaisesEnLaRutaLadron(Boolean rutaLadron){ this.paisesEnLaRutaLadron.add(rutaLadron); }

  public void setEncontrado(Boolean encontrado){ this.encontrado = encontrado; }

  public void addTime(Integer add){ this.totalHours += add; }
  public void initTotalHours(){ this.totalHours = 0; }


  public void initNewGame(){
    this.hour = 9;
    this.totalHours = 0;
    this.hourToShow = "9";
    this.ampm = "A.M.";
    this.dia = "LUNES";
    this.paisesVisitados = new ArrayList<String>();
    this.paisesEnLaRutaLadron = new ArrayList<>();
    this.encontrado = false;
  }


}
