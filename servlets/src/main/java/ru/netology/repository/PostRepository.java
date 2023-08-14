package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Stub
public class PostRepository {

    private final AtomicInteger count = new AtomicInteger(1);

    private final Map<Long, Post> mapOfPosts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new CopyOnWriteArrayList<>(mapOfPosts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(mapOfPosts.get(id));
    }


    public Post save(Post post) {
        if (post.getId() == 0) {
            mapOfPosts.put((long) count.get(), new Post(count.get(), post.getContent()));
            count.getAndIncrement();
        } else {
            if (mapOfPosts.containsKey(post.getId())) {
                mapOfPosts.put(post.getId(), new Post(post.getId(), post.getContent()));
                return post;
            }
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        mapOfPosts.remove(id);
    }
}
