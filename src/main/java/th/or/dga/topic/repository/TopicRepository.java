package th.or.dga.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.or.dga.topic.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {
}