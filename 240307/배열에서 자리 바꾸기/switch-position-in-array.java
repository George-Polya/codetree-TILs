import java.io.*;
import java.util.*;

public class Main {
	
	static int n,q;
	static class Node{
		int id;
		Node prev,next;
		
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static Node nodes[];
	static StringTokenizer st;
	
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		
		if(e != null)
			e.prev = s;
	}
	
	static void swap(Node A, Node B, Node C, Node D) {
		Node after_prevC = A.prev;
		Node after_nextD = B.next;
		Node after_prevA = C.prev;
		Node after_nextB = D.next;
		
		if(after_prevA == B) {
			after_nextD = A;
			after_prevA = D;
		}
		
		if(after_prevC == D) {
			after_nextB = C;
			after_prevC = B;
		}
		
		connect(after_prevC, C);
		connect(D, after_nextD);
		connect(after_prevA, A);
		connect(B, after_nextB);
	}
	
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        n = Integer.parseInt(br.readLine());
        nodes = new Node[n+1];
        for(int i = 1; i<=n;i++) {
        	nodes[i] = new Node(i);
        }
        
        for(int i = 1; i<n; i++) {
        	connect(nodes[i], nodes[i+1]);
        }
        
        q = Integer.parseInt(br.readLine());
        
        for(int i = 0; i<q;i++) {
        	st = new StringTokenizer(br.readLine());
        	int a = Integer.parseInt(st.nextToken());
        	int b = Integer.parseInt(st.nextToken());
        	int c = Integer.parseInt(st.nextToken());
        	int d = Integer.parseInt(st.nextToken());
        	
        	swap(nodes[a], nodes[b], nodes[c], nodes[d]);
        }
        
        StringBuilder sb = new StringBuilder();
        Node cur = nodes[1];
        while(cur.prev != null)
        	cur = cur.prev;
        
        while(cur != null) {
        	sb.append(cur.id).append(' ');
        	cur = cur.next;
        }
        
        System.out.println(sb);
        
    }
}