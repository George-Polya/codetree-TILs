import java.io.*;
import java.util.*;
public class Main {
    static int l,n,q;
    static StringTokenizer st;
    static int board[][];
    
    static class Knight{
    	int sy,sx,h,w;
    	int hp, damage;
    	public Knight(int sy,int sx, int h, int w, int hp, int damage) {
    		this.sy = sy;
    		this.sx = sx;
    		this.h = h;
    		this.w = w;
    		this.hp = hp;
    		this.damage = damage;
    	}
    }
    
    static Knight NO_KNIGHT = new Knight(-1,-1,-1,-1,-1,-1);
    
    static class Pair{
    	int y,x;
    	public Pair(int y,int x) {
    		this.y = y;
    		this.x = x;
    	}
    	
    	public String toString() {
    		return String.format("%d %d", y,x);
    	}
    }
    
    static Knight knights[];
    
    static void addQ(Knight knight, int dir, Stack<Pair> stk) {
//        if(knight == NO_KNIGHT)
//            return;
        
        int sy = knight.sy;
        int sx = knight.sx;
        int h = knight.h;
        int w = knight.w;
        int ey = sy + h - 1;
        int ex = sx + w - 1;
        
        if(dir == 0) {
            for(int x= sx; x<=ex; x++)
                stk.add(new Pair(sy,x));
        }else if(dir == 1) {
            for(int y=sy;y<=ey;y++) {
                stk.add(new Pair(y,ex));
            }
        }else if(dir == 2) {
            for(int x= sx;x<=ex;x++) {
                stk.add(new Pair(ey,x));
            }
        }else if(dir == 3) {
            for(int y = sy; y<=ey; y++)
                stk.add(new Pair(y,sx));
        }
    }
    
    static int dy[] = {-1,0,1,0};
    static int dx[] = {0,1,0,-1};
    static boolean OOB(int y,int x) {
    	return y<=0 || y>l || x<=0 || x>l;
    }
    
    static int TRAP = 1;
    static int WALL = 2;
    
    
    static boolean inKnight(Knight knight, int y,int x) {
        int sy = knight.sy;
        int sx = knight.sx;
        int ey = sy + knight.h - 1;
        int ex = sx + knight.w - 1;
        
        return sy <= y && y<= ey && sx<=x && x<=ex;
    }
    
    static int getNxtKnight(int y, int x) {
        for(int id =1; id<=n; id++) {
            if(knights[id] == NO_KNIGHT)
                continue;
            
            //생존한 기사들 중에서 
            if(inKnight(knights[id], y, x))
            	return id;
        }
        return -1;
    }
    
    /*
     * id에 해당하는 기사를 dir로 이동시킬 수 있는지 
     * 이동가능하다면 이동되는 기사들의 id를 리턴
     * 이동불가능하다면 empty list를 리턴 
     */
    static void simulate(int id, int dir) {
    	Knight knight = knights[id];
    	boolean canMove = true;
    	if(knight == NO_KNIGHT)
    		canMove = false;
//    	Queue<Pair> stk = new LinkedList<>();
    	Stack<Pair> stk = new Stack<>();
    	List<Integer> moveKnights = new ArrayList<>();
    	moveKnights.add(id);
    	addQ(knight, dir, stk);
    	
    	while(!stk.isEmpty()) {
//    		Pair cur = stk.poll();
    		Pair cur = stk.pop();
    		
    		int ny = cur.y + dy[dir];
    		int nx = cur.x + dx[dir];
    		
    		if(OOB(ny,nx) || board[ny][nx] == WALL) {
    			canMove = false;
    			break;
    		}
    		
    		int nxtId = getNxtKnight(ny,nx);
    		
    		if(nxtId != -1) {
    			Knight nxt = knights[nxtId];
    			addQ(nxt, dir, stk);
    			moveKnights.add(nxtId);
    		}
    		
    	}
    	
    	Arrays.fill(isMoved, false);
    	//이동가능하면 
    	if(canMove) {
    		// 이동
    		
    		for(int i = moveKnights.size() - 1; i>=0 ;i --) {
//    			Knight k = knights[moveKnights.get(i)];
    			int knightId = moveKnights.get(i);
    			Knight k = knights[knightId];
    					
    			if(isMoved[knightId] || k == NO_KNIGHT)
    				continue;
    			
    			// 이동 
    			k.sy += dy[dir];
    			k.sx += dx[dir];
    			
    			int h = k.h;
    			int w = k.w;
    			int sy = k.sy;
    			int sx = k.sx;
    			isMoved[knightId] = true;
    			
    			if(id != knightId) {//명령받은 기사는 피해를 받지 않는다 
    				int ey = sy + h - 1;
    				int ex = sx + w - 1;
    				
    				for(int y = sy; y<=ey; y++) {
    					for(int x= sx; x<=ex; x++) {
    						if(board[y][x] == TRAP)
    							k.damage++;
    					}
    				}
    			}
    			
    			if(k.hp <= k.damage)
    				knights[knightId] = NO_KNIGHT;
    			
    		}
    		
    		
    		
    	}
    }
    
    
    static boolean isMoved[];
    
    
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        l = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        board = new int[l+1][l+1];
        for(int y=1; y<=l; y++) {
            st = new StringTokenizer(br.readLine());
            for(int x=1; x<=l; x++) {
                board[y][x] = Integer.parseInt(st.nextToken());
            }
        }
        
        knights = new Knight[n+1];
        isMoved = new boolean[n+1];
        for(int id=1; id<=n; id++) {
            st = new StringTokenizer(br.readLine());
            int sy = Integer.parseInt(st.nextToken());
            int sx = Integer.parseInt(st.nextToken());
            
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int hp = Integer.parseInt(st.nextToken());
            
            knights[id] = new Knight(sy,sx,h,w,hp, 0);
            
        }
        
        int sum = 0;
        for(int i = 0; i <q ;i++) {
            st = new StringTokenizer(br.readLine());
            int id = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            
            
            simulate(id, dir);
        }
        
        for(int id = 1; id<=n;id++) {
            Knight knight = knights[id];
            if( knight == NO_KNIGHT)
                continue;
            
            sum += knight.damage;
        }
        System.out.println(sum);
    }

}