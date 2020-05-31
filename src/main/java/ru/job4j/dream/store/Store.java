package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    Collection<User> findAllUsers();

    void save(Post post);

    void save(Candidate candidate);

    int save(Photo photo);

    void save(User user);

    Post findByIdPost(int id);

    Candidate findByIdCandidate(int id);

    Photo findByIdPhoto(int id);

    User findByEmailUser(String email);

    void deleteCandidate(int id);

    void deletePhoto(int id);

    void deleteUser(int id);
}