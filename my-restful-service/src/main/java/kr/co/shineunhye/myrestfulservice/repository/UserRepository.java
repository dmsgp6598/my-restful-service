package kr.co.shineunhye.myrestfulservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.shineunhye.myrestfulservice.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
}
