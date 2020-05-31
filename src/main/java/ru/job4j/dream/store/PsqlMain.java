package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

public class PsqlMain {
    public static void main(String[] args) {

        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Junior Java Job"));
        store.save(new Post(0, "Middle Java Job"));
        store.save(new Post(0, "Senior Java Job"));

        store.save(new Candidate(0, "Junior Java", 0));
        store.save(new Candidate(0, "Middle Java", 0));
        store.save(new Candidate(0, "Senior Java", 0));

        store.save(new User(0, "User1", "email1", "1"));
        store.save(new User(0, "User2", "email2", "2"));
        store.save(new User(0, "User3", "email3", "3"));

        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }

        for (User user : store.findAllUsers()) {
            System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail() + " " + user.getPassword());
        }
    }
}