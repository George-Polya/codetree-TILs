import java.io.*;
import java.util.*;
public class Main {
    static int q;
    static StringTokenizer st;
    public static void main(String[] args) throws IOException{
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        q= Integer.parseInt(br.readLine());
        int x = 0;
        for(int i = 0; i < q; i++){
            st = new StringTokenizer(br.readLine());
            String oper = st.nextToken();
            
            switch(oper){
                case "add":
                int num = Integer.parseInt(st.nextToken());
                    x |= (1 << num);
                break;                    
                case "delete":
                num = Integer.parseInt(st.nextToken());
                    x -= (1 << num);
                break;

                case "toggle":
                num = Integer.parseInt(st.nextToken());
                    x ^= (1 << num);
                break;

                case "print":
                num = Integer.parseInt(st.nextToken());
                    System.out.println( ( (x & (1<<num)) != 0) ? 1 : 0);
                    break;

                case "clear":
                    x = 0;
                    break;

            }
        }
    }
}