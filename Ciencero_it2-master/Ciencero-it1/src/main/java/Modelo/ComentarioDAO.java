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
public class ComentarioDAO{

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
        	this.sessionFactory = sessionFactory;
    	}
  	
  	public synchronized void guardar(Comentario c) {
    
	        Session session = sessionFactory.openSession();
        	Transaction tx = null;
	        try {
			tx = session.beginTransaction();
         
           		session.persist(c);
           		tx.commit();
        	}catch (Exception e) {
           		if (tx!=null){ 
               			tx.rollback();
           		}
           		e.printStackTrace(); 
        	}finally {
                    session.flush();
           		session.close();
        	}
	}
        
        public synchronized Comentario getComentario(int comentario_id){
            Comentario c = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Comentario where comentario_id = :id";
                Query q = session.createQuery(hql);
                q.setParameter("id", comentario_id);
                c = (Comentario)q.uniqueResult();
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
	
	public synchronized void eliminar(Comentario c) {
    
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
