package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    lazy val s1 = singletonSet(1)
    lazy val s2 = singletonSet(2)
    lazy val s3 = singletonSet(3)
    lazy val s4 = singletonSet(1)
    lazy val divisibleBy6: FunSet = (x: Int) => x % 6 == 0
    lazy val divisibleBy4: FunSet = (x: Int) => x % 4 == 0
    lazy val divisibleBy2: FunSet = (x: Int) => x % 2 == 0

  test("singleton set only one contains one") {
    new TestSets:
      assert(contains(s1, 1))
      assert(!contains(s1, 2))
      assert(!contains(s1, 3))
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(s, 1))
      assert(contains(s, 2))
      assert(!contains(s, 3))
  }

  test("intersects disjoint sets") {
    new TestSets:
      val s = intersect(s1, s2)
      assert(!contains(s, 1))
      assert(!contains(s, 2))
  }

  test("intersects joint sets") {
    new TestSets:
      val s = intersect(s1, s4)
      assert(contains(s, 1))
      assert(!contains(s, 2))
  }

  test("diff disjoint sets") {
    new TestSets:
      val s = diff(s1, s2)
      assert(contains(s, 1))
  }

  test("diff joint sets") {
    new TestSets:
      val s = diff(s1, s4)
      assert(!contains(s, 1))
  }

  test("filters sets") {
    new TestSets:
      var isEven = (x: Int) => x % 2 == 0
      var odd = filter(s1, isEven)
      var even = filter(s2, isEven)
      assert(!contains(odd, 1))
      assert(contains(even, 2))
  }

  test("forall four multiples checks two divisibility") {
    new TestSets:
      assert(forall(divisibleBy4, divisibleBy2))
  }

  test("forall two multiples checks six divisibility") {
    new TestSets:
      assert(!forall(divisibleBy2, divisibleBy6))
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
