package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"), it.getInt("photo_id"), it.getInt("city_id")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("email"),
                            it.getString("password")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM city")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name, photo_id, city_id) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE post SET name = (?) WHERE id = (?)")) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE candidate SET name = (?), photo_id = (?), city_id = (?) WHERE id = (?)")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.setInt(4, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int save(Photo photo) {
        int result;
        if (photo.getId() == 0) {
            result = create(photo);
        } else {
            update(photo);
            result = photo.getId();
        }
        return result;
    }

    private int create(Photo photo) {
        int result = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("insert into photo(name) values(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, photo.getName());
            st.execute();
            try (ResultSet id = st.getGeneratedKeys()) {
                if (id.next()) {
                    result = id.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void update(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("UPDATE photo SET name = (?) WHERE id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, photo.getName());
            st.setInt(2, photo.getId());
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private int create(User user) {
        int result = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("INSERT INTO users(name, email, password) VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.execute();
            try (ResultSet id = st.getGeneratedKeys()) {
                if (id.next()) {
                    result = id.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("UPDATE users SET name = (?), email = (?), password = (?) WHERE id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.setInt(4, user.getId());
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(City city) {
        if (city.getId() == 0) {
            create(city);
        } else {
            update(city);
        }
    }

    private int create(City city) {
        int result = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("insert into city(name) values(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, city.getName());
            st.execute();
            try (ResultSet id = st.getGeneratedKeys()) {
                if (id.next()) {
                    result = id.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void update(City city) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("UPDATE city SET name = (?) WHERE id = (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, city.getName());
            st.setInt(2, city.getId());
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post findByIdPost(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = (?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(id, rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return post;
    }

    @Override
    public Candidate findByIdCandidate(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate WHERE id = (?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = new Candidate(id, rs.getString("name"), rs.getInt("photo_id"), rs.getInt("city_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return candidate;
    }

    @Override
    public Photo findByIdPhoto(int id) {
        Photo photo = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM photo WHERE id = (?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    photo = new Photo(id, rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }

    @Override
    public City findByIdCity(int id) {
        City city = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM city WHERE id = (?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    city = new City(id, rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return city;
    }

    @Override
    public User findByEmailUser(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users WHERE email = (?)")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM candidate WHERE id = (?)")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePhoto(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM photo WHERE id = (?)")) {
            st.setInt(1, id);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteUser(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM users WHERE id = (?)")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCity(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM city WHERE id = (?)")) {
            st.setInt(1, id);
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}