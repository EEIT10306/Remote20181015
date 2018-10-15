package misc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
@WebFilter(
		urlPatterns= {"/*"}
)
public class OpenSessionInViewFilter implements Filter {
	private SessionFactory sessionFactory;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext application = filterConfig.getServletContext();
		ApplicationContext context = (ApplicationContext)
				application.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		sessionFactory = (SessionFactory) context.getBean("sessionFactory");
	}
	@Override
	public void doFilter(ServletRequest req,
			ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			sessionFactory.getCurrentSession().beginTransaction();
			chain.doFilter(req, resp);
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			sessionFactory.getCurrentSession().getTransaction().rollback();
			chain.doFilter(req, resp);
		}
	}
	@Override
	public void destroy() {

	}
}
