public class Vec {
    private int index;
    private int a; // hmotnost
    private int c; //cena

    private double koeficient;
    private boolean spracovany;

    public Vec(int index, int a, int c){
        this.index = index;
        this.a = a;
        this.c = c;
        this.koeficient = (double)c/a;
        this.spracovany =false;
    }

    public int getIndex() {
        return index;
    }

    public int getA() {
        return a;
    }

    public int getC() {
        return c;
    }

    public double getKoeficient() {
        return koeficient;
    }

    public void setSpracovany(boolean spracovany) {
        this.spracovany = spracovany;
    }

    public boolean isSpracovany() {
        return spracovany;
    }
}
