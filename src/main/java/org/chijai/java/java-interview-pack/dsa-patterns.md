# DSA — Java templates and recognition

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Recognition table
| Signal | Pattern | Typical cost |
|---|---|---|
| sorted pair / inward scan | two pointers | O(n) |
| contiguous range with monotonic validity | sliding window | O(n) |
| subarray sum including negatives | prefix sum + map | O(n) expected |
| sorted search / monotonic feasibility | binary search | O(log n) probes |
| top K | heap | O(n log k) |
| next greater / histogram | monotonic stack | O(n) |
| unweighted shortest path | BFS | O(V+E) |
| components / cycles | DFS, union-find | O(V+E) or near constant amortized UF |
| prerequisites | topological sort | O(V+E) |
| nonnegative weighted shortest path | Dijkstra | O((V+E) log V) with suitable heap; lazy duplicates O(E log E) |
| overlapping subproblems | dynamic programming | states × transitions |

## P0 — Templates
```java
// Two sum: return indices or empty
static int[] twoSum(int[] a, int target) {
    Map<Long,Integer> seen = new HashMap<>();
    for (int i=0;i<a.length;i++) {
        Integer j = seen.get((long)target-a[i]);
        if (j!=null) return new int[]{j,i};
        seen.put((long)a[i],i);
    }
    return new int[0];
}
// Longest substring without repeating UTF-16 code units
static int longest(String s) {
    Map<Character,Integer> last = new HashMap<>();
    int left=0,best=0;
    for(int r=0;r<s.length();r++) {
        Integer prev=last.put(s.charAt(r),r);
        if(prev!=null) left=Math.max(left,prev+1);
        best=Math.max(best,r-left+1);
    }
    return best;
}
// Lower bound: first index with value >= target, or n
static int lowerBound(int[] a,int target) {
    int lo=0,hi=a.length;
    while(lo<hi) {
        int mid=lo+(hi-lo)/2;
        if(a[mid]<target) lo=mid+1; else hi=mid;
    }
    return lo;
}
// Count subarrays with sum k
static long countSum(int[] a,long k) {
    Map<Long,Long> freq=new HashMap<>(); freq.put(0L,1L);
    long sum=0,ans=0;
    for(int x:a){sum+=x; ans+=freq.getOrDefault(sum-k,0L); freq.merge(sum,1L,Long::sum);}
    return ans;
}
// BFS distance in adjacency list
static int[] bfs(List<List<Integer>> g,int start) {
    int[] d=new int[g.size()]; Arrays.fill(d,-1);
    Deque<Integer> q=new ArrayDeque<>(); q.add(start); d[start]=0;
    while(!q.isEmpty()){
        int u=q.remove();
        for(int v:g.get(u)) if(d[v]==-1){d[v]=d[u]+1;q.add(v);}
    }
    return d;
}
// Topological sort: empty result on cycle (or empty graph)
static List<Integer> topo(List<List<Integer>> g){
    int[] in=new int[g.size()];
    for(var edges:g) for(int v:edges) in[v]++;
    Deque<Integer> q=new ArrayDeque<>();
    for(int i=0;i<in.length;i++) if(in[i]==0) q.add(i);
    List<Integer> out=new ArrayList<>();
    while(!q.isEmpty()){int u=q.remove();out.add(u);for(int v:g.get(u))if(--in[v]==0)q.add(v);}
    return out.size()==g.size()?out:List.of();
}
// Coin change: positive coins, nonnegative amount
static int coins(int[] coins,int amount){
    int[] dp=new int[amount+1]; Arrays.fill(dp,amount+1);dp[0]=0;
    for(int i=1;i<=amount;i++)for(int c:coins)if(c<=i)dp[i]=Math.min(dp[i],dp[i-c]+1);
    return dp[amount]>amount?-1:dp[amount];
}
```
## P1 — Additional patterns to implement
DFS with visited and recursion stack for directed cycles; iterative DFS for deep graphs. Union-find with path compression and union by size. Dijkstra with priority queue of (distance,node), skip stale entries; use long distances and nonnegative weights. Monotonic stack stores indices, pop when invariant violated. Backtracking choose → recurse → undo; prune invalid branches. Interval merge: sort by start, merge when overlapping according to endpoint semantics. Heap top-K: maintain size k, remove smallest for largest K.

## Interview routine
Clarify constraints, propose brute force, derive invariant, implement, dry-run, state time/space. Test empty/singleton, duplicates, negatives, overflow, cycles, disconnected graph and extreme input as applicable. Practice one representative problem per pattern, then variants without notes.
