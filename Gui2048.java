//------------------------------------------------------------------//
// Gui2048.java                                                     //
//                                                                  //
// GUI Driver for 2048                                             //
//                                                                  //
// Author:  Ujjwal Gulecha		                             //
// Date:    11/09/16                                                //
//------------------------------------------------------------------//
/* 	
 * Name: Zhaoyi Huang
 * Login: cs8bwaic
 * Date:Feb 25 2017
 * File:Gui2048.java
 * Source for help:TA
 * Wring methods to do the GUI version of 2048 game.
 * */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/**
 * This class is to make the GUI version of 2048 by adding color values,
 * text values, and boards into the game.
 */
public class Gui2048 extends Application {
   private String outputBoard; // The filename for where to save the Board
   private Board board; // The 2048 Game Board

   private static final int TILE_WIDTH = 106;

   private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
   private static final int TEXT_SIZE_MID = 45; // Mid value tiles
   // (128, 256, 512)
   private static final int TEXT_SIZE_HIGH = 35; // High value tiles
   // (1024, 2048, Higher)

   // Fill colors for each of the Tile values
   private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
   private static final Color COLOR_2 = Color.rgb(238, 228, 218);
   private static final Color COLOR_4 = Color.rgb(237, 224, 200);
   private static final Color COLOR_8 = Color.rgb(242, 177, 121);
   private static final Color COLOR_16 = Color.rgb(245, 149, 99);
   private static final Color COLOR_32 = Color.rgb(246, 124, 95);
   private static final Color COLOR_64 = Color.rgb(246, 94, 59);
   private static final Color COLOR_128 = Color.rgb(237, 207, 114);
   private static final Color COLOR_256 = Color.rgb(237, 204, 97);
   private static final Color COLOR_512 = Color.rgb(237, 200, 80);
   private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
   private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
   private static final Color COLOR_OTHER = Color.BLACK;
   private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

   private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
   // For tiles >= 8

   private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);
   // For tiles < 8

   private GridPane pane;

   /** Add your own Instance Variables here */
   //the game name as final value
   private static final String GAME_NAME = "2048 flipped";

   /**
    * Method to  create the visible part
    * @param primaryStage the stage of the visible part
    */
   @Override
      public void start(Stage primaryStage) {
         // Process Arguments and Initialize the Game Board
         processArgs(getParameters().getRaw().toArray(new String[0]));

         // Create the pane that will hold all of the visual objects
         pane = new GridPane();
         pane.setAlignment(Pos.CENTER);
         pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
         pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
         // Set the spacing between the Tiles
         pane.setHgap(15);
         pane.setVgap(15);

         /** Add your Code for the GUI Here */
         //create new stackpane to hold grid pane
         StackPane root = new StackPane();
         root.getChildren().add(pane);
         //get Grid size and grid
         int GRID_SIZE = board.getGrid().length;
         int[][] GRID = board.getGrid();
         //set scene to be the one using root
         Scene scene = new Scene(root);
         primaryStage.setScene(scene);
         //create text1 to hold game name
         Text t1 = new Text(GAME_NAME);
         //set font of the text
         t1.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                  FontPosture.REGULAR, 30));
         t1.setFill(Color.WHITE);
         //set text of the score part
         Text t2 = new Text("Score: " + board.getScore());
         t2.setFont(Font.font("Courier", FontWeight.BOLD, 
                  FontPosture.REGULAR, 30));
         t2.setFill(COLOR_GAME_OVER);
         //create cells into the grid pane
         Rectangle[][] cells = new Rectangle[GRID_SIZE][];
         for (int i = 0; i < GRID_SIZE; i++) {
            cells[i] = new Rectangle[GRID_SIZE];
         }
         //add score and 2048 to the pane
         pane.add(t1, 0, 0, 2, 1);
         pane.add(t2, 2, 0, 2, 1);
         //set alignment of the title and score
         GridPane.setHalignment(t1, HPos.CENTER);
         GridPane.setHalignment(t2, HPos.CENTER);
         //add cells to the gridpane
         for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
               cells[i][j] = new Rectangle(changeGrid(GRID_SIZE),
                     changeGrid(GRID_SIZE),
                     updateColor(GRID[i][j]));
               //create text to be displayed on tiles
               Text t = new Text(numShow(GRID[i][j]));
               t.setFont(Font.font("Arial Black", FontWeight.BOLD, 
                        FontPosture.REGULAR, fontSize(GRID[i][j])));
               //bind the size of tiles to the width and height property
               cells[i][j].widthProperty().bind(primaryStage.widthProperty().
                     divide(1.3).subtract((GRID_SIZE) * 10).
                     divide(GRID_SIZE));
               cells[i][j].heightProperty().bind(primaryStage.
                     heightProperty().divide(1.3).
                     subtract((GRID_SIZE) * 10).divide(GRID_SIZE));
               //fill the color of the text
               t.setFill(numColor(GRID[i][j]));
               //add tiles and text
               pane.add(cells[i][j], j, i + 1, 1, 1);
               pane.add(t, j, i + 1, 1, 1);
               //set alignment
               GridPane.setHalignment(t, HPos.CENTER);
            }
         }

         //set the inner class for key event
         class myKeyHandler implements EventHandler<KeyEvent>
         {
            @Override
               public void handle(KeyEvent e)
               {
                  // new boolean l for determine the move condition
                  boolean l;
                 //if the board is game over, return without executing
                  if(board.isGameOver()){
                	  return;
                  }
                  //if it is not game over then moves
                  else if (!board.isGameOver()) {
                     if (e.getCode().equals(KeyCode.UP)) {
                        try {
                           l = board.move(Direction.UP);
                           // add a new random tile to the board if can move
                           if (l == true) {
                              System.out.println("MOVING UP");
                              board.addRandomTile();
                           }
                        } catch (IOException e1) {
                           e1.printStackTrace();
                        }

                     } else if (e.getCode() == KeyCode.DOWN) {
                        try {
                           //get the condition for moving down
                           l = board.move(Direction.DOWN);
                           // add a new random tile to the board
                           if (l == true) {
                              System.out.println("MOVING DOWN");
                              board.addRandomTile();
                           }
                        } catch (IOException e1) {
                           e1.printStackTrace();
                        }
                     } else if (e.getCode() == KeyCode.LEFT) {
                        try {
                           l = board.move(Direction.LEFT);
                           // add a new random tile to the board
                           if (l == true) {
                              System.out.println("MOVING LEFT");
                              board.addRandomTile();
                           }
                        } catch (IOException e1) {
                           e1.printStackTrace();
                        }
                     } else if (e.getCode() == KeyCode.RIGHT) {
                        try {
                           l = board.move(Direction.RIGHT);
                           // add a new random tile to the board
                           if (l == true) {
                              System.out.println("MOVING RIGHT");
                              board.addRandomTile();
                           }
                        } catch (IOException e1) {
                           e1.printStackTrace();
                        }
                     } else if (e.getCode() == KeyCode.R) {
                        //do the flip for the board
                        board.flip(3);
                     } else if (e.getCode() == KeyCode.S) {
                        try {
                           //save the board and print destination of saving
                           board.saveBoard(outputBoard);
                           System.out.println("Saving Board to "+ outputBoard);
                        } catch (IOException error) {
                           error.printStackTrace();
                        }
                     }

                  }
                  //clear the pane
                  pane.getChildren().clear();
                  //re add scores, title and grid in
                  pane.add(t1, 0, 0, 2, 1);
                  //remade the score to be t3
                  Text t3 = new Text("Score: " + board.getScore());
                  t3.setFont(Font.font("Courier", FontWeight.BOLD, 
                           FontPosture.REGULAR, 30));
                  t3.setFill(COLOR_2048);
                  //add t3 to the game
                  pane.add(t3, 2, 0, 2, 1);
                  GridPane.setHalignment(t1, HPos.CENTER);
                  GridPane.setHalignment(t3, HPos.CENTER);
                  //re add the grids and tiles into the game
                  for (int i = 0; i < GRID_SIZE; i++) {
                     for (int j = 0; j < GRID_SIZE; j++) {
                        cells[i][j] = new Rectangle(changeGrid(GRID_SIZE), 
                              changeGrid(GRID_SIZE), updateColor(GRID[i][j]));
                        cells[i][j].widthProperty().bind(primaryStage.
                              widthProperty().divide(1.3).
                              subtract((GRID_SIZE) * 10).divide(GRID_SIZE));
                        cells[i][j].heightProperty().bind(primaryStage.
                              heightProperty().divide(1.3).
                              subtract((GRID_SIZE) * 10).divide(GRID_SIZE));
                        //set text and displacement
                        Text t = new Text(numShow(GRID[i][j]));
                        t.setFont(Font.font("Arial Black", FontWeight.BOLD, 
                                 FontPosture.REGULAR, fontSize(GRID[i][j])));
                        //set color
                        t.setFill(numColor(GRID[i][j]));

                        pane.add(cells[i][j], j, i + 1, 1, 1);
                        pane.add(t, j, i + 1, 1, 1);
                        GridPane.setHalignment(t, HPos.CENTER);
                        // if the board is game over
                        if (board.isGameOver()) {
                           //set color of overlay
                           Color cgo = new Color(0.86, 0.78, 0.70, 0.06);
                           // if game is over, display a message for that
                           Rectangle r = new Rectangle(0, 0, 20, 20);
                           r.setFill(cgo);
                           //bind the overlay with the property of pane
                           r.widthProperty().bind(pane.widthProperty());
                           r.heightProperty().bind(pane.heightProperty());
                           //set game over text
                           Text tg = new Text("Game Over!");
                           tg.setFont(Font.font("Arial Black", FontWeight.BOLD, 
                                    FontPosture.REGULAR, 50));
                           tg.setFill(Color.BLACK);
                           //add overlay and text to the stack pane
                           root.getChildren().add(r);
                           root.getChildren().add(tg);

                        }
                     }
                  }
               }
         }
         //use the key handler
         scene.setOnKeyPressed(new myKeyHandler());
         //set title height and width, then show the stage
         primaryStage.setTitle("GUI2048 by Zhaoyi Huang");
         primaryStage.setMinHeight(600);
         primaryStage.setMinWidth(600);
         primaryStage.show();

      }

   /** Add your own Instance Methods Here */
   /**
    * Method to updateColor on the tile
    * @param numOnTile the number displayed on tile
    */
   private static Color updateColor(int numOnTile) {
      //using switch to switch between colors
      switch(numOnTile) {
         case (2):
            return COLOR_2;
         case (4):
            return COLOR_4;
         case (8):
            return COLOR_8;
         case (16):
            return COLOR_16;
         case (32):
            return COLOR_32;
         case (64):
            return COLOR_64;
         case (128):
            return COLOR_128;
         case (256):
            return COLOR_256;
         case (512):
            return COLOR_512;
         case (1024):
            return COLOR_1024;
         case (2048):
            return COLOR_2048;
         case (0):
            return COLOR_EMPTY;
      }
      return COLOR_OTHER;
   }

   /**
    * Method to updateColor on the text on tiles
    * @param numOnTile the number displayed on tile
    */
   private static Color numColor(int numOnTile) {
      // if num less than 8, return dark
      if (numOnTile <= 8) {
         return COLOR_VALUE_DARK;
      }
      //else return light color
      return COLOR_VALUE_LIGHT;
   }

   /**
    * Method to update font size on the tile
    * @param numOnTile the number displayed on tile
    */
   private static int fontSize(int numOnTile) {
      //if tile less than 128
      if (numOnTile < 128) {
         return TEXT_SIZE_LOW;
      } else if (numOnTile >= 128 && numOnTile <= 512) {
         return TEXT_SIZE_MID;
      }
      //return different case
      return TEXT_SIZE_HIGH;
   }

   /**
    * Method to update the number showing on tiles
    * @param numOnTile the number displayed on tile
    */
   private static String numShow(int numOnTile) {
      //if the integer is 0
      if (numOnTile == 0) {
         //return empty array
         return "";
      } else {
         //return numbers if not 0
         return "" + numOnTile;
      }
   }

   /**
    * Method to change tile size when grid increase in size(like from
    * 4 to 5) without change the window
    * @param gridsize the grid size inputing
    */
   private static int changeGrid(int gridsize) {
      if (gridsize == 4) {
         return TILE_WIDTH;
      } else {
         return (int) (TILE_WIDTH * Math.sqrt(4 / gridsize));
      }
   }

   /** DO NOT EDIT BELOW */

   // The method used to process the command line arguments
   private void processArgs(String[] args) {
      String inputBoard = null; // The filename for where to load the Board
      int boardSize = 0; // The Size of the Board

      // Arguments must come in pairs
      if ((args.length % 2) != 0) {
         printUsage();
         System.exit(-1);
      }

      // Process all the arguments
      for (int i = 0; i < args.length; i += 2) {
         if (args[i].equals("-i")) { // We are processing the argument that
            // specifies
            // the input file to be used to set the
            // board
            inputBoard = args[i + 1];
         } else if (args[i].equals("-o")) { // We are processing the argument
            // that specifies
            // the output file to be used to
            // save the board
            outputBoard = args[i + 1];
         } else if (args[i].equals("-s")) { // We are processing the argument
            // that specifies
            // the size of the Board
            boardSize = Integer.parseInt(args[i + 1]);
         } else { // Incorrect Argument
            printUsage();
            System.exit(-1);
         }
      }

      // Set the default output file if none specified
      if (outputBoard == null)
         outputBoard = "2048.board";
      // Set the default Board size if none specified or less than 2
      if (boardSize < 2)
         boardSize = 4;

      // Initialize the Game Board
      try {
         if (inputBoard != null)
            board = new Board(new Random(), boardSize);
         else
            board = new Board(new Random(), boardSize);
      } catch (Exception e) {
         System.out.println(
               e.getClass().getName() + " was thrown while creating a " + "Board from file " + inputBoard);
         System.out.println("Either your Board(String, Random) " + "Constructor is broken or the file isn't "
               + "formated correctly");
         System.exit(-1);
      }
   }

   // Print the Usage Message
   private static void printUsage() {
      System.out.println("Gui2048");
      System.out.println("Usage:  Gui2048 [-i|o file ...]");
      System.out.println();
      System.out.println("  Command line arguments come in pairs of the " + "form: <command> <argument>");
      System.out.println();
      System.out.println("  -i [file]  -> Specifies a 2048 board that " + "should be loaded");
      System.out.println();
      System.out.println("  -o [file]  -> Specifies a file that should be " + "used to save the 2048 board");
      System.out.println("                If none specified then the " + "default \"2048.board\" file will be used");
      System.out.println("  -s [size]  -> Specifies the size of the 2048" + "board if an input file hasn't been");
      System.out.println("                specified.  If both -s and -i" + "are used, then the size of the board");
      System.out.println("                will be determined by the input" + " file. The default size is 4.");
   }

}
