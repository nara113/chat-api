package chat.api.profile.entity;

import chat.api.entity.base.BaseTimeEntity;
import chat.api.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Entity
public class UploadFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String url;

    private String originalFileName;

    private Long size;

    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("'N'")
    private boolean isDeleted;

    @Builder
    private UploadFile(String url, String originalFileName, Long size, String contentType, User user) {
        this.url = url;
        this.originalFileName = originalFileName;
        this.size = size;
        this.contentType = contentType;
        this.user = user;
    }
}
