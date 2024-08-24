import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
    // 상수 값 정의
    private static final int INF = 1987654321;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        int[][] dp = new int[n + 1][m + 1];
        Pair[][] path = new Pair[n + 1][m + 1];
        int[][] curBest = new int[n + 1][m + 1];

        a.add(0); // 0번 인덱스를 위한 더미 데이터 추가
        b.add(0); // 0번 인덱스를 위한 더미 데이터 추가

        // 두 배열의 요소를 입력받습니다.
        for (int i = 1; i <= n; i++) a.add(sc.nextInt());
        for (int i = 1; i <= m; i++) b.add(sc.nextInt());

        // 배열을 뒤집습니다.
        Collections.reverse(a.subList(1, n + 1));
        Collections.reverse(b.subList(1, m + 1));

        // 초기화
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                curBest[i][j] = INF;
                path[i][j] = new Pair(0, 0);
            }
        }
        curBest[0][0] = 0;

        // 최장 공통 부분 수열을 찾기 위한 동적 프로그래밍을 수행합니다.
        // dp[i][j], cur_best[i][j], path[i][j]의 정의는 C++ 코드의 주석을 참조
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 각 단계에서 최적의 해를 찾습니다.
                if (dp[i - 1][j] > dp[i][j] || (dp[i - 1][j] == dp[i][j] && curBest[i - 1][j] < curBest[i][j])) {
                    dp[i][j] = dp[i - 1][j];
                    path[i][j] = new Pair(i - 1, j);
                    curBest[i][j] = curBest[i - 1][j];
                }

                if (dp[i][j - 1] > dp[i][j] || (dp[i][j - 1] == dp[i][j] && curBest[i][j - 1] < curBest[i][j])) {
                    dp[i][j] = dp[i][j - 1];
                    path[i][j] = new Pair(i, j - 1);
                    curBest[i][j] = curBest[i][j - 1];
                }

                if (a.get(i).equals(b.get(j)) && (dp[i - 1][j - 1] + 1 > dp[i][j] || (dp[i - 1][j - 1] + 1 == dp[i][j] && a.get(i) < curBest[i][j]))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    path[i][j] = new Pair(i - 1, j - 1);
                    curBest[i][j] = a.get(i);
                }
            }
        }

        // 최장 공통 부분 수열을 추적합니다.
        ArrayList<Integer> lcs = new ArrayList<>();
        for (int i = n, j = m; i > 0 && j > 0;) {
            if (path[i][j].equals(new Pair(i - 1, j - 1)) && a.get(i).equals(b.get(j))) {
                lcs.add(a.get(i));
                i--; j--;
            } else {
                Pair p = path[i][j];
                i = p.x;
                j = p.y;
            }
        }

        // 최장 공통 부분 수열을 출력합니다.
        for (int i = 0; i < lcs.size(); i++) {
            System.out.print(lcs.get(i) + " ");
        }
    }

    // 좌표를 저장하기 위한 간단한 Pair 클래스
    static class Pair {
        int x, y;
        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }
    }
}