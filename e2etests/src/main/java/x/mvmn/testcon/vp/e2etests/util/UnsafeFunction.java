package x.mvmn.testcon.vp.e2etests.util;

public interface UnsafeFunction<I, O> {

    O apply(I input) throws Exception;
}
