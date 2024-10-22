import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

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

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine());

        while (q-- > 0) {
            String[] input = br.readLine().split(" ");
            int o = Integer.parseInt(input[0]);

            if (o == 100) {
                int n = Integer.parseInt(input[1]);
                for (int i = 0; i < n; i++) {
                    int h = Integer.parseInt(br.readLine());
                    int idx = search(h);
                    index.add(idx);
                    if (idx == mount.size())
                        mount.add(new ArrayList<>());
                    mount.get(idx).add(h);
                }
            }

            if (o == 200) {
                int h = Integer.parseInt(input[1]);
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
                int m_idx = Integer.parseInt(input[1]) - 1;
                System.out.println((index.get(m_idx) + mount.size()) * 1000000 + mount.get(mount.size() - 1).get(0));
            }
        }
    }
}