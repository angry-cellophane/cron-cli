package com.github.ka.cron.cli

class BitUtils {
    static long setBitRange(int start, int end) {
        long result = 0
        for (int i = start; i <= end; i++) {
            result |= (1L << i)
        }
        return result
    }

    static long setBits(int ... bits) {
        long result = 0
        for (int b : bits) {
            result |= (1L << b)
        }
        return result
    }
}
