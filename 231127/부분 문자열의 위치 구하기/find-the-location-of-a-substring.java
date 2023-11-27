import java.util.*;
import java.io.*;
public class Main {
	static String text, pattern;
	static int p[] = {31,39};
	static int mod[] = {(int)1e9 + 7, (int)1e9+9};
	static long pPow[][];
	
	static int n,l;
	static int toInt(char ch) {
		return ch - 'a' + 1;
	}
    public static void main(String[] args) throws IOException{
//        String S = "abcd"; // 주어진 문자열
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	text = br.readLine();
    	pattern = br.readLine();
    	n = text.length();
    	l = pattern.length();
    	pPow = new long[2][n+1];
    	for(int k = 0; k < 2; k++) {
    		pPow[k][0] = 1;
    		for(int i=1; i<=n;i++ ) {
    			pPow[k][i] = (pPow[k][i-1] * p[k]) % mod[k];
    		}
    	}
    	
    	long tH[] = new long[2];
    	for(int k = 0; k < 2; k++) {
    		for(int i = 0; i < l ;i++) {
    			tH[k] = (tH[k] + toInt(text.charAt(i)) * pPow[k][l - i -1]) % mod[k];
    		}
    	}
    	
    	long pH[] = new long[2];
    	for(int k = 0; k< 2;k++) {
    		for(int i = 0; i < l; i++) {
    			pH[k] = (pH[k] + toInt(pattern.charAt(i)) * pPow[k][l-i-1]) % mod[k];
    		}
    	}
    	
    	int idx = -1;
    	for(int i = 1; i<= n - l;i++) {
    		for(int k = 0; k < 2; k++) {
    			tH[k] = (tH[k] * p[k] - toInt(text.charAt(i-1)) * pPow[k][l] + toInt(text.charAt(i + l -1)) ) % mod[k];
    			
    			if(tH[k] <= 0)
    				tH[k] += mod[k];
    		}
    		
    		if(tH[0] == pH[0] && tH[1] == pH[1]) {
    			idx= i;
    			break;
    		}
    	}
    	
    	System.out.println(idx);
    	
    }

  
}