import java.io.*;
import java.util.*;

public class Main {
    static class Rabbit {
        int pid;
        int d;
        int row, col;
        int totalJumps;
        int score;
        int idx;
        boolean selectedInCurrentRace;

        public Rabbit(int pid, int d, int idx) {
            this.pid = pid;
            this.d = d;
            this.row = 1;
            this.col = 1;
            this.totalJumps = 0;
            this.score = 0;
            this.idx = idx;
            this.selectedInCurrentRace = false;
        }
    }

    static int N, M, P;
    static HashMap<Integer, Rabbit> rabbitsMap = new HashMap<>();
    static List<Rabbit> rabbitsList = new ArrayList<>();
    static int[] dr = {-1, 1, 0, 0}; // 상하좌우
    static int[] dc = {0, 0, -1, 1}; // 상하좌우

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int Q = Integer.parseInt(br.readLine());

        for (int cmdCount = 0; cmdCount < Q; cmdCount++) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == 100) { // 경주 시작 준비
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                P = Integer.parseInt(st.nextToken());
                for (int i = 0; i < P; i++) {
                    int pid = Integer.parseInt(st.nextToken());
                    int d = Integer.parseInt(st.nextToken());
                    Rabbit rabbit = new Rabbit(pid, d, i);
                    rabbitsMap.put(pid, rabbit);
                    rabbitsList.add(rabbit);
                }
            } else if (cmd == 200) { // 경주 진행
                int K = Integer.parseInt(st.nextToken());
                int S = Integer.parseInt(st.nextToken());
                race(K, S);
            } else if (cmd == 300) { // 이동거리 변경
                int pid_t = Integer.parseInt(st.nextToken());
                int L = Integer.parseInt(st.nextToken());
                Rabbit rabbit = rabbitsMap.get(pid_t);
                if (rabbit != null) {
                    rabbit.d *= L;
                }
            } else if (cmd == 400) { // 최고의 토끼 선정
                int maxScore = 0;
                for (Rabbit rabbit : rabbitsList) {
                    if (rabbit.score > maxScore) {
                        maxScore = rabbit.score;
                    }
                }
                System.out.println(maxScore);
            }
        }
    }

    static void race(int K, int S) {
        // 현재 경주에서 선택된 토끼 초기화
        for (Rabbit rabbit : rabbitsList) {
            rabbit.selectedInCurrentRace = false;
        }

        for (int k = 0; k < K; k++) {
            Rabbit selectedRabbit = null;
            for (Rabbit rabbit : rabbitsList) {
                if (selectedRabbit == null || compareRabbitsForSelection(rabbit, selectedRabbit) < 0) {
                    selectedRabbit = rabbit;
                }
            }

            // 토끼 이동
            moveRabbit(selectedRabbit);

            selectedRabbit.totalJumps++;
            selectedRabbit.selectedInCurrentRace = true;

            int scoreToAdd = selectedRabbit.row + selectedRabbit.col;

            // 다른 토끼들 점수 증가
            for (Rabbit rabbit : rabbitsList) {
                if (rabbit.pid != selectedRabbit.pid) {
                    rabbit.score += scoreToAdd;
                }
            }
        }

        // 경주 후 추가 점수 부여
        Rabbit bestRabbit = null;
        for (Rabbit rabbit : rabbitsList) {
            if (rabbit.selectedInCurrentRace) {
                if (bestRabbit == null || compareRabbitsForScore(rabbit, bestRabbit) > 0) {
                    bestRabbit = rabbit;
                }
            }
        }

        if (bestRabbit != null) {
            bestRabbit.score += S;
        }
    }

    static void moveRabbit(Rabbit rabbit) {
        int maxRowColSum = -1;
        int maxRow = -1;
        int maxCol = -1;
        int selectedRow = -1;
        int selectedCol = -1;

        for (int dir = 0; dir < 4; dir++) {
            int d = rabbit.d;
            int r = rabbit.row;
            int c = rabbit.col;
            int drc = dr[dir];
            int dcc = dc[dir];

            int steps = 0;
            while (steps < d) {
                int nr = r + drc;
                int nc = c + dcc;
                if (nr < 1 || nr > N || nc < 1 || nc > M) {
                    // 방향 전환 및 한 칸 이동
                    drc = -drc;
                    dcc = -dcc;
                    nr = r + drc;
                    nc = c + dcc;
                    // 방향 전환 후에도 격자 밖인 경우 이동 중단
                    if (nr < 1 || nr > N || nc < 1 || nc > M) {
                        break;
                    }
                    r = nr;
                    c = nc;
                    steps++;
                    break;
                } else {
                    r = nr;
                    c = nc;
                    steps++;
                }
            }

            while (steps < d) {
                int nr = r + drc;
                int nc = c + dcc;
                if (nr < 1 || nr > N || nc < 1 || nc > M) {
                    // 방향 전환 및 한 칸 이동
                    drc = -drc;
                    dcc = -dcc;
                    nr = r + drc;
                    nc = c + dcc;
                    // 방향 전환 후에도 격자 밖인 경우 이동 중단
                    if (nr < 1 || nr > N || nc < 1 || nc > M) {
                        break;
                    }
                    r = nr;
                    c = nc;
                    steps++;
                } else {
                    r = nr;
                    c = nc;
                    steps++;
                }
            }

            int rowColSum = r + c;
            if (rowColSum > maxRowColSum ||
                    (rowColSum == maxRowColSum && r > maxRow) ||
                    (rowColSum == maxRowColSum && r == maxRow && c > maxCol)) {
                maxRowColSum = rowColSum;
                maxRow = r;
                maxCol = c;
                selectedRow = r;
                selectedCol = c;
            }
        }

        // 위치 업데이트
        rabbit.row = selectedRow;
        rabbit.col = selectedCol;
    }

    static int compareRabbitsForSelection(Rabbit a, Rabbit b) {
        if (a.totalJumps != b.totalJumps) {
            return Integer.compare(a.totalJumps, b.totalJumps);
        }
        int aPosSum = a.row + a.col;
        int bPosSum = b.row + b.col;
        if (aPosSum != bPosSum) {
            return Integer.compare(aPosSum, bPosSum);
        }
        if (a.row != b.row) {
            return Integer.compare(a.row, b.row);
        }
        if (a.col != b.col) {
            return Integer.compare(a.col, b.col);
        }
        return Integer.compare(a.pid, b.pid);
    }

    static int compareRabbitsForScore(Rabbit a, Rabbit b) {
        int aPosSum = a.row + a.col;
        int bPosSum = b.row + b.col;
        if (aPosSum != bPosSum) {
            return Integer.compare(aPosSum, bPosSum);
        }
        if (a.row != b.row) {
            return Integer.compare(a.row, b.row);
        }
        if (a.col != b.col) {
            return Integer.compare(a.col, b.col);
        }
        return Integer.compare(a.pid, b.pid);
    }
}