package ch.chang.mybatis.source.test.meta.object;

import ch.chang.mybatis.source.test.entity.Book;
import ch.chang.mybatis.source.test.entity.Student;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

import java.util.Arrays;

public class MetaObjectTest {

    /**
     *  可以没有get set 方法  达到获取属性值
     *
     */
    public static  void metaObjectGetValue(){
        Book java = new Book("java");
        Book php = new Book("php");
        Student zhangsan = new Student("zhangsan", "23");
        zhangsan.setBooks( Arrays.asList(java,php));

        MetaObject metaObject = MetaObject.forObject(zhangsan, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        Object bookName = metaObject.getValue("books[1].name");
        System.out.println(bookName);
    }

    public static void main(String[] args) {
        metaObjectGetValue();
    }
}
