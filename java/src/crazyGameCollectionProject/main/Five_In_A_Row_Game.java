package main;

import java.io.IOException;
import java.util.*;

public class Five_In_A_Row_Game extends db {
    static Scanner sc = new Scanner(System.in);
    static int size = 16;
    static String[][]board = new String[size][size];
    static  String lightRed = "\033[91m";
    static String lightBlue = "\033[94m";
    static String reset = "\033[0m";
    static  String nowPlayer = lightRed + "X"+reset;
    static int x = 0;
    static int y = 0;
    static boolean realFlag = true;
    static String player1;
    static String player2;
    public static void omogGame() throws IOException {
        boardspace();
        login(true);
        printBoard();
       //점수 초기화
        AnyPang_Game.totalScore[1] = 0;
        realFlag = true;
       while(realFlag){

//
           inputPlayer();
           printBoard();
           checkWinner();
           if(!realFlag){
               break;
           }
           changePlayer();
       }

    }

    public static void inputPlayer() {
        boolean flag = false;
        while(!flag) {
            if(nowPlayer.equals(lightRed + "X"+reset)) {
                System.out.println("플레이어 : " +player1+ nowPlayer + "의 차례입니다.");
            }else{
                System.out.println("플레이어 : " +player2+ nowPlayer + "의 차례입니다.");
            }

            System.out.println("좌표를 입력하세요 (예: 3 1)");
            System.out.print("행 : ");
            x = sc.nextInt();
            System.out.print("열 : ");
            y = sc.nextInt();
            if (x < 0 || y <0 || x > size || y > size) {
                System.out.println("범위를 벗어났습니다. 다시입력하세요");
                continue;
            }else if(!board[x][y].equals("  ")){
                System.out.println("중복 되었습니다 다시 입력하세요");
                continue;
            }
            else {
                board[x][y] = nowPlayer;
                flag = true;
            }

        }
    }

    public static void changePlayer(){

        if(nowPlayer.equals(lightRed + "X"+reset)){ //플레이어 변경
            nowPlayer = lightBlue + "O"+reset;
        }   else{
            nowPlayer = lightRed + "X"+reset;
        }
    }

    public static void boardspace() {   //board-로 채우기
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "  ";
            }
        }
    }

    public static void printBoard() {
        System.out.println("\n\033[93m     0    1    2    3    4    5     6    7    8    9    10   11   12   13   14   15  "+reset);
        System.out.println("  ----------------------------------------------------------------------------------");

        for (int i = 0; i < size  ; i++) {
            if(i<10) {
                System.out.print("\033[93m" + i + reset + " ");  // 각 행 번호 출력
            }else{
                System.out.print("\033[93m" + i + reset );
            }
            for (int j = 0; j < size; j++) {
                System.out.print(" | " + board[i][j]);  // 각 칸의 값 출력
            }
            System.out.println(" |");
            System.out.println("  ----------------------------------------------------------------------------------");
        }
    }
    public static void checkWinner() throws IOException{

        int rowRightCnt = 1;
        int rowLeftCnt = 1;
        int colDownCnt = 1;
        int colUpCnt = 1;
        int topRight = 1;
        int topLeft = 1;
        int bottomRight = 1;
        int bottomLeft = 1;

        // 오른쪽 방향 체크
        for (int k = y + 1; k < size && board[x][k].equals(nowPlayer); k++) {
            rowRightCnt++;
        }

        // 왼쪽 방향 체크
        for (int k = y - 1; k >= 0 && board[x][k].equals(nowPlayer); k--) {
            rowLeftCnt++;
        }

        // 아래 방향 체크
        for (int k = x + 1; k < size && board[k][y].equals(nowPlayer); k++) {
            colDownCnt++;
        }

        // 위 방향 체크
        for (int k = x - 1; k >= 0 && board[k][y].equals(nowPlayer); k--) {
            colUpCnt++;
        }

        // 대각선 오른쪽 아래 방향 체크
        for (int i = x + 1, j = y + 1; i < size && j < size && board[i][j].equals(nowPlayer); i++, j++) {
            bottomRight++;
        }

        // 대각선 왼쪽 위 방향 체크
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && board[i][j].equals(nowPlayer); i--, j--) {
            topLeft++;
        }

        // 대각선 왼쪽 아래 방향 체크
        for (int i = x + 1, j = y - 1; i < size && j >= 0 && board[i][j].equals(nowPlayer); i++, j--) {
            bottomLeft++;
        }

        // 대각선 오른쪽 위 방향 체크
        for (int i = x - 1, j = y + 1; i >= 0 && j < size && board[i][j].equals(nowPlayer); i--, j++) {
            topRight++;
        }


        if (rowRightCnt + rowLeftCnt - 1 >= 5 || colDownCnt + colUpCnt - 1 >= 5 ||
                topRight + bottomLeft - 1 >= 5 || topLeft + bottomRight - 1 >= 5) {
            String border = "";
            int borderLength = 40;
            for (int i = 0; i < borderLength; i++) {
                border += "★";
            }
            System.out.println("\n" + border);
            System.out.println("\033[1;33m" + "          🏆 축하합니다! 🏆          " + "\033[0m");
           if(nowPlayer.equals(lightRed + "X" + reset)) {
               System.out.println("\033[1;32m" + "       🎉 승자는: " +player1+ nowPlayer + " 🎉" + "\033[0m");
               AnyPang_Game.totalScore[1] += 4;
               updateScore(2,player1,true);
           }else{
               System.out.println("\033[1;32m" + "       🎉 승자는: " +player2+ nowPlayer + " 🎉" + "\033[0m");
               AnyPang_Game.totalScore[1] += 4;
               updateScore(2,player2,true);
           }

           System.out.println("\033[1;34m" + "        게임이 종료되었습니다!        " + "\033[0m");
           System.out.println(border);
            realFlag = false; // 게임 종료
        }
    }
}
