package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.Role;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.model.dto.UserDTO;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("RoleService")
@Transactional
public class RoleServiceImpl extends AbstractCriteriaService<Role> implements RoleService {

    private final ObjectMapper mapper = new ObjectMapper();

    public RoleServiceImpl() {
        super(Role.class);
    }

    @Override
    public void addUser(UserDTO user) {
        User user1 = ContextProvider.getBean(UserService.class).findByUsername(user.getUsername());
        try {
            Query query = getCurrentSession().createSQLQuery("INSERT INTO users_x_roles (user_id, role_id) " +
                    "SELECT :userId,2 " +
                    "WHERE NOT EXISTS(SELECT * FROM users_x_roles WHERE user_id=:userId1 AND role_id=2)");
            query.setParameter("userId",user1.getId());
            query.setParameter("userId1",user1.getId());
            query.executeUpdate();
        }catch (Exception e){
            log.error("RoleServiceImpl - addUser: "+e+", username: "+user.getUsername());
        }
    }

    @Override
    public Boolean addAdmin(String user) {
        User user1 = ContextProvider.getBean(UserService.class).findByUsername(user);
        try {
            Query query = getCurrentSession().createSQLQuery(
                    "INSERT INTO users_x_roles (user_id, role_id) " +
                    "SELECT :userId,1 WHERE " +
                    "NOT EXISTS(SELECT * FROM users_x_roles WHERE user_id=:userId1 AND role_id=1)");
            query.setParameter("userId",user1.getId());
            query.setParameter("userId1",user1.getId());
            query.executeUpdate();

            return Boolean.TRUE;
        }catch (Exception e){
            log.error("RoleServiceImpl - addAdmin: "+e+", username: "+user);
            return Boolean.FALSE;
        }
    }
}
