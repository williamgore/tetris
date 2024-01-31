import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GoreWilliamA5Q7 extends PApplet {

/*
 William Gore
 Heather Matheson
 COMP 1010 A01
 Assignment 5 - Units 1-19 - Tetris
 As per assignment details, piece rotation, saving and piece colour 
 variation are not to be implemented
 Question 7
 */

final int CELLSIZE = 30; //size of each cell
final int NUM_ROWS = 20; //number of rows
final int NUM_COLS = 12; //number of columns

final int SQUARES_PER_PIECE = 4; //number of cells in each piece

int[] fallingX; //the grid coordinates of the falling piece
int[] fallingY; //in x and y

int[] landedX;  //stores the x values of fallen cells
int[] landedY;  //stores the y values of fallen cells
int landedSize; //store the number of fallen cells

int fallSpeed = 60; //frames that pass between each fall
int counter = 0;    //keeps track of passed frames

int score;  //stores the number of full lines achieved

boolean falling = false; //stores wether or not any cell is falling

public void setup() {
   //size MUST be (NUM_COLS*CELLSIZE) by (NUM_ROWS*CELLSIZE);
  newGame();
}

/*
  sets all arrays, score, and array sizes to their default
  changes fallingX, fallingY, landedX, landedY, landedSize, and score
*/
public void newGame() {
  fallingX = new int[SQUARES_PER_PIECE];
  fallingY = new int[SQUARES_PER_PIECE];

  landedX = new int[NUM_ROWS*NUM_COLS];
  landedY = new int[NUM_ROWS*NUM_COLS];
  
  //setting the initial size of the landed arrays
  landedSize = 0;
  
  //setting initial score
  score = 0;
}

public void draw() {
  if (!testEndGame()) {
    background(180);
    
    //checking if there is currently a falling piece
    if (!falling) {
      //generate a random game piece
      generatePiece(fallingX, fallingY, 0); 
      falling = true;
    }
    
    //setting landed size, only changes if there is a full row
    landedSize = clearFullRows(landedX, landedY, landedSize);


    //draw the piece 
    drawCells(fallingX, fallingY, SQUARES_PER_PIECE, 0xff44FFFF);
    
    //drawing the landed pieces
    //in purple
    drawCells(landedX, landedY, landedSize, 0xffB026FF);
    
    
    //checking if enough frames have passed to make the
    //piece fall
    if (counter % fallSpeed == 0) {
      makeMove(fallingX, fallingY, 0, 1);
    }
    //increasing frame count w/ every frame
    counter++;
  }
}

/*
  generates a cell at a given x and y in a given colour
 uses CELLSIZE
 recieves an array of x values, and array of y values, the size of said arrays and
 a colour, all in integers
 */
public void drawCells(int[] x, int[] y, int size, int colour) {
  //setting the fill to given colour
  fill(colour);

  //looping through the given arrays
  for (int i = 0; i < size; i ++) {
    //setting the x and y values for the top left of a cell
    int cellX = x[i] * CELLSIZE;
    int cellY = y[i] * CELLSIZE;
    //drawing the cell
    rect(cellX, cellY, CELLSIZE, CELLSIZE);
  }
}


/*
  generates a random, 4 cell piece and puts it into fallingX and fallingY
  changes fallingX and fallingY
*/
public void generatePiece(int[] x, int[] y, int startRow) {
  int piece = (int)random(0, 12); 

  //vertical line
  if (piece == 0) {
    x[0] = 6;
    x[1] = 6;
    x[2] = 6;
    x[3] = 6;

    y[0] = startRow - 3;
    y[1] = startRow - 2;
    y[2] = startRow - 1;
    y[3] = startRow;
  }

  //L
  else if (piece == 1) {
    x[0] = 5;
    x[1] = 5;
    x[2] = 5;
    x[3] = 6;

    y[0] = startRow - 2;
    y[1] = startRow - 1;
    y[2] = startRow;
    y[3] = startRow;
  }

  //horizontal line
  else if (piece == 2) {
    x[0] = 4;
    x[1] = 5;
    x[2] = 6;
    x[3] = 7;

    y[0] = startRow;
    y[1] = startRow;
    y[2] = startRow;
    y[3] = startRow;
  }

  //square
  else if (piece == 3) {
    x[0] = 5;
    x[1] = 6;
    x[2] = 5;
    x[3] = 6;

    y[0] = startRow;
    y[1] = startRow;
    y[2] = startRow - 1;
    y[3] = startRow - 1;
  }

  //   O
  // O O O <- this shape
  else if (piece == 4) {
    x[0] = 6;
    x[1] = 5;
    x[2] = 6;
    x[3] = 7;

    y[0] = startRow - 1;
    y[1] = startRow;
    y[2] = startRow;
    y[3] = startRow;
  }



  //   O O
  // O O    <- this shape
  else if (piece == 5) {
    x[0] = 6;
    x[1] = 7;
    x[2] = 5;
    x[3] = 6;

    y[0] = startRow - 1;
    y[1] = startRow - 1;
    y[2] = startRow;
    y[3] = startRow;
  }

  //  O
  //O O
  //  O
  else if (piece == 6) {
    x[0] = 6;
    x[1] = 7;
    x[2] = 7;
    x[3] = 7;

    y[0] = startRow - 1;
    y[1] = startRow - 2;
    y[2] = startRow - 1;
    y[3] = startRow;
  }

  // O
  // O O
  //   O
  else if (piece == 7) {
    x[0] = 6;
    x[1] = 6;
    x[2] = 7;
    x[3] = 7;

    y[0] = startRow - 1;
    y[1] = startRow - 2;
    y[2] = startRow - 1;
    y[3] = startRow;
  }

  //   O
  // O O
  // O
  else if (piece == 8) {
    x[0] = 7;
    x[1] = 7;
    x[2] = 6;
    x[3] = 6;

    y[0] = startRow - 1;
    y[1] = startRow - 2;
    y[2] = startRow - 1;
    y[3] = startRow;
  }

  // O
  // O O
  // O
  else if (piece == 9) {
    x[0] = 6;
    x[1] = 6;
    x[2] = 6;
    x[3] = 7;

    y[0] = startRow - 2;
    y[1] = startRow - 1;
    y[2] = startRow;
    y[3] = startRow - 1;
  } 
  
  // O O O
  //   O
  else if (piece == 10) {
    x[0] = 5;
    x[1] = 6;
    x[2] = 7;
    x[3] = 6;

    y[0] = startRow - 1;
    y[1] = startRow - 1;
    y[2] = startRow - 1;
    y[3] = startRow;
  }

  // backwards L
  else if (piece == 11) {
    x[0] = 6;
    x[1] = 6;
    x[2] = 6;
    x[3] = 5;

    y[0] = startRow - 2;
    y[1] = startRow - 1;
    y[2] = startRow;
    y[3] = startRow;
  }
}


/*
  searches two arrays for a set of x and y values at the same index
  recieves two arrays, the size of the two arrays, and the values to
  search for
  returns a boolean, true if the values are found
*/
public boolean search(int[] xValues, int[] yValues, int numValues, int x, int y) {
  //going through all x and y values
  for (int i = 0; i < numValues; i++) {
    //checking if x[i] match the given x
    //and y[i] match given y
    if (xValues[i] == x && yValues[i] == y) {
      return true; //if so return true
    }
  }
  //otherwise false
  return false;
}


/*
  checks if a piece can move left(-1) or right(1)
  calls on search(), landPiece()
  uses fallingX, fallingY, landedX, landedY, SQUARES_PER_PIECE
  returns true if so
*/
public boolean moveAllowedX(int[] fallingX, int[] fallingY, int[] landedX, int[] landedY, int numLanded, int deltaX) {
  //cycling through all cells in the piece
  for (int i = 0; i < SQUARES_PER_PIECE; i++) {
    if (fallingX[i] + deltaX == NUM_COLS || fallingX[i] + deltaX < 0) {
      return false;
    } else if (search(landedX, landedY, numLanded, fallingX[i] + deltaX, fallingY[i])) {
      return false;
    }
  }
  return true;
}


/*
  checks if a piece can move down(+1)
  calls on search(), landPiece()
  uses fallingX, fallingY, landedX, landedY, SQUARES_PER_PIECE
  returns true if so
*/
public boolean moveAllowedY(int[] fallingX, int[] fallingY, int[] landedX, int[] landedY, int numLanded, int deltaY) {
  for (int i = 0; i < SQUARES_PER_PIECE; i++) {
    if (fallingY[i] + deltaY == NUM_ROWS) {
      landedSize = landPiece(fallingX, fallingY, landedX, landedY, numLanded);
      return false;
    } else if (search(landedX, landedY, numLanded, fallingX[i], fallingY[i] + deltaY)) {
      landedSize = landPiece(fallingX, fallingY, landedX, landedY, numLanded);
      return false;
    }
  }
  return true;
}


/*
  moves the falling piece left right or down
  calls on moveAllowedY(), moveAllowedX()
  uses SQUARES_PER_PIECE
*/
public void makeMove(int[] fallingX, int[] fallingY, int deltaX, int deltaY) {
  if (moveAllowedY(fallingX, fallingY, landedX, landedY, landedSize, deltaY)) {
    for (int i = 0; i < SQUARES_PER_PIECE; i++) {
      fallingY[i] += deltaY;
    }
  }
  if (moveAllowedX(fallingX, fallingY, landedX, landedY, landedSize, deltaX)) {
    for (int i = 0; i < SQUARES_PER_PIECE; i++) {
      fallingX[i] += deltaX;
    }
  }
}

public void keyPressed() {

  //left
  if (keyCode == 37) {
    makeMove(fallingX, fallingY, -1, 0);
  }

  //right
  else if (keyCode == 39) {
    makeMove(fallingX, fallingY, 1, 0);
  }

  //down
  else if (keyCode == 40) {
    makeMove(fallingX, fallingY, 0, 1);
  }
  
  //restarts the game
  else if (key == 'r') {
    if(testEndGame()) {
      newGame();
    }
  }
}



/*
  transfers contents of fallingX and fallingY to landedX and landedY
  uses SQUARES_PER_PIECE, landedX, landedY, fallingX fallingY, falling
  changes falling, landedX, landedY
  returns the new size of partially filled arrays, landedX and landedY
*/
public int landPiece(int[] fallingX, int[] fallingY, int[] landedX, int[] landedY, int numLanded) {
  for (int i = 0; i < SQUARES_PER_PIECE; i++) {
    //adding the fallen cells to the end of numLanded
    landedX[numLanded + i] = fallingX[i];
    landedY[numLanded + i] = fallingY[i];
  }
  
  //increasing the size of numlanded
  numLanded += SQUARES_PER_PIECE;
  
  //setting falling to false
  falling = false;
  
  //returning the new size of landed arrays
  return numLanded;
}


/*
  clears all values with a corresponding y value, that is given
  typically recieves, landedX, landedY, and landedSize
  uses NUM_ROWS
  calls on removeRow
*/
public int clearFullRows(int[] x, int[] y, int n) {
  for (int i = NUM_ROWS; i > 0; i--) {
    //checking if a given row is full
    if (rowFull(y, n, i)) {
      //sending that row to be removed
      n = removeRow(x, y, n, i);
    }
  }
  //returning the new size of the landed arrays
  return n;
}


/*
 checks if a given row is full
 uses NUM_COLS
 returns a boolean, recieves an array, array size and row number
 */
public boolean rowFull(int[] y, int n, int row) {
  int entries = 0; //to count number of entries in a given row
  for (int i = 0; i < n; i++) {
    if (y[i] == row) {
      entries++; //increasing entries if the item is in the given row
    }
  }
  
  //checking if the number of entries is equal to the
  //number of cells in a row
  if (entries == NUM_COLS) {
    return true;
  }
  return false;
}


/*
  removes all cells with a given y value
 calls on deleteItem()
 changes score
 returns the new size of the given arrays
 */
public int removeRow(int[] x, int[] y, int n, int row) {
  //deleting every item in a given row
  //from back to start, since index numbers change as we go
  for (int i = n - 1; i >= 0; i--) {
    if (y[i] == row) {
      deleteItem(x, n, i);
      n = deleteItem(y, n, i);
    }
  }
  
  //shifting all the cells above the deleted
  //row down one row
  for (int i = 0; i < n; i++) {
    if (y[i] < row) {
      y[i]++;
    }
  }

  //increasing the score
  score++;

  //checking if speed should be increased
  if (score % 5 == 0) {
    fallSpeed = (int)(fallSpeed * 0.8f);
  }

  return n;
}

/*
  deletes an item in a given array at a given index
 returns the new size of that partially filled array
 recieves an array, that arrays size, and the index of
 the item to be deleted
 */
public int deleteItem(int[] items, int numItems, int index) {
  items[index] = -1;
  
  //shifting all items down one index
  for (int i = index; i <= numItems; i++) {
    items[i] = items[i + 1];
  }
  
  //returning a size of one smaller
  return numItems - 1;
}

/*
  tests if the game should be ended
 uses landedSize,  score
 returns a boolean
 */
public boolean testEndGame() {
  for (int i = 0; i < landedSize; i++) {
    if (landedY[i] < 0) {
      
      //creating a string and its placement
      //to display game over
      String gameOver = "GAME OVER!";
      textSize(30);
      float x = width/2 - textWidth(gameOver)/2;
      fill(0);
      text(gameOver, x, height/2);

      //a second string to display score and 
      //play again option
      float y = height/2 + textAscent() + textDescent();
      textSize(20);
      String scoreDisplay = "Lines completed: " + score + "\n"
      + "Press R to play again";
      x = width/2 - textWidth(scoreDisplay)/2;
      text(scoreDisplay, x, y);
      
      //returning true to indicate the game has ended
      return true;
    }
  }
  return false;
}
  public void settings() {  size(360, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GoreWilliamA5Q7" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
