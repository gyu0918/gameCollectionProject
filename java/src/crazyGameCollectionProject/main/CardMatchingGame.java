
package crazyGameCollectionProject.main;


import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CardMatchingGame {
    private static final int SIZE = 4; // 4x4 보드 크기
    private static final int TIME_LIMIT = 60; // 제한 시간 (초)
    private static String[][] board;
    private static boolean[][] revealed;
    private static List<String> cards;
    private static Scanner scanner = new Scanner(System.in);
    private static Timer timer;
    private static int remainingTime = TIME_LIMIT;
    private static boolean timeOver = false;

    public static void cardMatchingGame() {
        initializeGame();
        startTimer();
        playGame();
    }

    // 게임 초기화
    private static void initializeGame() {
        board = new String[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        cards = new ArrayList<>();

        // 카드 쌍 생성 (A~H)
        for (char c = 'A'; c < 'I'; c++) {
            cards.add(String.valueOf(c));
            cards.add(String.valueOf(c));
        }
        Collections.shuffle(cards); // 카드 섞기

        // 배열에 카드 배치
        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = cards.get(index++);
                revealed[i][j] = false; // 초기에는 모두 가려짐
            }
        }
    }

    // 타이머 시작
    private static void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                remainingTime--;
                if (remainingTime <= 0) {
                    timeOver = true;
                    timer.cancel();
                    System.out.println("\n⏳ 시간이 초과되었습니다! Game Over ❌");
                    System.exit(0);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000); // 1초마다 실행
        //1초마다 실행
    }

    // 게임 진행
    private static void playGame() {
        int attempts = 0;
        int matchedPairs = 0;

        while (matchedPairs < (SIZE * SIZE) / 2) {
            if (timeOver) return; // 정답을 모두 맞히기 전까지 반복

            printBoard();
            System.out.println("⏳ 남은 시간: " + remainingTime + "초");
            System.out.print("카드를 선택하세요 (예: 1 2 3 4): ");

            int x1 = scanner.nextInt() - 1;
            int y1 = scanner.nextInt() - 1;
            int x2 = scanner.nextInt() - 1;
            int y2 = scanner.nextInt() - 1;

            // 입력 값 검증
            if (!isValidChoice(x1, y1, x2, y2)) {
                System.out.println("🚫 잘못된 선택입니다. 다시 입력하세요.");
                continue;
            }

            // 카드 공개
            revealed[x1][y1] = true;
            revealed[x2][y2] = true;
            printBoard();
            attempts++;

            // 짝이 맞는지 확인
            if (!board[x1][y1].equals(board[x2][y2])) {
                System.out.println("❌ 틀렸습니다! 다시 시도하세요.");
                revealed[x1][y1] = false;
                revealed[x2][y2] = false;
            } else {
                System.out.println("✅ 정답! 짝을 맞췄습니다.");
                matchedPairs++;
            }
        }

        timer.cancel(); // 게임 성공 시 타이머 정지
        System.out.println("🎉 축하합니다! " + attempts + "번 만에 모든 짝을 맞췄습니다.");
        System.exit(0);
    }

    // 유효한 선택인지 확인
    private static boolean isValidChoice(int x1, int y1, int x2, int y2) {
        return x1 >= 0 && x1 < SIZE && y1 >= 0 && y1 < SIZE &&
                x2 >= 0 && x2 < SIZE && y2 >= 0 && y2 < SIZE &&
                !(x1 == x2 && y1 == y2) &&
                !revealed[x1][y1] && !revealed[x2][y2];
    }

    // 현재 보드 상태 출력
    private static void printBoard() {
        System.out.println("\n현재 보드:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (revealed[i][j]) {
                    System.out.print(" " + board[i][j] + " ");
                } else {
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }
    }
}


