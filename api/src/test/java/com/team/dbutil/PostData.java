package com.team.dbutil;

import com.team.post.Post;
import com.team.post.PostRepository;
import com.team.user.User;
import org.springframework.stereotype.Component;

@Component
public class PostData {
    private final PostRepository postRepository;

    public PostData(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(User author){
        Post post = createPost(author);
        return postRepository.save(post);
    }

    private Post createPost(User author){
        String content = "content123123";
        return new Post(content,author);
    }
}
