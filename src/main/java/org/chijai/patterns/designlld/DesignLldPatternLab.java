package org.chijai.patterns.designlld;

import org.chijai.patterns.PatternChapter;

import java.util.HashMap;
import java.util.Map;

public final class DesignLldPatternLab {
    private DesignLldPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Design Data Structures",
                "Operations + Invariants",
                "State Per Operation",
                "API Contract First",
                "Encode And Decode TinyURL"
        );
    }

    public static final class TinyUrlCodec {
        private final Map<String, String> longByShort = new HashMap<>();
        private int nextId = 1;

        public String encode(String longUrl) {
            String key = "u" + nextId++;
            longByShort.put(key, longUrl);
            return key;
        }

        public String decode(String shortUrl) {
            return longByShort.get(shortUrl);
        }
    }

    public static final class TokenBucket {
        private final int capacity;
        private final int refillPerTick;
        private int tokens;
        private long lastTick;

        public TokenBucket(int capacity, int refillPerTick) {
            this.capacity = capacity;
            this.refillPerTick = refillPerTick;
            this.tokens = capacity;
        }

        public boolean allow(long nowTick) {
            refill(nowTick);
            if (tokens == 0) {
                return false;
            }
            tokens--;
            return true;
        }

        private void refill(long nowTick) {
            long elapsed = Math.max(0, nowTick - lastTick);
            tokens = (int) Math.min(capacity, tokens + elapsed * refillPerTick);
            lastTick = Math.max(lastTick, nowTick);
        }
    }
}
