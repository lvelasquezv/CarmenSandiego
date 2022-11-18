package com.vv.carmensandiego.paises;

import android.util.Log;

/**
 * Listado de datos y paises
 * Las coordenadas deben ser ingresadas en grados decimales para calcular la distancia entre ciudades
 */
public class Chile {
  String TAG = "CHI";

  final String nombre;
  final String region;
  final String bandera;
  final String moneda;
  final String capital;
  final String aeropuerto;
  final String cambioDolar;

  final String[] pistas = {"NA","NA","NA"};
  final String[] pistasUsadas = {"false","false","false"};

  final String[] trivias = {"NA","NA","NA"};
  final String[] triviaUsadas = {"false","false","false"};

  final String[] objetoRobado = {
    "CHI1",
    "CHI2",
    "CHI3"};

  final String[] lugares = {"Museo", "Aeropuerto", "Estación Buses"};

  final String[] paisesRelacionados = {"Argentina", "Peru", "Bolivia"};

  final String latitud;
  final String longitud;

  public Chile() {

    Log.d(TAG,"INIT");

    this.nombre = "Chile";
    this.region = "América Latina";
    this.bandera = "Azul, Blanco, Rojo";
    this.moneda = "Peso";
    this.capital = "Santiago";
    this.aeropuerto = "Arturo Merino Benitez";
    this.cambioDolar = "0.00020";
    this.latitud = "-33.39944428167937";
    this.longitud = "-70.79276588205691";

  }

  public String getNombre()                   {    return nombre;               }
  public String getRegion()                   {    return region;               }
  public String getBandera()                  {    return bandera;              }
  public String getMoneda()                   {    return moneda;               }
  public String getCapital()                  {    return capital;              }
  public String getAeropuerto()               {    return aeropuerto;           }
  public String getCambioDolar()              {    return cambioDolar;          }
  public String getPista(Integer index)           {    return pistas[index];        }
  public String getPistasUsadas(Integer index)    {    return pistasUsadas[index];  }
  public String getTrivia(Integer index)          {    return trivias[index];       }
  public String getTriviaUsadas(Integer index)    {    return triviaUsadas[index];  }
  public String getObjeto(Integer index)          {    return objetoRobado[index];  }
  public Integer getCantidadObjetos()          {   return this.objetoRobado.length; }
  public String getLatitud()                  {    return latitud;              }
  public String getLongitud()                 {    return longitud;             }
  public Integer getCantidadPaisesRelacionados(){return paisesRelacionados.length; }
  public String[] getTodosLosPaisesRelacionados(){   return paisesRelacionados;   }
  public String getPaisesRelacionados(Integer index) {   return paisesRelacionados[index]; }
  public String[] getLugares(){ return lugares; }
}
