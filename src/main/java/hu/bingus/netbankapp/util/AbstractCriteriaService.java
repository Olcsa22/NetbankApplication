package hu.bingus.netbankapp.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.query.Query;
import org.springframework.data.jpa.provider.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        cb=getCurrentSession().getCriteriaBuilder();
        cr=cb.createQuery(entityClass);
        root=cr.from(entityClass);
    }

    public Optional<List<E>> getWhereEq(Map<String,String> crits){
        for(Map.Entry<String,String> entry : crits.entrySet()){
            cr.where(cb.equal(root.get(entry.getKey()),entry.getValue()));
        }

        Query<E> query = getCurrentSession().createQuery(cr);
        Optional<List<E>> optionalEList = Optional.ofNullable(query.getResultList());

        return optionalEList;

    }

    public Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

}
