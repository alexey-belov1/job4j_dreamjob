package ru.job4j.dream.store;

import ru.job4j.dream.model.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final Store INST = new MemStore();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Map<Integer, User> users = new ConcurrentHashMap<>();

    private static AtomicInteger POST_ID = new AtomicInteger(4);

    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);

    private static AtomicInteger USER_ID = new AtomicInteger();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
        candidates.put(1, new Candidate(1, "Junior Java", 0, 0));
        candidates.put(2, new Candidate(2, "Middle Java", 0, 0));
        candidates.put(3, new Candidate(3, "Senior Java", 0, 0));
    }

    public static Store instOf() {
        return INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public Collection<City> findAllCities() {
        return null;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public int save(Photo photo) {
        return -1;
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void save(City city) {

    }

    @Override
    public Post findByIdPost(int id) {
        return posts.get(id);
    }

    @Override
    public Candidate findByIdCandidate(int id) {
        return candidates.get(id);
    }

    @Override
    public Photo findByIdPhoto(int id) {
        return null;
    }

    @Override
    public City findByIdCity(int id) {
        return null;
    }

    @Override
    public User findByEmailUser(String email) {
        User result = null;
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                result = user;
                break;
            }
        }
        return result;
    }

    @Override
    public void deleteCandidate(int id) {
    }

    @Override
    public void deletePhoto(int id) {
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public void deleteCity(int id) {

    }
}