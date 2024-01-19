package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ShortsComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shorts shorts;

    @NotNull
    private String comment;

    private Long parentCommentId;

    public static ShortsComment toEntity(CreateShortsCommentRequest createShortsCommentRequest,
        Shorts shorts) {
        return ShortsComment.builder()
            .comment(createShortsCommentRequest.getContent())
            //.user(user)
            .shorts(shorts)
            .build();
    }

    public void updateParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}