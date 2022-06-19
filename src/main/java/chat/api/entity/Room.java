package chat.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;


    @OneToMany(mappedBy = "room")
    private List<Group> users = new ArrayList<>();
}
