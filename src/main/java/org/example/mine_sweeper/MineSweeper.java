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
    private int revealedSafeCells = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        HBox hb = new HBox();
        MineButton[][] buttons = new MineButton[8][8];
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

                buttons[i][j] = new MineButton();
                MineButton temp = buttons[i][j];

                buttons[i][j].setOnMouseClicked(e -> {
                    buttons[1][1].state = 5;
                    buttons[3][2].state = 5;
                    buttons[3][3].state = 5;
                    MouseButton button = e.getButton();

                    // TODO: change so you can unflag a mine after being flagged
                    // TODO: create method to make a random board every game with the math to place numbers in the right place

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

                        if (temp == buttons[0][0] || temp == buttons[0][1] || temp == buttons[0][2] ||
                                temp == buttons[1][0] || temp == buttons[3][1] || temp == buttons[4][1] ||
                                temp == buttons[2][0] || temp == buttons[1][2] || temp == buttons[2][4] ||
                                temp == buttons[3][4] || temp == buttons[4][4]) {

                            temp.setGraphic(temp.image1);
                            temp.isClicked = true;
                            setState(buttons, face, temp);


                        } else if (temp == buttons[2][1] || temp == buttons[4][2] || temp == buttons[2][3] ||
                                temp == buttons[4][3]) {

                            temp.setGraphic(temp.image2);
                            temp.isClicked = true;
                            setState(buttons, face, temp);

                        } else if (temp == buttons[2][2]) {

                            temp.setGraphic(temp.image3);
                            temp.isClicked = true;
                            setState(buttons, face, temp);

                        } else if (temp == buttons[1][1] || temp == buttons[3][2] || temp == buttons[3][3]) {

                            temp.state = 4;
                            temp.isClicked = true;
                            temp.setGraphic(temp.imageMineRed);
                            face.setGraphic(face.imageLose);

                            for (int a = 0; a < 8; a++) {
                                for (int b = 0; b < 8; b++) {
                                    if (buttons[a][b].state == 5) {
                                        buttons[a][b].setGraphic(buttons[a][b].imageMine);
                                    }
                                }
                            }
                            for (int a = 0; a < 8; a++) {
                                for (int b = 0; b < 8; b++) {
                                    buttons[a][b].isClicked = true;
                                }
                            }
                        } else {
                            temp.setGraphic(temp.image0);
                            temp.isClicked = true;
                            setState(buttons, face, temp);
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