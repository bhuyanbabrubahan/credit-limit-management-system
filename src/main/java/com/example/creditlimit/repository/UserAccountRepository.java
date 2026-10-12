package com.example.creditlimit.repository;

import com.example.creditlimit.entity.UserAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	Optional<UserAccount> findByUsername(String username);

	List<UserAccount> findByParentId(Long parentId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserAccount u where u.id = :id")
	Optional<UserAccount> findByIdForUpdate(Long id);
}
