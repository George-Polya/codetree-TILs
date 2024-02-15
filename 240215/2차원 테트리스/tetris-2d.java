import java.io.*;
import java.util.*;
public class Main {
	static int k;
	static int MAX_R = 10;
	static int board[][] = new int[MAX_R][MAX_R];
	static StringTokenizer st;
	static int score;
	
	static void printBoard(int board[][]) {
		for(int y=0; y<MAX_R;y++) {
			for(int x=0; x<MAX_R;x++) {
				if(y >= 4 && x>= 4)
					System.out.print("  ");
				else
					System.out.print(board[y][x]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static void yellowMove(int type, int sy, int sx) {
		int y = sy;
		int x = sx;
		switch(type) {
		case 1:
			while(true) {
				int ey = y + 1;
				if(ey == 10 || board[ey][x] == 1)
					break;
				y += 1;
			}
			
			board[y][x] = 1;
			break;
		case 2:
			while(true) {
				int ey = y + 1;
				if(ey == 10 || board[ey][x] == 1 || board[ey][x+1] == 1)
					break;
				y += 1;
			}
			
			board[y][x] = 1;
			board[y][x+1] = 1;
			break;
		case 3:
			while(true) {
				int ey = y + 2;
				if(ey == 10 || board[ey][x] == 1)
					break;
				y += 1;
			}
			
			board[y][x] = 1;
			board[y+1][x] = 1;
			break;
		}
	}
	
	static void redMove(int type, int sy, int sx) {
		int y = sy;
		int x = sx;
		
		switch(type) {
		case 1:
			while(true) {
				int ex = x + 1;
				if(ex == 10 || board[y][ex] == 1)
					break;
				x += 1;
			}
			
			board[y][x] = 1;
			break;
		case 2:
			while(true) {
				int ex = x + 2;
				if(ex == 10 || board[y][ex] == 1)
					break;
				x += 1;
			}
			board[y][x] = 1;
			board[y][x+1] = 1;
			break;
			
		case 3:
			while(true) {
				int ex = x + 1;
				if(ex == 10 || board[y][ex] == 1 || board[y+1][ex] == 1)
					break;
				x += 1;
			}
			
			board[y][x] = 1;
			board[y+1][x] = 1;
			break;
		}
	}
	
	
	static void move(int type, int sy, int sx) {
		// 노란색 블록으로 이동 
		yellowMove(type, sy,sx);
		
		// 빨간색블록으로 이동
		redMove(type, sy,sx);
	}
	
	static boolean isFull(int arr[]) {
		for(int i = 0; i < 4; i++) {
			if(arr[i] == 0)
				return false;
		}
		
		// 꽉찬 행이 있으면 점수 + 1
		score += 1;
		return true;
	}
	
	static int findFullY() {
		for(int y = 9; y>=6; y--) {
			if(isFull(board[y]))
				return y;
		}
		return 10;
	}
	
	static int findFullX() {
		for(int x = 9; x>= 6; x--) {
			int temp[] = new int[4];
			for(int y = 0; y< 4; y++) {
				temp[y] = board[y][x];
			}
			
			if(isFull(temp))
				return x;
		}
		return 10;
	}
	
	static void removeFullLine() {
		/**
		 * 꽉찬 행 지우기 
		 * 1. 꽉찬행 찾기 
		 * 2. 찾은 행의 위에서 아래로 당기기 
		 * 3. 꽉찬행이 없을 때까지 반복.
		 */
		
		while(true) {
			int fullY = findFullY();
			if(fullY == 10)
				break;
			for(int y = fullY; y>=4; y--) {
				for(int x= 0; x< 4; x++)
					board[y][x] = board[y-1][x];
			}
			
		}
		
		/**
		 * 꽉찬 열 지우기 
		 * 1. 꽉찬 열 찾기 
		 * 2. 찾은 열의 왼쪽에서 오른쪽으로 당기기 
		 * 3. 꽉찬 열이 없을때까지 반복 
		 */
		while(true) {
			int fullX = findFullX();
			if(fullX == 10)
				break;
			for(int x = fullX; x>=4; x--) {
				for(int y= 0;y < 4; y++)
					board[y][x] = board[y][x-1];
			}
			
		}
		
	}
	
	static boolean lineCheck(int arr[]) {
		for(int i = 0; i<4; i++) {
			if(arr[i] == 1)
				return true;
		}
		return false;
	}
	
	static void redDimAreaProcess() {
		int temp[] = new int[4];
		for(int y = 0; y < 4; y++)
			temp[y] = board[y][5];
		while(lineCheck(temp)) {
			for(int x = 9; x>=4; x--) {
				for(int y= 0; y< 4; y++) {
					board[y][x] = board[y][x-1];
				}
			}
			
			for(int y = 0; y<4; y++) {
				temp[y] = board[y][5];
			}
		}
	}
	
	static void yellowDimAreaProcess() {
		while(lineCheck(board[5])) {
			for(int y = 9; y>=4; y--) {
				for(int x= 0; x< 4; x++)
					board[y][x] = board[y-1][x];
			}
		}
	}
	
	static void dimAreaProcess() {
		// 노란 연한 부분  
		yellowDimAreaProcess();
		// 빨간 연한 부분 
		redDimAreaProcess();
	}
	
	static void simulate(int type,int sy,int sx) {
		// 이동하기 
		move(type, sy,sx);
		
		
		// 꽉찬 행 또는 열  지우기
		removeFullLine();
		
		
		// 연한 부분 블록 처리 
		dimAreaProcess();
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		k = Integer.parseInt(br.readLine());
		for(int i = 0; i<k;i++) {
			st = new StringTokenizer(br.readLine());
			int type = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			simulate(type,y,x);
		}
		System.out.println(score);
		int cnt = 0;
		for(int y = 0 ; y<MAX_R;y++) {
			for(int x= 0; x<MAX_R;x++)
				cnt += board[y][x];
		}
		System.out.println(cnt);
		
	}
}