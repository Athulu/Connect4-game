package nikodem.connect4;

import sac.State;
import sac.StateFunction;

public class Heuristic extends StateFunction {

    @Override
    public double calculate(State state) {
        Connect4 c = (Connect4) state;
        byte[][] board = c.getBoard();
        double[] bonusForMiddle = new double[Connect4.m];
        bonusForMiddle = getMiddlePrices(bonusForMiddle);

        int znak;
        if(c.isMaximizingTurnNow()) znak = Connect4.X;
        else znak = Connect4.O;

        double sumX = 0.0;
        double sumO = 0.0;

        //sprawdzenie sufitu
        for (int i = 0; i < Connect4.m; i++) {
            if (board[0][i] == Connect4.X) {
                return Double.NEGATIVE_INFINITY;
            } else if (board[0][i] == Connect4.O) {
                return Double.POSITIVE_INFINITY;
            }
        }

        //premia za środek
        for(int i = 0; i < Connect4.n; i++){
            for(int j = 0; j < Connect4.m; j++){
                if(board[i][j] == Connect4.X) sumX += bonusForMiddle[j];
                else if(board[i][j] == Connect4.O) sumO += bonusForMiddle[j];
            }
        }

        // bonusy za powielone pionowo
        int strikeX = 0;
        int strikeO = 0;
        for(int i = 0; i < Connect4.m; i++){
            for(int j = 0; j < Connect4.n; j++){
                if(board[j][i] == Connect4.X)
                    strikeX++;
                else strikeX = 0;
                sumX += checkCombo(strikeX);

                if(board[j][i] == Connect4.O)
                    strikeO++;
                else strikeO = 0;
                sumO += checkCombo(strikeO);
            }
        }

        //bonusy za powielone poziomo
        strikeX = 0;
        strikeO = 0;
        for(int i = 0; i < Connect4.n; i++){
            for(int j = 0; j < Connect4.m; j++){
                if(board[i][j] == Connect4.X)
                    strikeX++;
                else strikeX = 0;
                sumX += checkCombo(strikeX);

                if(board[i][j] == Connect4.O)
                    strikeO++;
                else strikeO = 0;
                sumO += checkCombo(strikeO);
            }
        }

        for(int i = 0; i < Connect4.n-3; i++){
            for(int j = 0; j < Connect4.m; j++){
                if(board[i][j] == znak && board[i+1][j] == znak && board[i+2][j] == znak && board[i+3][j] == znak){
                    if(c.isMaximizingTurnNow()) return Double.NEGATIVE_INFINITY;
                    else return Double.POSITIVE_INFINITY;
                }
            }
        }

        // 4 w wierszach
        for(int i = 0; i < Connect4.n; i++){
            for(int j = 0; j < Connect4.m-3; j++){
                if(board[i][j] == znak && board[i][j+1] == znak && board[i][j+2] == znak && board[i][j+3] == znak){
                    if(c.isMaximizingTurnNow()) return Double.NEGATIVE_INFINITY;
                    else return Double.POSITIVE_INFINITY;
                }
            }
        }

        // 4 w kolumnach
        for(int i = 0; i < Connect4.n-3; i++){
            for(int j = 0; j < Connect4.m; j++){
                if(board[i][j] == znak && board[i+1][j] == znak && board[i+2][j] == znak && board[i+3][j] == znak){
                    if(c.isMaximizingTurnNow()) return Double.NEGATIVE_INFINITY;
                    else return Double.POSITIVE_INFINITY;
                }
            }
        }

        // 4 na lewo ukos
        for(int i = 3; i < board.length; i++){
            for(int j = 0; j < board[0].length - 3; j++){
                if (board[i][j] == znak && board[i-1][j+1] == znak && board[i-2][j+2] == znak && board[i-3][j+3] == znak){
                    if(c.isMaximizingTurnNow()) return Double.NEGATIVE_INFINITY;
                    else return Double.POSITIVE_INFINITY;
                }
            }
        }

        // 4 na prawo ukos
        for(int i = 0; i < board.length - 3; i++){
            for(int j = 0; j < board[0].length - 3; j++){
                if (board[i][j] == znak && board[i+1][j+1] == znak && board[i+2][j+2] == znak && board[i+3][j+3] == znak){
                    if(c.isMaximizingTurnNow()) return Double.NEGATIVE_INFINITY;
                    else return Double.POSITIVE_INFINITY;
                }
            }
        }

        return sumO - sumX;
    }

    private double checkCombo(int combo) {
        if (combo == 1) return 1.0;
        else if (combo == 2) return 1.0;
        else if (combo == 3) return 100.0;
        else if (combo == 4) return Double.NEGATIVE_INFINITY;
        else return 0.0;
    }


    private static double[] getMiddlePrices(double[] midPrices) {
        double maxValue = 1.0;
        midPrices[0] = 0.0;
        midPrices[Connect4.m - 1] = 0.0;
        if (Connect4.m % 2 == 0) {
            for (int i = 0; i < (Connect4.m / 2) - 1; i++) {
                midPrices[(Connect4.m / 2) + i] = maxValue / (i + 1);
                midPrices[(Connect4.m / 2) - 1 - i] = maxValue / (i + 1);
            }
        } else {
            midPrices[Connect4.m / 2] = 1.0;
            for (int i = 0; i < (Connect4.m / 2) - 1; i++) {
                midPrices[(Connect4.m / 2) - i - 1] = maxValue / (i + 2);
                midPrices[(Connect4.m / 2) + i + 1] = maxValue / (i + 2);
            }
        }
        return midPrices;
    }

}