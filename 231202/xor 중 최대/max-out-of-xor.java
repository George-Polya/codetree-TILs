import java.util.Scanner;

// Trie에 사용되는 노드를 정의합니다.
class TrieNode {
    // 각 노드에는 '0'부터 '1'까지의 문자에 대응되는 2개의 노드 정보가 관리됩니다.
    TrieNode[] children = new TrieNode[2];

    // 생성자입니다.
    TrieNode() {
        // 각 문자에 대응되는 노드 정보는 처음에 null이 됩니다.
        for(int i = 0; i < 2; i++)
            children[i] = null;
    }
};

public class Main {
    public static final int MAX_N = 150000;
    
    // 변수 선언
    public static int n;
    
    public static int[] p2 = new int[32];
    public static int[] a = new int[MAX_N];
    
    // 숫자를 31자리 2진수 문자열로 변환해줍니다.
    public static String convertToString(int x) {
        String ret = "";
        for(int i = 30; i >= 0; i--) {
            if(x >= p2[i]) {
                x -= p2[i];
                ret += "1";
            }
            else
                ret += "0";
        }

        return ret;
    }
    
    // 루트 노드에 해당하는 TrieNode를 처음 만들어줍니다.
    public static TrieNode root = new TrieNode();
    
    // 단어 s를 Trie에 넣어줍니다.
    public static void insertWord(String s) {
        // root에서 시작합니다.
        TrieNode t = root;
        for(int i = 0; i < (int) s.length(); i++) {
            // 문자 순서대로 따라가면 됩니다.
            // '0'부터 '1'까지 사용되므로
            // 각각을 0부터 1까지의 index로 매핑시켜줍니다.
            int index = s.charAt(i) - '0';
            // 해당하는 노드가 아직 없다면 새로운 노드를 만들어줍니다.
            if(t.children[index] == null)
                t.children[index] = new TrieNode();
            
            // index에 해당하는 노드로 옮겨갑니다.
            t = t.children[index];
        }
    }
    
    // Trie에서 각 단어들을 xor한 최댓값을 탐색합니다.
    // 각 자릿수에서 xor했을 때 1이 될 수 있도록 탐색하며 내려갑니다.
    public static int searchWord(String s) {
        String ret = "";
    
        // root에서 시작합니다.
        TrieNode t = root;
        for(int i = 0; i < (int) s.length(); i++) {
            int index = s.charAt(i) - '0';
    
            // 만약 1 - index가 있다면, 그쪽으로 따라갑니다.
            if(t.children[1 - index] != null) {
                t = t.children[1 - index];
                ret += "1";
            }
            // 없다면 반대쪽으로 따라갑니다.
            else {
                t = t.children[index];
                ret += "0";
            }
        }
    
        // ret 문자열을 십진수로 표현해서 출력합니다.
        int returnValue = 0;
        for(int i = 0; i < ret.length(); i++) {
            returnValue *= 2;
            returnValue += (ret.charAt(i) - '0');
        }
    
        return returnValue;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // p2[i] = 2의 i제곱. 값들을 계산하기에 앞서 미리 전처리합니다.
        p2[0] = 1;
        for(int i = 1; i <= 30; i++)
            p2[i] = p2[i - 1] * 2;

        // 입력:
        n = sc.nextInt();
        for(int i = 0; i < n; i++)
            a[i] = sc.nextInt();

        // Trie에 단어들을 넣어줍니다.
        for(int i = 0; i < n; i++)
            insertWord(convertToString(a[i]));
        
        // Trie에서 각 단어들을 xor한 최댓값을 탐색합니다.
        // 각 자릿수에서 xor했을 때 1이 될 수 있도록 탐색하며 내려갑니다.
        int ans = 0;
        for(int i = 0; i < n; i++) {
            int num = searchWord(convertToString(a[i]));

            ans = Math.max(ans, num);
        }
        
        // 답을 출력합니다.
        System.out.print(ans);
    }
}