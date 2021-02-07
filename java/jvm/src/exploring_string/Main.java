package exploring_string;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Date start = new Date();
        // String Pool의 데이터가 지워질 수 있으니 리스트에 넣어서 해당 현상을 방지
        List<String> strings = new ArrayList<String>();
        for(Integer i = 1; i < 10000000; i++) {
            String s = i.toString().intern();
            strings.add(s);
        }

        Date end  = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
