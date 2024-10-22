import java.io.*;
import java.util.*;

public class Main {
    static int q;
    static ArrayList<Integer> index = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> mount = new ArrayList<>();
    
    static int search(int h) {
        if(mount.size()==0)
            return 0;
        int s = 0, e = mount.size()-1;
        int idx = mount.size();
        while(s<=e) {
            int m = (s+e)/2;
            if(h<=mount.get(m).get(mount.get(m).size()-1)) {
                idx = m;
                e = m-1;
            } else {
                s = m+1;
            }
        }
        return idx;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        
        q = Integer.parseInt(br.readLine());
        while(q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd==100) {
                int n = Integer.parseInt(st.nextToken());
//                st = new StringTokenizer(br.readLine());
                for(int i=0; i<n; i++) {
                    int h = Integer.parseInt(st.nextToken());
                    int idx = search(h);
                    index.add(idx);
                    if(idx==mount.size())
                        mount.add(new ArrayList<>());
                    mount.get(idx).add(h);
                }
            }
            else if(cmd==200) {
                int h = Integer.parseInt(st.nextToken());
                int idx = search(h);
                index.add(idx);
                if(idx==mount.size())
                    mount.add(new ArrayList<>());
                mount.get(idx).add(h);
            }
            else if(cmd==300) {
                int idx = index.get(index.size()-1);
                index.remove(index.size()-1);
                ArrayList<Integer> lastList = mount.get(idx);
                lastList.remove(lastList.size()-1);
                if(mount.get(mount.size()-1).isEmpty())
                    mount.remove(mount.size()-1);
            }
            else if(cmd==400) {
                int m_idx = Integer.parseInt(st.nextToken()) - 1;
                sb.append((index.get(m_idx) + mount.size()) * 1000000 + 
                         mount.get(mount.size()-1).get(0)).append("\n");
            }
        }
        
        System.out.print(sb);
    }
}