package org.example.mine_sweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Random;

import static javafx.util.Duration.seconds;

public class MineSweeper extends Application {
    private static final int MINE = -1;
    private static final int EXPLODED_MINE = -2;
    private static final int EMPTY = 0;
    private final int GAME_ROWS = 8;
    private final int GAME_COLS = 8;
    private int gameMines = 10;

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean checkWin(MineButton[][] buttons) {
        // if all the mines are flagged and all the other cells are uncovered
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (buttons[i][j].state == MINE && !buttons[i][j].flagged) {
                    return false;
                }
                if (buttons[i][j].state >= 0 && buttons[i][j].state < 9 && !buttons[i][j].isClicked) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void start(Stage theStage) {

        int[] timeCount = new int[]{0};
        double preferredWidth = 70;
        double preferredHeight = 60;

        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        HBox hb = new HBox();
        Label minesLeft = new Label(Integer.toString(gameMines));
        Label time = new Label("000");
        Timeline timeline = new Timeline();
        MineButton[][] buttons = new MineButton[GAME_ROWS][GAME_COLS];
        FaceButton face = new FaceButton();
        Random rand = new Random();

        time.setStyle("-fx-border-color: Lightgrey; -fx-border-width: 5px; -fx-font-size: 20px; " +
                "-fx-text-alignment: center; -fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: black;");
        time.setPrefSize(preferredWidth, preferredHeight);
        time.setAlignment(Pos.CENTER);

        minesLeft.setStyle("-fx-border-color: lightGrey; -fx-border-width: 5px; -fx-font-size: 20px; " +
                "-fx-text-alignment: center;-fx-font-weight: bold; -fx-text-fill: red; -fx-background-color: black;");
        minesLeft.setPrefSize(preferredWidth, preferredHeight);
        minesLeft.setAlignment(Pos.CENTER);

        face.setPadding(new Insets(0, 0, 0, 0));

        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(seconds(1), e -> {
            timeCount[0]++;
            time.setText(String.format("%03d", timeCount[0]));
        });

        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLS; j++) {
                buttons[i][j] = new MineButton();
            }
        }

        timeline.getKeyFrames().add(keyFrame);

        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(time);
        hb.getChildren().add(face);
        hb.getChildren().add(minesLeft);
        hb.setSpacing(30);

        face.setOnMouseClicked(e -> {
            theStage.close();
            MineSweeper newGame = new MineSweeper();
            newGame.start(new Stage());
        });

        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLS; j++) {

                MineButton temp = buttons[i][j];
                temp.setPadding(new Insets(0, 0, 0, 0));
                int finalI1 = i;
                int finalJ1 = j;
                buttons[i][j].setOnMouseClicked(e -> {

                    MouseButton button = e.getButton();

                    if (temp.isClicked && button == MouseButton.PRIMARY) {
                        return;
                    } else if (temp.isClicked && button == MouseButton.SECONDARY && face.getGraphic() != face.imageLose && face.getGraphic() != face.imageWin) {
                        temp.isClicked = false;
                    } else if (face.getGraphic() == face.imageLose || face.getGraphic() == face.imageWin) {
                        return;
                    }

                    // handles left click
                    if (button == MouseButton.SECONDARY) {

                        // if the button being flagged is a mine
                        if (temp.state == MINE && !temp.flagged) {
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/flag.png")));
                            gameMines--;
                            minesLeft.setText(Integer.toString(gameMines));
                            temp.wasMine = true;
                            temp.isClicked = true;
                            temp.flagged = true;

                            // DEBUGGING
                            System.out.println();
                            System.out.println("Button state: " + temp.state);
                            System.out.println("Is button flagged: " + temp.flagged);
                            System.out.println("Was button a mine: " + temp.wasMine);

                            // if the button being flagged is not a mine
                        } else if (temp.state >= 0 && temp.state < 9 && !temp.flagged) {
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/flag.png")));
                            temp.isClicked = true;
                            gameMines--;
                            minesLeft.setText(Integer.toString(gameMines));
                            temp.flagged = true;

                            // DEBUGGING
                            System.out.println();
                            System.out.println("Button state: " + temp.state);
                            System.out.println("Is button flagged: " + temp.flagged);

                            // if the button being flagged is already flagged and was a mine
                        } else if (temp.flagged) {
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/cover.png")));
                            gameMines++;
                            minesLeft.setText(Integer.toString(gameMines));

                            temp.flagged = false;
                            temp.wasMine = false;

                            // DEBUGGING
                            System.out.println();
                            System.out.println("Button state: " + temp.state);
                            System.out.println("Is button flagged: " + temp.flagged);
                            System.out.println("Was button a mine: " + temp.wasMine);
                        }

                        // handles right click
                    } else {
                        if (timeCount[0] == 0) {
                            setMinesAfterFirstClick(buttons, finalI1, finalJ1);
                            setNumbers(buttons);
                            timeline.play();
                        }
                        if (temp.state == MINE) {
                            face.setGraphic(face.imageLose);
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-red.png")));
                            temp.state = EXPLODED_MINE;
                            for (int k = 0; k < GAME_ROWS; k++) {
                                for (int l = 0; l < GAME_COLS; l++) {
                                    buttons[k][l].isClicked = true;
                                    // if the button was flagged but not a mine
                                    if (buttons[k][l].flagged && !buttons[k][l].wasMine) {
                                        buttons[k][l].setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/mine-misflagged.png")));
                                    } // if the button was a mine
                                    if (buttons[k][l].state == MINE) {
                                        buttons[k][l].setGraphic(buttons[k][l].imageMine);
                                    }
                                    timeline.stop();
                                }
                            }
                        } else if (temp.state >= 1 && temp.state < 9) {
                            temp.isClicked = true;
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/" + temp.state + ".png")));

                            // DEBUGGING
                            System.out.println();
                            System.out.println("Button state: " + temp.state);
                            System.out.println("Is button clicked: " + temp.isClicked);

                        } else if (temp.state == EMPTY) {
                            uncoverCells(buttons, finalI1, finalJ1);
                            temp.setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/0.png")));
                        }
                    }

                    if (checkWin(buttons)) {
                        face.setGraphic(face.imageWin);
                        for (int k = 0; k < GAME_ROWS; k++) {
                            for (int l = 0; l < GAME_COLS; l++) {
                                buttons[k][l].isClicked = true;
                                timeline.stop();
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

    public void setMinesAfterFirstClick(MineButton[][] buttons, int firstClickX, int firstClickY) {
        Random rand = new Random();
        for (int n = 0; n < gameMines; n++) {
            int x = rand.nextInt(GAME_ROWS);
            int y = rand.nextInt(GAME_COLS);
            if (buttons[x][y].state == MINE || (Math.abs(x - firstClickX) <= 1 && Math.abs(y - firstClickY) <= 1)) {
                n--;
            } else {
                buttons[x][y].state = MINE;
            }
        }
    }

    public void setNumbers(MineButton[][] buttons) {
        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLS; j++) {
                if (buttons[i][j].state != MINE) {
                    int count = 0;
                    if (i > 0 && j > 0 && buttons[i - 1][j - 1].state == MINE) {
                        count++;
                    }
                    if (i > 0 && buttons[i - 1][j].state == MINE) {
                        count++;
                    }
                    if (i > 0 && j < GAME_COLS - 1 && buttons[i - 1][j + 1].state == MINE) {
                        count++;
                    }
                    if (j > 0 && buttons[i][j - 1].state == MINE) {
                        count++;
                    }
                    if (j < GAME_COLS - 1 && buttons[i][j + 1].state == MINE) {
                        count++;
                    }
                    if (i < GAME_ROWS - 1 && j > 0 && buttons[i + 1][j - 1].state == MINE) {
                        count++;
                    }
                    if (i < GAME_ROWS - 1 && buttons[i + 1][j].state == MINE) {
                        count++;
                    }
                    if (i < GAME_ROWS - 1 && j < GAME_COLS - 1 && buttons[i + 1][j + 1].state == MINE) {
                        count++;
                    }
                    if (count > 0) {
                        buttons[i][j].state = count;
                    } else {
                        buttons[i][j].state = EMPTY;
                    }
                }
            }
        }
    }

    public void uncoverCells(MineButton[][] buttons, int x, int y) {
        // Check if the cell is within the bounds of the game board
        if (x < 0 || x >= GAME_ROWS || y < 0 || y >= GAME_COLS) {
            return;
        }

        // Check if the cell is already uncovered
        if (buttons[x][y].isClicked) {
            return;
        }

        // Uncover the cell
        buttons[x][y].isClicked = true;
        buttons[x][y].setGraphic(new ImageView(new Image("file:src/main/java/org/example/mine_sweeper/pictures/" + buttons[x][y].state + ".png")));


        // If the cell is a safe cell, recursively uncover all its neighboring cells
        if (buttons[x][y].state == EMPTY) {
            uncoverCells(buttons, x - 1, y - 1);
            uncoverCells(buttons, x - 1, y);
            uncoverCells(buttons, x - 1, y + 1);
            uncoverCells(buttons, x, y - 1);
            uncoverCells(buttons, x, y + 1);
            uncoverCells(buttons, x + 1, y - 1);
            uncoverCells(buttons, x + 1, y);
            uncoverCells(buttons, x + 1, y + 1);
        }
    }
}

class MineButton extends Button {
    int state; // 0 = covered, 1 = uncovered, 2 = flagged, 3 = misflagged, 4 = mine, 5 = covered mine, 6 = Is Clicked
    boolean isClicked = false;
    boolean wasMine = false;
    boolean flagged = false;
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
