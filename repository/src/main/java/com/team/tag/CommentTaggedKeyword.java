package com.team.tag;

import com.team.comment.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentTaggedKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_keyword_id")
    private TagKeyword tagKeyword;

    public CommentTaggedKeyword(Comment comment, TagKeyword tagKeyword) {
        this.comment = comment;
        this.tagKeyword = tagKeyword;
    }
}
