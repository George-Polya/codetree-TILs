import java.util.*;
import java.io.*;

public class Main {
	static int board[][] = new int[4+1][8];
	
	static boolean rotated[];
	
	static void shift(int id, int dir) {
		int arr[] = board[id];
		if(dir == 1) {
			int temp = arr[7];
			for(int i = 7; i>=1; i--)
				arr[i] = arr[i-1];
			arr[0] = temp;
		}else {
			int temp = arr[0];
			for(int i = 0; i<=6;i++)
				arr[i] = arr[i+1];
			arr[7] = temp;
		}
	}
	
	static void rotate(int id, int dir) {
		if(rotated[id])
			return;
		
		rotated[id] = true;
		
		if(id > 1 && board[id][6] != board[id-1][2]) {
			rotate(id - 1, -dir);
		}
		
		if(id < 4 && board[id][2] != board[id+1][6]) {
			rotate(id + 1, -dir);
		}
		
		shift(id, dir);
	
	}
	
 	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(int y=1; y<=4; y++) {
			String str = br.readLine();
			for(int x=0; x<8; x++) {
				board[y][x] = str.charAt(x) - '0';
			}
		}
		
		int k = Integer.parseInt(br.readLine());
		
		rotated = new boolean[4+1];
		for(int turn = 1; turn<=k; turn++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			Arrays.fill(rotated, false);
			rotate(id, dir);
		}
		
		int ans = 0;
		for(int id = 1; id<=4;id++) {
			ans += board[id][0] * (1<<(id-1));
		}
		System.out.println(ans);
		
	}
    
}