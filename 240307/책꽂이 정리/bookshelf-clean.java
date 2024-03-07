import java.io.*;
import java.util.*;

public class Main {
	static int n,k,q;
	static StringTokenizer st;
	static class Node{
		int id;
		Node prev, next;
		
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static Node nodes[];
	static Node head[], tail[];
	
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		if(e != null)
			e.prev = s;
	}
	
	static void printLine(int line) {
//		Node cur = head[line];
		for(Node cur = head[line]; cur != null; cur=cur.next)
			System.out.print(cur.id+" ");
		System.out.println();
	}
	
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        nodes = new Node[n+1];
        head = new Node[k+1];
        tail = new Node[k+1];
        for(int i = 1; i<=n; i++) {
        	nodes[i] = new Node(i);
        }
        
        head[1] = nodes[1];
        for(int i = 1; i<n;i++) {
        	connect(nodes[i], nodes[i+1]);
        }
        tail[1] = nodes[n];
        
//        printLine(1);
        
        
        q = Integer.parseInt(br.readLine());
        
        for(int i = 0; i< q; i++) {
//        	System.out.println(i+1);
        	st = new StringTokenizer(br.readLine());
        	int oper = Integer.parseInt(st.nextToken());
        	int line1 = Integer.parseInt(st.nextToken());
        	int line2 = Integer.parseInt(st.nextToken());
        	
        	if(oper == 1) {
        		if(head[line1] == null)
        			continue;
        		Node node = head[line1];
        		head[line1] = head[line1].next;
        		head[line1].prev = null;
        		node.next = null;
        		connect(tail[line2], node);
        		tail[line2] = node;
        		if(head[line2] == null)
        			head[line2] = node;
        		
        	}else if(oper == 2) {
        		if(head[line1] == null)
        			continue;
        		
        		Node node = tail[line1];
        		tail[line1] = tail[line1].prev;
        		tail[line1].next = null;
        		
        		node.prev = null;
        		connect(node, head[line2]);
        		head[line2] = node;
        		if(tail[line2] == null)
        			tail[line2] = node;
        		
        	}else if(oper == 3) {
        		if(line1 == line2 || head[line1]== null)
        			continue;
        		connect(tail[line1], head[line2]);
        		head[line2] = head[line1];
        		head[line1] = tail[line1] = null;
        	}else if(oper == 4) {
        		if(line1 == line2 || head[line1] == null)
        			continue;
        		connect(tail[line2], head[line1]);
        		tail[line2] = tail[line1];
        		head[line1] = tail[line1] = null;
        	}
//        	for(int line=1; line<=k;line++) {
//        		printLine(line);
//        	}
//        	System.out.println("-----");
        }
        
        StringBuilder sb = new StringBuilder();
        
        for(int line = 1; line<=k; line++) {
        	StringBuilder sb2 = new StringBuilder();
        	
        	int cnt = 0;
        	Node cur = head[line];
        	while(cur != null) {
        		cnt++;
        		sb2.append(cur.id).append(' ');
        		cur = cur.next;
        	}
        	sb.append(cnt).append(' ').append(sb2).append('\n');
        }
        System.out.println(sb);
        
        
    }
}