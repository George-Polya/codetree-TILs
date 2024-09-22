package algostury;
import java.util.*;
import java.io.*;

/**
 * - 문제
 * 1. 각 칸의 나무가 인접한 나무 수 만큼 성장
 * 2. 기존 나무와 인접한 4개의 칸 중 다른 나무, 제초제, 벽이 아닌 칸으로 번식
 * 	- 이 때, 번식하는 나무의 수는 ( 현재 나무 값 / 번식 가능한 칸의 수 ) 로 번식하며, 남은 값은 버림
 * 3. 제초제를 뿌려 나무 번식 억제
 * 	- 나무가 가장 많이 박멸되는 칸에 분사
 * 	- 제초제는 나무가 있는 칸에 분사할 경우 k 범위만큼 대각선으로 퍼짐
 *  - 벽이나 나무가 없는 칸을 만날 경우, 해당 칸 까지 제초제를 뿌리고 정지
 *  - 박멸되는 나무의 양이 같은 경우 행이 작을수록, 열이 작을수록 우선순위
 *  - 제초제가 분사된 칸에는 c년째 까지 제초제가 남음
 * 4. m년동안 박멸되는 나무의 수 구하기
 * 
 * - 풀이
 * 1. 나무의 년도수가 저장된 yearMap 하나 더 만들기. 해당 map으로 현재 년도 이전에 자란 나무 모두 번식 => 2번
 * 2. 제초제가 뿌려진 칸의 경우 yearMap을 0으로 초기화 => 2번
 * 3. map에서 벽을 -100로 고정(잔여 제초제의 유지 기간을 음수로 표시하기 위해 벽을 큰 음수로 고정) => 3번
 * 4. 제초제가 뿌려진 구역은 제초제가 남은 년도수를 음수로 표시(제초제가 뿌려질 경우 -c로 해당 지역 표시) => 3번
 * 5. 제초제가 뿌려진 구역은 yearMap상 -1로 표기 => 2, 3번
 *
 */
public class Main {
	static int N, M, K, C;
	static int[] dr = {-1, 0, 1, 0, 1, 1, -1, -1}; // 0~3 + 이동 / 4~7 x 이동
	static int[] dc = {0, -1, 0, 1, 1, -1, 1, -1};
	static int[][] map, yearMap; //각 나무가 몇 년차 나무인지 저장하기 위한 yearMap
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		yearMap = new int[N][N];
		
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == -1) { //벽의 값 변경
					map[i][j] = -100; 
					yearMap[i][j] = -2;
				}
				if(map[i][j] > 0) yearMap[i][j] = 1; //현재 나무를 1년차 나무로 지정(현재 0년차 + 1)
			}
		}
		
		int answer = 0;
		
//		printMap();
		for(int m = 0; m < M; m++) {
//			System.out.printf("----------------- %d년차 ----------------\n", m + 1);
			growTree();
//			printMap();
			spreadTree(m + 1); //0년차 나무가 1로 저장되었으니, 현재 년차에 +1 해주기
//			printMap();
			answer += killTree();
//			printMap();
//			System.out.println(answer);
		}
		
		System.out.println(answer);
	}
	
	public static boolean OOB(int r, int c) { return r < 0 || r >= N || c < 0 || c >= N; }
	public static boolean isWall(int r, int c) { return map[r][c] == -100; }
	
	public static void growTree() {
		for(int r = 0; r < N; r++) {
			for(int c = 0; c < N; c++) {
				if(map[r][c] > 0) {
					int cnt = 0;
					for(int d = 0; d < 4; d++) {
						int nr = r + dr[d];
						int nc = c + dc[d];
						if(OOB(nr, nc) || isWall(nr, nc)) continue;
						if(map[nr][nc] > 0) cnt++;
					}
					
					map[r][c] += cnt;
				}
				
				if(map[r][c] < 0 && !isWall(r, c)) {
					map[r][c]++;
					if(map[r][c] == 0) yearMap[r][c] = 0;
				}
			}
		}
	}
	
	//번식될 나무를 찾고, 나무 번식 함수 실행
	public static void spreadTree(int year) {
		for(int r = 0; r < N; r++) {
			for(int c = 0; c < N; c++) {
				if(yearMap[r][c] > 0 && yearMap[r][c] <= year) spread(r, c, year);
			}
		}
	}
	
	public static void spread(int r, int c, int year) {
		int cnt = 0;
		for(int d = 0; d < 4; d++) {
			int nr = r + dr[d];
			int nc = c + dc[d];
			if(OOB(nr, nc) || isWall(nr, nc)) continue;
			if(yearMap[nr][nc] == 0 || yearMap[nr][nc] == year + 1) cnt++; //빈칸이거나 현재 년도 이전에 심어진 나무만 지정
		}
		
		if(cnt == 0) return; //번식할 공간이 없으면 종료
		int spreadNum = map[r][c] / cnt;
		
		if(spreadNum == 0) return; //번식할 나무가 없으면 종료
		
		for(int d = 0; d < 4; d++) {
			int nr = r + dr[d];
			int nc = c + dc[d];
			if(OOB(nr, nc) || isWall(nr, nc)) continue;
			if(yearMap[nr][nc] == 0 || yearMap[nr][nc] == year + 1) {
				map[nr][nc] += spreadNum;
				yearMap[nr][nc] = year + 1;
			}
		}
		
	}
	
	public static int killTree() { // 나무 성장시 제초제가 한 번 줄어들기 때문에, 기존 기간(C)보다 1년 더 지속되도록 제초제를 넣어주어야함
		int mr = 0; //max r (나무를 가장 많이 없애는 좌표의 row)
		int mc = 0; //max c (나무를 가장 많이 없애는 좌표의 col)
		int maxSum = 0;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(map[i][j] > 0) {
					int sum = map[i][j];
					for(int d = 4; d < 8; d++) {
						int r = i;
						int c = j;
						
						for(int len = 0; len < K; len++) {
							int nr = r + dr[d];
							int nc = c + dc[d];
							
							if(OOB(nr, nc) || map[nr][nc] <= 0 || isWall(nr, nc)) break;
							
							sum += map[nr][nc];
							r = nr;
							c = nc;
						}
						
					}
					
					if(sum > maxSum) {
						maxSum = sum;
						mr = i;
						mc = j;
					}
				}
			}
		}
		
		map[mr][mc] = -(C + 1);
		yearMap[mr][mc] = -1;
		for(int d = 4; d < 8; d++) {
			int r = mr;
			int c = mc;
			
			for(int len = 0; len < K; len++) {
				int nr = r + dr[d];
				int nc = c + dc[d];
				
				if(OOB(nr, nc) || isWall(nr, nc)) break;
				if(map[nr][nc] <= 0) {
					map[nr][nc] = -(C + 1);
					yearMap[nr][nc] = -1;
					break;
				}
				
				map[nr][nc] = -(C + 1);
				yearMap[nr][nc] = -1;
				r = nr;
				c = nc;
			}
			
		}
		
		return maxSum;
	}
	
	public static void printMap() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.printf("%3d ", map[i][j]);
			}
			System.out.println();
		}System.out.println();
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				System.out.printf("%3d ", yearMap[i][j]);
			}
			System.out.println();
		}System.out.println();
	}

}