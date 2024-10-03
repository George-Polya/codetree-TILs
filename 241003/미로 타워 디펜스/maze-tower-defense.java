import java.util.Scanner;
import java.util.ArrayList;

class Pair {
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {
    public static final int DIR_NUM = 4;
    public static final int MAX_N = 25;
    
    public static int n, m;
    
    public static int[][] grid = new int[MAX_N][MAX_N];
    public static int[][] temp = new int[MAX_N][MAX_N];
    
    public static ArrayList<Pair> spiralPoints = new ArrayList<>();
    
    public static int ans;
    
    public static void searchSpiral() {
        // 나선이 돌아가는 순서대로 
        // 왼쪽 아래 오른쪽 위 방향이 되도록 정의합니다.
        int[] dx = new int[]{0, 1, 0, -1};
        int[] dy = new int[]{-1, 0, 1, 0};
    
        // 시작 위치와 방향, 
        // 해당 방향으로 이동할 횟수를 설정합니다. 
        int currX = n / 2, currY = n / 2;
        int moveDir = 0, moveNum = 1;
    
        while(currX > 0 || currY > 0) {
            // moveNum 만큼 이동합니다.
            for(int i = 0; i < moveNum; i++) {
                currX += dx[moveDir]; currY += dy[moveDir];
                spiralPoints.add(new Pair(currX, currY));
    
                // 이동하는 도중 (0, 0)으로 오게 되면,
                // 움직이는 것을 종료합니다.
                if(currX == 0 && currY == 0)
                    break;
            }
            
            // 방향을 바꿉니다.
            moveDir = (moveDir + 1) % 4;
            // 만약 현재 방향이 왼쪽 혹은 오른쪽이 된 경우에는
            // 특정 방향으로 움직여야 할 횟수를 1 증가시킵니다.
            if(moveDir == 0 || moveDir == 2)
                moveNum++;
        }
    }
    
    public static void pull() {
        // Step 1. temp 배열을 초기화합니다.
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                temp[i][j] = 0;
    
        // Step2. 나선 순서대로 보며
        //        비어있지 않은 값들을 temp에 채워줍니다.
        int tempIdx = 0;
        for(Pair gridPoint: spiralPoints) {
            int gx = gridPoint.x, gy = gridPoint.y;
            
            if(grid[gx][gy] > 0) {
                int tx = spiralPoints.get(tempIdx).x;
                int ty = spiralPoints.get(tempIdx).y;
                temp[tx][ty] = grid[gx][gy];
                tempIdx++;
            }
        }
    
        // Step 3. temp 값을 다시 grid에 옮겨줍니다.
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                grid[i][j] = temp[i][j];
    }
    
    public static void attack(int d, int p) {
        // 문제에서 주어진 순서대로 → ↓ ← ↑
        int[] dx = new int[]{0, 1,  0, -1};
        int[] dy = new int[]{1, 0, -1,  0};
    
        // Step 1. d 방향으로 p마리의 몬스터를 제거합니다.
        int centerX = n / 2, centerY = n / 2;
        for(int dist = 1; dist <= p; dist++) {
            int nx = centerX + dx[d] * dist;
            int ny = centerY + dy[d] * dist;
    
            ans += grid[nx][ny];
            grid[nx][ny] = 0; 
        }
    
        // Step2. 비어 있는 자리를 당겨서 채워줍니다.
        pull();
    }
    
    public static int getNumBySpiralIdx(int spiralIdx) {
        int x = spiralPoints.get(spiralIdx).x, y = spiralPoints.get(spiralIdx).y;
        return grid[x][y];
    }
    
    // startIdx로부터 연속하여 같은 숫자로 이루어져 있는
    // 가장 끝 index를 찾아 반환합니다. 
    public static int getEndIdxOfSameNum(int startIdx) {
        int endIdx = startIdx + 1;
        int currNum = getNumBySpiralIdx(startIdx);
        int endOfArray = spiralPoints.size();
    
        while(endIdx < endOfArray) {
            if(getNumBySpiralIdx(endIdx) == currNum)
                endIdx++;
            else
                break;
        }
    
        return endIdx - 1;
    }
    
    public static void remove(int startIdx, int endIdx) {
        for(int i = startIdx; i <= endIdx; i++) {
            int x = spiralPoints.get(i).x;
            int y = spiralPoints.get(i).y;
            ans += grid[x][y];
            grid[x][y] = 0;
        }
    }
    
    // 4번 이상 반복하여 나오는 구간을 지워줍니다.
    public static boolean bomb() {
        boolean didExplode = false;
        int currIdx = 0;
        int endOfArray = spiralPoints.size();
    
        while(currIdx < endOfArray) {
            int endIdx = getEndIdxOfSameNum(currIdx);
            int currNum = getNumBySpiralIdx(currIdx);
    
            // 맨 끝에 도달하게 되면, 더이상 진행하지 않습니다.
            if(currNum == 0)
                break;
    
            if(endIdx - currIdx + 1 >= 4) {
                // 연속한 숫자의 개수가 4개 이상이면
                // 해당 구간을 지워줍니다.
                remove(currIdx, endIdx);
                didExplode = true;
            }
    
            // 그 다음 구간의 시작값으로 변경해줍니다.
            currIdx = endIdx + 1;
        }
    
        return didExplode;
    }
    
    // 4번 이상 반복하여 나오는 구간을 계속 지워줍니다.
    public static void organize() {
        while(true) {
            // 4번 이상 나오는 구간을 터뜨려봅니다.
            boolean keepGoing = bomb();
            
            if(!keepGoing)
                break;
    
            // 지운 이후에는 다시 당겨서 채워줍니다.
            pull();
        }
    }
    
    public static void lookAndSay() {
        // Step 1. temp 배열을 초기화합니다.
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                temp[i][j] = 0;
        
        // Step2. 보고 말하며 temp에 해당 값을 기록합니다.
        int tempIdx = 0;
    
        int currIdx = 0;
        int endOfArray = spiralPoints.size();
        while(currIdx < endOfArray) {
            int endIdx = getEndIdxOfSameNum(currIdx);
    
            // 연속하여 나온 숫자의 개수와 숫자 종류 값을 계산합니다.
            int contiguousCnt = endIdx - currIdx + 1;
            int currNum = getNumBySpiralIdx(currIdx);
    
            // 맨 끝에 도달하게 되면, 더이상 진행하지 않습니다.
            if(currNum == 0)
                break;
    
            // temp에 (개수, 종류) 순서대로 기록해줍니다.
            // 만약 격자를 벗어나면 종료합니다.
            if(tempIdx >= endOfArray)
                break;
            
            int tx = spiralPoints.get(tempIdx).x;
            int ty = spiralPoints.get(tempIdx).y;
            temp[tx][ty] = contiguousCnt;
            tempIdx++;
    
            if(tempIdx >= endOfArray)
                break;
    
            tx = spiralPoints.get(tempIdx).x;
            ty = spiralPoints.get(tempIdx).y;
            temp[tx][ty] = currNum;
            tempIdx++;
    
            // 그 다음 구간의 시작값으로 변경해줍니다.
            currIdx = endIdx + 1;
        }
    
        // Step 3. temp 값을 다시 grid에 옮겨줍니다.
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                grid[i][j] = temp[i][j];
    }
    
    public static void simulate(int d, int p) {
        // Step 1. 공격하여 몬스터를 제거합니다.
        attack(d, p);
    
        // Step 2. 4번 이상 반복하여 나오는 구간을 계속 지워줍니다.
        organize();
    
        // Step 3. 보고 말하기 행동을 진행합니다.
        lookAndSay();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();

        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                grid[i][j] = sc.nextInt();

        // 중심 탑을 기준으로 나선 모양으로 회전했을 때
        // 지나게 되는 위치의 좌표들을 순서대로 기록해 놓습니다.
        searchSpiral();

        // m번에 걸쳐 시뮬레이션을 진행합니다.
        while(m-- > 0) {
            int d = sc.nextInt();
            int p = sc.nextInt();

            simulate(d, p);
        }

        System.out.print(ans);
    }
}