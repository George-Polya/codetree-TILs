import java.util.*;
import java.io.*;

/*
 * 중복순열 + 백트래킹 문제
 */

public class Main {
    static int[] dice = new int[10];
    static int[] map = new int[39];
    static boolean[] isPieceExist = new boolean[39];
    static int[] pieces = new int[4];
    static int answer;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < 10; i++){
            dice[i] = Integer.parseInt(st.nextToken());
        }

        init();

        answer = 0;
        permutation(0, 0);
        System.out.println(answer);
    }

    // map을 초기화 해주는 함수
    private static void init(){
        // 검은 화살표 먼저 초기화 40까지 넣어준다.
        for(int i = 1; i < 21; i++){
            map[i] = i*2;
        }
        // map[21]은 종점으로 0에서 바꾸지 않는다.

        // 노드 13부터 19까지
        map[22] = 13;
        map[23] = 16;
        map[24] = 19;

        // 노드 22부터 24까지
        map[27] = 22;
        map[28] = 24;

        // 노드 28부터 26까지
        map[32] = 28;
        map[33] = 27;
        map[34] = 26;

        // 노드 25부터 35까지
        map[36] = 25;
        map[37] = 30;
        map[38] = 35;
    }

    // 재귀를 이용한 순열을 통해 최대 점수를 구하는 함수
    // depth : dice 배열의 현재 index
    // sum : 현재 점수들의 총합
    private static void permutation(int depth, int sum){
        // 기저 조건
        if(depth == 10){
            // 현재 점수들의 총합과 answer를 비교하여 최댓값을 answer에 넣는다.
            answer = Math.max(answer, sum);
            return;
        }

        // 말 4개를 모두 돌아봐야 한다.
        for(int i = 0; i < 4; i++){
            // 현재 말의 노드 위치 인덱스
            int cur = pieces[i];

            // 만약 현재 말의 노드 위치 인덱스가 종점이면 다음 말을 본다.
            if(cur == 21) continue;

            // 말이 갈 위치 인덱스
            int next = move(cur, 0, dice[depth]);

            // 만약 말이 갈 위치에 다른 말이 존재하지 않으면
            if(!isEixst(next)){
                // 해당 위치에 말이 있다고 하고
                isPieceExist[next] = true;
                // 현재 위치는 더 이상 말이 없다고 한다.
                isPieceExist[cur] = false;
                // 그리고 말을 이동한다.
                pieces[i] = next;

                permutation(depth+1, sum + map[next]);

                isPieceExist[next] = false;
                isPieceExist[cur] = true;
                pieces[i] = cur;

            }
        }
    }

    // 말을 이동할 다음 위치를 계산하는 함수
    // cur : 현재 노드의 인덱스
    // count : 현재 몇 칸 이동했는지 체크하는 변수
    // limit : 이동해야할 총 칸 수
    private static int move(int cur, int count, int limit){
        // 만약 종점이거나 이동해야 할 칸을 모두 이동했다면 현재 위치 인덱스를 반환한다.
        if(cur == 21 || count == limit) return cur;

        if(cur + limit <= 21)
            return cur + limit;
        // next는 다음 노드의 위치 인덱스
        int next = 0;

        // 만약 시작점이 10번 노드(index : 5), 20번 노드(index : 10), 30번 노드(index: 15)이면 index+17을 한다
        // 그렇게 하면 각각의 빨간색 루트를 타게 된다.
        if(count == 0 && (cur == 5 || cur == 10 || cur == 15)){
            next = cur + 17;
        }
        // 만약 현재 위치가 19번 노드(index: 24), 24번 노드(index: 28), 26번 노드(index: 34)이면 25번 노드(index: 36)으로 이동한다.
        else if(cur == 24 || cur == 28 || cur == 34){
            next = 36;
        }
        // 만약 현재 위치가 35번 노드(index: 38)이면 40번 노드(index: 20)로 이동해야한다.
        else if(cur == 38){
            next = 20;
        }
        // 만약 어떤 것에도 속하지 않으면 그냥 1을 더해 다음 인덱스를 본다.
        else {
            next = cur + 1;
        }
        return move(next, count+1, limit);
    }

    // 이동할 위치에 말이 있는지 판단하는 함수
    // 만약 있다면 true를 반환하고 없다면 false를 반환한다.
    private static boolean isEixst(int idx){
        if(idx == 21) return false; // 종점은 항상 비어있음
        return isPieceExist[idx];
    }
}