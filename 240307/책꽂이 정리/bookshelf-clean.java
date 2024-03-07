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
	
	static Node pop_front(int line) {
		Node node = head[line];
		if(node !=null) {
			head[line] = head[line].next;
			node.next = null;
			
			if(head[line] != null)
				head[line].prev = null;
			else
				tail[line] = null;
		}
		return node;
	}
	
	static void push_back(Node node, int line) {
		if(head[line] == null) {
			head[line] = tail[line] = node;
		}else {
			connect(tail[line], node);
			tail[line] = node;
		}
	}
	
	static Node pop_back(int line) {
		Node node = tail[line];
		if(node != null) {
			tail[line] = tail[line].prev;
			node.prev = null;
			
			if(tail[line] != null){
				tail[line].next = null;
			}else {
				head[line] = null;
			}
		}
		return node;
	}
	
	static void push_front(Node node, int line) {
		if(head[line] == null) {
			head[line] = tail[line] = node;
		}else {
			connect(node, head[line]);
			head[line] = node;
		}
	}
	
	static void move_all_front(int line1, int line2) {
		if(line1 == line2 || head[line1] == null)
			return;
		
		if(head[line2] == null) {
			head[line2] = head[line1];
			tail[line2] = tail[line1];
		}else {
			connect(tail[line1], head[line2]);
			head[line2] = head[line1];
		}
		head[line1] = tail[line1] = null;
		
	}
	
	static void move_all_back(int line1, int line2) {
		if(line1 == line2 || head[line1] == null)
			return;
		
		if(head[line2] == null) {
			head[line2] = head[line1];
			tail[line2] = tail[line1];
		}else {
			connect(tail[line2], head[line1]);
			tail[line2] = tail[line1];
		}
		
		head[line1] = tail[line1] = null;
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
        		Node node = pop_front(line1);
        		if(node != null)
        			push_back(node, line2);
        	}else if(oper == 2) {
        		Node node = pop_back(line1);
        		if(node != null)
        			push_front(node, line2);
        	}else if(oper == 3) {
        		move_all_front(line1, line2);
        		
        	}else if(oper == 4) {
        		move_all_back(line1, line2);
        	}
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