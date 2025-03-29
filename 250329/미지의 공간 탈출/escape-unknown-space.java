import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

// Python의 namedtuple과 유사한 역할을 하는 간단한 클래스
class Pair {
    final int y;
    final int x;

    Pair(int y, int x) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return "(" + y + ", " + x + ")";
    }

    // 필요시 equals 및 hashCode 구현
}

class Machine {
    final int y;
    final int x;
    final int plane;

    Machine(int y, int x, int plane) {
        this.y = y;
        this.x = x;
        this.plane = plane;
    }

    @Override
    public String toString() {
        return "(" + y + ", " + x + ") | plane: " + plane;
    }
}

class Anomaly {
    final int y;
    final int x;
    final int dir;
    final int v;

    Anomaly(int y, int x, int dir, int v) {
        this.y = y;
        this.x = x;
        this.dir = dir;
        this.v = v;
    }

    @Override
    public String toString() {
        return "(" + y + ", " + x + ") | dir: " + dir + " | v: " + v;
    }

    // diffuse 메서드를 Main 클래스 내 static 메서드로 두거나,
    // board, dy, dx, N 등을 이 클래스에 전달해야 함.
    // 여기서는 Main 클래스의 static 메서드를 호출한다고 가정.
    public void diffuse(int turn) {
        Main.diffuseAnomaly(this, turn);
    }
}

public class Main {

    static int N, M, F;
    static int[][] board; // 미지의 공간 보드
    static int[][] tBoard; // 시간의 벽 내부 보드
    static int[][] dist1; // 미지의 공간 방문 거리 (BFS용)
    static int[][] dist2; // 시간의 벽 내부 방문 거리 (BFS용)
    static Machine machine; // 현재 타임머신 위치/상태
    static Pair end; // 최종 목적지 (4)
    static Pair EXIT; // 시간의 벽 출구 (미지의 공간 좌표)
    static Pair wall_start; // 시간의 벽 좌상단 (미지의 공간 좌표)
    static List<Anomaly> anomalies;
    static Queue<Machine> q; // BFS용 큐

    static final int INF = -1;
    static final int ANORMAL = 2; // 이상현상 값 (Python 코드 기준)
    static final Pair NO_PAIR = new Pair(-1, -1);

    // dy, dx: 상하좌우 이동 (Python 코드 순서와 동일하게)
    static final int[] dy = {0, 0, 1, -1}; // 동, 서, 남, 북 순서에 대응하는 y 변화량 (Python 코드 기준)
    static final int[] dx = {1, -1, 0, 0}; // 동, 서, 남, 북 순서에 대응하는 x 변화량 (Python 코드 기준)


    // 보드 출력 함수
    public static void printBoard(int[][] b) {
        int rows = b.length;
        int cols = b[0].length;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                System.out.printf("%3d", b[y][x]);
            }
            System.out.println();
        }
    }

    // Python rotateLeft/Right 와 동일한 기능 (회전 변환)
    public static Pair rotateLeft(int y, int x) {
        return new Pair(M - 1 - x, y);
    }

    public static Pair rotateRight(int y, int x) {
        return new Pair(x, M - 1 - y);
    }

    // 시간의 벽 각 면 초기화
    public static void init(int plane, int[][] target, int[][] source) {
        if (plane == 4) { // 윗면
            for (int y = 0; y < M; y++) {
                for (int x = 0; x < M; x++) {
                    int ty = y + M;
                    int tx = x + M;
                    target[ty][tx] = source[y][x];
                    if (target[ty][tx] == 2) { // 기계 초기 위치 설정
                        machine = new Machine(ty, tx, getPlane(ty, tx));
                    }
                }
            }
        } else if (plane == 0) { // 동
            for (int y = 0; y < M; y++) {
                for (int x = 0; x < M; x++) {
                    Pair rotated = rotateLeft(y, x);
                    int ty = rotated.y + M;
                    int tx = rotated.x + 2 * M;
                    target[ty][tx] = source[y][x];
                }
            }
        } else if (plane == 1) { // 서
             for (int y = 0; y < M; y++) {
                for (int x = 0; x < M; x++) {
                    Pair rotated = rotateRight(y,x);
                    int ty = rotated.y + M;
                    int tx = rotated.x; // x + 0
                    target[ty][tx] = source[y][x];
                }
            }
        } else if (plane == 2) { // 남
             for (int y = 0; y < M; y++) {
                for (int x = 0; x < M; x++) {
                    int ty = y + 2 * M;
                    int tx = x + M;
                    target[ty][tx] = source[y][x];
                 }
            }
        } else if (plane == 3) { // 북
             for (int y = 0; y < M; y++) {
                for (int x = 0; x < M; x++) {
                    Pair rotated1 = rotateRight(y,x);
                    Pair rotated2 = rotateRight(rotated1.y, rotated1.x);
                    int ty = rotated2.y; // y + 0
                    int tx = rotated2.x + M;
                    target[ty][tx] = source[y][x];
                 }
            }
        }
    }

     // Anomaly 클래스의 diffuse 메서드 실제 구현 (static)
    public static void diffuseAnomaly(Anomaly anomaly, int turn) {
         if (anomaly.v == 0) return; // 속도가 0이면 확산 불가
         if (turn % anomaly.v == 0) {
            int dist = turn / anomaly.v;
            int ny = anomaly.y + dy[anomaly.dir] * dist;
            int nx = anomaly.x + dx[anomaly.dir] * dist;

            // OOB 체크는 board 기준
            if (OOB(ny, nx, 0, N, 0, N) || board[ny][nx] == 1 || board[ny][nx] == 4) { // 벽(1), 목적지(4)
                return;
            }
            // 빈 공간(0)일 경우 ANORMAL(2)로 변경
            if (board[ny][nx] == 0) {
                board[ny][nx] = ANORMAL;
            }
        }
    }

    // Out Of Bounds 체크
    public static boolean OOB(int y, int x, int minY, int maxY, int minX, int maxX) {
        return y < minY || y >= maxY || x < minX || x >= maxX;
    }

    // 미지의 공간에서 출구(0) 찾기 BFS (Python findExitFrom)
    public static Pair findExitFrom(int startY, int startX) {
        // dist1 배열 초기화 (매번 새로 탐색하므로)
        dist1 = new int[N][N];
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                dist1[i][j] = INF;
            }
        }

        Queue<Pair> bfsQ = new ArrayDeque<>();
        bfsQ.offer(new Pair(startY, startX));
        dist1[startY][startX] = 0;
        Pair ret = NO_PAIR;

        while (!bfsQ.isEmpty()) {
            Pair cur = bfsQ.poll();

            if (board[cur.y][cur.x] == 0) { // 출구(빈 공간) 찾음
                ret = new Pair(cur.y, cur.x);
                break;
            }

            for (int dir = 0; dir < 4; dir++) {
                int ny = cur.y + dy[dir];
                int nx = cur.x + dx[dir];

                // OOB(미지의 공간), 벽(1), 이미 방문(INF 아님) 체크
                if (OOB(ny, nx, 0, N, 0, N) || board[ny][nx] == 1 || dist1[ny][nx] != INF) {
                    continue;
                }

                dist1[ny][nx] = dist1[cur.y][cur.x] + 1;
                bfsQ.offer(new Pair(ny, nx));
            }
        }
        return ret;
    }

    // 미지의 공간에서 시작점(3)을 찾아 findExitFrom 호출 (Python findExit)
    // 반환: Pair[0] = 출구 좌표, Pair[1] = 시작점 좌표. 못 찾으면 null 반환
    public static Pair[] findExit() {
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (board[y][x] == 3) {
                    Pair exitPos = findExitFrom(y, x);
                    // findExitFrom이 출구를 못 찾을 수도 있음 (NO_PAIR 반환 시)
                    // 그래도 시작점(3)은 찾았으므로 반환은 해야 함.
                    return new Pair[]{exitPos, new Pair(y, x)};
                }
            }
        }
        return null; // 시작점(3)을 찾지 못함
    }

    // 모든 이상현상 확산 (Python diffuseAll)
    public static void diffuseAll(int turn) {
        for (Anomaly anomaly : anomalies) {
            anomaly.diffuse(turn);
        }
    }

    // 좌표가 시간의 벽 어느 면에 속하는지 반환 (Python getPlane)
    public static int getPlane(int y, int x) {
        // 동 (0)
        if (!OOB(y, x, M, 2 * M, 2 * M, 3 * M)) return 0;
        // 서 (1)
        if (!OOB(y, x, M, 2 * M, 0, M)) return 1;
        // 남 (2)
        if (!OOB(y, x, 2 * M, 3 * M, M, 2 * M)) return 2;
        // 북 (3)
        if (!OOB(y, x, 0, M, M, 2 * M)) return 3;
        // 윗면 (4)
        if (!OOB(y, x, M, 2 * M, M, 2 * M)) return 4;
        // 시간의 벽 외부 경계 체크 (미지의 공간 진입 직전)
        if (OOB(y, x, 0, 3 * M, 0, 3 * M)) return 5; // 미지의 공간 영역 (Python 코드 기준)

        return -1; // 시간의 벽 내부지만, 정의된 면이 아님 (경계선 등)
    }

     // 회전 변환 로직 (Python rotate)
     // 반환: int[] {ny, nx, nPlane}
    public static int[] rotate(int y, int x, int plane, int dir, Pair cStart, Pair nStart) {
         int nDir = -1, y1 = -1, x1 = -1, y2, x2, ny, nx;
         Pair rotated;

         y -= cStart.y; // 상대 좌표로 변환
         x -= cStart.x;

         switch (plane) {
            case 0: // 동
                nDir = 1; // 서쪽으로 나감
                 if (dir == 2) rotated = rotateRight(y,x); // 남쪽에서 왔으면 우회전
                 else if (dir == 3) rotated = rotateLeft(y,x); // 북쪽에서 왔으면 좌회전
                 else rotated = new Pair(y, x); // 동/서에서 오면 회전 없음 (이동 불가 로직은 getNxtPos에 있음)
                 y1 = rotated.y; x1 = rotated.x;
                 break;
            case 1: // 서
                 nDir = 0; // 동쪽으로 나감
                 if (dir == 2) rotated = rotateLeft(y,x); // 남쪽에서 왔으면 좌회전
                 else if (dir == 3) rotated = rotateRight(y,x); // 북쪽에서 왔으면 우회전
                 else rotated = new Pair(y, x);
                 y1 = rotated.y; x1 = rotated.x;
                 break;
            case 2: // 남
                 nDir = 3; // 북쪽으로 나감
                 if (dir == 0) rotated = rotateLeft(y,x); // 동쪽에서 왔으면 좌회전
                 else if (dir == 1) rotated = rotateRight(y,x); // 서쪽에서 왔으면 우회전
                 else rotated = new Pair(y, x);
                 y1 = rotated.y; x1 = rotated.x;
                 break;
             case 3: // 북
                 nDir = 2; // 남쪽으로 나감
                 if (dir == 0) rotated = rotateRight(y,x); // 동쪽에서 왔으면 우회전
                 else if (dir == 1) rotated = rotateLeft(y,x); // 서쪽에서 왔으면 좌회전
                 else rotated = new Pair(y, x);
                 y1 = rotated.y; x1 = rotated.x;
                 break;
             default: // 다른 면에서는 호출되지 않아야 함
                 return new int[]{-1, -1, -1};
         }

         y2 = y1 + nStart.y; // 새 면의 절대 좌표로 변환
         x2 = x1 + nStart.x;

         // 회전 후 해당 방향으로 한 칸 이동
         ny = y2 + dy[nDir];
         nx = x2 + dx[nDir];

         return new int[]{ny, nx, getPlane(ny, nx)};
     }

    // 현재 위치(y, x)의 면(plane)의 시작점 좌표 반환 (Python getStartPoint)
    public static Pair getStartPoint(int y, int x) {
        // 이 함수는 tBoard 좌표계 기준
        return new Pair((y / M) * M, (x / M) * M);
    }

    // 다음 위치 계산 (Python getNxtPos)
    // 반환: int[] {ny, nx, nPlane}
    public static int[] getNxtPos(int y, int x, int plane, int dir) {
        if (plane < 5) { // 시간의 벽 내부
            int ny = y + dy[dir];
            int nx = x + dx[dir];
            int nPlane = getPlane(ny, nx);

            if (nPlane != -1) { // 다음 위치가 유효한 면(0~4) 또는 미지의 공간(5)
                return new int[]{ny, nx, nPlane};
            } else { // 면 경계를 넘어 다른 면으로 이동 (회전 필요)
                Pair cStart = getStartPoint(y, x);
                // ny, nx는 경계를 넘은 좌표이므로, 이 좌표 기준으로 다음 면의 시작점 계산
                Pair nStart = getStartPoint(ny, nx);
                return rotate(y, x, plane, dir, cStart, nStart);
            }
        } else { // 미지의 공간 (plane == 5)
            int ny = y + dy[dir];
            int nx = x + dx[dir];
            // 미지의 공간에서는 plane이 바뀌지 않음
            return new int[]{ny, nx, plane};
        }
    }

    // 시간의 벽 외부 경계 좌표를 미지의 공간 좌표로 매핑 (Python mapping)
    public static Pair mapping(int y, int x) {
        // 이 함수는 tBoard 좌표계의 y, x 를 받아서 board 좌표계로 변환
        int ny = y % M + wall_start.y;
        int nx = x % M + wall_start.x;

        // 경계 처리 (Python 코드 로직 반영)
        if (y == 3 * M) ny = wall_start.y + M;
        if (y == -1) ny = wall_start.y - 1;
        if (x == 3 * M) nx = wall_start.x + M;
        if (x == -1) nx = wall_start.x - 1;

        return new Pair(ny, nx);
    }

    // 타임머신 이동 BFS (Python move)
    public static boolean move() {
        int size = q.size();
        if (size == 0) return false; // 큐가 비었으면 이동 불가

        boolean canMove = false;
        for (int i = 0; i < size; i++) {
            Machine cur = q.poll();
            machine = cur; // 현재 머신 위치 업데이트 (Python global machine = cur)

            for (int dir = 0; dir < 4; dir++) {
                int[] nextState = getNxtPos(cur.y, cur.x, cur.plane, dir);
                int ny = nextState[0];
                int nx = nextState[1];
                int nPlane = nextState[2];

                if (cur.plane != 5) { // 현재: 시간의 벽 안쪽
                    if (nPlane == 5) { // 다음: 미지의 공간
                        Pair mappedPos = mapping(ny, nx); // 미지의 공간 좌표로 변환
                        int nny = mappedPos.y;
                        int nnx = mappedPos.x;

                        // OOB(미지 공간), 방문 여부(dist1), 벽(1)/이상현상(2) 체크
                        if (OOB(nny, nnx, 0, N, 0, N) || dist1[nny][nnx] != INF || board[nny][nnx] == 1 || board[nny][nnx] == ANORMAL) {
                            continue;
                        }

                        q.offer(new Machine(nny, nnx, 5));
                        dist1[nny][nnx] = dist2[cur.y][cur.x] + 1; // 이전 거리(dist2) + 1
                        canMove = true;

                    } else if (nPlane != -1) { // 다음: 시간의 벽 다른 안쪽
                        // 벽(tBoard==1), 방문 여부(dist2) 체크
                        if (tBoard[ny][nx] == 1 || dist2[ny][nx] != INF) {
                            continue;
                        }
                        q.offer(new Machine(ny, nx, nPlane));
                        dist2[ny][nx] = dist2[cur.y][cur.x] + 1;
                        canMove = true;
                    }
                    // nPlane == -1 인 경우는 getNxtPos에서 처리됨 (회전)

                } else { // 현재: 미지의 공간 (cur.plane == 5)
                    // 다음 위치도 미지의 공간이어야 함 (getNxtPos 구조상)
                    // OOB(미지 공간), 방문 여부(dist1), 벽(1)/이상현상(2) 체크
                    if (OOB(ny, nx, 0, N, 0, N) || dist1[ny][nx] != INF || board[ny][nx] == 1 || board[ny][nx] == ANORMAL) {
                        continue;
                    }
                    q.offer(new Machine(ny, nx, 5));
                    dist1[ny][nx] = dist1[cur.y][cur.x] + 1; // 이전 거리(dist1) + 1
                    canMove = true;
                }
            }
        }
        return canMove;
    }

    // 종료 조건 확인 (Python finish)
    public static boolean finish() {
        if (end == null) return false; // 목적지가 없으면 종료 불가
        return dist1[end.y][end.x] != INF; // 목적지 도달 여부
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        F = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        dist1 = new int[N][N]; // 초기화는 findExitFrom 또는 필요한 시점에
        dist2 = new int[3 * M][3 * M];
        tBoard = new int[3 * M][3 * M];
        anomalies = new ArrayList<>();
        q = new ArrayDeque<>(); // 머신 이동 BFS 큐

        // 미지의 공간(board) 정보 입력 및 end 위치 찾기
        for (int y = 0; y < N; y++) {
            st = new StringTokenizer(br.readLine());
            for (int x = 0; x < N; x++) {
                board[y][x] = Integer.parseInt(st.nextToken());
                if (board[y][x] == 4) {
                    end = new Pair(y, x);
                }
            }
        }

        // 시작점(3) 찾기 및 출구(0) 찾기
        Pair[] exitInfo = findExit();
        if (exitInfo == null) { // 시작점(3) 없음
             System.out.println(-1);
             return;
        }
        EXIT = exitInfo[0];       // 출구 위치 (미지의 공간 좌표)
        wall_start = exitInfo[1]; // 시작점 위치 (미지의 공간 좌표, wall_start로 사용)

        if (EXIT == NO_PAIR) { // 시작점(3)은 찾았으나 거기서 출구(0)를 못찾음
            System.out.println(-1);
            return;
        }

        // dist1은 findExitFrom 내부에서 초기화되었으므로 여기선 불필요
        // dist2 초기화
        for(int i=0; i<3*M; i++) {
            for(int j=0; j<3*M; j++) {
                dist2[i][j] = INF;
            }
        }


        // 시간의 벽(tBoard) 정보 입력 및 초기화
        for (int plane = 0; plane < 5; plane++) {
            int[][] tmp = new int[M][M];
            for (int y = 0; y < M; y++) {
                st = new StringTokenizer(br.readLine());
                for (int x = 0; x < M; x++) {
                    tmp[y][x] = Integer.parseInt(st.nextToken());
                }
            }
            init(plane, tBoard, tmp);
        }

        // 이상현상 정보 입력
        for (int i = 0; i < F; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            anomalies.add(new Anomaly(y, x, dir, v));
            board[y][x] = ANORMAL; // 이상현상 위치 표시
        }


        // BFS 시작 설정
        if (machine == null) { // init에서 machine 위치(2)를 못찾은 경우 (입력 오류 등)
             System.out.println(-1);
             return;
        }
        q.offer(machine);
        dist2[machine.y][machine.x] = 0;

        int turn = 0;
        while (!finish()) {
            turn++;

            // 1. 이상현상 확산
            diffuseAll(turn);

            // 2. 출구(EXIT)가 이상현상에 막혔는지 체크
            if (board[EXIT.y][EXIT.x] == ANORMAL) {
                 System.out.println(-1);
                 return;
            }

            // 3. 타임머신 이동
            boolean canMove = move();
            if (!canMove && !finish()) { // 이동 불가하고 아직 도착도 못했으면
                System.out.println(-1);
                return;
            }
            // 큐가 비었는데 도착 못 한 경우도 여기에 포함됨

             // 시간 초과 방지 등 안전장치 필요 시 추가 (예: turn > MAX_TURN)
             if (turn > N * N + (3*M)*(3*M)) { // 대략적인 최대 턴 수 (조정 필요)
                 System.out.println(-1);
                 return;
             }
        }

        // 종료 조건 만족 시 최종 거리 출력
        System.out.println(dist1[end.y][end.x]);

    }
}