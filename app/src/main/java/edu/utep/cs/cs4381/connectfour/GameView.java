package edu.utep.cs.cs4381.connectfour;
//Joaquin Hidalgo

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import java.util.Iterator;

import edu.utep.cs.cs4381.connectfour.model.Board;
import edu.utep.cs.cs4381.connectfour.model.Logo;
import edu.utep.cs.cs4381.connectfour.model.Player;

public class GameView extends View {

    Board board;
    Paint outlineDisc = new Paint();
    Paint winDisc = new Paint();
    Paint squareBd = new Paint();
    Paint emptyDisc = new Paint();
    Paint p1Disc = new Paint();
    Paint p2Disc = new Paint();
    Paint endingSqu = new Paint();
    Paint textEnd = new Paint();
    Paint startNewSqu = new Paint();
    Paint startSquOutline = new Paint();
    Paint startNewText = new Paint();
    Paint logoText = new Paint();
    private int width, height;
    private int bottomOfPopUp = 1150;
    private int margin = 20;
    private int cirDiameter = 120;
    private int topOfBoard = 0;
    private int discPadding = 25;
    private boolean gameOver, fullB, validMove = false;
    Player p1 = new Player("player1");
    Player p2 = new Player("player2");
    Player curr;
    Logo lo = new Logo(this);
    public Iterable<Board.Place> winRow;

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    public void init(AttributeSet attrs, int defStyleAtr){
        this.board = new Board(); //maybe switch to top initiz.
        calculateWidthAndHeight();

        winDisc.setStyle(Paint.Style.FILL);
        winDisc.setColor(Color.BLACK);
        winDisc.setAntiAlias(true);

        squareBd.setStyle(Paint.Style.FILL);
        squareBd.setColor(Color.BLUE);

        emptyDisc.setStyle(Paint.Style.FILL);
        emptyDisc.setColor(Color.WHITE);

        p1Disc.setStyle(Paint.Style.FILL);
        p1Disc.setColor(Color.YELLOW);
        p1Disc.setAntiAlias(true);

        p2Disc.setStyle(Paint.Style.FILL);
        p2Disc.setColor(Color.RED);
        p2Disc.setAntiAlias(true);

        endingSqu.setStyle(Paint.Style.FILL);
        endingSqu.setARGB(230,0,0,0);

        textEnd.setStyle(Paint.Style.FILL);
        textEnd.setTextSize(150);
        textEnd.setAntiAlias(true);


        outlineDisc.setStyle(Paint.Style.STROKE);
        outlineDisc.setColor(Color.BLACK);
        outlineDisc.setStrokeWidth(10);
        outlineDisc.setAntiAlias(true);

        startNewSqu.setStyle(Paint.Style.FILL);
        startNewSqu.setColor(Color.BLUE);

        startNewText.setStyle(Paint.Style.FILL);
        startNewText.setColor(Color.WHITE);
        startNewText.setTextSize(110);

        logoText.setStyle(Paint.Style.FILL);
        logoText.setColor(Color.BLACK);
        logoText.setTextSize(150);

        startSquOutline.setStyle(Paint.Style.STROKE);
        startSquOutline.setColor(Color.WHITE);
        startSquOutline.setStrokeWidth(20);
        startSquOutline.setAntiAlias(true);

        curr = p1; //set p1 to first player
    }

    /////////////// TOUCH UI... when user touches screen
    //Loop engine of game
    public boolean onTouchEvent(MotionEvent event) {
        // 1st step (UI)
        switch (event.getActionMasked() ) {
            case MotionEvent.ACTION_DOWN:

                int index = locateDisc(event.getX(), event.getY());  // if (index >= 0) { discClickListener.clicked(index); } //maybe for toast

                //If game over and Clicks start new game
                if(gameOver && isInRect( event.getX(), event.getY(),90,bottomOfPopUp-410, width-90, bottomOfPopUp-110) ){
                    this.board.clear();
                    gameOver = false;
                    curr = p1;
                    invalidate();
                    break;
                }

                //player1 turn and drop disc into valid spot and update board
                if(index >= 0 && board.isColumnOpen(index) && !gameOver ){
                    validMove = true;
                    if(curr == p1){
                        board.dropDisc(index, p1);
                    }
                    //player2 turn
                    else{
                        board.dropDisc(index, p2);
                    }

                    //Checks to see if disc dropped is won by player
                    if(board.isWonBy(curr) ){
                        gameOver = true;
                        invalidate(); //ogß
                        break; //game over
                    }

                    //checks to see if TIE on board
                    else if(board.isFull() ){
                        fullB = true;
                        gameOver = true;
                        invalidate(); //og
                        break;
                    }


                    //if valid moved was acted upon, switch players then
                    if(validMove){
                        if(curr == p1){
                            curr = p2;
                        }
                        else{
                            curr = p1;
                        }
                        validMove = false;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    //checks every circles to determine if in circle. if so return that index of circle. circle 0,1,2,3...
    public int locateDisc(float x, float y) {
        for(int i =0; i< board.numOfColumns(); i++){
            if(isIn(x,y, (margin+cirDiameter/2)+(i*cirDiameter) +((i+1)*discPadding),topOfBoard - discPadding-(cirDiameter), cirDiameter/2) ){
                return i;
            }
        }
        return -1;
    }

    //checks to see if touch is within radius of user touch(cX, cY) circle
    private boolean isIn(float x, float y, float cX, float cY, float radius) {
        float dx = x - cX;
        float dy = y - cY;
        return dx * dx + dy * dy <= radius * radius;
    }

    //checks to see if touch is within area of user touch(cX, cY) rectangle
    private boolean isInRect(float x, float y, float left, float top, float right, float bottom) {
            return new RectF(left, top, right, bottom).contains(x, y);
    }
    ///////////////....

    //get width and height of screen showing
    private void calculateWidthAndHeight() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    width = Math.min(getWidth(), getHeight());
                    height = Math.max(getWidth(), getHeight());
                }
            });
        }
    }

    //draws the button to START NEW game
    public void drawPlayAgain(Canvas cv){
        cv.drawRect(100,bottomOfPopUp-400, width-100, bottomOfPopUp-120, startNewSqu);
        cv.drawRect(100,bottomOfPopUp-400, width-100, bottomOfPopUp-120, startSquOutline);
        cv.drawText("Start New",320,bottomOfPopUp-225, startNewText);
    }

    //Draws the wining 4 disc's that player played and colors them half-size
    public void drawWinDisc(Canvas canvas) {
        Iterable<Board.Place> t = board.winningRow();
        Iterator<Board.Place> t2 = t.iterator();

        int xPos[] = new int[4];
        int yPos[] = new int[4];

        for (int i = 0; i < 4; i++) {
            Board.Place t3 = t2.next();
            xPos[i] = t3.getX();
            yPos[i] = t3.getY();
        }

        for (int r = 0; r < board.numOfRows(); r++) {
            for (int c = 0; c < board.numOfColumns(); c++) {
                for (int j = 0; j < 4; j++) {
                    if (xPos[j] == c && yPos[j] == r) {
                        canvas.drawCircle((margin + cirDiameter / 2) + (c * cirDiameter) + ((c + 1) * discPadding), (((height - margin) - (cirDiameter / 2)) + ((r + 1) * cirDiameter) + ((r + 1) * discPadding)) - topOfBoard, cirDiameter / 3, winDisc);
                    }
                }
            }
        }
    }

    //draws pop square at the END of each game
    public void drawBackEnd(Canvas cv){
        cv.drawRect(0+margin, 0+margin, width-margin,bottomOfPopUp, endingSqu); //orginal 850  // bottomOfPopUp == 1150
    }

    //draws the Result's of the game
    public void drawWonBy(Canvas cv, Player p){
        if(p == p1){
            textEnd.setColor(Color.YELLOW);
            cv.drawText("Game Over",200,250, textEnd);
            cv.drawText("Player 1 WINS!",50,550, textEnd);
        }
        else if(p == p2){
            textEnd.setColor(Color.RED);
            cv.drawText("Game Over",200,250, textEnd);
            cv.drawText("Player 2 WINS!",50,550, textEnd);
        }
        else{
            textEnd.setColor(Color.WHITE);
            cv.drawText("Tie Game!",220,250, textEnd);
        }

    }

    //draws board and disc's in board corresponding to players specific disc or null
    public void drawBoard(Canvas canvas){
        //Background of gameboard
        topOfBoard = (height-margin)-(board.numOfRows() * cirDiameter) -(discPadding*board.numOfRows()+1 );
        canvas.drawRect(margin, topOfBoard, width-margin, height-margin, squareBd);

        //Draw empty discs
        for(int r =0; r<board.numOfRows();r++){
            for(int c =0; c<board.numOfColumns();c++){
                if(board.isEmpty(c,r) ){
                    canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),(((height-margin)-(cirDiameter/2))+((r+1)*cirDiameter) + ((r+1)*discPadding))-topOfBoard,cirDiameter /2,emptyDisc);
                }
                else if(board.playerAt(c,r) == p1){
                    canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),(((height-margin)-(cirDiameter/2))+((r+1)*cirDiameter) + ((r+1)*discPadding))-topOfBoard,cirDiameter /2,p1Disc); //color of disc
                    canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),(((height-margin)-(cirDiameter/2))+((r+1)*cirDiameter) + ((r+1)*discPadding))-topOfBoard,cirDiameter /2,outlineDisc); //outline of disc
                }
                else{
                    canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),(((height-margin)-(cirDiameter/2))+((r+1)*cirDiameter) + ((r+1)*discPadding))-topOfBoard,cirDiameter /2,p2Disc);
                    canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),(((height-margin)-(cirDiameter/2))+((r+1)*cirDiameter) + ((r+1)*discPadding))-topOfBoard,cirDiameter /2,outlineDisc); //outline of disc
                }
            }
        }
    }


    //Draw discs for users to click ontop of the board to drop disc
    public void drawDisc(Canvas canvas){
        for (int c = 0; c < board.numOfColumns(); c++) {
            if(curr == p1 ){
                canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),topOfBoard - discPadding-(cirDiameter),(cirDiameter/2),p1Disc);
                canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),topOfBoard - discPadding-(cirDiameter),(cirDiameter/2),outlineDisc);
            }
            else{
                canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),topOfBoard - discPadding-(cirDiameter),(cirDiameter/2),p2Disc);
                canvas.drawCircle((margin+cirDiameter/2)+(c*cirDiameter) +((c+1)*discPadding),topOfBoard - discPadding-(cirDiameter),(cirDiameter/2),outlineDisc);
            }
        }
    }

    //Draws the Connect Four logo
    public void drawTopLogo(Canvas canvas){
        canvas.drawBitmap(lo.getBitmap(), null, new RectF(40, 60, width-100, height/5), null);
    }


    //When, Invalidate() is called, this gets re-drawn to updated model
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        drawTopLogo(canvas);

        drawBoard(canvas); //game board and slots filled/unfilled
        drawDisc(canvas); //clickable circles to drop disc's

        if(gameOver){
            drawBackEnd(canvas);
            if(fullB){
                drawWonBy(canvas, null);
            }
            else{
                drawWonBy(canvas, curr);
                drawWinDisc(canvas);
            }
            drawPlayAgain(canvas);
        }
    }
}
