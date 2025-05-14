package sn.ept.git.seminaire.cicd.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.Instant;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class BaseDTO implements Serializable {

    private String id;
    @JsonProperty(value = "created_date")
    private Instant createdDate ;

    @JsonProperty(value = "last_modified_date")
    private Instant lastModifiedDate;
    private int version;

}
