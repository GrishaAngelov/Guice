package com.clouway.bank;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class ConnectionFilter implements Filter {
    private static GenericObjectPool objectPool;
    private static ConnectionFactory connectionFactory;
    private static PoolableConnectionFactory poolableConnectionFactory;
    private static ThreadLocal<PoolableConnection> connectionThreadLocal;

    private String url;
    private String user;
    private String password;


    @Inject
    public ConnectionFilter(@Named("databaseUrl") String url, @Named("user") String user, @Named("password")String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        setUpDatabaseConnection(url, user,password);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

    public static void setUpDatabaseConnection(String url, String user, String password) {
        objectPool = new GenericObjectPool();
        objectPool.setMaxActive(10);
        objectPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
        connectionFactory = new DriverManagerConnectionFactory(url, user, password);
        poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, objectPool, null, null, false, true);
        connectionThreadLocal = new ThreadLocal<PoolableConnection>();
    }

    public static PoolableConnection get() {
        try {
            connectionThreadLocal.set((PoolableConnection) poolableConnectionFactory.getPool().borrowObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectionThreadLocal.get();
    }
}
