import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestMain {
   public static void main(String[] args){
//     String a = "hello";
     List<Integer> integerList = IntStream.range(0,5).boxed().collect(Collectors.toList());

     test(integerList);

     integerList.forEach(System.out::println);
   }

   public static void test(final List<Integer> list){
       list.add(5);
   }
}
