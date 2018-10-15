package misc;

import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import model.CustomerBean;
import model.ProductBean;

@Configuration
@ComponentScan(basePackages={"model"})
public class SpringJavaConfiguration {
	@Bean
	public DataSource dataSource() {
		JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
		factory.setJndiName("java:comp/env/jdbc/xxx");
		factory.setProxyInterface(javax.sql.DataSource.class);
		try {
			factory.afterPropertiesSet();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return (DataSource) factory.getObject();
	}
	
	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder builder =
				new LocalSessionFactoryBuilder(dataSource());

		Properties props = new Properties();
		props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
		props.put("hibernate.current_session_context_class", "thread");
		props.put("hibernate.show_sql", "true");	
		builder.addProperties(props);
		
		builder.addAnnotatedClasses(CustomerBean.class, ProductBean.class);
	
		return builder.buildSessionFactory();
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(SpringJavaConfiguration.class);
		
//		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
//		sessionFactory.getCurrentSession().beginTransaction();
//		Session session = sessionFactory.getCurrentSession();
//		CustomerBean select = session.get(CustomerBean.class, "Alex");
//		System.out.println("select="+select);
//		sessionFactory.getCurrentSession().getTransaction().commit();
	
//		DataSource dataSource = (DataSource) context.getBean("dataSource");
//		Connection conn = dataSource.getConnection();
//		Statement stmt = conn.createStatement();
//		ResultSet rset = stmt.executeQuery("select * from dept");
//		while(rset.next()) {
//			String col1 = rset.getString(1);
//			String col2 = rset.getString(2);
//			System.out.println(col1+":"+col2);
//		}				
		((ConfigurableApplicationContext)  context).close();
	}
}
