package ch.chang.mybatis.source.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class XmlBuildTest {
    @Test
    public void build() throws IOException, SQLException {
        InputStream resourceAsStream = Resources.getResourceAsStream("conf/CustomizedSettingsMapperConfig.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory build = sqlSessionFactoryBuilder.build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement("select now()");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        System.out.println(resultSet.getString(1));
    }

    @Test
    public void cast() {
        String iszhangsan = PropertyNamer.methodToProperty("isZhangsan");
        System.out.println(iszhangsan);
    }

    @Test
    public void parent() throws NoSuchMethodException {
        Method getA = A.class.getMethod("getA");
        Method getB = B.class.getMethod("getB");
        //A 是B的父类
        System.out.println(getA.getReturnType().isAssignableFrom(getB.getReturnType()));
        System.out.println(getB.getReturnType().isAssignableFrom(getA.getReturnType()));
    }

    static class A {
        static {
            System.out.println("A");
        }

        public A getA() {
            return this;
        }
    }

    static class B extends A {
        static {
            System.out.println("B");
        }

        public B getB() {
            return this;
        }
    }

    static class C extends A {
        static {
            System.out.println("A");
        }

        @Override
        public C getA() {
            return null;
        }
    }
}
