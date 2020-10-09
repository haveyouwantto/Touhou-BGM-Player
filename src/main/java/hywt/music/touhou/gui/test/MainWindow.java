package hywt.music.touhou.gui.test;

import hywt.music.touhou.Constants;
import hywt.music.touhou.gui.Messages;
import hywt.music.touhou.pcmprocessing.PlayerThread;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class MainWindow extends Application {

    PlayerThread playerThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // load the bgm data
        try {
            Constants.init();
            playerThread = new PlayerThread();
            playerThread.start();
            primaryStage.setTitle(Messages.getString("GUI.title"));

            BorderPane mainPane = new BorderPane();

            GridPane musicSelectionPane = new GridPane();
            musicSelectionPane.setAlignment(Pos.TOP_CENTER);
            musicSelectionPane.setPadding(new Insets(4, 4, 4, 4));
            musicSelectionPane.setVgap(4);

            ComboBox<Game> gameComboBox = new ComboBox<>();
            ComboBox<Music> musicComboBox = new ComboBox<>();
            //gameComboBox.setStyle("-fx-max-width: fit-content");
            gameComboBox.getItems().addAll(Constants.bgmdata.games);
            gameComboBox.setValue(gameComboBox.getItems().get(0));
            gameComboBox.setOnAction(e -> {
                musicComboBox.getItems().clear();
                musicComboBox.getItems().addAll(gameComboBox.getValue().music);
                musicComboBox.setValue(musicComboBox.getItems().get(0));
            });

            /*
             * default music
             * */
            musicComboBox.getItems().clear();
            musicComboBox.getItems().addAll(Constants.bgmdata.games.get(0).music);
            musicComboBox.setValue(musicComboBox.getItems().get(0));

            musicSelectionPane.add(gameComboBox, 0, 0);
            musicSelectionPane.add(musicComboBox, 0, 1);

            mainPane.setTop(musicSelectionPane);

            FlowPane playControlPane = new FlowPane();

            playControlPane.setHgap(5);
            playControlPane.setVgap(2);
            playControlPane.setAlignment(Pos.TOP_CENTER);

            Button playBtn = new Button("\u25b6");
            Button pauseBtn = new Button(Messages.getString("GUI.btnP_1.text"));
            Button stopBtn = new Button("\u2588");
            playBtn.setOnAction(e -> {
                playerThread.setMG(gameComboBox.getValue(), musicComboBox.getValue());
                try {
                    playerThread.play();
                } catch (LineUnavailableException lineUnavailableException) {
                    lineUnavailableException.printStackTrace();
                }
            });
            pauseBtn.setOnAction(e -> playerThread.pause());
            stopBtn.setOnAction(e -> playerThread.terminate());
            playControlPane.getChildren().add(playBtn);
            playControlPane.getChildren().add(pauseBtn);
            playControlPane.getChildren().add(stopBtn);

            mainPane.setCenter(playControlPane);

            Scene scene = new Scene(mainPane, 440, 320);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
