package com.vv.carmensandiego.suspects;


import android.util.Log;

import com.vv.carmensandiego.util;

import java.util.List;
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

public class CarmenSandiego {

  final String name;
  final String sex;
  final String hobby;
  final String haircolor;
  final String feature;
  final String auto;
  final String age;
  final String height;
  final String weight;
  String[] clothing;
  String[] clothingColor;

  Boolean ladron = false;

  String stolenItem;
  List<String> visitedPlaces;
  List<Double> distancias;

  int random;

  public CarmenSandiego() {

    this.name = "Carmen Sandiego";
    this.sex = "mujer";
    this.age = "40";
    this.height = "1.80";
    this.weight = "60";

    this.haircolor = "red";
    this.clothing = new String[]{"boots", "coat", "hat"};
    this.clothingColor = new String[]{"red", "red", "red"};

    //random = new Random().nextInt((util.haircolors.length) + 1);
    //this.hair = util.haircolors[random];

    random = new Random().nextInt((util.hobbys.length));
    this.hobby = util.hobbys[random];

    random = new Random().nextInt((util.features.length));
    this.feature = util.features[random];

    random = new Random().nextInt((util.autos.length));
    this.auto = util.autos[random];

    for (int i = 0; i <= 2; i++) {
      random = new Random().nextInt((util.clothings.length));
      this.clothing[i] = util.clothings[random];
      random = new Random().nextInt((util.clothingColors.length));
      this.clothingColor[i] = util.clothingColors[random];
    }

    //GENERAR PAISES VISITADOS ALEATORIAMENTE DEPENDIENDO DEL NIVEL DEL INVESTIGADOR
    visitedPlaces = util.paisesVisitados("1");
    for(String place : visitedPlaces){
      Log.d(this.name,"Visit : " + place);
    }



  }


  public String getName()                 {    return name;                       }
  public String getSex()                  {    return sex;                        }
  public String getHobby()                {    return hobby;                      }
  public String getHaircolor()            {    return haircolor;                  }
  public String getFeature()              {    return feature;                    }
  public String getAuto()                 {    return auto;                       }
  public String getAge()                  {    return age;                        }
  public String getHeight()               {    return height;                     }
  public String getWeight()               {    return weight;                     }
  public String[] getClothing()           {    return clothing;                   }
  public String[] getClothingColor()      {    return clothingColor;              }

  public int getTotalPaises()             {   return this.visitedPlaces.size();     }
  public String getPaisVisitado(Integer index){   return this.visitedPlaces.get(index); }


  public void setStolenItem(String item)  {    this.stolenItem = item;  }
  public void setLadron()                 {    this.ladron = true;  }

}
