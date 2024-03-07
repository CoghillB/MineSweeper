package org.example.mine_sweeper;

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
import javafx.stage.Stage;

import java.util.Random;

public class MineSweeper extends Application {
    private static final int MINE = 5;
    private int revealedSafeCells = 0;
    private final int BEGINNERMINES = 10;
    private final int INTERMEDIATEMINES = 40;
    private final int EXPERTMINES = 99;
    private final int BEGINNERROWS = 8;
    private final int BEGINNERCOLUMNS = 8;
    private final int INTERMEDIATEROWS = 16;
    private final int INTERMEDIATECOLUMNS = 16;
    private final int EXPERTROWS = 30;
    private final int EXPERTCOLUMNS = 30;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        HBox hb = new HBox();
        MineButton[][] buttons = new MineButton[BEGINNERROWS][BEGINNERCOLUMNS];
        for (int i = 0; i < BEGINNERROWS; i++) {
            for (int j = 0; j < BEGINNERCOLUMNS; j++) {
                buttons[i][j] = new MineButton();
            }
        }
        setMines(buttons);
        FaceButton face = new FaceButton();
        Random rand = new Random();

        hb.getChildren().add(face);
        hb.setAlignment(Pos.TOP_CENTER);

        face.setOnMouseClicked(e -> {
            theStage.close();
            MineSweeper newGame = new MineSweeper();
            newGame.start(new Stage());
        });

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

//                buttons[i][j] = new MineButton();
                MineButton temp = buttons[i][j];


                buttons[i][j].setOnMouseClicked(e -> {
//                    buttons[1][1].state = 5;
//                    buttons[3][2].state = 5;
//                    buttons[3][3].state = 5;
                    MouseButton button = e.getButton();

                    //TODO: implement the logic for setNumbers and setState

                    if (temp.isClicked && button == MouseButton.PRIMARY) {
                        return;
                    } else if (temp.isClicked && button == MouseButton.SECONDARY) {
                        temp.isClicked = false;
                    }

                    if ((button == MouseButton.SECONDARY && temp.state == 0) ||
                            (button == MouseButton.SECONDARY && temp.state == 5)) {
                        temp.state = 2;
                        temp.setGraphic(new ImageView(new Image("File:src/main/java/org/example/mine_sweeper/pictures/flag.png")));
                        temp.isClicked = true;
                    } else if (button == MouseButton.SECONDARY && temp.state == 2) {
                        temp.state = 0;
                        temp.setGraphic(new ImageView(new Image("File:src/main/java/org/example/mine_sweeper/pictures/cover.png")));
                        temp.isClicked = false;
                    } else if (button == MouseButton.PRIMARY) {

                        if (temp.state == 5) {
                            temp.state = 4;

                            face.setGraphic(new ImageView(new Image("File:src/main/java/org/example/mine_sweeper/pictures/face-dead.png")));
                            temp.setGraphic(new ImageView(new Image("File:src/main/java/org/example/mine_sweeper/pictures/mine-red.png")));
                            // sets the rest of the mines grey and reveals them
                            for (int a = 0; a < BEGINNERROWS; a++) {
                                for (int b = 0; b < BEGINNERCOLUMNS; b++) {
                                    if (buttons[a][b].state == 5) {
                                        buttons[a][b].setGraphic(temp.imageMine = new ImageView(new Image
                                                ("file:src/main/java/org/example/mine_sweeper/pictures/mine-grey.png")));
                                    }
                                    buttons[a][b].isClicked = true;
                                }
                            }
                        }
                    }
                });
                gp.add(buttons[i][j], i, j);
            }
        }
        bp.setTop(hb);
        bp.setCenter(gp);
        theStage.setTitle("Mine Sweeper");
        theStage.setScene(new Scene(bp));
        theStage.show();
    }

    private void setState(MineButton[][] buttons, FaceButton face, MineButton temp) {
        temp.state = 1;
        revealedSafeCells++;
        int totalSafeCells = 64 - 10; // 25 cells - 3 mines
        if (revealedSafeCells == totalSafeCells) {
            for (int a = 0; a < 8; a++) {
                for (int b = 0; b < 8; b++) {
                    if (buttons[a][b].state == 5) {
                        buttons[a][b].setGraphic(temp.imageMine = new ImageView(
                                new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-grey.png")));
                    }
                    face.setGraphic(face.imageWin);
                    buttons[a][b].isClicked = true;
                }
            }
        }
    }

    private void setMines(MineButton[][] buttons) {
        Random rand = new Random();
        for (int n = 1; n < BEGINNERMINES; n++) {
            int x = rand.nextInt(BEGINNERROWS);
            int y = rand.nextInt(BEGINNERCOLUMNS);
            if (buttons[x][y].state == MINE) {
                n--;
            } else {
                buttons[x][y].state = MINE;
            }
        }
    }

    private void setNumbers(MineButton[][] buttons) {
        for (int i = 0; i < BEGINNERROWS; i++) {
            for (int j = 0; j < BEGINNERCOLUMNS; j++) {
                if (buttons[i][j].state != MINE) {
                    int count = 0;
                    if (i > 0 && j > 0 && buttons[i - 1][j - 1].state == MINE) {
                        count++;
                    }
                    if (i > 0 && buttons[i - 1][j].state == MINE) {
                        count++;
                    }
                    if (i > 0 && j < BEGINNERCOLUMNS - 1 && buttons[i - 1][j + 1].state == MINE) {
                        count++;
                    }
                    if (j > 0 && buttons[i][j - 1].state == MINE) {
                        count++;
                    }
                    if (j < BEGINNERCOLUMNS - 1 && buttons[i][j + 1].state == MINE) {
                        count++;
                    }
                    if (i < BEGINNERROWS - 1 && j > 0 && buttons[i + 1][j - 1].state == MINE) {
                        count++;
                    }
                    if (i < BEGINNERROWS - 1 && buttons[i + 1][j].state == MINE) {
                        count++;
                    }
                    if (i < BEGINNERROWS - 1 && j < BEGINNERCOLUMNS - 1 && buttons[i + 1][j + 1].state == MINE) {
                        count++;
                    }
                    if (count > 0) {
                        buttons[i][j].state = count;
                    }
                }
            }
        }
    }

}

class MineButton extends Button {
    int state; // 0 = covered, 1 = uncovered, 2 = flagged, 3 = misflagged, 4 = mine, 5 = covered mine, 6 = Is Clicked
    boolean isClicked = false;
    ImageView imageCover, image0, image1, image2, image3, image4, image5, image6, image7, image8, imageMine, imageMineRed,
            imageFlag, imageMisFlag;

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
        imageMineRed = new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-red.png"));
        imageFlag = new ImageView(new Image("File:src/main/java/org/example/mine_sweeper/pictures/flag.png"));

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

        setGraphic(imageRed9);
    }
}