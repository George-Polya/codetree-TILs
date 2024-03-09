import java.io.*;
import java.util.*;

public class Main {
	static int n,m,q;
	static StringTokenizer st;
	static class Node{
		int id;
		Node prev, next;
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static Map<Integer,Integer> id2idx = new HashMap<>();
	static Node nodes[];
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		if(e != null)
			e.prev = s;
	}
	
	static int idx = 1;
	static int MAX_ID = 100000001;
	static int getMinId(Node node) {
		Node cur = node;
		int minId = MAX_ID;
		
		while(true) {
			if(minId == cur.id)
				break;
			minId = Math.min(minId, cur.id);
			cur = cur.next;
		}
		
		return minId;
	}
	
	static void printCounterClockwise(int minId) {
		StringBuilder sb = new StringBuilder();
		
		Node minNode = nodes[id2idx.get(minId)];
		Node cur = minNode;
		boolean flag = false;
		
		while(true) {
			if(cur == minNode && flag)
				break;
			flag = true;
			sb.append(cur.id).append(' ');
			cur = cur.prev;
		}
		System.out.println(sb);
	}
	
	static void fusion(int a, int b) {
		int idxA = id2idx.get(a);
		int idxB = id2idx.get(b);
		
		Node nodeA = nodes[idxA];
		Node nodeB = nodes[idxB];
		
		Node nextA = nodeA.next;
		Node prevB = nodeB.prev;
		
		connect(prevB, nextA);
		connect(nodeA, nodeB);
	}
	
	static void divide(int a, int b) {
		int idxA = id2idx.get(a);
		int idxB = id2idx.get(b);
		
		Node nodeA = nodes[idxA];
		Node nodeB = nodes[idxB];
		
		Node prevA = nodeA.prev;
		Node prevB = nodeB.prev;
		
		connect(prevB, nodeA);
		connect(prevA, nodeB);
	}
	
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        nodes = new Node[n+1];
        
        for(int i = 0; i<m; i++) {
        	st = new StringTokenizer(br.readLine());
        	int num = Integer.parseInt(st.nextToken());
        	ArrayList<Integer> ids = new ArrayList<>();
        	for(int j = 0; j < num; j++) {
        		int id = Integer.parseInt(st.nextToken());
        		ids.add(id);
        		nodes[idx] = new Node(id);
        		id2idx.put(id, idx);
        		idx++;
        	}
        	
        	for(int j = 0; j < num - 1; j++) {
        		Node start = nodes[id2idx.get(ids.get(j))];
        		Node end = nodes[id2idx.get(ids.get(j+1))];
        		connect(start,end);
        	}
        	
        	Node start = nodes[id2idx.get(ids.get(num-1))];
    		Node end = nodes[id2idx.get(ids.get(0))];
    		connect(start, end);
        }
        	
        
      
        
        for(int i = 0; i < q; i++) {
        	st = new StringTokenizer(br.readLine());
        	int oper = Integer.parseInt(st.nextToken());
        	
        	if(oper == 1) {
        		int a = Integer.parseInt(st.nextToken());
        		int b = Integer.parseInt(st.nextToken());
        		fusion(a,b);
        	}else if(oper == 2) {
        		int a = Integer.parseInt(st.nextToken());
        		int b = Integer.parseInt(st.nextToken());
        		divide(a,b);
        	}else if(oper == 3) {
        		int id = Integer.parseInt(st.nextToken());
        		Node node = nodes[id2idx.get(id)];
        		int minId = getMinId(node);
        		printCounterClockwise(minId);
        	}
        }
        
        
    }
}