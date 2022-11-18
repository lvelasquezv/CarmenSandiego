package com.vv.carmensandiego;

import java.util.Map;

public interface FirebaseListener {
  void onSuccesFirebaseInitialData(String name, Integer countryNumber, Map<String, Object> data);
  void onSuccesFirebaseInfoData(String name, Integer countryNumber, String infoData, Map<String, Object> data);
  void onCompleteFirebaseData();
  void onFailFirebaseData();
}
