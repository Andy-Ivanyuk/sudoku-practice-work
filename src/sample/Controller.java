package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Controller {

    @FXML
    private Button button_one;

    @FXML
    private Button button_two;

    @FXML
    private Button button_three;

    @FXML
    private Button button_four;

    @FXML
    private Button button_five;

    @FXML
    private Button button_six;

    @FXML
    private Button button_seven;

    @FXML
    private Button button_eight;

    @FXML
    private Button button_nine;

    @FXML
    private Canvas canvas;

    GameBoard gameboard;

    private int player_selected_row;
    private int player_selected_col;

    @FXML
    public void initialize() {
        //Create an instance of our gameboard
        gameboard = new GameBoard();
        //Get graphics context from canvas
        GraphicsContext context = canvas.getGraphicsContext2D();
        //Call drawOnCanvas method, with the context we have gotten from the canvas
        drawOnCanvas(context);
        player_selected_row = 0;
        player_selected_col = 0;
    }

    public void buttonClearPressed(){
        gameboard.setNull(0, player_selected_row,player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void drawOnCanvas(GraphicsContext context) {

        context.clearRect(0, 0, 372, 372);
        context.setLineWidth(3.0);
        context.setStroke(Color.BLACK);
        context.moveTo(0, 123);
        context.lineTo(372, 123);
        context.moveTo(123, 0);
        context.lineTo(123, 372);
        context.moveTo(246, 0);
        context.lineTo(246, 372);
        context.moveTo(0, 246);
        context.lineTo(372, 246);
        context.stroke();
        // draw white rounded rectangles for our board
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {
                // finds the y position of the cell, by multiplying the row number by 50, which is the height of a row in pixels
                // then adds 2, to add some offset
                int position_y = row * 41 + 2;
                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a column in pixels
                // then add 2, to add some offset
                int position_x = col * 41 + 2;
                // defines the width of the square as 46 instead of 50, to account for the 4px total of blank space caused by the offset
                // as we are drawing squares, the height is going to be the same
                int width = 35;
                // set the fill color to white (you could set it to whatever you want)
                context.setFill(Color.WHITE);
                // draw a rounded rectangle with the calculated position and width. The last two arguments specify the rounded corner arcs width and height.
                // Play around with those if you want.
                context.fillRoundRect(position_x, position_y, width, width, 10, 10);
            }
        }



        // draw highlight around selected cell
        // set stroke color to res



        context.setFill(Color.gray(0, 0.1));
        context.fillRoundRect(2, player_selected_row*41+2, 363, 35,10, 10);
        context.fillRoundRect(player_selected_col*41+2, 2, 35, 363,10, 10);


        // draw the initial numbers from our GameBoard instance
        int[][] initial = gameboard.getInitial();
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {
                // finds the y position of the cell, by multiplying the row number by 50, which is the height of a row in pixels
                // then adds 2, to add some offset
                int position_y = row * 41 + 28;
                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a column in pixels
                // then add 2, to add some offset
                int position_x = col * 41 + 14;
                // set the fill color to black (you could set it to whatever you want)
                context.setFill(Color.BLACK);
                // set the font, from a new font, constructed from the system one, with size 20
                context.setFont(new Font(20));
                // check if value of coressponding array position is not 0
                if(initial[row][col]!=0) {
                    // draw the number
                    context.fillText(initial[row][col] + "", position_x, position_y);
                }
            }
        }

        // draw the players numbers from our GameBoard instance
        int[][] player = gameboard.getPlayer();
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {
                // finds the y position of the cell, by multiplying the row number by 50, which is the height of a row in pixels
                // then adds 2, to add some offset
                int position_y = row * 41 + 28;
                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a column in pixels
                // then add 2, to add some offset
                int position_x = col * 41 + 14;
                // set the fill color to purple (you could set it to whatever you want)
                if (gameboard.isCorrect(row,col)) {
                    context.setFill(Color.LIMEGREEN);
                }else {
                    context.setFill(Color.RED);
                }
                // set the font, from a new font, constructed from the system one, with size 20
                context.setFont(new Font(22));
                // check if value of coressponding array position is not 0
                if(player[row][col]!=0) {
                    // draw the number
                    context.fillText(player[row][col] + "", position_x, position_y);
                }
            }
        }


        // when the gameboard returns true with its checkForSuccess
        // method, that means it has found no mistakes

        if(gameboard.checkForSuccessGeneral() == true) {

            // clear the canvas
            context.clearRect(0, 0, 372, 372);
            // set the fill color to green
            context.setFill(Color.GREEN);
            // set the font to 36pt
            context.setFont(new Font(36));
            // display SUCCESS text on the screen
            context.fillText("SUCCESS!", 150, 250);
        }


    }

    /***
     * Method connected with the canvas onclick event handler
     */
    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                int mouse_x = (int) event.getX();
                int mouse_y = (int) event.getY();

                // convert the mouseX and mouseY into rows and cols
                // we are going to take advantage of the way integers are treated and we are going to divide by a cell's width
                // this way any value between 0 and 449 for x and y is going to give us an integer from 0 to 8, which is exactly what we are after
                player_selected_row = (int) (mouse_y / 41);
                player_selected_col = (int) (mouse_x / 41);

                //get the canvas graphics context and redraw

                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        });
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonOnePressed() {
        // The only thing that changes between all nine methods is the value we are injecting
        // in the player array. In this case, it is 1, because it corresponds to the button.
        gameboard.modifyPlayer(1, player_selected_row, player_selected_col);

        // refresh our canvas
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonTwoPressed() {
        gameboard.modifyPlayer(2, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonThreePressed() {
        gameboard.modifyPlayer(3, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonFourPressed() {
        gameboard.modifyPlayer(4, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonFivePressed() {
        gameboard.modifyPlayer(5, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonSixPressed() {
        gameboard.modifyPlayer(6, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonSevenPressed() {
        gameboard.modifyPlayer(7, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonEightPressed() {
        gameboard.modifyPlayer(8, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    /***
     * Method connected with a button onclick event handler
     */
    public void buttonNinePressed() {
        gameboard.modifyPlayer(9, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }
}

