package com.vv.carmensandiego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Intro1 extends AppCompatActivity implements FirebaseListener{

  String TAG = "INTRO 1";

  Intent mainIntent;

  ArrayList<Country> countries = new ArrayList<>();
  ArrayList<Suspects> vileBand = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro1);


    Objects.requireNonNull(getSupportActionBar()).hide();

    mainIntent = new Intent(this, MainActivity.class);
    mainIntent.putExtra("EXTRA_FROM_INTENT", "INTRO");

    //CARGAR DATOS DE FIREBASE
    dataFromFirebaseCountries();
    dataFromFirebaseSuspects();

    MotionLayout introMotionLayout = findViewById(R.id.IntroMotionLayout);
    introMotionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
      @Override
      public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

      }

      @Override
      public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

      }

      @Override
      public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

      }

      @Override
      public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

      }
    });
  }

  //FUNCION QUE TRAERA LA INFORMACION SOBRE CADA PAIS, GUARDADA EN FIREBASE
  public void dataFromFirebaseCountries(){

    //Log.d("INTRO1 dataFromFirebaseCountries", "INIT");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int[] countryNumber = {0};
    //1. OBTENER NOMBRE Y DATOS BASICOS DE PAISES
    db.collection("paises").get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        for (QueryDocumentSnapshot document : task.getResult()) {
          //Log.d("dataFromFirebaseCountries", document.getId());
          countries.add(new Country());
          onSuccesFirebaseInitialData(document.getId(), countryNumber[0], document.getData());
          countryNumber[0]++;
        }
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
        onFailFirebaseData("Countries");
      }
    });
  }


  @Override
  public void onSuccesFirebaseInitialData(String name, Integer countryNumber, Map<String, Object> data) {

    countries.get(countryNumber).setFirebaseName(name);
    for (String key : data.keySet()) {
      if (key.contains("flag") || key.contains("prod")) {
        countries.get(countryNumber).setClue(key, (String) data.get(key));
      } else if (key.contains("related")) {
        countries.get(countryNumber).setRelatedCountries(key, (String) data.get(key));
      } else if (key.contains("place")) {
        countries.get(countryNumber).setPlaces(key, (String) data.get(key));
      } else if (key.contains("stolen")) {
        countries.get(countryNumber).setStolenObjs(key, (String) data.get(key));
      } else if (key.contains("triv")) {
        countries.get(countryNumber).setTrivias(key, (String) data.get(key));
      } else if (key.contains("TAG")) {
        countries.get(countryNumber).setTAG((String) data.get(key));
      } else if (key.contains("airport")) {
        countries.get(countryNumber).setAeropuerto((String) data.get(key));
      } else if (key.contains("capital")) {
        countries.get(countryNumber).setCapital((String) data.get(key));
      } else if (key.contains("latitud")) {
        countries.get(countryNumber).setLatitud((String) data.get(key));
      } else if (key.contains("longitud")) {
        countries.get(countryNumber).setLongitud((String) data.get(key));
      } else if (key.contains("moneda")) {
        countries.get(countryNumber).setMoneda((String) data.get(key));
      } else if (key.contains("name")) {
        countries.get(countryNumber).setName((String) data.get(key));
      } else if (key.contains("population")) {
        countries.get(countryNumber).setPopulation((String) data.get(key));
      } else if (key.contains("region")) {
        countries.get(countryNumber).setRegion((String) data.get(key));
      }
    }
    Log.d("onSuccesFirebaseInitialData", "Country: " + countryNumber + "\n" + countries.get(countryNumber).countryToJSON());
  }


  //FUNCION QUE CARGA LOS DATOS DE LOS SOSPECHOSOS EN FIREBASE
  public void dataFromFirebaseSuspects(){
    //Log.d("INTRO1 dataFromFirebaseSuspects", "INIT");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int[] suspectNumber = {0};
    //1. OBTENER NOMBRE Y DATOS BASICOS DE PAISES
    db.collection("suspects").get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        for (QueryDocumentSnapshot document : task.getResult()) {
          //Log.d("dataFromFirebaseCountries", document.getId());
          vileBand.add(new Suspects());
          onSuccesFirebaseSuspects(document.getId(), suspectNumber[0], document.getData());
          suspectNumber[0]++;
        }
        onCompleteFirebaseData();
      } else {
        Log.d(TAG, "Error getting documents: ", task.getException());
        onFailFirebaseData("vileband");
      }
    });
  }

  @Override
  public void onSuccesFirebaseSuspects(String name, Integer suspectNumber, Map<String, Object> data) {
    vileBand.get(suspectNumber).setName(name);
    boolean randomClothing = true;
    for(String key : data.keySet()){
      //Log.d("INTRO1 onSuccesFirebaseSuspects", "Suspect " + name + " " + key + " " + data.get(key));
      if(key.contains("sex")){
        vileBand.get(suspectNumber).setSex((String) data.get(key));
      }else if(key.contains("age")){
        vileBand.get(suspectNumber).setAge((String) data.get(key));
      }else if(key.contains("height")) {
        vileBand.get(suspectNumber).setHeight((String) data.get(key));
      }else if(key.contains("weight")) {
        vileBand.get(suspectNumber).setWeight((String) data.get(key));
      }else if(key.contains("haircolor")) {
        vileBand.get(suspectNumber).setHaircolor((String) data.get(key));
      }else if(key.contains("hobby")) {
        vileBand.get(suspectNumber).setHobby((String) data.get(key));
      }else if(key.contains("favoritefood")) {
        vileBand.get(suspectNumber).setFavoriteFood((String) data.get(key));
      }else if(key.contains("feature")) {
        vileBand.get(suspectNumber).setFeature((String) data.get(key));
      }else if(key.contains("auto")) {
        vileBand.get(suspectNumber).setAuto((String) data.get(key));
      }else if(key.contains("clothing")) {
        randomClothing = false;
        vileBand.get(suspectNumber).setClothing((String) data.get(key));
      }else if(key.contains("color")) {
        vileBand.get(suspectNumber).setClothingColor((String) data.get(key));
      }
    }

    if(randomClothing){
      Log.d("INTRO1 onSuccesFirebaseSuspects", "Random Clothings");
      vileBand.get(suspectNumber).setClothingRandom();
    }

    Log.d("onSuccesFirebaseSuspects", "Suspect: " + suspectNumber + "\n" + vileBand.get(suspectNumber).suspectToJSON());
  }


  //FUNCION QUE HACER CUANDO SE COMPLETA LA CARGA DE DATOS DE FIREBASE
  public void onCompleteFirebaseData(){
    UtilityClassCountries.getInstance().setList(countries);
    UtilityClassSuspects.getInstance().setList(vileBand);
    startActivity(mainIntent);
  }

  //FUNCION QUE HACE CUANDO FALLA LA CARGA DE DATOS DE FIREBASE
  public void onFailFirebaseData(String from){
    //CARGAR UN JASON CON LA ULTIMA INFORMACION CARGADA DE CADA PAIS
    //TODO CARGAR INFORMACION CUANDO FALLA LA CONEXION CON FIREBASE POSIBLEMENTE DETENER LA CARGA
  }

}