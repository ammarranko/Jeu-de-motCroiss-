package ca.qc.bdeb.inf203.tp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MotsCroises {

    int longeurGrilleVertical = 0;
    int largeurGrilleHorizontal = 0;
    char[][] grille;

    ArrayList<String> tableIndexDejaRentre = new ArrayList<>();
    ArrayList<Mot> tableauDesMots = new ArrayList<>();

    /**
     * @param nomFichier C'est le nom de Fichier mot-croises
     *                   <p>
     *                   Cette méthode transfert le contenu d'un Fichier de mot croisés dans un
     *                   arrayListe<Mot> et elle le retourne. En même temps, elle vérifie si
     *                   le fichier est valdie.C-À-D s'il manque un V ou un H sur la ligne ou
     *                   si la ligne ne contient pas 4 deux points.
     * @return
     * @
     */
    public ArrayList<Mot> transfereFichier(String nomFichier) {
        ArrayList<Mot> tab = new ArrayList<>();
        try {
            String[] tableau;
            BufferedReader fichier = new BufferedReader(new FileReader(nomFichier));
            String ligne;
            tableau = new String[0];

            while ((ligne = fichier.readLine()) != null) {
                if (ligne.charAt(0) == '#') {
                    fichier.readLine();
                } else {
                    if (ligne.contains(":") && verifierSiLigneContient4DeuxPoints(ligne)) {
                        tableau = ligne.split(":");
                        tab.add(new Mot(tableau));
//
                    } else {

                        quitterProgramme();
                    }
                    if (tableau[3].equals("") || (!tableau[3].equals("H")) && (!tableau[3].equals("V"))) {
                        quitterProgramme();
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        return tab;
    }

    /**
     * Cette méthode place le mot passé en paramétre dans la grille de mots croisés
     *
     * @param positionMot position de mot dans le arrayListe des mots
     * @param mot         le mot
     */

    public void placerMot(int positionMot, String mot) {
        mot = tableauDesMots.get(positionMot).getMot();
        for (int i = 0; i < mot.length(); i++) {
            if (tableauDesMots.get(positionMot).isEstVertical()) {
                int positionDepartHorizontalMot = tableauDesMots.get(positionMot).getNumeroLigne();
                int positionDepartVerticalMot = tableauDesMots.get(positionMot).getNumeroColone();

                grille[positionDepartHorizontalMot + i][positionDepartVerticalMot] = mot.charAt(i);

            } else {
                grille[tableauDesMots.get(positionMot).getNumeroLigne()][tableauDesMots.get(positionMot).getNumeroColone() + i] = mot.charAt(i);
            }

        }

    }

    /**
     * Cette méthode crée la grille de mots croisés. Elle additionne la longeur de chaque mot avec sa position initial.
     * Et elle les ajoutent dans un array List des entiers selon s'ils étaient Vertcales ou Horizontales. Après,elle
     * appele la méthode @trouverMaxDansArrayList pour trouver la valeur maximal dans les deux tableaux. Ces valeurs
     * seront respictivement la largeur et le longeur de la grille.
     * La vérification de ConflitLettre est faite dans cette méthode.
     *
     * @return
     */
    public char[][] faireLaGrille() {
        ArrayList<Integer> tailleDesMotsHoriz = new ArrayList<>();
        ArrayList<Integer> tailleDesMotsVertical = new ArrayList<>();
        for (Mot tableauDesMot : tableauDesMots) {
            if (tableauDesMot.isEstVertical()) {
                tailleDesMotsHoriz.add(tableauDesMot.getNumeroLigne() + tableauDesMot.getMot().length());
            } else {
                tailleDesMotsVertical.add(tableauDesMot.getNumeroColone() + tableauDesMot.getMot().length());

            }

        }

        largeurGrilleHorizontal = trouverMaxDansArrayList(tailleDesMotsHoriz);
        longeurGrilleVertical = trouverMaxDansArrayList(tailleDesMotsVertical);


        grille = new char[largeurGrilleHorizontal][longeurGrilleVertical];
        if (!validerSiConflitDeLettre(grille, tableauDesMots)) {
            quitterProgramme();
        }
        return grille;
    }

    /**
     * @param x Cette méthode sert à vérifier un entier, elle retourne
     *          vrai s'il était positive, ou s'il respecte la taille de tableau. Autrement-dit, les
     *          options offertes à l'écran
     * @return
     */

    public boolean validerIndex(int x) {
        int z = tableauDesMots.size();
        String m = String.valueOf(z);
        boolean b = true;
        if (x < 0 || x > z) {
            b = false;
            System.out.println("Numero non valide");
        }


        return b;
    }


    /**
     * Cette méthode affiche à l'écran que le fichier est invalide et
     * elle quitte le programme
     */
    public void quitterProgramme() {
        System.out.println("***********************************");
        System.out.println("Fichier Non Valide");
        System.out.println("***********************************");
        System.exit(0);
    }

    /**
     * @param ligne
     * @return boolean qui retouen vrai si le String passé en paramétre contient quatre points.
     */
    public boolean verifierSiLigneContient4DeuxPoints(String ligne) {
        // https://stackoverflow.com/questions/11171445/how-matcher-find-works

        int x = 0;
        boolean ligneValide = true;
        Pattern pattern = Pattern.compile("[:]");
        Matcher matcher = pattern.matcher(ligne);

        while (matcher.find()) {
            x++;
        }
        if (x != 4) {
            ligneValide = false;
        }

        return ligneValide;
    }

    /**
     * Cette méthode retourne la valeur la plus grande dans une array liste des entiers.
     *
     * @param liste un arrayList des entiers.
     * @return retourne  Valeur Max
     */
    public int trouverMaxDansArrayList(ArrayList<Integer> liste) {

        int maximum = liste.get(0);
        for (int i = 1; i < liste.size(); i++) {
            if (maximum < liste.get(i)) {
                maximum = liste.get(i);
            }
        }
        return maximum;
    }

    /**
     * @param grille         la grille vide de mots-croisés ( un tableau 2d de Char)
     * @param tableauDesMots un ArrayList des mots
     * @return si le ficher contient un conflit de letter ou pas
     */
    public boolean validerSiConflitDeLettre(char[][] grille, ArrayList<Mot> tableauDesMots) {
        boolean estValide = true;
        char caseVide = (char) 0; // char valeur d'un case vide
        String mot;
        for (int i = 0; i < tableauDesMots.size(); i++) {
            mot = tableauDesMots.get(i).getMot();
            int longeurMot = (mot.length());
            for (int j = 0; j < longeurMot; j++) {
                {
                    // si le mot est vertical,
                    if (tableauDesMots.get(i).isEstVertical()) {
                        int positionDeDepartVertical = tableauDesMots.get(i).getNumeroColone();
                        // si la case dans laquelle le lettre sera écrite est vide
                        if (grille[tableauDesMots.get(i).getNumeroLigne() + j][positionDeDepartVertical] == caseVide) {
                            // écrire le mot dans la grille
                            grille[tableauDesMots.get(i).getNumeroLigne() + j][positionDeDepartVertical] = mot.charAt(j);
                        } else {
                            // si la valuer de la case dans laquelle le lettre sera écrite est égale au lettre
                            if (grille[tableauDesMots.get(i).getNumeroLigne() + j][positionDeDepartVertical] == mot.charAt(j)) {
                                grille[tableauDesMots.get(i).getNumeroLigne() + j][positionDeDepartVertical] = mot.charAt(j);
                            } else {
                                estValide = false;
                                // pour quitter la boucle dès qu'un conflit et détecté.
                                return estValide;
                            }
                        }
                        // si le mot est Horizontal,
                    } else {
                        // si la case dans laquelle la lettre sera écrite est vide
                        if (grille[tableauDesMots.get(i).getNumeroLigne()][tableauDesMots.get(i).getNumeroColone() + j] == caseVide) {
                            // écrire le mot dans la grille
                            grille[tableauDesMots.get(i).getNumeroLigne()][tableauDesMots.get(i).getNumeroColone() + j] = mot.charAt(j);

                        } else {
                            // si la valuer de la case dans laquelle le lettre sera écrite est égale au lettre
                            if ((grille[tableauDesMots.get(i).getNumeroLigne()][tableauDesMots.get(i).getNumeroColone() + j] == mot.charAt(j))) {
                                grille[tableauDesMots.get(i).getNumeroLigne()][tableauDesMots.get(i).getNumeroColone() + j] = mot.charAt(j);
                                // si la valuer de la case dans laquelle le lettre sera écrite est égale au lettre
                            } else {
                                estValide = false;
                                // pour quitter la boucle dès qu'un conflit et détecté.
                                return estValide;
                            }

                        }
                    }

                }
            }

        }
        return estValide;
    }
}





































