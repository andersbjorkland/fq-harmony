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
        WaveDisplay fq1Display = new WaveDisplay();
        WaveDisplay fq2Display = new WaveDisplay();
        WaveDisplay fq3Display = new WaveDisplay();
        WaveDisplay combinedDisplay = new WaveDisplay();
        ScaleDisplay scaleDisplay = new ScaleDisplay(fq1Display);

        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getChildren().addAll(fq1Display.waveGroup(), fq2Display.waveGroup(), fq3Display.waveGroup(), combinedDisplay.combinedWaves(), scaleDisplay);

        // Update the combinedDisplay when a fq#Display is updated. Will only work if at least the first two displays has FQ's.
        vBox.setOnKeyReleased(event -> {
            if (fq1Display.getWave() != null & fq2Display.getWave() != null & fq3Display.getWave() != null) {
                Wave combinedWave = WaveCombiner.combineInteractingWaves(512, fq1Display.getWave(), fq2Display.getWave(), fq3Display.getWave());
                combinedDisplay.setWave(combinedWave, Color.DARKGRAY);
            } else if (fq1Display.getWave() != null & fq2Display.getWave() != null) {
                Wave combinedWave = WaveCombiner.combineInteractingWaves(512, fq1Display.getWave(), fq2Display.getWave());
                combinedDisplay.setWave(combinedWave, Color.DARKGRAY);
            }
        });

        primaryStage.setTitle("Wave Display");
        primaryStage.setScene(new Scene(vBox));
        primaryStage.getIcons().add(new Image( Main.class.getResourceAsStream("Waveforms.png")));
        primaryStage.show();
    }


}
