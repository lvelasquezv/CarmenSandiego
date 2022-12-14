package com.vv.carmensandiego;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Carmen Sandiego
 */

public class MainActivity extends AppCompatActivity implements ThreadReloj{

  String TAG = "MAIN";
  SharedPreferences sharedPref;
  SharedPreferences.Editor editor;

  //INTERFACE IZQUIERDO
  LinearLayout llIzquierdo;

  //INTEFACE IZQUIERDO SUPERIOR
  LinearLayout llPaisActualYHora;
  TextView tvPaisActual;
  TextView tvHoraActual;

  //INTERFACE IZQUIERDO INFERIOR
  RelativeLayout rlIzqInf;
  //TRIVIAS, IMAGEN DEL PAIS
  ScrollView svImageTrivias;
  LinearLayout llImageCountry;
  TextView tvTextTrivias;
  ImageView ivCountry;

  //MISION ACTUAL
  ScrollView svIntroMision;
  LinearLayout llIntroYMision;
  TextView tvWritingIntroSaludo;
  EditText ETdetectiveName;
  TextView tvMision;
  Button btIniciarMision;

  //REPORTE INTERPOL
  ScrollView svReporteInterpol;
  TextView tvReporteInterpol;

  //INTEFACE DERECHO
  LinearLayout llDerecho;
  RelativeLayout rlDer;
  //NOTAS DE LA INVESTIGACION
  TextView tvInfoInvestigacion;
  //BASE DE DATOS INTERPOL
  TextView tvIntroSuspectTitulo;
  ScrollView svInfoSuspects;
  AutoCompleteTextView actvSex;
  AutoCompleteTextView actvHobby;
  AutoCompleteTextView actvHair;
  AutoCompleteTextView actvFeature;
  AutoCompleteTextView actvAuto;
  Button btBuscarInterpol;

  //INTERFACE INFERIOR OPCIONES
  GridLayout glMasterOptions;
  Button btConexiones;
  Button btViajar;
  Button btInvestigar;
  Button btInterpol;

  //VARIABLE DE INICIO SOSPECHOSOS
  ArrayList<Suspects> objetosSuspects;
  int idLadron;
  List<String> paisesVisitadosLadron;
  String objetoRobado;
  List<Map<String, String>> sospechosos;

  //VARIABLES PAISES
  Map<String, Integer> nombrePaises;
  List<String> lugaresInvestigar;
  ArrayList<Country> objetosPaises;

  //VARIABLES INVESTIGADOR
  String paisActual;
  Detective detective;
  List<String> destinos;
  List<String> nomDestinos;
  int regresarARuta = 0;

  //OPCIONES DEL JUEGO
  //Thread threadReloj = null;
  //Handler handler = null;
  //Runnable mRunnableReloj = null;
  final Executor mExecutor = Executors.newSingleThreadExecutor(); // change according to your requirements
  final Handler mHandler = new Handler(Looper.getMainLooper());


  public MainActivity() {
    super();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Objects.requireNonNull(getSupportActionBar()).hide();

    sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    editor = sharedPref.edit();

    //INICIALIZAR VARIABLES DE INTERFACE IZQUIERDO
    llIzquierdo = findViewById(R.id.LLIzquierdo);
    //SUPERIOR
    llPaisActualYHora = findViewById(R.id.llPaisActualYHora);
    tvPaisActual = findViewById(R.id.TVPaisActual);
    tvHoraActual = findViewById(R.id.TVHoraActual);

    //INFERIOR
    rlIzqInf = findViewById(R.id.RLIzqInf);
    //LINEAR LAYOUT IMAGE COUNTRY - TRIVIAS
    svImageTrivias = findViewById(R.id.SVImageTrivias);
    llImageCountry = findViewById(R.id.LLImageCountry);
    tvTextTrivias = findViewById(R.id.tvTextTrivias);
    ivCountry = findViewById(R.id.ivCountry);

    //btNuevaMision = findViewById(R.id.btNuevaMision);
    //btAceptarNuevaMision = findViewById(R.id.btAceptarNuevaMision);

    //INTRO & MISION
    svIntroMision = findViewById(R.id.SVIntroDetective);
    llIntroYMision = findViewById(R.id.LLIntroAndMision);
    tvWritingIntroSaludo = findViewById(R.id.TVWriterIntroSaludo);
    ETdetectiveName = findViewById(R.id.ETDetectiveName);
    ETdetectiveName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE){
          // Your action on done
          Log.d("MAIN", "Detective");
          initGame();
        }
        return false;
      }
    });
    tvMision = findViewById(R.id.TVIntroMision);
    btIniciarMision = findViewById(R.id.btIniciar);
    btIniciarMision.setTag("ACEPTAR");
    btIniciarMision.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String buttonTag = view.getTag().toString();
        Log.d("MAIN button NuevaMision", buttonTag);
        if(buttonTag.contains("ACEPTAR")){
          //ESCONDER VIEWS
          initInterfaceTriviasAndImage();
          setPaisActual();
          setHoraActual();
          setTrviasCountry();
        }else if(buttonTag.contains("NUEVA_MISION")){
          initNewMision();
          nuevaMision();
          //REINICIAR VALORES DETECTIVE
          detective.initNewGame();
        }else if(buttonTag.contains("ACEPTAR_MISION")){
          setPaisActual();
          setHoraActual();
          //ACTIVAR O DESACTIVAR VIEWS
          initInterfaceTriviasAndImage();
          //SET TRIVIAS
          setTrviasCountry();
        }
      }
    });
    btIniciarMision.setVisibility(View.INVISIBLE);

    //REPORTE INTERPOL
    svReporteInterpol = findViewById(R.id.SVReporteInterpol);
    tvReporteInterpol = findViewById(R.id.TVReporteInterpol);

    //INICIALIZAR VARIABLES DE INTERFACE DERECHA
    llDerecho = findViewById(R.id.LLDerecho);
    rlDer = findViewById(R.id.RLDer);
    //INFORMACION TESTIGOS Y NOTAS PAIS
    tvInfoInvestigacion = findViewById(R.id.TVInfoInvestigation);
    //INFORMACION INTERPOL SOSPECHOSOS
    tvIntroSuspectTitulo = findViewById(R.id.TVInfoSuspectsTitulo);
    svInfoSuspects = findViewById(R.id.SVInfoSuspects);

    initInfo();

    actvSex = findViewById(R.id.ACsex);
    actvHobby = findViewById(R.id.AChobby);
    actvHair = findViewById(R.id.AChair);
    actvFeature = findViewById(R.id.ACfeature);
    actvAuto = findViewById(R.id.ACauto);

    setAutoCompleteTextViewsOptions();
    btBuscarInterpol = findViewById(R.id.btBuscarInterpol);

    //INTERFACE OPCIONES INFERIORES
    glMasterOptions = findViewById(R.id.GLMasterOptions);
    glMasterOptions.setVisibility(View.INVISIBLE);

    btConexiones = findViewById(R.id.btConexiones);
    btViajar = findViewById(R.id.btViajar);
    btInvestigar = findViewById(R.id.btInvestigar);
    btInterpol = findViewById(R.id.btInterpol);

  }

  @Override
  protected void onStart() {    super.onStart();  }
  @Override
  protected void onResume() {    super.onResume();  }
  @Override
  protected void onPause() {    super.onPause();  }
  @Override
  protected void onStop() {    super.onStop();  }
  @Override
  protected void onRestart() {    super.onRestart();  }
  @Override
  protected void onDestroy() {    super.onDestroy();  }
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    moveTaskToBack(true);
  }

  /*
  *INTERFACE IZQUIERDA
   */
  public void ocultarInterfaceIzquierdo(){    llIzquierdo.setVisibility(View.INVISIBLE);  }
  public void mostrarInterfaceIzquierdo(){    llIzquierdo.setVisibility(View.VISIBLE);  }

  // SUPERIOR
  //TEXTO PAIS Y HORA
  public void cleanPaisActual(){    tvPaisActual.setText("");  }
  public void cleanHoraActual(){    tvHoraActual.setText("");  }
  public void setPaisActual(){    tvPaisActual.setText(objetosPaises.get(nombrePaises.get(paisActual)).getCapital());  }
  public void setHoraActual(){    tvHoraActual.setText(detective.getTime());  }
  public void cleanInterfacePaisYHoraActual(){
    cleanPaisActual();
    cleanHoraActual();
  }
  public void setInterfacePaisYHoraActual(){
    setPaisActual();
    setHoraActual();
  }
  public void ocultarInterfacePaisYHoraActual(){
    llPaisActualYHora.setVisibility(View.INVISIBLE);
  }
  public void mostrarInterfacePaisYHoraActual(){
    llPaisActualYHora.setVisibility(View.VISIBLE);
  }

  // INFERIOR
    //INF IMAGEN Y TRIVIAS
  public void cleanTrivias(){    tvTextTrivias.setText("");  }
  public void cleanImage(){    ivCountry.setImageDrawable(null);  }
  public void setTrivia(String trivia){    tvTextTrivias.setText(trivia);  Log.d("SET TRIVIA", trivia);}
  public void setImage(String country){    Log.d("MAIN setImage", "set image of " + country);  }
  public void ocultarInterfaceImageTrivias(){    svImageTrivias.setVisibility(View.INVISIBLE);  }
  public void mostrarInterfaceImageTrivias(){    svImageTrivias.setVisibility(View.VISIBLE);  }
  public void cleanInterfaceImageTrivias(){
    cleanTrivias();
    cleanImage();
  }
  public void setInterfaceImageTrivias(String trivia, String country){
    setTrivia(trivia);
    setImage(country);
  }

  //INTERFACE INTRO Y MISION
  public void cleanIntroSaludo(){    tvWritingIntroSaludo.setText("");  }
  public void ocultarIntroSaludo(){  tvWritingIntroSaludo.setVisibility(View.INVISIBLE); }
  public void mostrarIntroSaludo(){  tvWritingIntroSaludo.setVisibility(View.VISIBLE);}
  public void cleanNameDetective(){    ETdetectiveName.setText("");  }
  public void ocultarNameDetective(){ ETdetectiveName.setVisibility(View.INVISIBLE); }
  public void mostrarNameDetective(){  ETdetectiveName.setVisibility(View.VISIBLE); }
  public void cleanMision(){    tvMision.setText("");  }
  public void setIntroSaludo(String saludo){    tvWritingIntroSaludo.setText(saludo);  }
  public void setMision(String mision){    tvMision.setText(mision);  }
  public String getTextMision(){ return tvMision.getText().toString(); }
  public void ocultarBotonIniciarMision(){    btIniciarMision.setVisibility(View.INVISIBLE);  }
  public void mostrarBotonIniciarMision(){    btIniciarMision.setVisibility(View.VISIBLE);  }
  public void setTagBotonIniciarMision(String tag){ btIniciarMision.setTag(tag);  }
  public void setTextBotonIniciarMision(String text){ btIniciarMision.setText(text); }
  public void ocultarInterfaceIntroYMision(){    svIntroMision.setVisibility(View.INVISIBLE);  }
  public void mostrarInterfaceIntroYMision(){    svIntroMision.setVisibility(View.VISIBLE);  }
  public void cleanInterfaceIntroYMision(){
    cleanIntroSaludo();
    cleanNameDetective();
    cleanMision();
  }
  public void setInterfaceNewMision(String mision, String tag, String text){
    ocultarIntroSaludo();
    ocultarNameDetective();
    setMision(mision);
    setTagBotonIniciarMision(tag);
    setTextBotonIniciarMision(text);
  }

  //REPORTE INTERPOL
  public void ocultarInterfaceReporteInterpol(){    svReporteInterpol.setVisibility(View.INVISIBLE);  }
  public void mostrarInterfaceReporteInterpol(){    svReporteInterpol.setVisibility(View.VISIBLE);  }
  public void setInterfaceReporteInterpol(String reporte){    tvReporteInterpol.setText(reporte);  }
  public void cleanInterfaceReporteInterpol(){    tvReporteInterpol.setText("");      }
  public String getTextReporteInterpol(){ return tvReporteInterpol.getText().toString(); }
  public void setTextReporteInterpol(String text){ tvReporteInterpol.setText(text);}

  /*
  *INTERFACE DERECHA
   */
  public void ocultarInterfaceDerecho(){    llDerecho.setVisibility(View.INVISIBLE);  }
  public void mostrarInterfaceDerecho(){    llDerecho.setVisibility(View.VISIBLE);  }
    //INFO DE LA INVESTIGACION
  public void cleanInfoInvestigacion(){    tvInfoInvestigacion.setText("");  }
  public void setInfoInvestigacion(String info){    tvInfoInvestigacion.setText(info);  }
  public void cleanInterfaceInfoInvestigacion(){
    cleanInfoInvestigacion();
  }
  public void setInterfaceInfoInvestigacion(String info){
    setInfoInvestigacion(info);
  }
  public void ocultarInterfaceInfoInvestigacion(){
    tvInfoInvestigacion.setVisibility(View.INVISIBLE);
  }
  public void mostrarInterfaceInfoInvestigacion(){
    tvInfoInvestigacion.setVisibility(View.VISIBLE);
  }

    //BASE DE DATOS INTERPOL
  public void cleanTituloBaseDeDatos(){    tvIntroSuspectTitulo.setText("");  }
  public void setTituloBaseDeDatos(String titulo){    tvIntroSuspectTitulo.setText(titulo);  }
  public void ocultarTituloBaseDeDatos(){
    tvIntroSuspectTitulo.setVisibility(View.INVISIBLE);
  }
  public void mostrarTituloBaseDeDatos(){
    tvIntroSuspectTitulo.setVisibility(View.VISIBLE);
  }
  public void cleanSuspectData(){
    actvSex.setText("");
    actvHobby.setText("");
    actvHair.setText("");
    actvFeature.setText("");
    actvAuto.setText("");
  }
  public void setSuspectData(){
    actvSex.setText("");
    actvHobby.setText("");
    actvHair.setText("");
    actvFeature.setText("");
    actvAuto.setText("");
  }
  public void cleanInterfaceBaseDeDatos(){
    cleanSuspectData();
  }
  public void setInterfaceBaseDeDatos(){
    setTituloBaseDeDatos("BASE DE DATOS INTERPOL");
    setSuspectData();
  }
  public void mostrarInterfaceBaseDeDatos(){
    mostrarTituloBaseDeDatos();
    svInfoSuspects.setVisibility(View.VISIBLE);
  }
  public void ocultarInterfaceBaseDeDatos(){
    ocultarTituloBaseDeDatos();
    svInfoSuspects.setVisibility(View.INVISIBLE);
  }


  /*
  *INTERFACE INFERIOR
   */
  //FUNCION OCULTAR BOTONES DE OPCIONES
  public void ocultarOpciones(){    glMasterOptions.setVisibility(View.INVISIBLE);  }
  public void mostrarOpciones(){    glMasterOptions.setVisibility(View.VISIBLE);  }
  //FUNCION QUE DESACTIVA LAS OPCIONES DE LOS BOTONES MIENTRAS EL USUARIO VIAJA
  public void desactivarOpciones(){
    btConexiones.setClickable(false);
    btViajar.setClickable(false);
    btInvestigar.setClickable(false);
    btInterpol.setClickable(false);
  }
  public void activarOpciones(){
    btConexiones.setClickable(true);
    btViajar.setClickable(true);
    btInvestigar.setClickable(true);
    btInterpol.setClickable(true);
  }

  //OPCIONES DE SELECCION DE CARACTERISTICAS DE LOS SOSPECHOSOS
  public void setAutoCompleteTextViewsOptions(){
    ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.sexs);
    actvSex.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.hobbies);
    actvHobby.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.haircolors);
    actvHair.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.features);
    actvFeature.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.autos);
    actvAuto.setAdapter(adapter);
  }


  /*
  *FUNCIONES INICIAR GAME
   */

  //FUNCION PARA INICIAR EL JUEGO
  public void initGame(){
    initMision();
    //INICIALIZAR DETECTIVE
    initDetective(ETdetectiveName.getText().toString());
    //INICIALIZAR PAISES
    initCountries();
    //INICIALIZAR SOSPECHOSOS
    initSuspects();

    enviarTextoEntrada();
  }


  //INICIAR ELEMENTOS DEL LAYOUT DEL LADO IZQUIRDO
  public void initMision(){
    ocultarOpciones();
    cleanInterfaceImageTrivias();
    ocultarInterfaceImageTrivias();
    cleanInterfaceReporteInterpol();
    ocultarInterfaceReporteInterpol();
    mostrarInterfaceIntroYMision();
    ocultarBotonIniciarMision();
  }
  public void initInterfaceTriviasAndImage(){
    mostrarOpciones();
    cleanInterfaceImageTrivias();
    mostrarInterfaceImageTrivias();
    ocultarInterfaceIntroYMision();
    cleanInterfaceReporteInterpol();
    ocultarInterfaceReporteInterpol();
  }

  //FUNCION PARA INICIAR UNA NUEVA MISION
  public void setInterfaceAndAskNewMision(String mision, String tag, String text){
    cleanInterfacePaisYHoraActual();
    cleanInterfaceBaseDeDatos();

    ocultarOpciones();

    cleanInterfaceImageTrivias();
    ocultarInterfaceImageTrivias();

    cleanInterfaceReporteInterpol();
    ocultarInterfaceReporteInterpol();

    cleanInterfaceIntroYMision();
    mostrarInterfaceIntroYMision();

    cleanInterfaceInfoInvestigacion();

    setInterfaceNewMision(mision, tag, text);
  }

  public void initNewMision(){
    //INICIALIZAR PAISES
    initCountries();
    //INICIALIZAR SOSPECHOSOS
    initSuspects();
  }


  //SET LINEAR LAYOUTS INFO
  public void initInfo(){
    cleanInterfacePaisYHoraActual();
    mostrarInterfacePaisYHoraActual();

    cleanInterfaceImageTrivias();
    ocultarInterfaceImageTrivias();

    cleanInterfaceReporteInterpol();
    ocultarInterfaceReporteInterpol();

    cleanInterfaceInfoInvestigacion();
    mostrarInterfaceInfoInvestigacion();
    ocultarInterfaceBaseDeDatos();

    mostrarInterfaceIntroYMision();
  }

  public void onClicOptions(){

    mostrarInterfaceImageTrivias();

    cleanInterfaceInfoInvestigacion();
    mostrarInterfaceInfoInvestigacion();

    cleanInterfaceReporteInterpol();
    ocultarInterfaceReporteInterpol();

    cleanInterfaceIntroYMision();
    ocultarInterfaceIntroYMision();

    ocultarInterfaceBaseDeDatos();
  }

  public void setInfoSuspectstVisible(){
    cleanInterfaceInfoInvestigacion();
    ocultarInterfaceInfoInvestigacion();
    mostrarInterfaceBaseDeDatos();

    cleanInterfaceReporteInterpol();
    mostrarInterfaceReporteInterpol();
  }


  //INICIALIZAR OBJETO DETECTIVE
  public void initDetective(String name){
    //PREGUNTAS INICIALES AL DETECTIVE
    //String detectiveName = sharedPref.getString("name", "");
    String lvl = sharedPref.getString("nivel"+name, null);
    String rank = sharedPref.getString("rank"+name, null);

    if(lvl == null){      lvl = "1";      editor.putString("nivel"+name, "1");    }
    if(rank == null){ rank = "OFICIAL";   editor.putString("rank"+name, "OFICIAL"); }

    //OFICIAL, INVESTIGADOR, INSPECTOR, DETECTIVE

    detective = new Detective();
    detective.setNivel(lvl);
    detective.setRank(rank);
    detective.setName(name);

  }

  //INICIALIZAR OBJECTOS PAISES
  public void initCountries(){
    nombrePaises = new HashMap<>();
    objetosPaises = new ArrayList<>();

    objetosPaises = UtilityClassCountries.getInstance().getList();
    for(int i = 0; i < objetosPaises.size(); i++){
      nombrePaises.put(objetosPaises.get(i).getName(), i);
    }
  }

  //INICIALIZAR OBJECTOS SOSPECHOSOS
  public void initSuspects(){
    objetosSuspects = new ArrayList<>();
    paisesVisitadosLadron = new ArrayList<>();
    lugaresInvestigar  = new ArrayList<>();

    objetosSuspects = UtilityClassSuspects.getInstance().getList();


    //NUMERO ALEATORIO PARA SELECCIONAR UN LADRON
    idLadron = new Random().nextInt(objetosSuspects.size());
    //SET LADRON
    //objetosSuspects[idLadron].getClass().getMethod("setLadron").invoke(objetosSuspects[idLadron]);
    objetosSuspects.get(idLadron).setThief();
    //Object total = objetosSuspects[idLadron].getClass().getMethod("getTotalPaises").invoke(objetosSuspects[idLadron]);
    Log.d("MAIN initSuspects", "El ladron es " + objetosSuspects.get(idLadron).getName() );
    //INICIALIZAR LOS PAISES VISITADOS SEGUN EL NIVEL DEL DETECTIVE
    objetosSuspects.get(idLadron).setVisitedCountries(Integer.parseInt(detective.getNivel()), new ArrayList<>(nombrePaises.keySet()));

    int total = objetosSuspects.get(idLadron).getTotalVisitedCountries();

    Log.d(TAG, "TOTAL PAISES VISITADOS " + total);

    for(int i = 0; i < total; i++){

      //paisesVisitadosLadron.add ((String) objetosSuspects[idLadron].getClass().getMethod("getPaisVisitado", Integer.class).invoke(objetosSuspects[idLadron], i));
      paisesVisitadosLadron.add(objetosSuspects.get(idLadron).getVisitedCountry(i));
      Log.d("MAIN", "VISITO = " + paisesVisitadosLadron.get(i));
      //PAIS DEL ROBO
      int index = nombrePaises.get(paisesVisitadosLadron.get(i));
      if(i == 0){
        setPaisActual(paisesVisitadosLadron.get(i));
        //CANTIDAD DE OBJETOS PARA ROBAR
        //Object cantidadObjetos = objetosPaises[index].getClass().getMethod("getCantidadObjetos").invoke(objetosPaises[index]);
        int cantidadObjetos = objetosPaises.get(index).getTotalStolenObjects();
        Log.d("initSuspects", "cantidadObjetos = " + cantidadObjetos);
        int random = new Random().nextInt((Integer) cantidadObjetos - 1);
        //OBJETO ROBADO
        //ASIGNAR OBJECTO ALEATORIO DEL PRIMER PAIS QUE VISITO ESE LADRON
        //objetoRobado = (String) objetosPaises[index].getClass().getMethod("getObjeto", Integer.class).invoke(objetosPaises[index], random);
        objetoRobado = objetosPaises.get(index).getStolenObjs(random);
        Log.d("initSuspects", "Objeto robado = " + objetoRobado);
      //SI ES EL ULTIMO PAIS, ASIGNE UN LUGAR DONDE SE ENCUENTRA EL LADRON
      }else if(i == (total - 1)){
        int totalPlacesInCountry = objetosPaises.get(index).getTotalPlaces();
        int randomPlace = new Random().nextInt(totalPlacesInCountry-1) + 1;
        Log.d("initSuspects", "totalPlacesInCountry =  " + totalPlacesInCountry);
        String key = "place"+randomPlace;
        String lastPlace = objetosPaises.get(index).getPlace(key);
        objetosSuspects.get(idLadron).setLastPlace(lastPlace);
        Log.d("initSuspects", "Ladron se encuentra en " + lastPlace);
      }
    }
    siguientesPaises(paisActual);
  }

  //ASIGNAR PAIS ACTUAL
  public void setPaisActual(String pais){
    paisActual = pais;
    detective.addPaisVisitado(paisActual);
    //EL PAIS ACTUAL ESTA EN LA RUTA DEL LADRON ??
    boolean enLaRuta = false;
    for(int i = 0; i < paisesVisitadosLadron.size(); i++){
      if(paisesVisitadosLadron.get(i).contains(paisActual)){
        enLaRuta = true;
      }
    }
    detective.paisesEnLaRutaLadron.add(enLaRuta);

    //LUGARES A INVESTIGAR EN EL PAIS ACTUAL
    int index = nombrePaises.get(pais);
    String[] lugares = (String[]) objetosPaises.get(index).getPlaces();
    lugaresInvestigar = Arrays.asList(lugares);
    Log.d("MAIN lugaresInvetigar", "lugaresInvetigar = " + Arrays.toString(lugares));

  }

  //SET TV IMAGE / TRIVIA COUNTRY
  public void setTrviasCountry(){
    int index = nombrePaises.get(paisActual);
    Integer totalTrivias = objetosPaises.get(index).getTotalTrivias();
    int randomTrivia;
    if(totalTrivias != null){
      if(totalTrivias > 1){
        randomTrivia = new Random().nextInt(totalTrivias - 1) + 1;
      }else{
        randomTrivia = 0;
      }
      String trivia = "triv"+randomTrivia;
      Log.d("setTrviasCountry", "Trivia = " + trivia);
      setTrivia(objetosPaises.get(index).getTrivia(trivia));
    }
  }

  //FUNCION QUE CREARA EL TEXTO DE INTRO DEL JUEGO
  public void enviarTextoEntrada(){
    String texto = "\nHola " + detective.getName() + " \n" +
      "Tu rango actual es:" + "\n" + detective.getRank() + "\n" + "\n" + "\n" +
      "****FLASH****" + "\n" + "\n" +
      "Nuevo evento:" + "\n" +
      "se ha reportado el robo del objeto:"+ "\n"  +
      objetoRobado + "\n"  +
      "en " + paisActual + "\n" + "\n" +
      "se sospecha de la banda V.I.L.E." +
      " Por favor dirijase al lugar, investigue y encuentre al sospechoso" +
      " tiene hasta el domingo antes de las 18:00 " + "\n" + "\n" +
      "Buena suerte";
    //typing(texto, "MISION");
    setMision(texto);
    svIntroMision.fullScroll(View.FOCUS_DOWN);
    mostrarBotonIniciarMision();
  }


  //FUNCION QUE GENERARA LOS DESTINOS POSIBLES
  public void siguientesPaises(String paisActual){

    String siguientePaisLadron = null;
    destinos = new ArrayList<>();

    //Log.d("siguientesPaises", "ACTUAL = " + paisActual);

    //1. SI EL PAIS ACTUAL ES UNO DE LOS VISITADOS POR EL LADRON, A??ADA EL SIGUIENTE DESTINO EN LA
    //RUTA DEL LADRON, SI ES EL ULTIMO, A??ADA EL ANTERIOR.
    Log.d("siguientesPaises LOOP", "paisesVisitadosLadron = " + paisesVisitadosLadron);
    for(int i = 0; i < paisesVisitadosLadron.size(); i++){
      //SI EL PAIS DONDE LLEGO EL DETECTIVE ES UNO EN LA RUTA DEL LADRON ENTONCES:...
      if(paisesVisitadosLadron.get(i).contains(paisActual)){
        if(i < paisesVisitadosLadron.size() - 1){
          siguientePaisLadron = paisesVisitadosLadron.get(i+1);
          break;
        }else{
          siguientePaisLadron = paisesVisitadosLadron.get(i-1);
        }
      }
      //Log.d("siguientesPaises", "ITER = " + i + "siguientePaisLadron = " + siguientePaisLadron);
    }

    //2. SI EL PAIS ACTUAL NO ES UNO DE LOS VISITADOS POR EL LADRON, A??ADA EL ULTIMO PAIS VISITADO POR EL
    //DETECTIVE EN LA RUTA DEL LADRON CADA DOS VECES QUE SE PRESENTE ESTA SITUACION
    if(siguientePaisLadron == null){
      if(regresarARuta < 1){
        boolean terminar = false;
        while(!terminar){
          siguientePaisLadron = paisAleatorioDelTotal();
          if(!siguientePaisLadron.contains(paisActual)){
            terminar = true;
          }
        }
        //Log.d("siguientesPaises", "siguientePaisLadron ALEATORIO = " + siguientePaisLadron);
        regresarARuta++;
      }else{
        siguientePaisLadron = detective.getUltimoPaisRutaLadron();
        //Log.d("siguientesPaises", "siguientePaisLadron ULTIMO VISITADO = " + siguientePaisLadron);
        regresarARuta--;
      }
    }

    //A??ADIR EL SIGUIENTE PAIS VISITADO
    destinos.add(siguientePaisLadron);
    //VARIABLES QUE CONTENDRAN LOS PAISES RELACIONADOS
    Integer cantidadPaisesRelacionados = null;
    String[] paisesRelacionados = null;

    //CICLO PARA A??ADIR LOS POSIBLES DESTINOS
    boolean terminarDeAnadir = false;
    int index;
    String pais;
    int indexPaisA??adido = 0;
    int ingresados = 0;
    int intentosPaisRelacionado = 0;
    String nivel = detective.getNivel();
    int random;
    while(!terminarDeAnadir){
      if(destinos.size() > 3){
        terminarDeAnadir = true;
      }else{
        //Log.d("siguientesPaises", "destinos[" +indexPaisA??adido+"] = " + destinos.get(indexPaisA??adido) );
        //Log.d("siguientesPaises", "indexPaisA??adido = "+indexPaisA??adido);
        //Log.d("siguientesPaises", "destinos = "+destinos.get(indexPaisA??adido));
        String keyInNombrePaises = encontrarIndexNombrePaises(destinos.get(indexPaisA??adido));
        Log.d("siguientesPaises", "keyInNombrePaises = " + keyInNombrePaises);
        index = nombrePaises.get(keyInNombrePaises);
        //TRAER LOS PAISES RELACIONADOS CON EL PAIS ACTUAL INGRESADO
        cantidadPaisesRelacionados = objetosPaises.get(index).getTotalRelatedCountries();
        paisesRelacionados = (String[]) objetosPaises.get(index).getAllRelatedCountries();

          //A??ADIR PAISES RELACIONADO CON EL DESTINO DEPENDIENDO DEL NIVEL
        if (cantidadPaisesRelacionados != null && paisesRelacionados != null
        && intentosPaisRelacionado < 10) {
          random = new Random().nextInt(cantidadPaisesRelacionados - 1);
          pais = paisesRelacionados[random];
          intentosPaisRelacionado++;
          //Log.d("siguientesPaises", "RANDOM = "+ random + " PAIS RELACIONADO = " + Country);
        }else{
          //A??ADIR UN PAIS ALEATORIO
          pais = paisAleatorioDelTotal();
          //Log.d("siguientesPaises", "PAIS ALEATORIO = " + Country);
        }

        if(pais != null && !destinos.contains(pais) && !pais.contains(paisActual)){
          destinos.add(pais);
          indexPaisA??adido++;
          ingresados++;
        }
      }
    }
    //DAR UN ORDEN ALEATORIO A LA LISTA
    Collections.shuffle(destinos);
    Log.d(TAG, "destinos = " + destinos);
    obtenerNombreDestinos();
  }

  //FUNCION PARA OBTENER EL INDEX DE UN MAP CON LA MEJOR COINCIDENCIA DE KEY
  public String encontrarIndexNombrePaises(String keyToFind){
    final Collator instance = Collator.getInstance();
    // This strategy mean it'll ignore the accents
    instance.setStrength(Collator.NO_DECOMPOSITION);
    for(String key : nombrePaises.keySet()){
      if(instance.compare(key, keyToFind) == 0){
        //Log.d("MAIN encontrarIndexNombrePaises", "keyToFind = " + keyToFind + " keyFounded = " + key);
        return key;
      }
    }
    return "NOT FOUND";
  }


  //GENERAR UN PAIS ALEATORIAMENTE DEL TOTAL
  public String paisAleatorioDelTotal() {
    Integer random = new Random().nextInt(nombrePaises.size() - 1);
    String pais = "";
    for (Map.Entry<String, Integer> entry : nombrePaises.entrySet()) {
      if (Objects.equals(random, entry.getValue())) {
        pais = entry.getKey();
      }
    }
    return pais;
  }

  //FUNCION QUE BUSCA LOS NOMBRE DE LOS PAISES DESTINO
  public void obtenerNombreDestinos(){
    nomDestinos = new ArrayList<>();
    int index;
    for(int i = 0; i < destinos.size(); i++){
      //Log.d("MAIN ObtenerNombreDestinos", "destinos["+i+"] = " + destinos.get(i));
      index = nombrePaises.get(encontrarIndexNombrePaises(destinos.get(i)));
      nomDestinos.add(objetosPaises.get(index).getName());
    }
    Log.d(TAG, "NOMBRE DESTINOS = "+ nomDestinos);
  }

  //CALCULO DEL TIEMPO DE VIAJE
  public void viajarAPais(String Actual, String destino){

    //1. CAMBIAR EL PAIS ACTUAL
    setPaisActual(destino);
    setTrviasCountry();

    //2. CALCULAR LA DISTANCIA
    int indexActual = nombrePaises.get(encontrarIndexNombrePaises(Actual));
    int indexDestino = nombrePaises.get(encontrarIndexNombrePaises(destino));
    double latitud1 = 0, longitud1 = 0;
    double latitud2 = 0, longitud2 = 0;
    double distanciasPaises;
    int tiempoViaje;
    String cadenaTexto;

    //CALCULAR DISTANCIAS
    latitud1 = Double.parseDouble(objetosPaises.get(indexActual).getLatitud());
    longitud1 = Double.parseDouble(objetosPaises.get(indexActual).getLongitud());
    latitud2 = Double.parseDouble(objetosPaises.get(indexDestino).getLatitud());
    longitud2 = Double.parseDouble(objetosPaises.get(indexDestino).getLongitud());

    distanciasPaises = Util.haversine(latitud1, longitud1, latitud2, longitud2);
    Log.d(TAG, "Distancia "+ distanciasPaises);

    tiempoViaje = (int) Util.tiempoViaje(distanciasPaises);
    Log.d(TAG, "Tiempo de viaje "+ tiempoViaje);

    int nuevaHora = Util.nuevaHora(detective.getHour(),  tiempoViaje);
    int diferenciaReal = Util.diferenciaReal(nuevaHora, tiempoViaje);

    detective.addTime(diferenciaReal);
    desactivarOpciones();
    animarReloj(diferenciaReal, tiempoViaje);

    String mensajeViaje = "Distancia a viajar = " + (int) distanciasPaises + "\n" +
      "Tiempo de viaje = " + tiempoViaje + "\n" +
      "Tiempo descanso = " + (diferenciaReal - tiempoViaje);
    Util.makeToast(this, mensajeViaje, 0 );

    siguientesPaises(paisActual);

  }

  //FUNCION QUE CAMBIA EL RELOJ SEGUN EL CALCULO DEL NUEVO TIEMPO
  public void animarReloj(Integer diferenciaReal, Integer tiempoViaje){

    mExecutor.execute(() -> {
        for (int i = 0; i < diferenciaReal; i++) {
          String text = Util.newDayHour(detective.getHour(), detective.getDia());
          //String text = diasSemana[indexDiaSiguiente] + "/" + horaPresentar + "/" +franjaHoraria + "/" + hora;
          detective.setDia(text.split("/")[0]);
          detective.setHourToShow(text.split("/")[1]);
          detective.setAMPM(text.split("/")[2]);
          detective.setHour(Integer.parseInt(text.split("/")[3]));

          mHandler.post(this::setHoraActual);

          try{
            if(i > tiempoViaje){
              Thread.sleep(150);
              tvPaisActual.setText("Descansando");
            }else{
              Thread.sleep(300);
              tvPaisActual.setText("Viajando");
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      terminoReloj();
    });

  }

  //FUNCION QUE CAMBIARA LAS HORAS QUE PASAN DEPENDIENDO DEL LUGAR A VISITAR
  public void visitarLugar(String lugar){
    //1. NUMERO ALEATORIO PARA EL NUMERO DE HORAS
    //NOTA: HORARIO DE ATENCION ES ENTRE LAS 8 - 22, LOS TIEMPOS DE VIAJE ESTAN ENTRE 1-3 HORAS
    //SI LLEGA A LAS 22 o DESPUES, ES UNA PROBABLE CITA CON ALGUIEN, SE REPORTA LA PISTA Y PASA A DORMIR
    //1.1 SI LA HORA DE VIAJE ESTA ENTRE LAS 8 - 10 (POSIBLE HORA PICO) ES MAS PROBABLE QUE
    int tiempoDeViaje = 0;
    int rango1 = 60;
    int rango2 = 100;
    int horaActual = detective.getHour();
    //HORAS PICO
    if(horaActual > 8 && horaActual <= 10){rango1 = 10; rango2 = 60;}
    if(horaActual > 17 && horaActual <= 19){rango1 = 10; rango2 = 60;}
    //PROBABILIDAD DE HORAS EN VIAJE
    int random = new Random().nextInt(100);
    if(random <= rango1){ tiempoDeViaje = 1; }
    if(random > rango1 && random <= rango2){ tiempoDeViaje = 2; }
    if(random > rango2){ tiempoDeViaje = 3; }

    int nuevaHora = Util.nuevaHora(detective.getHour(),  tiempoDeViaje);
    int diferenciaReal = Util.diferenciaReal(nuevaHora, tiempoDeViaje);
    detective.addTime(diferenciaReal);

    animarReloj(diferenciaReal, tiempoDeViaje);
    setPaisActual();
    piastasEnElLugar(lugar);

  }

  //FUNCION QUE MUESTRA LAS PISTAS OBTENIDAS EN EL LUGAR A INVESTIGAR
  public void piastasEnElLugar(String lugar){

    String[] personas = new String[]{"Guardia de seguridad", "Peaton", "Vendedor", "Policia local",
    "Ciclista"};
    int randomPersonas =  new Random().nextInt(personas.length);


    int indexCountryActual = nombrePaises.get(paisActual);

    int indexRuta = paisesVisitadosLadron.stream()
      .filter(v -> v.contains(paisActual))
      .map(v -> paisesVisitadosLadron.indexOf(v)).findFirst()
      .orElse(-1);
    int sizePaisesVisitados = paisesVisitadosLadron.size() - 1;

    String siguientePais, pistaMoneda = "", pistaVestimenta = "", pistaBandera = "", mensaje = "";
    List<String> pistas = new ArrayList<>();

    int indexCountrySiguiente;

    //SI EL LADRON ESTUVO EN EL PAIS Y EL PAIS ACTUAL NO ES EL UTLTIMO ENTONCES:...
    if(indexRuta != -1 & indexRuta < sizePaisesVisitados){

      siguientePais = paisesVisitadosLadron.get(indexRuta+1);
      indexCountrySiguiente = nombrePaises.get(siguientePais);

      //MONEY
      if(!objetosPaises.get(indexCountrySiguiente).getMoneda().contains("Dolar") &&
        !objetosPaises.get(indexCountrySiguiente).getMoneda().isEmpty()){
        pistas.add("Vi a alguien sospechoso queriendo cambiar dolares a " + objetosPaises.get(indexCountrySiguiente).getMoneda());
      }

      //CLOTHING
      if(objetosSuspects.get(idLadron).getTotalClothing() != null){
        //TODO:SI CLOTHING = 1
        int randomVestimenta =  new Random().nextInt(objetosSuspects.get(idLadron).getTotalClothing() - 1);
        pistas.add("Vi a alguien sospechoso que vestia " +  objetosSuspects.get(idLadron).getClothing(randomVestimenta) +" de color " +
          objetosSuspects.get(idLadron).getClothingColor(randomVestimenta));
      }

      //FLAG
      pistas.add("Vi que llevaba una bandera con los colores " + objetosPaises.get(indexCountrySiguiente).getClue("flag"));

      //COUNTRY PRODUCTION
      int randomProduccion =  new Random().nextInt(objetosPaises.get(indexCountrySiguiente).getCluesProd()) + 1;
      if(objetosPaises.get(indexCountrySiguiente).getClue("prod"+randomProduccion) != null){
        pistas.add("Escuche a alguien hablar de un pais donde se exporta " +
          objetosPaises.get(indexCountrySiguiente).getClue("prod"+randomProduccion));
      }

      //AUTO
      if(objetosSuspects.get(idLadron).getAuto() != null & !objetosSuspects.get(idLadron).getAuto().isEmpty()){
        pistas.add("Me parecio escuchar a alguien preguntando donde podia alquilar un auto tipo " + objetosSuspects.get(idLadron).getAuto());
      }

      //HAIR COLOR & SEX
      if(objetosSuspects.get(idLadron).getHaircolor() != null){
        if(!objetosSuspects.get(idLadron).getHaircolor().isEmpty()){
          if(objetosSuspects.get(idLadron).getSex().contains("mujer")){
            pistas.add("Vi a una mujer sospechosa con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
          }else if(objetosSuspects.get(idLadron).getSex().contains("hombre")){
            pistas.add("Vi a un hombre sospechoso con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
          }else{
            pistas.add("Vi a alguien sospechoso con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
          }
        }else{
          if(objetosSuspects.get(idLadron).getSex().contains("mujer")){
            pistas.add("Vi a una mujer sospechosa sin cabello");
          }else if(objetosSuspects.get(idLadron).getSex().contains("hombre")){
            pistas.add("Vi a un hombre sospechoso sin cabello");
          }else{
            pistas.add("Vi a alguien sospechoso sin cabello");
          }
        }
      }

      //FAVORITE FOOD
      if(objetosSuspects.get(idLadron).getFavoriteFood() != null & !objetosSuspects.get(idLadron).getFavoriteFood().isEmpty()){
        pistas.add("Escuche a alguien preguntando donde podia comer " + objetosSuspects.get(idLadron).getFavoriteFood());
      }
      //FEATURE
      if(objetosSuspects.get(idLadron).getFeature() != null & !objetosSuspects.get(idLadron).getFeature().isEmpty()){
        pistas.add("Vi a alguien sospechoso con un " + objetosSuspects.get(idLadron).getFeature());
      }
      //HOBBIE
      if(objetosSuspects.get(idLadron).getHobby() != null & !objetosSuspects.get(idLadron).getHobby().isEmpty()){
        pistas.add("Escuche a alguien hablando que le gustaba " + objetosSuspects.get(idLadron).getHobby());
      }
      //NADIE HA VISTO ALGO
      pistas.add("No he visto nada sospechoso por aqui.");

    //SI EL LADRON ESTUVO EN ESTE PAIS Y ES EL ULTIMO
    }else if(indexRuta != -1 & indexRuta == sizePaisesVisitados){
      //SI EL DETECTIVE ESTA EN EL MISMO LUGAR DEL LADRON ENTONCES
      if(objetosSuspects.get(idLadron).getLastPlace().contains(lugar)){
        pistas.add("Ahi esta quien buscas!");
        String mensajeFinal = "Ladron Encontrado";
        detective.setEncontrado(true);
        verificarFin();

      }else{
        pistas.add("Hace poco vi a alguien sospechoso por ac??");
        pistas.add("me parecio haber visto el " + objetoRobado);
      }
    //SI EL LADRON NO ESTUVO EN EL PAIS ACTUAL
    }else if(indexRuta == -1){
      pistas.add("No he visto nada sospechoso por aqui.");
    }
    int randomPista;
    if(pistas.size() > 1){
      randomPista = new Random().nextInt(pistas.size() - 1);
    }else{
      randomPista = 0;
    }

    mensaje = personas[randomPersonas] + ": \n" + pistas.get(randomPista);
    setInfoInvestigacion(mensaje);
  }


  //FUNCION DE RESPUESTA A BOTON CONEXIONES
  public void conexiones(View view){
    //VER POSIBLES DESTINOS
    onClicOptions();
    construirAlertaConexiones("CONEXIONES");
  }

  //FUNCION DE RESPUESTA A BOTON VIAJAR
  public void viajar(View view){
    //VER POSIBLES DESTINOS
    onClicOptions();
    construirAlertaConexiones("VIAJAR");
  }

  //FUNCION DE RESPUESTA A BOTON INVESTIGAR
  public void investigar(View view){
    //VER LUGARES DEL PAIS ACTUAL QUE SE PUEDEN VISITAR
    onClicOptions();
    construirAlertaConexiones("INVESTIGAR");
  }

  //FUNCION DE RESPUESTA A BOTON INTERPOL
  public void interpol(View view){
    setInfoSuspectstVisible();

    ocultarInterfaceIntroYMision();
    ocultarInterfaceImageTrivias();
    cleanInterfaceReporteInterpol();
    mostrarInterfaceReporteInterpol();

  }

  //FUNCION DE RESPUESTA A BOTON BUSCAR
  public void buscarSospechoso(View view){

    sospechosos = new ArrayList<>();

    final Collator instance = Collator.getInstance();
    // This strategy mean it'll ignore the accents
    instance.setStrength(Collator.NO_DECOMPOSITION);

    String dbSex = actvSex.getText().toString();
    String dbHobby = actvHobby.getText().toString();
    String dbHair = actvHair.getText().toString();
    String dbFeature = actvFeature.getText().toString();
    String dbAuto = actvAuto.getText().toString();

    Log.d("INTERPOL", "Sex      = " + dbSex);
    Log.d("INTERPOL", "Hobby    = " + dbHobby);
    Log.d("INTERPOL", "Hair     = " + dbHair);
    Log.d("INTERPOL", "Feature  = " + dbFeature);
    Log.d("INTERPOL", "Auto     = " + dbAuto);

    String susname, sussex, sushobby, sushairColor, susfeature, susauto;
    Map<String, String> datosSospechoso = new HashMap<>();
    List<Map<String, String>> suspListMap = new ArrayList<>();

    //1. BUSCAR SOSPECHOSOS QUE COINCIDAN CON LA DESCRIPCION
    for(int i = 0; i < objetosSuspects.size(); i++) {
      datosSospechoso = objetosSuspects.get(i).basicValuesToMap();
      suspListMap.add(datosSospechoso);
    }
    Stream<Map<String, String>> streamMap = suspListMap.stream();

    if(dbSex != null & !dbSex.isEmpty()){
      streamMap = streamMap.filter(map -> map.containsKey("sex") && map.get("sex").equals(dbSex));
    }
    if(dbHobby != null & !dbHobby.isEmpty()){
      streamMap = streamMap.filter(map -> map.containsKey("hobby") && map.get("hobby").equals(dbHobby));
    }
    if(dbHair != null & !dbHair.isEmpty()){
      streamMap = streamMap.filter(map -> map.containsKey("haircolor") && map.get("haircolor").equals(dbHair));
    }
    if(dbFeature != null & !dbFeature.isEmpty()){
      streamMap = streamMap.filter(map -> map.containsKey("feature") && map.get("feature").equals(dbFeature));
    }
    if(dbAuto != null & !dbAuto.isEmpty()){
      streamMap = streamMap.filter(map -> map.containsKey("auto") && map.get("auto").equals(dbAuto));
    }

    sospechosos =  streamMap.collect(Collectors.toList());


    String mensaje;
    String finalText;
    //2.COSNTRUIR MENSAJE COON LOS NOMBRES DE LOS SOSPECHOSOS

    //2.1 SI SON VARIOS
    if(sospechosos.size() > 1){
      StringBuilder stringBuilder = new StringBuilder();
      mensaje = "Reporte de sospechosos:" + "\n" + "\n";
      stringBuilder.append(mensaje);
      //2.1.1 CONSTRUIR REPORTE DE NOMBRES
      for(int i = 0; i < sospechosos.size(); i++){
        stringBuilder.append(sospechosos.get(i).get("name")).append("\n");
      }
      //2.1.2 ENVIAR REPORTE AL TEXT VIEW
      finalText = stringBuilder.toString();
      Log.d("MAIN buscarSospechoso", "REPORTE: " + finalText);
      detective.setSospechoso("");

    //2.2 SI SOLO ES UNO
    }else if(sospechosos.size() == 1){
      detective.setSospechoso(sospechosos.get(0).get("name"));
      finalText = "Se ha emitido una orden de captura para: " + "\n" + "\n" +
        sospechosos.get(0).get("name");

    //2.3. SI NO HAY SOSPECHOSOS
    }else{
      //ERROR EN LA VERIFICACION DE SOSPECHOSOS
      finalText = "Ningun sospechoso de la banda coincide con esta descripcion";
      detective.setSospechoso("");
    }

    //3. ENVIAR MENSAJE A TYPE
    //typing(mensaje, "INTERPOL")
    setTextReporteInterpol(finalText);

  }

  //CONSTRUCCION DE ALERTAS CON RESPUESTAS
  AlertDialog dialog;
  public void construirAlertaConexiones(String from){
    //String textoBoton = null;
    //if(from.contains("CONEXIONES")){      textoBoton = "REGRESAR";}
    //else if ((from.contains("VIAJAR"))) { textoBoton = "VIAJAR";  }

    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

    // Get the layout inflater
    LayoutInflater inflater = this.getLayoutInflater();
    View mView = inflater.inflate(R.layout.dialog_show_selection, null);
    final ListView listViewConexiones = (ListView) mView.findViewById(R.id.LVConexiones);

    listViewConexiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(from.contains("VIAJAR")){
          Log.d("ALERT DIALOG","Pais Seleccionado = " + destinos.get(position));
          view.setBackgroundColor(Color.GRAY);
          dialog.dismiss();
          viajarAPais(paisActual, destinos.get(position));
        }else if(from.contains("INVESTIGAR")){
          Log.d("ALERT DIALOG","Lugar a Investigar = " + lugaresInvestigar.get(position));
          dialog.dismiss();
          visitarLugar(lugaresInvestigar.get(position));
        }
      }
    });

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    Log.d("AlerDIalog", "From = " + from);
    if(from.contains("VIAJAR") || from.contains("CONEXIONES")){

      ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        this, R.layout.listview_custom, nomDestinos);
      listViewConexiones.setAdapter(arrayAdapter);
      Log.d("AlerDIalog", "arrayAdapter = " + nomDestinos);

    }else if(from.contains("INVESTIGAR")){
      ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        this, R.layout.listview_custom, lugaresInvestigar);
      listViewConexiones.setAdapter(arrayAdapter);
      Log.d("AlerDIalog", "arrayAdapter = " + lugaresInvestigar);
    }

    builder.setView(mView)
      // Add action buttons
      .setPositiveButton("REGRESAR", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          Log.d("ALERT DIALOG", "CANCEL");
        }
      });

    dialog = builder.show();
  }


  //FUNCION QUE SIMULA EL INGRESO DEL TEXTO COMO MAQUINA DE ESCRIBIR
  public void typing(String newText, String donde){

    mExecutor.execute(() -> {
      //Background work here
      MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.typewriter15x);
      for (int i = 0; i < newText.length(); i++){
        char c = newText.charAt(i);
        //Process char
        StringBuilder stringBuilder = new StringBuilder();
        String text = "";
        if(donde.contains("MISION")){
          text = getTextMision();
        }else if(donde.contains("INTERPOL")){
          text = getTextReporteInterpol();
        }

        String finalText =  stringBuilder.append(text).append(c).toString();
        //Log.d("TYPING", "Text = " + tvIntroMision.getText().toString());
        mp.start();

        mHandler.post(() -> {
          if(donde.contains("MISION")){
            setMision(finalText);
            svIntroMision.fullScroll(View.FOCUS_DOWN);
          }else if(donde.contains("INTERPOL")){
            //ESCRIBIR REPORTE INTERPOL
            setTextReporteInterpol(finalText);
          }

        });
        mp.pause();
        try {
          Thread.sleep(80);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        mp.seekTo(0);
        mp.release();
      }
    });


    /*
    new Thread() {
      public void run() {
        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.typewriter15x);
        for (int i = 0; i < newText.length(); i++){
          try {
            char c = newText.charAt(i);
            //Process char
            StringBuilder stringBuilder = new StringBuilder();
            String text = tvIntroMision.getText().toString();
            String finalText =  stringBuilder.append(text).append(c).toString();
            //Log.d("TYPING", "Text = " + tvIntroMision.getText().toString());
            mp.start();
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                setMision(finalText);
                svIntroMision.fullScroll(View.FOCUS_DOWN);
              }
            });
            Thread.sleep(80);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          mp.pause();
        }
        mp.seekTo(0);
        mp.release();
      }
    }.start();

     */
  }


  //FUNCION QUE VERIFICARA LA HORA Y DIA
  public void verificarFin(){

    String dia = detective.getDia();
    int hora = detective.getHour();
    int horasPasadas = detective.getTotalHours();
    Log.d("MAIN verificarFin", "horasPasadas " + horasPasadas);
    Log.d("MAIN verificarFin", "DIA = " + dia + " hora = " + hora);
    //154
    if(horasPasadas >= 154){
      //TODO: SPLASH SCREEN LADRON ESCAPANDO
      Log.d("MAIN verificarFin", "Es el ultimo dia de la semana " + dia + " hora = " + hora);
      String mensaje = "Los sentimos " + detective.getRank() + " no hay mas presupuesto para continuar " +
        "con la busqueda. \n" +
        "Parece que el ladron ha escapado. ";
      enviarMensajeInterfaceIntroYMision(mensaje);
    }else{
      //VERIFICAR SI SE ENCONTRO AL LADRON
      if(detective.encontrado){
        Log.d("MAIN verificarFin", "Encontrado es " +dia + " a las " + hora + " AUMENTAR NIVEL DEL DETECTIVE");

        if(detective.getSospechoso() != null)
          if(!detective.getSospechoso().isEmpty()){
          //TODO: SPLASH SCREEN LADRON CORRIENDO Y DETENIDO
          //SI HA ENCONTRADO AL LADRON Y LA DESCRIPCION COINCIDE
          if(detective.getSospechoso().contains(objetosSuspects.get(idLadron).getName())){
            aumentarNivelDetective();
            Log.d("MAIN verificarFin", "Sospechoso encontrado = " + detective.getSospechoso());
            String mensaje = "Felicitaciones " + detective.getName() + " ha recuperado el " + objetoRobado +
              " la agencia agradece su gestion y en recompensa  sera acendido a " + detective.getRank() +
              " esperamos continue con su buena gestion ";
            enviarMensajeInterfaceIntroYMision(mensaje);
          }else{
            //TODO: SPLASH SCREEN LADRON CAMINANDO LIBRE
            String mensaje = detective.getRank() + " " + detective.getName() + "\n" +
              "Parece que el sospechoso detenido no tiene el/la " + objetoRobado + "\n" +
              "No contamos con mas recursos para continuar con la mision";
            enviarMensajeInterfaceIntroYMision(mensaje);
          }
        //HA ENCONTRADO AL LADRON PERO NO HAY DESCRIPCION DEL SOSPECHOSOS
        }else{
            //TODO: SPLASH SCREEN LADRON CAMINANDO LIBRE
            String mensaje = "Lo sentimos " + detective.getRank() + " " + detective.getName() + "\n" +
              "Sin una orden de captura no podemos proceder al arresto " + "\n" +
              "...";
            enviarMensajeInterfaceIntroYMision(mensaje);
        }
      }
    }
  }



  //FUNCION PARA ENVIAR EL MENSAJE QUE EL LADRON ESCAPO
  public void enviarMensajeInterfaceIntroYMision(String mensaje){
    mExecutor.execute(() -> {
      //Background work here
      mHandler.post(() -> {
        setInterfaceAndAskNewMision(mensaje,"NUEVA_MISION","NUEVA_MISION");
      });
    });
  }

  //FUNCION QUE GENERA LA NUEVA MISION
  public void nuevaMision(){
    Log.d("MAIN nuevaMision", "INIT");

    String mensaje = "\n" + "****FLASH****" + "\n" + "\n" +
      "Nuevo evento:" + "\n" +
      "se ha reportado el robo del objeto:"+ "\n"  +
      objetoRobado + "\n"  +
      "en " + paisActual + "\n" + "\n" +
      "se sospecha de la banda V.I.L.E." +
      " Por favor dirijase al lugar, investigue y encuentre al sospechoso " +
      " tiene hasta el domingo antes de las 18:00 " + "\n" + "\n" +
      "Buena suerte";

    setInterfaceNewMision(mensaje, "ACEPTAR_MISION", "ACEPTAR");
  }

  //FUNCION QUE AUMENTA EL NIVEL DEL DETECTIVE CUANDO HA LOGRADO ENCONTRAR AL SOSPECHOSOS
  public void aumentarNivelDetective(){
    String name = detective.getName();
    Integer lvl = Integer.parseInt(detective.getNivel());
    String[] rangos = new String[]{"OFICIAL", "INVESTIGADOR", "INSPECTOR", "DETECTIVE"};
    if(lvl <= 4){
      lvl++;
      editor.putString("nivel"+name, String.valueOf(lvl));
      detective.setNivel(String.valueOf(lvl));
      editor.putString("rank"+name, rangos[lvl-1]);
      detective.setRank( rangos[lvl-1] );
    }
  }

  @Override
  public void terminoReloj() {
    Log.d("MAIN terminoReloj", "FINALIZO THREAD");
    //terminarThreadReloj();
    setPaisActual();
    activarOpciones();
    verificarFin();
  }
}