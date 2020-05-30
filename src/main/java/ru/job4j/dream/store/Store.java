package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    int save(Photo photo);

    Post findByIdPost(int id);

    Candidate findByIdCandidate(int id);

    Photo findByIdPhoto(int id);

    void deleteCandidate(int id);

    void deletePhoto(int id);
}