package Modelo;

import Mapeo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;


/**
 *
 * @author Emilio
 */
public class CalificacionDAO{

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
        	this.sessionFactory = sessionFactory;
    	}
  	
  	public synchronized void guardar(Calificacion c) {
    
	        Session session = sessionFactory.openSession();
        	Transaction tx = null;
	        try {
			tx = session.beginTransaction();
         
           		session.persist(c);
           		tx.commit();
                        session.flush();
        	}catch (Exception e) {
           		if (tx!=null){ 
               			tx.rollback();
           		}
           		e.printStackTrace(); 
        	}finally {
                    
           		session.close();
        	}
	}
        
        public synchronized Calificacion getPuesto(int calificacion_id){
            Calificacion c = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Calificacion where calificacion_id = :id";
                Query q = session.createQuery(hql);
                q.setParameter("id", calificacion_id);
                c = (Calificacion)q.uniqueResult();
                if(!tx.wasCommitted())
                    tx.commit();
                return c;
            }catch(Exception e){
                if (tx!=null && tx.isActive()){ 
                    try{
                        tx.rollback();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        
                    }
                }
                e.printStackTrace();
                return null;
            }finally{
                session.close();
            }
        }
	
	public synchronized void eliminar(Calificacion c) {
    
        	Session session = sessionFactory.openSession();
        	Transaction tx = null;
        	try {
        	   	tx = session.beginTransaction();
	           	session.delete(c);
           		tx.commit();
        	}catch (Exception e) {
	        	if (tx!=null){ 
        	       		tx.rollback();
           		}
           		e.printStackTrace(); 
        	}finally {
           		session.close();
        	}
	}

}
