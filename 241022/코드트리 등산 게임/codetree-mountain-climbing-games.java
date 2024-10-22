import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Integer> index = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> mount = new ArrayList<>();

    public static int search(int h) {
        if (mount.size() == 0) 
            return 0;
        
        int s = 0, e = mount.size() - 1;
        int idx = mount.size();
        
        while (s <= e) {
            int m = (s + e) / 2;
            if (h <= mount.get(m).get(mount.get(m).size() - 1)) {
                idx = m;
                e = m - 1;
            } else {
                s = m + 1;
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int q = sc.nextInt();
        
        while (q-- > 0) {
            int o = sc.nextInt();
            
            if (o == 100) {
                int n = sc.nextInt();
                for (int i = 0; i < n; i++) {
                    int h = sc.nextInt();
                    int idx = search(h);
                    index.add(idx);
                    if (idx == mount.size())
                        mount.add(new ArrayList<>());
                    mount.get(idx).add(h);
                }
            }
            
            if (o == 200) {
                int h = sc.nextInt();
                int idx = search(h);
                index.add(idx);
                if (idx == mount.size())
                    mount.add(new ArrayList<>());
                mount.get(idx).add(h);
            }
            
            if (o == 300) {
                int idx = index.remove(index.size() - 1);
                mount.get(idx).remove(mount.get(idx).size() - 1);
                if (mount.get(mount.size() - 1).size() == 0)
                    mount.remove(mount.size() - 1);
            }
            
            if (o == 400) {
                int m_idx = sc.nextInt();
                m_idx -= 1;
                System.out.println((index.get(m_idx) + mount.size()) * 1000000 + mount.get(mount.size() - 1).get(0));
            }
        }
        sc.close();
    }
}