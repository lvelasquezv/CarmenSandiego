package com.vv.carmensandiego;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Funciones y variables para el desarrollo del juego
 */


public class util{

  //CARACTERISTICAS DE LOS SOSPECHOSOS
  public static final String[] sexs = {"male", "female", "non binary"};
  public static final String[] hobbys = {"tennis", "music", "mt climbing", "skydiving", "swimming", "croquet", "football"};
  public static final String[] haircolors = {"brown","blond","red","black"};
  public static final String[] features = {"limps", "ring", "tattoo", "scar", "jewelry" };
  public static final String[] autos = {"convertible","limousine","race car", "motorcycle"};
  public static final String[] clothings = {"Jacket", "jeans", "suit", "cap", "scarf", "boots", "coat", "tennis", "hat"};
  public static final String[] clothingColors = {"white", "red", "black", "gray", "dark blue"};

  //PAISES
  public static final String[] nombreObjetoPaises = {"Argentina",
    "Bolivia",
    "Brasil",
    "Chile",
    "Colombia",
    "Ecuador",
    "Panama",
    "Paraguay",
    "Peru",
    "Uruguay",
    "Venezuela"};

  //SOSPECHOSOS
  public static final String[] nombreObjetoSospechosos = {"CarmenSandiego"};

  //DIAS DE LA SEMANA
  public static final String[] diasSemana = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};

  //FUNCION QUE CALCULA LA DISTANCIA ENTRE DOS CIUDADES SEGUN LA LATITUD Y LONGITUD EN GRADOS DECIMALES
  public static Double haversine(Double lat1s, Double long1s, Double lat2s, Double long2s){
    double lat1 = lat1s   * 3.14159265/180;
    double lat2 = lat2s   * 3.14159265/180;
    double long1 = long1s * 3.14159265/180;
    double long2 = long2s  * 3.14159265/180;
    return 2 * 6378 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat2 - lat1) / 2), 2.0) +
      Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((long2 - long1) / 2), 2.0)));
  }

  //FUNCION QUE CALCULA EL TIEMPO DE VIAJE
  public static int tiempoViaje(Double distancia){
    //3 horas de espera en aeropuerto
    //2 horas para llegar al aeropuerto
    return (int) (distancia/500.0 + 3.0 + 2.0);
  }

  //FUNCION QUE SELECCIONA LA CANTIDAD DE PAISES SEGUN EL NIVEL SELECCIONADO
  public static List<String> paisesVisitados(String nivel) {
    int cantidadPaises = 0;
    List<Integer> randoms = new ArrayList<>();
    List<String> visitados = new ArrayList<>();
    //NIVEL 1
    if(nivel.equals("1")){
      cantidadPaises = 3;
    }

    //1. VERIFICAR QUE SE INGRESEN LOS PAISES DESEADOS
    while(visitados.size() < cantidadPaises){
      boolean siguiente = false;
      //2. VERIFICAR QUE ESE PAIS NO HA SIDO VISITADO ANTES A MENOS QUE SEA HACE 2 VIAJES
      while (!siguiente){
        int random = new Random().nextInt(nombreObjetoPaises.length);
        //Log.d("Util paisesVisitados", "random = " + random + " Pais = " + nombreObjetoPaises[random]);
        if(!randoms.contains(random)){
          randoms.add(random);
          visitados.add(nombreObjetoPaises[random]);
          siguiente = true;
          //PUEDE REGRESAR SI EL PAIS FUE VISITADO HACE MAS DE DOS VIAJES
        }else if(randoms.indexOf(random) < (randoms.size() - 3)){
          randoms.add(random);
          visitados.add(nombreObjetoPaises[random]);
          siguiente = true;
        }
      }
    }

    return visitados;
  }

  //FUNCION QUE CALCULARA LA NUEVA HORA Y DIA DE LA SEMANA
  public static int nuevaHora(Integer inicial, Integer adicional){
    int sumaHoras = inicial + adicional;
    int dias = (sumaHoras/24);
    double horaActualD = ((sumaHoras/24.0) - dias) * 24;
    return (int) Math.round(horaActualD);
  }

  //FUNCION QUE CALCULA LA DIFERENCIA HORARIA REAL TENIENDO EN CUENTA LA HORA DE APERTURA DE LOS
  //LUGARES A INVESTIGAR
  public static int diferenciaReal(Integer horaFinal, Integer adicional){
    int horasAdicional = 0;
    if(horaFinal >= 22){
      horasAdicional = (24 - horaFinal + 8);
    }else if(horaFinal < 8){
      horasAdicional = (8 - horaFinal);
    }
    return adicional + horasAdicional;
  }

  //FUNCION QUE CALCULA LAS DIFERENCIAS HORARIAS Y EN DIAS
  public static String newDayHour(int hora, String dia){
    //GENERADOR DEL SONIDO
    ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    //LOOP PARA ENCONTRAR EL INDICE DEL DIA ACTUAL EN ARRAY DIASSEMANA
    int indexDiaActual = 0;
    for(int i = 0; i < diasSemana.length; i++){
      if(diasSemana[i].contains(dia)){
        indexDiaActual = i;
      }
    }
    int indexDiaSiguiente = indexDiaActual;

    String franjaHoraria = "";
    String horaPresentar = "";
    //VERIFICACION DE VARIABLES DEPENDIENDO DE LA HORA
    hora = hora + 1;
    if(hora >= 24){    indexDiaSiguiente += 1; hora = hora - 24; if(indexDiaSiguiente > 6){indexDiaSiguiente = 0;}; }
    if(hora >= 12){    franjaHoraria = "P.M.";    }else{      franjaHoraria = "A.M.";    }
    if(hora > 12){     horaPresentar = String.valueOf(hora - 12); }else{horaPresentar = String.valueOf(hora);}

    toneGen.startTone(ToneGenerator.TONE_PROP_BEEP,200);

    toneGen.stopTone();

    String text = diasSemana[indexDiaSiguiente] + "/" + horaPresentar + "/" +franjaHoraria + "/" + hora;
    Log.d("NUEVO HORARIO", text );
    toneGen.release();
    return text;
  }


  //INICIAL ES HORA EN FORMATO 24h
  public static String nuevaHoraDia(Integer inicial, Integer adicional, String dia, View view){

    ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    TextView tvDayHour = (TextView) view;

    Log.d("UTIL nuevaHoraDia", "DIA HORA INICIAL = " + dia +" "+ inicial);

    int indexDiaActual = 0;
    for(int i = 0; i < diasSemana.length; i++){
      if(diasSemana[i].contains(dia)){
        indexDiaActual = i;
      }
    }

    String franjaHoraria = "";
    int sumaHoras = inicial + adicional;
    int horasAdicional = 0;
    int dias = (sumaHoras/24);
    double horaActualD = ((sumaHoras/24.0) - dias) * 24;
    //Log.d("UTIL nuevaHoraDia", "HoraActual double = " + horaActualD);
    int horaActual = (int) Math.round(horaActualD);
    //Log.d("UTIL nuevaHoraDia", "HoraActual = " + horaActual);
    if(horaActual >= 20){
      horasAdicional = (24 - horaActual + 8);
    }else if(horaActual < 8){
      horasAdicional = (8 - horaActual);
    }
    int diferenciaReal = adicional + horasAdicional;

    Log.d("UTIL nuevaHoraDia", "Horas adicionales = " + horasAdicional);

    int hora = inicial;
    int indexDiaSiguiente = indexDiaActual;
    String horaPresentar = "";
    for(int i = 0; i < diferenciaReal; i++){
      hora = hora + 1;
      if(hora >= 24){    indexDiaSiguiente += 1; hora = hora - 24; if(indexDiaSiguiente > 6){indexDiaSiguiente = 0;}; }
      if(hora >= 12){    franjaHoraria = "P.M.";    }else{      franjaHoraria = "A.M.";    }
      if(hora > 12){     horaPresentar = String.valueOf(hora - 12); }else{horaPresentar = String.valueOf(hora);}
      try {
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP,200);
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      toneGen.stopTone();
      String text = diasSemana[indexDiaSiguiente] + " " + horaPresentar +" "+franjaHoraria;
      Log.d("NUEVO HORARIO", text );

      tvDayHour.setText(text);

    }

    toneGen.release();

    int horaReal = hora;
    horaActual = Integer.parseInt(horaPresentar);
    String diaSemana = diasSemana[indexDiaSiguiente];

    //Log.d("UTIL nuevaHoraDia", "DIA HORA FINAL = " + diaSemana +" "+ horaReal);

    return diaSemana +"/"+ horaActual +"/"+ franjaHoraria + "/" + horaReal + "/" + horasAdicional;

  }

  //TOAST CON TEXTO CENTRADO
  public static void makeToast(Context context, String mensaje, int duration){
    //duration = 0 = Toast.LENGTH_SHORT
    //duration = 1 = Toast.LENGTH_LONG
    Spannable centeredText = new SpannableString(mensaje);
    centeredText.setSpan(
      new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
      0, mensaje.length() - 1,
      Spannable.SPAN_INCLUSIVE_INCLUSIVE
    );

    Toast toast = Toast.makeText(context,centeredText, duration);
    toast.show();
  }

}
