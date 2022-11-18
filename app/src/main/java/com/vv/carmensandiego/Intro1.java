package com.vv.carmensandiego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Intro1 extends AppCompatActivity implements FirebaseListener{

  String TAG = "INTRO 1";

  Intent mainIntent;

  Country country;
  ArrayList<Country> countries = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro1);


    Objects.requireNonNull(getSupportActionBar()).hide();

    mainIntent = new Intent(this, MainActivity.class);
    mainIntent.putExtra("EXTRA_FROM_INTENT", "INTRO");

    //CARGAR DATOS DE FIREBASE
    dataFromFirebaseCountries();

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int[] countryNumber = {0};
    //1. OBTENER NOMBRE Y DATOS BASICOS DE PAISES
    db.collection("paises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot document : task.getResult()) {
            //Log.d("dataFromFirebaseCountries", document.getId());
            countries.add(new Country());
            onSuccesFirebaseInitialData(document.getId(), countryNumber[0], document.getData());
            countryNumber[0]++;
          }
          onCompleteFirebaseData();
        } else {
          Log.d(TAG, "Error getting documents: ", task.getException());
          onFailFirebaseData();
        }
      }
    });
  }



  @Override
  public void onSuccesFirebaseInitialData(String name, Integer countryNumber, Map<String, Object> data) {

    countries.get(countryNumber).setFirebaseName(name);
    countries.get(countryNumber).setTAG((String) data.get("TAG"));
    countries.get(countryNumber).setAeropuerto((String) data.get("airport"));
    countries.get(countryNumber).setCapital((String) data.get("capital"));
    countries.get(countryNumber).setLatitud((String) data.get("latitud"));
    countries.get(countryNumber).setLongitud((String) data.get("longitud"));
    countries.get(countryNumber).setMoneda((String) data.get("moneda"));
    countries.get(countryNumber).setName((String) data.get("name"));
    countries.get(countryNumber).setPopulation((String) data.get("population"));
    countries.get(countryNumber).setRegion((String) data.get("region"));


    //2. OBTENER COLECCIONES DE PISTAS
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Query queryClues = db
      .collection("paises")
      .document(name)
      .collection("INFO");

    Task<QuerySnapshot> taskClues = queryClues.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot subdocument : task.getResult()) {
            onSuccesFirebaseInfoData(name, countryNumber, subdocument.getId(), subdocument.getData());
          }
          countries.add(country);
          Log.d("onSuccesFirebaseInitialData", "Country: " + countryNumber + "\n" + countries.get(countryNumber).countryToJSON());
        } else {
          Log.d(TAG, "Error getting " + "" + " : ", task.getException());
        }
      }
    });
  }

  @Override
  public void onSuccesFirebaseInfoData(String name, Integer countryNumber, String infoData, Map<String, Object> data) {
    for(String key : data.keySet()){
      //Log.d("onSuccesFirebaseInfoData", "key: " + key + " | value: " + data.get(key));
      if(infoData.contains("CLUES")){
        countries.get(countryNumber).setClue(key, (String) data.get(key));
      }
      else if(infoData.contains("COUNTRIES")){
        countries.get(countryNumber).setRelatedCountries(key, (String) data.get(key));
      }
      else if(infoData.contains("PLACES")){
        countries.get(countryNumber).setPlaces(key, (String) data.get(key));
      }
      else if(infoData.contains("STOLEN")){
        countries.get(countryNumber).setStolenObjs(key, (String) data.get(key));
      }
      else if(infoData.contains("TRIVIAS")){
        countries.get(countryNumber).setTrivias(key, (String) data.get(key));
      }
    }
  }

  //FUNCION QUE HACER CUANDO SE COMPLETA LA CARGA DE DATOS DE FIREBASE
  public void onCompleteFirebaseData(){
    UtilityClassCountries.getInstance().setList(countries);
    startActivity(mainIntent);
  }

  //FUNCION QUE HACE CUANDO FALLA LA CARGA DE DATOS DE FIREBASE
  public void onFailFirebaseData(){

  }

}