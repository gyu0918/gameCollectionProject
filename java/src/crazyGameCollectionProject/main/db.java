package crazyGameCollectionProject.main;


import java.io.*;
import java.util.*;

import static  crazyGameCollectionProject.main.CardMatchingGame.cardMatchingGame;
import static crazyGameCollectionProject.main.ChessGame.chessGame;

public class db {
    static Scanner in = new Scanner(System.in);

    static Random rand = new Random();
    static File file = new File("d:\\example\\writeFile.txt");   //window 용


//    static File file = new File("/Users/junggkim/Desktop/myfile.txt");   //mac용

    static int[][] a = new int[7][7];
    static int[][] parentSecond = new int[a.length][a[0].length];
    static int[][] third = new int[a.length][a[0].length];
    //회원 아이디별 필요 배열들
    static String[] id = new String[10];
    static String[] password = new String[10];
    static int[] coin = new int[10];
    static int[][] itemInfo = new int[10][4];
    //로그인되어있는 id를 멤버 변수로
    static String loginId;
    //오목게임이나 체스 게임 같이 둘이서 같이 하는거떄문에 멤버 변수 선언
    static String gameId2;

    //아이템별 구매할떄 필요한 코인 개수
    static int[] numberOfCoins = {3,4,5};

    static boolean stop = false;
    static int flag = 0;
    static int index = 0;  //로그인시 필요한 정보 인덱스 접근을 위해 선언
    static boolean openMenuflag = true;

    //Thread 부분
    static boolean isTimeout  = false;
    static boolean threadOut = false;


    //쓰레드 부분
    public static void threadGO(){
        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                threadOut = false ;
                try {
                    int i = 0;
                    while (!threadOut){
                        Thread.sleep(400); // 0.4초 대기
                        i += 10;
                        if (i == 1000)
                            break ;
                    }
                    isTimeout = true; // 타임아웃 설정
                    System.out.println("타이머가 종료되었습니다.");
                } catch (InterruptedException ignored) {}
            }
        });
        System.out.println("제한시간은 40초 입니다. 화이팅!!!!!!!!!!");
        timerThread.start();
    }

    public static String inputProcessString(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        while (!isTimeout) {
            try {
                if (reader.ready()) {
                    input = reader.readLine();
                    break;
                }
            } catch (IOException ignored) {}
        }
        return input;
    }
    public static int inputProcessInt(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int result = 1004;
        while (!isTimeout) {
            try {
                if (reader.ready()) {
                    result = Integer.parseInt(reader.readLine());
                    break;
                }
            } catch (IOException ignored) {}
        }
        return result;
    }
    //txt파일에서 회원 아이디 찾는 메서드
    public static boolean searchIdOrPassword(String id,String pwd,boolean flag) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        //아이디만 찾을경우 flag = true  비번을 찾을경우 flag = false

        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(id)) {
                if (!flag){
                    if (part[1].equals(pwd)){
                        br.close();
                        return true;
                    }
                }
                br.close();
                return false;
            }
        }
        br.close();
        return true;
    }
    public static boolean login(boolean opponentIdCheck) throws IOException {
        //파일 존재 여부 체크 및 생성
        if (!file.exists()){
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));  //append모드

        int cntPwd = 0;
        //로그인 부터
        //id를 멤버 변수로
        String name;
        if (opponentIdCheck) {
            while(true){
                System.out.println("상대방 아이디를 입력하세요.");
                name = in.nextLine();
                if (name.equals(loginId)) {
                    System.out.println(" 로그인된 아이디와 같은 아이디 입니다.");
                    continue;
                }
                gameId2 = name;
                Five_In_A_Row_Game.player2 = gameId2;
                break ;
            }


        }else{
            System.out.println("아이디를 입력하세요.");
            name = in.nextLine();
            loginId = name;
            Five_In_A_Row_Game.player1 = loginId;
        }

        //텍스트 파일에 아이디,비밀번호,애니펑점수,오목점수,체스게임점수,코인개수,폭탄아이템개수,십자가아이템개수,선택숫자삭제아이템개수
        //id 검색 + 회원가입부분
        if (searchIdOrPassword(name,null,true)) {
            System.out.println("아이디가 없네요 해당 아이디로 회원가입을 시작하겠습니다. 비번을 입력하세요.");
            String password = in.nextLine();
            bw.write(name + "," + password + "," + "0,0,0,0,1,1,1\n");
            System.out.println("id = " + name + " password = " + password);
            bw.close();
        }else{ //로그인 부분
            while(true){
                System.out.println("비번을 입력하세요.");
                String password = in.nextLine();
                if(searchIdOrPassword(name,password,false)){
                    break ;
                }else{
                    System.out.println("비번이 틀렸습니다 다시 입력해주세요.");
                    cntPwd++;
                    if (cntPwd >= 4) {
                        System.out.println("비밀번호 4번이상 틀렸습니다 처음부터 다시 시작하세요.");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //텍스트 파일에 0아이디,1비밀번호,2뿌요점수,3오목점수,4추가게임점수,5코인개수,6폭탄아이템개수,7십자가아이템개수,8선택숫자삭제아이템개수
    public static void ptrCoin() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(loginId)) {
                System.out.println("코인 개수 -> " + part[5] + "개 입니다.");
                break ;
            }
        }
        br.close();
    }
    public static void ptrItemCnt() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(loginId)) {
                System.out.println("아이템 개수 -> " + "폭탄 : " + part[6] + "개 " + " 십자가 : " + part[7] + "개 "+ " 선택숫자제거 : " + part[8] + "개 ");
            }
        }
        br.close();
    }
    public static void purchaseItem() throws IOException{
        //코인 개수 찾기
        BufferedReader br = new BufferedReader(new FileReader(file));
//        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));  //append모드
        StringBuilder sb = new StringBuilder();

        String str;
        int coinCnt = 0;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(loginId)) {
                System.out.println("현재 코인 개수는 -> " + part[5] + " 개 입니다.");
                coinCnt = Integer.parseInt(part[5]);
                break ;
            }
        }


        System.out.println("아이템별 구매시 필요한 코인 개수 입니다.구매하고싶은 아이템 숫자를 입력하고 개수를 입력하세요");
        System.out.println("1번 = 폭탄 : " + numberOfCoins[0] + " 2번 = 십자가 : " + numberOfCoins[1] + " 3번 = 선택숫자모두제거 : " + numberOfCoins[2]);
        while(true) {
            System.out.println("아이템 번호를 적으세요.");
            int itemNum = in.nextInt();
            //예외 처리
            if (itemNum < numberOfCoins[0] && itemNum > numberOfCoins[2]) {
                System.out.println("아이템 번호를 잘못 입력 하셨습니다. 다시 입력해주세요.");
                continue;
            }
            System.out.println("수량 을 적으세요");
            int quantity = in.nextInt();
            //코인개수 판별
            if (quantity * numberOfCoins[itemNum - 1] > coinCnt) {
                System.out.println("코인개수가 모자랍니다. 처음부터 다시 하세요.");
                System.out.println("더이상 살수 없다면 종료 하시겠습니가? yes = 1 no = 0");
                int temp = in.nextInt();
                if (temp == 1) {
                    break ;
                }
                continue;
            }
            coinCnt -= quantity * numberOfCoins[itemNum - 1];

            //코인개수, 아이템 개수 메모장에 넣기
            //텍스트 파일에 0아이디,1비밀번호,2애니펑점수,3오목점수,4추가게임점수,5코인개수,6폭탄아이템개수,7십자가아이템개수,8선택숫자삭제아이템개수
            // 파일의 내용을 다시 읽어와서 수정된 부분만 변경
            br.close(); // BufferedReader 종료
            br = new BufferedReader(new FileReader(file));
            boolean isModified = false;
            while ((str = br.readLine()) != null) {
                String[] part = str.split(",");
                if (part[0].equals(loginId)) {
                    // 아이템 개수 수정
                    int itemPlusCnt = Integer.parseInt(part[itemNum + 5]);
                    itemPlusCnt += quantity;
                    part[itemNum + 5] = String.valueOf(itemPlusCnt);
                    sb.append(loginId).append(",")
                            .append(part[1]).append(",")
                            .append(part[2]).append(",")
                            .append(part[3]).append(",")
                            .append(part[4]).append(",")
                            .append(coinCnt).append(",")
                            .append(part[6]).append(",")
                            .append(part[7]).append(",")
                            .append(part[8]).append("\n");
                    isModified = true;

                } else {  //login이 아닌 아이디 정보들은 그대로 개행문자만 붙여주고 복사한다.
                    sb.append(str).append("\n");
                }
            }
            br.close();

            if (isModified) {
                // 수정된 내용을 파일에 덮어쓰기
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(sb.toString());
                bw.close();
            }

            //종료 조건
            break ;
        }
    }
    public static void updateScore(int gameNum, String updateId, boolean oppenentCheck) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));

        //멤버 변수에 있는 total[] 게임의 점수를 보고 갱신해야될지 안해야될지 판단!

        //갱신해야된다면 br bw 이용해서 갱신!
        //gameNum 1 : 애니팡 2: 오목
        //텍스트 파일에 0아이디,1비밀번호,2애니팡점수,3오목점수,4추가게임점수,5코인개수,6폭탄아이템개수,7십자가아이템개수,8선택숫자삭제아이템개수
        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(updateId)) {

                // 아이템 개수 수정
                int score = Integer.parseInt(part[gameNum + 1]);
                //아이템 개수 체크해서 사용못하도록
                if (oppenentCheck){
                    AnyPang_Game.totalScore[gameNum - 1] += score;
                }else{
                    if (AnyPang_Game.totalScore[gameNum - 1] <= score){
                        br.close();
                        return  ;
                    }
                }

                part[gameNum + 1] = String.valueOf(AnyPang_Game.totalScore[gameNum - 1]);
                System.out.println(part[gameNum + 1]);
                sb.append(updateId).append(",")
                        .append(part[1]).append(",")
                        .append(part[2]).append(",")
                        .append(part[3]).append(",")
                        .append(part[4]).append(",")
                        .append(part[5]).append(",")
                        .append(part[6]).append(",")
                        .append(part[7]).append(",")
                        .append(part[8]).append("\n");
            } else {  //해당 로그인아이디가 아닐경우 이렇게 한줄을 읽어봐 그대로 sb에 넣어준다.
                sb.append(str).append("\n");
            }
        }

        br.close();
        // 수정된 내용을 파일에 덮어쓰기
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(sb.toString());
        bw.close();
    }
    public static void defindRanking(int rankingNum) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        //트리맵에다가 원하는 값들을 넣는다.
        TreeMap<Integer, Set<String>> board = new TreeMap<>(Collections.reverseOrder());

        //값들을 넣어준다. TreeMap에다가
        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            int score = Integer.parseInt(part[rankingNum + 1]);
            // 점수를 추가할 때, 점수가 이미 있으면 그 값에 이름을 추가
            board.putIfAbsent(score, new HashSet<>());  // 점수가 없으면 Set 생성
            board.get(score).add(part[0]);  // 해당 점수에 이름 추가
        }
        br.close();

        int i = 1;
        for (Map.Entry<Integer, Set<String>> entry : board.entrySet()) {
            System.out.println(i + " 등 = " + entry.getValue() + "  " + entry.getKey() + " 점");
            if (i++ == 10)
                break;
        }

    }
    public static void ptrRanking() throws  IOException{
        StringBuilder sb = new StringBuilder();

        System.out.println("각 게임의 점수 랭킹을 확일할려면 번호를 입력하세요.");
        System.out.println("1번: 애니팡 게임 랭킹   2번 : 오목 게임 랭킹");
        int rankingNum = in.nextInt();
        //디비를 검색해
        //여기는 애니팡 게임
        //텍스트 파일에 0아이디,1비밀번호,2뿌요점수,3오목점수,4추가게임점수,5코인개수,6폭탄아이템개수,7십자가아이템개수,8선택숫자삭제아이템개수
        if (rankingNum == 1) {
            defindRanking(rankingNum);

        }else if (rankingNum == 2){ //오목 게임
            defindRanking(rankingNum);
        }else{
            System.out.println("숫자를 잘못 입력하셨습니다. ");
        }

    }
    public static int openMenu() throws IOException {
        //이부분에서 점수가 갱신되어 db에 저장된다.


        System.out.println("실행하고 싶은 기능을 숫자로 고르세요");
        System.out.println("1번 = 애니팡 게임 스타트 | 2번 = 오목 게임 스타트 | 3번 = 체스 게임 스타트 | 4번 = 카드 매칭 게임 스타트 " +
                "| 5번 = 코인개수보기 | 6번 = 아이템개수확인 | 7번 = 아이템 구매 | 8번 = 랭킹보기 | 9번 = 전체종료");
        int menuNum = in.nextInt();
        if (menuNum == 1){
            return 1;
        }else if (menuNum == 2){
            in.nextLine();
            Five_In_A_Row_Game.omogGame();
        }else if (menuNum == 3){
            chessGame();
        }else if (menuNum == 4){
            cardMatchingGame();
        }else if (menuNum == 5){
            ptrCoin();
        }else if (menuNum == 6){
            ptrItemCnt();

        }else if (menuNum == 7){
            purchaseItem();

        }else if (menuNum == 8) {
            ptrRanking();

        }else if (menuNum == 9) {
            return 9;
        }
        return 0;
    }

    public static boolean updateItem(int choiceItem) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));

        //choiceItem 1 2 3 만 들어옴
        //텍스트 파일에 0아이디,1비밀번호,2뿌요점수,3오목점수,4추가게임점수,5코인개수,6폭탄아이템개수,7십자가아이템개수,8선택숫자삭제아이템개수
        String str;
        while ((str = br.readLine()) != null) {
            String[] part = str.split(",");
            if (part[0].equals(loginId)) {
                // 아이템 개수 수정
                int itemCnt = Integer.parseInt(part[choiceItem + 5]);
                //아이템 개수 체크해서 사용못하도록
                if (itemCnt == 0){
                    br.close();
                    return true ;
                }
                itemCnt -= 1;
                part[choiceItem + 5] = String.valueOf(itemCnt);
                sb.append(loginId).append(",")
                        .append(part[1]).append(",")
                        .append(part[2]).append(",")
                        .append(part[3]).append(",")
                        .append(part[4]).append(",")
                        .append(part[5]).append(",")
                        .append(part[6]).append(",")
                        .append(part[7]).append(",")
                        .append(part[8]).append("\n");

            } else {  //이 부분이 없다면 새로 변경되고 나머지 아이디는 모두 지워진다.
                sb.append(str).append("\n");
            }
        }

        br.close();
        // 수정된 내용을 파일에 덮어쓰기
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(sb.toString());
        bw.close();
        return false;
    }
    public static int item(int choiceItem) throws IOException {

        int temp = choiceItem;
        // 회원별 아이템이 얼마나 남아있는지 에 따라서 밑에 아이템 기능들이 작동하도록 해야한다.
        while(true){
            if (updateItem(choiceItem)){
                System.out.println("해당 아이템은 사용할수 있는 개수가 없습니다. 아이템 숫자를 다시 적어주세요 아이템을 사용하기 싫다면 999을 적으세요");
                choiceItem = inputProcessInt();
                if (choiceItem == 1004)
                    return 1004;
                System.out.println(choiceItem + "------------------------");
                if (choiceItem == 999)
                    return 0;

            }else{
                break ;
            }
        }
        // 폭탄 : 해당범위 숫자 0으로 제거 (랜덤범위로)
        if (temp == 1){
            //예외 처리

            int col = rand.nextInt(7);
            int row = rand.nextInt(7);
            int mark = 0;
            for (int i = 0; i < parentSecond.length; i++) {
                for (int j = 0; j < parentSecond[i].length; j++) {
                    if ((j > col - 2 && j < col + 2) && (i > row  - 2 && i < row + 2)) {
                        // 색깔 표시를 위해서
                        AnyPang_Game.second[i][j] = mark;
                    }
                }
            }
            // 지맘대로 십자가 : 가로 세로 한줄 전체를 랜덤한 위치에 모두 0으로 만들어준다.
        }else if (temp == 2){
            int col = rand.nextInt(7);
            int row = rand.nextInt(7);
            for (int i = 0; i < parentSecond.length; i++) {
                for (int j = 0; j < parentSecond[i].length; j++) {
                    if (j == col || i == row) {
                        AnyPang_Game.second[i][j] = 0;
                    }
                }
            }
            // 일심동체 :선택한 숫자만 모두 없애는 기능
        }else if (temp == 3){
            System.out.println("삭제하고 싶은 캐릭터를 고르세요");
            System.out.println("(1)"+"🐸"+" (2)"+"🐶"+" (3)"+"🦄"+" (4)"+"🦁"+" (5)"+"🐯"+" (6)"+"🐰"+" (7)"+"🐍"+" (8)"+"🐗"+" (9)"+"🐛"+" (10)"+"🍄"+" (11)"+"🔥");
            int deleteNum = in.nextInt();
            if (deleteNum < 12 && deleteNum > 0){
                for (int i = 0; i  <  parentSecond.length; i++) {
                    for (int j = 0; j < parentSecond[i].length; j++) {
                        if(AnyPang_Game.second[i][j] == deleteNum){
                            AnyPang_Game.second[i][j] = 0;
                        }
                    }
                }
            }
        }
        return 0;
    }

}
