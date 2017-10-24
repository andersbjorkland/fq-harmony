import display.ScaleDisplay;
import display.WaveDisplay;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Wave;
import util.WaveCombiner;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WaveDisplay display1 = new WaveDisplay();
        WaveDisplay display2 = new WaveDisplay();
        WaveDisplay display3 = new WaveDisplay();
        WaveDisplay display4 = new WaveDisplay();
        ScaleDisplay scaleDisplay = new ScaleDisplay(display1);

        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getChildren().addAll(scaleDisplay, display1.waveGroup(), display2.waveGroup(), display3.waveGroup(), display4.combinedWaves());
        vBox.setOnKeyReleased(event -> {
            if (display1.getWave() != null & display2.getWave() != null & display3.getWave() != null) {
                Wave combinedWave = WaveCombiner.combineInteractingWaves(512, display1.getWave(), display2.getWave(), display3.getWave());
                display4.setWave(combinedWave, Color.DARKGRAY);
            } else if (display1.getWave() != null & display2.getWave() != null) {
                Wave combinedWave = WaveCombiner.combineInteractingWaves(512, display1.getWave(), display2.getWave());
                display4.setWave(combinedWave, Color.DARKGRAY);
            }
        });

        primaryStage.setTitle("model.Wave Main");
        primaryStage.setScene(new Scene(vBox));
        primaryStage.getIcons().add(new Image( Main.class.getResourceAsStream("Waveforms.png")));
        primaryStage.show();
    }


}
