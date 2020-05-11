/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {

    private HashMap<String, Integer> teams;
    private String[] names;
    private int[] w; // wins
    private int[] l; // losses
    private int[] r; // matches remaining
    private int[][] g; // matches remaining between teams i and j

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int len = Integer.parseInt(in.readLine());
        teams = new HashMap<>();
        names = new String[len];
        w = new int[len];
        l = new int[len];
        r = new int[len];
        g = new int[len][len];
        int i = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] contents = line.trim().split(" +");
            teams.put(contents[0], i);
            names[i] = contents[0];
            StdOut.println(contents[0]);
            w[i] = Integer.parseInt(contents[1]);
            l[i] = Integer.parseInt(contents[2]);
            r[i] = Integer.parseInt(contents[3]);
            for (int j = 4; j < contents.length; j++)
                g[i][j - 4] = Integer.parseInt(contents[j]);
            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        int i = teams.get(team);
        return w[i];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        int i = teams.get(team);
        return l[i];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        int i = teams.get(team);
        return r[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException();
        int i = teams.get(team1);
        int j = teams.get(team2);
        return g[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        int i = teams.get(team);
        int max = w[i] + r[i];
        int c = teams.size();
        Queue<FlowEdge> q = new Queue<>();
        FlowNetwork net = new FlowNetwork(2 + teams.size() + binomial(teams.size() - 1, 2));
        for (String t1 : teams.keySet()) {
            if (t1.equals(team)) continue;
            int j = teams.get(t1);
            if (max < w[j]) return true;
            for (int k = j + 1; k < teams.size(); k++) {
                if (k == i) continue;
                q.enqueue(new FlowEdge(net.V() - 2, c, g[j][k])); // s to game
                q.enqueue(new FlowEdge(c, j, Double.POSITIVE_INFINITY)); // game to team a
                q.enqueue(new FlowEdge(c++, k, Double.POSITIVE_INFINITY)); // game to team b
            }
            q.enqueue(new FlowEdge(j, net.V() - 1, max - w[j])); // team a to t
        }
        for (FlowEdge e : q) net.addEdge(e);
        FordFulkerson ff = new FordFulkerson(net, net.V() - 2, net.V() - 1);
        for (FlowEdge e : net.adj(net.V() - 2))
            if (ff.inCut(e.to())) return true;
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
        HashSet<String> survivors = new HashSet<>();
        int i = teams.get(team);
        int max = w[i] + r[i];
        int c = teams.size();
        Queue<FlowEdge> q = new Queue<>();
        FlowNetwork net = new FlowNetwork(2 + teams.size() + binomial(teams.size() - 1, 2));
        for (String t1 : teams.keySet()) {
            if (t1.equals(team)) continue;
            int j = teams.get(t1);
            for (int k = j + 1; k < teams.size(); k++) {
                if (k == i) continue;
                q.enqueue(new FlowEdge(net.V() - 2, c, g[j][k])); // s to game
                q.enqueue(new FlowEdge(c, j, Double.POSITIVE_INFINITY)); // game to team a
                q.enqueue(new FlowEdge(c++, k, Double.POSITIVE_INFINITY)); // game to team b
            }
            if (max < w[j]) {
                survivors.add(names[j]);
                return survivors;
            }
            else q.enqueue(new FlowEdge(j, net.V() - 1, max - w[j])); // team a to t
        }
        for (FlowEdge e : q) net.addEdge(e);
        FordFulkerson ff = new FordFulkerson(net, net.V() - 2, net.V() - 1);
        for (int n = 0; n < names.length; n++)
            if (ff.inCut(n)) survivors.add(names[n]);
        if (survivors.size() < 1) return null;
        return survivors;
    }

    private static int binomial(int n, int k) {
        int[][] C = new int[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, k); j++) {
                if (j == 0 || j == i) C[i][j] = 1;
                else C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }

        return C[n][k];
    }

    // testing
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
