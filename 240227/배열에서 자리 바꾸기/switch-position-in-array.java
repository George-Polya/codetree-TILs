import java.io.*;
import java.util.*;
public class Main {
	static int n,q;
	static class Node{
		int id;
		Node prev, next;
		
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static Node nodeArr[];
	static StringTokenizer st;
	
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		
		if(e != null)
			e.prev = s;
		
	}
	
	static void swap(Node a,Node b, Node c, Node d) {
		Node after_prevA = c.prev;
		Node after_nextB = d.next;
		
		Node after_prevC = a.prev;
		Node after_nextD = b.next;
		
		if(b.next == c) {
			after_prevA = d;
			after_nextD = a;
		}
		
		if(d.next == a) {
			after_prevC = b;
			after_nextB = c; 
		}
		
		connect(after_prevA, a);
		connect(b,after_nextB);
		
		connect(after_prevC, c);
		connect(d, after_nextD);
		
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		q = Integer.parseInt(br.readLine());
		nodeArr = new Node[n + 1];
		for(int i =1; i<=n; i++)
			nodeArr[i] = new Node(i);
		
		for(int i = 1; i<n; i++)
			connect(nodeArr[i], nodeArr[i+1]);
		
		
		for(int i = 0; i< q;i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			swap(nodeArr[a], nodeArr[b], nodeArr[c], nodeArr[d]);
		}
		Node cur = nodeArr[1];
		while(cur.prev != null)
			cur = cur.prev;
		
		StringBuilder sb = new StringBuilder();
		while(cur !=null) {
			sb.append(cur.id).append(' ');
			cur = cur.next;
		}
			
		System.out.println(sb);
		
	}
}