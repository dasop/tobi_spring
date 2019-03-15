package springbook.user.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserdaoTest {
//	@Autowired
//	private ApplicationContext context;
	@Autowired
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		
//		System.out.println(this.context);
//		System.out.println(this);
		
//		this.dao = context.getBean("userDao",UserDao.class);
		
		this.user1 = new User("gyumee","박성철","springno1");
		this.user2 = new User("leegw700","이길원","springno2");
		this.user3 = new User("bumjin","박범진","springno3");
	}
	
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{
		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		
		
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));	
		
		
//		User user = new User();
//		user.setId("whiteship");
//		user.setName("백기선");
//		user.setPassword("married");
		
//		user.setId("gyumee");
//		user.setName("박성철");
//		user.setPassword("springno1");
		
//		User user = new User("gyumee","박성철","springno1");
		
//		dao.add(user);
		
//		assertThat(dao.getCount(), is(1));
		
//		System.out.println(user.getId()+"등록 성공");
		
//		User user2 = dao.get(user.getId());
		
		
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		System.out.println(user2.getId()+"조회 성공");
		
//		if(!user.getName().equals(user2.getName())) {
//			System.out.println("테스트 실패(name)");
//		}
//		else if(!user.getPassword().contentEquals(user2.getPassword())) {
//			System.out.println("테스트 실패(password)");
//		}
//		else {
//			System.out.println("조회 테스트 성공");
//		}
		
//		assertThat(user2.getName(), is(user.getName()));
//		assertThat(user2.getPassword(), is(user.getPassword()));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(),is(user1.getName()));
		assertThat(userget1.getPassword(),is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(),is(user2.getName()));
		assertThat(userget2.getPassword(),is(user2.getPassword()));
		}
	
	@Test
	public void count() throws SQLException{

		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(),is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(),is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(),is(3));
		
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		
		dao.deleteAll();
		assertThat(dao.getCount(),is(0));
		dao.get("unknown_id");
	}
	
	public static void main(String[] args) {
		JUnitCore.main("springbook.user.domain.UserdaoTest");
	}

}
