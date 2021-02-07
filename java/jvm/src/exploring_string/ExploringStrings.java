package exploring_string;

public class ExploringStrings {

    public static void main(String[] args) {
        // 두개의 hello 객체가 힙에 저장되고 가가 스택에서 포인팅된다
        // String은 immutable하다
        // 자바는 똑똑해서 one과 two를 하나의 hello 객체에 포인팅 시킨다
        String one = "hello";
        String two = "hello";

        // 값은 value를 가지는가
        System.out.println(one.equals(two));
        // 힙에 존재하는 같은 물리적인 객체인가
        System.out.println(one == two);

        Integer i = 76;
        String three = i.toString().intern();
        String four = "76";

        System.out.println(three.equals(four));
        System.out.println(three == four);
    }
}
