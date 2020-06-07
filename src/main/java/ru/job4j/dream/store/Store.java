package ru.job4j.dream.store;

import ru.job4j.dream.model.*;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    Collection<User> findAllUsers();

    Collection<City> findAllCities();

    void save(Post post);

    void save(Candidate candidate);

    int save(Photo photo);

    void save(User user);

    void save(City city);

    Post findByIdPost(int id);

    Candidate findByIdCandidate(int id);

    Photo findByIdPhoto(int id);

    City findByIdCity(int id);

    User findByEmailUser(String email);

    void deleteCandidate(int id);

    void deletePhoto(int id);

    void deleteUser(int id);

    void deleteCity(int id);
}