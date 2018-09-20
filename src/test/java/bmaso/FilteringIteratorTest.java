package bmaso;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.Vector;
import java.util.List;
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

  private static class NeverFailingObjectTest<E> implements IObjectTest<E> {
    @Override
    public boolean test(E e) {
      return true;
    }
  }
}