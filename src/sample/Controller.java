package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import sample.Wecker.WeckerAPI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Scanner;


public class Controller implements WeckerAPI {

    public Label lbldate, lblWeckeranzeige, lblUhr;
    public Button btnSchlummern, btnAusschalten, btneintraglöschen;
    public Pane paneMain, paneEingabe, paneUhr;
    public ScrollPane paneWeckeranzeige;
    public GridPane paneGridWeckeranzeige;

    public Spinner<Integer> SpinHour, SpinMin;
    SpinnerValueFactory<Integer> svfHour = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 1);
    SpinnerValueFactory<Integer> svfMin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 1);

    public CheckBox checkMo, checkDi, checkMi, checkDo, checkFr, checkSa, checkSo;

    Media sound;
    MediaPlayer player;


    Date date = new Date();
    String[] temp = date.toString().split(" ")[3].split(":");
    String time = temp[0] + ":" + temp[1];

    Runnable target = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if(timehandler()){

                    synchronized (thread){
                        try {
                            thread.wait(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {

                    synchronized (thread){
                        try {
                            thread.wait(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    };
    final Thread thread = new Thread(target) {
    };

    @FXML
    public void initialize() {
        String filenamemp3 = null;
        File src = new File("src/sample/Wecker/filename.txt");

        try {
            Scanner sc = new Scanner(src);
            if(!sc.hasNextLine()){
                filenamemp3 = "sound.mp3";
            }
            else {
                filenamemp3 = sc.nextLine();
                System.out.println(filenamemp3);
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lbldate.setText("Sound: " + filenamemp3);
        System.out.println("Sound: " + filenamemp3);
        sound = new Media(new File("src/sample/Wecker/"+filenamemp3).toURI().toString());
        player = new MediaPlayer(sound);

        setWeckerdata();
        thread.start();
    }


    public  void setWeckerdata(){
        for (int i = 0; i < getAnzahlWecker(); i++) {
            lblWeckeranzeige = new Label();
            lblWeckeranzeige.setText(getWeckerData(i));
            lblWeckeranzeige.setId(String.valueOf(i));
            lblWeckeranzeige.setMinWidth(300);

            btneintraglöschen = new Button();
            btneintraglöschen.setGraphic(new ImageView(getClass().getResource("delete.png").toExternalForm()));
            btneintraglöschen.setId(String.valueOf(i));
            btneintraglöschen.setOnAction(actionEvent -> {
                synchronized(thread) {
                    try {
                        thread.interrupt();
                        thread.wait(100);

                        final Node source = (Node) actionEvent.getSource();                        String id = source.getId();

                        if (deleteWecker(Integer.valueOf(id))) {
                            paneGridWeckeranzeige.getChildren().remove(lblWeckeranzeige);
                            paneGridWeckeranzeige.getChildren().remove(btneintraglöschen);
                            setWeckerdata();

                        } else {
                            System.out.println("An error has acourd");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            paneGridWeckeranzeige.add(lblWeckeranzeige, 0, i);
            paneGridWeckeranzeige.add(btneintraglöschen,1,i);
        }
    }



    public void hndlbtnNeuerEintrag(ActionEvent actionEvent) {
        paneMain.setVisible(false);
        paneMain.setDisable(true);
        paneEingabe.setVisible(true);
        paneEingabe.setDisable(false);
        SpinHour.setValueFactory(svfHour);
        SpinMin.setValueFactory(svfMin);
    }

    public void hndlbtnNeuerEintragBestätigen(ActionEvent actionEvent) {
        boolean[] set = new boolean[7];
        set[0] = checkMo.isSelected();
        set[1] = checkDi.isSelected();
        set[2] = checkMi.isSelected();
        set[3] = checkDo.isSelected();
        set[4] = checkFr.isSelected();
        set[5] = checkSa.isSelected();
        set[6] = checkSo.isSelected();
        eintragSpeichern(set, SpinHour.getValue(), SpinMin.getValue());

        paneMain.setVisible(true);
        paneMain.setDisable(false);
        paneEingabe.setVisible(false);
        paneEingabe.setDisable(true);
        setWeckerdata();
    }

    public boolean timehandler() {
        if (timewatcher()) {



            //lblUhr.setText(time);
            paneMain.setVisible(false);
            paneMain.setDisable(true);
            paneEingabe.setVisible(false);
            paneEingabe.setDisable(true);
            paneUhr.setVisible(true);
            paneUhr.setDisable(false);


            audiocontroll("an");
            return true;
        }
        return false;
    }

    public void hndlbtnSchlummern(ActionEvent actionEvent) {
        paneMain.setVisible(true);
        paneMain.setDisable(false);
        paneUhr.setVisible(false);
        paneUhr.setDisable(true);

        audiocontroll("aus");
    }

    public void hndlbtnAusschalten(ActionEvent actionEvent) {
        audiocontroll("aus");

        paneMain.setVisible(true);
        paneMain.setDisable(false);
        paneUhr.setVisible(false);
        paneUhr.setDisable(true);
    }


    private void audiocontroll(String controll) {
        switch (controll) {
            case ("aus"):
                player.stop();

                synchronized (thread) {
                    try {
                        thread.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case ("an"):
                player.play();
                break;

        }
    }

    public void hndlbtnFileUpload(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            URI path = new File(file.getPath()).toURI();
            saveFile(path);
        }
    }
    private void saveFile(URI path) {
        File source = new File(path);
        File dest = new File("src/sample/Wecker/");
        try {
            FileUtils.copyFileToDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] tempPath = path.toString().split("/");



        sound = new Media(new File("src/sample/Wecker/"+tempPath[tempPath.length - 1]).toURI().toString());
        player = new MediaPlayer(sound);


        try {
            FileWriter fw = new FileWriter("src/sample/Wecker/filename.txt");
            fw.write(tempPath[tempPath.length - 1]);
            fw.close();
            lbldate.setText("Sound: " + tempPath[tempPath.length - 1]);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
//TODO Uhrzeit richtig formatieren
