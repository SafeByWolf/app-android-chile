package com.safebywolf.safebywolf.Activity.TensorFlow;

import com.safebywolf.safebywolf.Class.Utils.Log;
import com.safebywolf.safebywolf.Class.Utils.Utils;

import java.text.Normalizer;

public class UtilsOCR {
    static String letrasPermitidas = "BCDFGHJKLMNPRSTVWXYZ";
    static String vocales = "AEIOU";
    static String letrasPatenteAntigua= letrasPermitidas.concat(vocales);

    public static String hasText(String text){
        text = quitaEspaciosEnBlanco(quitaGuion(Utils.normalizarString(text)));
        if(text != null && !text.equalsIgnoreCase("")){
            return text;
        }
        return null;
    }

    public static String esPatenteChilena(String patente){
        patente = quitaEspaciosEnBlanco(quitaGuion(Utils.normalizarString(patente)));
        //Log.v("pasdja", "************************ antes de ver el length: " + patente);
        if (patente.length() != 6) {
            return null;
        }

        //separo patente por letra
        String qA = patente.substring(0, 1);
        String qB = patente.substring(1, 2);
        String qC = patente.substring(2, 3);
        String qD = patente.substring(3, 4);
        String qE = patente.substring(4, 5);
        String qF = patente.substring(5, 6);

        if(!isInteger(qE) || !isInteger(qF)){
            return null;
        }

        //patente antigua TA8318
        if(isInteger(qC) ){
            //qD no puede ser letra
            if(!isInteger(qD)){
                return null;
            }
            //Log.v("asas",letrasPermitidas);
            if(!letrasPatenteAntigua.contains(qA) || !letrasPatenteAntigua.contains(qB)){
                return null;
            }

        } else {
            //patente nueva CBVJ45
            //qD no puede ser numero
            if(isInteger(qD)){
                return null;
            }

            if(!letrasPermitidas.contains(qA)
                    || !letrasPermitidas.contains(qB)
                    || !letrasPermitidas.contains(qC)
                    ||!letrasPermitidas.contains(qD)){
                return null;
            }
        }

        return patente;
    }

    public static String quitaEspaciosEnBlanco(String textToNormalize){
        textToNormalize = textToNormalize.replaceAll(" ","");
        return textToNormalize;
    }

    public static String quitaGuion(String textToNormalize){
        textToNormalize = textToNormalize.replaceAll("-","");
        return textToNormalize;
    }

    public static boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
