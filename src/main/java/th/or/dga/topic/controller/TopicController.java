package th.or.dga.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.or.dga.topic.exception.ResourceNotFoundException;
import th.or.dga.topic.model.Post;
import th.or.dga.topic.model.Topic;
import th.or.dga.topic.repository.PostRepository;
import th.or.dga.topic.repository.TopicRepository;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/topics")
@RestController
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Topic> getAllTopic() {
        return topicRepository.findAll();
    }

    @GetMapping("/{topicId}")
    public Topic getTopicById(@PathVariable(value = "topicId") String topicId) {
        return topicRepository.findById(topicId)
                              .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
    }

    @PostMapping
    public Topic createTopic(@Valid @RequestBody Topic topic) {
        topic.setStatus(Topic.STATUS_NEW);
        return topicRepository.save(topic);
    }

    @PutMapping("/{topicId}")
    public Topic updateTopic(@PathVariable String topicId, @Valid @RequestBody Topic topicRequest) {
        return topicRepository.findById(topicId)
                              .map(topic -> {
                                  topic.setSubject(topicRequest.getSubject());
                                  topic.setDetail(topicRequest.getDetail());
                                  topic.setAttachmentPath(topicRequest.getAttachmentPath());
                                  topic.setStatus(topicRequest.getStatus());
                                  return topicRepository.save(topic);
                              })
                              .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicId) {
        return topicRepository.findById(topicId)
                              .map(topic -> {
                                  topicRepository.delete(topic);
                                  return ResponseEntity.ok()
                                                       .build();
                              })
                              .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + topicId));
    }

}