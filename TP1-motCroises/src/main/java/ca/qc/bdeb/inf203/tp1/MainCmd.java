package ca.qc.bdeb.inf203.tp1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MainCmd {
    static MotsCroises motsCroises = new MotsCroises();
    static int verificateur = 1;

    public static void main(String[] args) throws IOException {
        motsCroises.tableauDesMots = motsCroises.transfereFichier("mots-croises1.txt");

        ajusterLaGrilleSelonLigneCmd();
        afficherGrille();
        afficherIndices();
        do {
            communicationAvecUtilsateur();
        } while (!validerSiJeuTermine());
    }

    /**
     * Cette méthode ajuste la grille selon la ligne de commande, C.-À-D, elle met des points et des ? selon les positions
     * et la longeur des mots
     */
 public static void ajusterLaGrilleSelonLigneCmd() {
        motsCroises.grille = motsCroises.faireLaGrille();
        if (!motsCroises.validerSiConflitDeLettre(motsCroises.grille, motsCroises.tableauDesMots)) {
            motsCroises.quitterProgramme();
        } else {
            int numeroLigne;
            int numeroColonne;
            int taiileMot;

            for (int i = 0; i < motsCroises.largeurGrilleHorizontal; i++) {
                for (int j = 0; j < motsCroises.longeurGrilleVertical; j++) {
                    motsCroises.grille[i][j] = '.';
                }
            }
            for (int i = 0; i < motsCroises.tableauDesMots.size(); i++) {
                int cpt = 1;
                numeroLigne = motsCroises.tableauDesMots.get(i).getNumeroLigne();
                numeroColonne = motsCroises.tableauDesMots.get(i).getNumeroColone();
                taiileMot = motsCroises.tableauDesMots.get(i).getMot().length();
                motsCroises.grille[numeroLigne][numeroColonne] = (char) ((char) 48 + i);
                if (motsCroises.tableauDesMots.get(i).isEstVertical()) {
                    while (cpt != taiileMot) {

                        motsCroises.grille[numeroLigne + cpt][numeroColonne] = '?';
                        cpt++;
                    }
                } else {
                    while (cpt != taiileMot) {

                        motsCroises.grille[numeroLigne][numeroColonne + cpt] = '?';
                        cpt++;
                    }
                }
            }
        }
    }

    /**
     * Cette méthode s'en occupe de la communication,elle prends les entrés de l'utilsateur, appelle les méthode de verification et
     * la méthode de placer.
     */
 public static void communicationAvecUtilsateur() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Quel mot voulez-vous deviner?");
        System.out.println("q pour quitter, s pour avoir la solution)");
        String index = sc.nextLine();
        if (index.equals("q")) {
            System.exit(0); //https://stackoverflow.com/questions/12117160/terminate-a-console-application-in-java
        } else if (index.equals("s")) {
            afficherSolution();
            System.exit(0);
        } else {

            if (motsCroises.validerIndex(Integer.parseInt(index)) && !motsCroises.tableIndexDejaRentre.contains(index)) {

                System.out.print("Tentative  :");
                String mot = sc.nextLine();
                boolean x;
                x = validermotLigneCmd(Integer.parseInt(index), mot);
                if (x) {

                    motsCroises.tableIndexDejaRentre.add(index);
                    motsCroises.placerMot(Integer.parseInt(index), mot);
                    // à chaque fois qu'un mot est validé et accepté, le verificateur s'incrimente de 1.
                    // le jeu termine quand le verificateur atteint la taille de arrayListe de mot ( voir javaDoc de la méthode
                    // en bas pour plus de détails.)
                   verificateur++;
                    afficherGrille();
                    System.out.println("Bonne reponse");
                    afficherIndices();
                } else System.out.println("Entree invalide, essayez encore!");


            }
        }
    }
    /**
     * Cette méthode vérifie si le verificateur n'a pas dépassé la taille de arrayListe des mots
     * @return true si la verificateur ne dépasse pas la taille.
     */
    public static boolean validerSiJeuTermine() {


        return verificateur > motsCroises.tableauDesMots.size();
    }

    /**
     * Cette méthode affiche la grille dans la ligne de commandee
     */

 public static void afficherGrille() {
        for (char[] x :
                motsCroises.grille) {
            System.out.println(Arrays.toString(x));

        }
    }

    /**
     * Cette méthode affiche les définition des mots dans la ligne de command
     */

 public static void afficherIndices() {

        for (int i = 0; i < motsCroises.tableauDesMots.size(); i++) {
            System.out.println(i + "- " + motsCroises.tableauDesMots.get(i).getDefenition());

        }

    }

    /**
     * cette méthode place tout les mots dans la grille, après elle appel la méthode afficherGrille() pour
     afficher la grille sur la ligne de commande.
     */

 public static void afficherSolution() {
        for (int i = 0; i < motsCroises.tableauDesMots.size(); i++) {
            motsCroises.placerMot(i, motsCroises.tableauDesMots.get(i).getMot());


        }
        afficherGrille();

    }

    /**
     *
     * @param z index de mot
     * @param mot un mot
     * @return true si le mot passé est égale au mot à l'index z (l'ordre de mot dans le arrayListe) .
     */

 public static boolean validermotLigneCmd(int z, String mot) {
        boolean motValide;

        if (!mot.equalsIgnoreCase(motsCroises.tableauDesMots.get(z).getMot())) {
            return false;

        } else {
            mot = motsCroises.tableauDesMots.get(z).getMot();
            motValide = motsCroises.grille[motsCroises.tableauDesMots.get(z).getNumeroLigne()][motsCroises.tableauDesMots.get(z).getNumeroColone()] == (char) ((char) z + 48);
        }
        return motValide;
    }




}


