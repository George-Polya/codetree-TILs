import java.io.*;
import java.util.*;
public class Main {
	static int q;
	static StringTokenizer st;
	static int idx = 1;
	static class Node{
		int id;
		Node prev, next;
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	static Map<Integer, Node> nodes = new HashMap<>();
	static void connect(Node s, Node e) {
		if(e != null)
			e.prev = s;
		if(s != null)
			s.next = e;
	}
	
	static void insertNext(Node cur, int b) {
		Node next = cur.next;
		
		for(int i = 0; i< b;i++) {
			Node node = new Node(idx);
			nodes.put(idx, node);
			idx++;
			connect(cur, node);
			cur = node;
		}
		connect(cur, next);
	}
	
	static void insertPrev(Node cur, int b) {
		Node prev = cur.prev;
		
		for(int i = 0; i< b;i++) {
			Node node = new Node(idx);
			nodes.put(idx, node);
			idx++;
			connect(prev,node);
			prev = node;
		}
		connect(prev, cur);
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Node cur = new Node(idx);
		nodes.put(idx, cur);
		idx++;
		
		q = Integer.parseInt(br.readLine());
		
		for(int i = 0; i< q; i++) {
			st = new StringTokenizer(br.readLine());
			int oper = Integer.parseInt(st.nextToken());
			
			if(oper == 1) {
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				insertNext(nodes.get(a), b);
				
				
			}else if(oper == 2) {
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				insertPrev(nodes.get(a), b);
			}else if(oper == 3) {
				int a = Integer.parseInt(st.nextToken());
				
				Node node = nodes.get(a);
				if(node.prev == null || node.next == null)
					System.out.println(-1);
				else {
					System.out.println(node.prev.id+" "+node.next.id);
				}
			}
		}
		
	}
}