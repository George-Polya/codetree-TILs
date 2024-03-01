import java.io.*;
import java.util.*;

public class Main {
    static int n,m,q;
    static StringTokenizer st;
    static class Node{
    	int line;
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
    	s.line = e.line;
        if(s!=null)
            s.next = e;
        if(e != null)
            e.prev = s;
    }

    static void print(){
        for(int i = 1; i<=m; i++){
            Node cur = head[i];
            while(cur != null){
                System.out.print(cur.id+" ");
                cur = cur.next;
            }
            System.out.println();
        }
    }
    
    static Node pop(int idx) {
    	Node node = nodes[idx];
    	
    	int line = node.line;
    	
    	if(head[line] == node) {
    		head[line] = node.next;
    		if(head[line] != null)
    			head[line].prev = null;
    	}else if(tail[line] == node) {
    		tail[line] = node.prev;
    		if(tail[line] != null)
    			tail[line].next = null;
    	}else {
    		node.prev.next = node.next;
    		node.next.prev = node.prev;
    	}
    	node.prev = node.next = null;
    	return node;
    } 
    
    static void insertPrev(int a,int b) {
    	Node aNode = pop(a);
    	Node bNode = nodes[b];
    	int bLine = bNode.line;
//    	aNode.line = bLine;
    	if(head[bLine] == bNode) {
    		connect(aNode, bNode);
    		head[bLine] = aNode;
    	}else {
    		aNode.prev = bNode.prev;
    		aNode.next = bNode;
    		
    		bNode.prev.next = aNode;
    		bNode.prev = aNode;
    	}
    	
    }
    
    static void insertPrev(int start, int end, int target) {
    	Node startNode = nodes[start];
    	Node endNode = nodes[end];
    	Node targetNode = nodes[target];
    	
    	int targetLine = targetNode.line;
    	int prevLine = startNode.line;
    	
    	// startNode가 head인 경우
    	if(head[prevLine] == startNode) {
    		head[prevLine] = endNode.next;
    		if(head[prevLine] != null)
    			head[prevLine].prev = null;
    	}
    	
    	// endNode가 tail인 경우
    	if(tail[prevLine] == endNode) {
    		tail[prevLine] = startNode.prev;
    		if(tail[prevLine] != null)
    			tail[prevLine].next = null;
    	}
    	
    	startNode.prev = endNode.next = null;
    	
    	
    	
    	// targetNode가 head인 경우
    	if(head[targetLine] == targetNode) {
    		head[targetLine] = startNode;
    		Node cur = startNode;
    		while(cur != null) {
    			cur.line = targetLine;
    			cur = cur.next;
    		}
    		
    		connect(cur, targetNode);
    	}else {
    		startNode.prev = targetNode.prev;
    		endNode.next = targetNode;
    		
    		targetNode.prev.next = startNode;
    		targetNode.prev = endNode;
    		
    		Node cur = startNode;
    		while(cur != null) {
    			cur.line = targetLine;
    			cur = cur.next;
    		}
    	}
    	
    }

    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        nodes = new Node[n+1];
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
                Node cur = nodes[idx];
                cur.line = i;
                if(prev == null){
                    head[i] = cur;
                }else{
                    connect(prev, cur);
                }
                prev = cur;
            }

            tail[i] = prev;

        }

//         print();
//         System.out.println("------");
         

        for(int i = 0; i< q; i++){
            st = new StringTokenizer(br.readLine());
            int oper = Integer.parseInt(st.nextToken());

            if(oper == 1){
            	int a = Integer.parseInt(st.nextToken());
            	int b = Integer.parseInt(st.nextToken());
            	
            	insertPrev(a,b);
            	

            }else if(oper == 2){
            	int a = Integer.parseInt(st.nextToken());
            	pop(a);
            }else if(oper == 3){
            	int start = Integer.parseInt(st.nextToken());
            	int end = Integer.parseInt(st.nextToken());
            	int target = Integer.parseInt(st.nextToken());
            	
            	insertPrev(start, end, target);
            }
//            print();

//            System.out.println("------");
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i<=m; i++) {
        	if(head[i] == null) {
        		sb.append(-1);
        	}else {
        		Node cur = head[i];
            	            	
            	while(cur != null) {
            		sb.append(cur.id).append(' ');
            		cur= cur.next;
            	}
        	}
        	
        	
        	sb.append('\n');
        }
        System.out.println(sb);

    }
}