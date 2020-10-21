package hello.chapter8;

import hello.domain.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class Chapter8Main {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Team team1 = new Team();
            team1.setName("팀1");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("팀2");
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(team1);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(team2);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(team2);
            em.persist(member3);

            // flush 자동 호출
            // member1,2,3 모두 DB에 저장된 상태
            String query = "update Member m set m.age = 20";

            int resultCount = em.createQuery(query)
                    .executeUpdate();

            // 이렇게 하면 영속성 컨텍스트에서 아무것도 없기 때문에 DB에서 새로 가온다
            em.clear();

            Member member = em.find(Member.class, member1.getId());

            // 이때 영속성 컨텍스트에는 member1의 나이 0 그대로 있다
            // DB에만 반영되었고 영속성 컨텍스트에는 아직 member1이 있으
            System.out.println(member.getAge());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        factory.close();
    }

}
