import java.io.*;
import java.util.*;

public class Main {
	
	static int k;
	static int n = 10;
	static int board[][] = new int[n][n];
	static void printBoard(int board[][]) {
		for(int y =0 ;y < n; y++) {
			for(int x=0; x< n; x++) {
				if(y >= 4 && x >= 4)
					continue;
				System.out.printf("%-3d", board[y][x]);
			}
			System.out.println();
		}
	}
	static StringTokenizer st;
	static boolean OOB(int y,int x) {
		return y<0 || y>=n || x<0 || x>=n;
	}
	
	/*
	 * type 2,3의 red,yellow로의 이동은 대칭적이다. 
	 */
	static void move(int type, int y,int x,boolean red) {
		if(red) {
			if(type == 1) {
				while(true) {
					if(OOB(y,x+1) || board[y][x+1] == 1)
						break;
					x = x+1;
				}
				board[y][x] = 1;
				
			}else if(type == 2) {
				while(true) {
					if(OOB(y,x+2) || board[y][x+2] == 1)
						break;
					x = x + 1;
				}
				board[y][x] = 1;
				board[y][x+1] = 1;
			}else if(type == 3) {
				while(true) {
					if(OOB(y,x+1) || board[y][x+1] == 1 || OOB(y+1,x+1) || board[y+1][x+1] == 1)
						break;
					x = x+1;
				}
				board[y][x] = 1;
				board[y+1][x] = 1;
			}
		}else {
			if(type == 1) {
				while(true) {
					if(OOB(y+1,x) || board[y+1][x] == 1)
						break;
					y = y + 1;
				}
				board[y][x] = 1;
			}else if(type == 2) {
				while(true) {
					if(OOB(y+1,x) || board[y+1][x] == 1 || OOB(y+1,x+1) || board[y+1][x+1] == 1)
						break;
					y = y + 1;
				}
				board[y][x] = 1;
				board[y][x+1] = 1;
			}else if(type == 3) {
				while(true) {
					if(OOB(y+2,x) || board[y+2][x] == 1)
						break;
					y = y + 1;
				}
				board[y][x] = 1;
				board[y+1][x] = 1;
			}
		}
	}
	
	static void move(int type, int y,int x) {
		move(type,y,x,true); // 빨간색 보드판으로 이동 
		move(type,y,x,false);// 노란색 보드판으로 이동 
	}
	
	static boolean check(int idx, boolean red) {
		if(red) {
			for(int y = 0; y <4 ;y++) {
				if(board[y][idx] == 0)
					return false;
			}
			return true;
		}else {
			for(int x = 0; x < 4; x++) {
				if(board[idx][x] == 0)
					return false;
			}
			return true;
		}
	}
	
	static void shift(int prev, boolean red) {
		if(red) {
			for(int cur = prev - 1; cur >=3; cur--) {
				for(int y = 0; y < 4; y++) {
					board[y][prev] = board[y][cur];
				}
				prev = cur;
			}
		}else {
			for(int cur = prev - 1; cur >= 3; cur-- ) {
				for(int x = 0; x<4; x++) {
					board[prev][x] = board[cur][x];
				}
				prev = cur;
			}
		}
	}
	
	static int score;
	
	static void explode(boolean red) {
		
		for(int i = 9; i>= 6; i--) {
			while(check(i,red)) {
				shift(i,red);
				score++;
			}
		}
		
	}
	
	static void explode() {
		explode(true);
		explode(false);
	}
	static boolean exist(int idx, boolean red) {
		if(red) {
			for(int y = 0; y< 4; y++) {
				if(board[y][idx] == 1)
					return true;
			}
			return false;
		}else {
			for(int x = 0; x < 4;x++) {
				if(board[idx][x] == 1)
					return true;
			}
			return false;
		}
	}
	
	static int calc(boolean red) {
		int cnt = 0;
		for(int i = 4; i<=5; i++) {
			if(exist(i,red))
				cnt++;
		}
		return cnt;
	}
	
	static void push(boolean red) {
		int cnt = calc(red);
		if(red) {
			for(int x = 9; x>=4; x--) {
				for(int y= 0; y< 4; y++)
					board[y][x] = board[y][x-cnt];
			}
		}else {
			for(int y =9; y>=4; y--) {
				for(int x= 0; x < 4; x++)
					board[y][x] = board[y-cnt][x];
			}
		}
		
	}
	
	static void push() {
		push(true);
		push(false);
	}
	
	static void simulate(int type, int y, int x) {
//		System.out.printf("type: %d (%d,%d)\n", type,y,x);
		// 각 보드판으로 이동 
		move(type,y,x);
//		System.out.println("after move");
//		printBoard(board);
		
		// 
		explode();
//		System.out.println("after explode");
//		printBoard(board);
		
		push();
//		System.out.println("after push");
//		printBoard(board);
		
		
	}
	
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        k = Integer.parseInt(br.readLine());
        for(int turn = 1; turn<=k; turn++) {
//        	System.out.println("-----");
//        	System.out.println("turn: "+turn);
        	st = new StringTokenizer(br.readLine());
        	int type = Integer.parseInt(st.nextToken());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	simulate(type,y,x);
        }
        System.out.println(score);
        int cnt = 0;
        for(int y=0;y<n;y++) {
        	for(int x= 0; x<n; x++) {
        		if(board[y][x] == 1)
        			cnt++;
        	}
        }
        System.out.println(cnt);
   }
}