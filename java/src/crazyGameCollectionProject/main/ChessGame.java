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
    static boolean absolute = moveCol >= 0 && moveCol <= 7 && moveRow >= 0 && moveRow <= 7 && row >= 0 && row <= 7 && col >= 0 && col <= 7;

    public static void main(String[] args) {
        resetBoard();
        printBoard();
        while (!chessstop) {
            chessplay = true;
            changeLocal();
            if (!chessplay) {
                printBoard();
                continue;
            }

            printBoard();
        }
    }

    // 보드 초기화
    public static void resetBoard() {
        // 체스 판 초기화 (초기 배치)
        String[] initialRow1 = {black + "♖" + reset, black + "♘" + reset, black + "♗" + reset, black + "♕" + reset, black + "♔" + reset, black + "♗" + reset, black + "♘" + reset, black + "♖" + reset};
        String[] initialRow2 = {black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset, black + "♙" + reset};
        String[] initialRow7 = {white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset, white + "♟" + reset};
        String[] initialRow8 = {white + "♖" + reset, white + "♘" + reset, white + "♗" + reset, white + "♕" + reset, white + "♔" + reset, white + "♗" + reset, white + "♘" + reset, white + "♖" + reset};

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
            if (absolute) {
                if (chessBoard[row][col].contains("♟") || chessBoard[row][col].contains("♙")) {
                    movePawn(row, col, moveRow, moveCol);
                } else if (chessBoard[row][col].contains("♖") || chessBoard[row][col].contains("♖")) {
                    moveRook(row, col, moveRow, moveCol);
                }else if(chessBoard[row][col].contains("♘") || chessBoard[row][col].contains("♘")){
                    moveKnight(row, col, moveRow, moveCol);
                }
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
            } else if (chessBoard[moveRow][moveCol].contains(white) && absolute && moveCol >= col - 1 && moveCol <= col + 1) {
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
            } else if (chessBoard[moveRow][moveCol].contains(black) && absolute && moveCol != col && moveCol >= col - 1 && moveCol <= col + 1) {
                chessBoard[moveRow][moveCol] = chessBoard[row][col];
                chessBoard[row][col] = "ㅡ";
            } else {
                wrongMove();
            }
        }
    }

    public static void moveRook(int row, int col, int moveRow, int moveCol) {
        int blackCntRight = col;
        int blackCntLeft = col;
        int blackCntDown = row;
        int blackCntUp = row;
        boolean attackWhite = false;

        if (chessBoard[row][col].contains(black + "♖") || chessBoard[row][col].contains(white + "♖")) { // 검정 룩
            if (row == moveRow && col != moveCol) { // 가로 이동
                for (int i = col + 1; i <= 7 && chessBoard[row][i].equals("ㅡ"); i++) {
                    blackCntRight++;
                }
                for (int i = col - 1; i >= 0 && chessBoard[row][i].equals("ㅡ"); i--) {
                    blackCntLeft--;
                }
                if (moveCol > blackCntRight + 1 || moveCol < blackCntLeft - 1) {
                    wrongMove();
                    return;
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
                    return;
                } else if (chessBoard[moveRow][moveCol].equals("ㅡ")) {
                    chessBoard[moveRow][moveCol] = chessBoard[row][col];
                    chessBoard[row][col] = "ㅡ";
                } else {
                    whenBlackAttack();
                    whenWhiteAttack();
                }
            } else {
                wrongMove();
                return;
            }
        }
    }
    public static void  moveKnight(int row, int col, int moveRow, int moveCol){

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
