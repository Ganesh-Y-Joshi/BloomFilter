
/**
 * This class provides a simple implementation of the Murmur3 hash function.
 */
public class MurmurHash {

    /**
     * Computes the Murmur3 32-bit hash for the given key and seed.
     *
     * @param key  The input key as a string.
     * @param seed The seed value for the hash function.
     * @return The computed 32-bit hash value.
     */
    public static int murmur3Hash32(String key, int seed) {
        byte[] bytes = key.getBytes();
        return murmur3Hash32(bytes, seed);
    }

    /**
     * Computes the Murmur3 32-bit hash for the given byte array and seed.
     *
     * @param data The input data as a byte array.
     * @param seed The seed value for the hash function.
     * @return The computed 32-bit hash value.
     */
    public static int murmur3Hash32(byte[] data, int seed) {
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;
        final int r1 = 15;
        final int r2 = 13;
        final int m = 5;
        final int n = 0xe6546b64;

        int hash = seed;

        int length = data.length;
        int roundedEnd = (length & 0xFFFFFFFC); // Round down to 4-byte block

        // Process 4-byte blocks
        for (int i = 0; i < roundedEnd; i += 4) {
            int k = data[i] & 0xFF | (data[i + 1] & 0xFF) << 8 | (data[i + 2] & 0xFF) << 16 | data[i + 3] << 24;

            k *= c1;
            k = Integer.rotateLeft(k, r1);
            k *= c2;

            hash ^= k;
            hash = Integer.rotateLeft(hash, r2);
            hash = hash * m + n;
        }

        // Process the remaining bytes
        int k1 = 0;
        switch (length & 0x03) {
            case 3:
                k1 ^= (data[roundedEnd + 2] & 0xff) << 16;
            case 2:
                k1 ^= (data[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k1 ^= (data[roundedEnd] & 0xff);

                k1 *= c1;
                k1 = Integer.rotateLeft(k1, r1);
                k1 *= c2;
                hash ^= k1;
        }

        // Finalization
        hash ^= length;
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);

        return hash;
    }
}
