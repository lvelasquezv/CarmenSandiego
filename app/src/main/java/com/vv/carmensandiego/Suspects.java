package com.vv.carmensandiego;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Sospechosos de los robos divididos en:
 * sex = {"mujer","hombre"}
 * hobby = {"tennis", "music", "mt climbing", "skydiving", "swimming", "croquet", "football"}
 * hair = {"brown","blond","red","black"}
 * feature = {"limps", "ring", "tattoo", "scar", "jewelry" }
 * auto = {"convertible","limousine","race car", "motorcycle"}
 * clothings = {"Jacket", "jeans", "suit", "cap", "scarf", "boots", "coat", "tennis"};
 * stolenItem & visitedPlaces se crean de forma aleatoria dependiendo dependiendo del recorrido realizado
 * por el ladron
 */
public class Suspects {

  public String name;
  public String sex;
  public String hobby;
  public String haircolor;
  public String feature;
  public String favoriteFood;
  public String auto;
  public String age;
  public String height;
  public String weight;
  public String lastPlace;

  public List<String> clothing = new ArrayList<>();
  public List<String> clothingColor = new ArrayList<>();

  public Boolean thief = false;
  public String stolenItem = "";
  public List<String> visitedCountries;
  public List<Double> distancias = new ArrayList<>();

  public void setClothingRandom(){
    int random;
    for (int i = 0; i <= 2; i++) {
      random = new Random().nextInt((Util.clothings.size()));
      this.clothing.add(Util.clothings.get(random));
      random = new Random().nextInt((Util.clothingColors.size()));
      this.clothingColor.add(Util.clothingColors.get(random));
    }
  }



  public void setName(String name) {    this.name = name;  }
  public void setSex(String sex) {    this.sex = sex;  }
  public void setAge(String age) {    this.age = age;  }
  public void setHeight(String height) {    this.height = height;  }
  public void setWeight(String weight) {    this.weight = weight;  }
  public void setHaircolor(String haircolor) {    this.haircolor = haircolor;  }
  public void setHobby(String hobby) {    this.hobby = hobby;  }
  public void setFavoriteFood(String favoriteFood) {    this.favoriteFood = favoriteFood;  }
  public void setFeature(String feature) {    this.feature = feature;  }
  public void setAuto(String auto) {    this.auto = auto;  }

  public void setClothing(String cloth){      this.clothing.add(cloth);  }
  public void setClothingColor(String color){    this.clothingColor.add(color);  }

  public void setThief() {    this.thief = true;  }
  public void setStolenItem(String stolenItem) {    this.stolenItem = stolenItem;  }
  public void setDistancias(List<Double> distancias) {    this.distancias = distancias;  }

  public void setLastPlace(String lastPlace){ this.lastPlace = lastPlace; }


  public String getName() {    return name;  }
  public String getSex() {    return sex;  }
  public String getHobby() {    return hobby;  }
  public String getHaircolor() {    return haircolor;  }
  public String getFeature() {    return feature;  }
  public String getFavoriteFood() {    return favoriteFood;  }
  public String getAuto() {    return auto;  }
  public String getAge() {    return age;  }
  public String getHeight() {    return height;  }
  public String getWeight() {    return weight;  }
  public Integer getTotalClothing(){  return clothing.size(); }
  public String getClothing(Integer index) {    return clothing.get(index);  }
  public String getClothingColor(Integer index) {    return clothingColor.get(index);  }
  public Boolean getThief() {    return thief;  }
  public String getStolenItem() {    return stolenItem;  }
  public List<String> getVisitedCountries() {    return visitedCountries;  }
  public String getVisitedCountry(Integer index){ return visitedCountries.get(index); }
  public int getTotalVisitedCountries() { return visitedCountries.size(); }
  public List<Double> getDistancias() {  return distancias;  }

  public String getLastPlace(){ return lastPlace; }


  public void setVisitedCountries(int level, List<String> countries){
    //INICIALIZAR
    visitedCountries = new ArrayList<>();

    int numberCountries;
    if(level == 1){
      numberCountries = 4;
    }else if(level == 2){
      numberCountries = 5;
    }else if(level == 3){
      numberCountries = 6;
    }else if(level == 4){
      numberCountries = 7;
    }else{
      numberCountries = 4;
    }

    //Log.d("Suspects setVisitedCountries", "L117 visitedCountries = " + visitedCountries);
    int random;
    String country;
    int addCountry = 0;
    while (addCountry < numberCountries){
      random = new Random().nextInt(countries.size());
      country = countries.get(random);
      if(!visitedCountries.contains(country)){ visitedCountries.add(country); addCountry++;}
    }

  }

  //BASIC DATA TO MAP
  public Map<String, String> basicValuesToMap(){
    Map<String, String> datos = new HashMap<>();
    datos.put("name", name);
    datos.put("sex", sex);
    datos.put("hobby", hobby);
    datos.put("haircolor", haircolor);
    datos.put("feature", feature);
    datos.put("auto", auto);

    return datos;
  }



  //VALUES TO JSON
  public String suspectToJSON(){

    Map<String, Map<String, String>> resumenMap = new HashMap<>();

    Map<String, String> initialData = new HashMap<>();
    initialData.put("name", name);
    initialData.put("sex", sex);
    initialData.put("hobby", hobby);
    initialData.put("haircolor", haircolor);
    initialData.put("feature", feature);
    initialData.put("favoriteFood", favoriteFood);
    initialData.put("auto", auto);
    initialData.put("age", age);
    initialData.put("height", height);
    initialData.put("weight", weight);
    initialData.put("Thief", Boolean.toString(thief));
    resumenMap.put("generalData", initialData);

    Map<String, String> clothingMap = new HashMap<>();
    Log.d("suspectToJSON", "Clothings " + clothing);
    for(int i = 0; i < clothing.size(); i++){
      clothingMap.put(clothing.get(i), clothingColor.get(i));
    }
    resumenMap.put("Clothing", clothingMap);

    Map<String, String> stolenItemMap = new HashMap<>();
    stolenItemMap.put("item", stolenItem);

    resumenMap.put("stolenItem", stolenItemMap);

    if(visitedCountries != null){
      if(!visitedCountries.isEmpty()){
        Map<String, String> countriesMap = new HashMap<>();
        for(int i = 0; i < visitedCountries.size(); i++){
          countriesMap.put(String.valueOf(i), visitedCountries.get(i));
          resumenMap.put("countries", countriesMap);
        }
      }
    }

    Map<String, Map<String, Map<String, String>>> suspectJSON = new HashMap<>();
    suspectJSON.put(name, resumenMap);

    Gson gson = new Gson();
    return gson.toJson(suspectJSON);

  }
}
