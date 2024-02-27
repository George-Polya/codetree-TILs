import java.io.*;
import java.util.*;
public class Main {
	static int n,k,q;
	static class Node{
		int id;
		Node prev, next;
		
		public Node(int id) {
			this.id = id;
			this.prev = this.next = null;
		}
	}
	
	static Node nodeArr[];
	static StringTokenizer st;
	static class LinkedList{
		Node head, tail;
		
		public boolean isEmpty() {
			return head == null;
		}
	}
	
	static LinkedList bookShelves[];
	
	static void connect(Node s, Node e) {
		if(s != null)
			s.next = e;
		if(e != null)
			e.prev = s;
	}
	
	static Node pop_front(int i) {
		Node ret = bookShelves[i].head;
		
		if(ret != null) {
			bookShelves[i].head = ret.next;
			ret.next = null;
			
			if(bookShelves[i].head != null)
				bookShelves[i].head.prev = null;
			else
				bookShelves[i].tail = null;
		}
		return ret;
	}
	
	static Node pop_back(int i) {
		Node ret = bookShelves[i].tail;
		
		if(ret != null) {
			bookShelves[i].tail = ret.prev;
			ret.prev = null;
			
			if(bookShelves[i].tail != null)
				bookShelves[i].tail.next = null;
			else
				bookShelves[i].head = null;
			
		}
		return ret;
	}
	
	static void push_front(int i, Node singleton) {
		if(bookShelves[i].head == null) {
			bookShelves[i].head =bookShelves[i].tail = singleton;
		}else {
			connect(singleton, bookShelves[i].head);
			bookShelves[i].head = singleton;
		}
	}
	
	static void push_back(int i, Node singleton) {
		if(bookShelves[i].head == null) {
			bookShelves[i].head =bookShelves[i].tail = singleton;
		}else {
			connect(bookShelves[i].tail , singleton);
			bookShelves[i].tail = singleton;
		}
	}
	
	static void move_all_front(int i, int j) {
		if( i == j || bookShelves[i].isEmpty())
			return;
		
		if(bookShelves[j].isEmpty()) {
			bookShelves[j].head = bookShelves[i].head;
			bookShelves[j].tail = bookShelves[i].tail;
		}else {
			connect(bookShelves[i].tail, bookShelves[j].head);
			bookShelves[j].head = bookShelves[i].head;
		}
		bookShelves[i].head = bookShelves[i].tail = null;
	}
	
	static void move_all_back(int i, int j) {
		if(i == j || bookShelves[i].isEmpty())
			return;
		if(bookShelves[j].isEmpty()) {
			bookShelves[j].head = bookShelves[i].head;
			bookShelves[j].tail = bookShelves[i].tail;
		}else {
			connect(bookShelves[j].tail, bookShelves[i].head);
			bookShelves[j].tail = bookShelves[i].tail;
		}
		bookShelves[i].head = bookShelves[i].tail = null;
	}
	
		
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		nodeArr = new Node[n+1];
		for(int i = 1; i<=n; i++)
			nodeArr[i] = new Node(i);
		for(int i =1; i<n;i++)
			connect(nodeArr[i], nodeArr[i+1]);
		
		
		bookShelves = new LinkedList[k+1];
		for(int i = 1; i<=k; i++) {
			bookShelves[i] = new LinkedList();
		}
		bookShelves[1].head = nodeArr[1];
		bookShelves[1].tail = nodeArr[n];
		q = Integer.parseInt(br.readLine());
		
	
		for(int t = 0; t< q; t++) {
			st = new StringTokenizer(br.readLine());
			int oper = Integer.parseInt(st.nextToken());
			int i = Integer.parseInt(st.nextToken());
			int j = Integer.parseInt(st.nextToken());
			
			if(oper == 1) {
				Node node = pop_front(i);
				if(node != null)
					push_back(j, node);
			}else if(oper == 2) {
				Node node = pop_back(i);
				if(node != null)
					push_front(j, node);
			}else if(oper == 3) {
				move_all_front(i,j);
			}else if(oper == 4) {
				move_all_back(i,j);
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i<=k; i++) {
			int cnt = 0;
			Node cur = bookShelves[i].head;
			StringBuilder sb2 = new StringBuilder();
			
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