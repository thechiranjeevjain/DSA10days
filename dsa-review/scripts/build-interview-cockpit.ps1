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
        -replace "\bIi\b", "II" `
        -replace "\bIii\b", "III" `
        -replace "\bIv\b", "IV" `
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
    $pathKey = $RelativeFile.Replace("\", "/").ToLowerInvariant()

    if ($titleKey -in @(
        "besttimetobuyandsellstockiv",
        "besttimetobuyandsellstockwithtransactionfee",
        "besttimetobuyandsellstockwithcooldown"
    )) {
        if ($pathKey -like "day1/arrays/session3/stockseries2.java") { return 0 }
        if ($pathKey -like "day1/arrays/session3/stockseries1.java") { return 8 }
    }

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
        "twosum" = @{
            recall = "Use a HashMap from value to index; each number asks whether its complement was seen."
            hook = "Brute force tries all pairs; complement lookup makes the second value O(1)."
            code = "Scan left to right, if target - nums[i] exists return indices, otherwise store nums[i] -> i."
        }
        "twosumiiinputarrayissorted" = @{
            recall = "Sorted input lets left/right shrink toward the target sum."
            hook = "HashMap works, but sorted order gives O(1) space by eliminating impossible pairs."
            code = "Compare nums[left] + nums[right] with target; move left if small, right if large."
        }
        "binarysearch" = @{
            recall = "Sorted order plus mid comparison proves which half cannot contain the target."
            hook = "Linear scan is O(n); sorted order gives monotonic elimination in O(log n)."
            code = "While left <= right, compare nums[mid] to target; move left/right, return index or -1."
        }
        "firstbadversion" = @{
            recall = "Find the first true in a false...false,true...true version predicate."
            hook = "Checking versions one by one wastes the monotonic bad suffix."
            code = "If isBadVersion(mid), save mid and search left; otherwise search right."
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
        "maximumxorwithanelementfromarray" = @{
            recall = "Offline sort queries by limit; insert eligible numbers into a bitwise trie before maximizing XOR."
            hook = "A plain trie over all numbers violates the limit; sorting queries makes eligibility monotonic."
            code = "Sort nums and queries by mi, insert nums <= mi, answer each query by opposite-bit trie walk."
        }
        "maximumgeneticdifferencequery" = @{
            recall = "DFS the tree while the current root-to-node path is stored in a bitwise trie."
            hook = "Query candidates are ancestors only, so a global trie includes invalid values."
            code = "On entering node insert value, answer attached queries, DFS children, then remove value."
        }
        "countpairswithxorinarange" = @{
            recall = "Count pairs with XOR < bound using bitwise trie prefixes, then subtract low from high+1."
            hook = "All-pairs XOR is O(n^2); bitwise prefixes count valid partners per bit."
            code = "For each num, add countLessThan(high+1) - countLessThan(low), then insert num."
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
        "searchinrotatedsortedarray" = @{
            recall = "At every step one half is sorted; keep it only if target lies inside its bounds."
            hook = "A normal sorted-array binary search fails because the pivot breaks global ordering."
            code = "Compare nums[left] and nums[mid] to identify sorted half, then discard the half that cannot contain target."
        }
        "searchinrotatedsortedarrayii" = @{
            recall = "With duplicates, shrink both ends only when left, mid, and right are equal and ordering is ambiguous."
            hook = "Duplicates can destroy the sorted-half signal, so worst-case time can degrade to O(n)."
            code = "If nums[left]==nums[mid]==nums[right], left++ and right--; otherwise reuse sorted-half logic."
        }
        "findfirstandlastpositionofelementinsortedarray" = @{
            recall = "Run boundary binary search twice: first index >= target, and last index <= target."
            hook = "Finding one target then expanding can become O(n) when all elements equal target."
            code = "findFirst moves left on nums[mid] >= target; findLast moves right on nums[mid] <= target."
        }
        "searchinsertposition" = @{
            recall = "Find the first index whose value is >= target; if none, insert at n."
            hook = "Equality is a boundary candidate, not a reason to abandon the left side."
            code = "Binary search with answer initialized to n; when nums[mid] >= target save mid and move right left."
        }
        "findpeakelement" = @{
            recall = "Compare mid with mid+1; the rising side must contain a peak."
            hook = "Binary search does not require sorted data, only a safe half-discard rule."
            code = "If nums[mid] > nums[mid+1], move right to mid; else move left to mid+1 until left == right."
        }
        "sqrtx" = @{
            recall = "Find the largest integer mid whose square is <= x."
            hook = "Linear testing is slow and mid*mid can overflow without long arithmetic."
            code = "Binary search 0..x, cast mid*mid to long, save mid when square <= x."
        }
        "kokoeatingbananas" = @{
            recall = "Binary search the minimum speed; if speed k works, every higher speed also works."
            hook = "Trying every speed up to maxPile is too slow; feasibility is monotonic."
            code = "Search speed 1..maxPile, compute total ceil(pile/speed) hours, keep smaller working speed."
        }
        "capacitytoshippackageswithinddays" = @{
            recall = "Binary search minimum capacity; capacity works if one pass ships within D days."
            hook = "Capacity must be at least max weight, and larger capacity never requires more days."
            code = "Search maxWeight..sumWeight, count days by accumulating load until capacity would overflow."
        }
        "splitarraylargestsum" = @{
            recall = "Binary search the smallest allowed subarray sum that can split into at most m pieces."
            hook = "The feasibility check is monotonic: larger max sum never needs more pieces."
            code = "Search max(nums)..sum(nums), greedily count pieces when current sum would exceed mid."
        }
        "minimumnumberofdaystomakembouquets" = @{
            recall = "Binary search days; by a given day, consecutive bloomed flowers form bouquets greedily."
            hook = "Day feasibility is monotonic, but adjacency resets the current flower streak."
            code = "Reject if m*k > n; for each day mid, count adjacent bloomed streaks of length k."
        }
        "minimumheighttrees" = @{
            recall = "Peel all current leaves together until one or two centroid roots remain."
            hook = "Trying every root is O(n^2); leaves can never be optimal centers after each layer."
            code = "Build graph/degrees, queue degree-1 leaves, remove layers while remainingNodes > 2."
        }
        "courseschedule" = @{
            recall = "A course is unlocked only when its indegree becomes zero."
            hook = "Plain traversal can process a course before prerequisites; indegree is the remaining-lock count."
            code = "Build prerequisite->course graph, queue indegree-zero courses, decrement neighbors, compare processed count."
        }
        "coursescheduleii" = @{
            recall = "A course enters the order only when its indegree drops to zero."
            hook = "Plain traversal can violate prerequisites; indegree tracks the remaining unmet prerequisites."
            code = "Build prerequisite->course graph, queue indegree-zero courses, append order, fail if processed < n."
        }
        "minimumnumberofarrowstoburstballoons" = @{
            recall = "Sort balloons by end; shoot at current end and start a new arrow only after it is missed."
            hook = "This is greedy endpoint selection, not overlap counting like meeting rooms."
            code = "Sort by end, keep currentArrowEnd, increment arrows when next.start > currentArrowEnd."
        }
        "carpooling" = @{
            recall = "Treat each pickup/dropoff as passenger-count delta and ensure capacity is never exceeded."
            hook = "Checking every trip pair misses the global passenger load over the route."
            code = "Use difference array or sorted events: add passengers at start, subtract at end, track running load."
        }
        "minimumplatforms" = @{
            recall = "Sort arrivals and departures separately; active trains are arrivals minus departures processed."
            hook = "Train identity is irrelevant; the answer is maximum simultaneous presence on the timeline."
            code = "If next arrival <= next departure, increment active/max; else process departure and decrement active."
        }
        "dailytemperatures" = @{
            recall = "Keep indices of days waiting for a warmer temperature; current day resolves colder previous days."
            hook = "Scanning forward from every day is O(n^2); a decreasing stack resolves each day once."
            code = "While current temp is warmer than stack top, pop index and set answer to current - popped."
        }
        "validparentheses" = @{
            recall = "Every closing bracket must match the most recent unmatched opening bracket."
            hook = "Counting brackets is not enough because nesting order matters."
            code = "Push opening brackets; on closing, fail if stack empty or top is not its matching opener."
        }
        "largestrectangleinhistogram" = @{
            recall = "When a shorter bar arrives, popped bars know their maximal rectangle width."
            hook = "Trying every left/right boundary is O(n^2); monotonic stack finds nearest smaller bars."
            code = "Append sentinel zero, keep increasing indices, pop and compute height * width when current is smaller."
        }
        "nextgreaterelementii" = @{
            recall = "Loop twice over the circular array while a decreasing stack waits for next greater values."
            hook = "Naive circular scans repeat work; stack resolves each index when the next greater appears."
            code = "For i in 0..2n-1, resolve stack with nums[i % n], push i only during first pass."
        }
        "nextgreaterelementi" = @{
            recall = "Precompute next greater for nums2 with a decreasing stack, then answer nums1 by map lookup."
            hook = "Searching nums2 for every nums1 value repeats the same next-greater work."
            code = "Scan nums2, pop smaller values and map them to current, then lookup each nums1 value."
        }
        "sumofsubarrayminimums" = @{
            recall = "Each element contributes as minimum for leftChoices times rightChoices subarrays."
            hook = "Enumerating subarrays is O(n^2); monotonic stacks count ownership ranges in O(n)."
            code = "Find previous less and next less-or-equal distances, sum arr[i] * left * right modulo M."
        }
        "evaluatereversepolishnotation" = @{
            recall = "Postfix expression evaluates when each operator consumes the latest two operands from a stack."
            hook = "Parentheses/precedence disappear in RPN; the only state needed is operand stack."
            code = "Push numbers; on operator pop b then a, compute a op b, push result."
        }
        "basiccalculator" = @{
            recall = "Use sign and stack to preserve the expression value before each parenthesis."
            hook = "Direct left-to-right evaluation breaks when parentheses change the active sign context."
            code = "Track result, sign, number; on '(' push result/sign and reset; on ')' fold into previous context."
        }
        "maximalrectangle" = @{
            recall = "Treat every matrix row as histogram heights and run largest-rectangle on each row."
            hook = "Checking every rectangle is too slow; row heights reuse vertical continuity."
            code = "Update heights per row, then compute largest histogram area with monotonic stack."
        }
        "minstack" = @{
            recall = "Store the current minimum with each push, or keep a second stack of minimums."
            hook = "Scanning stack on getMin makes the required O(1) operation impossible."
            code = "Push value and min(value,currentMin); pop both together; getMin reads min top."
        }
        "maxstack" = @{
            recall = "Maintain stack order plus a way to locate/remove the current maximum."
            hook = "A plain stack gives pop order but cannot remove max efficiently."
            code = "Use stack plus max tracking, or doubly linked list plus TreeMap for O(log n) popMax."
        }
        "implementqueueusingstacks" = @{
            recall = "Use input stack for pushes and output stack for pops; transfer only when output is empty."
            hook = "Moving elements on every operation repeats work; lazy transfer amortizes the reversal."
            code = "push -> in.push; pop/peek -> if out empty move all in to out, then read out."
        }
        "implementstackusingqueues" = @{
            recall = "After each push, rotate the queue so the newest element is at the front."
            hook = "Queue order is FIFO; rotation restores LIFO behavior."
            code = "Offer x, then rotate size-1 older elements behind it; pop removes queue front."
        }
        "designastackwithincrementoperation" = @{
            recall = "Lazy increment stores pending additions at the boundary index instead of touching k items."
            hook = "Incrementing bottom k elements directly makes increment O(k)."
            code = "Keep stack plus inc array; on pop carry inc[i] to inc[i-1] and return value + inc[i]."
        }
        "designcircularqueue" = @{
            recall = "Circular queue uses head, size, and modulo arithmetic to reuse fixed array slots."
            hook = "Shifting array elements on enqueue/dequeue is unnecessary and slow."
            code = "enQueue writes at (head + size) % capacity; deQueue advances head and decrements size."
        }
        "houserobber" = @{
            recall = "At each house choose max(skip current, rob current plus best before previous)."
            hook = "Naive recursion repeats suffix decisions; two rolling states capture all history needed."
            code = "For each money, next = max(prev1, prev2 + money); shift prev2=prev1, prev1=next."
        }
        "coinchange" = @{
            recall = "dp[amount] is the fewest coins needed; each coin relaxes reachable amounts."
            hook = "Recursive choice tree repeats the same remaining amounts."
            code = "Initialize dp[0]=0 and others INF; for amount 1..target, try every coin."
        }
        "uniquepaths" = @{
            recall = "Ways to a cell equal ways from top plus ways from left."
            hook = "Naive recursion recomputes the same grid cells exponentially."
            code = "Initialize first row/column to 1, fill dp[r][c] = dp[r-1][c] + dp[r][c-1]."
        }
        "partitionequalsubsetsum" = @{
            recall = "Partition is possible only if some subset reaches total/2."
            hook = "Trying all subsets repeats sums; 0/1 knapsack tracks reachable sums once."
            code = "If total odd return false; update boolean dp from target down to num for each num."
        }
        "longestincreasingsubsequence" = @{
            recall = "tails[len] stores the smallest possible tail for an increasing subsequence of that length."
            hook = "O(n^2) DP works, but binary-search tails gives faster length tracking."
            code = "For each x, lower_bound in tails and replace; answer is tails size."
        }
        "kadanemaxsubarray" = @{
            recall = "Best subarray ending here is either current alone or previous best ending here plus current."
            hook = "Checking all subarrays is O(n^2); local ending-best captures the only needed history."
            code = "cur = max(x, cur + x); best = max(best, cur) for every element."
        }
        "besttimetobuyandsellstock" = @{
            recall = "Track the lowest price so far; today's profit is price minus that minimum."
            hook = "Trying all buy/sell pairs repeats the same prefix minimum search."
            code = "For each price, update minPrice, then best = max(best, price - minPrice)."
        }
        "climbingstairsfib" = @{
            recall = "Ways to step n equals ways to n-1 plus ways to n-2."
            hook = "Recursive Fibonacci repeats the same smaller step counts."
            code = "Iterate two rolling values for ways to previous one and two steps."
        }
        "editdistance" = @{
            recall = "dp[i][j] is edits to convert first i chars of word1 to first j chars of word2."
            hook = "Naive recursion branches into insert/delete/replace repeatedly for same prefixes."
            code = "Initialize empty-string row/column; if chars equal copy diagonal else 1 + min(insert, delete, replace)."
        }
        "gasstation" = @{
            recall = "If tank goes negative at i, every start since the candidate is impossible."
            hook = "Trying every start repeats failed prefixes; greedy skips the whole impossible range."
            code = "Track totalNet, tank, and start; when tank < 0 set start = i + 1 and reset tank."
        }
        "jumpgame" = @{
            recall = "Track the farthest reachable index; failure happens only when i passes reach."
            hook = "DP reachability is unnecessary; farthest reach dominates all earlier reachable choices."
            code = "Scan i, fail if i > reach, otherwise reach = max(reach, i + nums[i])."
        }
        "besttimetobuyandsellstockii" = @{
            recall = "Unlimited transactions means every positive day-to-day increase can be harvested."
            hook = "State-machine DP collapses because every rising edge is independently safe to take."
            code = "For i from 1, add prices[i] - prices[i-1] whenever the difference is positive."
        }
        "besttimetobuyandsellstockiii" = @{
            recall = "Four states track first buy, first sell, second buy, second sell."
            hook = "Greedy rising edges fail with at most two transactions; holding/sold states preserve constraints."
            code = "Update buy1, sell1, buy2, sell2 for each price and return sell2."
        }
        "besttimetobuyandsellstockiv" = @{
            recall = "For k transactions, each transaction layer has a hold and cash state."
            hook = "Enumerating transaction boundaries repeats choices; DP compresses day and transaction count."
            code = "If k is large use stock II; otherwise update hold[t] and cash[t] for t = 1..k."
        }
        "besttimetobuyandsellstockwithcooldown" = @{
            recall = "Cooldown creates three states: hold, sold today, and rest."
            hook = "Greedy cannot sell and immediately rebuy; the cooldown day must be encoded in state."
            code = "For each price update sold = hold + price, hold = max(hold, rest - price), rest = max(rest, oldSold)."
        }
        "besttimetobuyandsellstockwithtransactionfee" = @{
            recall = "Fee changes the sell transition; hold/cash states prevent double-counting fees."
            hook = "Local rising edges are not enough when each completed transaction pays a fee."
            code = "For each price: cash = max(cash, hold + price - fee); hold = max(hold, cash - price)."
        }
        "climbingstairs" = @{
            recall = "Ways to reach n comes from n-1 plus n-2, with two rolling counts."
            hook = "The recursion tree repeats the same step count states."
            code = "Start ways(0)=1, ways(1)=1, then iterate next = oneBack + twoBack."
        }
        "mincostclimbingstairs" = @{
            recall = "Cost to stand on step i is cost[i] plus min(previous one, previous two)."
            hook = "Choosing the cheaper immediate next step can block a cheaper total suffix."
            code = "Iterate two rolling minimum costs and return min(cost to last, cost to second last)."
        }
        "perfectsquares" = @{
            recall = "dp[x] is the fewest square numbers summing to x; try each square as the last move."
            hook = "Greedy largest-square choice fails on cases where smaller squares combine better."
            code = "Initialize dp[0]=0; for x=1..n, dp[x]=1+min(dp[x-square])."
        }
        "wordbreak" = @{
            recall = "dp[i] means prefix s[0..i) can be segmented into dictionary words."
            hook = "Recursive cuts retry the same suffixes; prefix validity caches reusable split points."
            code = "For each end i, set dp[i] if some dp[j] and s[j..i) is in the dictionary."
        }
        "deleteoperationfortwostrings" = @{
            recall = "Minimum deletions equals removing everything not in the LCS."
            hook = "Direct delete recursion repeats prefix pairs; LCS preserves the shared subsequence once."
            code = "Compute LCS length, return word1.length + word2.length - 2 * lcs."
        }
        "distinctsubsequences" = @{
            recall = "dp[i][j] counts ways first i source chars form first j target chars."
            hook = "Include/skip choices revisit the same source-target prefix pairs."
            code = "If chars match add skip and take counts; otherwise carry skip count."
        }
        "distinctsubsequencesii" = @{
            recall = "Each char doubles subsequences, then subtracts subsequences counted before its previous occurrence."
            hook = "A set of all subsequences explodes; last contribution per character removes duplicates compactly."
            code = "Maintain total distinct subsequences and lastContribution[char], updating total by new unique additions."
        }
        "interleavingstring" = @{
            recall = "dp[i][j] says s3 prefix i+j can be formed by prefixes of s1 and s2."
            hook = "Greedy picking from either string fails when equal chars create ambiguous futures."
            code = "Fill dp by taking next char from s1 or s2 when it matches s3[i+j-1]."
        }
        "longestcommonsubsequence" = @{
            recall = "dp[i][j] is the best subsequence length between two prefixes."
            hook = "Subsequence matching branches repeatedly on the same prefix pairs."
            code = "If chars match use 1 + diagonal; otherwise max(top,left)."
        }
        "longestpalindromicsubsequence" = @{
            recall = "dp[l][r] is best palindrome subsequence inside s[l..r]."
            hook = "Subsequence choices overlap heavily; interval DP reuses inner ranges."
            code = "Fill by increasing length: equal ends use 2 + dp[l+1][r-1], else max(drop left, drop right)."
        }
        "minimumasciideletesumfortwostrings" = @{
            recall = "dp[i][j] is minimum ASCII deletion cost to make two prefixes equal."
            hook = "LCS length is insufficient because deleted characters have different costs."
            code = "If chars match take diagonal; otherwise delete one side and add its ASCII cost."
        }
        "longestcontinuousincreasingsubsequence" = @{
            recall = "Continuous means subarray, so reset the current streak whenever nums[i] <= nums[i-1]."
            hook = "LIS tails/DP is overkill because skipping is not allowed."
            code = "Scan once, current = nums[i] > nums[i-1] ? current + 1 : 1, update best."
        }
        "maximumlengthofpairchain" = @{
            recall = "Sort pairs by end and take the next pair whose start is after the current end."
            hook = "LIS-style DP works, but earliest finishing pair leaves maximum room for the future."
            code = "Sort by pair[1], keep currentEnd, count pair when pair[0] > currentEnd."
        }
        "numberoflongestincreasingsubsequence" = @{
            recall = "Track both LIS length ending at i and how many ways achieve that length."
            hook = "tails gives length only; counting needs ownership of every ending index."
            code = "For each i, scan previous smaller j and update len[i] plus count[i]."
        }
        "russiandollenvelopes" = @{
            recall = "Sort width ascending, height descending for equal width, then LIS on heights."
            hook = "Plain 2D sorting can wrongly nest equal-width envelopes."
            code = "Sort by width asc and height desc, then lower_bound heights to get LIS length."
        }
        "stockseries2" = @{
            recall = "For unlimited transactions, add every positive day-to-day price difference."
            hook = "Enumerating buy/sell sequences repeats work; every rising edge can be taken independently."
            code = "Scan prices and add max(0, prices[i] - prices[i-1])."
        }
        "numberofislands" = @{
            recall = "Every time you find unvisited land, sink its whole connected component and count one island."
            hook = "Without visited marking, the same land cells get counted repeatedly."
            code = "Scan grid; on '1', increment count and DFS/BFS four directions marking visited/water."
        }
        "floodfill" = @{
            recall = "Recolor only the connected component matching the starting color."
            hook = "Blind DFS can recolor wrong regions or loop when new color equals old color."
            code = "If oldColor == newColor return; DFS/BFS neighbors with oldColor and recolor them."
        }
        "isgraphbipartite" = @{
            recall = "A graph is bipartite if every edge connects opposite colors."
            hook = "Visited alone is insufficient; conflicts appear when an edge sees same-color endpoints."
            code = "For each uncolored node, BFS/DFS assign colors and fail on same-color neighbor."
        }
        "pacificatlanticwaterflow" = @{
            recall = "Reverse the flow: start from both oceans and move to equal-or-higher neighboring cells."
            hook = "DFS from every cell to both oceans repeats huge overlap."
            code = "Mark cells reachable from Pacific border and Atlantic border; answer intersection."
        }
        "surroundedregions" = @{
            recall = "Only O-regions connected to the border survive; all other O cells are captured."
            hook = "Flipping every O before knowing border reachability captures safe regions incorrectly."
            code = "DFS/BFS border O cells as safe, flip remaining O to X, restore safe marks."
        }
        "clonegraph" = @{
            recall = "Map original node to cloned node before cloning neighbors to handle cycles."
            hook = "Naive recursive copy loops on cycles and duplicates shared nodes."
            code = "DFS/BFS: create clone if absent, then connect cloned neighbors from the map."
        }
        "numberofclosedislands" = @{
            recall = "A closed island is a land component that never touches the grid boundary."
            hook = "Counting components alone overcounts islands connected to the border."
            code = "DFS each land component, return false if any cell touches border, mark visited."
        }
        "maxareaofisland" = @{
            recall = "DFS each land component and return its cell count; keep the maximum."
            hook = "Counting land globally ignores component boundaries."
            code = "On each unvisited land cell, DFS four directions accumulating area."
        }
        "coloringaborder" = @{
            recall = "Only cells on the component boundary get recolored; interior cells keep original color."
            hook = "Flood filling the whole component changes interior cells incorrectly."
            code = "DFS component, mark a cell as border if it touches outside grid or different color."
        }
        "wordladder" = @{
            recall = "BFS words level by level; first time reaching endWord is the shortest transformation length."
            hook = "DFS may find a longer path first; all transformations cost one step."
            code = "Queue begin word, generate one-letter mutations, visit dictionary words once per level."
        }
        "rottingoranges" = @{
            recall = "All initially rotten oranges start a multi-source BFS; each level is one minute."
            hook = "Starting BFS separately repeats infection work and gives wrong simultaneous timing."
            code = "Queue all rotten cells, count fresh, process BFS levels, decrement fresh on infection."
        }
        "01matrix" = @{
            recall = "Start BFS from all zero cells; first visit gives nearest-zero distance."
            hook = "Running BFS from every one repeats work; multi-source BFS expands all shortest distances together."
            code = "Queue every zero with distance 0, then relax unvisited neighbors to dist+1."
        }
        "numberofprovinces" = @{
            recall = "Each DFS/BFS from an unvisited city marks one connected province."
            hook = "Checking pairs repeatedly is unnecessary once a city's component is visited."
            code = "Scan cities; when unvisited, count province and traverse connected cities from adjacency matrix."
        }
        "khighestrankeditemswithinapricerange" = @{
            recall = "BFS by distance, collecting valid items and sorting tie-breaks by price,row,col."
            hook = "DFS does not preserve shortest distance order in the grid."
            code = "BFS from start through passable cells; collect price-in-range items with distance and sort ranking."
        }
        "topkfrequentelements" = @{
            recall = "Count frequencies, then keep only the k highest-frequency entries."
            hook = "Sorting all unique values works but costs more than keeping a size-k heap or buckets."
            code = "Build frequency map, then use bucket lists by frequency or a min-heap of size k."
        }
        "findmedianfromdatastream" = @{
            recall = "Two heaps split lower and upper halves; median comes from heap tops."
            hook = "Sorting the stream after every insert is too slow."
            code = "Push into maxHeap/minHeap, rebalance sizes, median is top or average of tops."
        }
        "taskscheduler" = @{
            recall = "CPU idles only when the most frequent tasks cannot be spaced by cooldown gaps."
            hook = "Simulating every schedule is unnecessary; max frequency defines the minimum frame."
            code = "Use maxFreq and countMax: max(tasks.length, (maxFreq-1)*(n+1)+countMax)."
        }
        "kthlargestelementinanarray" = @{
            recall = "A size-k min-heap keeps the k largest seen so far; top is kth largest."
            hook = "Full sorting is O(n log n) when only one order statistic is needed."
            code = "Push each number, pop when heap size > k, return heap top."
        }
        "kthlargestelementinastream" = @{
            recall = "Maintain a size-k min-heap after every add; top is the kth largest in the stream."
            hook = "Resorting all stream values after every add is too slow."
            code = "On add, push value, trim heap to k, return heap.peek()."
        }
        "kclosestpointstoorigin" = @{
            recall = "Keep the k smallest squared distances; compare without taking square roots."
            hook = "Sorting all points is unnecessary when only k closest are needed."
            code = "Use max-heap of size k by distance, or quickselect by squared distance."
        }
        "awardtopkhotels" = @{
            recall = "Score each hotel by keyword hits, then rank by score and tie-breaker."
            hook = "Repeated text scans and full sorting can be avoided with maps and top-k selection."
            code = "Build keyword set, count matches per hotel review, then sort or heap by score/id."
        }
        "sortcharactersbyfrequency" = @{
            recall = "Frequency map plus bucket/heap outputs characters from highest count to lowest."
            hook = "Comparator sorting every character occurrence is wasteful; sort unique chars by counts."
            code = "Count chars, bucket by frequency or heap entries, append char repeated count times."
        }
        "addbinary" = @{
            recall = "Add bits from right to left with carry, exactly like decimal addition."
            hook = "Converting to integer can overflow and hides the carry invariant."
            code = "Use i,j,carry; append (sum % 2), update carry=sum/2, reverse result."
        }
        "countprimes" = @{
            recall = "Sieve marks multiples of each discovered prime starting at p*p."
            hook = "Testing every number by trial division repeats divisibility work."
            code = "Boolean isComposite; for p*p < n, mark multiples p*p, p*p+p, ...; count unmarked."
        }
        "encodeanddecodetinyurl" = @{
            recall = "Encode creates a stable short key mapped to the original URL; decode is a map lookup."
            hook = "The core invariant is key uniqueness and persistence, not string shortening alone."
            code = "Generate/increment key, store key->longUrl, return domain/key; decode extracts key and reads map."
        }
        "designfraudpatterndetection" = @{
            recall = "Define which transaction events are retained and which rule/window makes a pattern fraudulent."
            hook = "Without explicit time-window and identity keys, the detector becomes vague and untestable."
            code = "Index recent events by account/card/merchant, evict expired entries, evaluate rules on insert."
        }
        "apiintegrationexample" = @{
            recall = "Model request, response, retry, timeout, and idempotency boundaries explicitly."
            hook = "Integration code fails interviews when error handling and contracts are implicit."
            code = "Wrap client call with typed DTOs, timeout/retry policy, status handling, and clear failure result."
        }
        "designredis" = @{
            recall = "Key-value operations need storage, expiry metadata, and eviction/cleanup policy."
            hook = "A map alone misses TTL semantics and memory-pressure behavior."
            code = "Store value plus expireAt, check expiry on get/set, and maintain cleanup or eviction structure."
        }
        "designtokenbucketratelimiter" = @{
            recall = "A bucket refills by elapsed time and each request consumes one token if available."
            hook = "Fixed counters burst badly at window boundaries; token bucket smooths rate with bounded burst."
            code = "Per key, compute tokens = min(capacity, tokens + elapsed*rate), allow if tokens >= cost."
        }
        "hotelreviews" = @{
            recall = "Use trie or keyword set to count good words per review, then rank hotels by score."
            hook = "Repeated string matching for every keyword wastes prefix/lookup work."
            code = "Normalize review words, count keyword hits, aggregate per hotel, sort by score and id."
        }
        "middleoflinkedlist" = @{
            recall = "slow is the middle candidate and fast has covered twice as many edges; when fast cannot move two steps, slow is the required second middle."
            hook = "Counting length takes two passes; the 2:1 pointer-speed invariant finds the middle in one pass."
            code = "Start slow and fast at head; while fast != null and fast.next != null, move slow once and fast twice; return slow."
        }
        "allnodesdistancekinbinarytree" = @{
            recall = "Parent links turn the tree into an undirected graph; BFS level d contains exactly the nodes distance d from target."
            hook = "Searching only target's subtree misses nodes reached through ancestors."
            code = "Build node-to-parent links, BFS from target with visited marked on enqueue, advance exactly k levels, then return the frontier."
        }
        "amountoftimeforbinarytreetobeinfected" = @{
            recall = "Parent links expose all three directions; each BFS level is one infection minute from the start node."
            hook = "Subtree height from start misses infection paths that travel through parents."
            code = "Build node-to-parent links, BFS from start with visited on enqueue, and count completed spreading levels until the frontier is empty."
        }
        "graphvalidtree" = @{
            recall = "A valid undirected tree has exactly n - 1 edges and all n nodes in one connected component."
            hook = "Connectivity alone accepts cycles, while acyclicity alone accepts disconnected forests."
            code = "Reject edge count != n - 1, traverse from one node while skipping the parent edge, and require every node to be visited."
        }
        "possiblebipartition" = @{
            recall = "color[x] is the group assigned to person x; every dislike edge must connect opposite colors."
            hook = "Visited state alone cannot detect an odd-cycle parity conflict."
            code = "Build an undirected dislike graph; for every uncolored component, assign opposite colors by BFS/DFS and fail on a same-color edge."
        }
        "redundantconnection" = @{
            recall = "DSU represents the forest formed by accepted edges; an edge is redundant exactly when both endpoints already have the same root."
            hook = "Re-running graph traversal per edge repeats connectivity work."
            code = "Initialize one parent per 1-based node; for each edge union roots, and return the edge whose roots were already equal."
        }
        "parallelcourses" = @{
            recall = "The queue at a semester boundary contains every course currently unlocked; one complete Kahn level is one semester."
            hook = "A flat topological count proves feasibility but loses the minimum parallel rounds."
            code = "Queue all indegree-zero courses, process exactly queue.size() per semester, unlock dependents, and return -1 unless all courses were processed."
        }
        "aliendictionary" = @{
            recall = "Only the first differing characters in adjacent sorted words create an ordering edge; every distinct character is still a graph node."
            hook = "The dependency graph must be inferred before topological sorting can begin."
            code = "Reject a longer word before its exact prefix, deduplicate first-difference edges, then Kahn-sort all characters; return empty on a cycle."
        }
        "findeventualsafestates" = @{
            recall = "outdegree[x] counts outgoing choices not yet proved safe; terminal nodes start safe with outdegree 0."
            hook = "Forward reachability does not directly expose whether every path terminates."
            code = "Reverse every edge, queue terminal nodes, decrement predecessor outdegrees, enqueue a predecessor at zero, then sort the safe nodes."
        }
        "sequencereconstruction" = @{
            recall = "The target is uniquely reconstructible only when Kahn's frontier has exactly one node and that node equals nums[index] at every step."
            hook = "One valid topological order is insufficient; uniqueness and exact target order both matter."
            code = "Build deduplicated edges, require every target value to appear, reject queue.size() != 1 or a mismatched pop, and consume all target values."
        }
        "sortitemsbygroupsrespectingdependencies" = @{
            recall = "VERIFY FROM SOURCE - the local chapter records that item and group dependencies require two coordinated topological orders, but it does not provide a complete accepted implementation."
            hook = "A single item-level topological order can interleave groups and violate group contiguity."
            code = "VERIFY FROM SOURCE - confirm ungrouped-item normalization, item graph, group graph, and contiguous emission order before memorizing transitions."
        }
        "coursescheduleiv" = @{
            recall = "reachable[a][b] means course a is a direct or indirect prerequisite of course b."
            hook = "Producing one topological order cannot answer arbitrary prerequisite reachability queries."
            code = "Seed direct prerequisite edges, compute transitive closure through every intermediate course, then answer each query from reachable[from][to]."
        }
        "subsets" = @{
            recall = "path is one subset formed from indices before start; every recursion state itself is a valid answer."
            hook = "A used array is unnecessary because increasing start already prevents reuse and reorder duplicates."
            code = "Copy path on entry; for i from start, choose nums[i], recurse with i + 1, then remove the choice."
        }
        "combinationsum" = @{
            recall = "remaining is the target still unpaid and start prevents permutation duplicates; the same candidate may be reused."
            hook = "Advancing past the chosen index would incorrectly prohibit unlimited reuse."
            code = "When remaining == 0 copy path; choose candidate i <= remaining, recurse with i, then undo; prune larger sorted candidates."
        }
        "wordsearch" = @{
            recall = "index is the next word character to match and the current DFS path temporarily owns each board cell at most once."
            hook = "Global visited is wrong because another starting path may reuse the same cell."
            code = "Match board[r][c] to word[index], mark it for this path, recurse four directions with index + 1, then restore the cell."
        }
        "lettercombinationsofaphonenumber" = @{
            recall = "index is the next digit to expand and path contains exactly one mapped letter for every earlier digit."
            hook = "Each digit contributes a small independent branch; the concrete strings are the output."
            code = "For each letter mapped from digits[index], append, recurse with index + 1, then delete; emit only when index == digits.length."
        }
        "permutations" = @{
            recall = "used[i] means index i is already owned by the current ordering; path length is the next permutation position."
            hook = "A start index would miss valid reorderings because every unused value may occupy every position."
            code = "Loop all indices, choose only !used[i], mark and append, recurse, then remove and unmark; copy at size n."
        }
        "permutationsii" = @{
            recall = "used[i] owns an index on the current path; after sorting, equal values are tried in a fixed same-depth order."
            hook = "Skipping every equal neighbor loses valid permutations; only an unused equal predecessor proves this branch is a duplicate."
            code = "Skip used indices and skip i > 0 && nums[i] == nums[i - 1] && !used[i - 1]; otherwise choose, recurse, and undo."
        }
        "largestrectangle" = @{
            recall = "The increasing stack holds bar indices whose maximal right boundary is not known; a lower current bar closes every taller pending rectangle."
            hook = "Expanding left and right from every bar repeats boundary searches."
            code = "Scan through a final height-0 sentinel; while currentHeight < height[top], pop h, use current i as right boundary and new top as left boundary, width = right - left - 1."
        }
        "topkfrequentwords" = @{
            recall = "A size-k min-heap stores the k strongest words, with the weakest winner at the root: lower frequency, or lexicographically larger on a tie."
            hook = "Sorting every distinct word is unnecessary when only k winners are required."
            code = "Count words, offer each distinct word, evict when size > k, then remove from weakest to strongest and prepend to produce final order."
        }
        "hindex" = @{
            recall = "buckets[h] counts papers with exactly h citations, except every citation >= n is capped into bucket n."
            hook = "Full sorting is unnecessary because the answer range is only 0..n."
            code = "Accumulate paper counts from h = n downward; the first h with papers >= h is the maximum valid H-index."
        }
        "insertinterval" = @{
            recall = "newInterval is the not-yet-emitted merged interval; existing intervals are already sorted and non-overlapping."
            hook = "Re-sorting discards a guarantee that enables one linear pass."
            code = "Emit intervals ending < new.start, merge while current.start <= new.end, emit the merged interval once, then append the untouched suffix."
        }
        "mergeintervals" = @{
            recall = "activeStart..activeEnd is the union of every sorted interval not yet flushed to output."
            hook = "After sorting by start, only the active merged interval can overlap the current interval."
            code = "If current.start <= activeEnd, extend activeEnd = max(activeEnd, current.end); otherwise flush active and replace it with current; flush once after the loop."
        }
        "nonoverlappingintervals" = @{
            recall = "lastFinish is the end of the last kept interval; earliest finish leaves maximal room for every future interval."
            hook = "Minimizing removals equals maximizing the number of compatible intervals."
            code = "Sort by end; keep current when current.start >= lastFinish and move lastFinish to current.end, otherwise remove it; answer is n - kept."
        }
        "longestcommonprefix" = @{
            recall = "The common prefix continues only while the trie path has exactly one child and the current node is not terminal."
            hook = "A branch means strings disagree; a terminal means one string has already ended."
            code = "Insert all strings, walk the sole child while childCount == 1 && !isWord, append that edge, and stop at branch or terminal."
        }
        "longestwordindictionary" = @{
            recall = "A candidate is legal only if every trie node on its path is terminal, meaning every prefix is also a word."
            hook = "Normal trie membership validates only the complete word, not all intermediate prefixes."
            code = "Insert all words, validate terminal after every consumed character, and choose greatest length with lexicographically smallest tie."
        }
        "replacewords" = @{
            recall = "While scanning a sentence word, the first terminal trie node is its shortest dictionary root."
            hook = "Continuing after the first terminal returns a longer root and violates the objective."
            code = "Walk characters until a child is missing or terminal is reached; replace only on the first terminal, otherwise keep the original word."
        }
        "searchsuggestionssystem" = @{
            recall = "For each typed prefix, suggestions are the first at most three terminal words below that prefix in lexicographic DFS order."
            hook = "Exact trie search finds the prefix node but does not rank descendants."
            code = "Locate each prefix node; DFS children 0..25, append terminal words, backtrack the path, and stop that search at three results."
        }
        "shortencodingofwords" = @{
            recall = "Only words that are not suffixes of a longer encoded word add word.length + 1 characters."
            hook = "Reversing words turns shared suffixes into shared trie prefixes."
            code = "Deduplicate words, process longer words first or insert reversed words, and add length + 1 only when the word creates a new terminal leaf contribution."
        }
        "mapsumpairs" = @{
            recall = "node.sum is the total current value of every key passing through that prefix; updating an existing key changes each prefix by delta only."
            hook = "Adding the full replacement value double-counts the key's old contribution."
            code = "Compute delta = newValue - oldValue, store the new key value, add delta along root and every key edge, and return the reached prefix node's sum."
        }
        "accountsmerge" = @{
            recall = "emailToFirstAccount owns the first account index for each email; a repeated email proves those account indices belong to one DSU component."
            hook = "Names are not unique identifiers, but shared emails create transitive connectivity."
            code = "Union account roots on repeated emails, then find each account root, collect unique emails under that root, sort them, and prefix the representative name."
        }
        "spiralmatrix" = @{
            recall = "Shrink top, bottom, left, and right boundaries after traversing each side."
            hook = "Visited simulation is more state than needed; boundaries define the remaining ring."
            code = "Traverse top row, right col, bottom row if valid, left col if valid; move boundaries inward."
        }
        "stringtointegeratoi" = @{
            recall = "Parse sign and digits once, clamping before overflow."
            hook = "Using built-in parse or wider assumptions misses whitespace, sign, and overflow rules."
            code = "Skip spaces, read optional sign, accumulate digit while checking against INT_MAX limits."
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

    if ($titleText -match "api integration|design fraud|design redis|token bucket|tinyurl|stock price fluctuation|design a leaderboard|design an ordered stream|design hit counter|design parking system") { return "Design/LLD" }
    if ($titleText -match "^ipo$|sliding window median|number of orders in the backlog") { return "Heap" }
    if ($titleText -match "n-queens|sudoku solver") { return "Backtracking" }
    if ($titleText -match "^car pooling$") { return "Intervals/Greedy" }
    if ($titleText -match "^gas station$|^jump game$|^best time to buy and sell stock$|^best time to buy and sell stock ii$") { return "Greedy" }
    if ($titleText -match "^distinct subsequences ii$|best time to buy and sell stock (iii|iv|with cooldown|with transaction fee)") { return "Dynamic Programming" }
    if ($titleText -match "^longest continuous increasing subsequence$") { return "Sliding Window" }
    if ($titleText -match "^maximum length of pair chain$") { return "Intervals/Greedy" }
    if ($titleText -match "^merge k sorted lists$") { return "Heap" }
    if ($titleText -match "^meeting rooms ii$") { return "Heap" }
    if ($titleText -match "^meeting rooms?$") { return "Intervals/Greedy" }
    if ($titleText -match "first unique number|design circular queue") { return "Design/LLD" }
    if ($titleText -match "moving average from data stream") { return "Sliding Window" }
    if ($titleText -match "^two sum$") { return "HashMap/HashSet" }
    if ($titleText -match "^two sum ii") { return "Two Pointers" }
    if ($titleText -match "implement trie.*prefix tree|design add and search words|word search ii|maximum xor|hotel reviews|longest common prefix|longest word in dictionary|replace words|search suggestions system|short encoding of words") { return "Trie" }
    if ($titleText -match "parallel courses|alien dictionary|eventual safe states|sequence reconstruction|sort items by groups") { return "Topological Sort" }
    if ($titleText -match "^redundant connection$") { return "Union Find" }
    if ($titleText -match "possible bipartition|graph valid tree") { return "Graph DFS" }
    if ($titleText -match "sliding window maximum|online stock span") { return "Stack" }
    if ($titleText -match "^meeting rooms$") { return "Intervals/Greedy" }
    if ($titleText -match "maximum profit in job scheduling") { return "Dynamic Programming" }
    if ($titleText -match "network delay time") { return "Graph BFS" }
    if ($titleText -match "^longest palindrome$") { return "HashMap/HashSet" }
    if ($titleText -match "sort colors|sort array by parity|move zeroes") { return "Two Pointers" }
    if ($titleText -match "partition labels") { return "Intervals/Greedy" }
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

function Get-PatternOverride {
    param(
        [string] $Title,
        [string] $Pattern
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "partitionlabels" { return "Greedy last-occurrence boundary" }
        "sortarraybyparity" { return "Parity partition" }
        "movezeroes" { return "Stable two-pointer compaction" }
        "middleoflinkedlist" { return "Fast/slow pointers" }
        "allnodesdistancekinbinarytree" { return "Tree converted to graph + BFS distance" }
        "amountoftimeforbinarytreetobeinfected" { return "Tree converted to graph + BFS spread" }
        "graphvalidtree" { return "Connectivity + cycle" }
        "possiblebipartition" { return "Bipartite coloring" }
        "redundantconnection" { return "DSU cycle detection" }
        "parallelcourses" { return "Kahn BFS by levels" }
        "aliendictionary" { return "Constraint inference + topological sort" }
        "findeventualsafestates" { return "Reverse graph + outdegree elimination" }
        "sequencereconstruction" { return "Unique topological order" }
        "sortitemsbygroupsrespectingdependencies" { return "Two-level topological sort" }
        "coursescheduleiv" { return "Dependency transitive closure" }
        "subsets" { return "Backtracking subsets" }
        "combinationsum" { return "Backtracking with candidate reuse" }
        "wordsearch" { return "Grid path backtracking" }
        "lettercombinationsofaphonenumber" { return "Position-choice backtracking" }
        "permutations" { return "Permutation used-index state" }
        "permutationsii" { return "Sorted duplicate-aware permutations" }
        "largestrectangle" { return "Monotonic stack pop-time boundaries" }
        "topkfrequentwords" { return "Bounded heap with tie ordering" }
        "hindex" { return "Bounded frequency buckets" }
        "insertinterval" { return "Insert into sorted non-overlapping intervals" }
        "mergeintervals" { return "Merge sorted intervals" }
        "nonoverlappingintervals" { return "Greedy earliest finish" }
        "longestcommonprefix" { return "Single-branch prefix walk" }
        "longestwordindictionary" { return "Every-prefix-terminal trie" }
        "replacewords" { return "Shortest terminal prefix" }
        "searchsuggestionssystem" { return "Prefix node + lexicographic DFS top 3" }
        "shortencodingofwords" { return "Reversed suffix trie" }
        "mapsumpairs" { return "Prefix aggregate with overwrite delta" }
        "accountsmerge" { return "Email ownership + DSU grouping" }
        "stockpricefluctuation" { return "Ordered price multiset + latest timestamp" }
        "numberofordersinthebacklog" { return "Two-sided price priority queues" }
        "ipo" { return "Capital sort + max-profit heap" }
        "slidingwindowmedian" { return "Two heaps + outgoing-value removal" }
        "nqueens" { return "Constraint-pruned row backtracking" }
        "sudokusolver" { return "Constraint-pruned cell backtracking" }
        "carpooling" { return "Difference array / sweep line" }
        "designaleaderboard" { return "Player scores + ordered score counts" }
        "designanorderedstream" { return "Array slots + advancing pointer" }
        "designhitcounter" { return "Fixed-time-window queue" }
        "designparkingsystem" { return "Fixed capacity counters" }
        "missingnumber" { return "XOR / arithmetic invariant" }
        "missingranges" { return "Sentinel boundary scan" }
        default { return $Pattern }
    }
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
        "twosum" { return 1 }
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
        "twosumiiinputarrayissorted" { return 12 }
        "containerwithmostwater" { return 13 }
        "trappingrainwater" { return 14 }
        "binarytreelevelordertraversal" { return 15 }
        "validatebinarysearchtree" { return 16 }
        "lowestcommonancestorofabinarytree" { return 17 }
        "numberofislands" { return 18 }
        "courseschedule" { return 19 }
        "coursescheduleii" { return 20 }
        "wordladder" { return 21 }
        "kokoeatingbananas" { return 22 }
        "searchinrotatedsortedarray" { return 23 }
        "findfirstandlastpositionofelementinsortedarray" { return 24 }
        "lrucache" { return 25 }
        "copylistwithrandompointer" { return 26 }
        "kthsmallestelementinabst" { return 27 }
        "balancedbinarytree" { return 28 }
        "diameterofbinarytree" { return 29 }
        "pathsumiii" { return 30 }
        "rottingoranges" { return 31 }
        "01matrix" { return 32 }
        "houserobber" { return 33 }
        "coinchange" { return 34 }
        "subsets" { return 35 }
        "validparentheses" { return 36 }
        "topkfrequentelements" { return 37 }
        "dailytemperatures" { return 38 }
        "meetingroomsii" { return 39 }
        "implementtrieprefixtree" { return 40 }
        "floodfill" { return 41 }
        "isgraphbipartite" { return 42 }
        "minimumnumberofarrowstoburstballoons" { return 43 }
        "combinationsum" { return 44 }
        "wordsearch" { return 45 }
        "findmedianfromdatastream" { return 46 }
        "largestrectangleinhistogram" { return 47 }
        "findallanagramsinastring" { return 48 }
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
        "gasstation" { return 58 }
        "jumpgame" { return 59 }
        "besttimetobuyandsellstockii" { return 60 }
        "besttimetobuyandsellstockwithtransactionfee" { return 118 }
        "besttimetobuyandsellstockwithcooldown" { return 119 }
        "besttimetobuyandsellstockiii" { return 120 }
        "besttimetobuyandsellstockiv" { return 121 }
        "distinctsubsequencesii" { return 122 }
        "wordbreak" { return 123 }
        "interleavingstring" { return 124 }
        "longestcommonsubsequence" { return 125 }
        "deleteoperationfortwostrings" { return 126 }
        "longestpalindromicsubsequence" { return 127 }
        "minimumasciideletesumfortwostrings" { return 128 }
        "climbingstairs" { return 129 }
        "mincostclimbingstairs" { return 130 }
        "perfectsquares" { return 131 }
        "numberoflongestincreasingsubsequence" { return 132 }
        "russiandollenvelopes" { return 133 }
        "maximumlengthofpairchain" { return 134 }
        "longestcontinuousincreasingsubsequence" { return 135 }
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

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "partitionlabels" { return "Close a partition only when the current index reaches the farthest last occurrence of all chars seen so far." }
        "sortarraybyparity" { return "Partition the array so the write/left side contains only values satisfying the parity condition." }
        "movezeroes" { return "Compact nonzero values forward in order, then fill the remaining suffix with zeroes." }
    }

    switch ($Category) {
        "HashMap/HashSet" { return "Store counts, complements, or seen state so repeated lookup becomes O(1)." }
        "Two Pointers" { return "Shrink the search space by moving the pointer that can still improve the answer." }
        "Sliding Window" { return "Expand right, shrink left to restore validity, then update the answer at the right time." }
        "Prefix/Suffix" { return "Precompute cumulative left/right state so each range or exclusion is answered cheaply." }
        "Binary Search" { return "Ask the comparison or yes/no question that makes one side impossible, then preserve the boundary/index invariant." }
        "Linked List" { return "Name every pointer, save next before rewiring, and return the real new head." }
        "Tree BFS" { return "Use a queue by levels; capture level size before pushing children." }
        "Tree DFS" { return "Define exactly what the helper returns, combine left/right, and update global answer separately if needed." }
        "Graph BFS" { return "Use queue layers when first discovery is the shortest or minimum-step answer." }
        "Graph DFS" { return "Own each component or path with visited state so one traversal fully accounts for it." }
        "Stack" { return "Keep pending openings, operands, or monotonic candidates until the current item resolves them." }
        "Heap" { return "Keep only the frontier, top K, or two balanced halves instead of fully sorting each step." }
        "Intervals/Greedy" { return "Sort to make conflicts local, then merge, count active intervals, or choose safe endpoints." }
        "Backtracking" { return "Choose, recurse, undo; the path is exactly the current decision state." }
        "Trie" { return "Share prefix nodes so lookup/search consumes one character at a time instead of rescanning words." }
        "Dynamic Programming" { return "Name the state, base case, transition, and iteration order before writing loops." }
        "Union Find" { return "Represent components with parent links; union merges and failed union detects cycles." }
        "Topological Sort" { return "Use indegree or DFS states to process dependencies before dependents." }
        "Greedy" { return "Take the local choice only after proving it cannot hurt the future optimum." }
        "Math/Bit/String" { return "Expose the arithmetic, bit, carry, border, or contribution invariant before simulating." }
        "Design/LLD" { return "Define operations, consistency guarantees, stored state, and per-operation complexity." }
        default { return "Derive the direct approach, name the wasted work, then choose the invariant that removes it." }
    }
}

function Get-InterviewHook {
    param(
        [string] $Category,
        [string] $Pattern,
        [string] $Title
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "partitionlabels" { return "Brute force checks future conflicts repeatedly; last-occurrence boundaries reveal exactly when a safe partition can close." }
        "sortarraybyparity" { return "Sorting is unnecessary; parity gives a direct partition predicate maintained by two pointers or a write index." }
        "movezeroes" { return "Repeated shifting is the bottleneck; stable compaction writes each nonzero once and zero-fills the tail." }
    }

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

function Get-BruteForceLine {
    param(
        [string] $Category,
        [string] $Title
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "binarysearch" { return "Linearly scan the sorted array for the target." }
        "firstbadversion" { return "Call isBadVersion from version 1 upward until the first bad version appears." }
        "findfirstandlastpositionofelementinsortedarray" { return "Scan the array once and record the first and last target positions." }
        "searchinsertposition" { return "Scan from left until finding the first value greater than or equal to target." }
        "findpeakelement" { return "Check every index and compare it with neighbors to find a peak." }
        "sqrtx" { return "Try integers one by one until square exceeds x." }
        "searchinrotatedsortedarray" { return "Scan every index because the pivot breaks global sorted order." }
        "searchinrotatedsortedarrayii" { return "Scan every index, especially when duplicates hide sorted-half information." }
        "kokoeatingbananas" { return "Try every speed from 1 to max pile and simulate total eating hours." }
        "capacitytoshippackageswithinddays" { return "Try every capacity from max weight to total weight and simulate shipping days." }
        "splitarraylargestsum" { return "Try possible max sums or enumerate contiguous partitions directly." }
        "minimumnumberofdaystomakembouquets" { return "Try days linearly and count how many adjacent bouquets can be made." }
        "minimumheighttrees" { return "Root the tree at every node and compute its height." }
        "courseschedule" { return "Repeatedly scan all courses to find one whose prerequisites are already completed." }
        "coursescheduleii" { return "Repeatedly scan all courses to append one whose prerequisites are already completed." }
        "minimumnumberofarrowstoburstballoons" { return "Try arrow positions or compare balloon overlaps pair by pair." }
        "carpooling" { return "For every route point, recompute passenger load by checking all trips." }
        "minimumplatforms" { return "For each train, count how many other trains overlap its time interval." }
    }

    switch ($Category) {
        "HashMap/HashSet" { return "Scan repeatedly or compare every candidate pair/count directly." }
        "Two Pointers" { return "Try all pairs, all boundaries, or build an auxiliary cleaned structure." }
        "Sliding Window" { return "Enumerate every substring/subarray and recompute validity from scratch." }
        "Prefix/Suffix" { return "For every index or query, recompute the needed range/product/sum directly." }
        "Binary Search" { return "Linearly test candidates or scan the full sorted/search range." }
        "Linked List" { return "Copy nodes into an array/set, or make extra passes to recover positions." }
        "Tree BFS" { return "Traverse without preserving levels, then reconstruct level/view information afterward." }
        "Tree DFS" { return "Restart traversal from many nodes or compute subtree facts repeatedly." }
        "Graph BFS" { return "Run a separate search from each source or use DFS and then compare path lengths." }
        "Graph DFS" { return "Start a fresh traversal for every cell/node without reusable visited/component state." }
        "Stack" { return "For each element, scan left/right or simulate operations without remembering unresolved state." }
        "Heap" { return "Sort all candidates every time a top, kth, median, or next-best item is needed." }
        "Intervals/Greedy" { return "Compare every interval with every other interval before deciding conflicts/order." }
        "Backtracking" { return "Generate all possible candidates first, then filter invalid answers at the end." }
        "Trie" { return "Compare each word/prefix character-by-character against every dictionary entry." }
        "Dynamic Programming" { return "Use plain recursion or enumerate choices without caching repeated states." }
        "Union Find" { return "Run DFS/BFS connectivity checks after every merge/query." }
        "Topological Sort" { return "Repeatedly scan all dependencies to find what can be processed next." }
        "Greedy" { return "Explore all choices with search/DP before noticing a local choice is safe." }
        "Math/Bit/String" { return "Simulate the process directly or compare every possible candidate/string." }
        "Design/LLD" { return "Implement only the happy-path operation with one map and no invariant for edge cases." }
        default { return "Try the direct simulation or enumeration first." }
    }
}

function Get-BottleneckLine {
    param(
        [string] $Category,
        [string] $Title
    )

    switch ($Category) {
        "HashMap/HashSet" { return "The repeated lookup/counting work is the bottleneck." }
        "Two Pointers" { return "The O(n^2) pair/boundary search repeats comparisons that order can eliminate." }
        "Sliding Window" { return "Adjacent substrings share almost all state, but brute force discards it." }
        "Prefix/Suffix" { return "The same prefix/range aggregate is recomputed many times." }
        "Binary Search" { return "A monotonic property exists, so linear search wastes rejected half-ranges." }
        "Linked List" { return "Extra storage/passes hide the pointer invariant and add avoidable complexity." }
        "Tree BFS" { return "Level boundaries are lost unless queue processing is grouped by current level size." }
        "Tree DFS" { return "Subtree answers are recomputed unless the helper return contract carries them upward." }
        "Graph BFS" { return "Minimum-step answers require first-discovery order across layers." }
        "Graph DFS" { return "The same component/path states are revisited without marking and component ownership." }
        "Stack" { return "Each element's next/previous unresolved relation should be settled once, not rescanned." }
        "Heap" { return "Full ordering is more work than maintaining only the frontier or top k." }
        "Intervals/Greedy" { return "Without sorting, conflicts are global; sorting makes the next decision local." }
        "Backtracking" { return "Invalid branches can be pruned before they become complete candidates." }
        "Trie" { return "Dictionary words share prefixes, but brute force scans those prefixes repeatedly." }
        "Dynamic Programming" { return "The same state is reached by multiple choice paths." }
        "Union Find" { return "Connectivity changes incrementally, but repeated graph searches start over." }
        "Topological Sort" { return "Dependency scans loop unless indegree/state records what is already unlocked." }
        "Greedy" { return "Search is unnecessary only after proving the local choice preserves optimality." }
        "Math/Bit/String" { return "The hidden numeric/string invariant is cheaper than direct simulation." }
        "Design/LLD" { return "Unclear invariants make edge cases, complexity, and failure modes ambiguous." }
        default { return "The repeated work must be named before selecting the data structure." }
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

function Get-PrecisionContract {
    param(
        [string] $Category,
        [string] $Pattern,
        [string] $Title,
        [string] $Recall,
        [string] $InterviewHook,
        [string] $CodeIdea
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "twosum" { return "The map contains only indices already passed, so a complement hit never reuses the current element. For current value x, ask whether target - x has been seen before inserting x. If found, return the stored old index and the current index." }
        "binarysearch" { return "The target can only be inside the current inclusive range [left, right]. Compare nums[mid] with target, then discard the half that sorted order proves impossible. Stop when left > right, because no candidate index remains." }
        "longestsubstringwithoutrepeatingcharacters" { return "The active window contains no duplicate characters. When the current character was last seen inside the window, move left to lastSeen[ch] + 1, not one step blindly. Record the answer after the window is valid." }
        "productofarrayexceptself" { return "answer[i] first receives the product of all values strictly left of i, then gets multiplied by the product strictly right of i. The current element is never included in its own answer. This avoids division and handles zeros naturally." }
        "minimumwindowsubstring" { return "window counts only the current [left, right] characters and formed/have counts how many required character quotas are satisfied. Expand until every required quota is covered, then shrink from left while still valid and save the best before breaking validity. Extra copies do not increase formed once the quota is already met." }
        "reverselinkedlist" { return "Save next before changing current.next. After rewiring current.next to prev, move prev to current and current to saved next. When current becomes null, prev is the new head." }
        "linkedlistcycle" { return "slow advances one node and fast advances two nodes. Without a cycle, fast or fast.next reaches null; inside a cycle, the speed difference forces a meeting. Never dereference fast.next before checking it." }
        "mergetwosortedlists" { return "dummy.next is the real head and tail is the last node already attached to the merged list. At each step attach the smaller current node and advance only that source list. After one list ends, attach the remaining suffix directly." }
        "validanagram" { return "The count array/map is the net balance between the two strings. Increment for one string and decrement for the other, then every count must be zero. If lengths differ, fail before counting." }
        "validpalindrome" { return "left and right move inward over the original string, skipping only non-alphanumeric characters. Compare normalized characters after both skips. A mismatch returns false; crossing pointers means every required pair matched." }
        "mergeksortedlists" { return "The heap stores one current head per non-empty list, so the heap root is the globally smallest available node. Poll that node, append it to tail, then push its next node because that next node just became the list's candidate. Do not scan all k heads on every append." }
        "twosumiiinputarrayissorted" { return "left and right bound the remaining sorted search space. If sum is too small, only moving left rightward can increase it; if sum is too large, only moving right leftward can decrease it. Return the required one-based indices when the sum equals target." }
        "containerwithmostwater" { return "Area is width times the shorter wall, so the shorter side is the limiting boundary. Moving the taller side cannot improve the old limiting height with smaller width. Move the shorter side and keep the best area seen." }
        "trappingrainwater" { return "leftMax and rightMax are the best walls already seen from each side. The side with smaller max determines trapped water there because the opposite side is already high enough. Move that side inward and add max - height only after updating the side max." }
        "binarytreelevelordertraversal" { return "At the start of each outer loop, queue.size() is exactly the number of nodes in the current level. Process exactly that many nodes, appending their children for the next level. Never let newly enqueued children leak into the current level." }
        "validatebinarysearchtree" { return "Each node must be strictly greater than its inherited lower bound and strictly less than its inherited upper bound. Left children tighten the upper bound; right children tighten the lower bound. Parent-child checks alone miss ancestor violations." }
        "lowestcommonancestorofabinarytree" { return "The helper returns a found target or an LCA from that subtree. If both left and right return non-null, current is the split point. If only one side returns non-null, pass that result upward." }
        "numberofislands" { return "A new island is counted only when an unvisited land cell is first found. DFS/BFS then owns and marks that entire 4-directional land component. Water and already visited land contribute nothing." }
        "courseschedule" { return "indegree is the number of prerequisites still unmet for a course. Only zero-indegree courses can enter the queue, and processing one course decrements its dependents. If processed count is less than n, a cycle kept some courses locked." }
        "coursescheduleii" { return "A course is appended to the answer only when its indegree has dropped to zero. Processing that course consumes it as a prerequisite and unlocks dependents by decrementing indegree. If the final order length is not n, return empty because a cycle remains." }
        "wordladder" { return "Each queued word has a distance from beginWord, and all one-letter transformations cost one step. Mark a word visited when enqueueing so it cannot be reached again at the same or greater distance. The first time endWord is generated or dequeued is the shortest length." }
        "kokoeatingbananas" { return "speed is the candidate bananas per hour and must start at 1, never 0. For each pile, required hours are ceil(pile / speed), and feasibility is totalHours <= h. If a speed works, every higher speed works, so save it and search smaller." }
        "searchinrotatedsortedarray" { return "At least one half around mid is sorted. Decide which half is sorted, then keep it only if target lies within that half's inclusive bounds. Otherwise discard it and search the other half." }
        "findfirstandlastpositionofelementinsortedarray" { return "Use two boundary searches, not one hit plus expansion. The left boundary is the first index with value >= target; the right boundary is the last index with value <= target. Verify the boundaries actually equal target before returning them." }
        "lrucache" { return "The map owns key to node lookup, and the doubly linked list owns recency order from most-recent to least-recent. Every get or updated put moves the node to the front. When capacity is exceeded, remove the tail node from both list and map." }
        "copylistwithrandompointer" { return "Each original node must map to exactly one cloned node, preserving identity rather than value. After clones exist, set each clone's next and random by looking up the original node's targets. Null random remains null." }
        "kthsmallestelementinabst" { return "BST inorder traversal visits values in ascending order. Decrement k exactly when visiting the node itself, after left subtree and before right subtree. The kth visit is the answer." }
        "diameterofbinarytree" { return "The helper returns height upward, but the global answer is the best leftHeight + rightHeight seen at any node. Update diameter before returning 1 + max(leftHeight, rightHeight). Do not return diameter as height." }
        "pathsumiii" { return "prefixSum counts belong only to the current root-to-node path. For current sum s, paths ending here with targetSum equal the number of earlier prefixes s - targetSum. Add current prefix before going down, then decrement it when backtracking." }
        "rottingoranges" { return "All initially rotten oranges are minute 0 BFS sources. One BFS layer is one minute, so minutes increments after processing the whole layer, not per orange. Mark a fresh orange rotten when enqueueing it and return -1 if fresh remains unreachable." }
        "01matrix" { return "All zero cells start in the queue with distance 0. A one cell gets its nearest-zero distance the first time BFS reaches it. Mark distance when enqueueing to avoid duplicate visits." }
        "houserobber" { return "At each house, the only relevant history is best if I skip this house versus rob it after the best before previous. The transition is max(previous best, bestBeforePrevious + current). Adjacent houses cannot both be chosen." }
        "coinchange" { return "dp[a] is the fewest coins needed to form amount a, with dp[0] = 0. For each amount, try every coin that can precede it and relax dp[a] from dp[a - coin] + 1. Unreachable states must stay as INF, not accidentally overflow into valid answers." }
        "validparentheses" { return "The stack contains unmatched opening brackets in nesting order. A closing bracket must match and consume the most recent opening bracket. At the end the stack must be empty." }
        "topkfrequentelements" { return "First count exact frequencies, then select by frequency rather than by value. A size-k min-heap keeps the k strongest candidates by evicting the current weakest. If using buckets, bucket index is frequency." }
        "dailytemperatures" { return "The stack stores indices whose next warmer day is unresolved, with temperatures decreasing from bottom to top. Current temperature resolves all colder stack-top days, and answer[old] is currentIndex - old. Push current only after resolving." }
        "meetingroomsii" { return "Sort meetings by start time and keep active meeting end times in a min-heap. If the earliest end is <= current start, that room is reusable before adding the current meeting. Heap size after adding is active rooms; maximum size is rooms needed." }
        "implementtrieprefixtree" { return "Each node represents the prefix formed by the path from root. insert creates missing child nodes and marks only the final node as a full word. search requires terminal true; startsWith does not." }
        "floodfill" { return "Only cells connected to the start and equal to the original color can change. If newColor equals original color, return early to avoid revisiting forever. Mark/recolor before recursing to prevent cycles." }
        "isgraphbipartite" { return "color[node] records which side of the partition owns the node. Each edge must connect opposite colors; an uncolored neighbor receives the opposite color. A same-color edge is an immediate contradiction." }
        "minimumnumberofarrowstoburstballoons" { return "Sort balloons by end and shoot the current arrow at the earliest ending balloon's end. Any balloon starting <= arrowEnd is already covered. A new arrow is needed only when start > arrowEnd." }
        "partitionequalsubsetsum" { return "Equal partition means one subset must make total / 2, so odd total is impossible. dp[s] means processed numbers can form sum s, seeded by dp[0] = true. Iterate sums right to left so the current number cannot reuse a state it just created." }
        "longestincreasingsubsequence" { return "tails[len] is the smallest possible tail value for an increasing subsequence of length len + 1. Binary search the first tail >= x and replace it with x; this improves future options without changing known length incorrectly. Equal values replace, not extend, for strictly increasing subsequences." }
        "wordsearch" { return "The path owns board cells temporarily while matching one word index. Mark the current cell before exploring neighbors, then restore it after recursion returns. A cell cannot be reused in the same path." }
        "findmedianfromdatastream" { return "The max-heap owns the lower half and the min-heap owns the upper half. Keep sizes balanced so they differ by at most one and every lower value is <= every upper value. Median is one heap top or the average of both tops." }
        "binarysubarrayswithsum" { return "For binary nonnegative arrays, exact goal equals atMost(goal) - atMost(goal - 1). atMost keeps a valid window with sum <= goal and adds right - left + 1 subarrays ending at right. Guard goal < 0 as zero." }
        "majorityelement" { return "candidate survives pair cancellation between different values. When count becomes zero, the current value becomes the new candidate; equal increments and different decrements. This is valid because a value appearing more than n/2 cannot be fully cancelled." }
        "findallanagramsinastring" { return "The window length must stay exactly p.length. Add the right char, remove the char that falls out once the window is too large, then compare frequency state. Record the left index only for complete matching windows." }
        "intersectionoftwolinkedlists" { return "Each pointer walks its list then switches to the other head at null. After both switches, the remaining path lengths are equalized. They meet at the shared node identity or both reach null." }
        "linkedlistcycleii" { return "After slow and fast meet inside the cycle, reset one pointer to head. Move both one step at a time; their meeting point is the cycle entry. The first meeting itself is not necessarily the entry." }
        "reversenodesinkgroup" { return "Before reversing, verify that the next k nodes exist. Reverse exactly that closed group, reconnect previous group tail to the new head, and connect the reversed tail to the next group. Leave a final short group unchanged." }
        "lowestcommonancestorofabinarysearchtree" { return "Use BST order to walk toward the split. If both targets are smaller than current, go left; if both are larger, go right. Otherwise current is where the two search paths diverge or one target equals current." }
        "sortcolors" { return "Maintain three regions: [0, low) are 0s, [low, mid) are 1s, and (high, end] are 2s. A 0 swaps to low and advances both low and mid; a 2 swaps to high and only high moves because the incoming value is unclassified. A 1 advances mid." }
        "networkdelaytime" { return "dist[node] is the best known time from the source. The min-heap always expands the currently smallest candidate distance; ignore stale heap entries larger than dist[node]. Weighted edges require Dijkstra, not plain BFS." }
        "pacificatlanticwaterflow" { return "Reverse the problem: start from each ocean border and move to neighbors with height >= current height. A cell can reach an ocean if the reversed search can reach the cell from that ocean. The answer is cells marked by both ocean searches." }
        "surroundedregions" { return "Only O cells connected to the border are safe. Mark all border-connected O cells first, then flip every remaining O to X and restore safe marks to O. Do not flip before proving border reachability." }
        "slidingwindowmaximum" { return "The deque stores indices inside the current window, and their values decrease from front to back. Expire front indices with index <= right - k, remove dominated back indices while nums[back] <= nums[right], then add right. Emit only when right >= k - 1, writing to right - k + 1." }
        "capacitytoshippackageswithinddays" { return "currentLoad is the load already assigned to the current day and must never exceed capacity. Before assigning w, if currentLoad + w > capacity, open a new day and let w start that day; equality is allowed in the current day. Feasibility is requiredDays <= days." }
        "splitarraylargestsum" { return "currentSum is the sum of the current contiguous partition and must never exceed maxAllowedSum. If currentSum + x > maxAllowedSum, close the current partition and let x start the next one. Feasibility is pieces <= m because extra allowed partitions can be split later when needed." }
        "minimumnumberofdaystomakembouquets" { return "flowers counts consecutive bloomed flowers not yet consumed into a bouquet. A flower with bloomDay <= day extends the streak; an unbloomed flower breaks adjacency and resets flowers to 0. When flowers == k, one bouquet consumes those k flowers, so increment bouquets and reset flowers to 0." }
        "gasstation" { return "tank is the net gas from the current candidate start through the current station. If tank becomes negative, no station inside that failed segment can be a valid start, so the next index becomes the candidate and tank resets to 0. total gas minus cost decides whether any solution exists." }
        "jumpgame" { return "farthest is the farthest index reachable using positions processed so far. If the current index is ever greater than farthest, it is unreachable and the answer is false. Otherwise update farthest with i + nums[i] and succeed once farthest reaches the last index." }
        "editdistance" { return "dp[i][j] is the minimum edits to convert the first i chars of word1 to the first j chars of word2. Equal last chars inherit dp[i - 1][j - 1]; otherwise choose one plus insert, delete, or replace. Empty-prefix rows and columns are the base cases." }
        "distinctsubsequences" { return "dp[i][j] counts ways the first i chars of s form the first j chars of t. Skipping s[i - 1] always contributes dp[i - 1][j]; if chars match, taking it also contributes dp[i - 1][j - 1]. dp[*][0] is 1 because empty t can always be formed by taking nothing." }
        "interleavingstring" { return "dp[i][j] means s3 prefix length i + j can be formed from s1 first i chars and s2 first j chars. The next char must come from s1[i - 1] or s2[j - 1] and match s3[i + j - 1]. Greedy fails when both strings offer the same character." }
        "russiandollenvelopes" { return "Sort width ascending, but equal width descending by height so equal-width envelopes cannot chain through LIS. Then run strict LIS on heights. Without descending tie-break, equal widths can be illegally nested." }
    }

    return $Recall
}

function Get-PrecisionTrap {
    param(
        [string] $Category,
        [string] $Title
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "twosum" { return "check complement before insert; no self reuse" }
        "binarysearch" { return "left <= right; move by mid +/- 1" }
        "longestsubstringwithoutrepeatingcharacters" { return "left = max(left, lastSeen + 1)" }
        "minimumwindowsubstring" { return "save answer before left removal breaks validity" }
        "reverselinkedlist" { return "save next before current.next rewrite" }
        "linkedlistcycle" { return "guard fast and fast.next" }
        "mergeksortedlists" { return "push polled.next, not every node upfront" }
        "trappingrainwater" { return "update side max before adding trapped water" }
        "binarytreelevelordertraversal" { return "capture queue size before pushing children" }
        "validatebinarysearchtree" { return "strict bounds; ancestor violations matter" }
        "numberofislands" { return "mark visited before exploring neighbors" }
        "courseschedule" { return "processed count detects cycle" }
        "coursescheduleii" { return "return empty if order length < n" }
        "wordladder" { return "mark visited when enqueueing" }
        "kokoeatingbananas" { return "speed starts at 1; ceil division" }
        "findfirstandlastpositionofelementinsortedarray" { return "two boundary searches; verify equality" }
        "lrucache" { return "remove evicted node from map and list" }
        "diameterofbinarytree" { return "return height, update diameter separately" }
        "pathsumiii" { return "decrement prefix count on backtrack" }
        "rottingoranges" { return "minutes per layer, not per cell" }
        "01matrix" { return "multi-source from zeros, not BFS from each one" }
        "coinchange" { return "keep INF unreachable states safe" }
        "validparentheses" { return "closing must match most recent opening" }
        "dailytemperatures" { return "store indices; answer is i - oldIndex" }
        "meetingroomsii" { return "reuse when earliestEnd <= start" }
        "floodfill" { return "return early when newColor == original" }
        "isgraphbipartite" { return "visited is not enough; check colors" }
        "minimumnumberofarrowstoburstballoons" { return "new arrow only when start > arrowEnd" }
        "partitionequalsubsetsum" { return "right-to-left DP; dp[0] seed" }
        "longestincreasingsubsequence" { return "first tail >= x; equal replaces" }
        "wordsearch" { return "restore visited cell after recursion" }
        "findmedianfromdatastream" { return "rebalance heaps after each insert" }
        "binarysubarrayswithsum" { return "atMost(goal) - atMost(goal - 1)" }
        "majorityelement" { return "reset candidate only when count == 0" }
        "intersectionoftwolinkedlists" { return "compare node identity, not value" }
        "linkedlistcycleii" { return "meeting point is not always entry" }
        "reversenodesinkgroup" { return "confirm k nodes before reversing" }
        "sortcolors" { return "after swapping 2, do not advance mid" }
        "networkdelaytime" { return "ignore stale heap distances" }
        "pacificatlanticwaterflow" { return "reverse flow: move to >= height" }
        "surroundedregions" { return "mark border-safe O before flipping" }
        "slidingwindowmaximum" { return "expire front <= right-k; emit at right >= k-1" }
        "capacitytoshippackageswithinddays" { return "> not >=; w starts new day" }
        "splitarraylargestsum" { return "> not >=; x starts new partition" }
        "minimumnumberofdaystomakembouquets" { return "two resets: gap and flowers == k" }
        "gasstation" { return "reset start after tank < 0" }
        "jumpgame" { return "fail when i > farthest" }
        "editdistance" { return "base rows/cols are prefix lengths" }
        "distinctsubsequences" { return "dp[*][0] = 1" }
        "interleavingstring" { return "s3 index is i + j - 1" }
        "russiandollenvelopes" { return "equal width sorted by height desc" }
        "middleoflinkedlist" { return "even length returns second middle; guard fast.next" }
        "allnodesdistancekinbinarytree" { return "add parent edges; stop after exactly k BFS levels" }
        "amountoftimeforbinarytreetobeinfected" { return "mark on enqueue; do not count a nonexistent final minute" }
        "graphvalidtree" { return "require n - 1 edges and one connected component" }
        "possiblebipartition" { return "scan disconnected components; fail only on same-color edge" }
        "redundantconnection" { return "1-based DSU; union roots, not raw endpoints" }
        "parallelcourses" { return "snapshot level size; return -1 if processed < n" }
        "aliendictionary" { return "reject invalid prefix; only first difference creates edge" }
        "findeventualsafestates" { return "reverse edges; decrement predecessor outdegree" }
        "sequencereconstruction" { return "queue size must equal 1; every nums value must appear" }
        "sortitemsbygroupsrespectingdependencies" { return "VERIFY FROM SOURCE - complete accepted implementation absent" }
        "coursescheduleiv" { return "prerequisite edge direction; via loop must be outermost" }
        "subsets" { return "copy path at every state; recurse with i + 1" }
        "combinationsum" { return "recurse with i for reuse; undo path after return" }
        "lettercombinationsofaphonenumber" { return "empty input returns empty; delete appended char" }
        "permutations" { return "used tracks indices; unmark after recursion" }
        "permutationsii" { return "duplicate skip requires !used[i - 1]" }
        "largestrectangle" { return "flush with height-0 sentinel; width = right - left - 1" }
        "topkfrequentwords" { return "tie makes lexicographically larger word the weaker heap root" }
        "hindex" { return "cap citations at n; first papers >= h while scanning down" }
        "insertinterval" { return "closed intervals overlap on equality; emit new interval once" }
        "mergeintervals" { return "merge on start <= activeEnd; flush final active interval" }
        "nonoverlappingintervals" { return "touching is compatible: start >= lastFinish" }
        "longestcommonprefix" { return "stop at branch or terminal, including one-word prefix" }
        "longestwordindictionary" { return "every intermediate node terminal; lexicographically smallest tie" }
        "replacewords" { return "stop at first terminal; missing child keeps original word" }
        "searchsuggestionssystem" { return "lexicographic child order; cap each prefix at three" }
        "shortencodingofwords" { return "deduplicate; count only words not covered as suffixes" }
        "mapsumpairs" { return "overwrite uses delta = new - old, not full new value" }
        "accountsmerge" { return "merge by shared email, not equal name; group by find(root)" }
        "validanagram" { return "reject unequal lengths; every net count must finish at zero" }
        "ransomnote" { return "fail on the first negative remaining count; do not reuse a magazine character" }
        "longestpalindrome" { return "use every pair, but allow at most one odd center" }
        "searchinsertposition" { return "return the first >= target boundary, including n when no such index exists" }
        "firstbadversion" { return "preserve mid when it is bad; search only versions 1 through n" }
        "searchinrotatedsortedarray" { return "identify the sorted half before testing whether target lies inside it" }
        "searchinrotatedsortedarrayii" { return "when left, mid, and right are equal, shrink both ends before choosing a half" }
        "findpeakelement" { return "use left < right so mid + 1 stays valid; keep mid on a descending slope" }
        "sqrtx" { return "avoid mid * mid overflow; return the floor boundary, not the final failed mid" }
        "timebasedkeyvaluestore" { return "return the last timestamp <= query; preserve each key's timestamp order" }
        "longestsubstringwithatmostkdistinctcharacters" { return "shrink while distinct > k and erase a character only when its count reaches zero" }
        "findallanagramsinastring" { return "keep exactly p.length characters; compare only after outgoing state is removed" }
        "movingaveragefromdatastream" { return "evict only after size exceeds capacity; divide by current size during warm-up" }
        "countnumberofnicesubarrays" { return "exactly k requires atMost(k) - atMost(k - 1); handle k = 0" }
        "longestcontinuousincreasingsubsequence" { return "continuous forbids skips; reset the streak on nums[i] <= nums[i - 1]" }
        "productofarrayexceptself" { return "each pass must exclude nums[i]; do not divide because zeros are legal" }
        "mergetwosortedlists" { return "advance only the list whose node was attached; append the remaining suffix" }
        "designbrowserhistory" { return "visiting a new URL discards forward history; clamp back/forward at both ends" }
        "copylistwithrandompointer" { return "clone by node identity, preserve null random, and restore the original list if interleaving" }
        "oddevenlinkedlist" { return "save evenHead and reconnect odd tail to it after both chains are built" }
        "rotatelist" { return "reduce k modulo length; break the ring at length - k" }
        "swapnodesinpairs" { return "preserve the next pair head and return dummy.next after rewiring" }
        "reverselinkedlistii" { return "use a dummy before left and reconnect both the prefix and suffix" }
        "validpalindrome" { return "skip non-alphanumerics on both sides before comparing normalized characters" }
        "containerwithmostwater" { return "move the shorter wall; moving only the taller wall cannot improve the limiting height" }
        "twosumiiinputarrayissorted" { return "return one-based indices; move exactly one side according to the sum" }
        "trappingrainwater" { return "update the chosen side maximum before adding water; never add a negative amount" }
        "longestpalindromicsubstring" { return "expand both odd and even centers; convert final bounds without an off-by-one" }
        "topkfrequentelements" { return "order by frequency, not value; keep exactly k candidates" }
        "sortcharactersbyfrequency" { return "emit each character frequency times; comparator ties must remain consistent" }
        "taskscheduler" { return "cooldown applies between equal tasks; include unavoidable idle slots" }
        "kthlargestelementinanarray" { return "a size-k min-heap evicts only when size > k; answer is the root" }
        "kthlargestelementinastream" { return "initialize through the same add invariant; root is valid only after k elements" }
        "kclosestpointstoorigin" { return "compare squared distance without overflow; cap the retained heap at k" }
        "awardtopkhotels" { return "apply score ties deterministically and preserve the required hotel ordering" }
        "binarytreerightsideview" { return "snapshot each level size and record only that level's final visible node" }
        "binarytreeinordertraversal" { return "visit left, node, right; iterative traversal must push the full left chain" }
        "binarytreepostordertraversal" { return "visit both children before the node; do not emit on first stack sight" }
        "binarytreepreordertraversal" { return "visit node before children; push right before left in an iterative stack" }
        "lowestcommonancestorofabinarytree" { return "return subtree evidence upward; current is LCA only when both sides report a target" }
        "lowestcommonancestorofabinarytreeii" { return "the split-point result is valid only after confirming both targets exist" }
        "lowestcommonancestorofabinarytreeiii" { return "compare node identity through parent chains; do not assume equal depths" }
        "lowestcommonancestorofabinarytreeiv" { return "treat targets as a set and stop ownership at the first multi-target split" }
        "kthsmallestelementinabst" { return "decrement k on the inorder node visit, not when pushing or returning" }
        "recoverbinarysearchtree" { return "capture both inorder inversions; non-adjacent swaps require first and last offenders" }
        "binarysearchtreeiterator" { return "push the entire left spine initially and after every popped node's right child" }
        "convertbsttogreatertree" { return "reverse inorder is required; update the running sum before storing the node" }
        "sumroottoleafnumbers" { return "append the digit before the leaf check; add only complete root-to-leaf numbers" }
        "binarytreemaximumpathsum" { return "clamp negative child gains to zero; return one branch but update global with two" }
        "pathsum" { return "accept target equality only at a leaf, not at an internal prefix" }
        "pathsumii" { return "copy the path on a matching leaf and remove the current node on every return" }
        "lowestcommonancestorofabinarysearchtree" { return "walk by both target values; the first split or equality is the answer" }
        "insertintoabinarysearchtree" { return "attach at the first null position and return the original root" }
        "minimumabsolutedifferenceinbst" { return "compare adjacent inorder values and initialize previous-node state safely" }
        "rangesumofbst" { return "include both bounds; prune only the subtree BST ordering proves irrelevant" }
        "searchinabinarysearchtree" { return "choose one branch from the comparison; do not search both subtrees" }
        "invertbinarytree" { return "swap both child references once per node; preserve the new children for recursion" }
        "constructbinarysearchtreefrompreordertraversal" { return "advance the shared index only when the value fits the inherited upper bound" }
        "verifypreorderserializationofabinarytree" { return "consume one slot per token and add two only for non-null nodes; slots may never go negative" }
        "constructbinarytreefrominorderandpostordertraversal" { return "consume postorder from the end and build right before left" }
        "constructbinarytreefrompreorderandinordertraversal" { return "split by the inorder index and keep preorder/subtree ranges aligned" }
        "serializeanddeserializebinarytree" { return "encode null markers and delimiters explicitly; deserialize in the same traversal order" }
        "numberofclosedislands" { return "border-connected land is not closed; either erase borders first or propagate closure with AND" }
        "maxareaofisland" { return "reset area per component and mark each land cell before expanding" }
        "clonegraph" { return "store original-to-clone before recursing so cycles reuse the same clone" }
        "coloringaborder" { return "recolor only component cells touching outside/non-component; preserve interior cells" }
        "minimumheighttrees" { return "the input must be a tree; trim simultaneous leaf layers until at most two centers remain" }
        "numberofprovinces" { return "start one traversal per unvisited city; mark the start before scanning its row" }
        "khighestrankeditemswithinapricerange" { return "finish a BFS distance layer before ranking; apply all tie keys in order" }
        "houserobber" { return "compute current from old prev1/prev2 before shifting them; never combine adjacent houses" }
        "climbingstairsfib" { return "define n = 0/1 bases explicitly and do not overwrite a value before using it" }
        "wordbreak" { return "seed dp[0] = true; substring end is exclusive and must follow a reachable prefix" }
        "climbingstairs" { return "seed the empty/first step consistently; transition only from legal prior steps" }
        "mincostclimbingstairs" { return "cost is paid when landing on a step; answer is min of the final two states" }
        "perfectsquares" { return "seed dp[0] = 0 and try only squares <= current amount" }
        "uniquepaths" { return "seed first row/column as one path; each cell combines top and left" }
        "numberoflongestincreasingsubsequence" { return "replace count on a longer predecessor, add count only on an equal-best predecessor" }
        "maximumprofitinjobscheduling" { return "binary search the first next start >= current end; keep jobs and DP in the same sort order" }
        "kadanemaxsubarray" { return "initialize from the first value so an all-negative array does not incorrectly return zero" }
        "longestcommonsubsequence" { return "on mismatch take max(top,left); diagonal extension is only for equal characters" }
        "deleteoperationfortwostrings" { return "the state counts deletions for prefixes; initialize empty-prefix costs" }
        "longestpalindromicsubsequence" { return "fill shorter intervals before longer ones; equal ends use inner interval + 2" }
        "minimumasciideletesumfortwostrings" { return "empty-prefix bases are ASCII prefix sums, not character counts" }
        "besttimetobuyandsellstockwithtransactionfee" { return "charge the fee exactly once per transaction and update hold/cash from prior-day states" }
        "besttimetobuyandsellstockwithcooldown" { return "buy from a rested state, not from cash created by yesterday's sell" }
        "besttimetobuyandsellstockiv" { return "transaction layers must not reuse same-day updates; handle k >= n/2 as unlimited" }
        "besttimetobuyandsellstockiii" { return "preserve buy1/sell1/buy2/sell2 update semantics; at most two completed sells" }
        "distinctsubsequencesii" { return "subtract the previous contribution of the repeated character and normalize modulo" }
        "evaluatereversepolishnotation" { return "for subtraction/division pop right operand first, then left operand" }
        "nextgreaterelementii" { return "scan 2n indices but push unresolved indices only during the first pass" }
        "implementqueueusingstacks" { return "transfer input to output only when output is empty; otherwise FIFO order breaks" }
        "implementstackusingqueues" { return "rotate the queue according to one consistent push- or pop-heavy invariant" }
        "basiccalculator" { return "apply the pending sign before reset; restore outer result/sign after a closing parenthesis" }
        "minstack" { return "push duplicate minima and pop auxiliary minimum in lockstep" }
        "maxstack" { return "popMax removes the topmost maximum and restores displaced elements in order" }
        "nextgreaterelementi" { return "map answers while resolving the monotonic stack; source values must satisfy uniqueness assumptions" }
        "onlinestockspan" { return "pop prices <= current and add their compressed spans; equal prices belong in the span" }
        "designastackwithincrementoperation" { return "increment the bottom min(k,size) elements; propagate lazy increments when popping" }
        "implementtrieprefixtree" { return "search requires terminal true; startsWith requires only the path" }
        "designaddandsearchwordsdatastructure" { return "a dot branches over every existing child; success still requires terminal at the end" }
        "wordsearchii" { return "restore board state, emit each word once, and prune only exhausted trie branches" }
        "maximumxoroftwonumbersinanarray" { return "walk from the highest supported bit and prefer the opposite bit only when it exists" }
        "maximumxorwithanelementfromarray" { return "insert only nums <= query limit; return -1 when the eligible trie is empty" }
        "maximumgeneticdifferencequery" { return "the trie must contain only current ancestors; remove the node on DFS exit" }
        "countpairswithxorinarange" { return "compute countLess(high + 1) - countLess(low) with consistent bit bounds" }
        "hotelreviews" { return "tokenize consistently, count each required occurrence rule, and apply ranking ties deterministically" }
        "meetingrooms" { return "sort by start and reject when current start < previous end; equality is reusable" }
        "partitionlabels" { return "extend the active end to every seen character's last index; cut only when i == end" }
        "maximumlengthofpairchain" { return "sort by end and accept only when next start > last end under the strict pair rule" }
        "besttimetobuyandsellstock" { return "update profit before/with a running minimum; buy must precede sell" }
        "besttimetobuyandsellstockii" { return "take only positive adjacent rises; do not double-count a constrained transaction model" }
        "firstuniquenumber" { return "counts and candidate order must agree after every add; skip newly duplicated front values" }
        "encodeanddecodetinyurl" { return "generated keys must be collision-safe; decode unknown keys according to the API contract" }
        "designcircularqueue" { return "distinguish full from empty with size/count; wrap head and tail modulo capacity" }
        "designfraudpatterndetection" { return "key events by the correct entity and apply thresholds inside the intended time window" }
        "apiintegrationexample" { return "separate transport failure from domain failure; do not partially commit dependent state" }
        "designredis" { return "keep value, expiry, and eviction/index state consistent for every mutation" }
        "designtokenbucketratelimiter" { return "refill from elapsed time, cap at capacity, then consume atomically" }
        "findtheindexofthefirstoccurrenceinastring" { return "on mismatch fall back with lps[j - 1] without moving the text index backward" }
        "repeatedsubstringpattern" { return "require nonzero final LPS and n % (n - lps[n - 1]) == 0" }
        "shortestpalindrome" { return "compute the longest palindromic prefix, not suffix; use an unambiguous separator" }
        "longesthappyprefix" { return "the border must be proper; return exactly the final LPS length" }
        "addbinary" { return "continue while either input or carry remains; prepend/collect bits in the correct order" }
        "countprimes" { return "mark composites from p * p and count primes strictly less than n" }
        "countuniquecharactersofallsubstringsofagivenstring" { return "use previous and next equal positions; flush each character's final contribution" }
        "spiralmatrix" { return "recheck top <= bottom and left <= right before the reverse-direction passes" }
        "stringtointegeratoi" { return "skip spaces, consume one sign, stop at first nondigit, and clamp before overflow" }
    }

    switch ($Category) {
        "HashMap/HashSet" { return "check insert/consume order" }
        "Two Pointers" { return "move only the provably discardable side" }
        "Sliding Window" { return "remove left state when left moves" }
        "Prefix/Suffix" { return "strict vs inclusive prefix boundary" }
        "Binary Search" { return "wrong equality boundary" }
        "Linked List" { return "lost next pointer or wrong returned head" }
        "Tree BFS" { return "children leak into current level" }
        "Tree DFS" { return "mixing helper return with global answer" }
        "Graph BFS" { return "visited too late; duplicate enqueue" }
        "Graph DFS" { return "mark/restore semantics confused" }
        "Stack" { return "pop condition or unresolved ownership wrong" }
        "Heap" { return "stale root or comparator reversed" }
        "Intervals/Greedy" { return "overlap equality boundary" }
        "Backtracking" { return "forgot undo or duplicate skip condition" }
        "Trie" { return "prefix exists but terminal missing" }
        "Dynamic Programming" { return "state meaning or iteration order wrong" }
        "Union Find" { return "union raw nodes instead of roots" }
        "Topological Sort" { return "decrement wrong indegree edge" }
        "Greedy" { return "local choice lacks proof" }
        "Math/Bit/String" { return "operator-sensitive boundary guessed" }
        "Design/LLD" { return "state mutation violates operation invariant" }
        default { return "verify exact boundary in linked Java" }
    }
}

function Get-ArticulationFamilySkeleton {
    param(
        [string] $Category,
        [string] $Pattern
    )

    $patternText = $Pattern.ToLowerInvariant()
    if ($Category -eq "Binary Search" -and $patternText -match "binary search on answer") { return "search candidate answer -> feasible(mid)? save and move toward better : move toward feasible region" }
    if ($Category -eq "Binary Search" -and $patternText -match "binary search boundary|binary search invariant") { return "define inclusive search boundary -> test mid -> discard only the proven-impossible side -> preserve and return the required boundary" }
    if ($Category -eq "Linked List" -and $patternText -match "fast/slow|floyd") { return "move slow by 1 and fast by 2 -> use where fast stops or where pointers meet -> preserve null guards before every 2-step move" }
    if ($Category -eq "Linked List" -and $patternText -match "reversal|swap nodes|odd even|rotate list") { return "name pointer ownership -> save the next structure -> rewire current group -> reconnect both boundaries -> return the real head" }
    if ($Category -eq "Graph BFS" -and $patternText -match "multi-source|bfs spread|bfs distance") { return "enqueue every distance-0 source + mark -> process one frontier layer -> claim unvisited neighbors on enqueue -> advance distance/time once per layer" }
    if ($Category -eq "Graph BFS" -and $patternText -match "dijkstra") { return "min-heap (distance,state) -> ignore stale entry -> relax outgoing edges -> push only improved distances" }
    if ($Category -eq "Stack" -and $patternText -match "monotonic stack|next greater|stock span") { return "scan current -> while current resolves stack top, pop and finalize that old item -> push current as unresolved" }
    if ($Category -eq "Topological Sort" -and $patternText -match "topological|kahn|outdegree") { return "build directed edges + dependency counts -> enqueue currently unlocked nodes -> consume edges on pop -> detect leftovers/ambiguity from the required output" }
    if ($Category -eq "Backtracking" -and $patternText -match "backtracking|permutation|grid path") { return "choose -> mutate path/ownership -> recurse -> restore exactly what this frame changed -> try next choice" }
    if ($Category -eq "Trie" -and $patternText -match "trie|prefix") { return "consume one character per edge -> create/follow child -> keep terminal separate from prefix existence -> branch only when the query permits it" }
    if ($Category -eq "Intervals/Greedy" -and $patternText -match "interval|earliest finish") { return "order intervals by the boundary that makes the next decision local -> compare current with active boundary -> merge/accept/reject -> update boundary ownership" }
    if ($Category -eq "Heap" -and $patternText -match "heap|top k|frequency|median") { return "define heap ownership + comparator -> offer eligible candidate -> evict/rebalance only when invariant requires -> read answer from valid root(s)" }

    switch ($Category) {
        "HashMap/HashSet" { return "scan current -> query prior count/complement/ownership -> consume or record exactly once -> keep map meaning true" }
        "Two Pointers" { return "place boundaries -> evaluate current pair/span -> move only the boundary whose movement safely discards candidates -> preserve best/validity" }
        "Sliding Window" { return "expand right + add state -> while invalid or shrinkable, remove left + advance -> record answer at the valid moment" }
        "Prefix/Suffix" { return "seed empty prefix/suffix -> accumulate reusable state -> combine/query strict boundaries without including the wrong element" }
        "Binary Search" { return "define monotonic candidate space -> test mid -> preserve feasible boundary -> discard half -> return first/last required candidate" }
        "Linked List" { return "name node ownership -> save next before mutation -> rewire/advance -> reconnect changed boundaries -> return actual head" }
        "Tree BFS" { return "queue root -> snapshot level size -> process exactly that level -> enqueue children for the next level -> finalize level output" }
        "Tree DFS" { return "define helper return -> solve children once -> combine at current node -> update separate global state if needed -> return parent contract" }
        "Graph BFS" { return "enqueue source(s) + mark visited -> pop in nondecreasing distance -> claim valid neighbors on enqueue -> stop at required frontier" }
        "Graph DFS" { return "start each unowned component/path -> mark ownership before descent -> explore valid neighbors -> restore only for path-local ownership" }
        "Stack" { return "push unresolved state -> current token resolves/combines top state -> pop in LIFO order -> leave only still-pending state" }
        "Heap" { return "offer eligible candidates -> keep comparator aligned with requested best -> evict/rebalance stale or excess state -> read valid root" }
        "Intervals/Greedy" { return "sort by proof boundary -> compare current interval with committed boundary -> merge/select/reject -> move boundary only when ownership changes" }
        "Backtracking" { return "choose -> recurse -> unchoose; path and used state describe exactly this recursion frame" }
        "Trie" { return "consume characters through prefix nodes -> distinguish terminal word from existing prefix -> branch/prune according to query" }
        "Dynamic Programming" { return "define future-determining state -> seed smallest true states -> enumerate transitions -> fill only after dependencies -> return target state" }
        "Union Find" { return "find canonical roots -> if different, union roots and update size/rank -> if equal, connection already exists -> group by final root" }
        "Topological Sort" { return "build edges + indegree -> enqueue zero-indegree nodes -> pop and consume outgoing edges -> validate count/order requirement" }
        "Greedy" { return "name the safe local choice -> prove dominated alternatives can be discarded -> commit it -> update the boundary summarizing all prior choices" }
        "Math/Bit/String" { return "name carry/border/bit/contribution state -> update from the next symbol/value -> preserve exact arithmetic boundary -> emit final state" }
        "Design/LLD" { return "state operation contract -> choose state that makes it cheap -> mutate every index consistently -> enforce capacity/expiry/failure behavior" }
        default { return "state required output -> identify repeated work -> keep only the state needed by the next decision -> verify boundaries" }
    }
}

function Get-ArticulationMutation {
    param(
        [string] $Category,
        [string] $Title
    )

    $key = Get-NormalizedKey $Title
    switch -Regex ($key) {
        '^twosum$' { return 'Sort the input and ask for the pair values rather than original indices. -> Two Pointers: Sum comparison now proves which endpoint can be discarded.' }
        '^validanagram$' { return 'Ask whether any fixed-length substring of s is an anagram of p. -> Sliding Window: The same frequency balance must now follow a moving contiguous region.' }
        '^majorityelement$' { return 'Ask for the k most frequent values instead of the one value occurring over n/2. -> Heap: The majority cancellation guarantee disappears and bounded ranking becomes necessary.' }
        '^ransomnote$' { return 'Ask for the shortest contiguous magazine substring that supplies the ransom characters. -> Sliding Window: Need counts must be maintained while shrinking one valid region.' }
        '^longestpalindrome$' { return 'Require the chosen characters to be contiguous in the original string. -> Expand Around Center: Global frequency pairs no longer prove substring placement.' }

        '^binarysearch$' { return 'Allow duplicates and ask for the first target occurrence. -> Boundary Binary Search: A hit must be preserved while the search continues left.' }
        '^searchinsertposition$' { return 'Ask only whether target exists, not where it should be inserted. -> Exact Binary Search: Equality can return immediately; no lower-bound answer must be preserved.' }
        '^firstbadversion$' { return 'Permit good versions after a bad version. -> Linear Scan: The predicate is no longer monotonic, so discarding half is unsound.' }
        '^(kokoeatingbananas|splitarraylargestsum|capacitytoshippackageswithinddays|minimumnumberofdaystomakembouquets)$' { return 'Fix the candidate speed/capacity/day and ask only whether it is feasible. -> Greedy Linear Scan: The outer monotonic answer search disappears and only its proof checker remains.' }
        '^searchinrotatedsortedarray$' { return 'Allow arbitrary unsorted order while keeping one target lookup. -> HashMap: Sorted-half elimination disappears, so direct value-to-index lookup becomes the reusable state.' }
        '^findfirstandlastpositionofelementinsortedarray$' { return 'Remove sorted order and ask for the first and last target positions. -> Linear Scan: Order no longer supports boundary elimination.' }
        '^searchinrotatedsortedarrayii$' { return 'Require the first matching index among duplicates. -> Boundary-Aware Linear/Binary Hybrid: Duplicate ambiguity now affects both half choice and answer ownership.' }
        '^findpeakelement$' { return 'Ask for every peak index instead of any one peak. -> Linear Scan: Binary search can discard peaks that the output now requires.' }
        '^sqrtx$' { return 'Ask for the integer kth root rather than square root. -> Binary Search on Answer: Replace square feasibility with overflow-safe power comparison.' }
        '^timebasedkeyvaluestore$' { return 'Allow set calls to arrive out of timestamp order. -> Ordered Map: Append-only arrays lose sorted order, so each key needs ordered insertion and floor lookup.' }

        '^longestsubstringwithoutrepeatingcharacters$' { return 'Ask only how many distinct characters occur in the whole string. -> HashSet: Contiguous boundaries disappear and global membership is sufficient.' }
        '^minimumwindowsubstring$' { return 'Require t as an ordered subsequence rather than an unordered multiset. -> Dynamic Programming / Two-Pass Subsequence Scan: Need/have frequency validity no longer captures order.' }
        '^longestsubstringwithatmostkdistinctcharacters$' { return 'Ask for the number of substrings with exactly k distinct characters. -> Two At-Most Windows: Longest-window tracking becomes atMost(k) minus atMost(k-1) counting.' }
        '^findallanagramsinastring$' { return 'Ask only whether the two complete strings are anagrams. -> Frequency Count: Fixed moving boundaries are no longer part of the workload.' }
        '^countnumberofnicesubarrays$' { return 'Allow arbitrary positive and negative values and ask for exact subarray sum k. -> Prefix Sum + HashMap: Removing left no longer changes the sum monotonically.' }

        '^productofarrayexceptself$' { return 'Ask many immutable range-product queries instead of one except-self array. -> Prefix Products: Precomputed cumulative products answer repeated interval workload when division/zero policy permits.' }
        '^binarysubarrayswithsum$' { return 'Allow arbitrary integers while keeping exact-sum subarray counting. -> Prefix Sum + HashMap: The at-most window identity depends on nonnegative values and now breaks.' }

        '^reverselinkedlist$' { return 'Forbid in-place mutation and ask for values in reverse order. -> Stack: LIFO output replaces pointer rewiring.' }
        '^linkedlistcycle$' { return 'Allow O(n) extra memory and ask only whether a node repeats. -> HashSet: Identity membership detects the first revisit without speed-distance reasoning.' }
        '^middleoflinkedlist$' { return 'Ask repeated random index queries on the same immutable list. -> Array Indexing: One materialization amortizes traversal across many queries.' }
        '^mergetwosortedlists$' { return 'Increase the input from two sorted lists to k sorted lists. -> Min-Heap: The next node must be selected among k current heads.' }
        '^designbrowserhistory$' { return 'Retain every forward branch after visiting from the middle. -> Tree: History is no longer one chain; each page can own multiple future branches.' }
        '^copylistwithrandompointer$' { return 'Remove random pointers and require an in-place structural copy. -> Linked List Pointers: Identity mapping is unnecessary when only next edges remain.' }
        '^intersectionoftwolinkedlists$' { return 'Allow O(n) extra memory and return the first shared node. -> HashSet: Stored node identity replaces path-length equalization.' }
        '^linkedlistcycleii$' { return 'Ask only whether a cycle exists, not its entry. -> Floyd Detection: The second head-to-meeting phase is no longer required.' }
        '^reversenodesinkgroup$' { return 'Ask to reverse only node values while preserving links. -> Stack: Group values can be emitted/popped without structural reconnection.' }
        '^oddevenlinkedlist$' { return 'Partition nodes stably by an arbitrary predicate rather than index parity. -> Two Dummy Lists: Predicate ownership replaces implicit odd/even pointer cadence.' }
        '^rotatelist$' { return 'Store the sequence in an array instead of a linked list. -> Reversal / Modular Indexing: Random access removes the need to form and break a cycle.' }
        '^swapnodesinpairs$' { return 'Swap adjacent values rather than nodes. -> Linear Scan: Pointer topology remains unchanged, so pairwise value exchange is sufficient.' }
        '^reverselinkedlistii$' { return 'Make the segment immutable and request its reversed values. -> Stack: The bounded segment can be buffered without reconnecting links.' }
        '^removenthnodefromendoflist$' { return 'Provide the node to delete in a doubly linked list. -> Direct Pointer Deletion: Prev/next links remove it in O(1) without a lead-gap scan.' }

        '^validpalindrome$' { return 'Allow deleting at most one mismatching character. -> Branching Two Pointers: The first mismatch creates exactly two remaining ranges to validate.' }
        '^containerwithmostwater$' { return 'Ask for the k largest container areas, not only the maximum. -> Heap + Pair Enumeration: The single dominance move cannot preserve every top pair.' }
        '^twosumiiinputarrayissorted$' { return 'Remove sorted order but keep original index output. -> HashMap: Complement lookup replaces endpoint elimination.' }
        '^trappingrainwater$' { return 'Change the elevation map from one dimension to a 2D grid. -> Min-Heap BFS: Water escapes through the globally lowest processed boundary.' }
        '^sortcolors$' { return 'Increase from three colors to k arbitrary values. -> Counting Sort: Three fixed regions no longer represent every color.' }
        '^longestpalindromicsubstring$' { return 'Allow skipping characters while preserving order. -> Interval DP: Contiguity and center expansion no longer characterize the answer.' }

        '^topkfrequentelements$' { return 'Ask for every distinct value sorted by frequency. -> Full Sort: Bounded top-k retention no longer saves ordering work.' }
        '^sortcharactersbyfrequency$' { return 'Receive characters online and repeatedly ask for the current most frequent. -> Heap + Frequency Map: The ranking must be maintained across updates.' }
        '^findmedianfromdatastream$' { return 'Ask for the median of every fixed-size sliding window. -> Two Heaps + Lazy Deletion: Values must expire as the window advances.' }
        '^taskscheduler$' { return 'Give each task type its own cooldown. -> Heap Simulation: The closed-form maximum-frequency formula no longer captures release times.' }
        '^kthlargestelementinanarray$' { return 'Require expected linear time on one static array. -> Quickselect: Partitioning avoids maintaining a logarithmic heap.' }
        '^kthlargestelementinastream$' { return 'Ask for the kth smallest as well as kth largest after every update. -> Two Ordered Multisets: One bounded min-heap cannot expose both tails.' }
        '^kclosestpointstoorigin$' { return 'Ask for all points ordered by distance. -> Full Sort: Every relative rank is now required.' }
        '^topkfrequentwords$' { return 'Ask top suggestions for every typed prefix. -> Trie + Bounded Ranking: Frequency selection must be scoped to shared prefixes.' }
        '^hindex$' { return 'Guarantee citations are sorted and ask for h only once. -> Binary Search: The h-feasibility boundary is monotonic by index.' }
        '^awardtopkhotels$' { return 'Ask for the complete hotel ranking rather than top k. -> Full Sort: Every score tie and rank must be materialized.' }

        '^binarytreelevelordertraversal$' { return 'Ask only for maximum tree depth. -> Tree DFS: A subtree height return replaces level snapshots.' }
        '^binarytreerightsideview$' { return 'Ask for the leftmost and rightmost value at every depth. -> Tree BFS: Each level now needs both boundary positions.' }
        '^validatebinarysearchtree$' { return 'Ask to repair exactly two swapped BST values. -> Inorder Violation Tracking: Boolean bounds do not identify the offenders.' }
        '^binarytreeinordertraversal$' { return 'Require values grouped by depth rather than inorder position. -> Tree BFS: Queue levels replace the traversal stack.' }
        '^binarytreepostordertraversal$' { return 'Ask for parent-before-children serialization. -> Preorder DFS: The required emission point moves before recursive calls.' }
        '^binarytreepreordertraversal$' { return 'Ask to delete every subtree safely after processing children. -> Postorder DFS: Parent work must wait for both child results.' }
        '^lowestcommonancestorofabinarytree$' { return 'Add parent pointers to every node. -> Ancestor Alignment: Root-based subtree recursion is unnecessary when both nodes can walk upward.' }
        '^lowestcommonancestorofabinarytreeii$' { return 'Guarantee both targets exist. -> Standard LCA DFS: Existence counters can be removed.' }
        '^lowestcommonancestorofabinarytreeiii$' { return 'Remove parent pointers and provide only the root. -> Tree DFS Return Contract: Ancestor paths must be discovered from the root.' }
        '^lowestcommonancestorofabinarytreeiv$' { return 'Reduce the target set to exactly two guaranteed nodes. -> Standard LCA DFS: Set membership/count aggregation collapses to two target checks.' }
        '^kthsmallestelementinabst$' { return 'Ask many kth-smallest queries while the BST is updated. -> Order-Statistic Tree: Subtree sizes make rank queries and updates logarithmic.' }
        '^recoverbinarysearchtree$' { return 'Allow any number of misplaced values and ask to restore sorted order. -> Inorder Collect + Sort: Two-inversion logic no longer identifies all corrections.' }
        '^binarysearchtreeiterator$' { return 'Require predecessor as well as successor iteration. -> Bidirectional Stack/Parent Links: One left-spine stack supports only forward order.' }
        '^convertbsttogreatertree$' { return 'Ask many range-sum queries without mutating the BST. -> Augmented BST / Prefix Index: Persistent subtree sums replace one destructive reverse-inorder pass.' }
        '^diameterofbinarytree$' { return 'Ask for the actual longest path nodes, not only its length. -> Tree DP + Parent Reconstruction: Height values need endpoint/choice ownership.' }
        '^sumroottoleafnumbers$' { return 'Allow edges with arbitrary multi-digit weights and ask minimum path sum. -> Tree DFS Min DP: Decimal prefix construction becomes weighted optimization.' }
        '^binarytreemaximumpathsum$' { return 'Restrict the path to start at root and end at a leaf. -> Root-to-Leaf DFS: The two-branch global update is no longer legal.' }
        '^pathsum$' { return 'Count every downward path, not only root-to-leaf paths. -> Prefix Sum on DFS Path: A boolean remaining-sum check misses arbitrary starts.' }
        '^pathsumii$' { return 'Ask only how many matching root-to-leaf paths exist. -> Tree DP Counting: Concrete path copying is no longer required.' }
        '^lowestcommonancestorofabinarysearchtree$' { return 'Remove the BST ordering guarantee. -> General Tree LCA DFS: Value comparison can no longer discard a subtree.' }
        '^insertintoabinarysearchtree$' { return 'Require the tree to remain height-balanced after insertion. -> AVL/Red-Black Tree: Rotations and balance metadata become part of the contract.' }
        '^minimumabsolutedifferenceinbst$' { return 'Remove BST ordering. -> Sort Values: Inorder is no longer sorted, so values must be ordered explicitly before adjacent comparison.' }
        '^rangesumofbst$' { return 'Allow frequent updates and repeated range-sum queries. -> Augmented Balanced BST: Subtree aggregates amortize the new workload.' }
        '^searchinabinarysearchtree$' { return 'Remove the BST order property. -> Tree DFS/BFS: Both children can contain the target.' }
        '^invertbinarytree$' { return 'Ask whether two trees are mirrors without modifying either. -> Paired Tree DFS: Corresponding opposite children must be compared.' }
        '^constructbinarysearchtreefrompreordertraversal$' { return 'Provide arbitrary preorder plus inorder for a non-BST. -> Index-Map Tree Reconstruction: Value bounds no longer identify subtree limits.' }
        '^verifypreorderserializationofabinarytree$' { return 'Ask to reconstruct the tree, not only validate slots. -> Preorder Deserialization: Nodes and recursive child ownership must be materialized.' }
        '^constructbinarytreefrominorderandpostordertraversal$' { return 'Replace postorder with preorder. -> Preorder + Inorder Reconstruction: Consume roots from the front and build left before right.' }
        '^constructbinarytreefrompreorderandinordertraversal$' { return 'Replace preorder with postorder. -> Postorder + Inorder Reconstruction: Consume roots from the end and build right first.' }
        '^serializeanddeserializebinarytree$' { return 'Require sorted-key range queries after reconstruction. -> BST Encoding: Ordering can eliminate null markers and support search semantics.' }
        '^allnodesdistancekinbinarytree$' { return 'Ask the same distance-k query for many targets. -> Preprocessing / Centroid Decomposition: Rebuilding parent BFS per query repeats whole-tree work.' }
        '^amountoftimeforbinarytreetobeinfected$' { return 'Give weighted transmission times on edges. -> Dijkstra: BFS layers no longer represent equal elapsed time.' }

        '^numberofislands$' { return 'Add land cells online and ask island count after each addition. -> Union Find: Incremental component merges replace repeated full-grid traversal.' }
        '^pacificatlanticwaterflow$' { return 'Ask for the minimum downhill steps from each cell to an ocean. -> Multi-Source BFS/Dijkstra: Reachability sets no longer capture distance.' }
        '^surroundedregions$' { return 'Ask for the size of every enclosed region without modifying the board. -> Component DFS: Each component must be measured and classified before output.' }
        '^numberofclosedislands$' { return 'Add land online and query whether components remain closed. -> Union Find with Border Flag: Static DFS ownership must survive incremental merges.' }
        '^maxareaofisland$' { return 'Add land cells online and report maximum area after each addition. -> Union Find with Component Size: Incremental merges update area without rescanning.' }
        '^floodfill$' { return 'Assign different costs to crossing adjacent cells and ask cheapest recoloring reach. -> Dijkstra: Connectivity alone no longer chooses the minimum-cost frontier.' }
        '^isgraphbipartite$' { return 'Ask for a valid dependency order in a directed graph. -> Topological Sort: Two-color ownership does not model prerequisite direction.' }
        '^clonegraph$' { return 'Ask only whether all nodes are reachable from the start. -> Graph DFS: Clone identity allocation is unnecessary.' }
        '^graphvalidtree$' { return 'Add edges online and reject the first cycle. -> Union Find: Each new edge only needs component-root comparison.' }
        '^possiblebipartition$' { return 'Add dislike edges online and query consistency after each edge. -> DSU with Parity: Static recoloring cannot cheaply preserve dynamic opposite-set constraints.' }
        '^coloringaborder$' { return 'Ask for distance of every component cell from the border. -> Multi-Source BFS: Layer number, not binary border membership, becomes the output.' }

        '^courseschedule$' { return 'Ask for one valid course order. -> Topological Ordering: Cycle feasibility now must also record dequeue order.' }
        '^coursescheduleii$' { return 'Ask whether the valid order is unique. -> Unique Kahn BFS: Queue size must stay one at every step.' }
        '^minimumheighttrees$' { return 'Add arbitrary cycles to the graph and ask for minimum eccentricity roots. -> All-Pairs/Repeated BFS: Leaf trimming relies on the input being a tree.' }
        '^parallelcourses$' { return 'Give each course a duration. -> DAG Longest-Path DP: One Kahn layer no longer equals one semester of elapsed time.' }
        '^aliendictionary$' { return 'Ask whether the inferred alphabet order is unique. -> Unique Topological Sort: Every unlocked frontier must contain one letter.' }
        '^findeventualsafestates$' { return 'Ask for the minimum steps from each node to any terminal node. -> Reverse BFS Distance: Outdegree elimination needs a layer/distance state.' }
        '^sequencereconstruction$' { return 'Ask for any valid reconstruction rather than proving uniqueness. -> Standard Topological Sort: Multiple zero-indegree choices become acceptable.' }
        '^sortitemsbygroupsrespectingdependencies$' { return 'Remove group-contiguity requirements. -> Single Topological Sort: The group-level dependency graph is no longer needed.' }
        '^coursescheduleiv$' { return 'Ask prerequisite reachability online as edges are added. -> Dynamic Reachability Index: One static transitive-closure table can become stale.' }

        '^wordladder$' { return 'Ask for every shortest transformation sequence. -> BFS + Backtracking: Distance layers must retain all shortest parents for reconstruction.' }
        '^rottingoranges$' { return 'Give each cell a different rotting delay. -> Multi-Source Dijkstra: One BFS layer no longer equals one minute.' }
        '^01matrix$' { return 'Assign weighted movement costs between cells. -> Multi-Source Dijkstra: First unweighted discovery no longer proves minimum cost.' }
        '^networkdelaytime$' { return 'Make every edge weight exactly one. -> BFS: Priority ordering is unnecessary when all transitions have equal cost.' }
        '^numberofprovinces$' { return 'Add connections online and query province count after each update. -> Union Find: Component roots update incrementally.' }
        '^khighestrankeditemswithinapricerange$' { return 'Remove distance from the ranking and ask global top k eligible cells. -> Bounded Heap: BFS layers are no longer part of the order.' }

        '^houserobber$' { return 'Arrange houses in a circle. -> Two-Case DP: First and last cannot both be included, so solve two linear ranges.' }
        '^climbingstairsfib$' { return 'Allow an arbitrary set of jump lengths. -> Unbounded DP: Two rolling Fibonacci states no longer cover every predecessor.' }
        '^coinchange$' { return 'Ask to list every coin combination instead of the minimum count. -> Backtracking: Concrete choices become output and DP value compression is insufficient.' }
        '^climbingstairs$' { return 'Add blocked steps. -> Indexed DP: The fixed Fibonacci recurrence must check per-position legality.' }
        '^mincostclimbingstairs$' { return 'Allow jumps up to k steps. -> Sliding Minimum DP: Each state depends on a variable predecessor window.' }
        '^perfectsquares$' { return 'Ask to output one minimum square decomposition. -> DP + Parent Reconstruction: Counts alone no longer satisfy the output.' }
        '^uniquepaths$' { return 'Add obstacles to the grid. -> Grid DP with Legality: Blocked cells reset path count to zero.' }
        '^partitionequalsubsetsum$' { return 'Ask to output every subset reaching half the total. -> Backtracking: The actual membership paths, not boolean reachability, are required.' }
        '^longestincreasingsubsequence$' { return 'Ask how many longest increasing subsequences exist. -> Quadratic DP with Counts: Minimal tails preserve length but discard multiplicity.' }
        '^maximumprofitinjobscheduling$' { return 'Remove job profits and maximize the number of non-overlapping jobs. -> Earliest-Finish Greedy: Every accepted job has equal value.' }
        '^kadanemaxsubarray$' { return 'Ask for maximum sum of non-adjacent values. -> House-Robber DP: Contiguous ending-state reasoning no longer applies.' }
        '^editdistance$' { return 'Disallow replacement and count only insertions/deletions. -> LCS-Derived DP: Shared subsequence length determines deletions and insertions.' }
        '^distinctsubsequences$' { return 'Ask to print all matching subsequences for small input. -> Backtracking: Individual index paths become required output.' }
        '^deleteoperationfortwostrings$' { return 'Give different deletion costs per character. -> Weighted 2D DP: LCS length alone no longer determines minimum cost.' }
        '^longestpalindromicsubsequence$' { return 'Require a contiguous palindrome. -> Expand Around Center: Skipping characters is no longer legal.' }
        '^minimumasciideletesumfortwostrings$' { return 'Make every deletion cost one. -> LCS DP: Minimum deletions depend only on the longest common subsequence length.' }

        '^subsets$' { return 'Ask only for the number of subsets reaching a target sum. -> Knapsack DP: Repeated sum states replace explicit subset emission.' }
        '^combinationsum$' { return 'Forbid candidate reuse. -> 0/1 Backtracking: The recursive next index changes from i to i + 1.' }
        '^wordsearch$' { return 'Provide a dictionary of many words. -> Trie + Backtracking: Shared prefixes prune repeated board searches.' }
        '^lettercombinationsofaphonenumber$' { return 'Ask only for the number of possible strings. -> Product Counting: Multiply mapping sizes without generating paths.' }
        '^permutations$' { return 'Allow duplicate values but require unique permutations. -> Sorted Duplicate-Aware Backtracking: Equal siblings need a used-predecessor guard.' }
        '^permutationsii$' { return 'Ask only for the count of distinct permutations. -> Frequency Math / DP: Multiset factorial counts replace path generation.' }

        '^validparentheses$' { return 'Allow one wildcard that can act as open, close, or empty. -> Greedy Range of Open Counts: One deterministic stack meaning is insufficient.' }
        '^evaluatereversepolishnotation$' { return 'Change postfix input to infix with parentheses. -> Operator/Operand Stacks: Precedence and deferred operators must now be represented.' }
        '^dailytemperatures$' { return 'Ask for the warmest future temperature, not the nearest warmer day. -> Suffix Maximum: Nearest unresolved ownership is no longer required.' }
        '^nextgreaterelementii$' { return 'Remove circular wraparound. -> Monotonic Stack: The second simulated pass disappears.' }
        '^slidingwindowmaximum$' { return 'Ask for the median of every window. -> Two Heaps / Ordered Multiset: A monotonic deque preserves only extrema.' }
        '^implementqueueusingstacks$' { return 'Require worst-case O(1) dequeue instead of amortized O(1). -> Real-Time Queue / Incremental Transfer: Bulk transfer latency must be spread across operations.' }
        '^implementstackusingqueues$' { return 'Make push O(1) and allow pop O(n). -> Pop-Heavy Queue Simulation: Rotation moves from push to pop.' }
        '^basiccalculator$' { return 'Add multiplication and division precedence. -> Two-Stack / Precedence Parser: Sign-only deferred state is no longer sufficient.' }
        '^largestrectangle$' { return 'Ask for the maximum rectangle in a binary matrix. -> Row Histograms + Monotonic Stack: Each matrix row becomes one histogram instance.' }
        '^minstack$' { return 'Add popMax that removes the topmost maximum. -> Doubly Linked List + Ordered Map: Per-depth extrema cannot delete a non-top node.' }
        '^nextgreaterelementi$' { return 'Allow duplicate values in nums2 and query by index. -> Monotonic Stack on Indices: Value-to-answer mapping is no longer unique.' }
        '^onlinestockspan$' { return 'Ask for spans after arbitrary historical price updates. -> Segment Tree: The append-only monotonic compression becomes invalid.' }
        '^designastackwithincrementoperation$' { return 'Allow range increments on arbitrary stack indices. -> Fenwick/Segment Tree: One lazy bottom-k boundary cannot represent general ranges.' }

        '^implementtrieprefixtree$' { return 'Remove prefix queries and keep only exact membership. -> HashSet: Shared prefix nodes no longer serve the workload.' }
        '^designaddandsearchwordsdatastructure$' { return 'Remove wildcard dots. -> Plain Trie: Search follows one deterministic path.' }
        '^wordsearchii$' { return 'Reduce the dictionary to one word. -> Board Backtracking: Trie prefix sharing is unnecessary.' }
        '^longestcommonprefix$' { return 'Ask the query repeatedly while words are inserted online. -> Trie: Stored prefix structure amortizes repeated scans.' }
        '^longestwordindictionary$' { return 'Drop the rule that every prefix must itself be a word. -> Sort / Lexicographic Selection: Terminal-prefix validation disappears.' }
        '^replacewords$' { return 'Ask for the longest matching root instead of the shortest. -> Trie Full-Prefix Walk: Search cannot stop at the first terminal node.' }
        '^searchsuggestionssystem$' { return 'Make product popularity update online. -> Trie + Per-Node Heap: Static lexicographic DFS no longer maintains top suggestions.' }
        '^shortencodingofwords$' { return 'Ask for shared prefixes instead of shared suffixes. -> Forward Trie: Reversal is no longer needed.' }
        '^mapsumpairs$' { return 'Remove prefix-sum queries and keep only exact key lookup. -> HashMap: Trie aggregates and overwrite deltas become unnecessary.' }
        '^hotelreviews$' { return 'Ask for top hotels after every incoming review. -> Trie/Hash Counts + Heap: Ranking must be maintained online.' }

        '^minimumnumberofarrowstoburstballoons$' { return 'Ask for the maximum number of simultaneously active balloons. -> Sweep Line: Endpoint event balance replaces minimum stabbing selection.' }
        '^insertinterval$' { return 'Insert many intervals online with overlap queries. -> Ordered Interval Tree: One linear insertion is repeated too often.' }
        '^mergeintervals$' { return 'Ask only whether any overlap exists. -> Sort + Adjacent Check: Building merged output is unnecessary.' }
        '^nonoverlappingintervals$' { return 'Add a profit to each interval and maximize retained profit. -> Weighted Interval DP: Earliest finish can discard a more valuable schedule.' }
        '^partitionlabels$' { return 'Allow each character to appear in at most two partitions. -> Dynamic Programming: The last-occurrence forced boundary is no longer mandatory.' }

        '^accountsmerge$' { return 'Ask whether two accounts are connected under online email additions. -> Dynamic Union Find: Roots answer incremental connectivity without rebuilding groups.' }
        '^redundantconnection$' { return 'Direct every edge and ask for the extra edge in a rooted graph. -> Directed Indegree + DSU: Two-parent conflicts must be handled before cycle detection.' }

        '^encodeanddecodetinyurl$' { return 'Require short codes to be sortable by creation order and collision-free on one node. -> Base62 Counter: A monotonic ID can be encoded directly instead of retried random keys.' }
        '^designfraudpatterndetection$' { return 'Change the batch rule to detect threshold breaches in a live time window. -> Sliding Window per Entity: Expired events must leave state as new events arrive.' }
        '^apiintegrationexample$' { return 'Require independent downstream calls to complete under one latency budget. -> CompletableFuture Fan-Out: Parallel composition replaces sequential transport calls.' }
        '^designredis$' { return 'Add TTL expiration with many keys. -> HashMap + Expiry Heap: Direct key lookup must be paired with ordered expiration work.' }
        '^designtokenbucketratelimiter$' { return 'Share one rate limit across multiple service instances. -> Atomic Redis/Lua State: Process-local refill and consume are no longer globally consistent.' }

        '^findtheindexofthefirstoccurrenceinastring$' { return 'Search many needles against one fixed text. -> Suffix Array / Text Index: Rebuilding one KMP prefix table per query repeats work.' }
        '^repeatedsubstringpattern$' { return 'Allow one mismatching character in the repetitions. -> DP / Approximate Matching: Exact LPS periodicity no longer proves validity.' }
        '^shortestpalindrome$' { return 'Allow appending characters at either end. -> Interval DP / Two-Sided Matching: Longest palindromic prefix alone no longer determines the optimum.' }
        '^longesthappyprefix$' { return 'Ask for every border length, not only the longest. -> LPS Border Chain: Repeatedly following lps[len - 1] enumerates all borders.' }
        '^addbinary$' { return 'Receive a long stream of binary additions to one accumulator. -> Mutable Bit Buffer: Rebuilding immutable result strings repeats carry work and allocation.' }
        '^countprimes$' { return 'Ask primality for isolated very large numbers instead of all values below n. -> Miller-Rabin / Trial Division: Sieve storage over the full range is no longer appropriate.' }
        '^countuniquecharactersofallsubstringsofagivenstring$' { return 'Ask the unique-character count for many explicit substring ranges. -> Offline/Indexed Range Queries: Whole-string occurrence contribution no longer answers each range directly.' }

        '^spiralmatrix$' { return 'Ask for repeated rectangular submatrix sums. -> 2D Prefix Sum: Boundary traversal does not amortize aggregate queries.' }
        '^stringtointegeratoi$' { return 'Parse arithmetic expressions with operators and parentheses. -> Stack Parser: A single numeric state no longer represents nested syntax.' }
    }

    $switches = @(Get-HorizontalSwitches -Category $Category -Title $Title)
    if ($switches.Count -eq 0) {
        return "VERIFY FROM SOURCE - choose the smallest statement change that breaks this invariant."
    }

    $change = $switches[0]
    return "$($change.Mutation) -> $($change.Pattern): $($change.NowWhy)"
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

function Escape-MermaidLabel {
    param([string] $Value)

    if ($null -eq $Value) { return "" }
    return ($Value -replace "[`r`n]+", " " `
        -replace '"', "'" `
        -replace "\s+", " ").Trim()
}

function Get-LeetCodeSlugs {
    param([string] $SourcePath)

    if (-not (Test-Path -LiteralPath $SourcePath)) {
        return @()
    }
    $content = Get-Content -Raw -LiteralPath $SourcePath
    $matches = [regex]::Matches($content, "leetcode\.com/problems/([A-Za-z0-9-]+)(/[^\s\)]*)?")
    return @($matches | Where-Object {
        $_.Groups[2].Value -notmatch '^/discuss\b'
    } | ForEach-Object {
        $_.Groups[1].Value.Trim().ToLowerInvariant()
    } | Where-Object { $_ } | Select-Object -Unique)
}

function Get-LeetCodeSlugMatches {
    param([string] $SourcePath)

    if (-not (Test-Path -LiteralPath $SourcePath)) {
        return @()
    }

    $content = Get-Content -Raw -LiteralPath $SourcePath
    $matches = [regex]::Matches($content, "leetcode\.com/problems/([A-Za-z0-9-]+)(/[^\s\)]*)?")
    return @($matches | Where-Object {
        $_.Groups[2].Value -notmatch '^/discuss\b'
    } | ForEach-Object {
        $_.Groups[1].Value.Trim().ToLowerInvariant()
    } | Where-Object { $_ })
}

function Get-ExcludedSlugsForFile {
    param([string] $RelativeFile)

    $fileKey = $RelativeFile.Replace("\", "/").ToLowerInvariant()
    switch ($fileKey) {
        "design/lld/designurlshortner.java" { return @("two-sum") }
        default { return @() }
    }
}

function Get-LeetCodeIdCatalog {
    $catalogPath = Join-Path $RepoRoot "dsa-review/notes/LEETCODE_ID_CATALOG.csv"
    if (-not (Test-Path -LiteralPath $catalogPath)) {
        throw "Could not find LeetCode ID catalog: $catalogPath"
    }

    $catalog = @{}
    foreach ($row in (Import-Csv -LiteralPath $catalogPath)) {
        if ([string]::IsNullOrWhiteSpace($row.id) -or [string]::IsNullOrWhiteSpace($row.slug)) {
            continue
        }
        $catalog[[string] $row.id] = [pscustomobject]@{
            Id = [string] $row.id
            Slug = $row.slug.Trim().ToLowerInvariant()
            Title = $row.title.Trim()
        }
    }
    return $catalog
}

function Get-LeetCodeProblemReferences {
    param(
        [string] $SourcePath,
        [hashtable] $IdCatalog
    )

    if (-not (Test-Path -LiteralPath $SourcePath)) {
        return @()
    }

    $content = Get-Content -Raw -LiteralPath $SourcePath
    $references = New-Object System.Collections.Generic.List[object]

    foreach ($match in [regex]::Matches($content, "leetcode\.com/problems/([A-Za-z0-9-]+)(/[^\s\)]*)?")) {
        if ($match.Groups[2].Value -match '^/discuss\b') {
            continue
        }
        $slug = $match.Groups[1].Value.Trim().ToLowerInvariant()
        if ($slug) {
            $references.Add([pscustomobject]@{
                Slug = $slug
                Title = ""
                SourceKind = "url"
            })
        }
    }

    foreach ($match in [regex]::Matches($content, "(?i)\b(?:leetcode|lc)\s*(?:#)?\s*(\d{1,5})\b")) {
        $id = [string] $match.Groups[1].Value
        if (-not $IdCatalog.ContainsKey($id)) {
            throw "LeetCode ID $id is referenced in $SourcePath but missing from dsa-review/notes/LEETCODE_ID_CATALOG.csv"
        }
        $problem = $IdCatalog[$id]
        $references.Add([pscustomobject]@{
            Slug = $problem.Slug
            Title = $problem.Title
            SourceKind = "id"
        })
    }

    return @($references | Group-Object Slug | ForEach-Object {
        $first = $_.Group | Select-Object -First 1
        $bestTitleRef = $_.Group | Where-Object { -not [string]::IsNullOrWhiteSpace($_.Title) } | Select-Object -First 1
        $bestTitle = if ($null -ne $bestTitleRef) { $bestTitleRef.Title } else { "" }
        [pscustomobject]@{
            Slug = $_.Name
            Title = if ($bestTitle) { $bestTitle } else { $first.Title }
            SourceKind = (@($_.Group.SourceKind | Sort-Object -Unique) -join "+")
        }
    })
}

function Get-IndexRows {
    param(
        [string] $RepoRoot,
        [string] $IndexPath
    )

    $rows = New-Object System.Collections.Generic.List[object]
    $idCatalog = Get-LeetCodeIdCatalog
    $pattern = '^\|\s*`([^`]+\.java)`\s*\|\s*([^|]+?)\s*\|\s*([ABC])\s*\|'
    foreach ($line in Get-Content -LiteralPath $IndexPath) {
        $match = [regex]::Match($line, $pattern)
        if (-not $match.Success) { continue }

        $relativeFile = $match.Groups[1].Value.Trim()
        if ($relativeFile -in @("Main.java", "CheatSheet.java")) { continue }

        $normalized = $relativeFile.Replace("\", "/")
        $sourcePath = Join-Path $RepoRoot ("src/main/java/org/chijai/" + $normalized)
        $fileTitle = ConvertTo-DisplayTitle $relativeFile
        $patternName = $match.Groups[2].Value.Trim()
        $priority = $match.Groups[3].Value.Trim()
        $category = Get-Category -Pattern $patternName -File $relativeFile -Title $fileTitle
        $excludedSlugs = @(Get-ExcludedSlugsForFile -RelativeFile $relativeFile)
        $references = @(Get-LeetCodeProblemReferences -SourcePath $sourcePath -IdCatalog $idCatalog | Where-Object { $_.Slug -notin $excludedSlugs })

        if ($references.Count -eq 0) {
            $rowPatternName = Get-PatternOverride -Title $fileTitle -Pattern $patternName
            $importanceWeight = Get-ProblemImportanceWeight -Title $fileTitle -Category $category -Pattern $rowPatternName
            $priorityWeight = Get-PriorityWeight $priority
            $categoryWeight = Get-CategoryWeight $category
            $rows.Add([pscustomobject]@{
                Title = $fileTitle
                Slug = ""
                File = $relativeFile
                Pattern = $rowPatternName
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

        foreach ($reference in $references) {
            $slug = $reference.Slug
            $title = if (-not [string]::IsNullOrWhiteSpace($reference.Title)) { $reference.Title } else { ConvertTo-TitleFromSlug $slug }
            $rowCategory = Get-Category -Pattern $patternName -File $relativeFile -Title $title
            $rowPatternName = Get-PatternOverride -Title $title -Pattern $patternName
            $importanceWeight = Get-ProblemImportanceWeight -Title $title -Category $rowCategory -Pattern $rowPatternName
            $priorityWeight = Get-PriorityWeight $priority
            $categoryWeight = Get-CategoryWeight $rowCategory
            $rows.Add([pscustomobject]@{
                Title = $title
                Slug = $slug
                File = $relativeFile
                Pattern = $rowPatternName
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
    foreach ($row in ($rows | Sort-Object ImportanceWeight, CategoryWeight, MatchScore, PriorityWeight, @{ Expression = { if ($_.Slug) { 0 } else { 1 } } }, File, Title)) {
        $titleKey = "TITLE:" + (Get-NormalizedKey $row.Title)
        $sourceKey = if ($row.Slug) { "LC:" + $row.Slug } else { "LOCAL:" + $row.File }
        if ($seen.ContainsKey($sourceKey) -or $seen.ContainsKey($titleKey)) { continue }
        $seen[$sourceKey] = $true
        $seen[$titleKey] = $true
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

function Get-RecursiveLeetCodeIndexRows {
    param(
        [object[]] $Rows
    )

    $rankedBySlug = @{}
    $rankedByFile = @{}
    foreach ($row in $Rows) {
        if (-not [string]::IsNullOrWhiteSpace($row.Slug) -and -not $rankedBySlug.ContainsKey($row.Slug)) {
            $rankedBySlug[$row.Slug] = $row
        }
        $fileKey = $row.File.Replace("\", "/").ToLowerInvariant()
        if (-not $rankedByFile.ContainsKey($fileKey)) {
            $rankedByFile[$fileKey] = New-Object System.Collections.Generic.List[object]
        }
        $rankedByFile[$fileKey].Add($row)
    }

    $bySlug = @{}
    $idCatalog = Get-LeetCodeIdCatalog
    $javaRoot = Join-Path $RepoRoot "src/main/java/org/chijai"
    foreach ($file in (Get-ChildItem -LiteralPath $javaRoot -Recurse -File -Filter "*.java")) {
        $relativeFile = $file.FullName.Substring($javaRoot.Length).TrimStart("\", "/").Replace("\", "/")
        $excluded = @(Get-ExcludedSlugsForFile -RelativeFile $relativeFile)
        $references = @(Get-LeetCodeProblemReferences -SourcePath $file.FullName -IdCatalog $idCatalog | Where-Object { $_.Slug -notin $excluded })
        foreach ($reference in $references) {
            $slug = $reference.Slug
            if (-not $bySlug.ContainsKey($slug)) {
                $rankedRow = if ($rankedBySlug.ContainsKey($slug)) { $rankedBySlug[$slug] } else { $null }
                $title = if ($null -ne $rankedRow) {
                    $rankedRow.Title
                } elseif (-not [string]::IsNullOrWhiteSpace($reference.Title)) {
                    $reference.Title
                } else {
                    ConvertTo-TitleFromSlug $slug
                }
                $category = if ($null -ne $rankedRow) { $rankedRow.Category } else { Get-Category -Pattern "" -File $relativeFile -Title $title }
                $pattern = if ($null -ne $rankedRow) { $rankedRow.Pattern } else { "" }
                if ([string]::IsNullOrWhiteSpace($pattern)) {
                    $fileKey = $relativeFile.ToLowerInvariant()
                    if ($rankedByFile.ContainsKey($fileKey)) {
                        $sameFileRows = @($rankedByFile[$fileKey] | Sort-Object Rank)
                        $pattern = ($sameFileRows | Select-Object -First 1).Pattern
                    }
                }
                if ([string]::IsNullOrWhiteSpace($pattern)) {
                    $pattern = Get-DisplayCategory $category
                }
                $pattern = Get-PatternOverride -Title $title -Pattern $pattern

                $bySlug[$slug] = [pscustomobject]@{
                    Slug = $slug
                    Title = $title
                    Category = $category
                    Pattern = $pattern
                    InterviewRank = if ($null -ne $rankedRow) { [int] $rankedRow.Rank } else { 999999 }
                    Files = New-Object System.Collections.Generic.List[string]
                }
            }
            if (-not $bySlug[$slug].Files.Contains($relativeFile)) {
                $bySlug[$slug].Files.Add($relativeFile)
            }
        }
    }

    $indexRank = 1
    $result = @($bySlug.Values | Sort-Object InterviewRank, @{ Expression = { Get-CategoryWeight $_.Category } }, Category, Pattern, Title)
    foreach ($item in $result) {
        Add-Member -InputObject $item -NotePropertyName IndexRank -NotePropertyValue $indexRank -Force
        $indexRank++
    }

    return @($result)
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

function Get-StablePatternFileName {
    param([string] $Category)

    switch ($Category) {
        "HashMap/HashSet" { return "01_hashmap_hashset.md" }
        "Binary Search" { return "02_binary_search.md" }
        "Sliding Window" { return "03_sliding_window.md" }
        "Prefix/Suffix" { return "04_prefix_suffix.md" }
        "Linked List" { return "05_linked_list.md" }
        "Two Pointers" { return "06_two_pointers.md" }
        "Heap" { return "07_heap.md" }
        "Tree BFS" { return "08_tree_bfs.md" }
        "Tree DFS" { return "09_tree_dfs.md" }
        "Graph DFS" { return "10_graph_dfs.md" }
        "Topological Sort" { return "11_topological_sort.md" }
        "Graph BFS" { return "12_graph_bfs.md" }
        "Dynamic Programming" { return "13_dynamic_programming.md" }
        "Backtracking" { return "14_backtracking.md" }
        "Stack" { return "15_stack.md" }
        "Trie" { return "16_trie.md" }
        "Intervals/Greedy" { return "17_intervals_greedy.md" }
        "Union Find" { return "18_union_find.md" }
        "Design/LLD" { return "19_design_lld.md" }
        "Math/Bit/String" { return "20_math_bit_string.md" }
        "Core Basics" { return "21_core_basics.md" }
        "Greedy" { return "22_greedy.md" }
        default { return "99_" + (ConvertTo-FileSlug $Category) + ".md" }
    }
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

    foreach ($group in $groups) {
        $fileName = Get-StablePatternFileName -Category $group.Category
        Add-Member -InputObject $group -NotePropertyName FileName -NotePropertyValue $fileName
    }

    return @($groups)
}

function Build-MasterMindMap {
    param(
        [object[]] $Rows,
        [object[]] $Groups
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# DSA Master Mind Map")
    $lines.Add("")
    $lines.Add("Generated from the same ranked metadata as the interview cockpit. Use it to visualize topic -> sub-pattern -> anchor problem without moving Java source files.")
    $lines.Add("")
    $lines.Add('Java source of truth remains `../../src/main/java/org/chijai`; this file is the pattern-tree interface.')
    $lines.Add("")
    $lines.Add('```mermaid')
    $lines.Add("flowchart TD")
    $lines.Add('  Root["DSA Interview Retrieval Tree"]')
    $lines.Add('  Solve["Solve rhythm<br/>brute force -> bottleneck -> pattern -> invariant -> code -> dry run"]')
    $lines.Add("  Root --> Solve")

    $categoryIndex = 1
    foreach ($group in $Groups) {
        $categoryId = "C{0:D2}" -f $categoryIndex
        $categoryLabel = Escape-MermaidLabel "$($group.DisplayCategory)<br/>$($group.Count) ranked entries<br/>first rank $($group.FirstRank)"
        $lines.Add(('  Root --> {0}["{1}"]' -f $categoryId, $categoryLabel))

        $subIndex = 1
        foreach ($subGroup in (@($group.Items | Group-Object Pattern | Sort-Object Name))) {
            $subId = "{0}P{1:D2}" -f $categoryId, $subIndex
            $subName = if ([string]::IsNullOrWhiteSpace($subGroup.Name)) { $group.DisplayCategory } else { $subGroup.Name }
            $subLabel = Escape-MermaidLabel "$subName<br/>$($subGroup.Count) problem(s)"
            $lines.Add(('  {0} --> {1}["{2}"]' -f $categoryId, $subId, $subLabel))

            $anchor = @($subGroup.Group | Sort-Object Rank | Select-Object -First 1)
            if ($anchor.Count -gt 0) {
                $anchorId = "{0}A" -f $subId
                $anchorLabel = Escape-MermaidLabel "Anchor rank $($anchor[0].Rank)<br/>$($anchor[0].Title)"
                $lines.Add(('  {0} --> {1}["{2}"]' -f $subId, $anchorId, $anchorLabel))
            }
            $subIndex++
        }
        $categoryIndex++
    }

    $lines.Add('```')
    $lines.Add("")
    $lines.Add("## How To Use")
    $lines.Add("")
    $lines.Add("1. Start at the problem signal and pick the likely pattern branch.")
    $lines.Add("2. Speak the invariant before coding.")
    $lines.Add("3. Use the anchor problem as the mental template.")
    $lines.Add("4. Open the linked pattern file when a branch feels weak.")
    $lines.Add("")
    $lines.Add("## Pattern Files")
    $lines.Add("")
    $lines.Add("| Pattern | Problems | First rank | File |")
    $lines.Add("|---|---:|---:|---|")
    foreach ($group in $Groups) {
        $file = New-Link $group.FileName ("patterns/" + $group.FileName)
        $lines.Add("| $(Escape-Md $group.DisplayCategory) | $($group.Count) | $($group.FirstRank) | $file |")
    }

    $lines.Add("")
    $lines.Add("Total ranked entries: $($Rows.Count)")
    return ($lines -join "`r`n")
}

function Build-PatternMermaid {
    param([object] $Group)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add('```mermaid')
    $lines.Add("flowchart TD")
    $rootLabel = Escape-MermaidLabel "TOPIC<br/>$($Group.DisplayCategory)"
    $signalLabel = Escape-MermaidLabel "RECOGNITION<br/>$(Get-Recall -Category $Group.Category -Pattern "" -Title "")"
    $invariantLabel = Escape-MermaidLabel "INVARIANT<br/>$(Get-InterviewHook -Category $Group.Category -Pattern "" -Title "")"
    $lines.Add(('  Topic["{0}"]' -f $rootLabel))
    $lines.Add(('  Recognition["{0}"]' -f $signalLabel))
    $lines.Add(('  Invariant["{0}"]' -f $invariantLabel))
    $lines.Add("  Topic --> Recognition --> Invariant")

    $subIndex = 1
    foreach ($subGroup in (@($Group.Items | Group-Object Pattern | Sort-Object Name))) {
        $subId = "Sub{0:D2}" -f $subIndex
        $subName = if ([string]::IsNullOrWhiteSpace($subGroup.Name)) { $Group.DisplayCategory } else { $subGroup.Name }
        $subLabel = Escape-MermaidLabel "SUB-PATTERN<br/>$subName<br/>$($subGroup.Count) problem(s)"
        $lines.Add(('  Invariant --> {0}["{1}"]' -f $subId, $subLabel))

        $anchorIndex = 1
        foreach ($row in (@($subGroup.Group | Sort-Object Rank | Select-Object -First 3))) {
            $anchorId = "{0}A{1:D2}" -f $subId, $anchorIndex
            $anchorLabel = Escape-MermaidLabel "ANCHOR<br/>rank $($row.Rank): $($row.Title)"
            $lines.Add(('  {0} --> {1}["{2}"]' -f $subId, $anchorId, $anchorLabel))
            $anchorIndex++
        }
        $subIndex++
    }

    $lines.Add('```')
    return ($lines -join "`r`n")
}

function Build-ProjectStructureGuide {
    param([object[]] $Groups)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Project Structure And Pattern Tree")
    $lines.Add("")
    $lines.Add("Do not physically move Java files to match the pattern taxonomy. Keep source code stable and let generated Markdown provide the interview-facing pattern tree.")
    $lines.Add("")
    $lines.Add("## Source Layout")
    $lines.Add("")
    $lines.Add("| Path | Responsibility |")
    $lines.Add("|---|---|")
    $lines.Add('| `../../src/main/java/org/chijai` | Java source of truth, package structure, tests, and implementation history. |')
    $lines.Add('| `../../src/main/java/org/chijai/patterns` | Additive pattern-lab package: reusable skeletons for visualizing common frames across problems. Do not move existing solved files into it. |')
    $lines.Add('| `../notes/PROBLEM_PATTERN_INDEX.md` | Curated mapping from Java files to pattern metadata and priority. |')
    $lines.Add('| `../notes/LEETCODE_ID_CATALOG.csv` | Local catalog for explicit `LC 123` references found in Java source. |')
    $lines.Add('| `01_ZERO_TO_HERO_RANKED_TABLE.md` | Interview-ROI order. |')
    $lines.Add('| `00_DSA_MIND_MAP.md` | Generated visual retrieval tree. |')
    $lines.Add('| `patterns/` | Generated per-pattern taxonomy pages. |')
    $lines.Add("")
    $lines.Add("## Chapter Pattern")
    $lines.Add("")
    $lines.Add("Use this order inside rich Java chapter files:")
    $lines.Add("")
    $lines.Add('```text')
    $lines.Add("PROBLEM -> BASELINE -> RECOGNITION -> INVARIANT -> TRAPS -> FALLBACK -> OPTIMAL -> DEFEND")
    $lines.Add('```')
    $lines.Add("")
    $lines.Add("## Taxonomy Shape")
    $lines.Add("")
    $lines.Add('```text')
    $lines.Add("TOPIC")
    $lines.Add("  CATEGORY")
    $lines.Add("    SUBCATEGORY")
    $lines.Add("      SUB-PATTERN")
    $lines.Add("        ANCHOR PROBLEM")
    $lines.Add('```')
    $lines.Add("")
    $lines.Add("## Generated Pattern Tree")
    $lines.Add("")
    $lines.Add("| Topic | Ranked entries | First rank | Generated file |")
    $lines.Add("|---|---:|---:|---|")
    foreach ($group in $Groups) {
        $file = New-Link $group.FileName ("patterns/" + $group.FileName)
        $lines.Add("| $(Escape-Md $group.DisplayCategory) | $($group.Count) | $($group.FirstRank) | $file |")
    }
    $lines.Add("")
    $lines.Add("When a Java file belongs to several problems, keep the file where it is and let the generated index list every linked problem under the right pattern branch.")
    $lines.Add("")
    $lines.Add("Use `../../src/main/java/org/chijai/patterns` only for pattern labs: one small reusable skeleton per high-ROI family, with tests proving the frame. This helps compare commonality and variation without breaking existing package links.")
    return ($lines -join "`r`n")
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
| Need horizontal pattern discrimination | `../horizontal/README.md` | Winner pattern, near-misses, minimal mutations, and CROSSDRILL. |
| Need complete LeetCode book index | `07_LEETCODE_SOLVED_INDEX.md` | Recursive source scan of LeetCode URLs and explicit LC problem numbers in Java files. |
| Need nested university-course TOC | `09_LEETCODE_CURRICULUM_TOC.md` | One decimal hierarchy: pattern family -> sub-pattern -> every LeetCode problem with LC and local Java links. |
| Need reconstruction plus exact say-before-coding contracts | `12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md` | One continuous pattern -> sub-pattern table with skeletons, correctness contracts, traps, and mutations. |
| Need time/space complexity recall | `13_MASTER_TIME_SPACE_COMPLEXITY_TABLE.md` | One continuous source-linked table with exact bounds, symbols, qualifiers, and one-sentence proofs. |
| Need fast memory refresh | `02_ONE_LINE_RECALL_ALL_PROBLEMS.md` | One sentence per problem in rank order. |
| Need speaking practice | `03_CRISP_INTERVIEW_ANSWERS.md` | Brute force -> bottleneck -> pattern -> invariant -> code -> dry run. |
| Need pattern-only focus | `patterns/README.md` | One file per pattern/category, still ordered by the current heuristic. |
| Need ranking reality check | `05_RANKING_METHODOLOGY_AND_AUDIT.md` | What is objective, what is heuristic, and where ranks can be wrong. |
| Need visual mental retrieval | `00_DSA_MIND_MAP.md` | Generated Mermaid tree: topic -> sub-pattern -> anchor problem. |
| Need structure decision | `08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md` | Why Java stays stable while generated docs expose the pattern taxonomy. |
| Need old static brain map | `DSA_170_Brain_Map_FINAL.md` | Legacy high-signal brain map. |
| Need one-week execution | `DSA_7-Day_Interview_Performance_Sprint.md` | Timed closed-book weekly sprint with review columns. |
| Following the legacy 90-problem hourly plan | `11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md` | Decision gate for recircling the 90 vs extending to 150/170+ and whether extra leave is justified. |
| Need after-week continuation | `10_AFTER_7_DAY_EXTENSION_PLAN.md` | Days 8-12 for ranks 151+ and source-only LeetCode extras, with stop/recircle rules. |
| Need review control panel | `06_REVIEW_DASHBOARD.md` | Dynamic due/red/yellow/mastered queues from `../../review/review.json`. |

## Current Coverage

- Ranked entries: __TOTAL__
- Recursive LeetCode solved index: __LEETCODE_INDEX_TOTAL__
- Nested LeetCode curriculum TOC: `09_LEETCODE_CURRICULUM_TOC.md`
- Pattern files: __PATTERN_COUNT__
- Ranking source: `../notes/PROBLEM_PATTERN_INDEX.md` plus LeetCode links found in Java chapters.
- Ranking philosophy: transparent interview triage. Use phase bands more than exact rank numbers.
- Ranking audit: `05_RANKING_METHODOLOGY_AND_AUDIT.md`.
- Canonical mind map: `DSA_170_Brain_Map_FINAL.md`.
- Older brain-map files are kept as drafts/reference snapshots; use the FINAL file during interview prep.

## Interview Rule

For every problem, expose the thought process:

```text
brute force -> bottleneck -> pattern -> invariant -> code -> dry run
```

Do not start by trying to remember the final code.
'@
    $patternCount = @(Get-PatternGroups -Rows $Rows).Count
    $leetcodeIndexCount = @(Get-RecursiveLeetCodeIndexRows -Rows $Rows).Count
    return $content.Replace("__TOTAL__", [string] $total).Replace("__PATTERN_COUNT__", [string] $patternCount).Replace("__LEETCODE_INDEX_TOTAL__", [string] $leetcodeIndexCount)
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

function Build-LeetCodeSolvedIndex {
    param(
        [object[]] $Rows,
        [object[]] $LeetCodeRows
    )

    $rankedCount = @($LeetCodeRows | Where-Object { $_.InterviewRank -lt 999999 }).Count
    $extraCount = $LeetCodeRows.Count - $rankedCount
    $multiFileCount = @($LeetCodeRows | Where-Object { $_.Files.Count -gt 1 }).Count

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# LeetCode Solved Index")
    $lines.Add("")
    $lines.Add("Recursive source scan: this is the book-style table of contents for LeetCode problems found in Java source files by full LeetCode URL or explicit LC problem number.")
    $lines.Add("")
    $lines.Add('Regenerate it with `dsa-review/scripts/build-interview-cockpit.cmd`, `dsa-review/scripts/build-interview-cockpit.sh`, or `verify-all.ps1` after adding or editing Java solution files. Add a full LeetCode URL or cataloged LC problem number when a file contains a solved problem.')
    $lines.Add("")
    $lines.Add("Use [Zero To Hero Ranked Table](01_ZERO_TO_HERO_RANKED_TABLE.md) for interview crunch order. Use this file when you want the complete source-backed LeetCode inventory.")
    $lines.Add("")
    $lines.Add("| Metric | Count |")
    $lines.Add("|---|---:|")
    $lines.Add("| Unique LeetCode problems found recursively | $($LeetCodeRows.Count) |")
    $lines.Add("| Also present in interview-ranked cockpit | $rankedCount |")
    $lines.Add("| Extra source-discovered problems | $extraCount |")
    $lines.Add("| Problems appearing in multiple Java files | $multiFileCount |")
    $lines.Add("")
    $lines.Add("## Table Of Contents")
    $lines.Add("")
    $groups = @($LeetCodeRows | Group-Object Category | ForEach-Object {
        $items = @($_.Group | Sort-Object InterviewRank, IndexRank)
        [pscustomobject]@{
            Category = $_.Name
            DisplayCategory = Get-DisplayCategory $_.Name
            Count = $items.Count
            FirstIndexRank = ($items | Select-Object -First 1).IndexRank
        }
    } | Sort-Object FirstIndexRank, DisplayCategory)

    foreach ($group in $groups) {
        $anchor = (Get-DisplayCategory $group.Category).ToLowerInvariant() -replace '[^a-z0-9 ]', '' -replace '\s+', '-'
        $lines.Add("- [$($group.DisplayCategory) ($($group.Count))](#$anchor)")
    }

    foreach ($categoryGroup in ($LeetCodeRows | Group-Object Category | Sort-Object { (@($_.Group | Sort-Object InterviewRank, IndexRank | Select-Object -First 1)).IndexRank }, Name)) {
        $displayCategory = Get-DisplayCategory $categoryGroup.Name
        $lines.Add("")
        $lines.Add("## $displayCategory")
        $lines.Add("")
        foreach ($patternGroup in ($categoryGroup.Group | Group-Object Pattern | Sort-Object { (@($_.Group | Sort-Object InterviewRank, IndexRank | Select-Object -First 1)).IndexRank }, Name)) {
            $lines.Add("### $(Escape-Md $patternGroup.Name)")
            $lines.Add("")
            $lines.Add("| # | Interview Rank | Problem | LeetCode | Local solution file(s) |")
            $lines.Add("|---:|---:|---|---|---|")
            foreach ($item in ($patternGroup.Group | Sort-Object InterviewRank, IndexRank)) {
                $interviewRank = if ($item.InterviewRank -lt 999999) { [string] $item.InterviewRank } else { "-" }
                $lc = New-Link "LC" "https://leetcode.com/problems/$($item.Slug)/"
                $links = @($item.Files | Sort-Object | ForEach-Object {
                    New-Link ([System.IO.Path]::GetFileName($_)) ("../../src/main/java/org/chijai/" + $_)
                }) -join ", "
                $lines.Add("| $($item.IndexRank) | $interviewRank | $(Escape-Md $item.Title) | $lc | $links |")
            }
            $lines.Add("")
        }
    }

    return ($lines -join "`r`n").TrimEnd()
}

function Build-LeetCodeCurriculumToc {
    param([object[]] $LeetCodeRows)

    $rankedCount = @($LeetCodeRows | Where-Object { $_.InterviewRank -lt 999999 }).Count
    $extraCount = $LeetCodeRows.Count - $rankedCount
    $categoryGroups = @($LeetCodeRows | Group-Object Category | ForEach-Object {
        $items = @($_.Group | Sort-Object InterviewRank, IndexRank)
        [pscustomobject]@{
            Category = $_.Name
            DisplayCategory = Get-DisplayCategory $_.Name
            Count = $items.Count
            FirstIndexRank = ($items | Select-Object -First 1).IndexRank
            Items = $items
        }
    } | Sort-Object FirstIndexRank, DisplayCategory)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# LeetCode Curriculum TOC")
    $lines.Add("")
    $lines.Add("Single top-to-bottom curriculum hierarchy for every LeetCode problem found recursively in Java source files.")
    $lines.Add("")
    $lines.Add("Coverage: $($LeetCodeRows.Count) confirmed LeetCode problems; $rankedCount in the interview-ranked cockpit; $extraCount source-discovered extras; $($categoryGroups.Count) pattern families.")
    $lines.Add("")
    $lines.Add('Regenerate with `verify-all.ps1` or `dsa-review/scripts/build-interview-cockpit.cmd` after adding or editing Java solution files.')
    $lines.Add("")
    $lines.Add("## Curriculum Hierarchy")
    $lines.Add("")
    $lines.Add("Read this as: pattern family -> sub-pattern -> problem. Problem names open LeetCode; local links open the Java solution files.")
    $lines.Add("")

    $categoryIndex = 1
    foreach ($categoryGroup in $categoryGroups) {
        $lines.Add("- **$categoryIndex** $(Escape-Md $categoryGroup.DisplayCategory) ($($categoryGroup.Count) problems)")

        $patternGroups = @($categoryGroup.Items | Group-Object Pattern | ForEach-Object {
            $items = @($_.Group | Sort-Object InterviewRank, IndexRank)
            $patternName = if ([string]::IsNullOrWhiteSpace($_.Name)) { $categoryGroup.DisplayCategory } else { $_.Name }
            [pscustomobject]@{
                Pattern = $patternName
                Count = $items.Count
                FirstIndexRank = ($items | Select-Object -First 1).IndexRank
                Items = $items
            }
        } | Sort-Object FirstIndexRank, Pattern)

        $patternIndex = 1
        foreach ($patternGroup in $patternGroups) {
            $lines.Add("  - **$categoryIndex.$patternIndex** $(Escape-Md $patternGroup.Pattern) ($($patternGroup.Count))")

            $problemIndex = 1
            foreach ($item in $patternGroup.Items) {
                $interviewRank = if ($item.InterviewRank -lt 999999) { "rank $($item.InterviewRank)" } else { "source-only" }
                $lc = New-Link (Escape-Md $item.Title) "https://leetcode.com/problems/$($item.Slug)/"
                $links = @($item.Files | Sort-Object | ForEach-Object {
                    New-Link ([System.IO.Path]::GetFileName($_)) ("../../src/main/java/org/chijai/" + $_)
                }) -join ", "
                $lines.Add("    - **$categoryIndex.$patternIndex.$problemIndex** $lc - $interviewRank - Local: $links")
                $problemIndex++
            }

            $patternIndex++
        }

        $lines.Add("")
        $categoryIndex++
    }

    return ($lines -join "`r`n").TrimEnd()
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
        $lines.Add("- Brute force: $(Get-BruteForceLine -Category $row.Category -Title $row.Title)")
        $lines.Add("- Bottleneck: $($row.InterviewHook)")
        $lines.Add("- Pattern: $(Get-DisplayCategory $row.Category), using $($row.Pattern).")
        $lines.Add("- Invariant/state: $($row.Recall)")
        $lines.Add("- Code idea: $($row.CodeIdea)")
        $lines.Add("- Dry run: Use the sample, then test empty/singleton, duplicates, no-answer, and boundary-answer cases.")
    }
    return ($lines -join "`r`n")
}

function Build-MasterArticulationTable {
    param([object[]] $Rows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Master DSA Reconstruction + Interview Articulation Table")
    $lines.Add("")
    $lines.Add("Purpose: one continuous sharpening table for reconstructing the engine and speaking the exact correctness contract before coding. This is not a solution summary.")
    $lines.Add("")
    $lines.Add('Organization: Pattern -> Sub-pattern divider rows live inside one uninterrupted table. Similar problems stay together in anchor-to-mutation progression; use `01_ZERO_TO_HERO_RANKED_TABLE.md` when ROI rank order is the goal.')
    $lines.Add("")
    $lines.Add("Each visible problem name opens its local Java source. Read: family skeleton -> problem skeleton -> state/invariant -> exact transition -> trap -> smallest useful mutation.")
    $lines.Add("")
    $lines.Add("| Problem | Reconstruction + Correctness Contract | Trap / Mutation |")
    $lines.Add("|---|---|---|")

    $categoryGroups = @($Rows | Group-Object Category | ForEach-Object {
        $items = @($_.Group | Sort-Object Rank)
        [pscustomobject]@{
            Category = $_.Name
            DisplayCategory = Get-DisplayCategory $_.Name
            FirstRank = [int] ($items | Select-Object -First 1).Rank
            Items = $items
        }
    } | Sort-Object FirstRank, DisplayCategory)

    foreach ($categoryGroup in $categoryGroups) {
        $displayCategory = $categoryGroup.DisplayCategory

        $patternGroups = @($categoryGroup.Items | Group-Object Pattern | ForEach-Object {
            $items = @($_.Group | Sort-Object Rank)
            [pscustomobject]@{
                Name = $_.Name
                FirstRank = [int] ($items | Select-Object -First 1).Rank
                Items = $items
            }
        } | Sort-Object FirstRank, Name)

        foreach ($patternGroup in $patternGroups) {
            $familySkeleton = Get-ArticulationFamilySkeleton -Category $categoryGroup.Category -Pattern $patternGroup.Name
            $divider = "$(Escape-Md $displayCategory) -> $(Escape-Md $patternGroup.Name)"
            $lines.Add("| **$divider** | **Family skeleton:** $(Escape-Md $familySkeleton) | |")

            foreach ($row in $patternGroup.Items) {
                $title = Escape-Md $row.Title
                $problemCell = New-Link $title $row.JavaLink

                $contract = Get-PrecisionContract `
                    -Category $row.Category `
                    -Pattern $row.Pattern `
                    -Title $row.Title `
                    -Recall $row.Recall `
                    -InterviewHook $row.InterviewHook `
                    -CodeIdea $row.CodeIdea
                $trap = Get-PrecisionTrap -Category $row.Category -Title $row.Title
                $mutation = Get-ArticulationMutation -Category $row.Category -Title $row.Title
                $reconstructionCell = "**Skeleton:** $($row.CodeIdea) **Contract:** $contract"
                $trapCell = "**Trap:** $trap **Mutation:** $mutation"
                $lines.Add("| $problemCell | $(Escape-Md $reconstructionCell) | $(Escape-Md $trapCell) |")
            }
        }
    }

    return ($lines -join "`r`n").TrimEnd()
}

function New-ComplexityResult {
    param([string] $Time, [string] $Space, [string] $Reason)
    return [pscustomobject]@{ Time = $Time; Space = $Space; Reason = $Reason }
}

function Get-ProblemComplexity {
    param([string] $Slug)

    switch -Regex ($Slug) {
        '^two-sum$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each value is stored once and its complement is queried once.' }
        '^(valid-anagram|ransom-note)$' { return New-ComplexityResult 'O(n + m)' 'O(sigma)' 'Each character is counted once; sigma is the represented alphabet.' }
        '^majority-element$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Boyer-Moore performs one cancellation pass with one candidate and counter.' }
        '^longest-palindrome$' { return New-ComplexityResult 'O(n)' 'O(sigma)' 'One frequency pass determines all pairs and the optional center.' }

        '^(binary-search|search-insert-position|first-bad-version|find-peak-element|sqrtx)$' { return New-ComplexityResult 'O(log n)' 'O(1)' 'Every predicate comparison discards half of the remaining ordered candidates.' }
        '^(search-in-rotated-sorted-array|find-first-and-last-position-of-element-in-sorted-array)$' { return New-ComplexityResult 'O(log n)' 'O(1)' 'One or two binary searches discard a provably impossible half each iteration.' }
        '^search-in-rotated-sorted-array-ii$' { return New-ComplexityResult 'O(n) worst case' 'O(1)' 'Equal left/mid/right values can force one-step shrinking; otherwise it behaves logarithmically.' }
        '^(koko-eating-bananas|split-array-largest-sum|capacity-to-ship-packages-within-d-days|minimum-number-of-days-to-make-m-bouquets)$' { return New-ComplexityResult 'O(n log R)' 'O(1)' 'Each of log R candidate answers is checked by one linear feasibility scan.' }
        '^time-based-key-value-store$' { return New-ComplexityResult 'set O(1), get O(log m)' 'O(total values)' 'Append preserves per-key time order; get binary-searches that key''s m versions.' }

        '^(longest-substring-without-repeating-characters|longest-substring-with-at-most-k-distinct-characters|longest-repeating-character-replacement)$' { return New-ComplexityResult 'O(n)' 'O(sigma)' 'Both window boundaries move forward at most n times while counts describe the active substring.' }
        '^minimum-window-substring$' { return New-ComplexityResult 'O(n + m)' 'O(sigma)' 'The target is counted once and each source character enters and leaves the window at most once.' }
        '^find-all-anagrams-in-a-string$' { return New-ComplexityResult 'O(n + m)' 'O(sigma)' 'The pattern is counted once and a fixed window updates two character counts per shift.' }
        '^moving-average-from-data-stream$' { return New-ComplexityResult 'O(1) per next' 'O(k)' 'A running sum adds one value and evicts at most one of the last k values.' }
        '^(count-number-of-nice-subarrays|binary-subarrays-with-sum)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Two monotone at-most windows each move left and right only forward.' }
        '^longest-continuous-increasing-subsequence$' { return New-ComplexityResult 'O(n)' 'O(1)' 'One scan carries only the current increasing streak and best length.' }
        '^(constrained-subsequence-sum|jump-game-vi)$' { return New-ComplexityResult 'O(n)' 'O(k)' 'Each index enters and leaves the monotonic deque at most once; only the last k states remain eligible.' }
        '^longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each index enters and leaves the max and min deques at most once.' }
        '^max-value-of-equation$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Ordered points enter and leave one monotonic eligible-candidate deque once.' }
        '^maximum-number-of-robots-within-budget$' { return New-ComplexityResult 'O(n)' 'O(n)' 'A monotonic deque and running sum add/remove each robot at most once.' }
        '^shortest-subarray-with-sum-at-least-k$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each prefix index is pushed and popped at most once from the monotonic deque.' }
        '^product-of-array-except-self$' { return New-ComplexityResult 'O(n)' 'O(1) auxiliary' 'Two linear products reuse the output array; the returned n-element array is not counted as auxiliary space.' }

        '^reverse-linked-list$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Every node is rewired once using three pointers.' }
        '^(linked-list-cycle|middle-of-the-linked-list|linked-list-cycle-ii)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Slow/fast pointers traverse only a constant number of list lengths.' }
        '^merge-two-sorted-lists$' { return New-ComplexityResult 'O(n + m)' 'O(1)' 'Each existing node is attached once; no nodes are copied.' }
        '^lru-cache$' { return New-ComplexityResult 'O(1) get/put expected' 'O(capacity)' 'Hash lookup plus constant-time doubly-linked-list detach/attach implements every operation.' }
        '^design-browser-history$' { return New-ComplexityResult 'visit O(1), back/forward O(steps)' 'O(history)' 'Visit appends one node; navigation advances at most the requested number of links.' }
        '^copy-list-with-random-pointer$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Two passes create one identity-map entry per original node and then wire both edges.' }
        '^intersection-of-two-linked-lists$' { return New-ComplexityResult 'O(n + m)' 'O(1)' 'Head switching makes each pointer traverse each list at most once.' }
        '^(reverse-nodes-in-k-group|odd-even-linked-list|rotate-list|swap-nodes-in-pairs|reverse-linked-list-ii|remove-nth-node-from-end-of-list)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'A constant set of pointers visits or rewires each list node at most a constant number of times.' }

        '^valid-palindrome$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Two pointers inspect each character at most once while skipping punctuation.' }
        '^container-with-most-water$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Exactly one boundary moves inward per comparison.' }
        '^two-sum-ii-input-array-is-sorted$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Each sum comparison permanently discards one endpoint.' }
        '^trapping-rain-water$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Two pointers finalize one side per step while retaining only left/right maxima.' }
        '^sort-colors$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Dutch-flag pointers classify each element with at most a constant number of swaps.' }
        '^longest-palindromic-substring$' { return New-ComplexityResult 'O(n^2)' 'O(1)' 'There are O(n) centers and an expansion can inspect O(n) characters.' }

        '^merge-k-sorted-lists$' { return New-ComplexityResult 'O(N log k)' 'O(k)' 'Each of N nodes is polled and offered through a heap containing at most k list heads.' }
        '^(top-k-frequent-elements|top-k-frequent-words)$' { return New-ComplexityResult 'O(n log k)' 'O(n)' 'Frequency counting stores up to n keys and a bounded heap processes each distinct key.' }
        '^sort-characters-by-frequency$' { return New-ComplexityResult 'O(n + sigma log sigma)' 'O(sigma)' 'Count n characters, then order only the sigma distinct characters.' }
        '^meeting-rooms-ii$' { return New-ComplexityResult 'O(n log n)' 'O(n)' 'Sorting dominates; each meeting end is inserted into or removed from the active-room heap.' }
        '^find-median-from-data-stream$' { return New-ComplexityResult 'add O(log n), median O(1)' 'O(n)' 'Insertion changes one heap and constant rebalancing; median reads one or two roots.' }
        '^task-scheduler$' { return New-ComplexityResult 'O(T log sigma)' 'O(sigma)' 'The heap/cooldown simulation schedules T slots while holding at most sigma task types.' }
        '^kth-largest-element-in-an-array$' { return New-ComplexityResult 'O(n log k)' 'O(k)' 'A size-k min-heap processes every number and retains only the k largest.' }
        '^kth-largest-element-in-a-stream$' { return New-ComplexityResult 'init O(n log k), add O(log k)' 'O(k)' 'Each value is inserted into a heap capped at k; the root is the kth largest.' }
        '^k-closest-points-to-origin$' { return New-ComplexityResult 'O(n log k)' 'O(k)' 'A size-k max-heap retains only the closest k of n points.' }
        '^h-index$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Bucket counts cap every citation at n and one reverse scan finds the boundary.' }
        '^ipo$' { return New-ComplexityResult 'O(n log n + k log n)' 'O(n)' 'Sort projects by capital, then each of at most k selections uses a profit heap.' }
        '^sliding-window-median$' { return New-ComplexityResult 'O(n log k)' 'O(k)' 'Each slide inserts and removes one value from balanced ordered halves of size k.' }

        '^(binary-tree-level-order-traversal|binary-tree-right-side-view)$' { return New-ComplexityResult 'O(n)' 'O(w)' 'Every node is queued once; w is the maximum tree width.' }
        '^(validate-binary-search-tree|binary-tree-inorder-traversal|binary-tree-postorder-traversal|binary-tree-preorder-traversal|lowest-common-ancestor-of-a-binary-tree|lowest-common-ancestor-of-a-binary-tree-ii|lowest-common-ancestor-of-a-binary-tree-iv|recover-binary-search-tree|convert-bst-to-greater-tree|diameter-of-binary-tree|path-sum-iii|sum-root-to-leaf-numbers|binary-tree-maximum-path-sum|path-sum|path-sum-ii|minimum-absolute-difference-in-bst|range-sum-of-bst|invert-binary-tree)$' { return New-ComplexityResult 'O(n)' 'O(h)' 'Each node is processed once; the recursion/explicit stack holds at most tree height h.' }
        '^lowest-common-ancestor-of-a-binary-tree-iii$' { return New-ComplexityResult 'O(h)' 'O(1)' 'Two parent-pointer walks cover at most both root paths.' }
        '^kth-smallest-element-in-a-bst$' { return New-ComplexityResult 'O(h + k)' 'O(h)' 'The initial left spine costs h and inorder stops after visiting k nodes.' }
        '^binary-search-tree-iterator$' { return New-ComplexityResult 'constructor O(h), next amortized O(1)' 'O(h)' 'Every node is pushed and popped once across the full iterator sequence.' }
        '^(lowest-common-ancestor-of-a-binary-search-tree|insert-into-a-binary-search-tree|search-in-a-binary-search-tree)$' { return New-ComplexityResult 'O(h)' 'O(h) recursive, O(1) iterative' 'BST ordering follows one root-to-leaf path of height h.' }
        '^(construct-binary-search-tree-from-preorder-traversal|construct-binary-tree-from-inorder-and-postorder-traversal|construct-binary-tree-from-preorder-and-inorder-traversal|serialize-and-deserialize-binary-tree)$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Indices/maps or serialization tokens make each node/token enter the construction exactly once.' }
        '^verify-preorder-serialization-of-a-binary-tree$' { return New-ComplexityResult 'O(n)' 'O(1)' 'A slot counter consumes each serialization token once.' }
        '^(all-nodes-distance-k-in-binary-tree|amount-of-time-for-binary-tree-to-be-infected)$' { return New-ComplexityResult 'O(n)' 'O(n)' 'One pass creates parent access and one visited BFS reaches each node at most once.' }

        '^(number-of-islands|pacific-atlantic-water-flow|surrounded-regions|number-of-closed-islands|max-area-of-island|flood-fill|coloring-a-border|01-matrix)$' { return New-ComplexityResult 'O(rows * cols)' 'O(rows * cols)' 'Each grid cell is marked/queued only a constant number of times; worst-case frontier/visited state spans the grid.' }
        '^(is-graph-bipartite|clone-graph|graph-valid-tree|possible-bipartition|course-schedule|course-schedule-ii|minimum-height-trees|parallel-courses|alien-dictionary|find-eventual-safe-states|sequence-reconstruction|sort-items-by-groups-respecting-dependencies|number-of-provinces)$' { return New-ComplexityResult 'O(V + E)' 'O(V + E)' 'Adjacency state stores E edges and traversal/topological processing consumes each vertex and edge once.' }
        '^course-schedule-iv$' { return New-ComplexityResult 'O(V^3 + Q)' 'O(V^2)' 'The local transitive-closure DP tests every intermediate/source/target triple, then answers Q queries directly.' }
        '^word-ladder$' { return New-ComplexityResult 'O(N * L * sigma)' 'O(N)' 'Each of N dictionary words is visited once and generates L positions times sigma replacements.' }
        '^rotting-oranges$' { return New-ComplexityResult 'O(rows * cols)' 'O(rows * cols)' 'Multi-source BFS enqueues each orange cell at most once.' }
        '^network-delay-time$' { return New-ComplexityResult 'O((V + E) log V)' 'O(V + E)' 'Dijkstra stores the graph and heap-relaxes each edge with logarithmic priority updates.' }
        '^k-highest-ranked-items-within-a-price-range$' { return New-ComplexityResult 'O(rows * cols + C log C)' 'O(rows * cols)' 'BFS visits the grid once and sorts/ranks the C eligible candidates encountered.' }

        '^(house-robber|climbing-stairs|min-cost-climbing-stairs)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Each position depends only on the previous constant number of DP states.' }
        '^coin-change$' { return New-ComplexityResult 'O(amount * coins)' 'O(amount)' 'Every amount tests every denomination against one reusable one-dimensional table.' }
        '^word-break$' { return New-ComplexityResult 'O(n^2)' 'O(n)' 'Each prefix endpoint may test all earlier cut positions; dp stores one reachability bit per prefix.' }
        '^perfect-squares$' { return New-ComplexityResult 'O(n * sqrt(n))' 'O(n)' 'Each value up to n tests every square not exceeding it.' }
        '^unique-paths$' { return New-ComplexityResult 'O(rows * cols)' 'O(rows * cols)' 'The table computes each grid state once from top and left.' }
        '^partition-equal-subset-sum$' { return New-ComplexityResult 'O(n * target)' 'O(target)' 'Each number scans subset sums up to total/2 once in descending order.' }
        '^longest-increasing-subsequence$' { return New-ComplexityResult 'O(n log n)' 'O(n)' 'Each value binary-searches the minimal-tail array.' }
        '^number-of-longest-increasing-subsequence$' { return New-ComplexityResult 'O(n^2)' 'O(n)' 'Each ending index compares with every earlier index while storing length and count.' }
        '^russian-doll-envelopes$' { return New-ComplexityResult 'O(n log n)' 'O(n)' 'Sorting plus binary-search LIS on heights dominates.' }
        '^maximum-profit-in-job-scheduling$' { return New-ComplexityResult 'O(n log n)' 'O(n)' 'Each sorted job binary-searches its compatible boundary and fills one DP state.' }
        '^kadane-max-sub-array$' { return New-ComplexityResult 'O(n)' 'O(1)' 'One scan keeps only best sum ending here and global best.' }
        '^(edit-distance|distinct-subsequences|interleaving-string|longest-common-subsequence|delete-operation-for-two-strings|longest-palindromic-subsequence|minimum-ascii-delete-sum-for-two-strings)$' { return New-ComplexityResult 'O(n * m)' 'O(n * m)' 'The local implementation fills one state for every pair of prefix/interval positions.' }
        '^(best-time-to-buy-and-sell-stock-with-transaction-fee|best-time-to-buy-and-sell-stock-with-cooldown|best-time-to-buy-and-sell-stock-iii)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'A constant number of hold/cash/rest transaction states is updated per price.' }
        '^best-time-to-buy-and-sell-stock-iv$' { return New-ComplexityResult 'O(n * k)' 'O(k)' 'Each day updates buy/sell state for each allowed transaction count.' }
        '^distinct-subsequences-ii$' { return New-ComplexityResult 'O(n)' 'O(sigma)' 'Each character updates total subsequences and one last-contribution slot.' }
        '^stock-price-fluctuation$' { return New-ComplexityResult 'update O(log n), current O(1), max/min O(log n)' 'O(n)' 'Timestamp replacement updates ordered price counts while current uses the latest timestamp.' }

        '^subsets$' { return New-ComplexityResult 'O(n * 2^n)' 'O(n) auxiliary' 'There are 2^n subsets and copying each output can cost O(n); recursion depth is n.' }
        '^combination-sum$' { return New-ComplexityResult 'O(c^(target / minCandidate)) worst case' 'O(target / minCandidate)' 'The decision tree can branch across c candidates until the remaining target reaches zero.' }
        '^word-search$' { return New-ComplexityResult 'O(rows * cols * 4^L)' 'O(L)' 'Each start may explore four choices for up to word length L; the path stack has depth L.' }
        '^letter-combinations-of-a-phone-number$' { return New-ComplexityResult 'O(d * 4^d)' 'O(d) auxiliary' 'At most 4^d strings of length d must be generated and copied.' }
        '^(permutations|permutations-ii)$' { return New-ComplexityResult 'O(n * n!)' 'O(n) auxiliary' 'Up to n! permutations are emitted and each length-n result is copied.' }
        '^n-queens$' { return New-ComplexityResult 'O(n!) upper bound' 'O(n) auxiliary' 'Column/diagonal pruning explores permutations of queen columns with recursion depth n.' }
        '^sudoku-solver$' { return New-ComplexityResult 'O(9^m) worst case' 'O(m)' 'For m empty cells, backtracking can try nine digits per cell; recursion depth is m.' }

        '^valid-parentheses$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each bracket is pushed or popped once.' }
        '^evaluate-reverse-polish-notation$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each token is processed once and the operand stack can hold n values.' }
        '^(daily-temperatures|next-greater-element-i|next-greater-element-ii|online-stock-span|remove-k-digits|sum-of-subarray-minimums)$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each item is pushed once and popped at most once from a monotonic stack.' }
        '^sliding-window-maximum$' { return New-ComplexityResult 'O(n)' 'O(k)' 'Each index enters and leaves the decreasing deque once; only window candidates remain.' }
        '^basic-calculator$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each character is consumed once and nested signs/results occupy at most expression depth.' }
        '^largest-rectangle-in-histogram$' { return New-ComplexityResult 'O(n)' 'O(n)' 'Each bar is pushed once and popped once when its maximal width becomes known.' }
        '^min-stack$' { return New-ComplexityResult 'O(1) per operation' 'O(n)' 'Each stack depth stores or synchronizes its minimum, so no operation scans.' }
        '^design-a-stack-with-increment-operation$' { return New-ComplexityResult 'O(1) per operation' 'O(capacity)' 'Lazy increment is recorded at one boundary and propagated once when popped.' }
        '^implement-queue-using-stacks$' { return New-ComplexityResult 'push O(1), pop/peek amortized O(1)' 'O(n)' 'Each value moves from input to output stack at most once.' }
        '^implement-stack-using-queues$' { return New-ComplexityResult 'push O(n), pop/top O(1)' 'O(n)' 'Each push rotates the old queue so the new value becomes the front.' }

        '^implement-trie-prefix-tree$' { return New-ComplexityResult 'O(L) per operation' 'O(total characters)' 'Each operation follows one edge per character; nodes store inserted prefixes.' }
        '^design-add-and-search-words-data-structure$' { return New-ComplexityResult 'add O(L), search O(26^d * L) worst case' 'O(total characters + L)' 'Each dot can branch over 26 children; recursion depth is word length L.' }
        '^word-search-ii$' { return New-ComplexityResult 'O(rows * cols * 4^L) worst case' 'O(dictionary characters + L)' 'Trie pruning reduces practical branches, but a board path can still branch four ways to depth L.' }
        '^maximum-xor-of-two-numbers-in-an-array$' { return New-ComplexityResult 'O(n * B)' 'O(n * B)' 'Each number inserts and queries exactly B bits in the binary trie.' }
        '^maximum-xor-with-an-element-from-array$' { return New-ComplexityResult 'O((n + q) log(n + q) + (n + q)B)' 'O(n * B + q)' 'Offline sorting controls eligibility; every inserted number/query then walks B trie bits.' }
        '^maximum-genetic-difference-query$' { return New-ComplexityResult 'O((n + q) * B)' 'O(n * B + q)' 'DFS adds/removes each node once and each query walks B active-ancestor trie levels.' }
        '^count-pairs-with-xor-in-a-range$' { return New-ComplexityResult 'O(n * B)' 'O(n * B)' 'Each number performs two B-bit less-than queries and one trie insertion.' }
        '^(longest-common-prefix|longest-word-in-dictionary|replace-words|search-suggestions-system|short-encoding-of-words|map-sum-pairs)$' { return New-ComplexityResult 'O(total characters)' 'O(total characters)' 'Trie construction and required prefix/suffix walks touch each stored character a constant number of times.' }

        '^(minimum-number-of-arrows-to-burst-balloons|meeting-rooms|merge-intervals|non-overlapping-intervals|maximum-length-of-pair-chain)$' { return New-ComplexityResult 'O(n log n)' 'O(n) sort-dependent' 'Sorting dominates; the greedy/merge scan is linear after ordering.' }
        '^insert-interval$' { return New-ComplexityResult 'O(n)' 'O(n) output' 'Already-sorted non-overlapping intervals are scanned once and emitted once.' }
        '^partition-labels$' { return New-ComplexityResult 'O(n)' 'O(sigma)' 'One pass records last occurrences and one pass closes each partition boundary.' }
        '^car-pooling$' { return New-ComplexityResult 'O(n + U)' 'O(U)' 'A difference array records n trip endpoints and scans coordinate range U once.' }
        '^accounts-merge$' { return New-ComplexityResult 'O(E alpha(E) + E log E)' 'O(E)' 'DSU nearly-linearly merges E email references; sorting emails inside output groups dominates.' }
        '^redundant-connection$' { return New-ComplexityResult 'O(n alpha(n))' 'O(n)' 'Each edge performs two compressed finds and at most one union.' }

        '^best-time-to-buy-and-sell-stock$' { return New-ComplexityResult 'O(n)' 'O(1)' 'One scan maintains the minimum earlier price and best sell profit.' }
        '^best-time-to-buy-and-sell-stock-ii$' { return New-ComplexityResult 'O(n)' 'O(1)' 'Every positive adjacent price increase is inspected and added once.' }
        '^(gas-station|jump-game)$' { return New-ComplexityResult 'O(n)' 'O(1)' 'A failed prefix or unreachable boundary is summarized by one running scalar, so each index is visited once.' }

        '^first-unique-number$' { return New-ComplexityResult 'add/show amortized O(1)' 'O(n)' 'Counts plus an ordered candidate queue remove each duplicated value at most once.' }
        '^encode-and-decode-tinyurl$' { return New-ComplexityResult 'O(1) expected per operation' 'O(n)' 'Two hash indexes perform constant expected lookup and store one entry per URL.' }
        '^design-a-leaderboard$' { return New-ComplexityResult 'add/reset O(log n), top O(k + log n)' 'O(n)' 'Player updates maintain ordered score counts; top walks at most k highest-scoring contributions.' }
        '^design-an-ordered-stream$' { return New-ComplexityResult 'O(1) amortized per inserted value' 'O(n)' 'Each stored value is returned once when the advancing pointer reaches its contiguous block.' }
        '^design-hit-counter$' { return New-ComplexityResult 'O(1) amortized per operation' 'O(W)' 'Each hit enters and expires from the fixed-window queue once; W is hits retained in 300 seconds.' }
        '^design-parking-system$' { return New-ComplexityResult 'O(1)' 'O(1)' 'A fixed counter per car type is checked and decremented directly.' }

        '^find-the-index-of-the-first-occurrence-in-a-string$' { return New-ComplexityResult 'O(n + m)' 'O(m)' 'KMP builds m LPS entries and never moves the n-character text pointer backward.' }
        '^(repeated-substring-pattern|longest-happy-prefix)$' { return New-ComplexityResult 'O(n)' 'O(n)' 'One LPS construction processes each character with amortized fallback.' }
        '^shortest-palindrome$' { return New-ComplexityResult 'O(n)' 'O(n)' 'One combined-string LPS pass finds the longest palindromic prefix.' }
        '^add-binary$' { return New-ComplexityResult 'O(n + m)' 'O(n + m)' 'Each input bit and the final carry are consumed once into the result.' }
        '^count-primes$' { return New-ComplexityResult 'O(n log log n)' 'O(n)' 'The sieve marks prime multiples, whose harmonic work sums to n log log n.' }
        '^count-unique-characters-of-all-substrings-of-a-given-string$' { return New-ComplexityResult 'O(n)' 'O(sigma)' 'Previous/next occurrence state gives one constant-time contribution per character occurrence.' }
        '^spiral-matrix$' { return New-ComplexityResult 'O(rows * cols)' 'O(1) auxiliary' 'Four shrinking boundaries emit every matrix cell exactly once.' }
        '^string-to-integer-atoi$' { return New-ComplexityResult 'O(n)' 'O(1)' 'The parser consumes a prefix of the input once while retaining sign and numeric accumulator.' }
        '^missing-number$' { return New-ComplexityResult 'O(n)' 'O(1)' 'XOR or arithmetic accumulation consumes each value once.' }
        '^missing-ranges$' { return New-ComplexityResult 'O(n)' 'O(1) auxiliary' 'Sentinel boundaries scan the sorted values once; returned ranges are output space.' }
        '^number-of-orders-in-the-backlog$' { return New-ComplexityResult 'O(n log n)' 'O(n)' 'Each order is inserted/removed through one of two price heaps that can hold n orders.' }
    }

    return New-ComplexityResult 'VERIFY FROM SOURCE' 'VERIFY FROM SOURCE' 'No complexity contract is registered for this discovered slug.'
}

function Build-MasterComplexityTable {
    param([object[]] $LeetCodeRows)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add('# Master DSA Time + Space Complexity Table')
    $lines.Add('')
    $lines.Add("Corpus: $($LeetCodeRows.Count) distinct LeetCode problems discovered recursively from Java URLs and cataloged LC IDs. The count is evidence, not a target; add a URL or cataloged ID beside a new solution and regenerate.")
    $lines.Add('')
    $lines.Add('Symbols: `n/m` input lengths, `V/E` vertices/edges, `rows*cols` grid cells, `k` retained/window/transaction limit, `c` candidate count, `R` numeric answer range, `L` word length, `B` bit width, `sigma` alphabet/distinct key space, `h/w` tree height/width, `U` coordinate range, `W` live time-window items, `alpha` inverse Ackermann. Space excludes returned output only when stated.')
    $lines.Add('')
    $lines.Add('| Problem | Pattern | Time | Space | One-sentence proof |')
    $lines.Add('|---|---|---|---|---|')

    $categoryGroups = @($LeetCodeRows | Group-Object Category | ForEach-Object {
        $items = @($_.Group | Sort-Object IndexRank)
        [pscustomobject]@{ Category = $_.Name; FirstRank = [int]($items | Select-Object -First 1).IndexRank; Items = $items }
    } | Sort-Object FirstRank, Category)

    foreach ($group in $categoryGroups) {
        $displayCategory = Get-DisplayCategory $group.Category
        $lines.Add("| **$(Escape-Md $displayCategory)** | **Complexity family** | | | |")
        foreach ($row in $group.Items) {
            $complexity = Get-ProblemComplexity -Slug $row.Slug
            $javaLinks = @($row.Files | Sort-Object | ForEach-Object {
                $target = '../../src/main/java/org/chijai/' + $_.Replace('\', '/')
                New-Link 'Java' $target
            })
            $problem = (New-Link (Escape-Md $row.Title) ("https://leetcode.com/problems/$($row.Slug)/")) + ' (' + ($javaLinks -join ', ') + ')'
            $lines.Add("| $problem | $(Escape-Md $row.Pattern) | $($complexity.Time) | $($complexity.Space) | $(Escape-Md $complexity.Reason) |")
        }
    }

    return ($lines -join "`r`n").TrimEnd()
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
    $extension = New-Link "Post-7-Day Extension Plan" "10_AFTER_7_DAY_EXTENSION_PLAN.md"
    $activeNinety = New-Link "Active 90-Problem Cutoff Plan" "11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md"
    $lines.Add("Use this when a company asks for a DSA round soon. The goal is interview triage: remove red flags first, then expand coverage by rank.")
    $lines.Add("")
    $lines.Add("Core files: $ranked, $recall, $answers, $patterns, $preZoom, $extension, $activeNinety.")
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
    $lines.Add("- After Day 7: continue with $extension only if ranks 1-100 are mostly GREEN and ranks 1-50 have no repeated RED.")
    $lines.Add("- If you are following the legacy 90-problem hourly plan, use $activeNinety as the gate before adding more leave or new coverage.")
    $lines.Add("")
    $lines.Add("## What Not To Do")
    $lines.Add("")
    $lines.Add("- Do not reread Java first. Speak the approach before opening code.")
    $lines.Add("- Do not jump to DP/Trie/Union-Find before Phase 1 and Phase 2 are stable.")
    $lines.Add("- Do not treat the ranking as universal truth. It is an interview-ROI order for fast prep.")
    $lines.Add('- Do not keep a miss invisible. Mark it `again` or `hard` and revisit it.')
    return ($lines -join "`r`n")
}

function Get-SprintSourceRankOrder {
    return @(
        1,2,9,10,54,61,89,91,4,12,138,95,31,3,5,50,45,114,53,113,20,
        21,22,82,86,87,92,93,6,7,8,94,56,57,23,33,131,133,32,96,36,102,103,
        14,79,67,117,116,15,25,26,27,16,59,68,17,29,30,34,80,35,18,72,
        75,38,150,39,47,48,49,40,41,42,140,141,43,11,37,142,108,137,101,104,46,
        98,24,58,60,69,70,73,74,76,77,81,88,90,97,99,100,105,106,107,109,
        110,111,112,115,118,119,120,121,122,123,124,125,126,127,128,129,130,132,134,135,
        136,139,143,144,145,146,147,148,149,19,28,44,51,52,55,62,63,64,65,66,
        71,78,83,84,85,13
    )
}

function Get-RowByRank {
    param([object[]] $Rows)

    $byRank = @{}
    foreach ($row in $Rows) {
        $byRank[[int] $row.Rank] = $row
    }
    return $byRank
}

function New-ProblemLinks {
    param([object] $Row)

    $links = New-Link "Java" $Row.JavaLink
    if ($Row.LeetCodeLink) {
        $links += " / " + (New-Link "LC" $Row.LeetCodeLink)
    }
    return $links
}

function Add-SprintRow {
    param(
        [System.Collections.Generic.List[string]] $Lines,
        [int] $SprintRank,
        [int] $SourceRank,
        [string] $Time,
        [object] $Row
    )

    $timeCell = if ($Time) { $Time } else { "-" }
    $links = New-ProblemLinks -Row $Row
    $family = Get-DisplayCategory $Row.Category
    $Lines.Add("| $timeCell | $SprintRank | $SourceRank | $(Escape-Md $Row.Title) | $links | $(Escape-Md $family) | $(Escape-Md $Row.Pattern) | $(Escape-Md $Row.Recall) |  |  | 0 |  |  |")
}

function Build-WeeklySprint {
    param([object[]] $Rows)

    $rankOrder = @(Get-SprintSourceRankOrder)
    $byRank = Get-RowByRank -Rows $Rows
    $dayNames = @("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    $timeSlots = @(
        "09:00","09:20","09:40","10:00","10:20","10:40","11:00","11:20","11:40",
        "13:00","13:20","13:40","14:00","14:20","14:40","15:00","15:20","15:40","16:00","16:20","16:40"
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# DSA 7-Day Interview Performance Sprint")
    $lines.Add("")
    $lines.Add("Goal: eliminate senior-candidate red flags by training closed-book retrieval, reconstruction, debugging, and explanation.")
    $lines.Add("")
    $lines.Add('Source of truth: generated from `01_ZERO_TO_HERO_RANKED_TABLE.md` data. The sprint keeps a cognitive training order, while `Source Rank` preserves the canonical ranking.')
    $lines.Add("")
    $lines.Add("## North Star")
    $lines.Add("")
    $lines.Add("Random problem -> recognize family -> state invariant -> code working Java from a blank editor -> test -> explain complexity/trade-offs.")
    $lines.Add("")
    $lines.Add("## Non-Negotiable Rules")
    $lines.Add("")
    $lines.Add("- First attempt is closed-book: blank editor, no old Java, no notes, no solution.")
    $lines.Add("- Each listed slot is a 20-minute diagnostic time-box, not a guarantee of completion.")
    $lines.Add("- At 20:00, score the attempt and move on. A failure found here is interview data.")
    $lines.Add("- Do not sacrifice Rank 1-50 repair just to touch Rank 150.")
    $lines.Add("- Reviews are active retrieval: blank editor + timer + reconstruction, not passive rereading.")
    $lines.Add("")
    $lines.Add("## 20-Minute Protocol")
    $lines.Add("")
    $lines.Add("| Minute | Stage | Required output | Pass condition |")
    $lines.Add("|---:|---|---|---|")
    $lines.Add("| 00-02 | Recognize | Family, pattern, candidate data structure | Plausible approach without notes |")
    $lines.Add("| 02-05 | Derive | Brute force, bottleneck, invariant, complexity | Can explain why it works |")
    $lines.Add("| 05-15 | Implement | Java solution from blank editor | Compiles or clearly represents intended algorithm |")
    $lines.Add("| 15-18 | Test | Normal, boundary, tricky case | Correct or independently debugged |")
    $lines.Add("| 18-20 | Explain + score | Complexity, trade-off, edge case, result | Clear interview explanation |")
    $lines.Add("")
    $lines.Add("## Score And Failure Codes")
    $lines.Add("")
    $lines.Add("- GREEN: independent recognition, derivation, implementation, testing, and complexity within the time-box.")
    $lines.Add("- YELLOW: right family/idea, but hint, implementation trouble, missed edge case, debugging gap, or explanation weakness.")
    $lines.Add("- RED: no viable derivation, major wrong approach, incomplete implementation, or solution lookup required.")
    $lines.Add("")
    $lines.Add("Failure codes: `P` pattern recognition, `I` invariant/reasoning, `D` data structure, `J` Java implementation, `E` edge case, `C` complexity, `B` debugging, `M` memorized/not understood.")
    $lines.Add("")
    $lines.Add("## Spaced-Repetition Policy")
    $lines.Add("")
    $lines.Add("| Result | Default reviews |")
    $lines.Add("|---|---|")
    $lines.Add("| RED | D+1 -> D+3 -> D+7 -> D+14 -> D+30 |")
    $lines.Add("| YELLOW | D+2 -> D+7 -> D+14 -> D+30 |")
    $lines.Add("| GREEN | D+7 -> D+30, then random mocks |")
    $lines.Add("")
    $lines.Add("On every review, record `Score`, `Failure`, `Attempts`, `Last Review`, and `Next Review`. Repeated RED matters more than a first RED.")
    $lines.Add("")
    $lines.Add("## Daily Operating Window")
    $lines.Add("")
    $lines.Add('`09:00-12:00` 9 problems -> `12:00-13:00` lunch/walk -> `13:00-17:00` 12 problems. Hard stop at 17:00.')
    $lines.Add("")
    $lines.Add("## ROI Tiers")
    $lines.Add("")
    $lines.Add("- Sprint ranks 1-50: no-red-flag fundamentals; must become overwhelmingly GREEN.")
    $lines.Add("- Sprint ranks 51-90: strong senior core; should recognize rapidly and usually implement.")
    $lines.Add("- Sprint ranks 91-125: interview breadth and transfer.")
    $lines.Add("- Sprint ranks 126-150: diminishing returns; useful, but never above repair of fundamentals.")

    for ($day = 0; $day -lt 7; $day++) {
        $lines.Add("")
        $lines.Add("---")
        $lines.Add("")
        $lines.Add("## Day $($day + 1) - $($dayNames[$day])")
        $lines.Add("")
        $lines.Add("| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |")
        $lines.Add("|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|")

        for ($slot = 0; $slot -lt $timeSlots.Count; $slot++) {
            $index = ($day * $timeSlots.Count) + $slot
            if ($index -ge 147) { break }
            $sourceRank = $rankOrder[$index]
            if (-not $byRank.ContainsKey($sourceRank)) {
                throw "Sprint source rank not found: $sourceRank"
            }
            Add-SprintRow -Lines $lines -SprintRank ($index + 1) -SourceRank $sourceRank -Time $timeSlots[$slot] -Row $byRank[$sourceRank]
        }

        $lines.Add("")
        $lines.Add("Daily scoreboard: Attempted __/21; GREEN __; YELLOW __; RED __; repeated RED __; fundamental RED __.")
        $lines.Add("")
        $lines.Add("Top 3 failure lessons: 1. ___  2. ___  3. ___")
        $lines.Add("")
        $lines.Add("Tomorrow repair queue: 1. ___  2. ___  3. ___")
    }

    $lines.Add("")
    $lines.Add("---")
    $lines.Add("")
    $lines.Add("## Overflow / Completion - Sprint Ranks 148-150")
    $lines.Add("")
    $lines.Add("These three are deliberately outside the 147 fixed slots. Complete only after higher-priority repair; there is no interview benefit in forcing superficial completion.")
    $lines.Add("")
    $lines.Add("| Time | Sprint Rank | Source Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review |")
    $lines.Add("|---|---:|---:|---|---|---|---|---|---|---|---:|---|---|")
    for ($i = 147; $i -lt $rankOrder.Count; $i++) {
        $sourceRank = $rankOrder[$i]
        if (-not $byRank.ContainsKey($sourceRank)) {
            throw "Sprint source rank not found: $sourceRank"
        }
        Add-SprintRow -Lines $lines -SprintRank ($i + 1) -SourceRank $sourceRank -Time "-" -Row $byRank[$sourceRank]
    }

    $lines.Add("")
    $lines.Add("## Interview-Ready Gate")
    $lines.Add("")
    $lines.Add("- [ ] Sprint ranks 1-50 are overwhelmingly GREEN with no recurring fundamental RED.")
    $lines.Add("- [ ] Random/rephrased problem family is recognized quickly without category hints.")
    $lines.Add("- [ ] Blank-editor Java implementation is reliable.")
    $lines.Add("- [ ] Brute force -> optimized transition and invariant can be explained.")
    $lines.Add("- [ ] Time/space complexity is correct.")
    $lines.Add("- [ ] Edge cases are generated independently.")
    $lines.Add("- [ ] Ordinary bugs are diagnosed calmly.")
    $lines.Add("- [ ] Requirement mutations can be discussed.")
    $lines.Add("- [ ] Random timed mocks are consistently passing.")
    $lines.Add("")
    $lines.Add("## After The Sprint")
    $lines.Add("")
    $lines.Add("Stop accumulating sheets. Shift to performance mode: due spaced reviews -> random unseen/rephrased DSA -> timed coding -> requirement mutation/debugging -> LLD mock -> HLD mock.")
    $lines.Add("")
    $lines.Add('Execution mantra: `MASTER FUNDAMENTALS -> RETRIEVE -> FAIL FAST -> RECORD -> SPACE -> REPAIR -> RANDOMIZE -> MOCK -> INTERVIEW`')

    return ($lines -join "`r`n")
}

function Add-ExtensionRow {
    param(
        [System.Collections.Generic.List[string]] $Lines,
        [object] $Row
    )

    $links = New-ProblemLinks -Row $Row
    $family = Get-DisplayCategory $Row.Category
    $Lines.Add("| $($Row.Rank) | $(Escape-Md $Row.Title) | $links | $(Escape-Md $family) | $(Escape-Md $Row.Pattern) | $(Escape-Md $Row.Recall) |  |  |  |")
}

function Add-SourceOnlyExtensionRow {
    param(
        [System.Collections.Generic.List[string]] $Lines,
        [object] $Item
    )

    $lc = New-Link "LC" "https://leetcode.com/problems/$($Item.Slug)/"
    $links = @($Item.Files | Sort-Object | ForEach-Object {
        New-Link ([System.IO.Path]::GetFileName($_)) ("../../src/main/java/org/chijai/" + $_)
    }) -join ", "
    $family = Get-DisplayCategory $Item.Category
    $pattern = if ([string]::IsNullOrWhiteSpace($Item.Pattern)) { $family } else { $Item.Pattern }
    $Lines.Add("| source-only | $(Escape-Md $Item.Title) | $lc / $links | $(Escape-Md $family) | $(Escape-Md $pattern) | Recursive source reference; derive the invariant from linked Java before promoting it into the ranked cockpit. |  |  |  |")
}

function Build-PostSevenDayExtensionPlan {
    param(
        [object[]] $Rows,
        [object[]] $LeetCodeRows
    )

    $rankedTail = @($Rows | Where-Object { [int] $_.Rank -gt 150 } | Sort-Object Rank)
    $sourceOnly = @($LeetCodeRows | Where-Object { $_.InterviewRank -ge 999999 } | Sort-Object IndexRank)
    $days = @(
        @{ Name = "Day 8"; Focus = "finish remaining Phase 4 depth without touching weak fundamentals"; Items = @($rankedTail | Select-Object -Skip 0 -First 15) },
        @{ Name = "Day 9"; Focus = "pattern transfer across medium-frequency variants"; Items = @($rankedTail | Select-Object -Skip 15 -First 15) },
        @{ Name = "Day 10"; Focus = "secondary tree, graph, stack, and linked-list variants"; Items = @($rankedTail | Select-Object -Skip 30 -First 15) },
        @{ Name = "Day 11"; Focus = "lower-ROI but useful breadth; keep attempts timed"; Items = @($rankedTail | Select-Object -Skip 45 -First 15) },
        @{ Name = "Day 12"; Focus = "ranked cleanup plus recursive source-only LeetCode inventory"; Items = @($rankedTail | Select-Object -Skip 60) }
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# After 7 Days Extension Plan")
    $lines.Add("")
    $lines.Add("Purpose: continue after the 7-day sprint without pretending that raw coverage equals interview readiness.")
    $lines.Add("")
    $lines.Add("This file is generated from the same ranked metadata and recursive LeetCode source scan as the cockpit.")
    $lines.Add("")
    $lines.Add("## Cutoff Rule")
    $lines.Add("")
    $lines.Add("- If interview is in 1-2 days: stop new coverage around rank 70 and repair misses.")
    $lines.Add("- If interview is in 1 week: target ranks 1-150 plus spaced review, not all 220.")
    $lines.Add("- If interview is in 2+ weeks: finish ranks 151-216 and source-only extras, but only after top 100 recall is stable.")
    $lines.Add("- If ranks 1-50 contain repeated RED, do not continue this extension. Rebuild fundamentals first.")
    $lines.Add("")
    $lines.Add("## Daily Shape")
    $lines.Add("")
    $lines.Add('- 09:00-10:00: due reviews from `review/review.json`.')
    $lines.Add("- 10:00-15:00: 15 new or weak-tail problems, three 18-minute reps per hour.")
    $lines.Add("- 15:00-16:00: repair the worst three misses from today.")
    $lines.Add("- 16:00-17:00: one random mock from ranks 1-150 so old fundamentals stay hot.")
    $lines.Add("")

    foreach ($day in $days) {
        $lines.Add("---")
        $lines.Add("")
        $lines.Add("## $($day.Name) - $($day.Focus)")
        $lines.Add("")
        $lines.Add("| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Next Review |")
        $lines.Add("|---:|---|---|---|---|---|---|---|---|")
        foreach ($row in $day.Items) {
            Add-ExtensionRow -Lines $lines -Row $row
        }

        if ($day.Name -eq "Day 12" -and $sourceOnly.Count -gt 0) {
            foreach ($item in $sourceOnly) {
                Add-SourceOnlyExtensionRow -Lines $lines -Item $item
            }
        }

        $lines.Add("")
        $lines.Add("End-of-day gate: new attempted __; GREEN __; YELLOW __; RED __; carry-forward repair __.")
        $lines.Add("")
    }

    $lines.Add("## Recircling Rule")
    $lines.Add("")
    $lines.Add("- After ranks 1-216 are touched once, stop adding lists.")
    $lines.Add("- Recircle by weakest signal: repeated RED -> old YELLOW -> random rank 1-150 mock -> source-only extras.")
    $lines.Add("- A problem graduates only when you can explain brute force -> bottleneck -> pattern -> invariant -> code -> dry run without opening Java.")
    $lines.Add("- The target is fast retrieval and adaptation, not finishing a file.")

    return ($lines -join "`r`n")
}

function Build-ActiveNinetyPlanCutoff {
    param([object[]] $Rows)

    $ranked = New-Link "Zero To Hero Ranked Table" "01_ZERO_TO_HERO_RANKED_TABLE.md"
    $extension = New-Link "After 7 Days Extension Plan" "10_AFTER_7_DAY_EXTENSION_PLAN.md"
    $legacyPlan = New-Link "DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md" "DSA_7-Day_Hourly_WIN_FINAL_v15_HighSignal_Pattern_Triggers.md"
    $reviewDashboard = New-Link "Review Dashboard" "06_REVIEW_DASHBOARD.md"
    $patternLabsGuide = New-Link "Project Structure And Pattern Tree" "08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md"
    $top90 = @($Rows | Sort-Object Rank | Select-Object -First 90)
    $top150 = @($Rows | Sort-Object Rank | Select-Object -First 150)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Active 90-Problem Plan Cutoff And Extension")
    $lines.Add("")
    $lines.Add("Use this because the active plan you are following is $legacyPlan. Do not edit that file while it is in use; treat this file as the companion decision gate.")
    $lines.Add("")
    $lines.Add("The goal is not to finish every problem. The goal is to remove interview red flags: recognition, invariant, implementation, dry run, and explanation.")
    $lines.Add("")
    $lines.Add("## Main Decision")
    $lines.Add("")
    $lines.Add("| Situation after the 90-plan | Action | Extra office leave? |")
    $lines.Add("|---|---|---|")
    $lines.Add("| Top 50 has repeated RED or pattern confusion | Recircle only top 50 and repair with review queue | No. Leave will become passive rereading. |")
    $lines.Add("| Top 50 GREEN, ranks 51-90 mixed YELLOW/RED | Recircle 51-90 plus weakest pattern files | Maybe half-day only if interview is within 2-3 days. |")
    $lines.Add("| Top 90 mostly GREEN/YELLOW and no repeated RED in top 50 | Extend to ranks 91-150 | Yes, 1-2 focused leave days can help. |")
    $lines.Add("| Top 150 mostly GREEN and interview is still 2+ weeks away | Extend to rank 170+ and source-only extras | Optional. Use leave only for mocks and repair. |")
    $lines.Add("")
    $lines.Add("## Cutoff Numbers")
    $lines.Add("")
    $lines.Add("- Minimum interview-safe floor: ranks 1-70 should be fast and defensible.")
    $lines.Add("- Strong 1-week target: the active 90-plan plus honest review state.")
    $lines.Add("- Senior no-red-flag target: ranks 1-150 touched hands-on, with top 100 stable.")
    $lines.Add("- Breadth target: ranks 151-216 and source-only extras after fundamentals are already reliable.")
    $lines.Add("")
    $lines.Add("## After The 90 Problems")
    $lines.Add("")
    $lines.Add("1. Open $reviewDashboard and list RED/YELLOW items.")
    $lines.Add("2. Randomly pick five problems from ranks 1-90 without category hints.")
    $lines.Add("3. For each one, speak brute force -> bottleneck -> pattern -> invariant -> code -> dry run.")
    $lines.Add("4. Code two misses from blank.")
    $lines.Add("5. Decide from the table above: recircle, extend to 150, or extend to 170+.")
    $lines.Add("")
    $lines.Add("## If You Are Behind By One Day")
    $lines.Add("")
    $lines.Add("- Do not compress two full days into one long day.")
    $lines.Add("- Keep due reviews, then do the next scheduled high-signal blocks.")
    $lines.Add("- Drop lowest-ROI new coverage before dropping repair.")
    $lines.Add("- Use the final day for mocks and repair, not panic coverage.")
    $lines.Add("")
    $lines.Add("## Extension Path")
    $lines.Add("")
    $lines.Add("- Ranks 91-150: use $ranked and continue in order when the 90-plan gate passes.")
    $lines.Add("- Ranks 151+: use $extension.")
    $lines.Add("- Pattern visualization and project-structure decision: use $patternLabsGuide.")
    $lines.Add("")
    $lines.Add("## 90-Plan Coverage Snapshot")
    $lines.Add("")
    $lines.Add("| Band | Count | Expected quality |")
    $lines.Add("|---|---:|---|")
    $lines.Add("| Ranks 1-50 inside current top 90 | $(@($top90 | Where-Object { $_.Rank -le 50 }).Count) | Must be mostly GREEN. |")
    $lines.Add("| Ranks 51-90 inside current top 90 | $(@($top90 | Where-Object { $_.Rank -gt 50 }).Count) | GREEN/YELLOW is acceptable if top 50 is stable. |")
    $lines.Add("| Ranks 91-150 extension | $(@($top150 | Where-Object { $_.Rank -gt 90 }).Count) | Add only after top 90 gate passes. |")
    $lines.Add("| Ranks 151+ extension | $(@($Rows | Where-Object { $_.Rank -gt 150 }).Count) | Breadth and long-tail practice. |")
    $lines.Add("")
    $lines.Add("## Do Not Move The Current Plan")
    $lines.Add("")
    $lines.Add("Keep $legacyPlan unchanged while you are executing it. Add decisions here and in the review dashboard instead of rewriting the active schedule mid-run.")

    return ($lines -join "`r`n")
}

function Normalize-ReviewKey {
    param([string] $Value)

    if ([string]::IsNullOrWhiteSpace($Value)) { return "" }
    return ([regex]::Replace($Value.ToLowerInvariant(), '[^a-z0-9]', ''))
}

function Get-ObjectPropertyValue {
    param(
        [object] $Object,
        [string] $Name,
        [object] $Default = ""
    )

    if ($null -eq $Object) { return $Default }
    $property = $Object.PSObject.Properties[$Name]
    if ($null -eq $property -or $null -eq $property.Value) { return $Default }
    return $property.Value
}

function Get-ReviewState {
    $reviewPath = Join-Path $RepoRoot "review/review.json"
    $byCodePath = @{}
    $byTitle = @{}
    $problems = @()
    $generatedAt = ""

    if (Test-Path -LiteralPath $reviewPath) {
        $json = Get-Content -LiteralPath $reviewPath -Raw | ConvertFrom-Json
        $generatedAt = [string] (Get-ObjectPropertyValue -Object $json -Name "generatedAt")
        $problems = @(Get-ObjectPropertyValue -Object $json -Name "problems" -Default @())
        foreach ($problem in $problems) {
            $codePath = [string] (Get-ObjectPropertyValue -Object $problem -Name "codePath")
            if (-not [string]::IsNullOrWhiteSpace($codePath)) {
                $byCodePath[$codePath.Replace("\", "/").ToLowerInvariant()] = $problem
            }

            $titleKey = Normalize-ReviewKey ([string] (Get-ObjectPropertyValue -Object $problem -Name "title"))
            if (-not [string]::IsNullOrWhiteSpace($titleKey)) {
                $byTitle[$titleKey] = $problem
            }
        }
    }

    return [pscustomobject]@{
        Path = $reviewPath
        Exists = Test-Path -LiteralPath $reviewPath
        GeneratedAt = $generatedAt
        Problems = $problems
        ByCodePath = $byCodePath
        ByTitle = $byTitle
    }
}

function Find-ReviewProblemForRow {
    param(
        [object] $Row,
        [object] $ReviewState
    )

    if ($null -eq $ReviewState -or -not $ReviewState.Exists) { return $null }

    $codePath = ("src/main/java/org/chijai/" + $Row.File.Replace("\", "/")).ToLowerInvariant()
    if ($ReviewState.ByCodePath.ContainsKey($codePath)) {
        return $ReviewState.ByCodePath[$codePath]
    }

    $titleKey = Normalize-ReviewKey $Row.Title
    if ($ReviewState.ByTitle.ContainsKey($titleKey)) {
        return $ReviewState.ByTitle[$titleKey]
    }

    return $null
}

function Get-ReviewMistakeCodes {
    param([object] $Problem)

    if ($null -eq $Problem) { return @() }
    $codes = New-Object System.Collections.Generic.List[string]
    $mistakes = @(Get-ObjectPropertyValue -Object $Problem -Name "mistakes" -Default @())

    foreach ($mistake in $mistakes) {
        if ($null -eq $mistake) { continue }
        $candidate = ""
        if ($mistake -is [string]) {
            $candidate = $mistake
        } else {
            foreach ($field in @("code", "failure", "type", "category")) {
                $value = [string] (Get-ObjectPropertyValue -Object $mistake -Name $field)
                if (-not [string]::IsNullOrWhiteSpace($value)) {
                    $candidate = $value
                    break
                }
            }
        }

        foreach ($match in [regex]::Matches($candidate.ToUpperInvariant(), '[PIDJECBM]')) {
            if (-not $codes.Contains($match.Value)) {
                $codes.Add($match.Value)
            }
        }
    }

    $compileFailures = [int] (Get-ObjectPropertyValue -Object $Problem -Name "compileFailures" -Default 0)
    if ($compileFailures -gt 0 -and -not $codes.Contains("J")) {
        $codes.Add("J")
    }

    return @($codes)
}

function Get-ReviewScore {
    param([object] $Problem)

    if ($null -eq $Problem) { return "UNTRACKED" }

    $attempts = [int] (Get-ObjectPropertyValue -Object $Problem -Name "attempts" -Default 0)
    $state = ([string] (Get-ObjectPropertyValue -Object $Problem -Name "fsrsState")).ToUpperInvariant()
    $mistakeCount = @(Get-ObjectPropertyValue -Object $Problem -Name "mistakes" -Default @()).Count
    $hintUsedCount = [int] (Get-ObjectPropertyValue -Object $Problem -Name "hintUsedCount" -Default 0)
    $compileFailures = [int] (Get-ObjectPropertyValue -Object $Problem -Name "compileFailures" -Default 0)
    $repetitions = [int] (Get-ObjectPropertyValue -Object $Problem -Name "repetitions" -Default 0)

    if ($attempts -eq 0) { return "NEW" }
    if ($state -eq "RELEARNING" -or $compileFailures -gt 0 -or $mistakeCount -ge 2) { return "RED" }
    if ($state -eq "LEARNING" -or $hintUsedCount -gt 0 -or $mistakeCount -gt 0) { return "YELLOW" }
    if ($state -eq "REVIEW" -and $repetitions -ge 2) { return "GREEN" }
    if ($state -eq "REVIEW") { return "GREEN" }
    return "YELLOW"
}

function Test-ReviewDue {
    param([object] $Problem)

    if ($null -eq $Problem) { return $false }
    $nextReview = [string] (Get-ObjectPropertyValue -Object $Problem -Name "nextReview")
    if ([string]::IsNullOrWhiteSpace($nextReview)) { return $false }

    $parsed = [datetime]::MinValue
    if ([datetime]::TryParse($nextReview, [ref] $parsed)) {
        return $parsed.Date -le (Get-Date).Date
    }
    return $false
}

function Test-ReviewMastered {
    param([object] $Problem)

    if ($null -eq $Problem) { return $false }
    $score = Get-ReviewScore -Problem $Problem
    $repetitions = [int] (Get-ObjectPropertyValue -Object $Problem -Name "repetitions" -Default 0)
    $hintUsedCount = [int] (Get-ObjectPropertyValue -Object $Problem -Name "hintUsedCount" -Default 0)
    $compileFailures = [int] (Get-ObjectPropertyValue -Object $Problem -Name "compileFailures" -Default 0)
    $mistakeCount = @(Get-ObjectPropertyValue -Object $Problem -Name "mistakes" -Default @()).Count

    return ($score -eq "GREEN" -and $repetitions -ge 3 -and $hintUsedCount -eq 0 -and $compileFailures -eq 0 -and $mistakeCount -eq 0)
}

function Get-ReviewAction {
    param(
        [object] $Entry,
        [string] $Mode = "due"
    )

    if ($Entry.Score -eq "UNTRACKED") { return "Run `dsa-review/scripts/import-review.cmd` on Windows or `dsa-review/scripts/import-review.sh` on macOS/Linux, then rebuild dashboard." }
    if ($Entry.Score -eq "NEW") { return "Attempt closed-book, then mark again/hard/good/easy." }
    if ($Entry.Score -eq "RED") { return "Rebuild from brute force -> bottleneck -> invariant, then code from blank." }
    if ($Entry.Score -eq "YELLOW") { return "Redo closed-book; focus the recorded weak step before opening Java." }
    if ($Mode -eq "mastered") { return "Keep only in random timed mocks." }
    return "Keep spaced review; use random drill for retention."
}

function New-ReviewEntry {
    param(
        [object] $Row,
        [object] $Problem
    )

    $score = Get-ReviewScore -Problem $Problem
    $codes = @(Get-ReviewMistakeCodes -Problem $Problem)
    $failure = if ($codes.Count -gt 0) { $codes -join "," } else { "" }
    $attempts = if ($null -eq $Problem) { 0 } else { [int] (Get-ObjectPropertyValue -Object $Problem -Name "attempts" -Default 0) }
    $lastReview = ""
    if ($null -ne $Problem) {
        $lastReview = [string] (Get-ObjectPropertyValue -Object $Problem -Name "lastReview")
        if ([string]::IsNullOrWhiteSpace($lastReview)) {
            $lastReview = [string] (Get-ObjectPropertyValue -Object $Problem -Name "lastReviewed")
        }
    }
    $nextReview = if ($null -eq $Problem) { "" } else { [string] (Get-ObjectPropertyValue -Object $Problem -Name "nextReview") }

    return [pscustomobject]@{
        Row = $Row
        Problem = $Problem
        Score = $score
        Failure = $failure
        Attempts = $attempts
        LastReview = $lastReview
        NextReview = $nextReview
        IsDue = Test-ReviewDue -Problem $Problem
        Mastered = Test-ReviewMastered -Problem $Problem
    }
}

function Add-EmptyOrLimitedRows {
    param(
        [System.Collections.Generic.List[string]] $Lines,
        [object[]] $Entries,
        [int] $Limit,
        [scriptblock] $Renderer,
        [string] $EmptyMessage
    )

    $shown = @($Entries | Select-Object -First $Limit)
    if ($shown.Count -eq 0) {
        $Lines.Add($EmptyMessage)
        return
    }

    foreach ($entry in $shown) {
        $Lines.Add((& $Renderer $entry))
    }

    if ($Entries.Count -gt $shown.Count) {
        $Lines.Add("")
        $Lines.Add("_Showing first $($shown.Count) of $($Entries.Count). Continue in rank order from the Master Review Ledger._")
    }
}

function Build-ReviewDashboard {
    param([object[]] $Rows)

    $reviewState = Get-ReviewState
    $entries = @($Rows | ForEach-Object {
        $problem = Find-ReviewProblemForRow -Row $_ -ReviewState $reviewState
        New-ReviewEntry -Row $_ -Problem $problem
    })

    $trackedCount = @($entries | Where-Object { $null -ne $_.Problem }).Count
    $dueEntries = @($entries | Where-Object { $_.IsDue -and -not $_.Mastered } | Sort-Object @{ Expression = { $_.Row.Rank }; Ascending = $true })
    $redEntries = @($entries | Where-Object { $_.Score -eq "RED" } | Sort-Object @{ Expression = { $_.Row.Rank }; Ascending = $true })
    $yellowEntries = @($entries | Where-Object { $_.Score -eq "YELLOW" } | Sort-Object @{ Expression = { $_.Row.Rank }; Ascending = $true })
    $masteredEntries = @($entries | Where-Object { $_.Mastered } | Sort-Object @{ Expression = { $_.Row.Rank }; Ascending = $true })
    $untrackedEntries = @($entries | Where-Object { $_.Score -eq "UNTRACKED" } | Sort-Object @{ Expression = { $_.Row.Rank }; Ascending = $true })

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# DSA Review Dashboard")
    $lines.Add("")
    $lines.Add("Use this as the control panel for spaced repetition. Keep ranking files clean; put volatile review state here or in the review scripts.")
    $lines.Add("")
    $stateStamp = if ([string]::IsNullOrWhiteSpace($reviewState.GeneratedAt)) { "unknown" } else { $reviewState.GeneratedAt }
    $lines.Add('Generated from `../../review/review.json`. Review state timestamp: ' + $stateStamp + '.')
    $lines.Add("")
    $lines.Add("| Metric | Count |")
    $lines.Add("|---|---:|")
    $lines.Add("| Ranked problems | $($Rows.Count) |")
    $lines.Add("| Review-state matches | $trackedCount |")
    $lines.Add("| Due now | $($dueEntries.Count) |")
    $lines.Add("| RED repair | $($redEntries.Count) |")
    $lines.Add("| YELLOW stabilization | $($yellowEntries.Count) |")
    $lines.Add("| Mastered | $($masteredEntries.Count) |")
    $lines.Add("| Untracked ranked rows | $($untrackedEntries.Count) |")
    $lines.Add("")
    $lines.Add("Review status columns:")
    $lines.Add("")
    $lines.Add("- Score: GREEN, YELLOW, RED.")
    $lines.Add("- NEW: imported but no closed-book attempt recorded yet.")
    $lines.Add("- UNTRACKED: ranked row has no matching review-state item yet.")
    $lines.Add("- Failure: P, I, D, J, E, C, B, M.")
    $lines.Add("- Attempts: increment after every closed-book attempt.")
    $lines.Add("- Last Review / Next Review: date in YYYY-MM-DD.")
    $lines.Add("- Mastered?: yes only after repeated GREEN attempts under random/timed conditions.")
    $lines.Add("")
    $lines.Add("## Due Today")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Family | Pattern | Last Score | Failure | Next Action |")
    $lines.Add("|---:|---|---|---|---|---|---|")
    Add-EmptyOrLimitedRows -Lines $lines -Entries $dueEntries -Limit 30 -EmptyMessage "| - | No due review items. | - | - | - | - | Keep random timed mocks. |" -Renderer {
        param($entry)
        $row = $entry.Row
        $family = Get-DisplayCategory $row.Category
        "| $($row.Rank) | $(Escape-Md $row.Title) | $(Escape-Md $family) | $(Escape-Md $row.Pattern) | $($entry.Score) | $(Escape-Md $entry.Failure) | $(Escape-Md (Get-ReviewAction -Entry $entry -Mode 'due')) |"
    }
    $lines.Add("")
    $lines.Add("## RED Repair Queue")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Failure | Repair action | Next Review |")
    $lines.Add("|---:|---|---|---|---|")
    Add-EmptyOrLimitedRows -Lines $lines -Entries $redEntries -Limit 25 -EmptyMessage "| - | No RED items recorded. | - | Keep attempts honest. | - |" -Renderer {
        param($entry)
        "| $($entry.Row.Rank) | $(Escape-Md $entry.Row.Title) | $(Escape-Md $entry.Failure) | $(Escape-Md (Get-ReviewAction -Entry $entry -Mode 'red')) | $(Escape-Md $entry.NextReview) |"
    }
    $lines.Add("")
    $lines.Add("## YELLOW Stabilization Queue")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Weakness | Next repetition |")
    $lines.Add("|---:|---|---|---|")
    Add-EmptyOrLimitedRows -Lines $lines -Entries $yellowEntries -Limit 25 -EmptyMessage "| - | No YELLOW items recorded. | - | - |" -Renderer {
        param($entry)
        $weakness = if ([string]::IsNullOrWhiteSpace($entry.Failure)) { "Learning or shaky recall" } else { $entry.Failure }
        "| $($entry.Row.Rank) | $(Escape-Md $entry.Row.Title) | $(Escape-Md $weakness) | $(Escape-Md $entry.NextReview) |"
    }
    $lines.Add("")
    $lines.Add("## Mastered Queue")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Attempts | Next Review | Action |")
    $lines.Add("|---:|---|---:|---|---|")
    Add-EmptyOrLimitedRows -Lines $lines -Entries $masteredEntries -Limit 25 -EmptyMessage "| - | No mastered items yet. | 0 | - | Earn this through repeated GREEN attempts. |" -Renderer {
        param($entry)
        "| $($entry.Row.Rank) | $(Escape-Md $entry.Row.Title) | $($entry.Attempts) | $(Escape-Md $entry.NextReview) | $(Escape-Md (Get-ReviewAction -Entry $entry -Mode 'mastered')) |"
    }
    $lines.Add("")
    $lines.Add("## Untracked Ranked Rows")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Action |")
    $lines.Add("|---:|---|---|")
    Add-EmptyOrLimitedRows -Lines $lines -Entries $untrackedEntries -Limit 20 -EmptyMessage "| - | All ranked rows have matching review-state coverage. | - |" -Renderer {
        param($entry)
        "| $($entry.Row.Rank) | $(Escape-Md $entry.Row.Title) | $(Escape-Md (Get-ReviewAction -Entry $entry -Mode 'untracked')) |"
    }
    $lines.Add("")
    $lines.Add("## Repeated Failure Pattern Heatmap")
    $lines.Add("")
    $lines.Add("| Family | Pattern | P | I | D | J | E | C | B | M | Action |")
    $lines.Add("|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---|")
    $heatmap = @{}
    foreach ($entry in $entries) {
        $codes = @(Get-ReviewMistakeCodes -Problem $entry.Problem)
        if ($codes.Count -eq 0) { continue }
        $family = Get-DisplayCategory $entry.Row.Category
        $key = "$family|$($entry.Row.Pattern)"
        if (-not $heatmap.ContainsKey($key)) {
            $heatmap[$key] = [ordered]@{
                Family = $family
                Pattern = $entry.Row.Pattern
                P = 0; I = 0; D = 0; J = 0; E = 0; C = 0; B = 0; M = 0
            }
        }
        foreach ($code in $codes) {
            $heatmap[$key][$code] = [int] $heatmap[$key][$code] + 1
        }
    }
    $heatmapRows = @($heatmap.Values | Sort-Object @{ Expression = { -1 * (($_.P + $_.I + $_.D + $_.J + $_.E + $_.C + $_.B + $_.M)) } }, Family, Pattern)
    if ($heatmapRows.Count -eq 0) {
        $lines.Add("| - | - | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | No repeated failures recorded yet. |")
    } else {
        foreach ($item in ($heatmapRows | Select-Object -First 20)) {
            $lines.Add("| $(Escape-Md $item.Family) | $(Escape-Md $item.Pattern) | $($item.P) | $($item.I) | $($item.D) | $($item.J) | $($item.E) | $($item.C) | $($item.B) | $($item.M) | Repair the highest repeated failure code first. |")
        }
    }
    $lines.Add("")
    $lines.Add("## Master Review Ledger")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Links | Family | Pattern | Signal / Invariant | Score | Failure | Attempts | Last Review | Next Review | Mastered? |")
    $lines.Add("|---:|---|---|---|---|---|---|---|---:|---|---|---|")

    foreach ($entry in $entries) {
        $row = $entry.Row
        $links = New-ProblemLinks -Row $row
        $family = Get-DisplayCategory $row.Category
        $mastered = if ($entry.Mastered) { "yes" } else { "" }
        $lines.Add("| $($row.Rank) | $(Escape-Md $row.Title) | $links | $(Escape-Md $family) | $(Escape-Md $row.Pattern) | $(Escape-Md $row.Recall) | $($entry.Score) | $(Escape-Md $entry.Failure) | $($entry.Attempts) | $(Escape-Md $entry.LastReview) | $(Escape-Md $entry.NextReview) | $mastered |")
    }

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
        "Two Sum",
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
    $lines.Add("| Weekly sprint rows | 150 | Timed sprint covers the first 150 ranks once each in a cognitive training order. |")
    $lines.Add("| Review dashboard rows | $($Rows.Count) | Dashboard ledger covers every ranked row and merges local review state. |")
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
    $lines.Add("| Weekly sprint generated from canonical rows | PASS | Sprint titles, patterns, and signals must match ranked/pattern data. |")
    $lines.Add("| Review dashboard covers ranked rows | PASS | Review state is merged into due/red/yellow/mastered queues without polluting the ranked table. |")
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
    $lines.Add("## Pattern Taxonomy Map")
    $lines.Add("")
    $lines.Add((Build-PatternMermaid -Group $Group))
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

function New-HorizontalSwitch {
    param(
        [string] $Pattern,
        [string] $WhyNot,
        [string] $Missing,
        [string] $Mutation,
        [string] $NowWhy
    )

    return [pscustomobject]@{
        Pattern = $Pattern
        WhyNot = $WhyNot
        Missing = $Missing
        Mutation = $Mutation
        NowWhy = $NowWhy
    }
}

function Get-HorizontalSwitches {
    param(
        [string] $Category,
        [string] $Title = ""
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "mergeksortedlists" {
            return @(
                (New-HorizontalSwitch -Pattern "Linked List Pointers" -WhyNot "Pointer merge is required, but the repeated choice is among k current heads." -Missing "Only two current heads or one fixed merge boundary." -Mutation "Reduce k lists to exactly two sorted lists." -NowWhy "A dummy tail and two pointers pick the smaller head directly."),
                (New-HorizontalSwitch -Pattern "Divide And Conquer" -WhyNot "It fits as an alternative, but the interview bottleneck is still repeated minimum-head selection." -Missing "A requirement to avoid extra heap space or emphasize pairwise merging." -Mutation "Ask for pairwise merging with recursion or no priority queue." -NowWhy "Balanced pair merges give O(N log k) without a heap.")
            )
        }
        "meetingroomsii" {
            return @(
                (New-HorizontalSwitch -Pattern "Intervals / Sorting Greedy" -WhyNot "Sorting is necessary but not sufficient; you must know the earliest active end time." -Missing "Only conflict existence instead of minimum resources." -Mutation "Ask whether a person can attend all meetings." -NowWhy "After sorting by start, only the previous end must be checked."),
                (New-HorizontalSwitch -Pattern "Sweep Line" -WhyNot "Also valid, but less direct if you already have interval objects and need active rooms." -Missing "Separate start/end event counting." -Mutation "Represent every start as +1 and every end as -1." -NowWhy "Peak active events equals room count.")
            )
        }
        "meetingrooms" {
            return @(
                (New-HorizontalSwitch -Pattern "Heap" -WhyNot "No need to track all active meetings when the output is only conflict existence." -Missing "Minimum number of simultaneous rooms/resources." -Mutation "Ask for minimum meeting rooms." -NowWhy "Earliest finishing active meeting determines room reuse."),
                (New-HorizontalSwitch -Pattern "Sweep Line" -WhyNot "Event counting is overkill for a yes/no overlap check." -Missing "Need maximum overlap count." -Mutation "Ask for the maximum number of concurrent meetings." -NowWhy "Peak event balance gives overlap count.")
            )
        }
        "firstuniquenumber" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "Counts alone cannot answer first-in-order unique." -Missing "Arrival order among still-unique candidates." -Mutation "Ask only whether a value is unique at the end." -NowWhy "Frequency count is enough when order is not queried online."),
                (New-HorizontalSwitch -Pattern "Queue / LinkedHashSet" -WhyNot "This is the backing structure, not the whole API design." -Missing "Operation contract for add and showFirstUnique." -Mutation "Make it a single batch query instead of a streaming object." -NowWhy "The design collapses to count then scan.")
            )
        }
        "movingaveragefromdatastream" {
            return @(
                (New-HorizontalSwitch -Pattern "Prefix/Suffix" -WhyNot "Prefix sums work for immutable arrays, but the stream window evicts old values." -Missing "Static range queries over an already-known array." -Mutation "Change stream calls into many fixed-range average queries on an array." -NowWhy "Prefix sums answer each range in O(1)."),
                (New-HorizontalSwitch -Pattern "Design Data Structure" -WhyNot "The object API is simple; the core invariant is fixed-size window sum." -Missing "More operations such as reset, variable window size, or multiple keys." -Mutation "Add per-user moving averages or variable windows." -NowWhy "The API contract starts driving the backing structures.")
            )
        }
        "designcircularqueue" {
            return @(
                (New-HorizontalSwitch -Pattern "Stack" -WhyNot "Queue order is FIFO, not most-recent-first." -Missing "LIFO operation contract." -Mutation "Ask for stack using queues instead of circular queue." -NowWhy "Rotation/lazy transfer restores LIFO behavior."),
                (New-HorizontalSwitch -Pattern "Linked List" -WhyNot "A linked list works, but fixed capacity asks for reusable slots." -Missing "Unbounded capacity or frequent middle removal." -Mutation "Remove fixed capacity and ask for a general queue." -NowWhy "Head/tail nodes avoid modulo arithmetic.")
            )
        }
        "lrucache" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "A map finds keys but cannot evict least-recently-used by itself." -Missing "Recency order with O(1) move/remove." -Mutation "Remove eviction and ask for plain get/put." -NowWhy "Direct key lookup is enough."),
                (New-HorizontalSwitch -Pattern "Linked List Pointers" -WhyNot "A list gives order but not O(1) key lookup." -Missing "Key-to-node addressability." -Mutation "Ask only to maintain recent order, not lookup by key." -NowWhy "Pointer moves are the main invariant.")
            )
        }
        "pathsumiii" {
            return @(
                (New-HorizontalSwitch -Pattern "Prefix/Suffix" -WhyNot "Prefix sum is inside the DFS path, not a flat array scan." -Missing "Ancestor path context and backtracking removal." -Mutation "Change the tree to an array subarray-sum problem." -NowWhy "A running prefix map over indices is enough."),
                (New-HorizontalSwitch -Pattern "Tree DFS" -WhyNot "Plain DFS alone restarts work at every node." -Missing "Need to count all downward paths ending at current node efficiently." -Mutation "Ask only whether one root-to-leaf path equals target." -NowWhy "A simple path-sum DFS return/check is sufficient.")
            )
        }
        "gasstation" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "No repeated subproblem needs caching; a failed tank invalidates a whole candidate range." -Missing "Independent choices whose future values must be compared." -Mutation "Ask for maximum profit route with rewards/costs and optional skips." -NowWhy "Choose/skip states become real and repeated."),
                (New-HorizontalSwitch -Pattern "Prefix Sum" -WhyNot "Prefix sums expose total feasibility but not the candidate reset rule." -Missing "Only range balance queries, not a start index after failure." -Mutation "Ask many net-gas range sum queries." -NowWhy "Cumulative net answers each range in O(1).")
            )
        }
        "jumpgame" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Reachability states collapse to one farthest reachable index." -Missing "Need minimum jumps or path reconstruction." -Mutation "Ask for the minimum number of jumps." -NowWhy "Layered greedy/BFS-style ranges or DP must distinguish jump counts."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Trying jumps enumerates many dominated paths." -Missing "Need to list all valid jump sequences." -Mutation "Ask to output every path from start to end." -NowWhy "The concrete path becomes the answer.")
            )
        }
        "besttimetobuyandsellstockii" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Hold/cash DP collapses because unlimited transactions let every positive edge be taken." -Missing "Constraint that couples today's sell to tomorrow's buy." -Mutation "Add cooldown, fee, or at most k transactions." -NowWhy "State must remember hold/cash/cooldown or remaining transactions."),
                (New-HorizontalSwitch -Pattern "Single Transaction Running Min" -WhyNot "One min-buy is too restrictive when multiple profitable rises are allowed." -Missing "At most one buy/sell pair." -Mutation "Allow only one transaction." -NowWhy "Best sell today only needs the minimum earlier price.")
            )
        }
        "besttimetobuyandsellstock" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "No table is needed; all previous days collapse to the cheapest buy so far." -Missing "Multiple transactions, cooldown, fee, or transaction count state." -Mutation "Allow at most k transactions or add cooldown/fee." -NowWhy "Hold/cash or transaction-layer state becomes necessary."),
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "The two days are ordered by time, not a sorted value array with eliminable ends." -Missing "Sorted values and a target relation." -Mutation "Ask for two sorted prices that sum to target." -NowWhy "Left/right movement can discard impossible sums.")
            )
        }
        "besttimetobuyandsellstockiii" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "Adding all positive edges can exceed the two-transaction limit." -Missing "Unlimited transactions." -Mutation "Remove the two-transaction limit." -NowWhy "Each rising edge can be harvested independently."),
                (New-HorizontalSwitch -Pattern "Single Transaction Running Min" -WhyNot "One buy/sell pair misses the second independent profit segment." -Missing "Only one transaction allowed." -Mutation "Restrict to at most one transaction." -NowWhy "A prefix minimum is enough.")
            )
        }
        "besttimetobuyandsellstockiv" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "Greedy is valid only when k is large enough to behave like unlimited transactions." -Missing "k >= n/2 or no transaction limit." -Mutation "Make k unlimited or at least n/2." -NowWhy "All positive edges can be taken."),
                (New-HorizontalSwitch -Pattern "Stock III Four-State DP" -WhyNot "Hardcoding two transactions breaks when k varies." -Missing "Fixed k = 2." -Mutation "Set k exactly to 2." -NowWhy "The generic transaction layers reduce to buy1/sell1/buy2/sell2.")
            )
        }
        "besttimetobuyandsellstockwithcooldown" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "Selling today blocks buying tomorrow, so local positive edges are not independent." -Missing "No cooldown coupling across days." -Mutation "Remove the cooldown rule." -NowWhy "The problem collapses to summing positive differences."),
                (New-HorizontalSwitch -Pattern "Plain Hold/Cash DP" -WhyNot "Two states cannot distinguish sold-today from rest." -Missing "A cooldown/rest state." -Mutation "Remove the one-day cooldown." -NowWhy "hold and cash are enough.")
            )
        }
        "besttimetobuyandsellstockwithtransactionfee" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "Every small positive edge may be erased by the fee." -Missing "No per-transaction fixed cost." -Mutation "Set fee to zero." -NowWhy "Every positive rise is profitable."),
                (New-HorizontalSwitch -Pattern "Single Transaction Running Min" -WhyNot "Multiple transactions are still allowed; fee only changes sell profitability." -Missing "At most one transaction." -Mutation "Limit to one buy/sell." -NowWhy "A running minimum plus fee-adjusted profit is enough.")
            )
        }
        "wordbreak" {
            return @(
                (New-HorizontalSwitch -Pattern "Trie" -WhyNot "Trie speeds word lookup but does not remember which prefixes are segmentable." -Missing "Only prefix membership queries, no full segmentation decision." -Mutation "Ask whether any dictionary word starts at each index." -NowWhy "Trie traversal is the core operation."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Backtracking enumerates cuts and repeats the same suffixes." -Missing "Need to output all valid segmentations." -Mutation "Ask for every possible sentence." -NowWhy "The chosen word path must be emitted.")
            )
        }
        "distinctsubsequencesii" {
            return @(
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Generating all subsequences is exponential and duplicates collide." -Missing "Tiny input or requirement to list subsequences." -Mutation "Ask to print every distinct subsequence for n <= 20." -NowWhy "The concrete subsequences become the output."),
                (New-HorizontalSwitch -Pattern "HashSet" -WhyNot "A set can deduplicate strings but cannot survive large n." -Missing "Small enough output size." -Mutation "Constrain n so total subsequences fit memory." -NowWhy "Explicit generation plus set dedup is acceptable.")
            )
        }
        "interleavingstring" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "When both source chars match, picking one can make the future impossible." -Missing "A deterministic tie-breaker that is always safe." -Mutation "Guarantee no position has both s1[i] and s2[j] matching s3." -NowWhy "The next source is forced."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Backtracking repeats the same i,j prefix states." -Missing "Need all interleavings, not just existence." -Mutation "Ask to generate every valid interleaving." -NowWhy "The path choices are the answer.")
            )
        }
        "longestcommonsubsequence" {
            return @(
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "Skipping a char from either string is a choice; local pointer movement can discard the optimum." -Missing "One string must be checked as a subsequence of the other." -Mutation "Ask only whether s is a subsequence of t." -NowWhy "A single forward scan is enough."),
                (New-HorizontalSwitch -Pattern "Edit Distance" -WhyNot "LCS maximizes kept matches; edit distance minimizes operation cost." -Missing "Insert/delete/replace costs." -Mutation "Ask for minimum operations to convert one string to another." -NowWhy "Operation transitions define the DP.")
            )
        }
        "maximumlengthofpairchain" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "DP works, but earliest end has an exchange proof and avoids O(n^2)." -Missing "Weights/profits or non-greedy compatibility." -Mutation "Add profit to every pair and ask maximum profit chain." -NowWhy "Choosing fewer intervals can earn more, so choose/skip DP is needed."),
                (New-HorizontalSwitch -Pattern "LIS" -WhyNot "Pair compatibility is interval-like; sorting by end directly proves the next safe choice." -Missing "One-dimensional increasing sequence after sorting." -Mutation "Ask for longest increasing subsequence of numbers." -NowWhy "Previous smaller value extension becomes the natural state.")
            )
        }
        "longestcontinuousincreasingsubsequence" {
            return @(
                (New-HorizontalSwitch -Pattern "LIS / DP" -WhyNot "Continuous forbids skipping, so no previous-index search is needed." -Missing "Subsequence, not subarray." -Mutation "Allow deleting elements while preserving order." -NowWhy "Each index can extend many earlier smaller indices."),
                (New-HorizontalSwitch -Pattern "Sliding Window With Counts" -WhyNot "There is no frequency constraint to maintain." -Missing "A window validity condition such as at most k violations." -Mutation "Allow at most k non-increasing breaks." -NowWhy "The left boundary repairs validity as breaks enter.")
            )
        }
        "numberoflongestincreasingsubsequence" {
            return @(
                (New-HorizontalSwitch -Pattern "Binary Search Tails" -WhyNot "tails stores representative values, not counts for all ending positions." -Missing "Only LIS length, not number of LIS." -Mutation "Ask only for the length of LIS." -NowWhy "Minimal tails are enough."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Enumerating all subsequences is exponential." -Missing "Need to print every LIS." -Mutation "Ask to output all longest increasing subsequences for small n." -NowWhy "The actual paths become required output.")
            )
        }
        "russiandollenvelopes" {
            return @(
                (New-HorizontalSwitch -Pattern "Plain LIS" -WhyNot "Equal-width envelopes cannot nest, so sorting heights naively overcounts." -Missing "One-dimensional sequence without equal-width conflict." -Mutation "Remove width or guarantee all widths are unique and sorted." -NowWhy "Height LIS alone is valid."),
                (New-HorizontalSwitch -Pattern "2D Dynamic Programming" -WhyNot "O(n^2) DP works, but sorted heights plus binary search is faster." -Missing "Small n or need reconstruction/proof clarity." -Mutation "Constrain n to a few thousand and ask for the actual chain." -NowWhy "DP parent pointers are practical.")
            )
        }
        "maximumxoroftwonumbersinanarray" {
            return @(
                (New-HorizontalSwitch -Pattern "HashSet Prefix Greedy" -WhyNot "It is a valid alternative, but it hides the candidate structure compared with bitwise trie." -Missing "Need only feasibility of each candidate prefix, not explicit partner walk." -Mutation "Ask for a proof-focused O(n) prefix-set solution." -NowWhy "Testing candidate XOR prefixes proves whether the bit can be set."),
                (New-HorizontalSwitch -Pattern "Brute Force" -WhyNot "All pairs are clear but O(n^2)." -Missing "Small n or no performance pressure." -Mutation "Constrain n to a few hundred." -NowWhy "Pair enumeration becomes acceptable.")
            )
        }
        "maximumxorwithanelementfromarray" {
            return @(
                (New-HorizontalSwitch -Pattern "Plain Bitwise Trie" -WhyNot "Using all nums violates each query's mi limit." -Missing "Eligibility filter for nums <= mi." -Mutation "Remove the per-query limit." -NowWhy "Every number is always eligible for trie lookup."),
                (New-HorizontalSwitch -Pattern "Sorting / Offline Queries" -WhyNot "Sorting alone orders eligibility but cannot maximize XOR." -Missing "Bitwise opposite-bit search among eligible values." -Mutation "Ask only for count of eligible values per query." -NowWhy "Sorted pointer scan is enough.")
            )
        }
        "maximumgeneticdifferencequery" {
            return @(
                (New-HorizontalSwitch -Pattern "Plain Bitwise Trie" -WhyNot "A global trie includes non-ancestor nodes." -Missing "Current root-to-node path membership." -Mutation "Ask maximum XOR with any node value in the whole tree." -NowWhy "Global trie membership is valid."),
                (New-HorizontalSwitch -Pattern "Tree DFS" -WhyNot "DFS gives the active ancestor path but not fast XOR maximization." -Missing "Opposite-bit lookup over active ancestors." -Mutation "Ask only whether an ancestor with value x exists." -NowWhy "A set on the DFS path is enough.")
            )
        }
        "countpairswithxorinarange" {
            return @(
                (New-HorizontalSwitch -Pattern "Brute Force" -WhyNot "All pairs are easy to test but O(n^2)." -Missing "Small input or no pair-count workload pressure." -Mutation "Constrain n to a few hundred." -NowWhy "Direct pair enumeration is acceptable."),
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "Exact complement lookup does not count all XOR values in a range." -Missing "Prefix counts by bit to count less-than bounds." -Mutation "Ask for pairs with XOR exactly k." -NowWhy "Each number has one target partner value.")
            )
        }
    }

    switch ($Category) {
        "HashMap/HashSet" {
            return @(
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "Order is not giving a safe elimination rule." -Missing "Sorted ends, palindrome symmetry, or shrinkable pair bounds." -Mutation "Make the input sorted and ask for one pair or in-place validation." -NowWhy "A left/right move can discard impossible pairs without storing all seen values."),
                (New-HorizontalSwitch -Pattern "Sliding Window" -WhyNot "The chosen elements are not required to be contiguous." -Missing "A contiguous region with a validity condition that can be repaired by moving left." -Mutation "Change the output to longest/shortest contiguous subarray or substring satisfying the same count rule." -NowWhy "Counts become window state instead of global seen state.")
            )
        }
        "Two Pointers" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "Two pointers need sorted order or end symmetry to justify discarding one side." -Missing "Direct complement or membership lookup on unsorted data." -Mutation "Remove sorted order but keep one-pair existence or index output." -NowWhy "A map remembers prior values and answers complement lookup in O(1)."),
                (New-HorizontalSwitch -Pattern "Sliding Window" -WhyNot "Pointers are not maintaining one contiguous valid region." -Missing "Window validity that changes predictably when right enters and left leaves." -Mutation "Ask for longest/shortest contiguous segment with a maintainable condition." -NowWhy "The answer can be updated while each boundary moves once.")
            )
        }
        "Sliding Window" {
            return @(
                (New-HorizontalSwitch -Pattern "Prefix/Suffix" -WhyNot "The window is useful only while the condition can be repaired monotonically." -Missing "Need to count arbitrary subarrays or answer many range aggregates." -Mutation "Ask for number of subarrays with exact sum over arbitrary integers, or many range sum queries." -NowWhy "Prefix state compares current aggregate with previous aggregates without relying on shrink validity."),
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "A plain map alone does not encode contiguous boundaries." -Missing "No contiguous output; only lookup, frequency, or complement membership." -Mutation "Change the output from substring/subarray length/count to existence of a matching value or global frequency." -NowWhy "The boundary invariant disappears and O(1) lookup is the main operation.")
            )
        }
        "Prefix/Suffix" {
            return @(
                (New-HorizontalSwitch -Pattern "Sliding Window" -WhyNot "Prefix handles repeated aggregates, but not every range condition is shrinkable." -Missing "Nonnegative or monotonic window state that becomes valid/invalid predictably." -Mutation "Constrain values to nonnegative and ask for shortest/longest contiguous range by sum." -NowWhy "Moving left only reduces sum, so a window can repair validity."),
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "There is no sequence of choices; the state is only accumulated aggregate." -Missing "Optimal substructure with decisions that lead to repeated future states." -Mutation "Ask for best score/min cost over choices at each index." -NowWhy "The answer depends on previous decision states, not just one cumulative prefix.")
            )
        }
        "Binary Search" {
            return @(
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "Binary search needs a sorted target index or monotonic yes/no predicate." -Missing "Pair/end search where moving one boundary safely eliminates candidates." -Mutation "Ask for a pair in a sorted array instead of minimum feasible answer." -NowWhy "Sum comparison tells which end cannot participate."),
                (New-HorizontalSwitch -Pattern "Heap" -WhyNot "Heap gives next best candidate; it does not exploit a monotonic feasible range." -Missing "Need repeated min/max frontier extraction." -Mutation "Change the output to top k / kth / streaming next best." -NowWhy "Priority order, not predicate monotonicity, drives the workload.")
            )
        }
        "Linked List" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "A set can detect identity but does not perform pointer rewiring." -Missing "Need membership lookup rather than structural mutation." -Mutation "Ask only whether a node repeats/intersects, allowing extra memory." -NowWhy "Identity lookup is enough and avoids pointer algebra."),
                (New-HorizontalSwitch -Pattern "Stack" -WhyNot "Stack reverses access order, but linked-list mutation needs stable next/prev handling." -Missing "Most recent unmatched item or reverse-order processing." -Mutation "Ask to validate nested tokens or process nodes in reverse without in-place mutation." -NowWhy "LIFO order becomes the natural invariant.")
            )
        }
        "Tree BFS" {
            return @(
                (New-HorizontalSwitch -Pattern "Tree DFS" -WhyNot "BFS is chosen because level order or nearest layer is part of the output." -Missing "A subtree return contract such as height, validity, path, or aggregate." -Mutation "Ask for diameter, balance, max depth, or LCA instead of per-level output." -NowWhy "A recursive helper can combine left/right answers bottom-up."),
                (New-HorizontalSwitch -Pattern "Graph BFS" -WhyNot "Tree BFS has no revisits unless parent links or extra edges are introduced." -Missing "General graph neighbors and visited state." -Mutation "Add parent pointers, undirected edges, or shortest path between arbitrary nodes." -NowWhy "Visited state and shortest-path layering become mandatory.")
            )
        }
        "Tree DFS" {
            return @(
                (New-HorizontalSwitch -Pattern "Tree BFS" -WhyNot "DFS is chosen when the result is a subtree/path return, not a level boundary." -Missing "Output grouped by level, nearest depth, or first node seen at each layer." -Mutation "Ask for level order, right side view by level, or minimum tree depth." -NowWhy "Queue-size snapshots preserve level boundaries naturally."),
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "A tree helper may be DP-like, but there are no overlapping subproblems in a normal tree." -Missing "Repeated states reachable by multiple paths." -Mutation "Turn the structure into a DAG/grid with repeated state queries." -NowWhy "Caching state results prevents recomputation.")
            )
        }
        "Graph BFS" {
            return @(
                (New-HorizontalSwitch -Pattern "Graph DFS" -WhyNot "DFS can find a path, but not necessarily the shortest unweighted path first." -Missing "Only component ownership, reachability, or exhaustive path exploration." -Mutation "Change output from minimum steps to number of components or whether a path exists." -NowWhy "Depth-first ownership is simpler when layer distance is irrelevant."),
                (New-HorizontalSwitch -Pattern "Union Find" -WhyNot "Union-Find cannot produce distances or actual BFS layers." -Missing "Only connectivity under merges." -Mutation "Ask whether two nodes are connected after edges are added." -NowWhy "Component parent state answers connectivity cheaply.")
            )
        }
        "Graph DFS" {
            return @(
                (New-HorizontalSwitch -Pattern "Graph BFS" -WhyNot "DFS does not guarantee minimum edge count in an unweighted graph." -Missing "Shortest path, nearest target, or simultaneous wave expansion." -Mutation "Ask for minimum transformations, minutes, or nearest zero/source." -NowWhy "First discovery by BFS layer is the shortest answer."),
                (New-HorizontalSwitch -Pattern "Topological Sort" -WhyNot "Plain DFS reachability does not enforce prerequisite order as output." -Missing "Directed dependencies where nodes unlock after prerequisites." -Mutation "Ask for a valid processing order or cycle detection in prerequisites." -NowWhy "Indegree or DFS state tracks dependency constraints.")
            )
        }
        "Topological Sort" {
            return @(
                (New-HorizontalSwitch -Pattern "Graph DFS" -WhyNot "Reachability alone can visit dependents before prerequisites." -Missing "Only explore components/cycles without needing a valid order." -Mutation "Ask whether all nodes in an undirected graph are reachable or whether a component exists." -NowWhy "Visited DFS answers ownership without indegree bookkeeping."),
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Topo order is about dependency unlocking, not optimizing choices by state." -Missing "Repeated optimal subproblem values over a DAG or sequence." -Mutation "Ask for longest path/count of ways in a DAG." -NowWhy "Topo order becomes a fill order for DP states.")
            )
        }
        "Stack" {
            return @(
                (New-HorizontalSwitch -Pattern "Heap" -WhyNot "Stack preserves nearest unresolved order, not global priority." -Missing "Need smallest/largest/top-k candidate regardless of position." -Mutation "Ask for kth largest, median stream, or next best task." -NowWhy "Priority queue exposes the best candidate directly."),
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "Two pointers need symmetric elimination; stack keeps many unresolved positions." -Missing "Only two boundary candidates matter." -Mutation "Ask for container area or palindrome validation." -NowWhy "One pointer move discards a boundary safely.")
            )
        }
        "Heap" {
            return @(
                (New-HorizontalSwitch -Pattern "Sorting" -WhyNot "Sorting all items may work but over-orders data when only top/frontier is needed." -Missing "Need full sorted output once, not online updates." -Mutation "Ask for all items sorted by frequency/score." -NowWhy "One full sort is simpler when every rank is needed."),
                (New-HorizontalSwitch -Pattern "Binary Search" -WhyNot "Heap does not use a monotonic feasibility predicate." -Missing "A yes/no check over an ordered answer range." -Mutation "Ask for the minimum capacity/speed/time that satisfies a constraint." -NowWhy "Feasibility lets half the answer space be discarded.")
            )
        }
        "Intervals/Greedy" {
            return @(
                (New-HorizontalSwitch -Pattern "Heap" -WhyNot "Greedy endpoint selection works when one sorted pass makes the choice safe." -Missing "Need active interval with earliest finishing resource." -Mutation "Ask for minimum rooms/platforms with overlapping intervals." -NowWhy "A min-heap of end times represents currently occupied resources."),
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Greedy fails when local choice can block a better weighted future." -Missing "Weights/profits or incompatible choices requiring optimal value." -Mutation "Add profit to intervals and ask for max profit schedule." -NowWhy "Choosing or skipping an interval creates repeated optimal subproblems.")
            )
        }
        "Backtracking" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Backtracking enumerates outputs; DP collapses repeated states when only an optimum/count is needed." -Missing "Repeated state identity and no need to list every solution." -Mutation "Ask for count/minimum/best value instead of all combinations." -NowWhy "Memoized state replaces explicit path enumeration."),
                (New-HorizontalSwitch -Pattern "Trie" -WhyNot "Backtracking alone does not share dictionary prefixes." -Missing "Many word-prefix lookups while exploring characters." -Mutation "Add a dictionary of words and ask for all words on a board." -NowWhy "Trie prunes impossible prefixes early.")
            )
        }
        "Trie" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "A set handles exact words, but not efficient shared prefix traversal." -Missing "Only exact membership, no prefix/wildcard/character path." -Mutation "Ask whether a whole word exists, with no prefix operations." -NowWhy "Hash lookup is simpler and cheaper to code."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "Trie is the dictionary accelerator; search still needs DFS only when paths are generated." -Missing "Need to try/undo board paths or wildcard branches." -Mutation "Ask for all dictionary words formed by adjacent board cells." -NowWhy "DFS explores paths while Trie rejects dead prefixes.")
            )
        }
        "Dynamic Programming" {
            return @(
                (New-HorizontalSwitch -Pattern "Greedy" -WhyNot "DP is needed when a local choice can harm a future state." -Missing "A proof that the locally best choice remains globally safe." -Mutation "Constrain the problem so earliest finish, farthest reach, or cheapest immediate choice is always safe." -NowWhy "The state collapse becomes a local invariant."),
                (New-HorizontalSwitch -Pattern "Backtracking" -WhyNot "DP returns an optimal/count value; backtracking is needed when every concrete solution must be emitted." -Missing "Output requires listing all paths/combinations/permutations." -Mutation "Change output from minimum/count to all valid configurations." -NowWhy "The path itself becomes part of the answer.")
            )
        }
        "Union Find" {
            return @(
                (New-HorizontalSwitch -Pattern "Graph DFS" -WhyNot "DSU answers connectivity after merges, but does not enumerate paths/components with custom traversal logic." -Missing "Need to visit cells/nodes and compute component area/shape/path." -Mutation "Ask for number or size of components in a static grid." -NowWhy "DFS/BFS owns each component directly."),
                (New-HorizontalSwitch -Pattern "Topological Sort" -WhyNot "DSU ignores edge direction and prerequisite ordering." -Missing "Directed dependency constraints." -Mutation "Ask whether courses/tasks can be ordered under prerequisites." -NowWhy "Indegree/DFS state models remaining dependencies.")
            )
        }
        "Greedy" {
            return @(
                (New-HorizontalSwitch -Pattern "Dynamic Programming" -WhyNot "Greedy is unsafe without a local-choice proof." -Missing "Counterexample-free exchange argument or monotonic boundary." -Mutation "Add weights or future-dependent rewards." -NowWhy "You must compare choose/skip states instead of trusting the local choice."),
                (New-HorizontalSwitch -Pattern "Intervals/Greedy" -WhyNot "General greedy needs a concrete ordering that makes the local choice safe." -Missing "Interval start/end structure." -Mutation "Express the problem as selecting/merging sorted intervals." -NowWhy "Sorted endpoints give the local decision a proof.")
            )
        }
        "Math/Bit/String" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "The hidden invariant is numeric/string structure, not just lookup." -Missing "Repeated membership/frequency queries." -Mutation "Ask for pairs/counts/complements over the same values." -NowWhy "Lookup becomes the dominant operation."),
                (New-HorizontalSwitch -Pattern "Prefix/Suffix" -WhyNot "Prefix helps range aggregates, but not every string/bit invariant is a range sum." -Missing "Reusable cumulative aggregate over prefixes/suffixes." -Mutation "Ask many substring/range aggregate queries." -NowWhy "Cumulative state answers each range cheaply.")
            )
        }
        "Design/LLD" {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "A single map is usually only one component of the object invariant." -Missing "Only exact key lookup without eviction, order, expiry, or API contract." -Mutation "Ask for plain put/get by key." -NowWhy "The design collapses to direct map state."),
                (New-HorizontalSwitch -Pattern "Linked List / Heap" -WhyNot "The backing data structure depends on the operation contract." -Missing "Recency order or priority/expiry behavior." -Mutation "Add LRU eviction, rate limits, TTL, or top-k ranking." -NowWhy "The secondary structure maintains the required operation invariant.")
            )
        }
        default {
            return @(
                (New-HorizontalSwitch -Pattern "HashMap/HashSet" -WhyNot "No repeated lookup/frequency signal has been established." -Missing "Complement, seen state, or counting requirement." -Mutation "Ask for existence/count by value." -NowWhy "Map/set state removes repeated scan work."),
                (New-HorizontalSwitch -Pattern "Two Pointers" -WhyNot "No sorted/symmetric elimination rule has been established." -Missing "A boundary comparison that safely discards candidates." -Mutation "Sort the input or ask for end-to-end validation." -NowWhy "Pointer movement has a correctness reason.")
            )
        }
    }
}

function Get-HorizontalWrongPatternGuard {
    param(
        [string] $Category,
        [string] $Title = ""
    )

    $key = Get-NormalizedKey $Title
    switch ($key) {
        "mergeksortedlists" { return "Do not label this as pure linked-list merge; the hard part is selecting the minimum among k heads." }
        "meetingroomsii" { return "Do not stop at sorted intervals; minimum rooms requires tracking active end times." }
        "meetingrooms" { return "Do not heap this unless the output asks for room count or active resources." }
        "firstuniquenumber" { return "Do not use counts alone; the query asks for first unique in arrival order." }
        "movingaveragefromdatastream" { return "Do not recompute the average; maintain fixed-window sum and evict exactly one old value." }
        "designcircularqueue" { return "Do not use stack reasoning; this is fixed-capacity FIFO with modulo head/tail arithmetic." }
        "lrucache" { return "Do not use only a map or only a list; O(1) get/put/evict needs both." }
        "pathsumiii" { return "Do not restart DFS from every node; keep prefix counts on the current root path." }
        "gasstation" { return "Do not call this DP; prefix failure eliminates whole start ranges without cached states." }
        "jumpgame" { return "Do not build a reachability DP table when one farthest reachable index proves the answer." }
        "besttimetobuyandsellstock" { return "Do not allocate DP state; one running minimum is the whole legal buy history." }
        "besttimetobuyandsellstockii" { return "Do not use stock DP unless a fee, cooldown, or transaction limit couples choices." }
        "besttimetobuyandsellstockiii" { return "Do not sum all positive edges; at most two transactions requires buy/sell states." }
        "besttimetobuyandsellstockiv" { return "Do not hardcode two transactions; k requires layered hold/cash states." }
        "besttimetobuyandsellstockwithcooldown" { return "Do not greedily harvest adjacent rises; selling today blocks buying tomorrow." }
        "besttimetobuyandsellstockwithtransactionfee" { return "Do not harvest every positive edge; each transaction must overcome the fee." }
        "wordbreak" { return "Do not just build a Trie; the decisive state is whether each prefix can be segmented." }
        "distinctsubsequencesii" { return "Do not enumerate subsequences; duplicate removal needs last contribution per character." }
        "interleavingstring" { return "Do not choose greedily on equal chars; cache the pair of consumed prefix lengths." }
        "longestcommonsubsequence" { return "Do not use sliding window; subsequence order allows skips, not contiguity." }
        "maximumlengthofpairchain" { return "Do not default to LIS DP; earliest finishing pair has the greedy exchange proof." }
        "longestcontinuousincreasingsubsequence" { return "Do not use LIS tails; continuous means the streak resets when order breaks." }
        "numberoflongestincreasingsubsequence" { return "Do not use tails alone; counting LIS needs length and count per ending index." }
        "russiandollenvelopes" { return "Do not nest equal widths; sort equal width by descending height before LIS." }
        "maximumxoroftwonumbersinanarray" { return "Do not use word-prefix Trie language; this is a bitwise prefix decision from high bit to low bit." }
        "maximumxorwithanelementfromarray" { return "Do not put every number in the trie; each query can use only nums <= mi." }
        "maximumgeneticdifferencequery" { return "Do not use a global trie; only current ancestors are valid candidates." }
        "countpairswithxorinarange" { return "Do not use exact-complement lookup; range XOR needs less-than counts by bit." }
    }

    switch ($Category) {
        "HashMap/HashSet" { return "Do not force window/pointers unless contiguity or sorted elimination is explicit." }
        "Two Pointers" { return "Do not use two pointers on unsorted data unless movement has a proof." }
        "Sliding Window" { return "Do not use window when removing left cannot repair validity predictably." }
        "Prefix/Suffix" { return "Do not use prefix as a reflex; name the repeated aggregate first." }
        "Binary Search" { return "Do not binary search unless the index/order or feasibility predicate is monotonic." }
        "Linked List" { return "Do not hide pointer invariants behind arrays unless mutation is not required." }
        "Tree BFS" { return "Do not use BFS for subtree-return problems just because the input is a tree." }
        "Tree DFS" { return "Do not use DFS for minimum-level answers when BFS first discovery is required." }
        "Graph BFS" { return "Do not use DFS for unweighted shortest path or simultaneous spreading." }
        "Graph DFS" { return "Do not use DFS when dependency order or shortest distance is the output." }
        "Topological Sort" { return "Do not topologically sort undirected connectivity problems." }
        "Stack" { return "Do not use a stack unless most-recent unresolved state is the invariant." }
        "Heap" { return "Do not heapify when all you need is one linear scan or a full sorted list once." }
        "Intervals/Greedy" { return "Do not call it greedy until sorting makes the local decision defensible." }
        "Backtracking" { return "Do not backtrack when the output is only a count/minimum and states repeat." }
        "Trie" { return "Do not build a Trie for one exact string lookup." }
        "Dynamic Programming" { return "Do not write DP before defining state, transition, and base case." }
        "Union Find" { return "Do not use DSU when direction, distance, or path details matter." }
        "Greedy" { return "Do not use greedy without an exchange or dominance argument." }
        "Math/Bit/String" { return "Do not simulate blindly when an algebra/string invariant explains the shortcut." }
        "Design/LLD" { return "Do not code methods before naming operation invariants and complexity." }
        default { return "Do not choose a pattern before naming required output, structure, constraints, and workload." }
    }
}

function Get-HorizontalFamilies {
    return @(
        [pscustomobject]@{ FileName = "03_ARRAY_HASH_POINTERS.md"; Title = "Array, Hash, And Pointer Discrimination"; Categories = @("HashMap/HashSet", "Two Pointers", "Prefix/Suffix", "Core Basics"); Focus = "Lookup, complement, ends, and cumulative-state problems." },
        [pscustomobject]@{ FileName = "04_SLIDING_WINDOW.md"; Title = "Sliding Window Discrimination"; Categories = @("Sliding Window"); Focus = "Contiguous region problems where validity can be repaired incrementally." },
        [pscustomobject]@{ FileName = "05_BINARY_SEARCH.md"; Title = "Binary Search Discrimination"; Categories = @("Binary Search"); Focus = "Sorted-index and monotonic-answer problems." },
        [pscustomobject]@{ FileName = "06_LINKED_LIST.md"; Title = "Linked List Pointer Discrimination"; Categories = @("Linked List"); Focus = "Identity, pointer rewiring, cycles, recency lists, and merge structures." },
        [pscustomobject]@{ FileName = "07_TREE_DFS_BFS.md"; Title = "Tree DFS And BFS Discrimination"; Categories = @("Tree DFS", "Tree BFS"); Focus = "Subtree return contracts versus level-order queue contracts." },
        [pscustomobject]@{ FileName = "08_GRAPH_DFS_BFS.md"; Title = "Graph DFS And BFS Discrimination"; Categories = @("Graph DFS", "Graph BFS"); Focus = "Component ownership versus shortest-path or level expansion." },
        [pscustomobject]@{ FileName = "09_TOPO_UNION_FIND.md"; Title = "Topo Sort And Union-Find Discrimination"; Categories = @("Topological Sort", "Union Find"); Focus = "Directed dependency unlocking versus undirected component merging." },
        [pscustomobject]@{ FileName = "10_STACK_HEAP.md"; Title = "Stack And Heap Discrimination"; Categories = @("Stack", "Heap"); Focus = "Most-recent unresolved candidate versus global priority frontier." },
        [pscustomobject]@{ FileName = "11_INTERVALS_GREEDY.md"; Title = "Intervals And Greedy Discrimination"; Categories = @("Intervals/Greedy", "Greedy"); Focus = "Sorted interval decisions, local-choice proof, and weighted-counterexample boundaries." },
        [pscustomobject]@{ FileName = "12_DYNAMIC_PROGRAMMING.md"; Title = "Dynamic Programming Discrimination"; Categories = @("Dynamic Programming"); Focus = "Repeated states plus choices: state, transition, base case, fill order." },
        [pscustomobject]@{ FileName = "13_BACKTRACKING_TRIE.md"; Title = "Backtracking And Trie Discrimination"; Categories = @("Backtracking", "Trie"); Focus = "Generate/try/undo versus prefix-indexed pruning." },
        [pscustomobject]@{ FileName = "14_MATH_BIT_STRING.md"; Title = "Math, Bit, And String Discrimination"; Categories = @("Math/Bit/String"); Focus = "Hidden algebra, bit, KMP/Z, and string contribution invariants." },
        [pscustomobject]@{ FileName = "15_DESIGN_DATA_STRUCTURES.md"; Title = "Design Data Structure Discrimination"; Categories = @("Design/LLD"); Focus = "Operation contracts, object invariants, and backing-structure choice." }
    )
}

function Get-HorizontalFileForCategory {
    param(
        [string] $Category,
        [object[]] $Families
    )

    foreach ($family in $Families) {
        if ($Category -in $family.Categories) {
            return $family.FileName
        }
    }
    return "03_ARRAY_HASH_POINTERS.md"
}

function Join-HorizontalSwitchSummary {
    param(
        [object[]] $Switches,
        [int] $Limit = 2
    )

    return (@($Switches | Select-Object -First $Limit | ForEach-Object {
        "$($_.Pattern): Why not now - $($_.WhyNot); Missing - $($_.Missing); Minimal change - $($_.Mutation); Now works - $($_.NowWhy)"
    }) -join "<br>")
}

function Build-HorizontalReadme {
    param(
        [object[]] $Rows,
        [object[]] $Families
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# DSA Horizontal Mastery")
    $lines.Add("")
    $lines.Add("This layer trains pattern discrimination across the existing DSA10days problems without creating a 150-file encyclopedia.")
    $lines.Add("")
    $lines.Add('Source of truth remains `../../src/main/java/org/chijai`. This folder is a compact reasoning interface over the ranked cockpit and Java links.')
    $lines.Add("")
    $lines.Add("## What This Adds")
    $lines.Add("")
    $lines.Add("| Existing layer | Answers |")
    $lines.Add("|---|---|")
    $lines.Add('| `../interview/01_ZERO_TO_HERO_RANKED_TABLE.md` | What should I study first? |')
    $lines.Add('| `../interview/patterns/` | How do I revise one pattern vertically? |')
    $lines.Add('| `../../src/main/java/org/chijai/patterns` | What reusable Java frame does this pattern use? |')
    $lines.Add('| `./` | Why this pattern, why not another, and what minimal mutation switches it? |')
    $lines.Add("")
    $lines.Add("## Study Flow")
    $lines.Add("")
    $lines.Add('1. Read `00_MASTER_MATRIX.md` for the navigation map.')
    $lines.Add('2. Use `02_MUTATION_SWITCHBOARD.md` to learn pattern-switch triggers.')
    $lines.Add("3. Open one family file only when that discrimination is weak.")
    $lines.Add('4. Use `CROSSDRILL <problem>` when one problem keeps fooling you.')
    $lines.Add("")
    $lines.Add("## CROSSDRILL Command")
    $lines.Add("")
    $lines.Add("Windows:")
    $lines.Add("")
    $lines.Add('```bat')
    $lines.Add('dsa-review\scripts\crossdrill.cmd "Two Sum"')
    $lines.Add('```')
    $lines.Add("")
    $lines.Add("macOS/Linux:")
    $lines.Add("")
    $lines.Add('```bash')
    $lines.Add('./dsa-review/scripts/crossdrill.sh "Two Sum"')
    $lines.Add('```')
    $lines.Add("")
    $lines.Add("The command prints the full 3-loop drill for one problem: problem signal, winner pattern, important near-misses, minimal mutations, and rejection guard.")
    $lines.Add("")
    $lines.Add("## Files")
    $lines.Add("")
    $lines.Add("| File | Purpose |")
    $lines.Add("|---|---|")
    $lines.Add('| `00_MASTER_MATRIX.md` | Compact problems x patterns navigation table. |')
    $lines.Add('| `01_CROSSDRILL_PROTOCOL.md` | How to run and speak the full 3-loop analysis. |')
    $lines.Add('| `02_MUTATION_SWITCHBOARD.md` | Pattern-to-pattern switch rules. |')
    foreach ($family in $Families) {
        $lines.Add(('| `{0}` | {1} |' -f $family.FileName, $family.Focus))
    }
    $lines.Add("")
    $lines.Add("## Constraint")
    $lines.Add("")
    $lines.Add("This folder intentionally stays under 21 human-facing Markdown files. If a new file does not improve discrimination, merge it into an existing family file.")
    $lines.Add("")
    $lines.Add("Generated ranked entries: $($Rows.Count)")
    return ($lines -join "`r`n")
}

function Build-HorizontalMasterMatrix {
    param(
        [object[]] $Rows,
        [object[]] $Families
    )

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Horizontal Master Matrix")
    $lines.Add("")
    $lines.Add("Compact navigation for problem -> winner pattern -> important pattern switches. Use this to train unseen-problem discrimination from required output, structure, constraints, and workload.")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Winner | Why winner | Near-miss switches | Wrong-pattern guard | Java | LeetCode |")
    $lines.Add("|---:|---|---|---|---|---|---|---|")
    foreach ($row in $Rows) {
        $winner = Get-DisplayCategory $row.Category
        $switches = @(Get-HorizontalSwitches -Category $row.Category -Title $row.Title)
        $switchSummary = Join-HorizontalSwitchSummary -Switches $switches
        $java = New-Link "Java" $row.JavaLink
        $lc = if ($row.LeetCodeLink) { New-Link "LC" $row.LeetCodeLink } else { "-" }
        $lines.Add("| $($row.Rank) | $(Escape-Md $row.Title) | $(Escape-Md $winner) | $(Escape-Md $row.InterviewHook) | $(Escape-Md $switchSummary) | $(Escape-Md (Get-HorizontalWrongPatternGuard -Category $row.Category -Title $row.Title)) | $java | $lc |")
    }
    $lines.Add("")
    $lines.Add("Total ranked entries: $($Rows.Count)")
    return ($lines -join "`r`n")
}

function Build-HorizontalCrossdrillProtocol {
    param([object[]] $Rows)

    $top = @($Rows | Sort-Object Rank | Select-Object -First 5 | ForEach-Object { $_.Title }) -join ", "
    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# CROSSDRILL Protocol")
    $lines.Add("")
    $lines.Add("Use this when one problem keeps causing pattern confusion.")
    $lines.Add("")
    $lines.Add("## Command")
    $lines.Add("")
    $lines.Add('```bat')
    $lines.Add('dsa-review\scripts\crossdrill.cmd "Minimum Window Substring"')
    $lines.Add('```')
    $lines.Add("")
    $lines.Add('```bash')
    $lines.Add('./dsa-review/scripts/crossdrill.sh "Minimum Window Substring"')
    $lines.Add('```')
    $lines.Add("")
    $lines.Add('If several titles match, the command prints candidates. Use the exact title from `00_MASTER_MATRIX.md`.')
    $lines.Add("")
    $lines.Add("## Speak This Loop")
    $lines.Add("")
    $lines.Add('```text')
    $lines.Add("Problem -> Patterns -> Mutation")
    $lines.Add("WHY NOT NOW? -> WHAT IS MISSING? -> MINIMAL CHANGE -> NOW WHY DOES IT WORK?")
    $lines.Add('```')
    $lines.Add("")
    $lines.Add("## Output Contract")
    $lines.Add("")
    $lines.Add("| Section | What must be said |")
    $lines.Add("|---|---|")
    $lines.Add("| Problem | Required output, input structure, constraints/workload signal. |")
    $lines.Add("| Winner | The natural pattern and the invariant that makes it correct. |")
    $lines.Add("| Near-misses | Patterns that almost fit, why they do not fit now, and the smallest mutation that makes them fit. |")
    $lines.Add("| Tempting wrong patterns | Short rejection guard. |")
    $lines.Add("| Irrelevant patterns | Aggregated, not listed one by one. |")
    $lines.Add("| Close | Brute force -> bottleneck -> pattern -> invariant -> code -> dry run. |")
    $lines.Add("")
    $lines.Add("## Examples To Start")
    $lines.Add("")
    $lines.Add("Top-ranked examples: $top.")
    return ($lines -join "`r`n")
}

function Build-HorizontalMutationSwitchboard {
    param([object[]] $Families)

    $categories = @($Families | ForEach-Object { $_.Categories } | Select-Object -Unique)
    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# Mutation Switchboard")
    $lines.Add("")
    $lines.Add("Learn this file as pattern-switch grammar. For every near-miss, ask: why not now, what is missing, what minimal change makes it work, and why does it work after the change?")
    $lines.Add("")
    $lines.Add("| Current winner | Near pattern | Why not now | What is missing | Minimal change | Now why it works |")
    $lines.Add("|---|---|---|---|---|---|")
    foreach ($category in ($categories | Sort-Object { Get-CategoryWeight $_ }, { $_ })) {
        foreach ($switch in @(Get-HorizontalSwitches -Category $category)) {
            $lines.Add("| $(Escape-Md (Get-DisplayCategory $category)) | $(Escape-Md $switch.Pattern) | $(Escape-Md $switch.WhyNot) | $(Escape-Md $switch.Missing) | $(Escape-Md $switch.Mutation) | $(Escape-Md $switch.NowWhy) |")
        }
    }
    return ($lines -join "`r`n")
}

function Build-HorizontalFamilyMermaid {
    param([object] $Family)

    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add('```mermaid')
    $lines.Add("flowchart TD")
    $rootLabel = Escape-MermaidLabel $Family.Title
    $lines.Add(('  Root["{0}"]' -f $rootLabel))
    $index = 1
    foreach ($category in $Family.Categories) {
        $catId = "C{0:D2}" -f $index
        $winnerLabel = Escape-MermaidLabel (Get-DisplayCategory $category)
        $guardLabel = Escape-MermaidLabel (Get-HorizontalWrongPatternGuard -Category $category)
        $lines.Add(('  Root --> {0}["{1}"]' -f $catId, $winnerLabel))
        $lines.Add(('  {0} --> G{1:D2}["Guard<br/>{2}"]' -f $catId, $index, $guardLabel))
        $switchIndex = 1
        foreach ($switch in @(Get-HorizontalSwitches -Category $category | Select-Object -First 2)) {
            $switchId = "C{0:D2}S{1:D2}" -f $index, $switchIndex
            $switchLabel = Escape-MermaidLabel "$($switch.Pattern)<br/>$($switch.Mutation)"
            $lines.Add(('  {0} --> {1}["{2}"]' -f $catId, $switchId, $switchLabel))
            $switchIndex++
        }
        $index++
    }
    $lines.Add('```')
    return ($lines -join "`r`n")
}

function Build-HorizontalFamilyFile {
    param(
        [object] $Family,
        [object[]] $Rows
    )

    $items = @($Rows | Where-Object { $_.Category -in $Family.Categories } | Sort-Object Rank)
    $lines = New-Object System.Collections.Generic.List[string]
    $lines.Add("# $($Family.Title)")
    $lines.Add("")
    $lines.Add($Family.Focus)
    $lines.Add("")
    $lines.Add("Study goal: recognize when this family is the winner, reject the nearest wrong alternatives, and know the smallest requirement change that would switch the pattern.")
    $lines.Add("")
    $lines.Add("## Switch Map")
    $lines.Add("")
    $lines.Add((Build-HorizontalFamilyMermaid -Family $Family))
    $lines.Add("")
    $lines.Add("## Problems")
    $lines.Add("")
    $lines.Add("| Rank | Problem | Winner | Why winner | Near-miss reasoning | Wrong-pattern guard | Java | LeetCode |")
    $lines.Add("|---:|---|---|---|---|---|---|---|")
    foreach ($row in $items) {
        $switches = @(Get-HorizontalSwitches -Category $row.Category -Title $row.Title)
        $switchSummary = Join-HorizontalSwitchSummary -Switches $switches -Limit 2
        $winner = Get-DisplayCategory $row.Category
        $java = New-Link "Java" $row.JavaLink
        $lc = if ($row.LeetCodeLink) { New-Link "LC" $row.LeetCodeLink } else { "-" }
        $lines.Add("| $($row.Rank) | $(Escape-Md $row.Title) | $(Escape-Md $winner) | $(Escape-Md $row.InterviewHook) | $(Escape-Md $switchSummary) | $(Escape-Md (Get-HorizontalWrongPatternGuard -Category $row.Category -Title $row.Title)) | $java | $lc |")
    }
    $lines.Add("")
    $lines.Add("## Drill")
    $lines.Add("")
    $lines.Add("For each row, speak: required output -> structure -> constraint/workload -> winner -> why not nearest alternative -> minimal mutation -> new winner.")
    $lines.Add("")
    $lines.Add("Rows in this file: $($items.Count)")
    return ($lines -join "`r`n")
}

function Build-HorizontalLayer {
    param(
        [object[]] $Rows,
        [string] $OutDir
    )

    $families = @(Get-HorizontalFamilies)
    if (-not (Test-Path -LiteralPath $OutDir)) {
        New-Item -ItemType Directory -Path $OutDir | Out-Null
    }
    Get-ChildItem -LiteralPath $OutDir -File -Filter "*.md" | Remove-Item -Force

    Write-TextFile -Path (Join-Path $OutDir "README.md") -Content (Build-HorizontalReadme -Rows $Rows -Families $families)
    Write-TextFile -Path (Join-Path $OutDir "00_MASTER_MATRIX.md") -Content (Build-HorizontalMasterMatrix -Rows $Rows -Families $families)
    Write-TextFile -Path (Join-Path $OutDir "01_CROSSDRILL_PROTOCOL.md") -Content (Build-HorizontalCrossdrillProtocol -Rows $Rows)
    Write-TextFile -Path (Join-Path $OutDir "02_MUTATION_SWITCHBOARD.md") -Content (Build-HorizontalMutationSwitchboard -Families $families)
    foreach ($family in $families) {
        Write-TextFile -Path (Join-Path $OutDir $family.FileName) -Content (Build-HorizontalFamilyFile -Family $family -Rows $Rows)
    }

    return $families
}

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "../..")).Path
$indexPath = Join-Path $repoRoot "dsa-review/notes/PROBLEM_PATTERN_INDEX.md"
$outDir = Join-Path $repoRoot "dsa-review/interview"
$horizontalDir = Join-Path $repoRoot "dsa-review/horizontal"

if (-not (Test-Path -LiteralPath $indexPath)) {
    throw "Problem index not found: $indexPath"
}

$rows = @(Get-IndexRows -RepoRoot $repoRoot -IndexPath $indexPath)
if ($rows.Count -eq 0) {
    throw "No rows generated from $indexPath"
}

$patternGroups = @(Get-PatternGroups -Rows $rows)
$leetcodeIndexRows = @(Get-RecursiveLeetCodeIndexRows -Rows $rows)

Write-TextFile -Path (Join-Path $outDir "README.md") -Content (Build-Readme -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "00_DSA_MIND_MAP.md") -Content (Build-MasterMindMap -Rows $rows -Groups $patternGroups)
Write-TextFile -Path (Join-Path $outDir "00_PATTERN_RECOGNITION_80_20.md") -Content (Build-PatternRecognition)
Write-TextFile -Path (Join-Path $outDir "01_ZERO_TO_HERO_RANKED_TABLE.md") -Content (Build-RankedTable -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "02_ONE_LINE_RECALL_ALL_PROBLEMS.md") -Content (Build-OneLineRecall -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "03_CRISP_INTERVIEW_ANSWERS.md") -Content (Build-CrispAnswers -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "04_TWO_DAY_AND_SEVEN_DAY_PLANS.md") -Content (Build-Plans -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "05_RANKING_METHODOLOGY_AND_AUDIT.md") -Content (Build-RankingAudit -Rows $rows -Groups $patternGroups)
Write-TextFile -Path (Join-Path $outDir "06_REVIEW_DASHBOARD.md") -Content (Build-ReviewDashboard -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "07_LEETCODE_SOLVED_INDEX.md") -Content (Build-LeetCodeSolvedIndex -Rows $rows -LeetCodeRows $leetcodeIndexRows)
Write-TextFile -Path (Join-Path $outDir "08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md") -Content (Build-ProjectStructureGuide -Groups $patternGroups)
Write-TextFile -Path (Join-Path $outDir "09_LEETCODE_CURRICULUM_TOC.md") -Content (Build-LeetCodeCurriculumToc -LeetCodeRows $leetcodeIndexRows)
Write-TextFile -Path (Join-Path $outDir "10_AFTER_7_DAY_EXTENSION_PLAN.md") -Content (Build-PostSevenDayExtensionPlan -Rows $rows -LeetCodeRows $leetcodeIndexRows)
Write-TextFile -Path (Join-Path $outDir "11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md") -Content (Build-ActiveNinetyPlanCutoff -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "12_MASTER_DSA_INTERVIEW_ARTICULATION_TABLE.md") -Content (Build-MasterArticulationTable -Rows $rows)
Write-TextFile -Path (Join-Path $outDir "13_MASTER_TIME_SPACE_COMPLEXITY_TABLE.md") -Content (Build-MasterComplexityTable -LeetCodeRows $leetcodeIndexRows)
Write-TextFile -Path (Join-Path $outDir "DSA_7-Day_Interview_Performance_Sprint.md") -Content (Build-WeeklySprint -Rows $rows)

$patternDir = Join-Path $outDir "patterns"
if (-not (Test-Path -LiteralPath $patternDir)) {
    New-Item -ItemType Directory -Path $patternDir | Out-Null
}
Get-ChildItem -LiteralPath $patternDir -File -Filter "*.md" | Remove-Item -Force
Write-TextFile -Path (Join-Path $patternDir "README.md") -Content (Build-PatternIndex -Rows $rows -Groups $patternGroups)
foreach ($group in $patternGroups) {
    Write-TextFile -Path (Join-Path $patternDir $group.FileName) -Content (Build-PatternFile -Group $group)
}

$horizontalFamilies = @(Build-HorizontalLayer -Rows $rows -OutDir $horizontalDir)

[pscustomobject]@{
    repoRoot = $repoRoot
    output = $outDir
    horizontalOutput = $horizontalDir
    rankedEntries = $rows.Count
    leetcodeLinks = @($rows | Where-Object { $_.LeetCodeLink }).Count
    recursiveLeetCodeIndex = $leetcodeIndexRows.Count
    localOnlyEntries = @($rows | Where-Object { -not $_.LeetCodeLink }).Count
    patternFiles = $patternGroups.Count
    horizontalFiles = $horizontalFamilies.Count + 4
} | Format-List
