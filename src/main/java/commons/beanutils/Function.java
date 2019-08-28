package commons.beanutils;

import commons.beanutils.support.Person;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghf
 * @className commons.beanutils.Function.java
 * @createTime 2019/8/27 11:41
 */
public class Function {



    /** 克隆功能*/
    public static void cloneFunc(){
        Person person = new Person();
        person.setName("John");
        person.setAge(37);
        System.out.println("person = " + person);

        try {
            Person clonePerson = (Person) BeanUtils.cloneBean(person);
            System.out.println("clonePerson = " + clonePerson);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /** map和bean的转换功能*/
    public static void convertFunc() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Stark");
        map.put("age", 43);

        Person person = new Person();
        try {
            BeanUtils.populate(person, map);
            System.out.println("person = " + person);

            person.setAge(44);
            Map describe = BeanUtils.describe(person);
            System.out.println("describe = " + describe);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        cloneFunc();
        convertFunc();
    }
}
