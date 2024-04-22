package datastructures;

import java.util.BitSet;

/**
 * BloomFilter is a probabilistic data structure used to test whether an element
 * is a member of a set. It may return false positives, but never false negatives.
 */
public class BloomFilter {

    private final BitSet bitSet;
    private final int numHashFunctions;
    private final int bitSetSize;
    private final int seed;

    /**
     * Initializes a new BloomFilter.
     *
     * @param expectedElements   The expected number of elements to be inserted into the filter.
     * @param falsePositiveRate The desired false positive rate (probability of false positives).
     * @param seed               The seed for hash functions to ensure different hash values.
     */
    public BloomFilter(int expectedElements, double falsePositiveRate, int seed) {
        this.seed = seed;
        this.bitSetSize = calculateBitSetSize(expectedElements, falsePositiveRate);
        this.numHashFunctions = calculateNumHashFunctions(expectedElements, bitSetSize);
        this.bitSet = new BitSet(bitSetSize);
    }

    /**
     * Adds a key to the BloomFilter.
     *
     * @param key The key to be added to the filter.
     */
    public void add(String key) {
        for (int i = 0; i < numHashFunctions; i++) {
            int hash = MurmurHash.murmur3Hash32(key, seed + i);
            int index = Math.abs(hash % bitSetSize);
            bitSet.set(index);
        }
    }

    /**
     * Checks whether a key might be a member of the BloomFilter.
     *
     * @param key The key to be checked.
     * @return True if the key might be in the filter (may have false positives), false otherwise.
     */
    public boolean mightContain(String key) {
        for (int i = 0; i < numHashFunctions; i++) {
            int hash = MurmurHash.murmur3Hash32(key, seed + i);
            int index = Math.abs(hash % bitSetSize);
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the size of the BitSet based on the expected number of elements
     * and the desired false positive rate.
     *
     * @param expectedElements   The expected number of elements.
     * @param falsePositiveRate The desired false positive rate.
     * @return The calculated size of the BitSet.
     */
    private static int calculateBitSetSize(int expectedElements, double falsePositiveRate) {
        return (int) Math.ceil(-(expectedElements * Math.log(falsePositiveRate)) / Math.pow(Math.log(2), 2));
    }

    /**
     * Calculates the number of hash functions based on the size of the BitSet
     * and the expected number of elements.
     *
     * @param expectedElements The expected number of elements.
     * @param bitSetSize       The size of the BitSet.
     * @return The calculated number of hash functions.
     */
    private static int calculateNumHashFunctions(int expectedElements, int bitSetSize) {
        return Math.max(1, (int) Math.round(((double) bitSetSize / expectedElements) * Math.log(2)));
    }
}

