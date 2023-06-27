import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class H15 {
    private Vec[] veci;
    private int n;
    private int r;
    private int K; // hmotnost batohu

    private Vec[] batoh;
    private int ucelovaFunkcia;
    private int aktualnaHmotnost;
    private int aktualnyPocetVeci;

    public H15(int n, int r, int K){
        this.n = n; //pocet prvkov
        this.K = K; // kapacita batohu
        this.r = r; // pocet predmetov potrebnych v batohu
        this.veci = new Vec[this.n];
        this.batoh = new Vec[this.n];
        this.aktualnaHmotnost = 0;
        this.aktualnyPocetVeci = 0;
        this.ucelovaFunkcia = 0;
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

    public void dualHeuristika(){
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
        System.out.println("Účelová funkcia: " + hodnota + "\nHmotnosť batohu: " + vaha
                + "\nPocet veci v batohu: " + pocetVeci);
        this.zapisBatoh();
    }

    public void vypisBatoh(){
        for(Vec v : this.batoh){
            if(v!= null) { //velkost tohto pola je n ale pole nemusi byt upne zaplnene
                System.out.println(v.getIndex() + " " + v.getC() + " " + v.getA() + " " + v.getKoeficient());
            }
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
