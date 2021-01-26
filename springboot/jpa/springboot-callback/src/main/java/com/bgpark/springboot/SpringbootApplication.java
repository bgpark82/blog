package com.bgpark.springboot;

import com.bgpark.springboot.domain.Person;
import com.bgpark.springboot.domain.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringbootApplication implements CommandLineRunner {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person(1L, "bgpark");
		Person newPerson = personRepository.save(person);
		System.out.println("3. Saved person : " +  newPerson);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
}
