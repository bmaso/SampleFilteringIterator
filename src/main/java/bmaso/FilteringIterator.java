package bmaso;

import java.util.Iterator;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class FilteringIterator<E> implements Iterator<E> {
    private Iterator<E> underlying;
    private IObjectTest<E> objectTest;
    
    private Optional<E> nextValue_opt = Optional.<E>empty();
    
    public FilteringIterator(Iterator<E> anIterator, IObjectTest<E> anObjectTest) {
        this.underlying = anIterator;
        this.objectTest = anObjectTest;
    }
    
    public synchronized boolean hasNext() {
        while(underlying.hasNext()) {
            E e = underlying.next();
            if(objectTest.test(e)) {
                nextValue_opt = Optional.ofNullable(e);
                return true;
            }
        }
        
        return false;
    }
    
    public synchronized E next() {
        if(nextValue_opt.isPresent()) {
            Optional<E> n = nextValue_opt;
            nextValue_opt = Optional.<E>empty();
            return n.get();
        } else {
            throw new NoSuchElementException();
        }
    }
    
    public void remove() {
        //...rely on underlying to properly throw exceptions...
        underlying.remove();
    }
    
    /**
     * Synchronization is tricky with this method -- this object remains locked while consumer
     * function executes, which means there is a <i>potential</i> for thread deadlock.
     * The consumer must not block the callback thread, and must be conscious that this
     * object's monitor is owned by the callback thread. Also the callback thread should not
     * directly or indirectly invoke hasNext or next methods on this object.
     **/
    public synchronized void forEachRemaining(Consumer<? super E> action) {
        nextValue_opt.ifPresent(action);

        //...implementation taken straight from java.util.Iterator<E> javadocs...
        while(hasNext()) {
            action.accept(next());
        }
    }
}