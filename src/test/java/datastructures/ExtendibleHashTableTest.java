package datastructures;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ExtendibleHashTableTest {
	public static class SamePair extends Pair<Integer, String> {
		public SamePair(Integer key) {
			super(key, key + "");
		}

		public SamePair(Integer key, String value) {
			super(key, value);
		}
	}

	public ExtendibleHashTable<Integer, String> hashtable;

	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class ManySplitsTester {
		public Stream<Arguments> manySplitsPair() {
			return Stream.of(Arguments.of(new SamePair(2)), Arguments.of(new SamePair(6)),
					Arguments.of(new SamePair(10)), Arguments.of(new SamePair(114)), Arguments.of(new SamePair(242)),
					Arguments.of(new SamePair(498)), Arguments.of(new SamePair(1010)), Arguments.of(new SamePair(2034)),
					Arguments.of(new SamePair(4082)), Arguments.of(new SamePair(8178)),
					Arguments.of(new SamePair(16370)), Arguments.of(new SamePair(32754)));
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(1)
		public void insertTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.put(input.getKey(), input.getValue()), null);
		}

		@Test
		@Order(2)
		public void nonEmptySizeTest() {
			Assertions.assertEquals(hashtable.size(), manySplitsPair().count());
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(3)
		public void containsKeyTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(4)
		public void containsValueTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(5)
		public void removeTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.remove(input.getKey()), input.getValue());
		}

		@Test
		@Order(6)
		public void emptySizeTest() {
			Assertions.assertEquals(hashtable.size(), 0);
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(7)
		public void insertAfterAllRemovedTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.put(input.getKey(), input.getValue()), null);
		}

		@Test
		@Order(8)
		public void nonEmptySizeAgainTest() {
			Assertions.assertEquals(hashtable.size(), manySplitsPair().count());
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(9)
		public void containsKeyAgainTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(10)
		public void containsValueAgainTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("manySplitsPair")
		@Order(11)
		public void removeAgainTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.remove(input.getKey()), input.getValue());
		}

		@Test
		@Order(12)
		public void emptySizeAgainTest() {
			Assertions.assertEquals(hashtable.size(), 0);
		}

		@AfterAll
		public void cleanUp() {
			hashtable = null;
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class ManySplitsSizeBucketOneTester extends ManySplitsTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(1, obj -> obj.hashCode());
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class ManySplitsSizeTwoBucketTester extends ManySplitsTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(2, obj -> obj.hashCode());
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class ManySplitsSizeFourBucketTester extends ManySplitsTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(4, obj -> obj.hashCode());
		}
	}

	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class LargeCaseTester {

		public Stream<Arguments> negativePairs() {
			return IntStream.range(-1500, 0).mapToObj(i -> Arguments.of(new SamePair(i)));
		}

		public Stream<Arguments> positivePairs() {
			return IntStream.range(1, 1500).mapToObj(i -> Arguments.of(new SamePair(i)));
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(1)
		public void insertTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.put(input.getKey(), input.getValue()), null);
		}

		@Test
		@Order(2)
		public void nonEmptySizeTest() {
			Assertions.assertEquals(hashtable.size(), positivePairs().count());
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(3)
		public void containsKeyTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(4)
		public void doesNotContainsKeyTest(Pair<Integer, String> input) {
			Assertions.assertFalse(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(5)
		public void containsValueTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(6)
		public void doesNotContainsValueTest(Pair<Integer, String> input) {
			Assertions.assertFalse(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(7)
		public void removeTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.remove(input.getKey()), input.getValue());
		}

		@Test
		@Order(8)
		public void emptySizeTest() {
			Assertions.assertEquals(hashtable.size(), 0);
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(9)
		public void insertAfterAllRemovedTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.put(input.getKey(), input.getValue()), null);
		}

		@Test
		@Order(10)
		public void nonEmptySizeAgainTest() {
			Assertions.assertEquals(hashtable.size(), negativePairs().count());
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(11)
		public void containsKeyAgainTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(12)
		public void doesNotContainsKeyAgainTest(Pair<Integer, String> input) {
			Assertions.assertFalse(hashtable.containsKey(input.getKey()));
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(13)
		public void containsValueAgainTest(Pair<Integer, String> input) {
			Assertions.assertTrue(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("positivePairs")
		@Order(14)
		public void doesNotContainsValueAgainTest(Pair<Integer, String> input) {
			Assertions.assertFalse(hashtable.containsValue(input.getValue()));
		}

		@ParameterizedTest
		@MethodSource("negativePairs")
		@Order(15)
		public void removeAgainTest(Pair<Integer, String> input) {
			Assertions.assertEquals(hashtable.remove(input.getKey()), input.getValue());
		}

		@Test
		@Order(16)
		public void emptySizeAgainTest() {
			Assertions.assertEquals(hashtable.size(), 0);
		}

		@AfterAll
		public void cleanUp() {
			hashtable = null;
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class NormalSizeOneBucketTester extends LargeCaseTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(1, obj -> obj.hashCode());
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class NormalSizeTwoBucketTester extends LargeCaseTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(2, obj -> obj.hashCode());
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@TestMethodOrder(OrderAnnotation.class)
	public class NormalSizeFourBucketTester extends LargeCaseTester {
		@BeforeAll
		public void initAll() {
			hashtable = new ExtendibleHashTable<>(4, obj -> obj.hashCode());
		}
	}

}
