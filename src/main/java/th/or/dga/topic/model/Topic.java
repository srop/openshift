package th.or.dga.topic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "topics")
public class Topic {
    public static final String STATUS_NEW = "new";
    public static final String STATUS_REPLIED = "replied";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String subject;
    private String detail;
    private String attachmentPath;
    private String status;

    @JsonIgnore
    @OneToMany(
            mappedBy = "topic",
            cascade = CascadeType.ALL
    )
    private List<Post> posts = new ArrayList<>();
}
