import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class H15_vylepsenie {
    private Vec[] veci;
    private int n;
    private int r;
    private int K; // hmotnost batohu

    private Vec[] batoh;
    private int ucelovaFunkcia;
    private int aktualnaHmotnost;
    private int aktualnyPocetVeci;

    private final static int MAX_INDEX = 999999999;

    public H15_vylepsenie(int n, int r, int K){
        this.n = n; //pocet prvkov
        this.K = K; // kapacita batohu (minmalna hmotnost batohu)
        this.r = r; // pocet predmetov potrebnych v batohu
        this.veci = new Vec[this.n];
        this.batoh = new Vec[this.n];
        this.aktualnaHmotnost = 0;
        this.aktualnyPocetVeci = 0;
        this.ucelovaFunkcia = 0;
        // nacitanie veci do zoznamu veci
        try {
            Scanner scannerA = new Scanner(new File("src/H5_a.txt"));
            Scanner scannerC = new Scanner(new File("src/H5_c.txt"));
            for (int i = 0; i < n; i++) {
                int a = scannerA.nextInt();
                int c = scannerC.nextInt();
                this.veci[i] = new Vec(i + 1, a, c);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Súbor sa nenašiel!");
            return;
        }
    }
    public void vypisVeci(){
        for(Vec v : this.veci){
            System.out.println(v.getIndex() + " " + v.getC() + " " + v.getA() + " " + v.getKoeficient());
        }
    }

    private void dualHeuristika(){
        int pocetVeci = 0;
        int vaha = 0;
        int hodnota = 0;
        int i = 0;
        while (true) {
            if(this.K <= vaha && this.r <= pocetVeci){
                break;
            }
            double minKoeficient = Double.MAX_VALUE;
            Vec najVec = null;
            for (Vec vec : this.veci) {
                if (!vec.isSpracovany()) {
                    double koeficient = vec.getKoeficient();
                    if (koeficient < minKoeficient) {
                        minKoeficient = koeficient;
                        najVec = vec;
                    }
                }
            }
            if (najVec == null) {
                break;
            }
            pocetVeci++;
            vaha += najVec.getA();
            hodnota += najVec.getC();
            this.batoh[i] = najVec;
            i++;
            najVec.setSpracovany(true);
        }
        this.aktualnaHmotnost = vaha;
        this.ucelovaFunkcia = hodnota;
        this.aktualnyPocetVeci = pocetVeci;
        System.out.println("Pôvodna heuristika:\nÚčelová funkcia: " + hodnota + "\nHmotnosť batohu: " + vaha
                + "\nPočet veci v batohu: " + pocetVeci);
        this.zapisBatoh();
    }

    public void vypisBatoh(){
        for(Vec v : this.batoh){
            if(v!= null) { //velkost tohto pola je n ale pole nemusi byt upne zaplnene
                System.out.println(v.getIndex() + " " + v.getC() + " " + v.getA() + " " + v.getKoeficient());
            }
        }
    }

    public void dualHeuristikaVylepsenie()
    {
        this.dualHeuristika();
        int hodnota = 0;
        int index = 0;
        boolean nasielSaPrvyVyhodny = false;
        int hmotnost = this.aktualnaHmotnost;
        Vec[] veciMimoBatohu = new Vec[this.veci.length];
        for (Vec v : this.veci) {
            if(!this.containsBatoh(v) && v != null){
                veciMimoBatohu[index] = v;
                index++;
            }
        }
        for (Vec v : this.batoh) {
            if(v == null){
                break;
            }
            for (Vec v2 : veciMimoBatohu) {
                if (v2 != null) {
                    hodnota = v2.getC() - v.getC() + this.ucelovaFunkcia;
                    hmotnost = this.aktualnaHmotnost - v.getA() + v2.getA();
                    if (hodnota < this.ucelovaFunkcia && this.K <= hmotnost) {
                        nasielSaPrvyVyhodny = true;
                        this.batoh[this.dajIndexVeciVBatohu(v)] = v2;
                        break;
                    }
                }
            }
            if(nasielSaPrvyVyhodny){
                break;
            }
        }
        this.ucelovaFunkcia = hodnota;
        if(nasielSaPrvyVyhodny) {
            System.out.println("----------------------------------------\n" +
                    "Vylepšena hueristika\nÚčelová funkcia: " + hodnota + "\nHmotnosť batohu: " + hmotnost
                    + "\nPočet veci v batohu: " + this.aktualnyPocetVeci);
        } else {
            System.out.println("Vylepsenie sa nenaslo :( \n");
        }
        this.zapisBatohVylepsenie();
    }

    private boolean containsBatoh(Vec vec){
        for (Vec v: this.batoh) {
            if(vec == v){
                return true;
            }
        }
        return false;
    }

    private int dajIndexVeciVBatohu(Vec v){
        for (int i = 0; i < this.batoh.length; i++) {
            if(v != null){
                if(this.batoh[i] == v){
                    return i;
                }
            }
        }
        return MAX_INDEX;
    }

    private void zapisBatohVylepsenie(){
        try {
            FileWriter zapisovac = new FileWriter("src/VysledokVylepsenie.txt");
            String zaznam = "";
            int i = 1;
            zapisovac.write( "Vylepšena hueristika\nÚčelová funkcia: " + this.ucelovaFunkcia + "\nHmotnosť batohu: "
                    + this.aktualnaHmotnost + "\nPočet veci v batohu: " + this.aktualnyPocetVeci + "\n");
            for (Vec v : this.batoh) {
                if(v != null){
                    zaznam = i + ". Vec, Cena: " + v.getC() + ", Hmotnosť: " + v.getA() + "\n";
                    zapisovac.write(zaznam);
                    i++;
                }
            }
            zapisovac.close();

        } catch (IOException e) {
            System.out.println("Nastala nejaka chyba!!!!");
            e.printStackTrace();
        }
    }

    private void zapisBatoh(){
        try {
            FileWriter zapisovac = new FileWriter("src/Vysledok.txt");
            String zaznam = "";
            int i = 1;
            zapisovac.write( "Pôvodna hueristika\nÚčelová funkcia: " + this.ucelovaFunkcia + "\nHmotnosť batohu: "
                    + this.aktualnaHmotnost + "\nPočet veci v batohu: " + this.aktualnyPocetVeci + "\n");
            for (Vec v : this.batoh) {
                if(v != null){
                    zaznam = i + ". Vec, Cena: " + v.getC() + ", Hmotnosť: " + v.getA() + "\n";
                    zapisovac.write(zaznam);
                    i++;
                }
            }
            zapisovac.close();

        } catch (IOException e) {
            System.out.println("Nastala nejaka chyba!!!!");
            e.printStackTrace();
        }
    }
}
