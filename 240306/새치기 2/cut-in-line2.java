import java.io.*;
import java.util.*;

public class Main {
    static int n,m,q;
    static class Node{
        String name;
        Node prev, next;
        public Node(String name) {
            this.name = name;
            this.prev = this.next = null;
        }
    }
    
    static Map<String, Node> name2Node = new HashMap<>();
    static Map<String, Integer> lines = new HashMap<>();
    static StringTokenizer st;
    
    static Node head[], tail[];
    
    static void connect(Node s, Node e) {
        if(s != null)
            s.next = e;
        if(e != null)
            e.prev = s;
    }
    
    static void pop(String name) {
        Node node = name2Node.get(name);
        int line = lines.get(name);
        
        if(line == 0)
            return;
        
        if(head[line] == node)
            head[line] = head[line].next;
        
        if(tail[line] == node)
            tail[line] = tail[line].prev;
        
        connect(node.prev, node.next);
        
        lines.put(name, 0);
        node.prev = node.next = null;
    }
    
    static void insertPrev(String a, String b) {
        Node nodeA = name2Node.get(a);
        Node nodeB = name2Node.get(b);
        int lineA = lines.get(a);
        int lineB = lines.get(b);
        

	    if(head[lineB] == nodeB){
	        head[lineB] = nodeA;
	    }
	    
        pop(a);


        connect(nodeB.prev, nodeA);
        connect(nodeA, nodeB);
        
//        if(head[lineB] == nodeB) {
//            connect(nodeA, nodeB);
//            head[lineB] = nodeA;
//            
//        }else {
//            connect(nodeB.prev, nodeA);
//            connect(nodeA, nodeB);
//        }

        lines.put(a, lineB);
        
    }
    
    
    static void printLine(int line) {
        Node cur = head[line];
        while(cur != null) {
            System.out.print(cur.name+" ");
            cur = cur.next;
        }
        System.out.println();
    }
    

    static void popRangeAndInsertPrev(String a, String b, String c){
        int lineA = lines.get(a);
        int lineB = lines.get(b);
        int lineC = lines.get(c);

        Node nodeA = name2Node.get(a);
        Node nodeB = name2Node.get(b);
        Node nodeC = name2Node.get(c);
        
        if(head[lineA] == nodeA)
        	head[lineA] = nodeA.next;
        if(tail[lineB] == nodeB)
        	tail[lineB] = nodeB.prev;
        
        connect(nodeA.prev, nodeB.next);
        
        
        if(head[lineC]  == nodeC) {
        	head[lineC] = nodeA;
        }else {
        	connect(nodeC.prev, nodeA);
        }
        	
        	
        
        Node cur = nodeA;
        while(cur != nodeB) {
        	lines.put(cur.name, lineC);
        	cur = cur.next;
        }
    	connect(nodeB, nodeC);
        	
        
    }
    
    
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        head = new Node[m+1];
        tail = new Node[m+1];
        
        st = new StringTokenizer(br.readLine());
        
        for(int i = 1; i <= n; i++) {
            String name = st.nextToken();
            Node node = new Node(name);
            name2Node.put(name, node);
            
            int x = n / m;
            int pos = ((i - 1) % x) + 1;
            
            int line = i % x == 0 ? i /x : (i/x + 1);
//            System.out.println(i+" "+line+" "+pos);
            lines.put(name, line);
            if(head[line] == null) {
                head[line] = tail[line] = node;
            }else {
                connect(tail[line], node);
                tail[line] = node;
            }
            
        }
        
//        for(int line = 1; line<=m; line++)
//            printLine(line);
        
        for(int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int oper = Integer.parseInt(st.nextToken());
            if(oper == 1) {
                String a = st.nextToken();
                String b = st.nextToken();
                insertPrev(a,b);
            }else if(oper == 2) {
                String a = st.nextToken();
                pop(a);
            }else if(oper == 3) {
                String a = st.nextToken();
                String b = st.nextToken();
                String c = st.nextToken();

                popRangeAndInsertPrev(a,b,c);
                
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i<=m; i++) {
        	Node cur = head[i];
        	if(cur == null)
        		sb.append(-1).append('\n');
        	else {
        		while(cur != null) {
        			sb.append(cur.name).append(' ');
        			cur = cur.next;
        		}
        		sb.append('\n');
        	}
        }
        System.out.println(sb);
        
    }
}