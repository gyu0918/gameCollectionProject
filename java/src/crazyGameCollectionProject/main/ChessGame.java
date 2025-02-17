package crazyGameCollectionProject.main;

import java.util.Scanner;

public class ChessGame {
    static String reset = "\033[0m";
    static String black = "\033[33m";//사실 노란색
    static String white = "\033[97m";
    static String[][] chessBoard = new String[8][8];
    static Scanner in = new Scanner(System.in);
    static boolean chessstop = false;
    static boolean chessplay = true;
    static int row = 0;
    static int col = 0;
    static int moveRow = 0;
    static int moveCol = 0;


    public static void chessGame() {
        resetBoard();
        printBoard();
        while (!chessstop) {
            chessplay = true;
            changeLocal();
            if (!chessplay) {
                printBoard();
                continue;
            }
            endGame();
            printBoard();
        }
    }

    // 보드 초기화
    public static void resetBoard() {
        // 체스 판 초기화 (초기 배치)
        String[] initialRow1 = {black + "♖" + reset, black + "♘" + reset, black + "♗" + reset, black + "♕" + reset, black + "♔" + reset, black + "♗" + reset, black + "♘" + reset, black + "♖" + reset};
        String[] initialRow2 = {black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset};
        String[] initialRow7 = {white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset};
        String[] initialRow8 = {white + "♖" + reset, white + "♘" + reset, white + "♗" + reset, white + "♔" + reset, white + "♕" + reset, white + "♗" + reset, white + "♘" + reset, white + "♖" + reset};

        for (int i = 0; i < 8; i++) {
            chessBoard[0][i] = initialRow1[i];
            chessBoard[1][i] = initialRow2[i];
            chessBoard[6][i] = initialRow7[i];
            chessBoard[7][i] = initialRow8[i];
        }

        // 나머지 빈 칸 초기화
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = "ㅡ";  // 빈 공간
            }
        }
    }

    // 체스판 출력
    public static void printBoard() {
        System.out.println("             0    1    2   3    4   5    6    7");
        System.out.println("          +--------------------------------------+");
        for (int i = 0; i < 8; i++) {
            System.out.print("         " + i + " | ");
            for (int j = 0; j < 8; j++) {
                System.out.print(chessBoard[i][j] + " | ");
            }
            System.out.println();
            System.out.println("          +--------------------------------------+");
        }
    }

    // 말 이동 처리
    public static void changeLocal() {
        System.out.println("이동할 말을 선택하세요.");
        System.out.print("행 : ");
        row = in.nextInt();
        System.out.print("열 : ");
        col = in.nextInt();


        if (!chessBoard[row][col].equals("ㅡ")) {
            System.out.println("이동시킬곳을 선택하세요.");
            System.out.print("행 : ");
            moveRow = in.nextInt();
            System.out.print("열 : ");
            moveCol = in.nextInt();
            boolean absolute = (moveCol >= 0 && moveCol <= 7 && moveRow >= 0 && moveRow <= 7 && row >= 0 && row <= 7 && col >= 0 && col <= 7);
            if (absolute) {
                if (chessBoard[row][col].contains("♟") || chessBoard[row][col].contains("♙")) {
                    movePawn(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains("♖") || chessBoard[row][col].contains("♖")) {
                    moveRook(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains("♘") || chessBoard[row][col].contains("♘")) {
                    moveKnight(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains("♗") || chessBoard[row][col].contains("♗")) {
                    moveBishop(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains(white + "♔" + reset) || chessBoard[row][col].contains(black + "♔" + reset)) {
                    moveQueen(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains(white + "♕") || chessBoard[row][col].contains(black + "♕")) {
                    moveKing(row, col, moveRow, moveCol);
                }
            } else {
                wrongMove();
            }
        } else {
            System.out.println("그 칸은 비어 있습니다.");
            chessplay = false;
        }
    }


    // 폰 이동
    public static void movePawn(int row, int col, int moveRow, int moveCol) {
        // 폰이 이동할 수 있는지 체크
        if (chessBoard[row][col].contains("♙")) {  // 검정 폰
            // 폰이 한 칸 또는 두 칸 전진 가능
            if (moveRow == row + 1 && moveCol == col && chessBoard[moveRow][moveCol].equals("ㅡ")) {
                // 1칸 전진
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else if (row == 1 && moveRow == row + 2 && moveCol == col && chessBoard[moveRow][moveCol].equals("ㅡ")) {
                // 첫 번째 이동에서만 2칸 전진
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else if (chessBoard[moveRow][moveCol].contains(white) && moveCol >= col - 1 && moveCol <= col + 1) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                wrongMove();
            }
        } else if (chessBoard[row][col].contains("♟")) {  // 흰색 폰
            // 검은색 폰의 이동 규칙 처리 (흰색과 동일하게)
            if (moveRow == row - 1 && moveCol == col && chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else if (row == 6 && moveRow == row - 2 && moveCol == col && chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else if (chessBoard[moveRow][moveCol].contains(black) && moveCol != col && moveCol >= col - 1 && moveCol <= col + 1) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                wrongMove();
            }
        }
    }

    public static void moveRook(int row, int col, int moveRow, int moveCol) {
        int blackCntRight = col; //black이름은 변경필요  / 오른쪽 가능길이
        int blackCntLeft = col; //왼쪽 이동가능 길이
        int blackCntDown = row; //아래 이동가능 길이
        int blackCntUp = row;//외로 이동가능길이

        if (row == moveRow && col != moveCol) { // 가로 이동
            for (int i = col + 1; i <= 7 && chessBoard[row][i].equals("ㅡ"); i++) {
                blackCntRight++;
            }
            for (int i = col - 1; i >= 0 && chessBoard[row][i].equals("ㅡ"); i--) {
                blackCntLeft--;
            }
            if (moveCol > blackCntRight + 1 || moveCol < blackCntLeft - 1) {
                wrongMove();

            } else if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                whenBlackAttack();
                whenWhiteAttack();
            }
        } else if (col == moveCol && row != moveRow) { // 세로 이동
            for (int i = row + 1; i <= 7 && chessBoard[i][col].equals("ㅡ"); i++) {
                blackCntDown++;
            }
            for (int i = row - 1; i >= 0 && chessBoard[i][col].equals("ㅡ"); i--) {
                blackCntUp--;
            }

            if (moveRow > blackCntDown + 1 || moveRow < blackCntUp - 1) {
                wrongMove();

            } else if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                whenBlackAttack();
                whenWhiteAttack();
            }
        } else {
            wrongMove();

        }

    }

    public static void moveKnight(int row, int col, int moveRow, int moveCol) {
        if ((moveRow == row + 2 || moveRow == row - 2) && (moveCol == col + 1 || moveCol == col - 1)) { //십자가 형태로 2번 이동후 양쪽으로1번 가능
            if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                whenBlackAttack();
                whenWhiteAttack();
            }
        } else if ((moveRow == row + 1 || moveRow == row - 1) && (moveCol == col + 2 || moveCol == col - 1)) {//십자가 형태로 1번 이동후 양쪽으로 2번가능
            if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                whenWhiteAttack();
                whenBlackAttack();
            }
        } else {
            wrongMove();
        }
    }

    public static void moveBishop(int row, int col, int moveRow, int moveCol) {
        if (moveRow - row == moveCol - col || row - moveRow == moveCol - col || moveRow - row == col - moveCol) { //대각선으로만 가는 조건
            int leftTop = 1;
            int rightTop = 1;
            int leftBottom = 1;
            int rightBottom = 1;
            boolean absolute = (moveCol >= 0 && moveCol <= 7 && moveRow >= 0 && moveRow <= 7 && row >= 0 && row <= 7 && col >= 0 && col <= 7);

            if (moveRow > row && moveCol > col) {
                for (int i = row + 1, j = col + 1; i >= 0 && j <= 7 && absolute && chessBoard[i][j].equals("ㅡ"); i++, j++) {
                    rightBottom++;
                }
            }
            if (moveRow < row && moveCol < col) {
                for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && absolute && chessBoard[i][j].equals("ㅡ"); i--, j--) {
                    leftTop++;
                }
            }
            if (moveRow < row && moveCol > col) {
                for (int i = row - 1, j = col + 1; i >= 0 && j <= 7 && absolute && chessBoard[i][j].equals("ㅡ"); i--, j++) {
                    rightTop++;
                }
            }
            if (moveRow > row && moveCol < col) {
                for (int i = row + 1, j = col - 1; i <= 7 && j >= 0 && absolute && chessBoard[i][j].equals("ㅡ"); i++, j--) {
                    leftBottom++;
                }
            }

            if (moveRow >= row && moveCol >= col && moveRow <= rightBottom + row && moveCol <= rightBottom + col) {
                if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else if (moveRow >= row && moveCol <= col && moveRow <= leftBottom + row && moveCol >= col - leftBottom) {
                if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else if (moveRow <= row && moveCol >= col && moveRow >= row - rightTop && moveCol <= rightTop + col) {
                if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else if (moveRow < row && moveCol < col && moveRow >= row - leftTop && moveCol >= col - leftTop) {
                if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            }

        } else {
            wrongMove();
        }
    }

    public static void moveQueen(int row, int col, int moveRow, int moveCol) {
        int blackCntRight = col; //black이름은 변경필요  / 오른쪽 가능길이
        int blackCntLeft = col; //왼쪽 이동가능 길이
        int blackCntDown = row; //아래 이동가능 길이
        int blackCntUp = row;//외로 이동가능길이

        if (chessBoard[row][col].contains(white + "♔") || chessBoard[row][col].contains(black + "♔")) {
            if (row == moveRow && col != moveCol) { // 가로 이동
                for (int i = col + 1; i <= 7 && chessBoard[row][i].equals("ㅡ"); i++) {
                    blackCntRight++;
                }
                for (int i = col - 1; i >= 0 && chessBoard[row][i].equals("ㅡ"); i--) {
                    blackCntLeft--;
                }
                if (moveCol > blackCntRight + 1 || moveCol < blackCntLeft - 1) {
                    wrongMove();

                } else if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else if (col == moveCol && row != moveRow) { // 세로 이동
                for (int i = row + 1; i <= 7 && chessBoard[i][col].equals("ㅡ"); i++) {
                    blackCntDown++;
                }
                for (int i = row - 1; i >= 0 && chessBoard[i][col].equals("ㅡ"); i--) {
                    blackCntUp--;
                }

                if (moveRow > blackCntDown + 1 || moveRow < blackCntUp - 1) {
                    wrongMove();

                } else if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else if (moveRow - row == moveCol - col || row - moveRow == moveCol - col || moveRow - row == col - moveCol) { //대각선으로만 가는 조건
                int leftTop = 1;
                int rightTop = 1;
                int leftBottom = 1;
                int rightBottom = 1;
                boolean absolute = (moveCol >= 0 && moveCol <= 7 && moveRow >= 0 && moveRow <= 7 && row >= 0 && row <= 7 && col >= 0 && col <= 7);

                if (moveRow > row && moveCol > col) {
                    for (int i = row + 1, j = col + 1; i >= 0 && j <= 7 && absolute && chessBoard[i][j].equals("ㅡ"); i++, j++) {
                        rightBottom++;
                    }
                }
                if (moveRow < row && moveCol < col) {
                    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && absolute && chessBoard[i][j].equals("ㅡ"); i--, j--) {
                        leftTop++;
                    }
                }
                if (moveRow < row && moveCol > col) {
                    for (int i = row - 1, j = col + 1; i >= 0 && j <= 7 && absolute && chessBoard[i][j].equals("ㅡ"); i--, j++) {
                        rightTop++;
                    }
                }
                if (moveRow > row && moveCol < col) {
                    for (int i = row + 1, j = col - 1; i <= 7 && j >= 0 && absolute && chessBoard[i][j].equals("ㅡ"); i++, j--) {
                        leftBottom++;
                    }
                }

                if (moveRow >= row && moveCol >= col && moveRow <= rightBottom + row && moveCol <= rightBottom + col) {
                    if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                        chessBoard[moveRow][moveCol] = chessBoard[row][col];
                        chessBoard[row][col] = "ㅡ";
                    } else {
                        whenBlackAttack();
                        whenWhiteAttack();
                    }
                } else if (moveRow >= row && moveCol <= col && moveRow <= leftBottom + row && moveCol >= col - leftBottom) {
                    if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                        chessBoard[moveRow][moveCol] = chessBoard[row][col];
                        chessBoard[row][col] = "ㅡ";
                    } else {
                        whenBlackAttack();
                        whenWhiteAttack();
                    }
                } else if (moveRow <= row && moveCol >= col && moveRow >= row - rightTop && moveCol <= rightTop + col) {
                    if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                        chessBoard[moveRow][moveCol] = chessBoard[row][col];
                        chessBoard[row][col] = "ㅡ";
                    } else {
                        whenBlackAttack();
                        whenWhiteAttack();
                    }
                } else if (moveRow < row && moveCol < col && moveRow >= row - leftTop && moveCol >= col - leftTop) {
                    if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                        chessBoard[moveRow][moveCol] = chessBoard[row][col];
                        chessBoard[row][col] = "ㅡ";
                    } else {
                        whenBlackAttack();
                        whenWhiteAttack();
                    }
                }
            }
        } else {
            wrongMove();
        }
    }
    public static void moveKing(int row , int col , int moveRow , int moveCol) {
        if(moveRow<=row+1 && moveRow>=row-1 && moveCol<=col+1 && moveCol>=col-1) {
            if(chessBoard[moveRow][moveCol] == "ㅡ"){
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            }else{
                whenBlackAttack();
                whenBlackAttack();
            }
        }else{
            wrongMove();
        }
    }
    public static void endGame(){
        int cntWhite = 0;
        int cntBlack = 0;
        for(int i = 0 ; i <=7 ; i++){
            for(int j = 0 ; j <=7 ; j++){
               if(chessBoard[i][j].equals(black + "♕" + reset)){
                    cntBlack++;
                }else if(chessBoard[i][j].equals(white + "♕" + reset)){
                   cntWhite++;
               }
            }
        }
        if(cntWhite>cntBlack){
            existWhiteKing(true);
        }else if(cntWhite<cntBlack){
            existBlackKing(true);
        }else{
            return;
        }
    }
    public static boolean existWhiteKing(boolean whiteKing){
        if(whiteKing){
            System.out.println("\033[1;33m" + "          🏆 축하합니다! 🏆          " + "\033[0m");
            System.out.println("\033[1;32m" + "       🎉 승자는: " +"흰색" + " 🎉" + "\033[0m");
            System.out.println("\033[1;34m" + "        게임이 종료되었습니다!        " + "\033[0m");
        }
        chessstop = true;
        return chessstop;
    }
    public static boolean existBlackKing(boolean blackKing){
        if(blackKing){
            System.out.println("\033[1;33m" + "          🏆 축하합니다! 🏆          " + "\033[0m");
            System.out.println("\033[1;32m" + "       🎉 승자는: " +"검은색" + " 🎉" + "\033[0m");
            System.out.println("\033[1;34m" + "        게임이 종료되었습니다!        " + "\033[0m");
        }
        chessstop = true;
        return chessstop;
    }


    public static void whenBlackAttack() {
        if (chessBoard[row][col].contains(white) && chessBoard[moveRow][moveCol].contains(black)) {
            chessBoard[moveRow][moveCol] = chessBoard[row][col];
            chessBoard[row][col] = "ㅡ";
            System.out.println("⚔⚫ 적 기물을 공격했습니다! ⚫⚔");
        }
    }

    public static void whenWhiteAttack() {
        if (chessBoard[row][col].contains(black) && chessBoard[moveRow][moveCol].contains(white)) {
            chessBoard[moveRow][moveCol] = chessBoard[row][col];
            chessBoard[row][col] = "ㅡ";
            System.out.println("⚔⚪ 적 기물을 공격했습니다! ⚪⚔");
        }
    }

    public static void wrongMove() {   //잘못된 이동일때 사용할 메서드
        System.out.println();
        System.out.println("                  ⛔✖⛔" + "잘못된 이동입니다." + "⛔✖⛔");
        System.out.println();
        chessplay = false;
    }

}
