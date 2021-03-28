package datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A hash table that uses extendible hashing.
 * 
 * Null values are not allowed.
 * 
 * Resources: https://www2.cs.sfu.ca/CourseCentral/354/lxwu/notes/chapter11.pdf
 * https://www.geeksforgeeks.org/extendible-hashing-dynamic-approach-to-dbms/
 * 
 * This is primarily useful for using as an index in a database.
 * 
 * @author Ethan
 * @version 1.0
 * @since 2021-03-27
 *
 * @param <K> key type
 * @param <V> value type
 */
public class ExtendibleHashTable<K, V> implements Map<K, V> {
	// global depth can't exceed more than 32 bits due to an int hash
	public static final int DEFAULT_BUCKET_SIZE = 4;

	private static final int MINIMUM_DEPTH = 1;
	private static final int NOT_FOUND = -1;
	private byte globalDepth;
	private int bucketSize;
	private Object[] directory;
	private int size;
	private Function<Object, Integer> hasher;

	private class Bucket {
		byte localDepth;
		int nKeys; // Number of current keys
		K[] keys;
		V[] values;

		@SuppressWarnings("unchecked")
		public Bucket(byte depth) {
			localDepth = depth;
			keys = (K[]) new Object[bucketSize];
			values = (V[]) new Object[bucketSize];
		}

		int indexOf(Object o, Object[] objects) {
			int index = NOT_FOUND;

			for (int i = 0; i < nKeys; i++) {
				if (objects[i].equals(o)) {
					return i;
				}
			}

			return index;
		}

		public void decrementDepth() {
			if (localDepth > MINIMUM_DEPTH) {
				localDepth--;
			}
		}

		public boolean contains(Object o, Object[] objects) {
			return indexOf(o, objects) != NOT_FOUND;
		}

		boolean insert(K key, V value) {
			if (isFull()) {
				return false;
			}

			keys[nKeys] = key;
			values[nKeys] = value;
			nKeys++;
			return true;
		}

		public V put(K key, V value, int index) {
			V oldValue = values[index];
			values[index] = value;
			keys[index] = key;
			return oldValue;
		}

		public V remove(Object key) {
			int index = indexOf(key, keys);

			if (index == NOT_FOUND) {
				return null;
			}

			V oldValue = values[index];
			System.arraycopy(values, index + 1, values, index, nKeys - index - 1); // Cleaning up memory
			System.arraycopy(keys, index + 1, keys, index, nKeys - index - 1);
			nKeys--;
			keys[nKeys] = null;
			values[nKeys] = null;
			size--;
			return oldValue;
		}

		public boolean isFull() {
			return nKeys == bucketSize;
		}

		public boolean isEmpty() {
			return nKeys == 0;
		}

		public Bucket split(int hash) {
			localDepth++;
			nKeys = 0;
			Bucket otherBucket = new Bucket(localDepth);

			// There are cases where both buckets will have the same leading character.
			String input = getLeastSigBitsBinaryStr(hash, localDepth);
			boolean isZeroLeading = input.charAt(0) == '0';
			if (isZeroLeading) {
				for (int i = 0; i < keys.length; i++) {
					String binaryForm = getLeastSigBitsBinaryStr(hasher.apply(keys[i]), localDepth);
					if (binaryForm.charAt(0) == '0') {
						this.insert(keys[i], values[i]);
					} else {
						otherBucket.insert(keys[i], values[i]);
					}
				}
			} else {
				for (int i = 0; i < keys.length; i++) {
					String binaryForm = getLeastSigBitsBinaryStr(hasher.apply(keys[i]), localDepth);
					if (binaryForm.charAt(0) == '1') {
						this.insert(keys[i], values[i]);
					} else {
						otherBucket.insert(keys[i], values[i]);
					}
				}
			}

			Arrays.fill(keys, nKeys, keys.length, null); // Cleaning up memory
			Arrays.fill(values, nKeys, values.length, null);
			return otherBucket;
		}

		public void borrow(Bucket other) {
			nKeys = other.nKeys;
			System.arraycopy(other.keys, 0, keys, 0, nKeys);
			System.arraycopy(other.values, 0, values, 0, nKeys);
			decrementDepth();
		}

		public String toString() {
			return String.format("Bucket, depth=%d, total keys=%d, keys=%s, values=%s", localDepth, nKeys,
					Arrays.toString(keys), Arrays.toString(values));
		}
	}

	// Default hash function from
	// https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/java/util/HashMap.java
	final static Function<Object, Integer> DEFAULT_HASHER = key -> {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	};

	public ExtendibleHashTable() {
		this(DEFAULT_BUCKET_SIZE);
	}

	public ExtendibleHashTable(int bucketSize) {
		this(bucketSize, DEFAULT_HASHER);
	}

	public ExtendibleHashTable(int bucketSize, Function<Object, Integer> hashFunction) {
		Objects.requireNonNull(hashFunction);
		if (bucketSize < 0) {
			throw new IllegalArgumentException("Bucket size must be greater than 0");
		}

		this.hasher = hashFunction;
		this.bucketSize = bucketSize;
		globalDepth = MINIMUM_DEPTH;
		directory = new Object[totalPotentialBuckets()];
		for (int i = 0; i < directory.length; i++) {
			directory[i] = new Bucket(globalDepth);
		}
	}

	private int totalPotentialBuckets() {
		// Working with powers of 2
		return 1 << globalDepth;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	private Bucket getBucket(int hash) {
		int directoryIndex = getLeastSigBitsValue(hash, globalDepth);
		Bucket bucket = (Bucket) directory[directoryIndex];
		return bucket;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified key.
	 *
	 * @param key The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified key.
	 */
	@Override
	public boolean containsKey(Object key) {
		Objects.requireNonNull(key);
		Bucket keyBucket = getBucket(hasher.apply(key));
		return keyBucket.contains(key, keyBucket.keys);
	}

	/**
	 * Returns <tt>true</tt> if the hashtable contains a mapping for the specified
	 * value. This is a very computationally expensive operation.
	 * 
	 * @param The value whose presence in this hashtable to be tested.
	 * @return <tt>true</tt> if the hashtable contains a mapping for the specified
	 *         value.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsValue(Object value) {
		Objects.requireNonNull(value);

		Set<Bucket> seenBuckets = new HashSet<>();
		for (Object bucket : directory) {
			Bucket b = (Bucket) bucket;
			if (seenBuckets.add(b)) {
				if (b.contains(value, b.values)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the associated value that the key maps to.
	 * 
	 * @param The key to retrieve the associated value.
	 * @return The value associated with the key. Returns null if there is no
	 *         associated value.
	 */
	@Override
	public V get(Object key) {
		Objects.requireNonNull(key);
		Bucket keyBucket = getBucket(hasher.apply(key));
		int index = keyBucket.indexOf(key, keyBucket.keys);
		return index != NOT_FOUND ? keyBucket.values[index] : null;
	}

	/**
	 * Returns the previous associated value that was overrided by the new value
	 * given the key.
	 *
	 * @param The key create or override an association.
	 * @param The value override and store.
	 * @return The previous value that was overrided.
	 */
	@Override
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		return putHelper(key, value);
	}

	private V putHelper(K key, V value) {
		int hash = hasher.apply(key);
		Bucket keyBucket = getBucket(hash);

		int index = keyBucket.indexOf(key, keyBucket.keys);
		if (index != NOT_FOUND) {
			return keyBucket.put(key, value, index);
		} else if (keyBucket.insert(key, value)) {
			size++;
			return null;
		}

		if (keyBucket.localDepth == globalDepth) {
			growDirectory();
		}

		// split buckets
		Bucket splitBucket = keyBucket.split(hash);
		List<Integer> samePointingDirectories = getAllDirectoryIndicesPointingTo(keyBucket);

		int bitsToMatch = getLeastSigBitsValue(hash, keyBucket.localDepth);
		List<Integer> splitDirectoriesIndices = samePointingDirectories.stream()
				.filter(directoryBin -> getLeastSigBitsValue(directoryBin, keyBucket.localDepth) != bitsToMatch)
				.collect(Collectors.toList());

		for (int directoryIndex : splitDirectoriesIndices) {
			directory[directoryIndex] = splitBucket;
		}

		// recursively try to reinsert in case of numerous splits
		return putHelper(key, value);
	}

	private List<Integer> getAllDirectoryIndicesPointingTo(Bucket bucket) {
		List<Integer> directoryIndices = new LinkedList<>();
		for (int i = 0; i < directory.length; i++) {
			if (directory[i] == bucket) {
				directoryIndices.add(i);
			}
		}
		return directoryIndices;
	}

	private void growDirectory() {
		globalDepth++;
		Object[] newDirectory = new Object[totalPotentialBuckets()];
		int halfDirectoryPosition = totalPotentialBuckets() / 2;
		for (int i = 0; i < directory.length; i++) {
			newDirectory[i] = directory[i];
			newDirectory[i + halfDirectoryPosition] = directory[i];
		}
		directory = newDirectory;
	}

	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		Objects.requireNonNull(key);

		int hash = hasher.apply(key);
		Bucket keyBucket = getBucket(hash);

		V value = keyBucket.remove(key);

		if (keyBucket.isEmpty() && globalDepth > MINIMUM_DEPTH) {
			int keyBucketDirectory = getLeastSigBitsValue(hash, globalDepth);
			int upperHalfBucketDirectory = totalPotentialBuckets() / 2;

			if (keyBucketDirectory < upperHalfBucketDirectory) {
				Bucket upperHalfSiblingBucket = (Bucket) this.directory[upperHalfBucketDirectory + keyBucketDirectory];

				keyBucket.borrow(upperHalfSiblingBucket);
				updateDirectoryPointers(upperHalfSiblingBucket, keyBucket);
			}

			while (canCollapseDirectory()) {
				collapseDirectory();
			}

			if (globalDepth == MINIMUM_DEPTH) {
				// Weird special case that isn't handled when collapsing the directory nor
				// borrowing
				for (int i = 0; i < this.totalPotentialBuckets(); i++) {
					Bucket b = (Bucket) directory[i];
					b.localDepth = 1;
				}
			}
		}

		return value;
	}

	private void updateDirectoryPointers(Bucket oldBucket, Bucket newBucket) {
		for (int i : getAllDirectoryIndicesPointingTo(oldBucket)) {
			this.directory[i] = newBucket;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean canCollapseDirectory() {
		if (globalDepth == MINIMUM_DEPTH) {
			return false;
		}

		final int fullBuckets = totalPotentialBuckets();
		for (int i = fullBuckets / 2; i < fullBuckets; i++) {
			Bucket b = (Bucket) directory[i];
			if (!b.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private void collapseDirectory() {
		globalDepth--;
		Object[] halfDirectory = new Object[totalPotentialBuckets()];
		System.arraycopy(directory, 0, halfDirectory, 0, totalPotentialBuckets());
		directory = halfDirectory;
	}

	/**
	 * Expects non null map with non null entry values.
	 */
	public void putAll(Map<? extends K, ? extends V> map) {
		Objects.requireNonNull(map);
		map.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
	}

	/**
	 * All contents are deleted.
	 */
	public void clear() {
		size = 0;
		globalDepth = 1;
		directory = new Object[totalPotentialBuckets()];
		for (int i = 0; i < directory.length; i++) {
			directory[i] = new Bucket(globalDepth);
		}
	}

	/**
	 * The unique non-null keys found in the current hash table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keySet() {
		Set<K> keys = new HashSet<>();

		for (Object bucket : directory) {
			Bucket b = (Bucket) bucket;
			for (int i = 0; i < b.nKeys; i++) {
				keys.add(b.keys[i]);
			}
		}

		return keys;
	}

	/**
	 * The associated value of each key. Output will include duplicates.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Collection<V> values = new ArrayList<>();

		for (Object bucket : directory) {
			Bucket b = (Bucket) bucket;
			for (int i = 0; i < b.nKeys; i++) {
				values.add(b.values[i]);
			}
		}

		return values;
	}

	/**
	 * The entryset similar to what you would get from a map key set.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> entries = new HashSet<>();

		for (Object bucket : directory) {
			Bucket b = (Bucket) bucket;
			for (int i = 0; i < b.nKeys; i++) {
				entries.add(new Pair<>(b.keys[i], b.values[i]));
			}
		}

		return entries;
	}

	/**
	 * Builds the binary string representation of the given value for n bits from
	 * left to right by masking each position for the specific bit at that position.
	 * 
	 * @param value value The value you want to get your bits from
	 * @param nBits nBits the amount of bits you want. Max of 32 bits.
	 * @return The least significant n bits as string
	 */
	private static String getLeastSigBitsBinaryStr(int value, int nBits) {
		// Get the least significant n bits, ex: value = 122, nBits = 4 => ...1111010 ->
		// 1010
		StringBuilder binarySB = new StringBuilder(nBits);
		nBits = Math.min(nBits, 32); // int is only 32 bits at most
		for (int i = nBits - 1; i >= 0; i--) {
			int mask = 1 << i;
			char bit = (value & mask) == 0 ? '0' : '1';
			binarySB.append(bit);
		}
		return binarySB.toString();
	}

	/**
	 * Convert the given value into X least significant bits as an int value.
	 * 
	 * @param value The value you want to get your bits from
	 * @param nBits the amount of bits you want. Max of 32 bits.
	 * @return the least significant n bits as an int
	 */
	private static int getLeastSigBitsValue(int value, int nBits) {
		return Integer.parseInt(getLeastSigBitsBinaryStr(value, nBits), 2);
	}

	private void printAllUniqueBuckets() {
		Set<Object> set = new HashSet<>();

		for (int i = 0; i < this.directory.length; i++) {
			if (set.add(this.directory[i])) {
				System.out.println(i + "\t" + directory[i]);
			}

		}
	}
}