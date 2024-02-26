import java.io.*;
import java.util.*;
public class Main {
	static int n,q;
	static StringTokenizer st;
	static class Node{
		int id;
		Node next, prev;
		
		public Node(int id) {
			this.id = id;
			this.next = this.next = null;
		}
	}
	
	static Node[] nodeList;
	
	static void insertPrev(int u, int v) {
		if(nodeList[u] == null) {
			Node node = new Node(u);
			nodeList[u] = node;
		}
		
		Node uNode = nodeList[u];
		
		Node singleton = new Node(v);
		
		singleton.next = uNode;
		singleton.prev = uNode.prev;
		
		if(singleton.prev != null)
			singleton.prev.next = singleton;
		if(singleton.next != null)
			singleton.next.prev = singleton;
			
		nodeList[v] = singleton;
	}
	
	static void insertNext(int u, int v) {
		if(nodeList[u] == null) {
			Node node = new Node(u);
			nodeList[u] = node;
		}
		
		Node uNode = nodeList[u];
		
		Node singleton = new Node(v);
		
		singleton.prev = uNode;
		singleton.next = uNode.next;
		
		if(singleton.prev != null)
			singleton.prev.next = singleton;
		if(singleton.next != null)
			singleton.next.prev = singleton;
		
		nodeList[v] = singleton;
	}
	
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		q = Integer.parseInt(br.readLine());
		nodeList = new Node[n+1];
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < q;i++) {
			st = new StringTokenizer(br.readLine());
			int oper = Integer.parseInt(st.nextToken());
			int u = Integer.parseInt(st.nextToken());
			switch(oper) {
			case 1:
				if(nodeList[u] != null) {
					Node node = nodeList[u];
					if(node.prev != null)
						node.prev.next = node.next;
					if(node.next != null)
						node.next.prev = node.prev;
					nodeList[u] = null;
				}
				break;
			case 2:{
				int v = Integer.parseInt(st.nextToken());
                insertPrev(u, v);
			
			}
				break;
			case 3:{
				int v = Integer.parseInt(st.nextToken());
				insertNext(u,v);
			}
				break;
			case 4:
				if(nodeList[u] != null) {
					Node node = nodeList[u];
					sb.append(node.prev == null ? 0 : node.prev.id).append(' ');
					sb.append(node.next == null ? 0 : node.next.id);
					
				}else{
					sb.append("0 0");
                }
				sb.append('\n');
				break;
				
			}
		}

        for(int i = 1; i<=n; i++){
            if(nodeList[i] == null)
            	sb.append(0).append(' ');
            else{
                Node node = nodeList[i];
                sb.append(node.next == null ? 0 : node.next.id).append(' ');
            }
            
        }
		
        System.out.println(sb);
	}
}