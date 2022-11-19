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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Carmen Sandiego
 */

public class MainActivity extends AppCompatActivity{

  String TAG = "MAIN";
  SharedPreferences sharedPref;
  SharedPreferences.Editor editor;

  //INTEFACE IZQUIERDO SUPERIOR
  TextView tvPaisActual;
  TextView tvHoraActual;
  //INTERFACE IZQUIERDO INFERIOR
  LinearLayout llImageCountry;
  TextView tvTextTrivias;
  ScrollView svIntroMision;
  TextView tvWritingIntro;
  TextView tvIntroMision;
  EditText ETdetectiveName;
  Button btIniciar;

  //INTEFACE DERECHO
  TextView tvIntroSuspectTitulo;
  TextView TVInfoInvestigacion;
  ScrollView SVInfoSuspects;

  AutoCompleteTextView actvSex;
  AutoCompleteTextView actvHobby;
  AutoCompleteTextView actvHair;
  AutoCompleteTextView actvFeature;
  AutoCompleteTextView actvAuto;

  //INTERFACE INFERIOR OPCIONES
  GridLayout glMasterOptions;
  Button btConexiones;
  Button btViajar;
  Button btInvestigar;
  Button btInterpol;

  //VARIABLE DE INICIO SOSPECHOSOS
  ArrayList<Suspects> objetosSuspects;
  int idLadron;
  List<String> paisesVisitadosLadron = new ArrayList<String>();;
  String objetoRobado;

  //VARIABLES PAISES
  Map<String, Integer> nombrePaises = new HashMap<>();
  List<String> lugaresInvestigar  = new ArrayList<String>();
  ArrayList<Country> objetosPaises;

  //VARIABLES INVESTIGADOR
  String paisActual;
  Detective detective;
  List<String> destinos  = new ArrayList<String>();
  List<String> nomDestinos  = new ArrayList<String>();
  int regresarARuta = 0;

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
    //SUPERIOR
    tvPaisActual = findViewById(R.id.TVPaisActual);
    tvHoraActual = findViewById(R.id.TVHoraActual);
    //INFERIOR
    llImageCountry = findViewById(R.id.LLImageCountry);
    tvTextTrivias = findViewById(R.id.tvTextTrivias);
    svIntroMision = findViewById(R.id.SVIntroDetective);
    tvWritingIntro = findViewById(R.id.TVWriterIntro);
    tvIntroMision = findViewById(R.id.TVIntroMision);
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
    btIniciar = findViewById(R.id.btIniciar);
    btIniciar.setVisibility(View.INVISIBLE);

    //INICIALIZAR VARIABLES DE INTERFACE DERECHA
    //INFORMACION TESTIGOS Y NOTAS PAIS
    TVInfoInvestigacion = findViewById(R.id.TVInfoInvestigation);
    //INFORMACION INTERPOL SOSPECHOSOS
    SVInfoSuspects = findViewById(R.id.SVInfoSuspects);
    tvIntroSuspectTitulo = findViewById(R.id.TVInfoSuspectsTitulo);

    setInfoInvestVisible();

    actvSex = findViewById(R.id.ACsex);
    actvHobby = findViewById(R.id.AChobby);
    actvHair = findViewById(R.id.AChair);
    actvFeature = findViewById(R.id.ACfeature);
    actvAuto = findViewById(R.id.ACauto);

    setAutoCompleteTextViewsOptions();

    //INTERFACE OPCIONES INFERIORES
    glMasterOptions = findViewById(R.id.GLMasterOptions);
    glMasterOptions.setVisibility(View.INVISIBLE);
    btConexiones = findViewById(R.id.btConexiones);
    btViajar = findViewById(R.id.btViajar);
    btInvestigar = findViewById(R.id.btInvestigar);
    btInterpol = findViewById(R.id.btInterpol);

  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    moveTaskToBack(true);
  }

  //FUNCION PARA INICIAR EL JUEGO
  public void initGame(){
    setInfoInvestVisible();
    initMision();
    //INICIALIZAR DETECTIVE
    initDetective(ETdetectiveName.getText().toString());
    //INICIALIZAR PAISES
    initCountries();
    //INICIALIZAR SOSPECHOSOS
    initSuspects();
    //SET TRIVIAS
    setTrviasCountry();

    enviarTextoEntrada();
  }

  //INICIAR ELEMENTOS DEL LAYOUT DEL LADO IZQUIRDO
  public void initMision(){
    glMasterOptions.setVisibility(View.INVISIBLE);
    llImageCountry.setVisibility(View.INVISIBLE);
    svIntroMision.setVisibility(View.VISIBLE);
    btIniciar.setVisibility(View.INVISIBLE);
  }
  public void initImage(){
    glMasterOptions.setVisibility(View.VISIBLE);
    llImageCountry.setVisibility(View.VISIBLE);
    svIntroMision.setVisibility(View.INVISIBLE);
  }
  //FUNCION DE RESPUESTA AL BOTON INICIAR AL FINAL DEL TEXTO MISION
  public void initGame(View view){
    //ESCONDER VIEWS
    initImage();
    setTVPaisActual();
  }

  //SET LINEAR LAYOUTS INFO
  public void setInfoInvestVisible(){
    TVInfoInvestigacion.setVisibility(View.VISIBLE);
    TVInfoInvestigacion.setText("");
    tvIntroSuspectTitulo.setVisibility(View.INVISIBLE);
    SVInfoSuspects.setVisibility(View.INVISIBLE);
  }
  public void setInfoSuspectstVisible(){
    TVInfoInvestigacion.setVisibility(View.INVISIBLE);
    TVInfoInvestigacion.setText("");
    tvIntroSuspectTitulo.setVisibility(View.VISIBLE);
    SVInfoSuspects.setVisibility(View.VISIBLE);
  }
  //OPCIONES DE SELECCION DE CARACTERISTICAS DE LOS SOSPECHOSOS
  public void setAutoCompleteTextViewsOptions(){
    ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.sexs);
    actvSex.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.hobbys);
    actvHobby.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.haircolors);
    actvHair.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.features);
    actvFeature.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.autos);
    actvAuto.setAdapter(adapter);
  }

  //INICIALIZAR OBJETO DETECTIVE
  public void initDetective(String name){
    //PREGUNTAS INICIALES AL DETECTIVE
    //TODO OBTENER INFROMACION DE SHERED PREFERENCES
    //String detectiveName = sharedPref.getString("name", "");
    String lvl = sharedPref.getString("nivel"+name, null);
    String rank = sharedPref.getString("rank"+name, null);

    if(lvl == null){      lvl = "1";      editor.putString("nivel"+name, "1");    }
    if(rank == null){ rank = "OFICIAL";   editor.putString("rank"+name, "OFICIAL"); }

    //OFICIAL, INVESTIGADOR, INSPECTOR, DETECTIVE

    /*
    Gumshoe	4	Start
    Jr. Investigator	5	1
    Investigator	6	4
    Sr. Investigator	7	7
    Inspector	8	10
    Sr. Inspector	9	14
    Jr. Detective	10	18
    Detective	11	23
    Sr. Detective	12	28
    Master Detective	13	33
    Super Sleuth	14	Catch Carmen
   */

    detective = new Detective();
    detective.setNivel(lvl);
    detective.setRank(rank);
    detective.setName(name);

    setTVHoraActual();
  }

  //INICIALIZAR OBJECTOS PAISES
  public void initCountries(){
    objetosPaises = UtilityClassCountries.getInstance().getList();
    for(int i = 0; i < objetosPaises.size(); i++){
      nombrePaises.put(objetosPaises.get(i).getName(), i);
    }
  }

  //INICIALIZAR OBJECTOS SOSPECHOSOS
  public void initSuspects(){
    objetosSuspects = UtilityClassSuspects.getInstance().getList();

    //NUMERO ALEATORIO PARA SELECCIONAR UN LADRON
    idLadron = new Random().nextInt(objetosSuspects.size());

    //SET LADRON
    //objetosSuspects[idLadron].getClass().getMethod("setLadron").invoke(objetosSuspects[idLadron]);
    objetosSuspects.get(idLadron).setThief();
    //Object total = objetosSuspects[idLadron].getClass().getMethod("getTotalPaises").invoke(objetosSuspects[idLadron]);

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
    lugaresInvestigar = new ArrayList<String>(Arrays.asList(lugares));
    Log.d("MAIN lugaresInvetigar", "lugaresInvetigar = " + Arrays.toString(lugares));

  }

  //SET TV PAIS ACTUAL
  public void setTVPaisActual(){
    int index = nombrePaises.get(paisActual);
    String capital = objetosPaises.get(index).getCapital();
    tvPaisActual.setText(capital);
  }

  //SET TV HORA ACTUAL
  public void setTVHoraActual(){
    String hora = detective.getTime();
    tvHoraActual.setText(hora);
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
      tvTextTrivias.setText(objetosPaises.get(index).getTrivia(trivia));
    }
  }

  //SET TV IMAGE OF COUNTRY
  public void setImageCountry(){
    Log.d("setImageCountry", "setImageOfCountry");
  }

  //FUNCION QUE GENERARA LOS DESTINOS POSIBLES
  public void siguientesPaises(String paisActual){

    String siguientePaisLadron = null;
    destinos = new ArrayList<String>();

    //Log.d("siguientesPaises", "ACTUAL = " + paisActual);

    //1. SI EL PAIS ACTUAL ES UNO DE LOS VISITADOS POR EL LADRON, AÑADA EL SIGUIENTE DESTINO EN LA
    //RUTA DEL LADRON, SI ES EL ULTIMO, AÑADA EL ANTERIOR.
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

    //2. SI EL PAIS ACTUAL NO ES UNO DE LOS VISITADOS POR EL LADRON, AÑADA EL ULTIMO PAIS VISITADO POR EL
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

    //AÑADIR EL SIGUIENTE PAIS VISITADO
    destinos.add(siguientePaisLadron);
    //VARIABLES QUE CONTENDRAN LOS PAISES RELACIONADOS
    Integer cantidadPaisesRelacionados = null;
    String[] paisesRelacionados = null;

    //CICLO PARA AÑADIR LOS POSIBLES DESTINOS
    boolean terminarDeAnadir = false;
    int index;
    String pais;
    int indexPaisAñadido = 0;
    int ingresados = 0;
    int maximo = 0;
    int intentosPaisRelacionado = 0;
    String nivel = detective.getNivel();
    int random;
    if(nivel.contains("1")){maximo = 2;}
    else if(nivel.contains("2")){maximo = 3;}
    while(!terminarDeAnadir){
      if(destinos.size() > 3){
        terminarDeAnadir = true;
      }else{
        //Log.d("siguientesPaises", "destinos[" +indexPaisAñadido+"] = " + destinos.get(indexPaisAñadido) );
        Log.d("siguientesPaises", "indexPaisAñadido = "+indexPaisAñadido);
        Log.d("siguientesPaises", "destinos = "+destinos.get(indexPaisAñadido));
        index = nombrePaises.get(destinos.get(indexPaisAñadido));
        //TRAER LOS PAISES RELACIONADOS CON EL PAIS ACTUAL INGRESADO
        cantidadPaisesRelacionados = objetosPaises.get(index).getTotalRelatedCountries();
        paisesRelacionados = (String[]) objetosPaises.get(index).getAllRelatedCountries();

          //AÑADIR PAISES RELACIONADO CON EL DESTINO DEPENDIENDO DEL NIVEL
        if (cantidadPaisesRelacionados != null && paisesRelacionados != null && ingresados < maximo
        && intentosPaisRelacionado < 10) {
          random = new Random().nextInt(cantidadPaisesRelacionados - 1);
          pais = paisesRelacionados[random];
          intentosPaisRelacionado++;
          //Log.d("siguientesPaises", "RANDOM = "+ random + " PAIS RELACIONADO = " + Country);
        }else{
          //AÑADIR UN PAIS ALEATORIO
          pais = paisAleatorioDelTotal();
          //Log.d("siguientesPaises", "PAIS ALEATORIO = " + Country);
        }

        if(pais != null && !destinos.contains(pais) && !pais.contains(paisActual)){
          destinos.add(pais);
          indexPaisAñadido++;
          ingresados++;
        }
      }
    }
    //DAR UN ORDEN ALEATORIO A LA LISTA
    Collections.shuffle(destinos);
    Log.d(TAG, "destinos = " + destinos);
    obtenerNombreDestinos();
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
      index = nombrePaises.get(destinos.get(i));
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
    int indexActual = nombrePaises.get(Actual);
    int indexDestino = nombrePaises.get(destino);
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

    desactivarOpciones();
    animarReloj(diferenciaReal, tiempoViaje);

    String mensajeViaje = "Distancia a viajar = " + (int) distanciasPaises + "\n" +
      "Tiempo de viaje = " + tiempoViaje + "\n" +
      "Tiempo descanso = " + (diferenciaReal - tiempoViaje);
    Util.makeToast(this, mensajeViaje, 0 );

    siguientesPaises(paisActual);

  }

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

  //FUNCION QUE CAMBIA EL RELOJ SEGUN EL CALCULO DEL NUEVO TIEMPO
  public void animarReloj(Integer diferenciaReal, Integer tiempoViaje){

    new Thread() {
      public void run() {
        for (int i = 0; i < diferenciaReal; i++) {
          try {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                String text = Util.newDayHour(detective.getHour(), detective.getDia());
                //String text = diasSemana[indexDiaSiguiente] + "/" + horaPresentar + "/" +franjaHoraria + "/" + hora;
                detective.setDia(text.split("/")[0]);
                detective.setHourToShow(text.split("/")[1]);
                detective.setAMPM(text.split("/")[2]);
                detective.setHour(Integer.parseInt(text.split("/")[3]));
                tvHoraActual.setText(detective.getTime());
              }
            });
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
        setTVPaisActual();
        activarOpciones();
      }
    }.start();
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

    animarReloj(diferenciaReal, tiempoDeViaje);
    setTVPaisActual();
    piastasEnElLugar(lugar);

  }

  //FUNCION QUE MUESTRA LAS PISTAS OBTENIDAS EN EL LUGAR A INVESTIGAR
  public void piastasEnElLugar(String lugar){

    String[] personas = new String[]{"Guardia de seguridad", "Peaton", "Vendedor", "Policia local",
    "Ciclista"};
    int randomPersonas =  new Random().nextInt(personas.length);
    int randomVestimenta =  new Random().nextInt(objetosSuspects.get(idLadron).getTotalClothing() - 1);

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
      if(!objetosPaises.get(indexCountrySiguiente).getMoneda().contains("Dolar")){
        pistas.add("Vi a alguien sospechoso queriendo cambiar Dolares a " + objetosPaises.get(indexCountrySiguiente).getMoneda());
      }
      //CLOTHING
      pistas.add("Vi a alguien sospechoso que vestia " +  objetosSuspects.get(idLadron).getClothing(randomVestimenta) +" de color " +
        objetosSuspects.get(idLadron).getClothingColor(randomVestimenta));
      //FLAG
      pistas.add("Vi que llevaba una bandera con los colores " + objetosPaises.get(indexCountrySiguiente).getClue("flag"));
      //COUNTRY PRODUCTION
      int randomProduccion =  new Random().nextInt(objetosPaises.get(indexCountrySiguiente).getCluesProd());
      pistas.add("Escuche a alguien hablar de un pais con un mercado grande de " +
        objetosPaises.get(indexCountrySiguiente).getClue("prod"+randomProduccion));
      //AUTO
      if(!objetosSuspects.get(idLadron).getAuto().isEmpty()){
        pistas.add("Me parecio escuchar a alguien preguntando donde podia alquilar un " + objetosSuspects.get(idLadron).getAuto());
      }
      //HAIR COLOR & SEX
      if(!objetosSuspects.get(idLadron).getHaircolor().isEmpty()){
        if(objetosSuspects.get(idLadron).getSex().contains("mujer")){
          pistas.add("Vi a una mujer sospechoso con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
        }else if(objetosSuspects.get(idLadron).getSex().contains("hombre")){
          pistas.add("Vi a un hombre sospechoso con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
        }else{
          pistas.add("Vi a alguien sospechoso con cabello de color " + objetosSuspects.get(idLadron).getHaircolor());
        }
      }
      //FAVORITE FOOD
      if(!objetosSuspects.get(idLadron).getFavoriteFood().isEmpty()){
        pistas.add("Escuche a alguien preguntando donde podia comer " + objetosSuspects.get(idLadron).getFavoriteFood());
      }
      //FEATURE
      if(!objetosSuspects.get(idLadron).getFeature().isEmpty()){
        pistas.add("Vi a alguien sospechoso con " + objetosSuspects.get(idLadron).getFeature());
      }
      //HOBBIE
      if(!objetosSuspects.get(idLadron).getHobby().isEmpty()){
        pistas.add("Escuche a alguien hablando que le gustaba " + objetosSuspects.get(idLadron).getHobby().isEmpty());
      }
      //NADIE HA VISTO ALGO
      pistas.add("No he visto nada sospechoso por aqui.");

    //SI EL LADRON ESTUVO EN ESTE PAIS Y ES EL ULTIMO
    }else if(indexRuta != -1 & indexRuta == sizePaisesVisitados){
      //SI EL DETECTIVE ESTA EN EL MISMO LUGAR DEL LADRON ENTONCES
      if(objetosSuspects.get(idLadron).getLastPlace().contains(lugar)){
        pistas.add("Ahi esta quien buscas!");
        String mensajeFinal = "Ladron Encontrado";
        Util.makeToast(this, mensajeFinal, 1);
      }else{
        pistas.add("Hace poco vi a alguien sospechoso por acá");
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
    mostrarPista(mensaje);

  }

  //FUNCION QUE CARGARA LA PISTA GENERADA EN EL TEXTVIEW
  public void mostrarPista(String mensaje){
    TVInfoInvestigacion.setText(mensaje);
  }


  //FUNCION DE RESPUESTA A BOTON CONEXIONES
  public void conexiones(View view){
    //VER POSIBLES DESTINOS
    setInfoInvestVisible();
    construirAlertaConexiones("CONEXIONES");
  }

  //FUNCION DE RESPUESTA A BOTON VIAJAR
  public void viajar(View view){
    //VER POSIBLES DESTINOS
    setInfoInvestVisible();
    construirAlertaConexiones("VIAJAR");
  }

  //FUNCION DE RESPUESTA A BOTON INVESTIGAR
  public void investigar(View view){
    //VER LUGARES DEL PAIS ACTUAL QUE SE PUEDEN VISITAR
    setInfoInvestVisible();
    construirAlertaConexiones("INVESTIGAR");
  }

  //FUNCION DE RESPUESTA A BOTON INTERPOL
  public void interpol(View view){
    //1.HACER VISIBLE EL LINEARLAYOUT CON LAS OPCIONES
    setInfoSuspectstVisible();
  }

  //FUNCION DE RESPUESTA A BOTON BUSCAR
  public void buscarSospechoso(View view){
    Log.d("INTERPOL", "sex = " + actvSex.getText().toString());
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
      " Por favor dirijase al lugar e investigue," +
      " tiene hasta el domingo antes de las 18:00 " + "\n" + "\n" +
      "Buena suerte";
    typing(texto);
    btIniciar.setVisibility(View.VISIBLE);
  }

  //FUNCION QUE SIMULA EL INGRESO DEL TEXTO COMO MAQUINA DE ESCRIBIR
  public void typing(String newText){

    new Thread() {
      public void run() {
        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.typewriter);
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
                tvIntroMision.setText(finalText);
                svIntroMision.fullScroll(View.FOCUS_DOWN);
              }
            });
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          mp.pause();
        }
        mp.seekTo(0);
        mp.release();
      }
    }.start();
  }


  //FUNCION QUE VERIFICARA LA HORA Y DIA
  public void verificarFin(){
    String dia = detective.getDia();
    int hora = detective.getHour();
    if(dia.contains("DOMINGO") & hora >= 18){
      //TERMINA EL JUEGO
      endGame();
    }else{
      aumentarNivelDetective();
    }
  }

  //FUNCION TERMINAR EL JUEGO
  public void endGame(){
    Log.d("END GAME", "...");
    reiniciarJuego();
  }

  //FUNCION QUE AUMENTA EL NIVEL DEL DETECTIVE CUANDO HA LOGRADO ENCONTRAR AL SOSPECHOSOS
  public void aumentarNivelDetective(){

    String name = detective.getName();
    Integer lvl = Integer.parseInt(detective.getNivel());
    String[] rangos = new String[]{"OFICIAL", "INVESTIGADOR", "INSPECTOR", "DETECTIVE"};
    if(lvl <= 4){
      lvl++;
      editor.putString("nivel"+name, String.valueOf(lvl));
      editor.putString("rank"+name, rangos[lvl-1]);
    }
    reiniciarJuego();
  }

  //FUNCION PARA REINICIAR EL JUEGO
  public void reiniciarJuego(){

  }



}