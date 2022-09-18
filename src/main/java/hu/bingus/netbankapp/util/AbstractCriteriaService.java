package hu.bingus.netbankapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AbstractCriteriaService<E> {

    private Class<E> entityClass;
    private CriteriaBuilder cb;
    private CriteriaQuery<E> cr;
    Root<E> root;
    @Autowired
    private SessionFactory sessionFactory;

    public AbstractCriteriaService(Class<E> entityClass){
        this.entityClass=entityClass;
    }

    public List<E> getWhereEq(Map<String,Object> crits){
        cb=getCurrentSession().getCriteriaBuilder();
        cr=cb.createQuery(entityClass);
        root=cr.from(entityClass);
        List<Predicate> predicates = new ArrayList<>();
        for(Map.Entry<String,Object> entry : crits.entrySet()){
            predicates.add(cb.equal(root.get(entry.getKey()),entry.getValue()));
        }

        cr.select(root).where(predicates.toArray(new Predicate[]{}));
        Query<E> query = getCurrentSession().createQuery(cr);
        List<E> entities = query.getResultList();

        if(entities!=null && entities.size()>0) {
            return entities;
        }else{
            return null;
        }

    }

    public Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

}
