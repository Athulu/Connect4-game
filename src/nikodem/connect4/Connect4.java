package nikodem.connect4;

import sac.game.*;

import java.util.*;

public class Connect4 extends GameStateImpl {
    public static final int n = 5;
    public static final int m = 6;

    final static int X = 1;
    final static int O = 2;

    private final byte[][] board;
    private byte[] howManyInColumnPlaced;

    public Connect4() {
        this.setMaximizingTurnNow(false);
        this.board = new byte[n][m];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                this.board[i][j] = 0;

        this.howManyInColumnPlaced = new byte[m];
        for(int i = 0; i < m; i++)
            this.howManyInColumnPlaced[i] = 0;

    }


    public Connect4(Connect4 parent) {
        this.setMaximizingTurnNow(parent.isMaximizingTurnNow());
        this.board  = new byte[n][m];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.board[i][j] = parent.board[i][j];
            }
        }
        this.howManyInColumnPlaced = new byte[m];
        for(int i = 0; i < m; i++)
            this.howManyInColumnPlaced[i] = parent.howManyInColumnPlaced[i];
    }

    public static void main(String[] args) {
//        double[] board = new double[Connect4.m];
//        board = getMiddlePrices(board);
//        for(int i = 0; i < board.length; i++) System.out.println(board[i] + " ");

//        int[] tab = {1,0,0,0,1,1,1,0,0,1,1};
//        int strike = 0;
//        for(int i = 0; i < tab.length; i++){
//                if(tab[i] == Connect4.X){
//                    strike++;
//                    System.out.println(strike);
//                }
//                if(tab[i] != Connect4.X){
//                    strike = 0;
//                    System.out.println(strike);
//                }
//        }

        boolean zaczynaGracz = false;

        Scanner scanner = new Scanner(System.in);
        Connect4 game = new Connect4();
        System.out.println(game);

        Connect4.setHFunction(new Heuristic());
        GameSearchAlgorithm algorithm = new AlphaBetaPruning();
        algorithm.setInitial(game);


        int best = 0;
        if(zaczynaGracz){
            while(true){
                System.out.print("Tura gracza ");
                if(game.isMaximizingTurnNow()) System.out.println("O");
                else System.out.println("X");
                algorithm.execute();
                System.out.println(algorithm.getMovesScores());
                System.out.println("Najlepszy ruch: " + algorithm.getFirstBestMove());

                game.move(scanner.nextInt());
                System.out.println(game);
                if(game.isVictory()) break;

                algorithm.execute();
                System.out.println(algorithm.getMovesScores());
                try{
                    best = Integer.parseInt(algorithm.getFirstBestMove());
                }catch(Exception e){
                    System.out.println(e);
                    System.out.println(best);
                }

                System.out.println("Najlepszy ruch: " + algorithm.getFirstBestMove());
                game.move(best);
                System.out.println(game);
                if(game.isVictory()) break;

            }
        }else{
            while(true){
                algorithm.execute();
                System.out.println(algorithm.getMovesScores());
                best = Integer.parseInt(algorithm.getFirstBestMove());
                System.out.println("Najlepszy ruch: " + algorithm.getFirstBestMove());
                game.move(best);
                System.out.println(game);
                if(game.isVictory()) break;


                System.out.print("Tura gracza ");
                if(game.isMaximizingTurnNow()) System.out.println("O");
                else System.out.println("X");
                algorithm.execute();
                System.out.println(algorithm.getMovesScores());
                System.out.println("Najlepszy ruch: " + algorithm.getFirstBestMove());

                game.move(scanner.nextInt());
                System.out.println(game);
                if(game.isVictory()) break;
            }
        }
        System.out.print("Wygrywa ");
        if(game.isMaximizingTurnNow())
            System.out.println("MAX");
        else System.out.println("MIN");
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();
        for(int i = 0; i < m; i++){
            Connect4 child = new Connect4(this);

            if(child.move(i)) {
                children.add(child);
                child.setMoveName("" + i);
            }
        }
        return children;
    }

    public byte[][] getBoard() {
        return board;
    }

    private boolean move(int column){
        setMaximizingTurnNow(!isMaximizingTurnNow());
        if(n-1-howManyInColumnPlaced[column] < 0 || column > m) return false;
        if(isMaximizingTurnNow()) board[n-1-howManyInColumnPlaced[column]][column] = X;
        else board[n-1-howManyInColumnPlaced[column]][column] = O;

        howManyInColumnPlaced[column]++;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(board[i][j] == 0) tmp.append(".");
                else if (board[i][j] == 1) tmp.append("X");
                else if (board[i][j] == 2) tmp.append("O");
                tmp.append("\t");
            }
            tmp.append("\n");
        }
        for (int i = 0; i < m; i++) {
            tmp.append("=");
            tmp.append("\t");
        }
        tmp.append("\n");
        for (int i = 0; i < m; i++) {
            tmp.append(i);
            tmp.append("\t");
        }
        return tmp.toString();
    }

    public int hashCode() {
        byte[] flat = new byte[n*m];
        int k = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                flat[k++] = board[i][j];
            }
        }
        return Arrays.hashCode(flat);
    }

    public boolean isVictory(){
        int znak;
        if(isMaximizingTurnNow()) znak = X;
        else znak = O;

        for(int i = 0; i < m; i++){
            if(howManyInColumnPlaced[i] == n) return true;
        }

        for(int i = 0; i < Connect4.n; i++){
            for(int j = 0; j < Connect4.m-3; j++){
                if(board[i][j] == znak && board[i][j+1] == znak && board[i][j+2] == znak && board[i][j+3] == znak)
                    return true;
            }
        }

        for(int i = 0; i < Connect4.n-3; i++){
            for(int j = 0; j < Connect4.m; j++){
                if(board[i][j] == znak && board[i+1][j] == znak && board[i+2][j] == znak && board[i+3][j] == znak)
                    return true;
            }
        }


        for(int i = 3; i < board.length; i++){
            for(int j = 0; j < board[0].length - 3; j++){
                if (board[i][j] == znak   &&
                        board[i-1][j+1] == znak &&
                        board[i-2][j+2] == znak &&
                        board[i-3][j+3] == znak){
                    return true;
                }
            }
        }
        for(int i = 0; i < board.length - 3; i++){
            for(int j = 0; j < board[0].length - 3; j++){
                if (board[i][j] == znak   &&
                        board[i+1][j+1] == znak &&
                        board[i+2][j+2] == znak &&
                        board[i+3][j+3] == znak){
                    return true;
                }
            }
        }
        return false;
    }
}

//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < m; j++){
//                if(board[i][j] == znak){
//                    if(j+1 < m && board[i][j+1] == znak)
//                        if(j+2 < m && board[i][j+2] == znak)
//                            if(j+3 < m &&board[i][j+3] == znak) return true;
//                    if(i+1 < n && board[i+1][j] == znak)
//                        if(i+2 < n && board[i+2][j] == znak)
//                            if(i+3 < n && board[i+3][j] == znak) return true;
//                }
//            }
//        }