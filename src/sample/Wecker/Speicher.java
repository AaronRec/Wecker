package sample.Wecker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public interface Speicher {
    File src = new File("src/sample/Wecker/speicher.json");

    default void eintragSpeichern(boolean[] days, int hour, int minute) {
        String List = null;

        Scanner sc = null;
        try {
            sc = new Scanner(src);

            if(!sc.hasNextLine()){
                List = "[]";
            }
            else {
                List = sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject newobj = new JSONObject("{\"0\":" + days[0] + ",\"1\":" + days[1] + ",\"2\":" + days[2] + ",\"3\":" + days[3] + ",\"4\":" + days[4] + ",\"5\":" + days[5] + ",\"6\":" + days[6] + ",\"Hour\":" + hour + ",\"Minute\":" + minute + ",\"_UID\":"+ ((int)(Math.random() * 99999)) +"}");

        JSONArray obj = new JSONArray(List);
        obj.put(newobj);

        try {
            FileWriter fw = new FileWriter(src);
            fw.write(String.valueOf(obj));
            fw.close();
        } catch (IOException e) {
            System.out.println("An error");
            e.printStackTrace();
        }
    }

    default int getAnzahlWecker(){
        String List = "[]";

        Scanner sc = null;
        try {
            sc = new Scanner(src);

            if(!sc.hasNextLine()){
                List = "[]";

            }
            else {
                List = sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (List.equals("[]")){
            return 0;
        }
        JSONArray obj = new JSONArray(List);

        return obj.length();
    }

    default String getWeckerData(int wecker){
        String List = null;

        Scanner sc = null;
        try {
            sc = new Scanner(src);

            if(!sc.hasNextLine()){
                List = "[]";
            }
            else {
                List = sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (List.equals("[]")){
            return null;
        }
        JSONArray obj = new JSONArray(List);
        obj.get(wecker);

        int size = obj.length();
        ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
        for (int i = 0; i < size; i++) {
            JSONObject another_json_object = obj.getJSONObject(i);
            arrays.add(another_json_object);
        }

        JSONObject[] jsons = new JSONObject[arrays.size()];
        arrays.toArray(jsons);

        StringBuilder days = new StringBuilder();
        for (int k = 0; k < 7;k++){
            if (arrays.get(wecker).get(String.valueOf(k)).equals(true)) {
                switch (k) {
                    case 0:
                        days.append("Mon");
                        break;
                    case 1:
                        days.append("Tue");
                        break;
                    case 2:
                        days.append("Wed");
                        break;
                    case 3:
                        days.append("Thu");
                        break;
                    case 4:
                        days.append("Fri");
                        break;
                    case 5:
                        days.append("Sat");
                        break;
                    case 6:
                        days.append("Sun");
                        break;
                }
                if (k < 6) {
                    days.append(" ");
                }
            }
        }

        String back = arrays.get(wecker).get("Hour") +":"+arrays.get(wecker).get("Minute")+ " " +days;

        return back;
    }

    default boolean deleteWecker(int wecker){

        String List = null;

        Scanner sc = null;
        try {
            sc = new Scanner(src);

            if(!sc.hasNextLine()){
                List = "[]";
            }
            else {
                List = sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONArray obj = new JSONArray(List);

        obj.remove(wecker);

        try {
            FileWriter fw = new FileWriter(src);
            fw.write(String.valueOf(obj));
            fw.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error");
            e.printStackTrace();
            return false;
        }
    }
}
