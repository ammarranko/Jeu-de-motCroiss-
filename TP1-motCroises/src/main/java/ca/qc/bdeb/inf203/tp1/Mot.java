package ca.qc.bdeb.inf203.tp1;

public class Mot {
    private String mot;
    private int numeroColone;

    public String getMot() {
        return mot;
    }

    public int getNumeroColone() {
        return numeroColone;
    }

    public int getNumeroLigne() {
        return numeroLigne;
    }


    public String getDefenition() {
        return defenition;
    }

    public String[] getX() {
        return x;
    }

    private int numeroLigne;

    public boolean isEstVertical() {
        return estVertical;
    }

    private boolean estVertical;
    private String defenition;
    private String[] x;


    public Mot(String[] x) {
        this.mot = x[0];
        this.numeroColone = Integer.parseInt(x[1]);
        this.numeroLigne = Integer.parseInt(x[2]);
        this.estVertical = x[3].equalsIgnoreCase("V");

        this.defenition = x[4];
    }
}
