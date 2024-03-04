import java.io.*;
import java.util.*;

public class Main {
    static int n,m,q;
    static StringTokenizer st;
    static class Node{
        int id;
        Node prev, next;
        public Node(int id){
            this.id = id;
            this.prev = this.next = null;
        }
    }

    static Node nodes[];
    static Node head[], tail[];
    
    
    
    static void connect(Node s, Node e){
        if(s!=null)
            s.next = e;
        if(e != null)
            e.prev = s;
    }
    
    static int lines[];
    
    static void pop(Node node) {
    	int line = lines[node.id];
    	
    	if(line == 0)
    		return;
    	
    	if(head[line] == node)
    		head[line] = head[line].next;
    	if(tail[line] == node)
    		tail[line] = tail[line].prev;
    	
    	
    	if(node.prev != null)
    		node.prev.next = node.next;
    	
    	if(node.next != null)
    		node.next.prev = node.prev;
    	
    	lines[node.id] = 0;
    	node.next = node.prev = null;
    	
    }
    
    static void insertFront(Node a ,Node b) {
    	int lineB = lines[b.id];
    	
    	
    	if(head[lineB] == b)
    		head[lineB] = a;
    	
    	pop(a);
    	
    	connect(b.prev, a);
    	connect(a,b);
    	
    	lines[a.id] = lineB;
    }
    
    static void popRangeAndInsertPrev(Node a, Node b, Node c) {
    	int lineA = lines[a.id];
    	int lineC = lines[b.id];
    	
    	if(head[lineA] == a)
    		head[lineA] = b.next;
    	if(tail[lineA] == b)
    		tail[lineA] = a.prev;
    	
    	connect(a.prev, b.next);
    	
    	if(head[lineC] == c) {
    		connect(b,c);
    		head[lineC] = a;
    	}else {
    		connect(c.prev, a);
    		connect(b,c);
     	}
    	
    	Node cur = a;
    	while(cur != b) {
    		lines[cur.id] = lineC;
    		cur = cur.next;
    	}
    	
    	
    }
    static StringBuilder sb = new StringBuilder();
    static void printLine(int idx) {
    	Node cur = head[idx];
    	
    	if(cur == null) {
    		sb.append(-1).append('\n');
    		return;
    	}
    	
    	while(cur != null) {
    		sb.append(cur.id).append(' ');
    		cur = cur.next;
    	}
    	
    	sb.append('\n');
    	
    }
    
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        nodes = new Node[n+1];
        lines = new int[n+1];
        for(int i = 1; i<=n; i++){
            nodes[i] = new Node(i);
        }

        head = new Node[m+1];
        tail = new Node[m+1];
        
        for(int i = 1; i<=m; i++){
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            Node prev = null;
            for(int j = 0; j < num; j++){
                int idx = Integer.parseInt(st.nextToken());
                lines[idx] = i;
                Node cur = nodes[idx];
                if(prev == null){
                    head[i] = cur;
                }else{
                    connect(prev, cur);
                }
                prev = cur;
            }

            tail[i] = prev;

        }

        // print();

        for(int i = 0; i< q; i++){
            st = new StringTokenizer(br.readLine());
            int oper = Integer.parseInt(st.nextToken());

            if(oper == 1){
            	int a = Integer.parseInt(st.nextToken());
            	int b = Integer.parseInt(st.nextToken());
            	insertFront(nodes[a], nodes[b]);
            }else if(oper == 2){
            	int a = Integer.parseInt(st.nextToken());
            	pop(nodes[a]);
            }else if(oper == 3){
            	int a = Integer.parseInt(st.nextToken());
            	int b = Integer.parseInt(st.nextToken());
            	int c = Integer.parseInt(st.nextToken());
                popRangeAndInsertPrev(nodes[a], nodes[b], nodes[c]);
            }
        }
        
        for(int idx = 1; idx<=m; idx++) {
        	printLine(idx);
        }
        
        System.out.println(sb);

    }
}