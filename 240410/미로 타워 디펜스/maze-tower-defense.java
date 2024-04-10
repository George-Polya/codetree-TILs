import java.io.*;
import java.util.*;

public class Main{
	static int n,m;
	static int arr[], nxtArr[];
	static int dy[] = {0,1,0,-1};
	static int dx[] = {1,0,-1,0};
	static boolean OOB(int y,int x) {
		return y<=0 || y>n || x<=0 || x>n;
	}
	static StringTokenizer st;
	static int board[][], pair2idx[][];
	static int cy, cx;
	static void printBoard(int board[][]) {
		for(int y=1; y<=n; y++) {
			for(int x=1; x<=n; x++) {
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
	}
	
	
	
	static void init() {
		int y = cy;
		int x = cx;
		int moveDir = 2;

		int cnt = 2;
		int idx = 1;
		int moveNum = 1;
		
		while(idx < n*n) {
			for(int i = 0; i < cnt;i++) {
				for(int num=1; num<=moveNum; num++) {
					int ny = y + dy[moveDir];
					int nx = x + dx[moveDir];
					if((OOB(ny,nx)) || idx >= n*n)
						break;
					pair2idx[ny][nx] = idx;
					arr[idx] = board[ny][nx];
					idx++;
					y = ny;
					x = nx;
				}
				moveDir = (moveDir + 3) % 4;
			}
			moveNum += 1;
		}
	}
	
	
	static int score;
	
	static void fill() {
		Arrays.fill(nxtArr, 0);
		int idx = 1;
		for(int i = 1; i < n*n;i++) {
			if(arr[i] == 0)
				continue;
			nxtArr[idx++] = arr[i];
		}
		System.arraycopy(nxtArr, 1, arr, 1, n*n-1);
	}
	
	/*
	 * 1. 공격 
	 *  1.1 d방향으로 p만큼 공격 
	 *  1.2 공격받은 위치는 0이 되고 점수 획득 
	 *  1.3 빈공간이 채워짐  
	 */
	static void attack(int d, int p) {
		int y = cy;
		int x = cx;
		
		for(int dist = 1; dist<=p; dist++) {
			int ny = y + dy[d] * dist;
			int nx = x + dx[d] * dist;
			score += arr[pair2idx[ny][nx]];
			arr[pair2idx[ny][nx]] = 0;
		}
		
		// 빈 공간 채우기 
		fill();
		
	}
	
	static int findEnd(int start) {
		int end = start;
		
		while(end < n*n) {
			if(arr[end] == arr[start]) {
				end++;
			}else {
				break;
			}
		}
		return end - 1;
	}
	
	static void remove(int start,int end) {
		for(int i = start; i<=end;i++) {
			score += arr[i];
			arr[i] = 0;
		}
	}
	
	/*
	 * 같은 몬스터가 4번 이상 나오면 삭제 
	 * 4번 이상 나오는게 없을 때까지 반복 
	 */
	static void chainRemove() {
		boolean explode;
		do {
			explode = false;
			
			
			// 4번 이상 나오면
			// explode = true;
			// arr에서 삭제 
			// fill()
			
			for(int start = 1; start<n*n; start++) {
				if(arr[start] == 0)
					continue;
				int end = findEnd(start); // 같은 종류의 숫자가 나타나는 끝 인덱스 
				
				if(end - start+1 >= 4) {
//					System.out.println(end);
					explode = true;
					// arr에서 삭제 
					remove(start, end);
				}
			}
			fill();
		}while(explode);
	
	
	}
	
	/*
	 * 몬스터 추가 
	 * 1. 같은숫자끼리 짝을 지음
	 * 2. 총 개수, 숫자의 크기로 바꾸워서 미로에 집어넣음 
	 */
	static void add() {
		ArrayList<int[]> list = new ArrayList<>();
		int start = 1;
		while(start < n*n) {
			if(arr[start] == 0)
				break;
			int end = findEnd(start);
//			System.out.println(start+" "+end);
			int[] pair = new int[2];
			pair[0] = (end - start) + 1;
			pair[1] = arr[start];
			list.add(pair);
			if(end - start == 0)
				start++;
			else
				start = end + 1;
		}
		
		Arrays.fill(arr, 0);
		int idx = 1;
		for(int[] pair : list) {
			arr[idx++] = pair[0];
			if(idx >= n*n) {
//				System.out.println(idx);
				break;
			}
			arr[idx++] = pair[1];
			if(idx >= n*n) {
//				System.out.println(idx);
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n*n];
		nxtArr = new int[n*n];
		board = new int[n+1][n+1];
		pair2idx = new int[n+1][n+1];
		for(int y=1; y<=n; y++) {
			st = new StringTokenizer(br.readLine());
			for(int x=1; x<=n; x++) {
				board[y][x] = Integer.parseInt(st.nextToken());
			}
		}
		cy = cx = (n+1)/2;
		init();
		
		
		for(int turn =1; turn<=m;turn++) {
			st = new StringTokenizer(br.readLine());
			int d = Integer.parseInt(st.nextToken());
			int p = Integer.parseInt(st.nextToken());
			
			
			// 공격 
			attack(d, p); 
//			System.out.println(Arrays.toString(arr));
			
			// 연쇄 삭제 
			chainRemove(); 
//			System.out.println(Arrays.toString(arr));
			
			// 몬스터 추가 
			add();
//			System.out.println(Arrays.toString(arr));
		}
		
		System.out.println(score);
		
	}
}