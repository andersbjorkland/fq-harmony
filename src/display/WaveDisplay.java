package display;

import model.Wave;
import util.Math;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import util.WaveCombiner;

import java.util.ArrayList;
import java.util.List;

public class WaveDisplay {
    public static final int CANVAS_WIDTH = 1000;
    public static final int CANVAS_HEIGHT = 160;
    public static final int LABEL_WIDTH = 100;
    public static final int LABEL_HEIGHT = 10;
    public static final int X_ITERATOR = 10;
    public static final int X_SCALE_MULTIPLIER = 1;
    public static final double AMP_RESOLUTION = 5.0;
    public static final int Y_AXIS_CORRECTION = -1; // Positive amp values will have a lower X-axis score, thus being placed higher in the canvas.
    public static final int Y_AXIS_OFFSET = java.lang.Math.round(CANVAS_HEIGHT / 2); // Center the wave around the middle of the display.
    public static final double X_SCALE = Wave.RESOLUTION_CELLS_PER_METERS / (X_ITERATOR * X_SCALE_MULTIPLIER); // px/meter (1000px = 1 meter)

    Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    private Wave wave;
    private Color color;
    private Label freqLabel;
    private Label ampLabel;
    private Label phaseLabel;
    private List<Wave> waves = new ArrayList<>();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Wave getWave() {
        return wave;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
        drawShapes();
    }

    public void setWave(Wave wave, Color waveColor) {
        this.wave = wave;
        drawShapes(waveColor);
    }

    public Group waveGroup() throws Exception {
        Group root = new Group();
        drawShapes();
        StackPane stack = new StackPane();
        stack.getChildren().add(canvas);
        stack.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox hBox = new HBox();
        hBox.getChildren().add(stack);
        hBox.setSpacing(5.0);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        Font font = Font.font("Verdana", FontWeight.BOLD, 12);
        freqLabel = new Label("Frequency (MHz)");
        freqLabel.setFont(font);
        freqLabel.setPadding(new Insets(5));
        freqLabel.setMinWidth(LABEL_WIDTH);
        freqLabel.setMinHeight(LABEL_HEIGHT);
        TextField freqField = new TextField();
        freqField.setOnAction(event -> {
            double freq = 0.0;
            try {
                freq = Double.parseDouble(freqField.getText());
            } catch (Exception e) {
                System.out.println("Wrong format.");
            }
            if (freq > 0) {
                wave = new Wave(Math.calculateFrequency(freq * 1_000_000.0));
                drawShapes();
            }
        });


        ampLabel = new Label("Amplitude");
        ampLabel.setFont(font);
        ampLabel.setPadding(new Insets(5));
        ampLabel.setMinWidth(LABEL_WIDTH);
        ampLabel.setMinHeight(LABEL_HEIGHT);

        TextField amplitudeField = new TextField("");
        amplitudeField.setOnAction(event -> {
            double amp = -1.0;
            try {
                amp = java.lang.Math.abs(Double.parseDouble(amplitudeField.getText()));
            } catch (Exception e) {
                System.out.println("Wrong format.");
            }
            if (amp != -1.0) {
                if (wave != null) {
                    wave.amplifyAmplitude(amp);
                    drawShapes();
                }
            }
        });

        phaseLabel = new Label("Phase shift (degrees)");
        phaseLabel.setFont(font);
        phaseLabel.setPadding(new Insets(5));
        phaseLabel.setMinWidth(LABEL_WIDTH);
        phaseLabel.setMinHeight(LABEL_HEIGHT);

        TextField phaseField = new TextField("");
        phaseField.setOnAction(event -> {
            int shift = 0;
            try {
                shift = java.lang.Math.abs(Integer.parseInt(phaseField.getText()));
            } catch (Exception e) {
                System.out.println("Wrong format.");
            }
            if (shift != 0) {
                if (wave != null) {
                    wave.phaseShift(shift);
                    drawShapes();
                }
            }
        });

        vBox.getChildren().addAll(freqLabel, freqField, ampLabel, amplitudeField, phaseLabel, phaseField);

        hBox.getChildren().add(vBox);
        root.getChildren().add(hBox);

        return root;
    }

    public Group combinedWaves() {
        Group root = new Group();
        drawShapes(Color.BLACK);
        HBox hBox = new HBox();
        StackPane stack = new StackPane();
        stack.getChildren().add(canvas);
        stack.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox.getChildren().add(stack);
        hBox.setSpacing(5.0);

        VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.setPadding(new Insets(10));
        Font font = Font.font("Verdana", FontWeight.BOLD, 16);
        freqLabel = new Label("Combined waves");
        freqLabel.setFont(font);
        freqLabel.setPadding(new Insets(5));
        freqLabel.setMinWidth(LABEL_WIDTH);
        freqLabel.setMinHeight(LABEL_HEIGHT);

        vBox.getChildren().addAll(freqLabel);

        hBox.getChildren().add(vBox);
        root.getChildren().add(hBox);

        return root;
    }

    private void drawShapes() {
        if (wave != null) {
            graphicsContext.clearRect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.rect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);
            Wave wave1Combined = WaveCombiner.combineInteractingWaves(2, wave);
            graphicsContext.setStroke(Color.LIGHTBLUE);
            graphicsContext.setLineWidth(2);
            drawWaves(wave1Combined);
        }
    }

    private void drawShapes(Color waveColor) {
        if (wave != null) {
            graphicsContext.clearRect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.rect(0,0, CANVAS_WIDTH, CANVAS_HEIGHT);
            Wave wave1Combined = WaveCombiner.combineInteractingWaves(2, wave);
            graphicsContext.setStroke(waveColor);
            graphicsContext.setLineWidth(2);
            drawWaves(wave1Combined);
        }
    }

    private void drawWaves(Wave combinedWave) {
        int x1, y1, x2, y2;
        int j = 0;
        for (int i = 0; i < combinedWave.getAmplitude().length; i += X_ITERATOR) {
            x1 = j * X_SCALE_MULTIPLIER;
            y1 = interpretY(combinedWave.getAmplitude()[i]);
            x2 = x1 + X_SCALE_MULTIPLIER;
            if ((i + X_ITERATOR) >= combinedWave.getAmplitude().length) {
                y2 = interpretY(combinedWave.getAmplitude()[0]);

            } else {
                y2 = interpretY(combinedWave.getAmplitude()[i + X_ITERATOR]);
            }
            graphicsContext.strokeLine(x1, y1,  x2, y2);
            j++;
            if (x2 > graphicsContext.getCanvas().getWidth()) {
                break;
            }
        }
    }

    private int interpretY(double amp) {
        return Y_AXIS_OFFSET + (int) java.lang.Math.round(amp * AMP_RESOLUTION * Y_AXIS_CORRECTION);
    }
}
