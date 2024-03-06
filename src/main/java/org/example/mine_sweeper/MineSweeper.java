package org.example.mine_sweeper;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class MineSweeper extends Application {
    private final int TOTALSAFECELLS = 22; // 25 cells - 3 mines
    private int revealedSafeCells = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {

        MineButton[][] buttons = new MineButton[5][5];
        FaceButton face = new FaceButton();
        Pane pane = new Pane();
        RedDigits redDigits = new RedDigits();

        pane.getChildren().add(redDigits);
        GridPane gp = new GridPane();
        BorderPane bp = new BorderPane();

        HBox hb = new HBox();
        hb.getChildren().add(face);
        hb.setAlignment(Pos.TOP_CENTER);

//        HBox leftSide = new HBox();
//        leftSide.setAlignment(Pos.TOP_LEFT);
//        leftSide.getChildren().add(new Pane());
//
//        HBox rightSide = new HBox();
//        rightSide.setAlignment(Pos.TOP_RIGHT);
//        rightSide.getChildren().add(pane);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {


                buttons[i][j] = new MineButton();
                MineButton temp = buttons[i][j];
                buttons[i][j].setOnMouseClicked(e -> {
                    buttons[1][1].state = 5;
                    buttons[3][2].state = 5;
                    buttons[3][3].state = 5;
                    MouseButton button = e.getButton();
                    if (button == MouseButton.SECONDARY) {
                        //place flag
                        if (temp.state == 0) {
                            temp.setGraphic(temp.imageFlag);
                            temp.state = 2;
                        } else if (temp.state == 2) {
                            temp.setGraphic(temp.imageCover);
                            temp.state = 0;
                        }
                    }
                    if (button == MouseButton.PRIMARY && temp == buttons[0][0] ||
                            button == MouseButton.PRIMARY && temp == buttons[0][1] ||
                            button == MouseButton.PRIMARY && temp == buttons[0][2] ||
                            button == MouseButton.PRIMARY && temp == buttons[1][0] ||
                            button == MouseButton.PRIMARY && temp == buttons[3][1] ||
                            button == MouseButton.PRIMARY && temp == buttons[4][1] ||
                            button == MouseButton.PRIMARY && temp == buttons[2][0] ||
                            button == MouseButton.PRIMARY && temp == buttons[1][2] ||
                            button == MouseButton.PRIMARY && temp == buttons[2][4] ||
                            button == MouseButton.PRIMARY && temp == buttons[3][4] ||
                            button == MouseButton.PRIMARY && temp == buttons[4][4]) {

                        temp.setGraphic(temp.image1);
                        temp.state = 1;
                        revealedSafeCells++;

                    } else if (button == MouseButton.PRIMARY && temp == buttons[2][1] ||
                            button == MouseButton.PRIMARY && temp == buttons[4][2] ||
                            button == MouseButton.PRIMARY && temp == buttons[2][3] ||
                            button == MouseButton.PRIMARY && temp == buttons[4][3]) {

                        temp.setGraphic(temp.image2);
                        temp.state = 1;
                        revealedSafeCells++;

                    } else if (button == MouseButton.PRIMARY && temp == buttons[2][2]) {

                        temp.setGraphic(temp.image3);
                        temp.state = 1;
                        revealedSafeCells++;

                    } else if (button == MouseButton.PRIMARY && temp == buttons[1][1] ||
                            button == MouseButton.PRIMARY && temp == buttons[3][2] ||
                            button == MouseButton.PRIMARY && temp == buttons[3][3]) {

                        temp.state = 4;
                        temp.setGraphic(temp.imageMineRed);
                        face.setGraphic(face.imageLose);

                        for (int a = 0; a < 5; a++) {
                            for (int b = 0; b < 5; b++) {
                                if (buttons[a][b].state == 5) {
                                    buttons[a][b].setGraphic(buttons[a][b].imageMine);
                                }
                            }
                        }
                        for (int a = 0; a < 5; a++) {
                            for (int b = 0; b < 5; b++) {
                                buttons[a][b].setDisable(true);
                            }
                        }
                    } else {
                        temp.setGraphic(temp.image0);
                        temp.state = 1;
                        // Start a fade-in animation.
                    }
                });
                gp.add(buttons[i][j], i, j);
            }
        }

        bp.setTop(hb);
        bp.setCenter(gp);

        theStage.setScene(new Scene(bp));
        theStage.show();
    }
}

class MineButton extends Button {
    int state; // 0 = covered, 1 = uncovered, 2 = flagged, 3 = misflagged, 4 = mine, 5 = covered mine
    ImageView imageCover, image0, image1, image2, image3, image4, image5, image6, image7, image8, imageMine, imageFlag,
            imageMisFlagged, imageMineRed;

    public MineButton() {
        imageCover = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/cover.png"));
        image0 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/0.png"));
        image1 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/1.png"));
        image2 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/2.png"));
        image3 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/3.png"));
        image4 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/4.png"));
        image5 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/5.png"));
        image6 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/6.png"));
        image7 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/7.png"));
        image8 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/8.png"));

        imageMine = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-grey.png"));
        imageFlag = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/flag.png"));
        imageMisFlagged = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-misflagged.png"));
        imageMineRed = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-red.png"));

        setGraphic(imageCover);
    }
}

class FaceButton extends Button {
    ImageView imageSmile, imageLose, imageWin;

    public FaceButton() {
        imageSmile = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/face-smile.png"));
        imageLose = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/face-dead.png"));
        imageWin = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/face-win.png"));

        setGraphic(imageSmile);
    }
}

class RedDigits extends Button {
    ImageView imageRed0, imageRed1, imageRed2, imageRed3, imageRed4, imageRed5, imageRed6, imageRed7, imageRed8, imageRed9;

    public RedDigits() {
        imageRed0 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/0.png"));
        imageRed1 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/1.png"));
        imageRed2 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/2.png"));
        imageRed3 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/3.png"));
        imageRed4 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/4.png"));
        imageRed5 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/5.png"));
        imageRed6 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/6.png"));
        imageRed7 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/7.png"));
        imageRed8 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/8.png"));
        imageRed9 = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/digits/9.png"));

        setGraphic(imageRed0);
    }
}