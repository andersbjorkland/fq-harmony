package display;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ScaleDisplay extends Group {
    WaveDisplay waveDisplay;
    final int WIDTH = WaveDisplay.CANVAS_WIDTH;
    final int HEIGHT = 15;
    HBox hBox = new HBox();
    Label label = new Label(" 1 meter");

    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

    public ScaleDisplay(WaveDisplay waveDisplay) {
        super();
        this.waveDisplay = waveDisplay;
        hBox.getChildren().addAll(canvas, label);
        this.getChildren().add(hBox);
        drawScale();
    }

    public void drawScale() {

        int scaleInterval = (int)Math.round(waveDisplay.X_SCALE / 10);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(0, HEIGHT/2, WIDTH, HEIGHT/2);
        for (int i = 0; i < WIDTH; i++) {
            if (i % scaleInterval == 0) {
                graphicsContext.strokeLine(i, 2.5, i, HEIGHT - 2.5);
            }
        }
        graphicsContext.strokeLine(WIDTH - 1, 2.5, WIDTH - 1, HEIGHT - 2.5);

    }
}
