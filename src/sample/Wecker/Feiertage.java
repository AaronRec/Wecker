package sample.Wecker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public interface Feiertage {

    default boolean isFeiertag() {
        GregorianCalendar today = new GregorianCalendar();

        int jahr = today.get(Calendar.YEAR);

        int a = jahr % 19;
        int b = jahr % 4;
        int c = jahr % 7;
        int monat = 0;

        int m = (8 * (jahr / 100) + 13) / 25 - 2;
        int s = jahr / 100 - jahr / 400 - 2;
        m = (15 + s - m) % 30;
        int n = (6 + s) % 7;

        int d = (m + 19 * a) % 30;

        if (d == 29)
            d = 28;
        else if (d == 28 && a >= 11)
            d = 27;

        int e = (2 * b + 4 * c + 6 * d + n) % 7;

        int tag = 21 + d + e + 1;

        if (tag > 31) {
            tag = tag % 31;
            monat = 3;
        }
        if (tag <= 31) {
            monat = 2;
        }

        GregorianCalendar gc_ostersonntag = new GregorianCalendar(jahr, monat, tag);
        GregorianCalendar gc_ostermontag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 1));
        GregorianCalendar gc_karfreitag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) - 2));
        GregorianCalendar gc_rosenmontag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) - 48));
        GregorianCalendar gc_christihimmelfahrt = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 39));
        GregorianCalendar gc_pfinstsonntag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 49));
        GregorianCalendar gc_pfinstmontag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 50));
        GregorianCalendar gc_frohnleichnahm = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 60));
        GregorianCalendar gc_wiedervereinigung = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 9, 1);
        GregorianCalendar gc_weihnachten_1 = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 24);
        GregorianCalendar gc_weihnachten_2 = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 25);
        GregorianCalendar gc_weihnachten_3 = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 26);
        GregorianCalendar gc_silvester = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 31);
        GregorianCalendar gc_neujahr = new GregorianCalendar(gc_silvester.get(Calendar.YEAR), 0, 1);

        if (gc_pfinstsonntag.equals(today) ||gc_ostermontag.equals(today) || gc_karfreitag.equals(today) || gc_rosenmontag.equals(today) || gc_christihimmelfahrt.equals(today) || gc_pfinstmontag.equals(today) || gc_frohnleichnahm.equals(today) || gc_weihnachten_1.equals(today) || gc_weihnachten_2.equals(today) || gc_weihnachten_3.equals(today) || gc_silvester.equals(today) || gc_neujahr.equals(today) || gc_wiedervereinigung.equals(today)) {
            return true;

        } else {
            return false;
        }
    }
}
