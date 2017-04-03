package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.TicketUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TicketUserRepositoryImpl implements TicketUserRepository {

    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final class TicketUserRowMapper implements RowMapper<TicketUser> {

        @Override
        public TicketUser mapRow(ResultSet rs, int i) throws SQLException {
            TicketUser user = new TicketUser();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }

    private static final String SQL_INSERT_USER
            = "insert into users (username, password) values (?, ?)";
    private static final String SQL_INSERT_ROLE
            = "insert into user_roles (username, role) values (?, ?)";

    @Override
    public void create(TicketUser user) {
        jdbcOp.update(SQL_INSERT_USER,
                user.getUsername(),
                user.getPassword());
        for (String role : user.getRoles()) {
            jdbcOp.update(SQL_INSERT_ROLE,
                    user.getUsername(),
                    role);
        }
    }

    private static final String SQL_SELECT_ALL_USER
            = "select username, password from users";
    private static final String SQL_SELECT_ROLES
            = "select username, role from user_roles where username = ?";

    @Override
    public List<TicketUser> findAll() {
        List<TicketUser> users = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_USER);

        for (Map<String, Object> row : rows) {
            TicketUser user = new TicketUser();
            String username = (String) row.get("username");
            user.setUsername(username);
            user.setPassword((String) row.get("password"));
            List<Map<String, Object>> roleRows = jdbcOp.queryForList(SQL_SELECT_ROLES, username);
            for (Map<String, Object> roleRow : roleRows) {
                user.addRole((String) roleRow.get("role"));
            }
            users.add(user);
        }
        return users;
    }
    private static final String SQL_SELECT_USER
            = "select username, password from users where username = ?";

    @Override
    public TicketUser findByUsername(String username) {
        TicketUser ticketUser = jdbcOp.queryForObject(SQL_SELECT_USER, new TicketUserRowMapper(), username);
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ROLES, username);
        for (Map<String, Object> row : rows) {
            ticketUser.addRole((String) row.get("role"));
        }
        return ticketUser;
    }

    private static final String SQL_DELETE_USER
            = "delete from users where username = ?";
    private static final String SQL_DELETE_ROLES
            = "delete from user_roles where username = ?";

    @Override
    public void deleteByUsername(String username) {
        jdbcOp.update(SQL_DELETE_ROLES, username);
        jdbcOp.update(SQL_DELETE_USER, username);
    }

}
