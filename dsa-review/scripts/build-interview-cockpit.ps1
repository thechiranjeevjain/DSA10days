[CmdletBinding()]
param(
    [ValidateRange(1, 500)]
    [int] $MaxRows = 500
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function ConvertTo-DisplayTitle {
    param([string] $Value)

    $stem = [System.IO.Path]::GetFileNameWithoutExtension($Value)
    $special = @{
        "Three3Sum2Sum" = "2Sum / 3Sum / 4Sum"
        "AGGRCOW" = "Aggressive Cows"
        "LruCache" = "LRU Cache"
        "LCA" = "LCA"
        "LCA_BST" = "LCA BST"
        "LIS" = "LIS"
        "KmpPatterns" = "KMP Patterns"
        "ZFunction" = "Z Function"
    }
    if ($special.ContainsKey($stem)) {
        return $special[$stem]
    }
    $title = $stem -replace "_", " "
    $title = $title -creplace "([A-Z]+)([A-Z][a-z])", '$1 $2'
    $title = $title -creplace "([a-z0-9])([A-Z])", '$1 $2'
    $title = $title -replace "-", " "
    $title = $title -replace "\s+", " "
    return (Get-Culture).TextInfo.ToTitleCase($title.Trim().ToLowerInvariant()) `
        -replace "\bDfs\b", "DFS" `
        -replace "\bBfs\b", "BFS" `
        -replace "\bDp\b", "DP" `
        -replace "\bLca\b", "LCA" `
        -replace "\bBst\b", "BST" `
        -replace "\bKmp\b", "KMP" `
        -replace "\bLru\b", "LRU" `
        -replace "\bXor\b", "XOR" `
        -replace "\bAtoi\b", "Atoi" `
        -replace "\b3sum\b", "3Sum" `
        -replace "\b2sum\b", "2Sum"
}

function ConvertTo-TitleFromSlug {
    param([string] $Slug)

    if ([string]::IsNullOrWhiteSpace($Slug)) {
        return ""
    }
    $special = @{
        "3sum" = "3Sum"
        "01-matrix" = "01 Matrix"
        "lru-cache" = "LRU Cache"
        "kth-largest-element-in-an-array" = "Kth Largest Element In An Array"
        "kth-largest-element-in-a-stream" = "Kth Largest Element In A Stream"
        "koko-eating-bananas" = "Koko Eating Bananas"
        "string-to-integer-atoi" = "String To Integer Atoi"
        "maximum-xor-of-two-numbers-in-an-array" = "Maximum XOR Of Two Numbers In An Array"
    }
    if ($special.ContainsKey($Slug)) {
        return $special[$Slug]
    }
    return ConvertTo-DisplayTitle $Slug
}

function Get-NormalizedKey {
    param([string] $Value)
    if ($null -eq $Value) { return "" }
    return ($Value -replace "[^A-Za-z0-9]", "").ToLowerInvariant()
}

function Get-LinkMatchScore {
    param(
        [string] $RelativeFile,
        [string] $Title,
        [string] $Slug
    )

    $fileStem = [System.IO.Path]::GetFileNameWithoutExtension($RelativeFile)
    $fileKey = Get-NormalizedKey $fileStem
    $titleKey = Get-NormalizedKey $Title
    $slugKey = Get-NormalizedKey $Slug

    if ($fileKey -eq $slugKey -or $fileKey -eq $titleKey) { return 0 }
    if ($fileKey.Contains($slugKey) -or $slugKey.Contains($fileKey)) { return 1 }

    $words = @($Slug -split "-" | Where-Object { $_.Length -ge 4 })
    if ($words.Count -gt 0) {
        $hits = 0
        foreach ($word in $words) {
            if ($fileKey.Contains((Get-NormalizedKey $word))) {
                $hits++
            }
        }
        if ($hits -eq $words.Count) { return 2 }
        if ($hits -gt 0) { return 4 }
    }

    return 9
}

function Get-ProblemOverride {
    param([string] $Title)

    $key = Get-NormalizedKey $Title
    $overrides = @{
        "ransomnote" = @{
            recall = "Count magazine chars, then spend counts for ransom; fail when a needed char is missing."
            hook = "Brute force repeatedly searches magazine; counting turns every char check into O(1)."
            code = "Build int[26] or map from magazine, decrement while scanning ransomNote, return false below zero."
        }
        "majorityelement" = @{
            recall = "Boyer-Moore cancels different values; surviving candidate is majority after optional verification."
            hook = "Counting uses O(n) space; majority > n/2 lets pair cancellation preserve the answer."
            code = "Track candidate and count; reset at zero, increment on match, decrement otherwise."
        }
        "validanagram" = @{
            recall = "Two strings are anagrams when every character count nets to zero."
            hook = "Sorting works but costs O(n log n); frequency counts compare in linear time."
            code = "Reject different lengths, increment for s and decrement for t, then verify all counts zero."
        }
        "2sum3sum4sum" = @{
            recall = "For sum families: hash for 2Sum, sort/fix one value, then two-pointer the remaining sum."
            hook = "Brute force tries all tuples; sorting makes duplicate skipping and pair elimination possible."
            code = "Sort when indices are not required, loop fixed values, move left/right by sum comparison, skip duplicates."
        }
        "validpalindrome" = @{
            recall = "Skip non-alphanumeric chars and compare normalized ends while pointers move inward."
            hook = "Building a cleaned string is extra space; two pointers validate in place."
            code = "Advance left/right past invalid chars, compare lowercase chars, stop when pointers cross."
        }
        "containerwithmostwater" = @{
            recall = "Area is limited by shorter wall, so move the shorter side inward."
            hook = "Brute force checks all pairs; moving taller side cannot improve the limiting height."
            code = "Compute area at left/right, update max, move pointer with smaller height."
        }
        "trappingrainwater" = @{
            recall = "Water at a side depends on the smaller max boundary seen so far."
            hook = "Brute force rescans left/right max for each index; two pointers maintain both maxima."
            code = "Move the side with lower height, update max, add max-height when bounded."
        }
        "longestsubstringwithatmostkdistinctcharacters" = @{
            recall = "Keep a frequency map with at most k distinct chars; shrink until valid."
            hook = "All substrings repeat counting; sliding window updates counts as boundaries move once."
            code = "Expand right count, while distinct > k decrement/remove left, update max length."
        }
        "longestsubstringwithoutrepeatingcharacters" = @{
            recall = "Window must contain unique chars; move left past duplicates."
            hook = "Restarting at every duplicate loses useful overlap; a set/map keeps current window valid."
            code = "Expand right, while duplicate exists remove left, then update max."
        }
        "longestrepeatingcharacterreplacement" = @{
            recall = "Window is valid when size - maxFreq <= k replacements."
            hook = "Trying every target char wastes work; max frequency tells replacement cost."
            code = "Track counts and maxFreq, shrink when windowLen - maxFreq > k, update best."
        }
        "minimumsizesubarraysum" = @{
            recall = "For positive numbers, expand until sum >= target, then shrink to minimize length."
            hook = "Brute force recomputes sums; positivity makes sum monotonic under window movement."
            code = "Add right to sum, while sum >= target update min and subtract left."
        }
        "minimumwindowsubstring" = @{
            recall = "Expand until all needed chars are covered, then shrink while still valid."
            hook = "Checking every substring repeats frequency validation; need/have counts update incrementally."
            code = "Build need map, update have on right, while have == needCount update best and remove left."
        }
        "permutationinstring" = @{
            recall = "A fixed-size window is a permutation when its frequency counts match the target."
            hook = "Sorting every window is too slow; maintain counts as window slides."
            code = "Track counts/matches for window length s1, slide one char in and one char out."
        }
        "substringwithconcatenationofallwords" = @{
            recall = "Scan word-sized windows by offset and keep word counts bounded by need."
            hook = "Trying every substring repeats tokenization; fixed word length gives aligned sliding windows."
            code = "For each offset, move in wordLen steps, count words, shrink when a word is overused."
        }
        "countnumberofnicesubarrays" = @{
            recall = "Exactly k odds equals atMost(k) minus atMost(k-1), or prefix count of odd count."
            hook = "Enumerating subarrays repeats odd counts; prefix/window reuses odd-count state."
            code = "Count subarrays with at most k odd numbers using a sliding left pointer, subtract atMost(k-1)."
        }
        "findallanagramsinastring" = @{
            recall = "Slide a fixed-size frequency window and record starts where counts match p."
            hook = "Sorting each candidate window is expensive; update char counts by one in/out."
            code = "Maintain difference counts or match count across a window of length p."
        }
        "binarysubarrayswithsum" = @{
            recall = "For binary arrays, exact goal count can be atMost(goal) - atMost(goal-1)."
            hook = "Brute force sums all ranges; binary nonnegative values let the window count at-most sums."
            code = "Implement atMost(sum): expand right, shrink while sum > goal, add window length."
        }
        "productofarrayexceptself" = @{
            recall = "Answer is product of everything left times everything right, no division needed."
            hook = "For each index recomputing products is O(n^2); prefix/suffix accumulates in two passes."
            code = "Fill answer with left products, then multiply by running right product from the end."
        }
        "intersectionoftwolinkedlists" = @{
            recall = "Switch heads at null; equal path lengths make pointers meet at intersection or null."
            hook = "HashSet works but costs space; pointer switching aligns the remaining distances."
            code = "Move a and b one step; when null redirect to other head; return when a == b."
        }
        "linkedlistcycle" = @{
            recall = "Slow and fast meet only if a cycle exists."
            hook = "HashSet detects repeats with memory; Floyd uses speed difference in O(1) space."
            code = "Move slow one, fast two while fast and fast.next exist; meeting means cycle."
        }
        "reverselinkedlist" = @{
            recall = "Reverse one edge at a time after saving next."
            hook = "Stack/list copy is extra memory; three pointers reverse in place."
            code = "Keep prev, curr, next; curr.next = prev; advance; return prev."
        }
        "copylistwithrandompointer" = @{
            recall = "Clone nodes then connect next/random using old-to-new mapping or interleaving."
            hook = "Random pointers prevent simple one-pass copy; a map preserves identity mapping."
            code = "First create clones in map, second assign clone.next and clone.random from mapped nodes."
        }
        "oddevenlinkedlist" = @{
            recall = "Keep odd and even chains separately, then attach even head after odd tail."
            hook = "Array grouping is extra space; pointers can preserve relative order in place."
            code = "Move odd to even.next and even to odd.next until even chain ends, then connect."
        }
        "reverselinkedlistii" = @{
            recall = "Use a dummy and reverse exactly the sublist between left and right."
            hook = "Head can change; dummy plus sublist predecessor prevents edge-case bugs."
            code = "Find node before left, then head-insert nodes from the sublist for right-left steps."
        }
        "reversenodesinkgroup" = @{
            recall = "Only reverse a group after confirming k nodes exist."
            hook = "Blind reversal corrupts final short group; group boundary check preserves list."
            code = "Use dummy/groupPrev, locate kth, reverse group, reconnect, advance groupPrev."
        }
        "rotatelist" = @{
            recall = "Make the list circular, then break at length - k % length."
            hook = "Repeated single rotations are too slow; length gives the final split directly."
            code = "Count length and tail, connect tail to head, move to new tail, break circle."
        }
        "swapnodesinpairs" = @{
            recall = "Dummy node lets you swap each adjacent pair without special-casing head."
            hook = "Value swap is not always allowed; pointer swap preserves nodes."
            code = "For each pair, rewire prev->second, first->second.next, second->first."
        }
        "designbrowserhistory" = @{
            recall = "Back/forward are pointer moves over a history chain; visit drops forward history."
            hook = "Arrays are simple but pointer/list model makes state transitions explicit."
            code = "Maintain current node; visit creates current.next and clears forward branch."
        }
        "firstuniquenumber" = @{
            recall = "Queue/list stores arrival order; counts decide whether the front is still unique."
            hook = "Scanning every query is slow; counts plus ordered candidates make showFirstUnique cheap."
            code = "On add update count and queue/list, while front count > 1 pop it."
        }
        "lrucache" = @{
            recall = "HashMap gives O(1) lookup; doubly linked list keeps recency order."
            hook = "A plain map cannot evict least-recently-used; a list gives O(1) move/remove."
            code = "On get/put move node to front; if over capacity remove tail and map entry."
        }
        "movingaveragefromdatastream" = @{
            recall = "Queue last size values and running sum; average is sum divided by queue size."
            hook = "Recomputing average scans the window; running sum updates in O(1)."
            code = "Offer val, add to sum, if queue too large poll and subtract, return sum/count."
        }
        "linkedlistcycleii" = @{
            recall = "After slow/fast meet, move one pointer from head and both one step to find entry."
            hook = "Cycle existence is not enough; Floyd distance math locates the entry in O(1) space."
            code = "Detect meeting, reset one pointer to head, move both until equal."
        }
        "mergetwosortedlists" = @{
            recall = "Dummy tail repeatedly takes the smaller current node."
            hook = "Creating an array loses list structure; merge pointers preserve nodes in one pass."
            code = "Compare l1/l2, append smaller to tail, advance, then attach remainder."
        }
        "mergeksortedlists" = @{
            recall = "A min-heap stores the current smallest head among k lists."
            hook = "Repeatedly scanning k heads costs O(kN); heap reduces selection to O(log k)."
            code = "Push non-null heads, poll min, append it, push its next."
        }
        "middleofthelinkedlist" = @{
            recall = "Fast moves twice as fast; slow lands at the middle when fast finishes."
            hook = "Counting length needs two passes; fast/slow finds middle in one pass."
            code = "While fast and fast.next exist, move slow one and fast two, return slow."
        }
        "binarytreerightsideview" = @{
            recall = "The last node seen at each BFS level is visible from the right."
            hook = "DFS can work, but level BFS directly exposes the rightmost node per depth."
            code = "For each level size, process nodes and record value when i == size - 1."
        }
        "binarytreelevelordertraversal" = @{
            recall = "Capture queue size to process exactly one tree level at a time."
            hook = "Naive queue loop loses level boundaries; size snapshot preserves grouping."
            code = "For each level, poll size nodes, collect values, enqueue children."
        }
        "binarytreeinordertraversal" = @{
            recall = "Inorder is left, node, right; for BST it yields sorted order."
            hook = "Recursive or stack both follow the same left-spine invariant."
            code = "Push left chain, pop node, visit, then go right."
        }
        "binarytreepostordertraversal" = @{
            recall = "Postorder visits children before the node, useful when parent depends on subtree results."
            hook = "A parent cannot be finalized before children when return data flows upward."
            code = "Use recursion or stack with last-visited tracking; visit after left and right."
        }
        "binarytreepreordertraversal" = @{
            recall = "Preorder visits node before children, useful for serialization and copying structure."
            hook = "Root-first order captures decisions before descending."
            code = "Visit node, then left, then right; iterative stack pushes right before left."
        }
        "validatebinarysearchtree" = @{
            recall = "Every node must stay inside strict min/max bounds inherited from ancestors."
            hook = "Checking only parent-child misses ancestor violations."
            code = "DFS with low/high bounds, reject value <= low or >= high, recurse tightened bounds."
        }
        "lowestcommonancestorofabinarytree" = @{
            recall = "If left and right both return a target, current node is the split point."
            hook = "Paths can be found separately, but DFS return contract finds LCA in one pass."
            code = "Return node if null/p/q; ask left/right; if both non-null return root else non-null side."
        }
        "lowestcommonancestorofabinarytreeii" = @{
            recall = "Same split-point idea, but verify both targets actually exist."
            hook = "Returning one found node is wrong when the other target is absent."
            code = "DFS returns found node/count flags; only accept LCA when both p and q are found."
        }
        "lowestcommonancestorofabinarytreeiii" = @{
            recall = "With parent pointers, walk ancestors or switch pointers like linked-list intersection."
            hook = "No root traversal is needed when each node can move upward."
            code = "Move a and b upward; when null redirect to the other node; meeting is LCA."
        }
        "lowestcommonancestorofabinarytreeiv" = @{
            recall = "For many target nodes, current node is answer when multiple target paths meet."
            hook = "Pairwise LCA repeats work; a target set lets DFS aggregate matches."
            code = "Return root if in target set; combine child returns and current membership."
        }
        "minimumabsolutedifferenceinbst" = @{
            recall = "BST inorder is sorted, so minimum difference is between adjacent inorder values."
            hook = "Comparing all pairs is unnecessary once sorted order is available."
            code = "Inorder traverse, track previous value and best difference."
        }
        "rangesumofbst" = @{
            recall = "BST ordering lets you prune subtrees outside [low, high]."
            hook = "Full traversal works but wastes branches that cannot contribute."
            code = "If node < low go right, if node > high go left, else add node and both sides."
        }
        "constructbinarysearchtreefrompreordertraversal" = @{
            recall = "Preorder root plus BST bounds tells where each next value belongs."
            hook = "Searching split points repeatedly is slower; bounds consume preorder once."
            code = "Use index over preorder and recursive upper/lower bounds to build nodes."
        }
        "constructbinarytreefrominorderandpostordertraversal" = @{
            recall = "Postorder last is root; inorder index splits left and right subtrees."
            hook = "Linear search for root each time is slow; map inorder value to index."
            code = "Pop root from postorder end, build right then left using inorder bounds."
        }
        "constructbinarytreefrompreorderandinordertraversal" = @{
            recall = "Preorder first is root; inorder index splits left and right subtrees."
            hook = "The two traversals define structure when values are unique."
            code = "Read preorder index, split by inorder map, recursively build left and right ranges."
        }
        "verifypreorderserializationofabinarytree" = @{
            recall = "Slots start at one; every node consumes a slot, non-null nodes create two."
            hook = "Building the tree is unnecessary; valid serialization preserves slot balance."
            code = "For each token decrement slots, fail below zero, add two slots if token is not #."
        }
        "serializeanddeserializebinarytree" = @{
            recall = "Include null markers so structure can be reconstructed unambiguously."
            hook = "Values alone lose missing-child positions; null markers preserve shape."
            code = "Preorder/BFS serialize with # for null; deserialize by consuming tokens in same order."
        }
        "balancedbinarytree" = @{
            recall = "Return height, but use -1 or flag to propagate unbalanced subtrees early."
            hook = "Computing height repeatedly causes O(n^2); postorder height does it once."
            code = "DFS left/right heights, if either -1 or diff > 1 return -1 else max+1."
        }
        "diameterofbinarytree" = @{
            recall = "Diameter through a node is left height plus right height; return height upward."
            hook = "Global answer differs from helper return value."
            code = "Postorder compute heights, update max diameter with left+right, return max height+1."
        }
        "maximumdepthofbinarytree" = @{
            recall = "Depth is one plus the deeper child depth."
            hook = "Each subtree depth is independent and computed once."
            code = "Return 0 for null, else 1 + max(depth(left), depth(right))."
        }
        "invertbinarytree" = @{
            recall = "Swap left and right at every node."
            hook = "The operation is local and identical for all subtrees."
            code = "DFS or BFS each node, swap children, continue."
        }
        "kthsmallestelementinabst" = @{
            recall = "BST inorder gives ascending values; kth visited is the answer."
            hook = "Heap/sort is unnecessary because BST already encodes order."
            code = "Iterative inorder with stack, decrement k on visit, return when k hits zero."
        }
        "binarytreemaximumpathsum" = @{
            recall = "Helper returns best non-splitting gain; global answer may split through node."
            hook = "Return value and global maximum are different concepts."
            code = "Clamp child gains at zero, update global with node+left+right, return node+max(left,right)."
        }
        "pathsum" = @{
            recall = "Subtract node values along root-to-leaf paths and check target at leaf."
            hook = "Only root-to-leaf complete paths count."
            code = "DFS with remaining sum; at leaf return remaining == node.val."
        }
        "pathsumii" = @{
            recall = "Backtrack the current root-to-leaf path and copy it when the target is hit."
            hook = "Path list is mutable, so choose/explore/undo is required."
            code = "Add node, recurse children with remaining sum, copy on valid leaf, remove node."
        }
        "pathsumiii" = @{
            recall = "Use prefix sums on the root-to-current path to count paths ending at this node."
            hook = "Brute force restarts DFS at every node; prefix sums reuse ancestor sums."
            code = "DFS with running sum, add count[sum-target], increment before children, decrement on backtrack."
        }
        "sumroottoleafnumbers" = @{
            recall = "Carry the number formed so far; at a leaf, add it to the total."
            hook = "The state is the path value, not the full path list."
            code = "DFS with value = value * 10 + node.val; return value at leaves, sum children otherwise."
        }
        "insertintoabinarysearchtree" = @{
            recall = "Use BST ordering to walk one branch until a null child is found, then insert there."
            hook = "BST property removes the need to search both sides."
            code = "Iterate or recurse: if val < node.val go left, else go right; attach new node at null."
        }
        "lowestcommonancestorofabinarysearchtree" = @{
            recall = "If both targets are smaller go left, if both are larger go right, else current node is the split."
            hook = "BST ordering turns LCA into one directed walk instead of full DFS."
            code = "Loop from root; compare p and q to node.val and move left/right until they diverge."
        }
        "searchinabinarysearchtree" = @{
            recall = "Compare target with node value and move only to the branch that can still contain it."
            hook = "BST ordering prunes half the tree at every step."
            code = "While node != null and node.val != val, move left if val < node.val else right."
        }
        "binarysearchtreeiterator" = @{
            recall = "Maintain a stack of the current left spine so next() returns the next inorder value lazily."
            hook = "Need sorted iteration without flattening the whole tree up front."
            code = "pushLeft(root); next() pops, then pushLeft(node.right); hasNext() checks stack."
        }
        "convertbsttogreatertree" = @{
            recall = "Reverse inorder visits larger values first, so a running sum can rewrite each node."
            hook = "BST sorted order makes right-node-left the natural accumulation order."
            code = "Traverse right, add node.val into running sum, rewrite node.val, then traverse left."
        }
        "recoverbinarysearchtree" = @{
            recall = "Inorder traversal should be sorted; the two broken nodes appear at one or two inversions."
            hook = "BST validity is an inorder ordering invariant, not a local parent-child check."
            code = "Track prev, first, second during inorder; after traversal swap first.val and second.val."
        }
        "longestpalindrome" = @{
            recall = "At most one character may have an odd count; pairs from all counts build the longest palindrome."
            hook = "Order does not matter here; frequency parity decides how many chars can be used."
            code = "Count chars, add count / 2 * 2, and allow one odd center if any count is odd."
        }
        "longestpalindromicsubstring" = @{
            recall = "Expand around every odd and even center and keep the longest span."
            hook = "Every palindrome is defined by its center, which is cheaper than checking all substrings."
            code = "For each index, expand(i,i) and expand(i,i+1), update best start/length."
        }
        "sortcolors" = @{
            recall = "Dutch flag keeps < pivot, unknown, and > pivot regions with three pointers."
            hook = "Sorting is overkill for three values; partitioning maintains regions in one pass."
            code = "Use low, mid, high; swap 0 to low, 2 to high, advance mid on 1."
        }
        "meetingrooms" = @{
            recall = "After sorting intervals by start, any overlap with the previous end means a conflict."
            hook = "Unsorted pair checks are noisy; sorting makes the only dangerous interval the previous one."
            code = "Sort by start, scan adjacent intervals, return false if current.start < previous.end."
        }
        "meetingroomsii" = @{
            recall = "Sort meetings by start; a min-heap of end times counts active rooms."
            hook = "Need the earliest finishing active meeting to decide whether a room can be reused."
            code = "Sort intervals, pop heap while end <= start, push current end, track max heap size."
        }
        "slidingwindowmaximum" = @{
            recall = "A decreasing deque stores candidate indices; front is always the current window maximum."
            hook = "Recomputing max for each window is O(nk); the deque removes dominated elements once."
            code = "Drop out-of-window front, pop smaller/equal from back, push index, read front after first window."
        }
        "online stock span" = @{
            recall = "A decreasing stack of price/span pairs merges all previous prices <= current price."
            hook = "Scanning backward repeats work; collapsed spans let each price enter and leave once."
            code = "Start span=1, while stack top price <= current add its span and pop, then push current/span."
        }
        "onlinestockspan" = @{
            recall = "A decreasing stack of price/span pairs merges all previous prices <= current price."
            hook = "Scanning backward repeats work; collapsed spans let each price enter and leave once."
            code = "Start span=1, while stack top price <= current add its span and pop, then push current/span."
        }
        "implementtrieprefixtree" = @{
            recall = "Each trie node represents one prefix; terminal marks distinguish full words from prefixes."
            hook = "HashSet handles exact lookup, but prefix queries need shared character paths."
            code = "For insert/search/startsWith, walk chars through children; create on insert, fail on missing child."
        }
        "designaddandsearchwordsdatastructure" = @{
            recall = "Trie search branches only on '.', otherwise it follows exactly one child."
            hook = "Wildcard lookup cannot be solved by one HashSet lookup; branching is limited by trie prefixes."
            code = "DFS over trie and word index; on '.', try every child, otherwise follow the matching child."
        }
        "wordsearchii" = @{
            recall = "Trie prunes dictionary prefixes while board DFS chooses, marks, explores, and unmarks cells."
            hook = "Running Word Search for every word repeats prefix work; trie shares the dictionary search."
            code = "Build trie, DFS board paths, stop when prefix missing, collect terminal words, mark cells in-place."
        }
        "maximumxoroftwonumbersinanarray" = @{
            recall = "Binary trie chooses the opposite bit greedily to maximize each XOR bit from high to low."
            hook = "Checking all pairs is O(n^2); bitwise trie preserves candidate prefixes cheaply."
            code = "Insert numbers by bits, then for each number walk preferred opposite bits and update max."
        }
        "networkdelaytime" = @{
            recall = "Dijkstra keeps the next shortest unsettled node in a min-heap."
            hook = "Unweighted BFS is not valid with weighted edges; heap order settles shortest distances."
            code = "Build adjacency, push source distance 0, relax neighbors when a smaller distance is found."
        }
        "maximumprofitinjobscheduling" = @{
            recall = "Sort jobs by end time; dp[i] is best profit up to i, with binary search for compatible previous job."
            hook = "Trying all subsets repeats compatibility checks; DP plus sorted end times reuses optimal prefixes."
            code = "Sort by end, for each job compute max(skip, profit + dp[lastNonOverlapping])."
        }
        "timebasedkeyvaluestore" = @{
            recall = "Map each key to timestamped values in order; binary search finds latest timestamp <= query."
            hook = "Scanning history on every get is slow; timestamps are monotonic per key."
            code = "Append on set; on get binary search the key's list for rightmost timestamp <= target."
        }
        "findtheindexofthefirstoccurrenceinastring" = @{
            recall = "KMP reuses the longest proper prefix that is also a suffix after a mismatch."
            hook = "Naive matching restarts too far; LPS tells how much matched work remains valid."
            code = "Build LPS for needle, scan haystack with i/j, and fallback j = lps[j - 1] on mismatch."
        }
        "longesthappyprefix" = @{
            recall = "The answer is the final LPS value: longest proper prefix that is also suffix."
            hook = "Trying every prefix repeats comparisons; KMP prefix table stores reusable border lengths."
            code = "Build LPS over the string and return substring(0, lps[n - 1])."
        }
        "repeatedsubstringpattern" = @{
            recall = "A repeated pattern exists when the final LPS leaves a block length that divides n."
            hook = "Testing every divisor naively repeats string comparisons; KMP exposes the repeated border."
            code = "Let len = lps[n - 1]; return len > 0 and n % (n - len) == 0."
        }
        "shortestpalindrome" = @{
            recall = "Find the longest palindromic prefix, then prepend the reverse of the remaining suffix."
            hook = "Expanding every prefix is expensive; KMP on s + # + reverse(s) finds the prefix length."
            code = "Compute LPS on combined string, reverse suffix from lps length, prepend it to s."
        }
        "burnbinarytree" = @{
            recall = "Treat the tree as an undirected graph from the target node and BFS by minutes."
            hook = "The question is minimum time layers, so BFS is the natural model."
            code = "Build parent links, start BFS from target, expand left/right/parent, count levels."
        }
        "countuniquecharactersofallsubstringsofagivenstring" = @{
            recall = "Each character occurrence contributes by distance to the previous same char times distance to the next one."
            hook = "Contribution counting avoids enumerating all substrings."
            code = "Record previous and next positions for each occurrence, sum leftGap * rightGap contributions."
        }
    }

    if ($overrides.ContainsKey($key)) {
        return $overrides[$key]
    }

    $aliases = @{
        "lcabinarytree" = "lowestcommonancestorofabinarytree"
        "lcabinarytreeii" = "lowestcommonancestorofabinarytreeii"
        "lcabinarytreeiii" = "lowestcommonancestorofabinarytreeiii"
        "lcabinarytreeiv" = "lowestcommonancestorofabinarytreeiv"
        "constructbstfrompreorder" = "constructbinarysearchtreefrompreordertraversal"
        "constructbtfrominorderpostorder" = "constructbinarytreefrominorderandpostordertraversal"
        "constructbtfrompreorderinorder" = "constructbinarytreefrompreorderandinordertraversal"
        "verifypreorderserialization" = "verifypreorderserializationofabinarytree"
        "serializeanddeserializebt" = "serializeanddeserializebinarytree"
        "balancedbt" = "balancedbinarytree"
        "diameterofbt" = "diameterofbinarytree"
        "maximumdepthofbt" = "maximumdepthofbinarytree"
        "invertbt" = "invertbinarytree"
        "kthsmallestinbst" = "kthsmallestelementinabst"
    }

    if ($aliases.ContainsKey($key) -and $overrides.ContainsKey($aliases[$key])) {
        return $overrides[$aliases[$key]]
    }

    return $null
}

function Get-Category {
    param(
        [string] $Pattern,
        [string] $File,
        [string] $Title
    )

    $text = (($Pattern + " " + $File + " " + $Title).ToLowerInvariant())
    $titleText = $Title.ToLowerInvariant()

    if ($titleText -match "api integration|design fraud|design redis|token bucket|tinyurl") { return "Design/LLD" }
    if ($titleText -match "implement trie prefix tree|design add and search words|word search ii|maximum xor|hotel reviews") { return "Trie" }
    if ($titleText -match "sliding window maximum|online stock span") { return "Stack" }
    if ($titleText -match "^meeting rooms$") { return "Intervals/Greedy" }
    if ($titleText -match "maximum profit in job scheduling") { return "Dynamic Programming" }
    if ($titleText -match "network delay time") { return "Graph BFS" }
    if ($titleText -match "^longest palindrome$") { return "HashMap/HashSet" }
    if ($titleText -match "sort colors") { return "Two Pointers" }
    if ($titleText -match "find the index of the first occurrence|longest happy prefix|repeated substring pattern|shortest palindrome") { return "Math/Bit/String" }

    if ($titleText -match "sum of subarray minimums|daily temperatures|next greater|largest rectangle|valid parentheses|calculator|reverse polish|stack") { return "Stack" }
    if ($titleText -match "longest palindromic substring") { return "Two Pointers" }
    if ($titleText -match "count unique characters of all substrings") { return "Math/Bit/String" }
    if ($titleText -match "burn binary tree") { return "Graph BFS" }
    if ($titleText -match "range sum of bst|binary search tree") { return "Tree DFS" }
    if ($titleText -match "maximum subarray|best time to buy|stock") { return "Dynamic Programming" }
    if ($titleText -match "subarray sum equals k|binary subarrays with sum") { return "Prefix/Suffix" }
    if ($titleText -match "valid anagram|ransom note|majority element") { return "HashMap/HashSet" }
    if ($titleText -match "find all anagrams|minimum window|longest substring|substring with|permutation in string|character replacement|subarray") { return "Sliding Window" }
    if ($titleText -match "product of array except self|range sum|prefix") { return "Prefix/Suffix" }
    if ($titleText -match "linked list|lru cache|browser history|first unique|merge .*list|middle of|rotate list|swap nodes|odd even|copy list") { return "Linked List" }
    if ($titleText -match "clone graph|course schedule|bipartite|network delay|accounts merge|word ladder|minimum height trees|island|flood fill|01 matrix|rotting oranges|coloring a border|surrounded regions|pacific atlantic|ranked items") {
        if ($titleText -match "accounts merge") { return "Union Find" }
        if ($titleText -match "course schedule|minimum height trees") { return "Topological Sort" }
        if ($titleText -match "word ladder|01 matrix|rotting oranges|ranked items") { return "Graph BFS" }
        return "Graph DFS"
    }
    if ($titleText -match "level order|right side view|binary tree side view") { return "Tree BFS" }
    if ($titleText -match "binary tree|bst|lca|path sum|diameter|depth|invert|validate|kth smallest|serialize|deserialize|inorder|preorder|postorder|binary search tree") { return "Tree DFS" }
    if ($titleText -match "daily temperatures|next greater|largest rectangle|valid parentheses|calculator|reverse polish|stack") { return "Stack" }
    if ($titleText -match "top k|kth largest|median|task scheduler|closest points|heap") { return "Heap" }
    if ($titleText -match "coin change|edit distance|house robber|unique paths|climbing stairs|partition equal|longest increasing|stock") { return "Dynamic Programming" }
    if ($titleText -match "combination|permutation|subsets|letter combinations|word search") { return "Backtracking" }
    if ($titleText -match "trie|word dictionary|prefix|word search ii") { return "Trie" }

    if ($text -match "sliding|window|substring|anagram") { return "Sliding Window" }
    if ($text -match "two pointer|3sum|2sum|container|palindrome|valid palindrome") { return "Two Pointers" }
    if ($text -match "prefix|product of array|subarray sum|nice sub") { return "Prefix/Suffix" }
    if ($text -match "binary tree|bst|lca_bst|recoverbst|binarysearchtree") { return "Tree DFS" }
    if ($text -match "binary search|koko|aggrcow|search range|rotated sorted|time based") { return "Binary Search" }
    if ($text -match "linkedlist|linked list|cycle|merge2|mergek|middle|reverse linked|copy list|lru") { return "Linked List" }
    if ($text -match "tree|bst|lca|serialize|deserialize|inorder|preorder|postorder") {
        if ($text -match "level|side view|bfs|burn") { return "Tree BFS" }
        return "Tree DFS"
    }
    if ($text -match "graph|island|flood|matrix01|01 matrix|oranges|ladder|course|bipartite|network|accounts|minimum height") {
        if ($text -match "shortest|minimum|level|bfs|matrix01|01 matrix|oranges|ladder|ranked") { return "Graph BFS" }
        if ($text -match "union|accounts") { return "Union Find" }
        if ($text -match "course|topological|height trees") { return "Topological Sort" }
        return "Graph DFS"
    }
    if ($text -match "stack|parentheses|calculator|rpn|temperature|rectangle|rain|greater") { return "Stack" }
    if ($text -match "heap|top k|kth|median|scheduler|closest|priority") { return "Heap" }
    if ($text -match "interval|meeting|platform|scheduling") { return "Intervals/Greedy" }
    if ($text -match "backtracking|combination|permutation|subsets|letter") { return "Backtracking" }
    if ($text -match "trie|prefix|dictionary|word search ii") { return "Trie" }
    if ($text -match "dp|dynamic|coin|robber|paths|lis|edit|partition|kadane|stock") { return "Dynamic Programming" }
    if ($text -match "greedy|gas station") { return "Greedy" }
    if ($text -match "bit|xor|binary|prime|math|unique chars|kmp|zfunction|happy prefix") { return "Math/Bit/String" }
    if ($text -match "design|lld|api|redis|rate limiter|shortner|shortener") { return "Design/LLD" }
    if ($text -match "hash|map|set|frequency|majority|ransom") { return "HashMap/HashSet" }

    return "Core Basics"
}

function Get-DisplayCategory {
    param([string] $Category)

    switch ($Category) {
        "HashMap/HashSet" { return "HashMap / Frequency / Set" }
        "Prefix/Suffix" { return "Prefix Sum / Prefix-Suffix" }
        "Linked List" { return "Linked List Pointers" }
        "Tree BFS" { return "Tree BFS / Level Order" }
        "Tree DFS" { return "Tree DFS / Recursion" }
        "Graph BFS" { return "Graph BFS / Shortest Path" }
        "Graph DFS" { return "Graph DFS / Components" }
        "Binary Search" { return "Binary Search / Answer Search" }
        "Stack" { return "Stack / Monotonic Stack" }
        "Heap" { return "Heap / Priority Queue" }
        "Intervals/Greedy" { return "Intervals / Sorting Greedy" }
        "Backtracking" { return "Backtracking / Combinatorial DFS" }
        "Union Find" { return "Union Find / DSU" }
        "Math/Bit/String" { return "Math / Bit / String" }
        "Design/LLD" { return "Design Data Structures" }
        "Core Basics" { return "Basics / Implementation" }
        default { return $Category }
    }
}

function Get-CategoryWeight {
    param([string] $Category)

    switch ($Category) {
        "HashMap/HashSet" { return 10 }
        "Two Pointers" { return 20 }
        "Sliding Window" { return 30 }
        "Prefix/Suffix" { return 40 }
        "Linked List" { return 50 }
        "Tree BFS" { return 60 }
        "Tree DFS" { return 70 }
        "Graph BFS" { return 80 }
        "Graph DFS" { return 90 }
        "Binary Search" { return 100 }
        "Stack" { return 110 }
        "Heap" { return 120 }
        "Intervals/Greedy" { return 130 }
        "Backtracking" { return 140 }
        "Trie" { return 150 }
        "Dynamic Programming" { return 160 }
        "Union Find" { return 170 }
        "Topological Sort" { return 180 }
        "Greedy" { return 190 }
        "Math/Bit/String" { return 200 }
        "Design/LLD" { return 300 }
        default { return 250 }
    }
}

function Get-ProblemImportanceWeight {
    param(
        [string] $Title,
        [string] $Category,
        [string] $Pattern
    )

    $key = Get-NormalizedKey $Title

    switch ($key) {
        "2sum3sum4sum" { return 1 }
        "binarysearch" { return 2 }
        "longestsubstringwithoutrepeatingcharacters" { return 3 }
        "productofarrayexceptself" { return 4 }
        "minimumwindowsubstring" { return 5 }
        "reverselinkedlist" { return 6 }
        "linkedlistcycle" { return 7 }
        "mergetwosortedlists" { return 8 }
        "validanagram" { return 9 }
        "validpalindrome" { return 10 }
        "mergeksortedlists" { return 11 }
        "containerwithmostwater" { return 12 }
        "trappingrainwater" { return 13 }
        "binarytreelevelordertraversal" { return 14 }
        "validatebinarysearchtree" { return 15 }
        "lowestcommonancestorofabinarytree" { return 16 }
        "numberofislands" { return 17 }
        "courseschedule" { return 18 }
        "coursescheduleii" { return 19 }
        "wordladder" { return 20 }
        "kokoeatingbananas" { return 21 }
        "searchinrotatedsortedarray" { return 22 }
        "findfirstandlastpositionofelementinsortedarray" { return 23 }
        "lrucache" { return 24 }
        "copylistwithrandompointer" { return 25 }
        "kthsmallestelementinabst" { return 26 }
        "balancedbinarytree" { return 27 }
        "diameterofbinarytree" { return 28 }
        "pathsumiii" { return 29 }
        "rottingoranges" { return 30 }
        "01matrix" { return 31 }
        "meetingroomsii" { return 32 }
        "dailytemperatures" { return 33 }
        "validparentheses" { return 34 }
        "topkfrequentelements" { return 35 }
        "houserobber" { return 36 }
        "coinchange" { return 37 }
        "subsets" { return 38 }
        "combinationsum" { return 39 }
        "wordsearch" { return 40 }
        "findmedianfromdatastream" { return 41 }
        "largestrectangleinhistogram" { return 42 }
        "findallanagramsinastring" { return 43 }
        "longestrepeatingcharacterreplacement" { return 44 }
        "longestsubstringwithatmostkdistinctcharacters" { return 45 }
        "permutationinstring" { return 46 }
        "binarysubarrayswithsum" { return 47 }
        "majorityelement" { return 48 }
        "ransomnote" { return 49 }
        "intersectionoftwolinkedlists" { return 50 }
        "linkedlistcycleii" { return 51 }
        "reversenodesinkgroup" { return 52 }
        "lowestcommonancestorofabinarysearchtree" { return 53 }
        "binarytreerightsideview" { return 54 }
        "binarytreeinordertraversal" { return 55 }
        "serializeanddeserializebinarytree" { return 56 }
        "maximumdepthofbinarytree" { return 57 }
        "numberofprovinces" { return 58 }
        "clonegraph" { return 59 }
        "searchinsertposition" { return 60 }
        "findpeakelement" { return 61 }
        "firstbadversion" { return 62 }
        "splitarraylargestsum" { return 63 }
        "capacitytoshippackageswithinddays" { return 64 }
        "minimumnumberofdaystomakembouquets" { return 65 }
        "middleofthelinkedlist" { return 66 }
        "meetingrooms" { return 67 }
        "slidingwindowmaximum" { return 68 }
        "constructbinarytreefrominorderandpostordertraversal" { return 69 }
        "constructbinarytreefrompreorderandinordertraversal" { return 70 }
        "binarytreemaximumpathsum" { return 71 }
        "taskscheduler" { return 72 }
        "kthlargestelementinanarray" { return 73 }
        "kthlargestelementinastream" { return 74 }
        "timebasedkeyvaluestore" { return 75 }
        "timedbasedkeyvaluestore" { return 75 }
        "nextgreaterelementii" { return 76 }
        "sumofsubarrayminimums" { return 77 }
        "evaluatereversepolishnotation" { return 78 }
        "basiccalculator" { return 79 }
    }

    $tier0 = @()
    $tier1 = @(
        "timedbasedkeyvaluestore",
        "timebasedkeyvaluestore",
        "binarytreerightsideview",
        "binarytreeinordertraversal",
        "maximumdepthofbinarytree",
        "reversenodesinkgroup",
        "constructbinarytreefrompreorderandinordertraversal",
        "constructbinarytreefrominorderandpostordertraversal",
        "binarytreemaximumpathsum"
    )
    $tier2 = @(
        "floodfill",
        "isgraphbipartite",
        "nextgreaterelementii",
        "sumofsubarrayminimums",
        "basiccalculator",
        "evaluatereversepolishnotation",
        "findmedianfromdatastream",
        "topkfrequentelements",
        "taskscheduler",
        "minimumnumberofarrows toburstballoons".Replace(" ",""),
        "combinationsum",
        "wordsearch",
        "implementtrieprefixtree",
        "houserobber",
        "uniquepaths",
        "coinchange",
        "longestincreasingsubsequence",
        "partitionequalsubsetsum"
    )
    $tier3 = @(
        "substringwithconcatenationofallwords",
        "oddevenlinkedlist",
        "rotatelist",
        "swapnodesinpairs",
        "designbrowserhistory",
        "firstuniquenumber",
        "movingaveragefromdatastream",
        "constructbinarysearchtreefrompreordertraversal",
        "verifypreorderserializationofabinarytree",
        "invertbinarytree",
        "sumroottoleafnumbers",
        "surroundedregions",
        "pacificatlanticwaterflow",
        "networkdelaytime",
        "burnbinarytree",
        "slidingwindowmaximum",
        "designaddandsearchwordsdatastructure",
        "wordsearchii",
        "maximumprofitinjobscheduling",
        "kad anemaxsubarray".Replace(" ",""),
        "sortcolors",
        "besttimetobuyandsellstock",
        "accountsmerge",
        "minimumheighttrees"
    )

    $weight = 80
    if ($key -in $tier0) { $weight = 0 }
    elseif ($key -in $tier1) { $weight = 15 }
    elseif ($key -in $tier2) { $weight = 35 }
    elseif ($key -in $tier3) { $weight = 55 }

    if ($key -match "(ii|iii|iv)$") { $weight += 12 }
    if ($key -match "design|iterator|serialization|deserialize|stream") { $weight += 6 }
    if ($Category -in @("Dynamic Programming", "Trie", "Math/Bit/String", "Design/LLD")) { $weight += 8 }
    if ($Pattern.ToLowerInvariant() -match "variant|ranking|fundamentals") { $weight += 10 }

    switch ($key) {
        "searchinrotatedsortedarrayii" { return 56 }
        "binarysearchtreeiterator" { return 80 }
        "maximumxoroftwonumbersinanarray" { return 84 }
        "countuniquecharactersofallsubstringsofagivenstring" { return 88 }
        "findtheindexofthefirstoccurrenceinastring" { return 78 }
        "longesthappyprefix" { return 86 }
        "repeatedsubstringpattern" { return 82 }
        "shortestpalindrome" { return 90 }
        "designfraudpatterndetection" { return 98 }
        "apiintegrationexample" { return 99 }
        "designredis" { return 99 }
        "designtokenbucketratelimiter" { return 99 }
        "encodeanddecodetinyurl" { return 92 }
        default { return $weight }
    }
}

function Get-PriorityWeight {
    param([string] $Priority)

    switch ($Priority) {
        "A" { return 0 }
        "B" { return 1000 }
        "C" { return 2000 }
        default { return 3000 }
    }
}

function Get-MustLevel {
    param(
        [int] $Rank
    )

    if ($Rank -le 30) { return "Phase 1 - No Red Flags" }
    if ($Rank -le 70) { return "Phase 2 - Strong Core" }
    if ($Rank -le 110) { return "Phase 3 - Important" }
    if ($Rank -le 150) { return "Phase 4 - Secondary" }
    return "Phase 5 - If Time"
}

function Get-PhaseSummary {
    param([string] $Phase)

    switch ($Phase) {
        "Phase 1 - No Red Flags" { return "Ranks 1-30. Remove common interview red flags first." }
        "Phase 2 - Strong Core" { return "Ranks 31-70. High-frequency core patterns after the first pass is stable." }
        "Phase 3 - Important" { return "Ranks 71-110. Important breadth once the core signal is reliable." }
        "Phase 4 - Secondary" { return "Ranks 111-150. Good coverage after the main interview patterns are under control." }
        "Phase 5 - If Time" { return "Ranks 151+. Cover only if time remains or a target interviewer leans this way." }
        default { return "" }
    }
}

function Get-Recall {
    param(
        [string] $Category,
        [string] $Pattern,
        [string] $Title
    )

    switch ($Category) {
        "HashMap/HashSet" { return "Store counts, complements, or seen state so repeated lookup becomes O(1)." }
        "Two Pointers" { return "Shrink the search space by moving the pointer that can still improve the answer." }
        "Sliding Window" { return "Expand right, shrink left to restore validity, then update the answer at the right time." }
        "Prefix/Suffix" { return "Precompute cumulative left/right state so each range or exclusion is answered cheaply." }
        "Binary Search" { return "Maintain a monotonic search space and discard the half that cannot contain the answer." }
        "Linked List" { return "Name every pointer, save next before rewiring, and return the real new head." }
        "Tree BFS" { return "Use a queue by levels; capture level size before pushing children." }
        "Tree DFS" { return "Define exactly what the helper returns, combine left/right, and update global answer separately if needed." }
        "Graph BFS" { return "For unweighted minimum steps, mark when enqueuing because first discovery is shortest." }
        "Graph DFS" { return "Mark visited before recursion and explore one component/path completely." }
        "Stack" { return "Stack stores unresolved candidates; current item resolves or validates the top." }
        "Heap" { return "Heap top is the next best candidate; keep only the frontier or top K when possible." }
        "Intervals/Greedy" { return "Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints." }
        "Backtracking" { return "Choose, recurse, undo; the path is exactly the current decision state." }
        "Trie" { return "Each node is a prefix; branch only when wildcard or board search requires it." }
        "Dynamic Programming" { return "Fix dp state meaning, base cases, transition, and iteration order before coding." }
        "Union Find" { return "Represent components with parent links; union merges and failed union detects cycles." }
        "Topological Sort" { return "Use indegree or DFS states to process dependencies before dependents." }
        "Greedy" { return "Take the local choice only after proving it cannot hurt the future optimum." }
        "Math/Bit/String" { return "Use the algebra, bit, or string invariant instead of simulating blindly." }
        "Design/LLD" { return "State API contract, data structures, invariants, and complexity per operation." }
        default { return "Start from brute force, find repeated work, and state the invariant before coding." }
    }
}

function Get-InterviewHook {
    param(
        [string] $Category,
        [string] $Pattern,
        [string] $Title
    )

    switch ($Category) {
        "HashMap/HashSet" { return "Brute force scans for matches; bottleneck is repeated lookup; use a map/set to preserve processed state." }
        "Two Pointers" { return "Brute force tries pairs; sorting/order lets pointers eliminate impossible pairs." }
        "Sliding Window" { return "Brute force checks all substrings/subarrays; a window reuses counts while boundaries move once." }
        "Prefix/Suffix" { return "Brute force recomputes ranges; prefix/suffix stores reusable aggregate state." }
        "Binary Search" { return "Brute force scans candidates; monotonicity lets each check discard half the search space." }
        "Linked List" { return "Brute force may use extra storage; pointer invariants let us solve in one pass or O(1) space." }
        "Tree BFS" { return "DFS can mix levels; BFS preserves level order for views, distances, and serialization." }
        "Tree DFS" { return "Brute force revisits subtrees; helper return contracts summarize each subtree once." }
        "Graph BFS" { return "DFS finds a path, but BFS gives shortest path when every edge has equal cost." }
        "Graph DFS" { return "Brute force revisits states; visited DFS gives each component/path a single exploration." }
        "Stack" { return "Brute force searches previous/next matches; stack keeps unresolved candidates in useful order." }
        "Heap" { return "Sorting everything is wasteful; a heap keeps only the next best or top K frontier." }
        "Intervals/Greedy" { return "Unsorted comparisons are noisy; sorting makes overlap or greedy choice local." }
        "Backtracking" { return "Brute force generates blindly; backtracking prunes invalid decision paths early." }
        "Trie" { return "Repeated string scans waste prefix work; trie shares prefixes across words." }
        "Dynamic Programming" { return "Naive recursion repeats states; DP caches each state and reuses transitions." }
        "Union Find" { return "Repeated graph searches are expensive; union-find maintains components incrementally." }
        "Topological Sort" { return "Brute force dependency checks loop; topo processes nodes only when prerequisites are done." }
        "Greedy" { return "DP/search may be possible, but a proven safe local choice collapses the state space." }
        "Math/Bit/String" { return "Simulation is often slow or bug-prone; use the invariant encoded in arithmetic or bits." }
        "Design/LLD" { return "Start from operations and constraints, then pick data structures that preserve per-operation invariants." }
        default { return "Use brute force to expose repeated work, then choose the invariant and data structure." }
    }
}

function Get-CodeIdea {
    param([string] $Category)

    switch ($Category) {
        "HashMap/HashSet" { return "Maintain the map/set while scanning; check before or after insert based on reuse rules." }
        "Two Pointers" { return "Initialize pointers, compare current state, move the pointer whose movement is justified." }
        "Sliding Window" { return "Move right to include, move left while invalid or while answer can improve." }
        "Prefix/Suffix" { return "Build prefix/suffix arrays or running aggregates, then combine in O(1) per query/index." }
        "Binary Search" { return "Define left/right and predicate; update the boundary without losing the answer." }
        "Linked List" { return "Use dummy when head can change; update prev/current/next in a fixed order." }
        "Tree BFS" { return "Queue root, loop by level size, push children, collect per-level result." }
        "Tree DFS" { return "Base case null, recurse left/right, compute local result, return contract." }
        "Graph BFS" { return "Queue start states, mark visited immediately, expand valid neighbors by level." }
        "Graph DFS" { return "Mark visited, recursively explore neighbors, carry parent/state when cycles matter." }
        "Stack" { return "While top is resolved by current value, pop and compute; then push current." }
        "Heap" { return "Push candidates with comparator; poll when size or frontier rules require it." }
        "Intervals/Greedy" { return "Sort by start/end, then merge/count/select with one pass or heap." }
        "Backtracking" { return "Loop candidates, choose, recurse, undo, and skip duplicates/prune invalid paths." }
        "Trie" { return "Insert words by characters; search follows children and DFS branches on wildcard/board." }
        "Dynamic Programming" { return "Initialize base states, fill states in dependency order, return target state." }
        "Union Find" { return "Initialize parent/rank, find with compression, union by rank/size." }
        "Topological Sort" { return "Build graph and indegrees, queue zero-indegree nodes, process order." }
        "Greedy" { return "Sort or scan to make the safe local choice repeatedly." }
        "Math/Bit/String" { return "Track the exact numeric/string invariant and update it in constant or linear time." }
        "Design/LLD" { return "Implement operations around maps, lists, queues, heaps, or tries with clear invariants." }
        default { return "Code the invariant directly, then dry-run edge cases." }
    }
}

function Escape-Md {
    param([string] $Value)
    if ($null -eq $Value) { return "" }
    return $Value.Replace("|", "\|")
}

function New-Link {
    param(
        [string] $Text,
        [string] $Href
    )
    return "[$Text]($Href)"
}

function Get-LeetCodeSlugs {
    param([string] $SourcePath)

    if (-not (Test-Path -LiteralPath $SourcePath)) {
        return @()
    }
    $content = Get-Content -Raw -LiteralPath $SourcePath
    $matches = [regex]::Matches($content, "leetcode\.com/problems/([A-Za-z0-9-]+)")
    return @($matches | ForEach-Object { $_.Groups[1].Value.Trim().ToLowerInvariant() } | Where-Object { $_ } | Select-Object -Unique)
}

function Get-ExcludedSlugsForFile {
    param([string] $RelativeFile)

    $fileKey = $RelativeFile.Replace("\", "/").ToLowerInvariant()
    switch ($fileKey) {
        "design/lld/designurlshortner.java" { return @("two-sum") }
        default { return @() }
    }
}

function Get-IndexRows {
    param(
        [string] $RepoRoot,
        [string] $IndexPath
    )

    $rows = New-Object System.Collections.Generic.List[object]
    $pattern = '^\|\s*`([^`]+\.java)`\s*\|\s*([^|]+?)\s*\|\s*([ABC])\s*\|'
    foreach ($line in Get-Content -LiteralPath $IndexPath) {
        $match = [regex]::Match($line, $pattern)
        if (-not $match.Success) { continue }

        $relativeFile = $match.Groups[1].Value.Trim()
        if ($relativeFile -in @("Main.java", "CheatSheet.java")) { continue }

        $normalized = $relativeFile.Replace("/", "\")
        $sourcePath = Join-Path $RepoRoot ("src\main\java\org\chijai\" + $normalized)
        $fileTitle = ConvertTo-DisplayTitle $relativeFile
        $patternName = $match.Groups[2].Value.Trim()
        $priority = $match.Groups[3].Value.Trim()
        $category = Get-Category -Pattern $patternName -File $relativeFile -Title $fileTitle
        $excludedSlugs = @(Get-ExcludedSlugsForFile -RelativeFile $relativeFile)
        $slugs = @(Get-LeetCodeSlugs -SourcePath $sourcePath | Where-Object { $_ -notin $excludedSlugs })

        if ($slugs.Count -eq 0) {
            $importanceWeight = Get-ProblemImportanceWeight -Title $fileTitle -Category $category -Pattern $patternName
            $priorityWeight = Get-PriorityWeight $priority
            $categoryWeight = Get-CategoryWeight $category
            $rows.Add([pscustomobject]@{
                Title = $fileTitle
                Slug = ""
                File = $relativeFile
                Pattern = $patternName
                Category = $category
                Priority = $priority
                JavaLink = "../../src/main/java/org/chijai/" + $relativeFile.Replace("\", "/")
                LeetCodeLink = ""
                SourceExists = Test-Path -LiteralPath $sourcePath
                MatchScore = 0
                PriorityWeight = $priorityWeight
                ImportanceWeight = $importanceWeight
                CategoryWeight = $categoryWeight
                SortKey = ($importanceWeight * 10000000) + ($categoryWeight * 1000) + $priorityWeight
            })
            continue
        }

        foreach ($slug in $slugs) {
            $title = ConvertTo-TitleFromSlug $slug
            $rowCategory = Get-Category -Pattern $patternName -File $relativeFile -Title $title
            $importanceWeight = Get-ProblemImportanceWeight -Title $title -Category $rowCategory -Pattern $patternName
            $priorityWeight = Get-PriorityWeight $priority
            $categoryWeight = Get-CategoryWeight $rowCategory
            $rows.Add([pscustomobject]@{
                Title = $title
                Slug = $slug
                File = $relativeFile
                Pattern = $patternName
                Category = $rowCategory
                Priority = $priority
                JavaLink = "../../src/main/java/org/chijai/" + $relativeFile.Replace("\", "/")
                LeetCodeLink = "https://leetcode.com/problems/$slug/"
                SourceExists = Test-Path -LiteralPath $sourcePath
                MatchScore = Get-LinkMatchScore -RelativeFile $relativeFile -Title $title -Slug $slug
                PriorityWeight = $priorityWeight
                ImportanceWeight = $importanceWeight
                CategoryWeight = $categoryWeight
                SortKey = ($importanceWeight * 10000000) + ($categoryWeight * 1000) + $priorityWeight
            })
        }
    }

    $deduped = New-Object System.Collections.Generic.List[object]
    $seen = @{}
    foreach ($row in ($rows | Sort-Object ImportanceWeight, CategoryWeight, PriorityWeight, MatchScore, File, Title)) {
        $key = if ($row.Slug) { "LC:" + $row.Slug } else { "LOCAL:" + $row.File }
        if ($seen.ContainsKey($key)) { continue }
        $seen[$key] = $true
        $deduped.Add($row)
    }

    $rank = 1
    foreach ($row in ($deduped | Sort-Object ImportanceWeight, CategoryWeight, PriorityWeight, MatchScore, File, Title | Select-Object -First $MaxRows)) {
        Add-Member -InputObject $row -NotePropertyName Rank -NotePropertyValue $rank
        Add-Member -InputObject $row -NotePropertyName MustLevel -NotePropertyValue (Get-MustLevel -Rank $rank)
        $override = Get-ProblemOverride -Title $row.Title
        if ($null -ne $override) {
            Add-Member -InputObject $row -NotePropertyName Recall -NotePropertyValue $override.recall
            Add-Member -InputObject $row -NotePropertyName InterviewHook -NotePropertyValue $override.hook
            Add-Member -InputObject $row -NotePropertyName CodeIdea -NotePropertyValue $override.code
        } else {
            Add-Member -InputObject $row -NotePropertyName Recall -NotePropertyValue (Get-Recall -Category $row.Category -Pattern $row.Pattern -Title $row.Title)
            Add-Member -InputObject $row -NotePropertyName InterviewHook -NotePropertyValue (Get-InterviewHook -Category $row.Category -Pattern $row.Pattern -Title $row.Title)
            Add-Member -InputObject $row -NotePropertyName CodeIdea -NotePropertyValue (Get-CodeIdea -Category $row.Category)
        }
        $rank++
    }

    return @($deduped | Sort-Object Rank)
}

function Write-TextFile {
    param(
        [string] $Path,
        [string] $Content
    )

    $parent = Split-Path -Parent $Path
    if (-not (Test-Path -LiteralPath $parent)) {
        New-Item -ItemType Directory -Path $parent | Out-Null
    }
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $Content, $utf8NoBom)
}

function ConvertTo-FileSlug {
    param([string] $Value)

    $slug = $Value.ToLowerInvariant() -replace '[^a-z0-9]+', '_'
    $slug = $slug.Trim("_")
    if ([string]::IsNullOrWhiteSpace($slug)) {
        return "misc"
    }
    return $slug
}

function Get-PatternGroups {
    param([object[]] $Rows)

    $groups = @($Rows | Group-Object Category | ForEach-Object {
        $items = @($_.Group | Sort-Object Rank)
        [pscustomobject]@{
            Category = $_.Name
            DisplayCategory = Get-DisplayCategory $_.Name
            Count = $items.Count
            FirstRank = ($items | Select-Object -First 1).Rank
            Phase1 = @($items | Where-Object { $_.MustLevel -eq "Phase 1 - No Red Flags" }).Count
            Phase2 = @($items | Where-Object { $_.MustLevel -eq "Phase 2 - Strong Core" }).Count
            Phase3 = @($items | Where-Object { $_.MustLevel -eq "Phase 3 - Important" }).Count
            Later = @($items | Where-Object { $_.MustLevel -in @("Phase 4 - Secondary", "Phase 5 - If Time") }).Count
            Items = $items
        }
    } | Sort-Object FirstRank, Category)

    $index = 1
    foreach ($group in $groups) {
        $fileName = "{0:D2}_{1}.md" -f $index, (ConvertTo-FileSlug $group.Category)
        Add-Member -InputObject $group -NotePropertyName FileName -NotePropertyValue $fileName
        $index++
    }

    return @($groups)
}

function Build-Readme {
    param([object[]] $Rows)

    $total = $Rows.Count
    $content = @'
# DSA Interview Cockpit

This folder is the near-interview view over the existing Java chapters.

Source of truth remains `src/main/java/org/chijai`. These files link back to the Java chapters and to LeetCode where a link exists.

## What To Open

| Time available | Open this | Goal |
|---|---|---|
| 10 minutes before Zoom | `../notes/PRE_ZOOM_INTERVIEW_RAM_CACHE.md` | Warm up the solve script and blunder guard. |
| 2 hours | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover the top 20 no-red-flag problems. |
| 1 day | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover top 40 plus weak recall. |
| 2 days | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover top 60 with implementation drills. |
| 1 week | `04_TWO_DAY_AND_SEVEN_DAY_PLANS.md` | Cover the full Priority A/B path. |
| Need one master list | `01_ZERO_TO_HERO_RANKED_TABLE.md` | Ranked all-problem table with Java and LeetCode links. |
| Need fast memory refresh | `02_ONE_LINE_RECALL_ALL_PROBLEMS.md` | One sentence per problem in rank order. |
| Need speaking practice | `03_CRISP_INTERVIEW_ANSWERS.md` | Brute force -> bottleneck -> pattern -> invariant -> code -> dry run. |
| Need pattern-only focus | `patterns/README.md` | One file per pattern/category, still ordered by the current heuristic. |
| Need ranking reality check | `05_RANKING_METHODOLOGY_AND_AUDIT.md` | What is objective, what is heuristic, and where ranks can be wrong. |

## Current Coverage

- Ranked entries: __TOTAL__
- Pattern files: __PATTERN_COUNT__
- Ranking source: `../notes/PROBLEM_PATTERN_INDEX.md` plus LeetCode links found in Java chapters.
- Ranking philosophy: transparent interview triage. Use phase bands more than exact rank numbers.
- Ranking audit: `05_RANKING_METHODOLOGY_AND_AUDIT.md`.

## Interview Rule

For every problem, expose the thought process:

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

Do not start by trying to remember the final code.
'@
    $patternCount = @(Get-PatternGroups -Rows $Rows).Count
    return $content.Replace("__TOTAL__", [string] $total).Replace("__PATTERN_COUNT__", [string] $patternCount)
}

function Build-PatternRecognition {
    $content = @'
# Pattern Recognition 80/20

Pattern name is only level 1. The real interview signal is whether you can derive the solution from constraints and invariants.

## Opening Script

Use this rhythm:

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

1. Let me restate the problem.
2. What are the constraints and edge cases?
3. A brute-force way is...
4. The bottleneck is...
5. This looks like [pattern] because...
6. The invariant/state is...
7. I will code that, then dry-run.

## Core Pattern Selector

| Signal | Pattern | Why |
|---|---|---|
| Contiguous array/string | Sliding Window | Fixed or variable contiguous region with maintainable condition. |
| Pair, ends, sorted, palindrome | Two Pointers | Search space can shrink from one or both ends. |
| Repeated range/subarray aggregate | Prefix Sum / Prefix-Suffix | Precompute cumulative information. |
| Monotonic search space | Binary Search / Answer Search | If X works, all larger or smaller X also work. |
| Tree/graph path/component exploration | DFS | Explore deeply and define recursive state. |
| Minimum steps or levels | BFS | Unweighted shortest path or layer expansion. |
| Connectivity/component merging | Union Find / DSU | Maintain dynamic components cheaply. |
| Dependencies/order | Topological Sort | Process prerequisites before dependents. |
| Repeated states plus choices | Dynamic Programming | State, transition, base case. |
| Locally best safe choice | Greedy / Proof-Based Choice | Only valid when local choice is globally safe. |
| Generate/try/undo | Backtracking | Decision tree with constraints and pruning. |
| Top K, next best, stream priority | Heap / Priority Queue | Priority-based frontier. |
| Fast lookup, frequency, complement | HashMap / Frequency / Set | O(1) lookup, counting, caching. |
| Prefix/dictionary search | Trie | Shared prefixes. |
| Range query plus updates | Segment Tree | Fast range aggregation with mutation. |

## Force These Questions

1. What is the brute force?
2. What work is being repeated?
3. What property can I exploit?
4. What state must I maintain?
5. What is the invariant?
6. Which data structure maintains it cheaply?
7. Why is the algorithm correct?
8. Time and space?
9. What change would break this approach?

## No-Red-Flag Defaults

- Minimum moves in unweighted graph: BFS first.
- Contiguous substring/subarray: sliding window or prefix sum first.
- Sorted or answer-feasibility range: binary search first.
- Tree problem: define DFS helper return value before coding.
- Linked list: name pointers and save `next` before rewiring.
- DP: never code before stating `dp[...]` meaning.
- Greedy: do not use it unless you can justify the local choice.
'@
    return $content
}

function Build-RankedTable {
    param([object[]] $Rows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Zero To Hero Ranked Table")
    $lines.Add("")
    $lines.Add("Use this as the crunch-time order. Start at rank 1 and go down until time runs out.")
    $lines.Add("")
    $lines.Add("This is an interview-ROI order, not a universal algorithm curriculum order.")
    $lines.Add("")
    $lines.Add("For the scoring model and limitations, read [Ranking Methodology And Audit](05_RANKING_METHODOLOGY_AND_AUDIT.md).")
    $lines.Add("")
    $currentPhase = ""
    foreach ($row in $Rows) {
        if ($row.MustLevel -ne $currentPhase) {
            if ($currentPhase) {
                $lines.Add("")
            }
            $currentPhase = $row.MustLevel
            $lines.Add("## $currentPhase")
            $lines.Add("")
            $lines.Add((Get-PhaseSummary -Phase $currentPhase))
            $lines.Add("")
            $lines.Add("| Rank | Problem | Java | LeetCode | One-line recall | Interview hook |")
            $lines.Add("|---:|---|---|---|---|---|")
        }
        $java = New-Link "Java" $row.JavaLink
        $lc = if ($row.LeetCodeLink) { New-Link "LC" $row.LeetCodeLink } else { "-" }
        $problem = Escape-Md $row.Title
        $line = "| $($row.Rank) | $problem | $java | $lc | $(Escape-Md $row.Recall) | $(Escape-Md $row.InterviewHook) |"
        $lines.Add($line)
    }
    return ($lines -join "`r`n")
}

function Build-OneLineRecall {
    param([object[]] $Rows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# One-Line Recall All Problems")
    $lines.Add("")
    $lines.Add("Read this when you need a fast RAM warmup. The order matches the ranked table.")
    $lines.Add("")
    $current = ""
    foreach ($row in $Rows) {
        if ($row.MustLevel -ne $current) {
            $current = $row.MustLevel
            $lines.Add("")
            $lines.Add("## $current")
            $lines.Add("")
            $lines.Add((Get-PhaseSummary -Phase $current))
            $lines.Add("")
        }
        $lc = if ($row.LeetCodeLink) { " | " + (New-Link "LC" $row.LeetCodeLink) } else { "" }
        $java = New-Link "Java" $row.JavaLink
        $lines.Add("$($row.Rank). **$(Escape-Md $row.Title)** - $(Escape-Md $row.Recall) ($java$lc)")
    }
    return ($lines -join "`r`n")
}

function Build-CrispAnswers {
    param([object[]] $Rows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Crisp Interview Answers")
    $lines.Add("")
    $lines.Add("Practice speaking these in the interview rhythm.")
    $lines.Add("")
    $lines.Add("~~~text")
    $lines.Add("brute force -> bottleneck -> pattern -> invariant -> code -> dry run")
    $lines.Add("~~~")
    $currentPhase = ""
    foreach ($row in $Rows) {
        if ($row.MustLevel -ne $currentPhase) {
            $currentPhase = $row.MustLevel
            $lines.Add("")
            $lines.Add("## $currentPhase")
            $lines.Add("")
            $lines.Add((Get-PhaseSummary -Phase $currentPhase))
        }
        $java = New-Link "Java" $row.JavaLink
        $lc = if ($row.LeetCodeLink) { " | " + (New-Link "LeetCode" $row.LeetCodeLink) } else { "" }
        $lines.Add("")
        $lines.Add("### $($row.Rank). $(Escape-Md $row.Title)")
        $lines.Add("")
        $lines.Add("- Links: $java$lc")
        $lines.Add("- Brute force: Try all candidate states or combinations directly.")
        $lines.Add("- Bottleneck: Repeated work appears as rescanning, recomputing, or revisiting state.")
        $lines.Add("- Pattern: $(Get-DisplayCategory $row.Category), using $($row.Pattern).")
        $lines.Add("- Invariant/state: $($row.Recall)")
        $lines.Add("- Code idea: $($row.CodeIdea)")
        $lines.Add("- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.")
    }
    return ($lines -join "`r`n")
}

function Build-Plans {
    param([object[]] $Rows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Two-Day And Seven-Day Plans")
    $lines.Add("")
    $ranked = New-Link "Zero To Hero Ranked Table" "01_ZERO_TO_HERO_RANKED_TABLE.md"
    $recall = New-Link "One-Line Recall" "02_ONE_LINE_RECALL_ALL_PROBLEMS.md"
    $answers = New-Link "Crisp Interview Answers" "03_CRISP_INTERVIEW_ANSWERS.md"
    $patterns = New-Link "Pattern Files" "patterns/README.md"
    $preZoom = New-Link "Pre-Zoom RAM Cache" "../notes/PRE_ZOOM_INTERVIEW_RAM_CACHE.md"
    $lines.Add("Use this when a company asks for a DSA round soon. The goal is interview triage: remove red flags first, then expand coverage by rank.")
    $lines.Add("")
    $lines.Add("Core files: $ranked, $recall, $answers, $patterns, $preZoom.")
    $lines.Add("")
    $lines.Add("## If You Have 2 Hours")
    $lines.Add("")
    $lines.Add('- Read `00_PATTERN_RECOGNITION_80_20.md` once.')
    $lines.Add("- Speak ranks 1-20 from $recall without opening Java.")
    $lines.Add("- Code 2 problems from blank: one HashMap/Two Pointers/Sliding Window, one Linked List/Tree.")
    $lines.Add("- Read $preZoom in the last 10 minutes.")
    $lines.Add("")
    $lines.Add("## If You Have 1 Day")
    $lines.Add("")
    $lines.Add("- Cover Phase 1 from ${ranked}: ranks 1-30.")
    $lines.Add("- Skim ranks 31-40 if Phase 1 recall is stable.")
    $lines.Add("- Implement 4 problems from blank: one HashMap/Two Pointers, one Sliding Window, one Linked List, one Tree/Graph.")
    $lines.Add("- For each unsolved problem, speak the crisp answer instead of rereading code.")
    $lines.Add("")
    $lines.Add("## If You Have 2 Days")
    $lines.Add("")
    $lines.Add("- Day 1: Phase 1, ranks 1-30. Code 4 problems from blank.")
    $lines.Add("- Day 2: Phase 2, ranks 31-70. Code 3 problems from blank, then review all one-line recalls.")
    $lines.Add("- Mark every miss as again or hard in the review system.")
    $lines.Add("")
    $lines.Add("## If You Have 1 Week")
    $lines.Add("")
    $lines.Add("- Days 1-2: Phase 1, then code weak items from blank.")
    $lines.Add("- Days 3-4: Phase 2, then code weak items from blank.")
    $lines.Add("- Day 5: Phase 3 plus weakest pattern file.")
    $lines.Add("- Day 6: mock interview, two random Priority A/B drills.")
    $lines.Add("- Day 7: pre-Zoom cache, one-line recall, and no-blunder review.")
    $lines.Add("")
    $lines.Add("## What Not To Do")
    $lines.Add("")
    $lines.Add("- Do not reread Java first. Speak the approach before opening code.")
    $lines.Add("- Do not jump to DP/Trie/Union-Find before Phase 1 and Phase 2 are stable.")
    $lines.Add("- Do not treat the ranking as universal truth. It is an interview-ROI order for fast prep.")
    $lines.Add('- Do not keep a miss invisible. Mark it `again` or `hard` and revisit it.')
    return ($lines -join "`r`n")
}

function Build-RankingAudit {
    param(
        [object[]] $Rows,
        [object[]] $Groups
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $ranked = New-Link "Zero To Hero Ranked Table" "01_ZERO_TO_HERO_RANKED_TABLE.md"
    $index = New-Link "Problem Pattern Index" "../notes/PROBLEM_PATTERN_INDEX.md"
    $leetcodeCount = @($Rows | Where-Object { $_.LeetCodeLink }).Count
    $localOnlyCount = @($Rows | Where-Object { -not $_.LeetCodeLink }).Count
    $missingSourceCount = @($Rows | Where-Object { -not $_.SourceExists }).Count
    $patternRowCount = ($Groups | Measure-Object -Property Count -Sum).Sum
    $phase1 = @($Rows | Where-Object { $_.Rank -le 30 })
    $top40 = @($Rows | Where-Object { $_.Rank -le 40 })
    $top70 = @($Rows | Where-Object { $_.Rank -le 70 })
    $phase1RoleSpecificCount = @($phase1 | Where-Object { $_.Category -in @("Design/LLD", "Math/Bit/String", "Core Basics") }).Count
    $top70DesignCount = @($top70 | Where-Object { $_.Category -eq "Design/LLD" }).Count
    $top40CategoryCount = @($top40 | Select-Object -ExpandProperty Category -Unique).Count
    $anchorTitles = @(
        "2Sum / 3Sum / 4Sum",
        "Binary Search",
        "Longest Substring Without Repeating Characters",
        "Minimum Window Substring",
        "Product Of Array Except Self",
        "Reverse Linked List",
        "Linked List Cycle",
        "Binary Tree Level Order Traversal",
        "Validate Binary Search Tree",
        "Number Of Islands",
        "Course Schedule Ii",
        "Word Ladder",
        "LRU Cache",
        "Top K Frequent Elements",
        "House Robber",
        "Coin Change",
        "Subsets"
    )
    $missingTop40Anchors = @()
    foreach ($anchorTitle in $anchorTitles) {
        $anchorRow = $Rows | Where-Object { $_.Title -eq $anchorTitle } | Select-Object -First 1
        if ($null -eq $anchorRow -or $anchorRow.Rank -gt 40) {
            $missingTop40Anchors += $anchorTitle
        }
    }

    $lines.Add("# Ranking Methodology And Audit")
    $lines.Add("")
    $lines.Add("Read this before treating $ranked as truth.")
    $lines.Add("")
    $lines.Add("## Verdict")
    $lines.Add("")
    $lines.Add("This ranking is not objectively correct in the mathematical sense. It is a transparent interview-ROI heuristic generated from the local repo.")
    $lines.Add("")
    $lines.Add("It is useful for crunch-time triage. It would be a scam if presented as a universal proof that rank 42 is objectively more important than rank 57.")
    $lines.Add("")
    $lines.Add("Use phase bands more than exact rank numbers:")
    $lines.Add("")
    $lines.Add("- Phase 1 beats Phase 2.")
    $lines.Add("- Phase 2 beats Phase 3.")
    $lines.Add("- Inside the same phase, your weak pattern or target company signal can override the exact rank.")
    $lines.Add("")
    $lines.Add("## What Is Objective")
    $lines.Add("")
    $lines.Add("| Check | Current result | Meaning |")
    $lines.Add("|---|---:|---|")
    $lines.Add("| Ranked rows generated | $($Rows.Count) | Rows came from $index and Java LeetCode links. |")
    $lines.Add("| Java source missing | $missingSourceCount | Should stay 0. |")
    $lines.Add("| LeetCode-linked rows | $leetcodeCount | Rows that open LeetCode directly. |")
    $lines.Add("| Local-only rows | $localOnlyCount | Repo-only or design rows without direct LeetCode source link. |")
    $lines.Add("| Pattern files generated | $($Groups.Count) | One focused view per generated category. |")
    $lines.Add("| Pattern rows covered | $patternRowCount | Should match ranked rows so no problem disappears from pattern files. |")
    $lines.Add("")
    $lines.Add("These are objective repository checks. They do not prove the ranking is globally correct.")
    $lines.Add("")
    $lines.Add("## Scoring Model")
    $lines.Add("")
    $lines.Add("The generator sorts rows by a per-problem interview-ROI weight first. Category ROI, source priority, and match confidence are tie-breakers only:")
    $lines.Add("")
    $lines.Add("~~~text")
    $lines.Add("SortKey = ImportanceWeight, then CategoryWeight, then PriorityWeight")
    $lines.Add("then MatchScore, File, Title")
    $lines.Add("~~~")
    $lines.Add("")
    $lines.Add("ImportanceWeight is hand-tuned in the generator for individual problems. That is the main answer to 'rank by individual problem ROI, not only by pattern or source chapter.'")
    $lines.Add("")
    $lines.Add("| Input | Weight | Meaning |")
    $lines.Add("|---|---:|---|")
    $lines.Add("| Priority A | 0 | Master first from the source index. |")
    $lines.Add("| Priority B | 1000 | Stabilize after Priority A. |")
    $lines.Add("| Priority C | 2000 | Review after core is stable. |")
    $lines.Add("")
    $lines.Add("Problem ROI tiers currently used:")
    $lines.Add("")
    $lines.Add("| Importance weight | Meaning |")
    $lines.Add("|---:|---|")
    $lines.Add("| 0 | Core no-red-flag interview staples. |")
    $lines.Add("| 15 | Very common and still high-value. |")
    $lines.Add("| 35 | Strong secondary problems once the core is stable. |")
    $lines.Add("| 55 | Useful breadth, but not first-pass mandatory. |")
    $lines.Add("| 80+ | Low-priority or role-specific for general DSA prep. |")
    $lines.Add("")
    $lines.Add("## Top-Band Policy")
    $lines.Add("")
    $lines.Add("The first pass is designed to reduce interviewer red flags, not to teach algorithms in textbook order.")
    $lines.Add("")
    $lines.Add("| Band | Purpose | Examples |")
    $lines.Add("|---|---|---|")
    $lines.Add("| Ranks 1-20 | No-red-flag staples and high-signal patterns | Binary Search, Anagram, Sliding Window, Linked List Cycle, Tree BFS/DFS, Islands, Course Schedule |")
    $lines.Add("| Ranks 21-40 | Common follow-ups and must-know implementation drills | Rotated Search, LRU, Copy Random List, Rotting Oranges, Heap, DP baseline, Backtracking baseline |")
    $lines.Add("| Ranks 41-70 | Strong second pass after the core is stable | Monotonic Stack, more Sliding Window, List variants, Tree variants, Binary Search on answer |")
    $lines.Add("| Ranks 71+ | Breadth, variants, and role/company-specific extras | Advanced DP, Trie variants, Union-Find, design-style local rows |")
    $lines.Add("")
    $lines.Add("## Ranking QA Gates")
    $lines.Add("")
    $lines.Add("These checks make the ranking less scammy by catching obvious placement mistakes.")
    $lines.Add("")
    $lines.Add("| Gate | Result | Why it matters |")
    $lines.Add("|---|---|---|")
    $lines.Add("| Problem ROI sorts before category/source | PASS | Individual problem importance is the first ranking signal. |")
    $lines.Add("| Java links resolve | $(if ($missingSourceCount -eq 0) { "PASS" } else { "FAIL: $missingSourceCount missing" }) | A review row must open its real Java source. |")
    $lines.Add("| Pattern files cover ranked rows | $(if ($patternRowCount -eq $Rows.Count) { "PASS" } else { "FAIL: $patternRowCount of $($Rows.Count)" }) | Pattern-specific review must not drop problems. |")
    $lines.Add("| Phase 1 avoids role-specific/design rows | $(if ($phase1RoleSpecificCount -eq 0) { "PASS" } else { "CHECK: $phase1RoleSpecificCount rows" }) | The first 30 should remove broad DSA red flags, not niche extras. |")
    $lines.Add("| Top 40 has broad pattern coverage | $(if ($top40CategoryCount -ge 12) { "PASS: $top40CategoryCount categories" } else { "CHECK: $top40CategoryCount categories" }) | Early prep should not be trapped inside one pattern family. |")
    $lines.Add("| Top 40 contains core anchor problems | $(if ($missingTop40Anchors.Count -eq 0) { "PASS" } else { "CHECK missing: $($missingTop40Anchors -join ", ")" }) | The obvious high-ROI anchors should not drift late. |")
    $lines.Add("| Design rows deferred from top 70 | $(if ($top70DesignCount -eq 0) { "PASS" } else { "CHECK: $top70DesignCount rows" }) | Design-flavored rows are useful, but not first-pass DSA ROI. |")
    $lines.Add("")
    $lines.Add("Category weights currently used:")
    $lines.Add("")
    $lines.Add("| Weight | Category | Rationale |")
    $lines.Add("|---:|---|---|")
    $lines.Add("| 10 | HashMap / Frequency / Set | Low implementation cost, high red-flag risk if missed. |")
    $lines.Add("| 20 | Two Pointers | Common pair/string/array interview pattern. |")
    $lines.Add("| 30 | Sliding Window | High ROI for contiguous array/string problems. |")
    $lines.Add("| 40 | Prefix Sum / Prefix-Suffix | Frequent repeated-range optimization. |")
    $lines.Add("| 50 | Linked List Pointers | Low theory, high bug-risk in interviews. |")
    $lines.Add("| 60 | Tree BFS / Level Order | Core tree traversal and level logic. |")
    $lines.Add("| 70 | Tree DFS / Recursion | Core recursive return contracts and tree invariants. |")
    $lines.Add("| 80 | Graph BFS / Shortest Path | Minimum-step and level-expansion problems. |")
    $lines.Add("| 90 | Graph DFS / Components | Components, visited state, path exploration. |")
    $lines.Add("| 100 | Binary Search / Answer Search | Important, but usually easier to recover once invariant is known. |")
    $lines.Add("| 110 | Stack / Monotonic Stack | Parentheses, monotonic stack, deque-like candidate maintenance. |")
    $lines.Add("| 120 | Heap / Priority Queue | Top-K, stream, and frontier problems. |")
    $lines.Add("| 130+ | Remaining categories | Useful breadth after the core signal is reliable. |")
    $lines.Add("")
    $lines.Add("## Why It Can Feel Off")
    $lines.Add("")
    $lines.Add("- A Java chapter can contain many LeetCode links; source priority is only a tie-breaker after problem ROI and category ROI.")
    $lines.Add("- Importance weights are curated heuristics, not measured company frequency data.")
    $lines.Add("- Exact rank inside one phase is weaker than the phase itself.")
    $lines.Add("- The ranking is not trained on company-specific interview data.")
    $lines.Add("- Some rows need problem-specific hooks; generic pattern text is only a fallback.")
    $lines.Add("- If the target company emphasizes DP, graphs, or tries, manually promote that pattern for that week.")
    $lines.Add("")
    $lines.Add("## Current Anti-Scam Rule")
    $lines.Add("")
    $lines.Add("Say this: 'This is my local interview triage order based on repo priorities, pattern ROI, and no-red-flag risk.'")
    $lines.Add("")
    $lines.Add("Do not say this: 'This is the objectively correct global ranking of DSA problems.'")
    $lines.Add("")
    $lines.Add("## Practical Use")
    $lines.Add("")
    $lines.Add("For a 2-hour or 1-day crunch, follow Phase 1 in order.")
    $lines.Add("")
    $lines.Add("For a 2-day crunch, follow Phase 1, then Phase 2, but swap in your weakest pattern if it is already known.")
    $lines.Add("")
    $lines.Add("For a 1-week prep, use the rank order for coverage and the pattern files for targeted repair.")
    return ($lines -join "`r`n")
}

function Build-PatternIndex {
    param(
        [object[]] $Rows,
        [object[]] $Groups
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Pattern Files")
    $lines.Add("")
    $lines.Add("Use these when you know the weak pattern and want a focused pass without losing the current global order.")
    $lines.Add("")
    $lines.Add("Recommended flow: read the pattern signal, speak the top rows without code, then implement one missed problem from blank.")
    $lines.Add("")
    $lines.Add("| Order | Pattern | Problems | First rank | Phase 1 | Phase 2 | Phase 3 | Later | File |")
    $lines.Add("|---:|---|---:|---:|---:|---:|---:|---:|---|")

    $order = 1
    foreach ($group in $Groups) {
        $file = New-Link $group.FileName $group.FileName
        $lines.Add("| $order | $(Escape-Md $group.DisplayCategory) | $($group.Count) | $($group.FirstRank) | $($group.Phase1) | $($group.Phase2) | $($group.Phase3) | $($group.Later) | $file |")
        $order++
    }

    $lines.Add("")
    $lines.Add("Total ranked entries: $($Rows.Count)")
    return ($lines -join "`r`n")
}

function Build-PatternFile {
    param([object] $Group)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# $($Group.DisplayCategory)")
    $lines.Add("")
    $lines.Add("Focused pattern pass. Keep the global rank order inside this file; lower rank means a higher score in the current interview-ROI heuristic.")
    $lines.Add("")
    $lines.Add("## Recognition Signal")
    $lines.Add("")
    $lines.Add((Get-Recall -Category $Group.Category -Pattern "" -Title ""))
    $lines.Add("")
    $lines.Add("## Interview Move")
    $lines.Add("")
    $lines.Add((Get-InterviewHook -Category $Group.Category -Pattern "" -Title ""))
    $lines.Add("")
    $lines.Add("## Problems")
    $lines.Add("")
    $lines.Add("| Global Rank | Phase | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |")
    $lines.Add("|---:|---|---|---|---|---|---|---|")

    foreach ($row in $Group.Items) {
        $java = New-Link "Java" ("../" + $row.JavaLink)
        $lc = if ($row.LeetCodeLink) { New-Link "LC" $row.LeetCodeLink } else { "-" }
        $line = "| $($row.Rank) | $($row.MustLevel) | $(Escape-Md $row.Title) | $(Escape-Md $row.Pattern) | $java | $lc | $(Escape-Md $row.Recall) | $(Escape-Md $row.CodeIdea) |"
        $lines.Add($line)
    }

    $lines.Add("")
    $lines.Add("## Drill")
    $lines.Add("")
    $lines.Add("1. Read only the problem title.")
    $lines.Add("2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.")
    $lines.Add("3. Open Java only after the spoken answer is complete.")
    $lines.Add("4. Code one missed problem from blank before moving to another pattern.")
    return ($lines -join "`r`n")
}

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$indexPath = Join-Path $repoRoot "dsa-review\notes\PROBLEM_PATTERN_INDEX.md"
$outDir = Join-Path $repoRoot "dsa-review\interview"

if (-not (Test-Path -LiteralPath $indexPath)) {
    throw "Problem index not found: $indexPath"
}

$rows = @(Get-IndexRows -RepoRoot $repoRoot -IndexPath $indexPath)
if ($rows.Count -eq 0) {
    throw "No rows generated from $indexPath"
}

$patternGroups = @(Get-PatternGroups -Rows $rows)

Write-TextFile -Path (Join-Path $outDir "README.md") -Content (Build-Readme -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "00_PATTERN_RECOGNITION_80_20.md") -Content (Build-PatternRecognition)
Write-TextFile -Path (Join-Path $outDir "01_ZERO_TO_HERO_RANKED_TABLE.md") -Content (Build-RankedTable -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "02_ONE_LINE_RECALL_ALL_PROBLEMS.md") -Content (Build-OneLineRecall -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "03_CRISP_INTERVIEW_ANSWERS.md") -Content (Build-CrispAnswers -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "04_TWO_DAY_AND_SEVEN_DAY_PLANS.md") -Content (Build-Plans -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "05_RANKING_METHODOLOGY_AND_AUDIT.md") -Content (Build-RankingAudit -Rows $rows -Groups $patternGroups)

$patternDir = Join-Path $outDir "patterns"
if (-not (Test-Path -LiteralPath $patternDir)) {
    New-Item -ItemType Directory -Path $patternDir | Out-Null
}
Get-ChildItem -LiteralPath $patternDir -File -Filter "*.md" | Remove-Item -Force
Write-TextFile -Path (Join-Path $patternDir "README.md") -Content (Build-PatternIndex -Rows $rows -Groups $patternGroups)
foreach ($group in $patternGroups) {
    Write-TextFile -Path (Join-Path $patternDir $group.FileName) -Content (Build-PatternFile -Group $group)
}

[pscustomobject]@{
    repoRoot = $repoRoot
    output = $outDir
    rankedEntries = $rows.Count
    leetcodeLinks = @($rows | Where-Object { $_.LeetCodeLink }).Count
    localOnlyEntries = @($rows | Where-Object { -not $_.LeetCodeLink }).Count
    patternFiles = $patternGroups.Count
} | Format-List
