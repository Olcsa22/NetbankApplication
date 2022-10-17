package hu.bingus.netbankapp.security;

import hu.bingus.netbankapp.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
@EnableTransactionManagement
public class UserDetailsServiceImpl implements UserDetailsService {


    private final SessionFactory sessionFactory;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.where(cb.equal(root.get("username"),username));

        Query<User> query = session.createQuery(cr);
        User result = query.getSingleResult();


        if(result!=null) {
            return new UserDetailsImpl(result);
        }
        else {
            log.error("Nem található felhasználó");
            return null;
        }
    }

}
