import java.util.LinkedList;
import java.util.List;

public class ReversiGame {
    private Board board;
    private int [][] boardArr;

    private static final int GRAY = 0;
    private static final int RED = 1;
    private static final int BLUE = 2;
    private boolean helpIsOn = false;

    private static List<Pair<Integer, Integer>> pairs = new LinkedList<>(){
        {
            add(new Pair<>(1,0));
            add(new Pair<>(0,1));
            add(new Pair<>(1,1));
            add(new Pair<>(-1, -1));
            add(new Pair<>(-1, 0));
            add(new Pair<>(0 , -1));
            add(new Pair<>(-1, 1));
            add(new Pair<>(1, -1));
        }
    };

    private int turn;
    public ReversiGame(){
        this(10);
    }

    public ReversiGame(int length){
        boardArr = new int[length][length];
        board = new Board(boardArr);
        init();
    }

    public void buttonClick(int x){
        if (isLegal()) {
            int row, col;
            col = board.getCellCol();
            row = board.getCellRow();
            List<Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>> toPaint = toPaint();
            for (Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> p : toPaint){
                paint(p);
            }
            updateCounters();
            board.updateBoard(boardArr);
            switchTurn();
            if(helpIsOn){
                board.updateHelp(getHelp());
            }
        }
    }

    private void switchTurn(){
        turn = (turn == 1) ? 2 : 1;
        if(searchLegalMoves().size() == 0){
            turn = (turn == 1) ? 2 : 1;
        }
        if(searchLegalMoves().size() == 0){
            updateCounters();
            board.whoWins();
        }
        board.setTurn(turn);
    }

    private void updateCounters(){
        Pair<Integer, Integer> p = countColors();
        board.setReds(p.left);
        board.setBlues(p.right);
        board.updateLabels();
    }
    private boolean isLegal(){
        return isLegal(board.getCellRow(), board.getCellCol());
    }
    private boolean isLegal(int row, int col){
        return toPaint(row,col).size() != 0;
    }

    private List<Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>> toPaint(){
        return toPaint(board.getCellRow(), board.getCellCol());
    }

    private List<Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>> toPaint(int row, int col){
        if (boardArr[row][col] != GRAY) return new LinkedList<>();
        List<Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>> res = new LinkedList<>();
        for (Pair<Integer, Integer> p : pairs){
            if(check(p.left, p.right, row, col)){
                res.add(new Pair<>(p ,findLegalMove(p.left, p.right, row, col)));
            }
        }
        return res;
    }

    private Pair<Integer, Integer> findLegalMove(int moveRow, int moveCol){
        return findLegalMove(moveRow, moveCol, board.getCellRow(), board.getCellCol());
    }

    private Pair<Integer, Integer> findLegalMove(int moveRow, int moveCol, int row, int col){
        int counter = 0;
        if (boardArr[row][col] != GRAY) return null;
        for (int i = row + moveRow, j = col + moveCol;
             i >= 0 && i < boardArr.length && j >= 0 && j < boardArr[0].length;
             i += moveRow, j+= moveCol){
            if(boardArr[i][j] == GRAY){
                return null;
            }
            else if(boardArr[i][j] == turn){
                return (counter != 0) ? new Pair<>(i, j) : null;
            }
            else{
                counter++;
            }
        }
        return null;
    }
    private boolean check(int moveRow, int moveCol){
        return check(moveRow, moveCol, board.getCellRow(), board.getCellCol());
    }

    private boolean check(int moveRow, int moveCol, int row, int col){
        return findLegalMove(moveRow, moveCol, row, col) != null;
    }

    private void paint(Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> p){
        int col = board.getCellCol(), row = board.getCellRow();
        for(int i = row , j = col;i != p.right.left || j != p.right.right;i += p.left.left, j+= p.left.right){
            boardArr[i][j] = turn;
        }
        boardArr[p.right.left][p.right.right] = turn;
    }
    private void init(){
        for (int row=0;row<boardArr.length;row++){
            for (int col=0;col<boardArr[0].length;col++){
                boardArr[row][col] = GRAY;
            }
        }
        boardArr[boardArr.length/2-1][boardArr.length/2] = RED;
        boardArr[boardArr.length/2][boardArr.length/2-1] = RED;
        boardArr[boardArr.length/2-1][boardArr.length/2-1] = BLUE;
        boardArr[boardArr.length/2][boardArr.length/2] = BLUE;
        board.init(boardArr);
        turn = 1;
        board.dansMethod(0, x -> buttonClick(x));
        board.setHelpMovesCallback(() -> getHelp());
        board.setBoardCallback(() -> getBoardArr());
        board.setSizeChangeCallback(i -> { boardArr = new int[i][i];init(); });
    }

    public static class Pair<K, V>{
        private K left;
        private V right;

        public Pair(K left, V right){
            this.left = left;
            this.right = right;
        }

        public K getLeft(){
            return left;
        }
        public V getRight(){
            return right;
        }
    }

    private List<Pair<Integer, Integer>> getHelp(){
        helpIsOn = true;
        return searchLegalMoves();
    }

    public int[][] getBoardArr(){
        helpIsOn = false;
        return boardArr;
    }
    private List<Pair<Integer, Integer>> searchLegalMoves(){
        List<Pair<Integer, Integer>> res = new LinkedList<>();
        for (int i = 0;i < boardArr.length; i++){
            for (int j = 0;j < boardArr[0].length; j++){
                if(boardArr[i][j] == GRAY && isLegal(i, j)){
                    res.add(new Pair<>(i, j));
                }
            }
        }
        return res;
    }

    private Pair<Integer, Integer> countColors(){
        int blue = 0, red = 0;
        for (int i = 0; i< boardArr.length; i++){
            for (int j = 0;j < boardArr[0].length;j++){
                blue += (boardArr[i][j] == BLUE) ? 1 : 0;
                red += (boardArr[i][j] == RED) ? 1 : 0;
            }
        }
        return new Pair<>(red, blue);
    }

}