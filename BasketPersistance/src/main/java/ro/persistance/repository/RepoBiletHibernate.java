package ro.persistance.repository;

import org.hibernate.Session;
import ro.model.Bilet;
import ro.persistance.IRepoBilet;

public class RepoBiletHibernate implements IRepoBilet {

    @Override
    public Bilet save(Bilet bilet){
        HibernateUtils.getSessionFactory().inTransaction(session->session.persist(bilet));
        return bilet;
    }

    @Override
    public Bilet findOne(Integer id){
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.createSelectionQuery("from Bilet where id=:idBilet", Bilet.class)
                    .setParameter("idBilet",id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Iterable<Bilet> findAll(){
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Bilet", Bilet.class).getResultList();
        }
    }

    @Override
    public Bilet delete(Integer integer) {
        return null;
    }

    @Override
    public Bilet update(Bilet Bilet) {
        return null;
    }
}
