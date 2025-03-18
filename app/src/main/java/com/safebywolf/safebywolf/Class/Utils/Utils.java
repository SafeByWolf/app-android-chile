package com.safebywolf.safebywolf.Class.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.safebywolf.safebywolf.Activity.LoginActivity;
import com.safebywolf.safebywolf.Activity.TensorFlow.DetectorActivity;
import com.safebywolf.safebywolf.Class.Referencias;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safebywolf.safebywolf.Interface.JsonPlaceHolderApi;
import com.safebywolf.safebywolf.Model.TimeOffset;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    public static float leerValorFloat(Context context, String keyPref, float defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getFloat(keyPref, defaultValue);
    }

    public static int leerValorInt(Context context, String keyPref, int defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getInt(keyPref, defaultValue);
    }

    public static String leerValorString(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }

    public static Set<String> leerValorSetString(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        Set<String> hash_Set = new HashSet<String>();
        return  preferences.getStringSet(keyPref,hash_Set);
    }

    public static long leerValorLong(Context context, String keyPref, long defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);

        if(isLong(String.valueOf(preferences.getLong(keyPref, defaultValue)))) {
            return  preferences.getLong(keyPref, defaultValue);
        }
        return defaultValue;
    }

    public static boolean leerValorBoolean(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getBoolean(keyPref, false);
    }
    public static void guardarValorBoolean(Context context, String keyPref, boolean valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putBoolean(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorString(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorLong(Context context, String keyPref, Long valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putLong(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorInt(Context context, String keyPref, int valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putInt(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorFloat(Context context, String keyPref, float valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putFloat(keyPref, valor);
        editor.commit();
    }

    public static void guardarValorSetString(Context context, String keyPref, Set<String> valor) {
        SharedPreferences settings = context.getSharedPreferences(Referencias.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putStringSet(keyPref, valor);
        editor.commit();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ArrayList<String> convertirSetStringArrayListString(Set<String> set, ArrayList<String>arrayList) {
        for(int i=0;i<set.size();i++){
            //set.g
        }
        return arrayList;
    }

    public static boolean horaActualEsMayorAHoraLimite(String sHoraActual, String sHoraLimite2){
        if(sHoraLimite2 == null){
            return false;
        }
        String[] parts = sHoraActual.split(":");
        int horaActual=Integer.parseInt(parts[0]);
        int minutoActual=Integer.parseInt(parts[1]);
        //Log.v("sHoraLimite2",sHoraLimite2);
        parts = sHoraLimite2.split(":");
        int horaLimite=Integer.parseInt(parts[0]);
        int minutoLimite=Integer.parseInt(parts[1]);

        if (horaActual>horaLimite) {
            Log.v("configTotem", "es mayor horaActualEsMayorAHoraLimite");
            return true;
        }
        if (horaActual==horaLimite) {
            if(minutoActual>=minutoLimite){
                Log.v("configTotem", "es mayor horaActualEsMayorAHoraLimite");
                return true;
            }
        }
        Log.v("configTotem", "NO es mayor horaActualNOEsMayorAHoraLimite");
        return false;
    }

    public static boolean horaActualEsMenorAHoraInicio(String sHoraActual, String sHoraInicio2){
        String[] parts = sHoraActual.split(":");
        int horaActual=Integer.parseInt(parts[0]);
        int minutoActual=Integer.parseInt(parts[1]);
        parts = sHoraInicio2.split(":");
        int horaInicio=Integer.parseInt(parts[0]);
        int minutoInicio=Integer.parseInt(parts[1]);
        if (horaActual<horaInicio) {
            Log.v("configTotem", "es menor, horaActualEsMenorAHoraInicio");
            return true;
        }
        if (horaActual==horaInicio) {
            if(minutoActual<minutoInicio){
                Log.v("configTotem", "es menor, horaActualEsMenorAHoraInicio");
                return true;
            }
        }
        Log.v("configTotem", "NO es menor, horaActualNOEsMenorAHoraInicio");
        return false;
    }

    /**
     * Metodo que normaliza un texto, es decir, le quita los caracteres extraños y lo deja en Mayuscula
     * @param textToNormalize Texto a normalizar
     * @return Devuelve el texto original sin caracteres extraños como tildes y en mayusculas
     */
    public static String normalizarString(String textToNormalize) {
        if (textToNormalize == null) {
            return "";
        }

        // Elimina las tildes
        textToNormalize = Normalizer.normalize(textToNormalize, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Elimina los espacios en blanco iniciales y finales
        textToNormalize = textToNormalize.trim();

        // mayuscula
        textToNormalize = textToNormalize.toUpperCase();

        return textToNormalize;
    }

    public static String listMapToString(List<Map<String, String>> list){
        if (list == null){
            return null;
        }

        String gruposString = "";
        for (Map<String, String> map : list){
            String nombre = map.get("nombre");
            String tipo = map.get("tipo");
            String isAlertGroup = null;
            String alertGroupName = null;
            if(map.containsKey("isAlertGroup")) {
                isAlertGroup = map.get("isAlertGroup");
            }
            if(map.containsKey("alertGroupName")) {
                alertGroupName = map.get("alertGroupName");
            }

            String tipoNombre = nombre + "@" + tipo; // el tipo y el nombre del grupo se separa por @
            if(isAlertGroup != null){
                tipoNombre = tipoNombre + "@" + isAlertGroup;
            }

            if(alertGroupName != null){
                tipoNombre = tipoNombre + "@" + alertGroupName;
            }

            gruposString =  gruposString + tipoNombre + "#"; // cada item se separa con #
        }

        return gruposString;
    }

    public static List<Map<String, String>> stringToListMap(String gruposString){
        List<Map<String, String>> listGrupos = new ArrayList<>();
        String[] items = gruposString.split("#"); // separador de items

        for (String item : items){
            String[] itemsTipoNombre = item.split("@"); // separador de tipo y nombre
            Map<String, String> map = new HashMap<>();
            map.put("nombre", itemsTipoNombre[0]);
            map.put("tipo", itemsTipoNombre[1]);
            if(itemsTipoNombre.length > 2){
                map.put("isAlertGroup", itemsTipoNombre[2]);
            }
            if(itemsTipoNombre.length > 3){
                map.put("alertGroupName", itemsTipoNombre[3]);
            }

            listGrupos.add(map);
        }
        return listGrupos;
    }

    public static String patentePuntoSuspensivos(String patente){
        String patenteSeparadaPorPuntosSuspencivos = "";
        for (int x = 0; x < patente.length(); x++) {
            patenteSeparadaPorPuntosSuspencivos = patenteSeparadaPorPuntosSuspencivos + String.valueOf(patente.charAt(x)) + "...";
        }
        return patenteSeparadaPorPuntosSuspencivos;
    }

    public static int random(){
        final int min = 10000;
        final int max = 99999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        return random;
    }

    public static String parseDateToString(Date date, String pattern){
        //"dd-MM-yyyy"
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void logout(FirebaseFirestore db, Context context) {
        
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            //actualiza campos de logout del usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(user.getEmail())
                    .update("expiredSession", FieldValue.serverTimestamp(),
                            "tokenFirebaseInstalation", "",
                            "activo", "false",
                            "ultimaConexion", FieldValue.serverTimestamp());
        }

        guardarValorBoolean(context, Referencias.SESIONINICIADA, false);
        guardarValorSetString(context, Referencias.PATENTES, null);
        guardarValorString(context, Referencias.IDUSUARIO, null);
        guardarValorString(context, Referencias.ACCVERSION, null);
        guardarValorString(context, Referencias.TOKENFIREBASEINSTALATION, null);
        guardarValorString(context, Referencias.NOMBRE, null);
        guardarValorString(context, Referencias.APELLIDO, null);
        guardarValorString(context, Referencias.CORREO, null);
        guardarValorString(context, Referencias.CONTACTO, null);
        guardarValorString(context, Referencias.PASSWORD, null);
        guardarValorSetString(context, Referencias.GRUPO, null);
        guardarValorString(context, Referencias.NOMBREGRUPO, null);
        guardarValorString(context, Referencias.TIPOGRUPOS, null);
        guardarValorString(context, Referencias.GRUPOSSTRING, null);
        guardarValorString(context, Referencias.IP, null);
        guardarValorString(context, Referencias.MODELTYPE, null);
        Toast.makeText(context,"Sesión cerrada correctamente",Toast.LENGTH_SHORT).show();


        ejecutarLoginActivity(context);
    }

    public static void logoutSinUpdatear(Context context) {
        guardarValorBoolean(context,Referencias.SESIONINICIADA, false);
        guardarValorSetString(context,Referencias.PATENTES, null);
        guardarValorString(context, Referencias.IDUSUARIO, null);
        guardarValorString(context, Referencias.ACCVERSION, null);
        guardarValorString(context, Referencias.TOKENFIREBASEINSTALATION, null);
        guardarValorString(context, Referencias.NOMBRE, null);
        guardarValorString(context, Referencias.APELLIDO, null);
        guardarValorString(context, Referencias.CORREO, null);
        guardarValorString(context, Referencias.CONTACTO, null);
        guardarValorString(context, Referencias.PASSWORD, null);
        guardarValorSetString(context, Referencias.GRUPO, null);
        guardarValorString(context, Referencias.NOMBREGRUPO, null);
        guardarValorString(context, Referencias.TIPOGRUPOS, null);
        guardarValorString(context, Referencias.IP, null);
        guardarValorString(context, Referencias.MODELTYPE, null);
    }

    public static void ejecutarLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static Timestamp updateExpiredSession(FirebaseFirestore db){
        //actualiza ingreso
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Timestamp timestamp = new Timestamp(new Date(new CurrentDate(new Date()).getDate().getTime()+60000*60));
        if(user!=null){
            //actualiza el campo expiredSession del usuario (costo de escritura 1)
            db.collection(Referencias.USUARIO).document(user.getEmail())
                    .update("expiredSession", timestamp);
        }
        return timestamp;
    }

    public static boolean isBoolean(String cadena) {
        boolean resultado;
        try {
            Boolean.parseBoolean(cadena);
            resultado = true;
        } catch (Exception excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean isInt(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean isLong(String cadena) {
        boolean resultado;
        try {
            Long.parseLong(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean isDouble(String cadena) {
        boolean resultado;
        try {
            Double.parseDouble(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean isFloat(String cadena) {
        boolean resultado;
        try {
            Float.parseFloat(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public static void getServerTime(Context context, String API_URL, int i) {
        Log.v("dattimecs", "getServerTime" );
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call callTime = jsonPlaceHolderApi.getTime();
        try {
            Response response = callTime.execute();
            Date myTime = new Date();
            if (!response.isSuccessful()) {
                Log.v("dattimecs", "" + response.code());
                return;
            }
            Gson gson = new Gson();
            String jsonString = gson.toJson(response.body());
            Log.v("dattimecs", jsonString);
            try {
                JSONObject objeto = new JSONObject(jsonString);
                String datetime = objeto.getString("datetime");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date serverDate = dateFormat.parse(datetime);
                Log.v("dattimecsServer", "Utils: "+serverDate.toString());
                long offset = serverDate.getTime() - myTime.getTime();
                new TimeOffset(offset);
                Utils.guardarValorLong(context,Referencias.OFFSETTIME,TimeOffset.getOffset());
                Log.v("dattimecsServer", "offset utils:" + TimeOffset.getOffset());
            } catch (JSONException | ParseException e) {
                Log.v("dattimecs", "" +  e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setBrilloDePantalla(int brightness, Activity activity) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness =  brightness / (float)255;
        activity.getWindow().setAttributes(layoutParams);
    }

    public static void touchListener(View view, Activity activity){
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DetectorActivity miActivity = (DetectorActivity) activity;
                if (miActivity != null) {
                    miActivity.brilloDePantallaNormal();
                }
                return false;
            }
        });
    }
}
