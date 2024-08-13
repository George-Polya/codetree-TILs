import java.util.*;
import java.io.*;

/**
 * 격자 : N x M
 * 모든 위치에는 포탑이 존재
 *
 * 모든 포탑은 공격력이 존재 -> 공격력은 상황에 따라 변함
 * 포탑의 공격력이 0 이하 => 포탑이 부서졌다. 그리고 처음부터 이렇게 부서진 포탑은 존재할 수 있어
 *
 * 한 턴에 진행되는 동작
 * 1. 공격자 선정
 *  1-1. 가장 약한 포탑을 공격자로 선정
 *  1-2. 포탑의 공격력이 N+M만큼 증가
 *
 *  가장 약한 포탑 선정 기준
 *      1. 공격력이 가장 약한 포탑
 *      2. 만약 공격력이 가장 낮은 포탑이 2개 이상이면, 가장 최근에 공격한 포탑
 *      3. 2번을 만족하는 포탑이 2개 이상이면, 각 포탑의 행과 열의 합이 가장 큰 포탑
 *      4. 3번을 만족하는 포탑이 2개 이상이면, 각 포탑의 열이 가장 큰 포탑
 * 2. 공격자가 공격
 *  2-1. 자신을 제외한 가장 강한 포탑을 공격
 *
 *  가장 강한 포탑 선정 기준
 *      1. 공격력이 가장 높은 포탑
 *      2. 만약 공격력이 가장 높은 포탑이 2개 이상이면, 가장 오래 전에 공격한 포탑
 *      3. 만약 2번을 만족하는 포탑이 2개 이상이면, 각 포탑의 행과 열의 합이 가장 작은 포탑
 *      4. 만약 3번을 만족하는 포탑이 2개 이상이면, 각 포탑의 열이 가장 작은 포탑
 *
 *   공격 시 레이저 공격 시도 안되면 포탄 공격
 *
 *   레이저 공격
 *   레이저
 *      1. 상하좌우 4개의 방향으로 움지이기 가능
 *      2. 부서진 포탑이 있는 위치는 지날 수 없음
 *      3. 가장자리에서 넘어가면 반대편으로 나옴
 *      최단 경로로 공격 -> 만약 최단경로가 2개 이상이면, 우 / 하 / 좌 / 상 순위로 경로가 선택
 *      공격대상은 공격한 포탑의 공격력만큼 공격력이 줄고, 공격대상까지의 경로에 있는 포탑들은 해당 포탑의 공격력의 반절만큼 줄음
 *
 *   포탑 공격
 *   공격대상이 피해를 입고 추가적으로 주위 8개의 방향에 있는 포탑들도 대미지를 입음
 *   주위 8군데는 절반 공격력 피해
 *   만약 피해가 가장자리를 넘어가면 반대편이 피해
 *
 * 3. 포탑이 부서져
 *
 * 4. 포탑을 정비
 *  부서지지 않은 포탑 중 공격과 무관한 포탑은 공격력이 1씩 증가
 *  공격이 무관하다 : 공격자도 아니고 공격받지도 않았다
 *
 */

public class Main {
    static class Tower implements Comparable<Tower>{

        int r;
        int c;
        int power;
        int lastAttack;
        boolean isRelevantWithAttack;
        public Tower(int r, int c, int power, int lastAttack, boolean isRelevantWithAttack){
            this.r = r;
            this.c = c;
            this.power = power;
            this.lastAttack = lastAttack;
            this.isRelevantWithAttack = isRelevantWithAttack;
        }
        public int compareTo(Tower t){
            // 만약 공격력이 같다면
            if(power == t.power){
                // 만약 최근 공격한 턴 수가 같다면
                if(lastAttack == t.lastAttack){
                    // 열과 합이 같다면
                    if(r+c == t.r+t.c){
                        return t.c - c;
                    }
                    return (t.r + t.c) - (r+c);
                }
                // 최근 공격한 턴이 더 적은애를 선택
                return lastAttack - t.lastAttack;
            }
            // 가장 우선 조건인 더 공격력이 작은 순으로 정렬
            return power - t.power;
        }
    }
    static int N,M,K;
    static Tower[][] map;
    static Tower attacker, target;
    static int answer;
    // 우 우하 하 좌하 좌 좌상 상 우상
    static int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
    static int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new Tower[N][M];

        for(int i = 0; i < N; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++){
                map[i][j] = new Tower(i,j,Integer.parseInt(st.nextToken()),0, false);
            }
        }

//        printMap();

        answer = 0;

        simulate();

        System.out.println(answer);
    }
    static int k;
    private static void simulate(){
        for( k = 1; k <= K ; k++){

            boolean isEnd = setTowerAndAttacker();
//            System.out.println("k: "+k);
//            System.out.println("isEnd: "+isEnd);
            if(!isEnd) break;

            attack();
            repair();
        }
        getAnswer();
    }

    private static boolean setTowerAndAttacker(){
        ArrayList<Tower> list = new ArrayList<>();
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(map[i][j].power > 0){
                    list.add(map[i][j]);
                }
            }
        }

        if(list.size() <= 1) return false;

        Collections.sort(list);

//        System.out.println("after sort list");
//        for(Tower t : list){
//            System.out.println(t.power);
//        }

        attacker = list.get(0);


        attacker.power += (N+M);
        attacker.lastAttack = k;
        attacker.isRelevantWithAttack = true;


        target = list.get(list.size()-1);

        return true;
    }

    private static void attack() {
    Queue<Tower> q = new LinkedList<>();
    boolean canAttackWithLaser = false;
    boolean[][] visited = new boolean[N][M];
    int[][] prev = new int[N][M];  // 이전 노드를 저장하기 위한 배열
    for (int[] row : prev) Arrays.fill(row, -1);  // 초기화

    visited[attacker.r][attacker.c] = true;
    q.offer(attacker);

    while (!q.isEmpty()) {
        Tower curTower = q.poll();

        if (curTower.r == target.r && curTower.c == target.c) {
            canAttackWithLaser = true;
            break;
        }

        for (int d = 0; d < 8; d += 2) {
            int nr = (curTower.r + dx[d] + N) % N;
            int nc = (curTower.c + dy[d] + M) % M;

            if (!visited[nr][nc] && map[nr][nc].power > 0) {
                visited[nr][nc] = true;
                prev[nr][nc] = curTower.r * M + curTower.c;  // 이전 위치 저장
                q.offer(new Tower(nr, nc, map[nr][nc].power, 0, false));
            }
        }
    }

    // 레이저 공격
    if (canAttackWithLaser) {
        int curR = target.r;
        int curC = target.c;

        while (curR != attacker.r || curC != attacker.c) {
            Tower tower = map[curR][curC];
            int previous = prev[curR][curC];
            int prevR = previous / M;
            int prevC = previous % M;

            if (tower.r != target.r || tower.c != target.c) {
                tower.power -= attacker.power / 2;
                tower.isRelevantWithAttack = true;
            }

            curR = prevR;
            curC = prevC;
        }

        target.power -= attacker.power;
        target.isRelevantWithAttack = true;
    } else {
        // 포탄공격
        for (int d = 0; d < 8; d++) {
            int nr = (target.r + dx[d] + N) % N;
            int nc = (target.c + dy[d] + M) % M;
            if (map[nr][nc].power <= 0 || (nr == attacker.r && nc == attacker.c)) continue;

            map[nr][nc].isRelevantWithAttack = true;
            map[nr][nc].power -= (attacker.power / 2);
        }
        target.power -= attacker.power;
    }
}

    private static void repair(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(map[i][j].power <= 0) continue;

                if(map[i][j].isRelevantWithAttack) continue;

                map[i][j].power++;
            }
        }
    }

    private static void getAnswer(){
//        System.out.println("getAnswer");
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(map[i][j].power > answer) {
//                	System.out.println("map["+i+"]["+j+"].power: "+map[i][j].power);
                }
                answer = Math.max(map[i][j].power, answer);
            }
        }
    }

    private static void printMap(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                System.out.print(map[i][j].power+" ");
            }
            System.out.println();
        }
    }

}