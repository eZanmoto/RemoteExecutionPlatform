import com.ezanmoto.rep.Callable;

public class TestClass implements Callable {
    public Object call() {
        return new Integer( 1 + 1 );
    }
}