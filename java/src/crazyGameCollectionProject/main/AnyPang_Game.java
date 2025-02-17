package main;

import java.io.IOException;
import java.util.*;



public  class AnyPang_Game extends Five_In_A_Row_Game {
    static int[][] a = new int[7][7];
    static int[][] second = new int[a.length][a[0].length];
    static int[][] third = new int[a.length][a[0].length];
    static Scanner in = new Scanner(System.in);
    static Random rand = new Random();
    static boolean stop = false;
    static int flag = 0;
    static String reset = "\033[0m"; //색생리셋
    static String [] image = {"  ","🐸","🐶","🦄","🦁","🐯","🐰","🐍","🐗","🐛","🍄","🔥"};
    static int[] totalScore = new int[3];  // 각 게임별로 점수를 배열로 만들어둠
    //0번쨰 애니팡 1번째 오목
    //상속 플레그
    static boolean gameStartFlag = false;

    public static void main(String[] args) throws IOException{

        while (true) {
            if (openMenuflag) {
                if (login(false)) {
                    openMenuflag = false;
                    continue;
                }
                openMenuflag = false;
            }
            int trueflag = openMenu();
            if(trueflag== 1) {
                threadGO();
                gameStart();
                updateScore(trueflag, loginId, false);
            }else if (trueflag == 9) {
                break;
            }

        }
    }
    public static void fillBoard() {
        int chose = rand.nextInt(6)+1;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                int randNum = rand.nextInt(11)+1;
                a[i][j] =randNum;
            }
        }
    }

    public static void gameStart() throws IOException {
         boolean gameFFlag = true;
         totalScore[0] = 0;
         isTimeout = false;
         gameStartFlag = false;
          while(!isTimeout){

            if(!gameStartFlag ) {
                fillBoard();
                copyArray(1);
                prtTool();
                gameStartFlag = true;
            }
            copyArray(1);
            System.out.println("아이템 사용하겠습니까? yes or no");

            String input = inputProcessString();
            if (input == null) {
                threadOut = true;
                break;
            }
            if (input.equals("yes")) {
                System.out.println("아이템 목록 : (1)"+"💣"+ "폭탄 "+"(2)"+"✝️"+"십자가 (3)"+"❤️" +"일심동체 ");
                int gameItemnum = inputProcessInt();
                if (gameItemnum == 1004) {
                    break;
                }
                if (item(gameItemnum) == 1004) {
                    break;
                }
                prtTool();
                downZero();
                fillZero();
                checkDuplicate();
                downZero();
                fillZero();
                prtTool();
                checkDuplicate();

            }

            if (changeCoordintae() == 1004) {  //좌표 바꾸기
                break;
            }
            checkDuplicate();
            downZero();
            fillZero();
            prtTool();
            copyArray(2);

            if (endGame() == 1) {
                System.out.println("총점수는 : " + totalScore[0] + "입니다!!");
                    break;
            }
          }
          threadOut = true;

    }


    //------------------------------------------------------------------------------------------


    public static void checkDuplicate() {
        boolean flag = true;
        while (flag) {

            int j = 0;
            for (int i = 0; i < a.length; ) {
                int len = 1;

                while (j < a[i].length - 1 && second[i][j] == second[i][j + 1]) {
                    len++;
                    j++;
                }
                if (len >= 3) {
                    for (int k = 0; k < len; k++) {
                        second[i][j - len + k + 1] = 0;
                        totalScore[0]+=4;//전체 애니팡 점수
                    }
                } else {
                    j++;
                }
                if (j == a[0].length) {
                    i++;
                    j = 0;
                }

            }
            flag = false;
        }

        boolean haha = true;

        while (haha) {
            int i = 0;
            for (int j = 0; j < a[0].length; ) {
                int len = 1;
                while (i < a.length - 1 && second[i][j] == second[i + 1][j]) {
                    len++;
                    i++;
                }
                if (len >= 3) {
                    for (int k = 0; k < len; k++) {
                        second[i - len + k + 1][j] = 0;
                        totalScore[0]+=4;//전체 애니팡 점수
                    }
                } else {
                    i++;
                }
                if (i == a.length) {
                    j++;
                    i = 0;
                }
            }
            haha = false;
        }
    }

    public static void downZero() {
        for (int i = second.length - 1; i >= 1; i--) {
            for (int j = 0; j < second[0].length; j++) {
                if (second[i][j] == 0) {
                    int ii = i;
                    for (int k = i - 1; k >= 0; k--) {
                        if (second[k][j] != 0) {
                            second[ii][j] = second[k][j];
                            second[k][j] = 0;
                            ii--;
                        }
                    }

                }
            }
        }
    }

    public static void prtTool() {

        System.out.println("\n\033[93m           0    1    2    3    4    5    6  "+reset);
        System.out.println("         -----------------------------------");

        for (int i = 0; i < second.length  ; i++) {
            System.out.print("       \033[93m"+i+reset );  // 각 행 번호 출력
            for (int j = 0; j < second.length; j++) {
                System.out.print(" | ");  // 각 칸의 값 출력
                System.out.print(returnImage(i,j));
            }

            System.out.println(" |");
            System.out.println("         -----------------------------------");
        }
    }

    public static String returnImage(int i , int j){
        int index = second[i][j];
        if (index >= 0 && index < image.length) {
            return image[index];
        }
        return "-";
    }


    public static void copyArray(int x) {
        if (x == 1){
            for (int i = 0; i < second.length; i++) {
                for (int j = 0; j < second[0].length; j++) {
                    second[i][j] = a[i][j];
                }
            }
        }else{
            for (int i = 0; i < second[0].length; i++) {                 //배열 복사
                for (int j = 0; j < second[0].length; j++) {
                    third[i][j] = second[i][j];
                }
            }


            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    a[i][j] = third[i][j];
                }
            }

        }
    }

    public static int endGame() {
        System.out.println("종료(0) / 계속(1)");
        int choice = inputProcessInt();
        if (choice == 1004)
            return 1;
        if (choice == 0) {               //게임수동 종료
            return 1;
        }
        return 0;
    }
    public static int changeCoordintae() {
        System.out.println();
        System.out.println("좌표를 변경하시겠습니까? 예(0) 아니오(1)");
        int choice = AnyPang_Game.inputProcessInt();
        if (choice == 1004)
            return 1004;
        int temp = 0;
        if(choice == 0){

            System.out.println("변경할 좌표를 입력하세요");
            String coordinate = inputProcessString();
            if (coordinate == null)
                return 1004;
            String[] x_and_Y = coordinate.split(",");
            int x1 = Integer.parseInt(x_and_Y[0]);
            int y1 = Integer.parseInt(x_and_Y[1]);
            System.out.println("이동할 좌표를 입력하세요");
            String moveCoordinate = inputProcessString();
            if (moveCoordinate == null)
                return 1004;
            String[] changeX_and_Y = moveCoordinate.split(",");
            int x2 = Integer.parseInt(changeX_and_Y[0]);
            int y2 = Integer.parseInt(changeX_and_Y[1]);
            temp = second[x1][y1];
            second[x1][y1] = second[x2][y2];
            second[x2][y2] = temp;

        }
        return 0;
    }
    public static void endGameCnt(){
        for(int i = 0 ; i < second.length ; i++){
            int endGameCnt1 = 0;
            for(int j = 0 ; j < second[0].length ; j++){
                if(second[j][i] == 0){
                    endGameCnt1++;
                }
            }
            if(endGameCnt1==0){
                System.out.println("게임 실패 ");
                stop = true;
            }
        }
    }
    public static void fillZero(){

        for(int i = 0 ; i < second.length ; i++){
            for(int j = 0 ; j < second[0].length ; j++){
                int rrr = rand.nextInt(11)+1;
                if(second[i][j] == 0){
                    second[i][j] = rrr;
                }
            }
        }
    }
}






