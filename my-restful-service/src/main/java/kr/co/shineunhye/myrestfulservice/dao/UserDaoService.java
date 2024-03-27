package kr.co.shineunhye.myrestfulservice.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.shineunhye.myrestfulservice.bean.User;

@Component
public class UserDaoService {
	private static List<User> users = new ArrayList<>();
	
	private static int userCount = 3;
	
	//초기 데이터 값 저장
	static {
		users.add(new User(1, "Coldmea", new Date(), "test1", "111111-1111111"));
		users.add(new User(2, "YeHee", new Date(), "test2", "222222-2222222"));
		users.add(new User(3, "SeoungHee", new Date(), "test3", "333333-3333333"));
	}

	//전체 데이터 반환
	public List<User> findAll() {
		return users;
	}
	
	public User save(User user) {
		if(user.getId() == null) {
			user.setId(++userCount);
		}
		
		if(user.getJoinDate() == null) {
			user.setJoinDate(new Date());
		}
		
		users.add(user);
		
		return user;
	}
	
	public User findOne(int id) {
		for(User user : users) {
			if(user.getId() == id) {
				return user;
			}
		}
		
		return null;
	}
	
	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();
		
		while(iterator.hasNext()) {
			User user = iterator.next();
			
			if(user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
}
