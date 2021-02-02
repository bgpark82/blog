package com.bgpark.jpa;

import com.bgpark.jpa.domain.Comment;
import com.bgpark.jpa.domain.CommentRepository;
import com.bgpark.jpa.domain.Post;
import com.bgpark.jpa.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class JpaApplication implements CommandLineRunner {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final EntityManager em;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        Post post = Post.builder()
                .title("게시글")
                .build();

        Comment comment = Comment.builder()
                .comment("댓글")
                .build();

        post.addComment(comment);

        em.persist(post);
//        em.persist(comment);



        em.flush();
        em.remove(post);
    }

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

}
