package Modelo;

import Mapeo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;

public class UsuarioDAO {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
        	this.sessionFactory = sessionFactory;
    	}
  	
  	public synchronized boolean guardar(Usuario c) {
    
	        Session session = sessionFactory.openSession();
        	Transaction tx = null;
	        try {
			tx = session.beginTransaction();
         
           		session.persist(c);
           		tx.commit();
                        return true;
        	}catch (Exception e) {
           		if (tx!=null){ 
               			tx.rollback();
           		}
           		e.printStackTrace();
                        return false;
        	}finally {
                    session.flush();
           		session.close();
        	}
	}
        
        public synchronized Usuario getUsuario(String correo, String psswd){
            Usuario u = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Usuario where correo = :correo";
                Query q = session.createQuery(hql);
                q.setParameter("correo", correo);
                u = (Usuario)q.uniqueResult();
                if(u==null)
                    throw new Exception("Usuario nulo");
                if(!tx.wasCommitted())
                    tx.commit();
                if(u.getPsswd().equals(psswd)){
                    return u;
                }else{
                    return null;
                }
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
                session.flush();
                session.close();
            }
        }
        
        public synchronized Usuario getUsuario(int usuario_id){
            Usuario u = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Usuario where usuario_id = :id";
                Query q = session.createQuery(hql);
                q.setParameter("id", usuario_id);
                u = (Usuario)q.uniqueResult();
                if(!tx.wasCommitted())
                tx.commit();
                return u;
            }catch(Exception e){
                if (tx!=null && tx.isActive()){ 
                    try{
                        tx.rollback();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        
                    }
                }
                //e.printStackTrace();
                return null;
            }finally{
                session.flush();
                session.close();
            }
        }
        
        public synchronized Usuario getUsuario(String correo){
            Usuario u = null;
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String hql = "from Usuario where correo = :correo";
                Query q = session.createQuery(hql);
                q.setParameter("correo", correo);
                u = (Usuario)q.uniqueResult();
                if(!tx.wasCommitted())
                tx.commit();
                return u;
            }catch(Exception e){
                if (tx!=null && tx.isActive()){ 
                    try{
                        tx.rollback();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        
                    }
                }
                //e.printStackTrace();
                return null;
            }finally{
                session.flush();
                session.close();
            }
        }
	
	public synchronized void eliminar(Usuario c) {
    
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
