package th.or.dga.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.or.dga.topic.exception.ResourceNotFoundException;
import th.or.dga.topic.model.Post;
import th.or.dga.topic.repository.PostRepository;
import th.or.dga.topic.repository.TopicRepository;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/topics/{topicId}/posts")
@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<Post> getAllPost(@PathVariable(value = "topicId") String topicId) {
        checkExistTopic(topicId);
        return postRepository.findByTopicId(topicId);
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable(value = "topicId") String topicId,
            @PathVariable(value = "postId") String postId) {
        checkExistTopic(topicId);
        return postRepository.findById(postId)
                             .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
    }

    @PostMapping
    public Post createPost(@PathVariable(value = "topicId") String topicId, @Valid @RequestBody Post post) {
        return topicRepository.findById(topicId)
                              .map(topic -> {
                                  post.setTopic(topic);
                                  return postRepository.save(post);
                              })
                              .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable(value = "topicId") String topicId, @PathVariable String postId,
                           @Valid @RequestBody Post postRequest) {
        checkExistTopic(topicId);
        return postRepository.findById(postId)
                             .map(post -> {
                                 post.setMessage(postRequest.getMessage());
                                 return postRepository.save(post);
                             })
                             .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "topicId") String topicId, @PathVariable String postId) {
        checkExistTopic(topicId);
        return postRepository.findById(postId)
                             .map(post -> {
                                 postRepository.delete(post);
                                 return ResponseEntity.ok()
                                                      .build();
                             })
                             .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
    }

    private void checkExistTopic(String topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic not found with id " + topicId);
        }
    }

}