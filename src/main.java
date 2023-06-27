public class main {
    public static void main(String[] args) {
        // povodne zadanie H15 bez vylepsenia
        //H15 h = new H15(500,350,9500);
        //h.dualHeuristika();

        //  povodne zadanie H15 aj s vylepsnenim vymennej heuristiky  (Vypis a porovnaie)
        H15_vylepsenie h15_vylepsenie = new H15_vylepsenie(500,350,9500);
        h15_vylepsenie.dualHeuristikaVylepsenie();
    }

}
