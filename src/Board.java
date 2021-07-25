
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;
import java.util.function.IntConsumer;



import javax.swing.*;
import javax.swing.plaf.IconUIResource;

public class Board {

    private Label currentTurn = new Label();
    private int turn;
    private SizeChangeCallback sizeChangeCallback;
    private BoardCallback boardCallback;
    private HelpMovesCallback helpMovesCallback;
    private int reds;
    private int blues;
    private Label blueCount = new Label();
    private Label redCount = new Label();
    private static final int buttonsMove = 100;
    private static final int width = 50;
    private static final int height = 50;
    private static final int addedWidth = 30;
    private static final int addedHeight = 20;
    private int cellRow,cellCol;
    private JFrame frame = new JFrame();
    private JButton [][] btnArr;
    private IntConsumer clickButton;
    private Checkbox help = new Checkbox("help");
    private JComboBox<String> sizes = new JComboBox<>();
    ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int [] idx;
            if (e.getSource() instanceof JButton) {
                idx = getBtnIdx((JButton)e.getSource());
                String text = "row = "+idx[0]+" col = "+idx[1];

                cellRow = idx[0];
                cellCol = idx[1];
                clickButton.accept(0);
            }
        }
    };

    public void dansMethod(int x, IntConsumer aMethod) {

        clickButton = aMethod;
    }

    public void setHelpMovesCallback(HelpMovesCallback helpMovesCallback){
        this.helpMovesCallback = helpMovesCallback;
    }
    public void setBoardCallback(BoardCallback boardCallback){
        this.boardCallback = boardCallback;
    }
    public void setSizeChangeCallback(SizeChangeCallback sizeChangeCallback){
        this.sizeChangeCallback = sizeChangeCallback;
    }
    public void messageBox(String message,String title){
        JOptionPane.showMessageDialog(null, message,title,1);
    }
    public Board(int [][] arr){
        frame = new JFrame();//creating instance of JFrame
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to end the game?", "End Game?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

                    System.exit(0);
                }
            }
        });
        sizes.addItem("10X10");sizes.addItem("11X11");sizes.addItem("12X12");sizes.addItem("13X13");sizes.addItem("14X14");sizes.addItem("15X15");
        sizes.addItemListener(e -> sizeChangeCallback.initSize(Integer.parseInt(((String)e.getItem()).split("X")[0])));
        help.addItemListener(e -> { if(help.getState()){ updateHelp(helpMovesCallback.getHelp()); } else{ updateBoard(boardCallback.getBoard()); }});
        init(arr);
    }

    public void init(int[][] arr){
        reds = 2;blues = 2;cellRow = -1;cellCol = -1; turn = 1;
        if(btnArr != null) {
            Arrays.stream(btnArr).forEach(i -> Arrays.stream(i).forEach(b -> frame.remove(b)));
            frame.remove(sizes);
            frame.remove(help);
            help.setState(false);
            frame.remove(blueCount);
            frame.remove(redCount);
        }
        frame.setSize(arr.length*width + arr.length*addedWidth,arr[0].length*height + arr[0].length*addedHeight);//400 width and 500 height
        btnArr = new JButton[arr.length][arr[0].length];
        for (int row=0;row<btnArr.length;row++){
            for (int col=0;col<btnArr[0].length;col++){
                btnArr[row][col] = new JButton("");
                btnArr[row][col].setBounds(row*width+row, col*height+col, width,  height);
                btnArr[row][col].addActionListener(listener);
                frame.add(btnArr[row][col]);
            }
        }

        updateBoard(arr);
        currentTurn.setBounds(btnArr[arr.length-1][arr[0].length-1].getBounds().x + buttonsMove, height, width*5,  height);
        sizes.setBounds(btnArr[arr.length-1][arr[0].length-1].getBounds().x + buttonsMove, height*2, width*4,  height);
        help.setBounds(btnArr[arr.length-1][arr[0].length-1].getBounds().x + buttonsMove, height*9, width,  height-20);
        blueCount.setBounds(btnArr[arr.length-1][arr[0].length-1].getBounds().x + buttonsMove, height*4, width*2,  height);
        redCount.setBounds(btnArr[arr.length-1][arr[0].length-1].getBounds().x + buttonsMove, height*6, width*2,  height);
        updateLabels();
        currentTurn.setText(String.format("%s player turn", ColorFactory.produceColorName(turn)));
        currentTurn.setForeground(ColorFactory.produceColor(turn));
        currentTurn.setFont(Font.getFont("BOLD"));
        frame.add(help);
        frame.add(sizes);
        frame.add(blueCount);
        frame.add(redCount);
        frame.add(currentTurn);
        frame.setLayout(null);//using no layout managers
        frame.setVisible(true);//making the frame visible
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void setTurn(int turn){
        currentTurn.setText(String.format("%s player turn", ColorFactory.produceColorName(turn)));
        currentTurn.setForeground(ColorFactory.produceColor(turn));
        this.turn = turn;
    }
    private String redCountText(){
        return "RED: " + reds;
    }

    private String blueCountText(){
        return "BLUE: " + blues;
    }

    public void updateLabels(){
        blueCount.setText(blueCountText());
        blueCount.setForeground(ColorFactory.produceColor(2));
        redCount.setText(redCountText());
        redCount.setForeground(ColorFactory.produceColor(1));
    }
    public int getCellRow() {
        return cellRow;
    }
    public int getCellCol() {
        return cellCol;
    }
    public void updateBoard(int [][] arr){
        for (int row=0;row<btnArr.length;row++){
            for (int col=0;col<btnArr[0].length;col++){
                btnArr[row][col].setBackground(ColorFactory.produceColor(arr[row][col]));
            }
        }
    }

    private int[] getBtnIdx(JButton b){
        int [] ret = {-1,-1};
        for (int row=0;row<btnArr.length;row++){
            for (int col=0;col<btnArr[0].length;col++){
                if (btnArr[row][col] == b){
                    ret[0] = row;
                    ret[1] = col;
                    return ret;
                }
            }
        }
        return ret;

    }

    public void updateHelp(List<ReversiGame.Pair<Integer, Integer>> pairs){
        for (ReversiGame.Pair<Integer, Integer> p : pairs){
            btnArr[p.getLeft()][p.getRight()].setBackground(Color.LIGHT_GRAY);
        }
    }

    public void whoWins(){
        String massage = "";
        if(reds == blues){
            massage = "Tie";
        }
        else if(blues > reds){
            massage = "blue player wins";
        }
        else {
            massage = "red player wins";
        }
        String finalMassage = massage;
        messageBox(massage, "Game Ended");
        System.exit(0);
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        this.reds = reds;
    }

    public int getBlues() {
        return blues;
    }

    public void setBlues(int blues) {
        this.blues = blues;
    }
}