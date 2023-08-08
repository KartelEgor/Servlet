package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Stub
public class PostRepository {

  private final List<Post> listOfPosts = new CopyOnWriteArrayList<>();
  AtomicInteger count = new AtomicInteger(1);

  public List<Post> all() {
    return listOfPosts;
  }

  public Optional<Post> getById(long id) {
    for (Post idPost : listOfPosts) {
      if (id == idPost.getId()) {
        return Optional.of(idPost);
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if(post.getId() == 0) {
      listOfPosts.add(new Post(count.get(), post.getContent()));
      count.getAndIncrement();
    } else {
      for (Post idPost : listOfPosts) {
        if (idPost.getId() == post.getId()) {
          idPost.setContent(post.getContent());
          return post;
        }
      }
      throw new NotFoundException();
    }
    return post;
  }

  public void removeById(long id) {
      listOfPosts.removeIf(idPost -> id == idPost.getId());
  }
}
