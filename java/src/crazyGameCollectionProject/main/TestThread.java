package crazyGameCollectionProject.main;

import java.io.*;

public class TestThread {
    static boolean isTimeout = false; // 시간 초과 여부

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // 타이머 스레드
        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000); // 10초 대기
                    isTimeout = true; // 타임아웃 설정
                    System.out.println("\nTime limit reached. Stopping input.");
                } catch (InterruptedException ignored) {}
            }
        });

        timerThread.start();

        System.out.println("Enter numbers (Type 'done' to finish):");

        // 입력 처리 (메인 스레드에서 실행)
        while (!isTimeout) {
            try {
                if (reader.ready()) { // 입력이 있는 경우
                    String input = reader.readLine();
                    if ("done".equals(input)) break;
                    System.out.println("You entered: " + input);
                }
            } catch (IOException ignored) {}
        }

        System.out.println("Proceeding to the next step...");
    }
}
