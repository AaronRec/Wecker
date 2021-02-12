package sample.Wecker;

import java.util.Date;

public interface WeckerAPI extends Speicher, Feiertage {
    default Date getDate() {
        return new Date();
    }

    default boolean timewatcher(){
        String[] temp = getDate().toString().split(" ");


        for (int i = 0; i < getAnzahlWecker(); i++) {
            if (getWeckerData(i) != null) {
                String[] test = getWeckerData(i).split(" ");
                String[] UrzeitWecker = getWeckerData(i).split(" ")[0].split(":");

                String[] UrzeitJetzt = getDate().toString().split(" ")[3].split(":");
                Integer[] UrzeitWeckerInt = new Integer[2], UrzeitJetztInt = new Integer[2];


                for (int sad = 0; sad < 2; sad++) {
                    UrzeitJetztInt[sad] = Integer.valueOf(UrzeitJetzt[sad]);
                    UrzeitWeckerInt[sad] = Integer.valueOf(UrzeitWecker[sad]);
                }

                for (int tage = 1; tage < test.length; tage++) {
                    if (test[tage].equals(temp[0]) && UrzeitJetztInt[0].equals(UrzeitWeckerInt[0]) && UrzeitJetztInt[1].equals(UrzeitWeckerInt[1]) && !isFeiertag()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
