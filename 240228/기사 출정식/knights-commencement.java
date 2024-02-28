import java.io.*;
import java.util.*;
public class Main {
	static int n,m;
	static StringTokenizer st;
	static class Node{
		int id;
		Node prev, next;
		
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static void connect(Node s, Node e) {
		if(e != null)
			e.prev = s;
		if(s != null )
			s.next = e;
	}
	
	static Node nodeArr[];
	
	static void pop(Node node) {
		System.out.println(node.next.id+" "+node.prev.id);
		
		node.prev.next = node.next;
		node.next.prev = node.prev;
		
		node.prev = null;
		node.next = null;
	}
	static Map<Integer, Integer> id2idx = new HashMap<>();
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		nodeArr = new Node[n+1];
		st = new StringTokenizer(br.readLine());
		int idx = 1;
		for(int i = 1; i<=n; i++) {
			int id = Integer.parseInt(st.nextToken());
			id2idx.put(id,  idx);
			idx++;
			nodeArr[i] = new Node(id);
		}
		
		for(int i = 1; i<n;i++)
			connect(nodeArr[i],nodeArr[i+1]);
		connect(nodeArr[n], nodeArr[1]);
		
		for(int i = 0; i < m ;i++) {
			int id = Integer.parseInt(br.readLine());
			idx = id2idx.get(id);
			pop(nodeArr[idx]);
		}
	}
}