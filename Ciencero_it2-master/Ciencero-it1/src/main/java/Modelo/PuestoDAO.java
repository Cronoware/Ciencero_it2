package Modelo;

import Mapeo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.List;
import java.util.ArrayList;

public class PuestoDAO{
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
        	this.sessionFactory = sessionFactory;
    	}
  	
  	public synchronized void guardar(Puesto c) {
    
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
        
        public synchronized ArrayList<Puesto> getPuestos(String nombre){
            ArrayList<Puesto> p = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Puesto where nombre like :nombre";
                Query q = session.createQuery(hql);
                q.setParameter("nombre", nombre);
                p = (ArrayList<Puesto>)q.list();
                if(!tx.wasCommitted())
                    tx.commit();
                return p;
            }catch(Exception e){
                if (tx!=null && !tx.wasRolledBack()){ 
                    tx.rollback();
                }
                e.printStackTrace();
                return null;
            }finally{
                session.flush();
                session.close();
            }
        }
        
        public synchronized Puesto getPuesto(int puesto_id){
            Puesto p = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Puesto where puesto_id = :id";
                Query q = session.createQuery(hql);
                q.setParameter("id", puesto_id);
                p = (Puesto)q.uniqueResult();
                if(!tx.wasCommitted())
                    tx.commit();
                return p;
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
	
	public synchronized void eliminar(Puesto c) {
    
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
                    session.flush();
           		session.close();
        	}
	}
}
