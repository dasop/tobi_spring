package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

//import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.domain.User;

public class UserDao {
	
//	private ConnectionMaker connectionMaker;
	
//	jdbcContextWithStatementStrategy 함수를 클래스 분리
// 1. 별도 JdbcContext 클래스로 분리
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
//2. JdbcContext 를 DI로 받음
	private JdbcContext jdbcContext;
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

 //생성자 사용한 의존관계 주입	
//	public UserDao(ConnectionMaker connectionMaker) {
//		this.connectionMaker = connectionMaker;
//	}
//	public UserDao() {}
//	
//	//수정자 사용한 의존관계 주입
//	public void setConnectionMaker(ConnectionMaker connectionMaker) {
//		this.connectionMaker = connectionMaker;
//	}

//	의존관계 검색
//	public UserDao() {
//		DaoFactory daoFactory = new DaoFactory();
//		this.connectionMaker = daoFactory.connectionMaker();
//	}

	//의존관계 검색
//	public UserDao() {
//		AnnotationConfigApplicationContext context =
//				new AnnotationConfigApplicationContext(DaoFactory.class);
//		this.connectionMaker = context.getBean("connectionMaker",ConnectionMaker.class);
//	}
	
	
//	public void add(User user) throws ClassNotFoundException, SQLException{
/*		
	public void add(User user) throws SQLException{
		
//		Connection c = connectionMaker.makeConnection();
		
		Connection c = null;
		
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement(
					"insert into users(id, name, password) values(?,?,?)");
			
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}finally {
			if(ps != null) {
				try {
					ps.close();
				}catch(SQLException e) {
					
				}
			}
			if(c != null) {
				try {
					c.close();
				}catch(SQLException e) {
					
				}
			}
		}
	}
*/

	public void add(final User user) throws SQLException{
		/* ver1. 로컬 클래스 방식
		class AddStatement implements StatementStrategy {
			
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(
						"insert into users(id, name, password) values(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}

		}
		
		StatementStrategy st = new AddStatement();
		jdbcContextWithStatementStrategy(st);
		*/
		
		/* ver2. 내부클래스 방식
		StatementStrategy st = new StatementStrategy() {
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(
						"insert into users(id, name, password) values(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		};
		
		jdbcContextWithStatementStrategy(st);
		 */
		
		/*ver3. 메소드 파라미터로 이전한 익명 내부 클래스
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(
						"insert into users(id, name, password) values(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		}
		);*/
		//3. JdbcContext의 메소드로 변경
		
//		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
//			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//				PreparedStatement ps = c.prepareStatement(
//						"insert into users(id, name, password) values(?,?,?)");
//				
//				ps.setString(1, user.getId());
//				ps.setString(2, user.getName());
//				ps.setString(3, user.getPassword());
//				return ps;
//			}
//		});
		
		this.jdbcContext.executeSql("insert into users(id, name, password) values(?,?,?)",user.getId(),user.getName(),user.getPassword());
	}		

	
	
	public User get(String id) throws ClassNotFoundException, SQLException{
//		Connection c = connectionMaker.makeConnection();
		
		Connection c = null;		
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			
			if(user==null) throw new EmptyResultDataAccessException(1);
			
			return user;
			
		}catch(SQLException e) {
			throw e;
		}finally {
			if(rs!=null) {try {rs.close();}catch(SQLException e) {}	}
			if(ps!=null) {try {ps.close();}catch(SQLException e) {}	}
			if(c!=null) {try {c.close();}catch(SQLException e) {}}
		}

//		rs.next();
//		User user = new User();
//		user.setId(rs.getString("id"));
//		user.setName(rs.getString("name"));
//		user.setPassword(rs.getString("password"));
	}

	/*
	public void deleteAll() throws SQLException{
//		Connection c = dataSource.getConnection();
		Connection c = null;
		PreparedStatement ps = null;
		
//		PreparedStatement ps = c.prepareStatement("delete from users");
		
		try {
			c=dataSource.getConnection();
			
//			ps=c.prepareStatement("delete from users");
			
			StatementStrategy strategy = new DeleteAllStatement();
			ps = strategy.makePreparedStatement(c);
			
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}finally {
			if(ps != null) {
				try {
					ps.close();
				}catch(SQLException e) {
					
				}
			}
			if(c != null) {
				try {
					c.close();
				}catch(SQLException e) {
					
				}
			}
		}
		
		
		ps.close();
		c.close();
	}
	*/
	
/*	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw e;
		}finally {
			if(ps != null) {
				try {
					ps.close();
				}catch(SQLException e) {
					
				}
			}
			if(c != null) {
				try {
					c.close();
				}catch(SQLException e) {
					
				}
			}
		}
	}
*/	
	
/*	public void deleteAll() throws SQLException{
//		StatementStrategy st = new DeleteAllStatement();
//		jdbcContextWithStatementStrategy(st);
		

 
//		jdbcContextWithStatementStrategy(
//				new StatementStrategy() {
//					public PreparedStatement makePreparedStatement(Connection c)
//							throws SQLException {
//						return c.prepareStatement("delete from users");
//					}
//				}
//		);
		
		this.jdbcContext.workWithStatementStrategy(
				new StatementStrategy() {
					public PreparedStatement makePreparedStatement(Connection c)
							throws SQLException {
						return c.prepareStatement("delete from users");
					}
				}
		);
		
	}
*/	
	public void deleteAll() throws SQLException{
		this.jdbcContext.executeSql("delete from users");
	}
	

	public int getCount() throws SQLException{
//		Connection c = dataSource.getConnection();
//		PreparedStatement ps = c.prepareStatement("select count(*) from users");
//		ResultSet rs = ps.executeQuery();
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		}catch(SQLException e) {
			throw e;
		}finally {
			if(rs!=null) {try {rs.close();}catch(SQLException e) {}	}
			if(ps!=null) {try {ps.close();}catch(SQLException e) {}	}
			if(c!=null) {try {c.close();}catch(SQLException e) {}}
		}
	}

	
}
