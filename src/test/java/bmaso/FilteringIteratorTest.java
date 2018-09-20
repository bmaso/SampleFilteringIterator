package bmaso;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.Vector;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class FilteringIteratorTest {

  @Test
  void emptyUnderlyingIterator() {
    Iterator<String> i = new Vector<String>().iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new NeverFailingObjectTest<String>());
    List<String> actualList = Lists.newArrayList(fis);

    assertEquals(0, actualList.size(), () -> "List is not empty");
  }

  @Test
  void nonEmptyUnderlyingIterator() {
    Iterator<String> i = Lists.<String>newArrayList("a", "b", "c").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new NeverFailingObjectTest<String>());
    List<String> actualList = Lists.newArrayList(fis);

    assertEquals(3, actualList.size(), () -> "List is not empty");
  }

  @Test
  void objectTestRejectsAll() {
    Iterator<String> i = Lists.<String>newArrayList("a", "b", "c").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new CompletelyRejectingObjectTest<String>());
    List<String> actualList = Lists.newArrayList(fis);

    assertEquals(0, actualList.size(), () -> "List is not empty");
  }

  @Test
  void objectTestRejectsCompletelyFilteredIterator() {
    Iterator<String> i = Lists.<String>newArrayList("ABC", "DEF", "GHI").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new RejectCapitalizedObjectTest());
    List<String> actualList = Lists.newArrayList(fis);

    assertEquals(0, actualList.size(), () -> "List is not empty");
  }

  @Test
  void objectTestRejectsFirstIteratorElement() {
    Iterator<String> i = Lists.<String>newArrayList("ABC", "def", "ghi").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new RejectCapitalizedObjectTest());
    List<String> actualList = Lists.newArrayList(fis);

    List<String> expectedList = Lists.<String>newArrayList("def", "ghi");
    assertEquals(expectedList, actualList);
  }

  @Test
  void objectTestRejectsLastIteratorElement() {
    Iterator<String> i = Lists.<String>newArrayList("abc", "def", "GHI").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new RejectCapitalizedObjectTest());
    List<String> actualList = Lists.newArrayList(fis);

    List<String> expectedList = Lists.<String>newArrayList("abc", "def");
    assertEquals(expectedList, actualList);
  }

  @Test
  void consumerReceivesAllWhenNoneRejected() {
    Iterator<String> i = Lists.<String>newArrayList("a", "b", "c").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new NeverFailingObjectTest<String>());

    Vector<String> consumerReceives = new Vector<String>();
    fis.forEachRemaining((s) -> consumerReceives.add(s));

    List<String> expected = Lists.<String>newArrayList("a", "b", "c");
    assertEquals(expected, consumerReceives);
  }

  @Test
  void consumerReceivesNoneWhenAllRejected() {
    Iterator<String> i = Lists.<String>newArrayList("a", "b", "c").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new CompletelyRejectingObjectTest<String>());

    Vector<String> consumerReceives = new Vector<String>();
    fis.forEachRemaining((s) -> consumerReceives.add(s));

    List<String> expected = Lists.<String>newArrayList();
    assertEquals(expected, consumerReceives);
  }

  @Test
  void consumerReceivesSomeWhenOthersRejected() {
    Iterator<String> i = Lists.<String>newArrayList("abc", "DEF", "ghi", "JKL").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new RejectCapitalizedObjectTest());

    Vector<String> consumerReceives = new Vector<String>();
    fis.forEachRemaining((s) -> consumerReceives.add(s));

    List<String> expected = Lists.<String>newArrayList("abc", "ghi");
    assertEquals(expected, consumerReceives);
  }

  @Test
  void consumerReceivesRestWhenSomeIterated() {
    Iterator<String> i = Lists.<String>newArrayList("a", "b", "c", "d").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new NeverFailingObjectTest<String>());

    assertTrue(fis.hasNext());
    assertEquals("a", fis.next());
    assertTrue(fis.hasNext());
    assertEquals("b", fis.next());

    Vector<String> consumerReceives = new Vector<String>();
    fis.forEachRemaining((s) -> consumerReceives.add(s));

    List<String> expected = Lists.<String>newArrayList("c", "d");
    assertEquals(expected, consumerReceives);
  }

  @Test
  void consumerReceivesRestWhenSomeIteratedAndSomeFiltered() {
    Iterator<String> i = Lists.<String>newArrayList("abc", "DEF", "ghi", "JKL", "mno").iterator();
    FilteringIterator<String> fis = new FilteringIterator<String>(i, new RejectCapitalizedObjectTest());

    assertTrue(fis.hasNext());
    assertEquals("abc", fis.next());
    assertTrue(fis.hasNext());
    assertEquals("ghi", fis.next());

    Vector<String> consumerReceives = new Vector<String>();
    fis.forEachRemaining((s) -> consumerReceives.add(s));

    List<String> expected = Lists.<String>newArrayList("mno");
    assertEquals(expected, consumerReceives);
  }

  /**
   * test method accepts all input of any kind.
   **/
  private static class NeverFailingObjectTest<E> implements IObjectTest<E> {
    @Override
    public boolean test(E e) {
      return true;
    }
  }

  /**
   * test method rejects all input of any kind
   **/
  private static class CompletelyRejectingObjectTest<E> implements IObjectTest<E> {
    @Override
    public boolean test(E e) {
      return false;
    }
  }

  /**
   * Tests strings. test method rejects strings comprised entirely of caitalized letters.
   **/
  private static class RejectCapitalizedObjectTest implements IObjectTest<String> {
    @Override
    public boolean test(String s) {
      return (!Pattern.compile("[A-Z]+").matcher(s).matches());
    }
  }
}