import java.io.*;
import java.util.*;

public class Main {

	static String s_init;
	static int n;
	
	static class Node{
		String value;
		Node next, prev;
		
		public Node(String value) {
			this.value = value;
			this.next = this.prev = null;
		}
	}
	
	static Node cur;
	static StringTokenizer st;
	
	static void insertPrev(Node u, Node v) {
		v.next = u;
		v.prev = u.prev;
		
		
		if(v.next != null)
			v.next.prev = v;
		if(v.prev != null)
			v.prev.next = v;
	}
	
	static void insertNext(Node u, Node v) {
		v.next = u.next;
		v.prev = u;
		if(v.next != null)
			v.next.prev = v;
		if(v.prev != null)
			v.prev.next = v;
		
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		s_init = br.readLine();
		
		cur = new Node(s_init);
		
		n = Integer.parseInt(br.readLine());
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n ;i++) {
			st = new StringTokenizer(br.readLine());
			int oper = Integer.parseInt(st.nextToken());
			
			
			switch(oper) {
			case 1:{
				String value = st.nextToken();
				Node node = new Node(value);
				insertPrev(cur, node);
			}
				break;
			case 2:{
				String value = st.nextToken();
				Node node = new Node(value);
				insertNext(cur, node);
			}
				break;
			case 3:
				if(cur.prev != null) {
					cur.prev.next = cur.next;
					if(cur.next != null)
						cur.next.prev = cur.prev;
					cur = cur.prev;
				}
				break;
			case 4:
				if(cur.next != null) {
					cur.next.prev = cur.prev;
					if(cur.prev !=null)
						cur.prev.next = cur.next;
					cur = cur.next;
				}
				break;
			}
			sb.append(cur.prev == null ? "(Null)" : cur.prev.value).append(' ');
			sb.append(cur.value).append(' ');
			sb.append(cur.next == null ? "(Null)": cur.next.value).append('\n');
			
			
		}
		System.out.println(sb);
		
	}
}