package com.team.tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentTaggedUserRepository extends JpaRepository<CommentTaggedUser, Long> {
}
