package com.vv.carmensandiego;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Country {

  public String firebaseName;
  public String TAG = "";
  public String name;
  public String region;
  public String bandera;
  public String moneda;
  public String capital;
  public String aeropuerto;
  public String latitud;
  public String longitud;
  public String population;


  public Map<String, String> clues = new HashMap<>();
  public Map<String, String> usedClues = new HashMap<>();
  public Map<String, String> stolenObjs = new HashMap<>();
  public Map<String, String> relatedCountries = new HashMap<>();
  public Map<String, String> places = new HashMap<>();
  public Map<String, String> trivias = new HashMap<>();
  public Map<String, String> usedTrivias = new HashMap<>();

  public void setFirebaseName(String name){ this.firebaseName = name; }
  public void setTAG(String TAG) { this.TAG = TAG;  }
  public void setName(String nombre){  this.name = nombre;  }
  public void setRegion(String region){ this.region = region;  }
  public void setBandera(String bandera){ this.bandera = bandera;    }
  public void setMoneda(String moneda){ this.moneda = moneda;    }
  public void setCapital(String capital){ this.capital = capital;  }
  public void setAeropuerto(String aeropuerto){ this.aeropuerto = aeropuerto;  }
  public void setLatitud(String latitud){ this.latitud = latitud;  }
  public void setLongitud(String longitud){  this.longitud = longitud;  }
  public void setPopulation(String population){ this.population = population; }

  public void setClue(String key, String clue){
    this.clues.put(key, clue);
    this.usedClues.put(key, "false");
  }

  public void setTrivias(String key, String trivia){
    this.trivias.put(key, trivia);
    this.usedTrivias.put(key, "false");
  }
  public void setStolenObjs(String key, String object){
    this.stolenObjs.put(key, object);
  }
  public void setRelatedCountries(String key, String country){
    this.relatedCountries.put(key, country);
  }
  public void setPlaces(String key, String place){
    this.places.put(key, place);
  }


  public String getFirebaseName(){ return firebaseName; }
  public String getTAG(){ return TAG;  }
  public String getName(){ return name;  }
  public String getRegion(){ return region;  }
  public String getBandera(){ return bandera;  }
  public String getMoneda(){ return moneda;  }
  public String getCapital(){ return capital; }
  public String getAeropuerto(){ return aeropuerto; }
  public String getLatitud(){ return latitud; }
  public String getLongitud(){ return longitud; }
  public String getPopulation(){ return population; }

  public String getClue(String key){
    this.usedClues.put(key, "true");
    return clues.get(key);
  }
  public String getUsedClue(String key){  return usedClues.get(key);  }

  public Integer getCluesProd(){
    int prod = 0;
    for(String key : clues.keySet()){
      if(key.contains("prod")){
        prod++;
      }
    }
    return prod;
  }

  public String getTrivia(String key) {
    this.usedTrivias.put(key, "true");
    return trivias.get(key);
  }
  public String getUsedTrivia(String key){ return usedTrivias.get(key);  }
  public int getTotalTrivias(){ return trivias.size(); }

  public String getStolenObjs(Integer index){
    String[] values = stolenObjs.values().toArray(new String[0]);
    return (String) values[index];
  }
  public Integer getTotalStolenObjects(){ return stolenObjs.size(); }

  public Integer getTotalRelatedCountries(){ return relatedCountries.size(); }
  public String[] getAllRelatedCountries() {
    return relatedCountries.values().toArray(new String[0]);
  }
  public String getRelatedCountry(Integer index) {   return relatedCountries.get(index); }

  public int getTotalPlaces(){ return places.size(); }
  public String getPlace(String key){ return places.get(key); }
  public String[] getPlaces(){
    return places.values().toArray(new String[0]);
  }


  public Map<String, String> basicDataToMap(){
    Map<String, String> basicData = new HashMap<>();
    basicData.put("Name", this.name);
    basicData.put("COI", this.TAG);
    basicData.put("Region", this.region);
    basicData.put("Population",this.population);
    basicData.put("Capital", this.capital);
    basicData.put("Latitud", this.latitud);
    basicData.put("longitud", this.longitud);
    basicData.put("Moneda",this.moneda);
    return basicData;
  }


  public String countryToJSON(){
    Map<String, Map<String, String>> countryMapData = new HashMap<>();
    countryMapData.put("DATA", basicDataToMap());
    countryMapData.put("CLUES", this.clues);
    countryMapData.put("COUNTRIES", this.relatedCountries);
    countryMapData.put("PLACES", this.places);
    countryMapData.put("STOLENOBJECTS", this.stolenObjs);
    countryMapData.put("TRIVIAS", this.trivias);

    Map<String, Map<String, Map<String, String>>> countryMap = new HashMap<>();
    countryMap.put(this.firebaseName, countryMapData);

    Gson gson = new Gson();
    return gson.toJson(countryMap);
  }



}
