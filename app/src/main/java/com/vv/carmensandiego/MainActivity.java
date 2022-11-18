package com.vv.carmensandiego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.vv.carmensandiego.detective.Detective;

import java.lang.reflect.InvocationTargetException;

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

  //INTEFACE
  TextView tvPaisActual;
  TextView tvHoraActual;
  TextView tvWritingIntro;
  TextView tvIntroMision;
  TextView tvIntroSuspectTitulo;
  EditText ETdetectiveName;

  TextView TVInfoInvestigacion;
  ScrollView SVInfoSuspects;
  AutoCompleteTextView actvSex;
  AutoCompleteTextView actvHobby;
  AutoCompleteTextView actvHair;
  AutoCompleteTextView actvFeature;
  AutoCompleteTextView actvAuto;

  //VARIABLE DE INICIO SOSPECHOSOS
  Object[] objetosSuspects = new Object[util.nombreObjetoSospechosos.length];
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

    //INICIALIZAR VARIABLES DE INTERFACE
    tvPaisActual = findViewById(R.id.TVPaisActual);
    tvHoraActual = findViewById(R.id.TVHoraActual);
    tvWritingIntro = findViewById(R.id.TVWriterIntro);
    tvIntroMision = findViewById(R.id.TVIntroMision);
    ETdetectiveName = findViewById(R.id.ETDetectiveName);
    ETdetectiveName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE){
          // Your action on done
          Log.d("MAIN", "Detective");
          enviarTextoEntrada();
        }
        return false;
      }
    });
    TVInfoInvestigacion = findViewById(R.id.TVInfoInvestigation);
    SVInfoSuspects = findViewById(R.id.SVInfoSuspects);
    tvIntroSuspectTitulo = findViewById(R.id.TVInfoSuspectsTitulo);
    setInfoInvestVisible();
    actvSex = findViewById(R.id.ACsex);
    actvHobby = findViewById(R.id.AChobby);
    actvHair = findViewById(R.id.AChair);
    actvFeature = findViewById(R.id.ACfeature);
    actvAuto = findViewById(R.id.ACauto);
    setAutoCompleteTextViewsOptions();

    //PREGUNTAS INICIALES AL DETECTIVE
    detective = new Detective();
    detective.setNivel("1");
    setTVHoraActual();

    //INICIALIZAR LUGARES
    initCountries();

    //INICIALIZAR SOSPECHOSOS
    initSuspects();
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

  //INICIALIZAR OBJECTOS PAISES
  public void initCountries(){
    objetosPaises = UtilityClassCountries.getInstance().getList();
    for(int i = 0; i < objetosPaises.size(); i++){
      nombrePaises.put(objetosPaises.get(i).getName(), i);
      Log.d("initCountries", objetosPaises.get(i).getName());
    }

    /*
    for(int i = 0; i < util.nombreObjetoPaises.length; i++){
      String nombre = util.nombreObjetoPaises[i];
      try {
        Class<?> clazz = Class.forName("com.vv.carmensandiego.paises." + nombre);
        //objetosPaises[i] = clazz.getDeclaredConstructor().newInstance();
        //SE GUARDA AL NOMBRE DEL OBJETO Y EL INDICE PARA FACILIDAD DE UBICACION
        nombrePaises.put(nombre, i);
      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
        NoSuchMethodException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

     */
  }

  //INICIALIZAR OBJECTOS SOSPECHOSOS
  public void initSuspects(){
    //LOOP POR LOS SOSPECHOSOS
    for(int i = 0; i < util.nombreObjetoSospechosos.length; i++){
      String nombre = util.nombreObjetoSospechosos[i];
      try {
        Class<?> clazz = Class.forName("com.vv.carmensandiego.suspects." + nombre);
        objetosSuspects[i] = clazz.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                NoSuchMethodException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    /* SELECCIONA UN LADRON
      https://stackoverflow.com/questions/942326/calling-static-method-on-a-class
      Method method = className.getDeclaredMethod("getInfo", String.class);
      method.invoke(instance, "your parameter");
      Where instance is either: Object instance = null; if the method is static. Or:
      Object instance = className.getDeclaredConstructor().newInstance();
      If the method is a member method
     */
    //NUMERO ALEATORIO PARA SELECCIONAR UN LADRON
    idLadron = new Random().nextInt(util.nombreObjetoSospechosos.length);
    try {
      //SET LADRON
      objetosSuspects[idLadron].getClass().getMethod("setLadron").invoke(objetosSuspects[idLadron]);
      //SET DISTANCIAS ENTRE LUGARES PARA CALCULO DE TIEMPO
      Object total = objetosSuspects[idLadron].getClass().getMethod("getTotalPaises").invoke(objetosSuspects[idLadron]);
      Log.d(TAG, "TOTAL PAISES VISITADOS " + total);

      for(int i = 0; i < (Integer) total; i++){

        paisesVisitadosLadron.add ((String) objetosSuspects[idLadron].getClass().getMethod("getPaisVisitado", Integer.class).invoke(objetosSuspects[idLadron], i));
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
          Log.d(TAG, "Objeto robado = " + objetoRobado);
        }
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    siguientesPaises(paisActual);
  }

  //ASIGNAR PAIS ACTUAL
  public void setPaisActual(String pais){
    paisActual = pais;
    detective.addPaisVisitado(paisActual);
    //EL PAIS ACTUAL ESTA EN LA RUTA DEL LADRON ??
    Boolean enLaRuta = false;
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

    /*
    try{
      //String[] lugares = (String[]) objetosPaises[index].getClass().getMethod("getLugares").invoke(objetosPaises[index]);
      assert lugares != null;
      lugaresInvestigar = new ArrayList<String>(Arrays.asList(lugares));
      Log.d("MAIN lugaresInvetigar", "lugaresInvetigar = " + Arrays.toString(lugares));
    }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

     */

  }

  //SET TV PAIS ACTUAL
  public void setTVPaisActual(){
    int index = nombrePaises.get(paisActual);
    String capital = objetosPaises.get(index).getCapital();
    tvPaisActual.setText(capital);
    /*
    try{
      String capital = (String) objetosPaises[index].getClass().getMethod("getCapital").invoke(objetosPaises[index]);
      tvPaisActual.setText(capital);
    }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

     */
  }

  //SET TV HORA ACTUAL
  public void setTVHoraActual(){
    String hora = detective.getTime();
    tvHoraActual.setText(hora);
  }

  //SET LINEAR LAYOUTS INFO
  public void setInfoInvestVisible(){
    TVInfoInvestigacion.setVisibility(View.VISIBLE);
    tvIntroSuspectTitulo.setVisibility(View.INVISIBLE);
    SVInfoSuspects.setVisibility(View.INVISIBLE);
  }
  public void setInfoSuspectstVisible(){
    TVInfoInvestigacion.setVisibility(View.INVISIBLE);
    tvIntroSuspectTitulo.setVisibility(View.VISIBLE);
    SVInfoSuspects.setVisibility(View.VISIBLE);
  }
  //OPCIONES DE SELECCION DE CARACTERISTICAS DE LOS SOSPECHOSOS
  public void setAutoCompleteTextViewsOptions(){
    ArrayAdapter<String> adapter;
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, util.sexs);
    actvSex.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, util.hobbys);
    actvHobby.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, util.haircolors);
    actvHair.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, util.features);
    actvFeature.setAdapter(adapter);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, util.autos);
    actvAuto.setAdapter(adapter);
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
      if(destinos.size() > maximo){
        terminarDeAnadir = true;
      }else{
        //Log.d("siguientesPaises", "destinos[" +indexPaisAñadido+"] = " + destinos.get(indexPaisAñadido) );
        index = nombrePaises.get(destinos.get(indexPaisAñadido));
        //TRAER LOS PAISES RELACIONADOS CON EL PAIS ACTUAL INGRESADO
        cantidadPaisesRelacionados = objetosPaises.get(index).getTotalRelatedCountries();
        paisesRelacionados = (String[]) objetosPaises.get(index).getAllRelatedCountries();

        /*
        try{
          cantidadPaisesRelacionados = (Integer) objetosPaises[index].getClass().getMethod("getCantidadPaisesRelacionados").invoke(objetosPaises[index]);
          paisesRelacionados = (String[]) objetosPaises[index].getClass().getMethod("getTodosLosPaisesRelacionados").invoke(objetosPaises[index]);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
          e.printStackTrace();
        }
         */
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
      /*
      try{
        nomDestinos.add((String) objetosPaises[index].getClass().getMethod("getNombre").invoke(objetosPaises[index]));
      }catch(Exception e){
        e.printStackTrace();
      }

       */
    }
    Log.d(TAG, "NOMBRE DESTINOS = "+ nomDestinos);
  }

  //CALCULO DEL TIEMPO DE VIAJE
  public void viajarAPais(String Actual, String destino){

    //1. CAMBIAR EL PAIS ACTUAL
    setPaisActual(destino);

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
    /*
    try{
      latitud1 = Double.parseDouble((String) objetosPaises[indexActual].getClass().getMethod("getLatitud").invoke(objetosPaises[indexActual]));
      longitud1 = Double.parseDouble((String) objetosPaises[indexActual].getClass().getMethod("getLongitud").invoke(objetosPaises[indexActual]));

      latitud2 = Double.parseDouble((String) objetosPaises[indexDestino].getClass().getMethod("getLatitud").invoke(objetosPaises[indexDestino]));
      longitud2 = Double.parseDouble((String) objetosPaises[indexDestino].getClass().getMethod("getLongitud").invoke(objetosPaises[indexDestino]));
    }catch(Exception e){
      e.printStackTrace();
    }

     */

    distanciasPaises = util.haversine(latitud1, longitud1, latitud2, longitud2);
    Log.d(TAG, "Distancia "+ distanciasPaises);

    tiempoViaje = (int) util.tiempoViaje(distanciasPaises);
    Log.d(TAG, "Tiempo de viaje "+ tiempoViaje);

    int nuevaHora = util.nuevaHora(detective.getHour(),  tiempoViaje);
    int diferenciaReal = util.diferenciaReal(nuevaHora, tiempoViaje);

    animarReloj(diferenciaReal, tiempoViaje);

    String mensajeViaje = "Distancia a viajar = " + (int) distanciasPaises + "\n" +
      "Tiempo de viaje = " + tiempoViaje + "\n" +
      "Tiempo descanso = " + (diferenciaReal - tiempoViaje);
    util.makeToast(this, mensajeViaje, 0 );

    siguientesPaises(paisActual);

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
                String text = util.newDayHour(detective.getHour(), detective.getDia());
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

    int nuevaHora = util.nuevaHora(detective.getHour(),  tiempoDeViaje);
    int diferenciaReal = util.diferenciaReal(nuevaHora, tiempoDeViaje);

    animarReloj(diferenciaReal, tiempoDeViaje);
    setTVPaisActual();

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
      "Tu rango actual es:" + "\n" + detective.getRank() +"\n"+ "\n" +
      "****FLASH****" + "\n" + "\n" +
      "Ojbeto robado en. ....";
    typing(texto);
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
            Log.d("TYPING", "Text = " + tvIntroMision.getText().toString());
            mp.start();
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                tvIntroMision.setText(finalText);
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

}