package hu.bingus.netbankapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.service.AccountService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.persistence.NoResultException;
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

    public E findById(Long id) throws EntityNotFoundException {
        try {
            cb = getCurrentSession().getCriteriaBuilder();
            cr = cb.createQuery(entityClass);
            root = cr.from(entityClass);
            Predicate predicate = cb.equal(root.get("id"), id);
            cr.where(predicate);
            Query<E> query = getCurrentSession().createQuery(cr);
            E entity = query.getSingleResult();
            return entity;
        }catch (NoResultException e){
            throw new EntityNotFoundException("Nem található");
        }
    }

    public Session getCurrentSession(){
        return sessionFactory.openSession();
    }





}
