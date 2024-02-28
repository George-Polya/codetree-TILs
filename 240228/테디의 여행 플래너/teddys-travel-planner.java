import java.io.*;
import java.util.*;
public class Main {
	static StringTokenizer st;
	static int n,q;
	static class Node{
		String value;
		Node next, prev;
		public Node(String value) {
			this.value = value;
			this.next = this.prev = null;
		}
	}
	
	static Node cur;
	static Node nodeArr[];
	
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		if(e != null)
			e.prev = s;
	}
	
	static void moveNext() {
		if(cur.next != null)
			cur = cur.next;
	}
	
	static void movePrev() {
		if(cur.prev != null)
			cur = cur.prev;
	}
	
	static void pop() {
		if(cur.next != null) {
			cur.next = cur.next.next;
			if(cur.next != null)
				cur.next.prev = cur;
		}
	}
	
	static void insertNext(Node cur, Node  node) {
		node.prev = cur;
		node.next = cur.next;
		
		cur.next.prev = node;
		cur.next = node;
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		nodeArr = new Node[n+1];
		st = new StringTokenizer(br.readLine());
		for(int i = 1; i<=n; i++) {
			String city = st.nextToken();
			nodeArr[i] = new Node(city);
		}
		
		for(int i = 1; i< n ;i++) {
			connect(nodeArr[i], nodeArr[i+1]);
		}
		connect(nodeArr[n], nodeArr[1]);
		cur = nodeArr[1];
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			int oper = Integer.parseInt(st.nextToken());
			if(oper == 1) {
				moveNext();
			}else if(oper == 2) {
				movePrev();
			}else if(oper == 3) {
				pop();
			}else if(oper == 4) {
				String city = st.nextToken();
				Node node = new Node(city);
				insertNext(cur, node);
			}
		
			if(cur.prev == null || cur.next == null || cur.prev.value.equals(cur.next.value))
				System.out.println(-1);
			else {
				System.out.println(cur.prev.value+" "+cur.next.value);
			}
			
			
		}
	}
}