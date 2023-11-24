// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.*;
import java.util.*;

public class Main {
    static class TrieNode{
        TrieNode children[] = new TrieNode[10];
        boolean isEnd;

        public TrieNode(){
            isEnd = false;
            for(int i = 0; i < 10; i++){
                children[i] = null;
            }
        }
    }

    static TrieNode root = new TrieNode();
    static void insertWord(String str){
        TrieNode t = root;
        for(int i = 0; i < str.length();i++){
            int idx = str.charAt(i) - '0';
            if(t.children[idx] == null){
                t.children[idx] = new TrieNode();
            }

            t = t.children[idx];
        }

        t.isEnd = true;
    }
    static int n;
    static String[] list;

    static boolean searchWord(String str){
        TrieNode t = root;
        for(int i = 0; i < str.length();i++){
            if(t.isEnd)
                return true;
            int idx = str.charAt(i) - '0';
            t = t.children[idx];
        }
        return false;
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        list = new String[n+1];
        for(int i = 1; i<=n; i++){
            list[i] = br.readLine();
            insertWord(list[i]);
        }

        boolean exist = false;
        for(int i = 1; i<=n; i++){
            if(searchWord(list[i]))
                exist = true;
        }

        System.out.println(exist ? 0 : 1);



    }
}