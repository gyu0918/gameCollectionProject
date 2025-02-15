package crazyGameCollectionProject.main;


import java.util.*;

public class MineGame {
    static  int ROWS = 8, COLS = 10, MINES = 10;
    static String[][] board = new String[ROWS][COLS];
    static int[][] landMineCounts = new int[ROWS][COLS];
    static boolean[][] landMines = new boolean[ROWS][COLS];
    static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    static  int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    static  int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    public static void main(String[] args) {
        System.out.println("지뢰찾기 게임 시작!");
        Scanner scanner = new Scanner(System.in);

        initBoard();
        placeMines();
        calculateMineCounts();

        while (true) {
            printBoard();
            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            System.out.println("\n선택할 좌표를 입력하세요. (예: a1)");
            String input = scanner.nextLine();
            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
            String action = scanner.nextLine();

            int col = input.charAt(0) - 'a';
            int row = Character.getNumericValue(input.charAt(1)) - 1;

            if (action.equals("2")) {
                board[row][col] = "⚑";
            } else if (action.equals("1")) {
                if (landMines[row][col]) {
                    board[row][col] = "☼";
                    gameStatus = -1;
                } else {
                    open(row, col);
                }
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }

            if (checkWin()) {
                gameStatus = 1;
            }
        }
    }

    public static void initBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = "□";
            }
        }
    }

    public static void placeMines() {
        Random random = new Random();
        Set<String> mineLocations = new HashSet<>();

        while (mineLocations.size() < MINES) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);
            String key = row + "," + col;

            if (!mineLocations.contains(key)) {
                mineLocations.add(key);
                landMines[row][col] = true;
            }
        }
    }

    public static void calculateMineCounts() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (!landMines[i][j]) {
                    int count = 0;
                    for (int d = 0; d < 8; d++) {
                        int ni = i + dx[d], nj = j + dy[d];
                        if (isValid(ni, nj) && landMines[ni][nj])
                            count++;
                    }
                    landMineCounts[i][j] = count;
                } else {
                    landMineCounts[i][j] = 0;
                }
            }
        }
    }

    public static void printBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int i = 0; i < ROWS; i++) {
            System.out.printf("%d  ", i + 1);
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean checkWin() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j].equals("□") && !landMines[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void open(int row, int col) {
        if (!isValid(row, col) || !board[row][col].equals("□") || landMines[row][col]) {
            return;
        }

        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
        } else {
            board[row][col] = "■";
            for (int d = 0; d < 8; d++) {
                open(row + dx[d], col + dy[d]);
            }
        }
    }

    public static boolean isValid(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
}
