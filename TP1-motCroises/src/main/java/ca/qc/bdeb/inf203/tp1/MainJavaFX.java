package ca.qc.bdeb.inf203.tp1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MainJavaFX extends Application {
    static MotsCroises motsCroises2 = new MotsCroises();
    ArrayList<TextField> casesDeReponse = new ArrayList<>();
    GridPane grille = new GridPane();
    static HBox[][] d;
    Text lettre = new Text();
    Text numero = new Text();
    static HBox cellule = new HBox();
    VBox indices = new VBox();
    Text phraseMagique;
    public static final String MAUVAIS_REPONSE = "Mauvaise Réponse, Essayez Encore!";
    public static final String Bonne_Reponse = "Bravo";
    public static final String Fichier_NonValide = "Le fichier est invalide";
    public static final String FIN_PARTIE = "VOUS AVEZ GAGNÉ!";

    public static final Color ROUGE = Color.RED;
    public static final Color Vert = Color.GREEN;
    public static final Color Gray = Color.GRAY;
    String nomFichier = "mots-croises1.txt";
    HBox indiceEtReponse = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage stage) throws Exception {
        // Scene et root principal
        VBox root = new VBox();
        root.setSpacing(10);
        Scene scene = new Scene(root, 1000, 800);


        // le logo et la phrase
        HBox titre = new HBox();
        titre.setAlignment(Pos.CENTER);
        Image logo = new Image("mots.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(50);
        logoView.setFitWidth(50);
        Text textDeTitre = new Text("Super Mots-Croisés Master 3000");
        textDeTitre.setFont(Font.font(30));
        titre.getChildren().add(logoView);
        titre.getChildren().add(textDeTitre);
        root.getChildren().add(titre);

        // le menu d'option pour chosir le fichier
        HBox optionGrille = new HBox();
        optionGrille.setAlignment(Pos.TOP_CENTER);
        optionGrille.setSpacing(20);
        VBox checkBoxEtButton = new VBox();
        Text textChangerGrille = new Text("Changer de grille");
        optionGrille.getChildren().add(textChangerGrille);
        String[] nomFichiersDisponible = {"mots-croises1.txt", "mots-croises2.txt", "mots-croises3.txt", "invalide1.txt", "invalide2.txt", "invalide3.txt"};
        ChoiceBox<String> menuFichier = new ChoiceBox<>();
        menuFichier.getItems().addAll(nomFichiersDisponible);
        Button accederOrdi = new Button("Ouvrir un autre mots-croisés");
        checkBoxEtButton.getChildren().add(menuFichier);
        checkBoxEtButton.getChildren().add(accederOrdi);
        optionGrille.getChildren().add(checkBoxEtButton);
        root.getChildren().add(optionGrille);


        // La phrase magique:)
        phraseMagique = new Text();
        phraseMagique.setText("");
        phraseMagique.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, 30));
        root.getChildren().add(phraseMagique);
        root.setAlignment(Pos.CENTER);


        // la grille
        motsCroises2.tableauDesMots = motsCroises2.transfereFichier(nomFichier);
        initialiserGrille();
        grille.setAlignment(Pos.CENTER);
        root.getChildren().add(grille);

        // faire la bar des défenitions avec réponses
        initialiserDefEtTextField(root);
        evenmentQuandMotEstEntre();

        // quitter le jeu
        scene.setOnKeyPressed((event2) -> {
            if (event2.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }


        });


        // cliquer sur le button ( Ouvrir une autre fichier)
        //https://jenkov.com/tutorials/javafx/directorychooser.html
        FileChooser fileChooser = new FileChooser();
        accederOrdi.setOnMouseClicked((event3) -> {
            File fichierChosie = fileChooser.showOpenDialog(stage);
            if (fichierChosie == null) {

            } else {
                motsCroises2.tableauDesMots = motsCroises2.transfereFichier(fichierChosie.getAbsolutePath());
            }
            grille.getChildren().clear();
            initialiserGrille();
            indiceEtReponse.getChildren().clear();
            initialiserDefEtTextField(root);
            evenmentQuandMotEstEntre();


        });

        // cliquer sur un des fichiers dans le menu
        menuFichier.setOnAction((event4) -> {
            motsCroises2.tableauDesMots = motsCroises2.transfereFichier(menuFichier.getValue());

            grille.getChildren().clear();

            initialiserGrille();
            indiceEtReponse.getChildren().clear();
            initialiserDefEtTextField(root);
            evenmentQuandMotEstEntre();


        });


        stage.setScene(scene);
        stage.setTitle("Mots-Croisés");
        Image icon = new Image("icon.png");
        //https://stackoverflow.com

        stage.getIcons().add(icon);
        stage.show();

    }

    /**
     * Cette méthode ajoute des textField et des Def dans un Vbox
     *
     * @param root Vbox dans lequel les indices et les textField seront ajoutées
     */
    private void initialiserDefEtTextField(VBox root) {
        indiceEtReponse = new HBox();
        indices = new VBox();
        ecrireDefinition(indices);
        VBox reponses = new VBox();
        faireDesTexteFields(reponses);
        indiceEtReponse.setSpacing(20);
        indices.setSpacing(10);
        indiceEtReponse.getChildren().addAll(reponses, indices);
        root.getChildren().add(indiceEtReponse);
    }

    /**
     * Cette méthode crée un tableau 2d de cellules et elle l'ajoute chauqe cellule dans le GridPane.
     */
    private void initialiserGrille() {
        motsCroises2.faireLaGrille(); // pour trouver largeur

        d = new HBox[motsCroises2.largeurGrilleHorizontal][motsCroises2.longeurGrilleVertical];


        for (int i = 0; i < motsCroises2.largeurGrilleHorizontal; i++) {
            for (int j = 0; j < motsCroises2.longeurGrilleVertical; j++) {
                HBox cellule = new HBox();
                stylingBox(cellule);

                d[i][j] = cellule;

                grille.add(d[i][j], j, i);


            }
        }
        ajusterLaGrilleSelonJavaFX();
    }

    /**
     * Cette méthode donne le style d'un cellule au Hbox passé en paramétre
     *
     * @param cellule un Hbox
     */
    public void stylingBox(HBox cellule) {
        this.cellule = cellule;

        cellule.setPadding(new Insets(3, 8, 3, 8)); // Un peu de marge autour du contenu
        cellule.setMaxSize(30, 30); // Force la taille de chaque cellule à 30x30 pixels
        cellule.setMinSize(30, 30);
// Donne une bordure noire et une couleur d'arrière-plan à la cellule
        cellule.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        Color color = Color.GRAY;
        cellule.setBackground(new Background(new BackgroundFill(color, null, null)));
// Ajoute une lettre du mot qui va aller dans la case
        lettre = new Text();

        lettre.setFont(Font.font("monospace", 20));
        cellule.getChildren().add(lettre);
// Le petit numéro à afficher si cette case est le début d'un mot
        numero = new Text();
        numero.setFont(Font.font(10));
        cellule.getChildren().add(numero);


    }

    /**
     * Cette méthode écrit les définitions des mots dans des Texte et elle les ajoute dans un VBox.
     *
     * @param b un VBox
     */
    public void ecrireDefinition(VBox b) {


        for (int i = 0; i < motsCroises2.tableauDesMots.size(); i++) {
            b.getChildren().addAll(new Text(i + "- " + motsCroises2.tableauDesMots.get(i).getDefenition()));
        }


    }

    /**
     * Cette méthode initialise des textes fields selon le nombre de mots dans le arrayListe des mots. Elle les ajoute par
     * la suite dans un Vbox
     *
     * @param x un VBox;
     */
    public void faireDesTexteFields(VBox x) {


        casesDeReponse = new ArrayList<>();

        for (int i = 0; i < motsCroises2.tableauDesMots.size(); i++) {
            casesDeReponse.add(new TextField());
        }
        x.getChildren().addAll(casesDeReponse);

    }

    /**
     * Cette méthode assigne une valuer( le numéro de mot ) à l'indexe dans la cellule et elle colore les cellules
     * qui vont centenir des lettres
     */
    public void ajusterLaGrilleSelonJavaFX() {
        motsCroises2.transfereFichier(nomFichier);

        HBox x = new HBox();
        Text indexDeMot;

        for (int i = 0; i < motsCroises2.tableauDesMots.size(); i++) {

            int positionDepartHoriz = motsCroises2.tableauDesMots.get(i).getNumeroLigne();
            int PositionDepartVertic = motsCroises2.tableauDesMots.get(i).getNumeroColone();
            indexDeMot = (Text) d[positionDepartHoriz][PositionDepartVertic].getChildren().get(1);
            indexDeMot.setText(String.valueOf(i));


            int cpt = 0;
            if (motsCroises2.tableauDesMots.get(i).isEstVertical()) {

                while (cpt != motsCroises2.tableauDesMots.get(i).getMot().length()) {
                    // colorer les cellules qui vont contenir des lettres

                    d[positionDepartHoriz + cpt][PositionDepartVertic].setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    cpt++;
                }
            } else {


                while (cpt != motsCroises2.tableauDesMots.get(i).getMot().length()) {

                    d[positionDepartHoriz][PositionDepartVertic + cpt].setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    cpt++;
                }

            }

        }


    }

    /**
     * Cette méthode place le mot entré par l'utilisateur dans la grille
     *
     * @param z   l'index de mot
     * @param mot le mot
     */

    public static void placerMotJavaFx(int z, String mot) {


        mot = motsCroises2.tableauDesMots.get(z).getMot();
        Text texteDeCelle = new Text();
        for (int i = 0; i < mot.length(); i++) {
            if (motsCroises2.tableauDesMots.get(z).isEstVertical()) {

                cellule = d[motsCroises2.tableauDesMots.get(z).getNumeroLigne() + i][motsCroises2.tableauDesMots.get(z).getNumeroColone()];

                texteDeCelle = (Text) cellule.getChildren().get(0);
                texteDeCelle.setText(String.valueOf(mot.charAt(i))); // placer les lettres
                chnagerCouleurEnVert(cellule);

            } else {
                cellule = d[motsCroises2.tableauDesMots.get(z).getNumeroLigne()][motsCroises2.tableauDesMots.get(z).getNumeroColone() + i];

                texteDeCelle = (Text) cellule.getChildren().get(0);
                texteDeCelle.setText(String.valueOf(mot.charAt(i)));
                chnagerCouleurEnVert(cellule);
            }
        }


    }

    /**
     * Cette méthode change le couleur d'un Hbox en vert.
     *
     * @param cellule
     */
    public static void chnagerCouleurEnVert(HBox cellule) {
        cellule.setBackground(new Background(new BackgroundFill(Vert, null, null)));
    }

    /**
     * Cette méthode bloque l'accées au textField d'un mot deviné. elle change aussi sa couelur.
     *
     * @param index index de mot devinés
     */

    public void modificationApresMotValide(final int index) {
        casesDeReponse.get(index).setEditable(false); //https://stackoverflow.com/questions/20205145/javafx-how-to-show-read-only-text
        casesDeReponse.get(index).setBackground(new Background(new BackgroundFill(Gray, null, null)));

        Text v = (Text) indices.getChildren().get(index);
        v.setFill(Gray);


    }

    /**
     * Cette méthode ajuste la phrase magique et sa couleur seolon ce ce que l'utilisateur avait fait.
     *
     * @param color
     * @param phraseAAfficher
     */
    public void phraseMagiqueAjuster(Color color, String phraseAAfficher) {
        phraseMagique.setText(phraseAAfficher);
        phraseMagique.setFill(color);

    }

    /**
     * Cette méthode vérifie si le jeu est terminé en vérifiant si tous les TextField sont bloqueés.
     *
     * @return true si le jeu est terminé
     */
    public boolean verifierSiPartieTermine() {
        boolean jeuTermine = true;
        for (TextField textField : casesDeReponse) {
            if (textField.isEditable()) jeuTermine = false;

        }

        return jeuTermine;
    }


    public boolean validerMotJavaFx(int index, String mot) {
        return mot.equalsIgnoreCase(motsCroises2.tableauDesMots.get(index).getMot());
    }

    /**
     * Cette méthode appele les méthode quand on entre un mot, elle ajuste la phrase magique
     * si la partie est finie.
     */
    public void evenmentQuandMotEstEntre() {
        final String[] mot = new String[1];
        for (int i = 0; i < casesDeReponse.size(); i++) {
            int finalI = i;
            casesDeReponse.get(i).setOnKeyPressed((event) -> {
                if (event.getCode() == KeyCode.ENTER) {

                    evenemntDeMotEntreActiver(finalI, mot);
                }
//

                if (verifierSiPartieTermine()) {
                    phraseMagiqueAjuster(Vert, FIN_PARTIE);
                }
            });
        }
    }

    /**
     * Cette méthode prend est appelée quand un mot est entreé . Elle appelle les méthode pour valider le mot et poour le placer.
     * Elle ajuste la phrase magique selon le mot entré ( si c'est bon ou pas)
     *
     * @param finalI
     * @param mot
     */
    public void evenemntDeMotEntreActiver(final int finalI, String mot[]) {
        mot[0] = casesDeReponse.get(finalI).getText();
        if (validerMotJavaFx(finalI, mot[0])) {
            placerMotJavaFx(finalI, mot[0]);
            phraseMagiqueAjuster(Vert, Bonne_Reponse);
            modificationApresMotValide(finalI);


        } else {
            casesDeReponse.get(finalI).setText("");
            phraseMagiqueAjuster(ROUGE, MAUVAIS_REPONSE);

        }
    }


}



