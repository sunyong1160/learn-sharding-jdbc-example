package learn.sharding.jdbc.example.guavacache;

import com.google.common.base.Optional;

import java.util.Set;

/**
 * Optional.of(T)：获得一个Optional对象，其内部包含了一个非null的T数据类型实例，若T=null，则立刻报错。
 * Optional.absent()：获得一个Optional对象，其内部包含了空值
 * Optional.fromNullable(T)：将一个T的实例转换为Optional对象，T的实例可以不为空，也可以为空[Optional.fromNullable(null)，和Optional.absent()等价。
 * boolean isPresent()：如果Optional包含的T实例不为null，则返回true；若T实例为null，返回false
 * T get()：返回Optional包含的T实例，该T实例必须不为空；否则，对包含null的Optional实例调用get()会抛出一个IllegalStateException异常
 * T or(T)：若Optional实例中包含了传入的T的相同实例，返回Optional包含的该T实例，否则返回输入的T实例作为默认值
 * T orNull()：返回Optional实例中包含的非空T实例，如果Optional中包含的是空值，返回null，逆操作是fromNullable()
 * Set<T> asSet()：返回一个不可修改的Set，该Set中包含Optional实例中包含的所有非空存在的T实例，且在该Set中，每个T实例都是单态，
 *          如果Optional中没有非空存在的T实例，返回的将是一个空的不可修改的Set。
 */
public class OptionalTest {

    public static void testMethodReturn() {
        Optional<Long> value = method();
        //boolean isPresent()：如果Optional包含的T实例不为null，则返回true；若T实例为null，返回false
        if (value.isPresent() == true) {
            System.out.println("获得返回值: " + value.get());
        } else {
            System.out.println("获得返回值: " + value.or(-12L));
        }

        System.out.println("获得返回值 orNull: " + value.orNull());

        Optional<Long> valueNoNull = methodNoNull();
        if (valueNoNull.isPresent() == true) {
            Set<Long> set = valueNoNull.asSet();
            System.out.println("获得返回值 set 的 size : " + set.size());
            System.out.println("获得返回值: " + valueNoNull.get());
        } else {
            System.out.println("获得返回值: " + valueNoNull.or(-12L));
        }

        System.out.println("获得返回值 orNull: " + valueNoNull.orNull());
    }

    private static Optional<Long> method() {
        return Optional.fromNullable(null);
    }

    //将一个T的实例转换为Optional对象，T的实例可以不为空，也可以为空[Optional.fromNullable(null)
    private static Optional<Long> methodNoNull() {
        return Optional.fromNullable(15L);
    }

    public static void main(String[] args) {
        testMethodReturn();
    }

}
