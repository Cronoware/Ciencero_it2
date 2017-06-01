package Controlador;

import Mapeo.*;
import Modelo.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author Jaime & Javier
 */
@Controller 
public class Controlador {

    @Autowired
    public UsuarioDAO usuario_db;
    
    @Autowired
    public CalificacionDAO calificacion_db;
    
    @Autowired
    public ComentarioDAO comentario_db;
    
    @Autowired
    public PuestoDAO puesto_db;  
    
    @RequestMapping(value="/")
    public ModelAndView inicio(){
        ModelMap model = new ModelMap();
        Usuario usuarioActual = null;
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        model.addAttribute("puestos", puesto_db.getPuestos("%"));
        model.addAttribute("botones1", botones1(null));
        return new ModelAndView("PaginaInicioIH", model);
    }
    
    @RequestMapping(value="/eliminarUsuario", method=RequestMethod.POST)
    public ModelAndView eliminarUsuario(ModelMap model, HttpServletRequest request){
        String pid = request.getParameter("correoElim");
        Usuario p = usuario_db.getUsuario(pid);
        usuario_db.eliminar(p);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/eliminarPuesto", method=RequestMethod.POST)
    public ModelAndView eliminarPuesto(ModelMap model, HttpServletRequest request){
        Puesto p = (Puesto)request.getSession().getAttribute("puestoActual");
        puesto_db.eliminar(p);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/eliminarComentario", method=RequestMethod.POST)
    public ModelAndView eliminarComentario(ModelMap model, HttpServletRequest request){
        String cid = request.getParameter("textoComentario");
        System.out.println(" "+cid);
        int id = Integer.parseInt(cid);
        Comentario c = comentario_db.getComentario(id);
        comentario_db.eliminar(c);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/irPuesto", method=RequestMethod.GET)
    public ModelAndView irPuesto(ModelMap model, HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        String nom_puesto = request.getParameter("irId");
        System.out.println(" "+nom_puesto);
        Puesto puestoActual = puesto_db.getPuestos(nom_puesto).get(0);
        String comidas, nombrePuesto, ubicacion, descripcion, calificacion;
        nombrePuesto = puestoActual.getNombre();
        ubicacion = "(" + puestoActual.getPosX() + "," + puestoActual.getPosY() + ")";           
        List<Comentario> comentariosActuales = puestoActual.getComentarios_usuario();       
        List<String> des = puestoActual.getTags();
        calificacion = promedioLista(puestoActual.getCalificaciones_usuario()) + "";                
        if (!des.isEmpty()){
            descripcion = des.get(0);
        } else {
            descripcion = "No hay comidas registradas";
        } model.addAttribute("nombrePuesto", nombrePuesto);
        model.addAttribute("comidas",descripcion);
        model.addAttribute("ubicacion",ubicacion);
        model.addAttribute("descripcion",descripcion);
        model.addAttribute("calificacion",calificacion);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        request.getSession().setAttribute("comentariosActuales", comentariosActuales);
        request.getSession().setAttribute("puestoActual", puestoActual);
        model.addAttribute("elimP", elimP(usuarioActual));
        model.addAttribute("elimC", elimC(usuarioActual));
        model.addAttribute("campos", campos(usuarioActual));
        if(usuarioActual!=null)
        model.addAttribute("nua", usuarioActual.getNombre());
        return new ModelAndView("VerPuestoIH",model);
    }
    
    @RequestMapping(value="/comentarioSeleccionado", method = RequestMethod.GET)
    public ModelAndView comentarioSeleccionado(ModelMap model,HttpServletRequest request){       
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        String comentarioTexto = request.getParameter("textoComentario");
        System.out.println(comentarioTexto);
        model.addAttribute("puestos", puesto_db.getPuestos(""));
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/cerrarSesion", method = RequestMethod.GET)
    public ModelAndView cerrarSesion(ModelMap model,HttpServletRequest request){         
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("puestos", puesto_db.getPuestos(""));
        model.addAttribute("sesion", obtenerSesion(null));
        request.getSession().setAttribute("usuarioActual", null);
                model.addAttribute("puestos", puesto_db.getPuestos("%"));

        return new ModelAndView("PaginaInicioIH",model);
    }
    
    @RequestMapping(value="/usuarioSeleccionado", method = RequestMethod.GET)
    public ModelAndView usuarioSeleccionado(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        String idUS = request.getParameter("idUsuarioSeleccionado");
        System.out.println(idUS);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
                model.addAttribute("elimP", elimP(usuarioActual));
                model.addAttribute("elimC", elimC(usuarioActual));
                model.addAttribute("campos", campos(usuarioActual));
                if(usuarioActual!=null)
        model.addAttribute("nua", usuarioActual.getNombre());
        return verPuestoIH(model,request);   
    }
    
    @RequestMapping(value="/iniciarSesionIH", method = RequestMethod.GET)
    public ModelAndView iniciarSesionIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("IniciarSesionIH",model);
    }
    
    @RequestMapping(value="/iniciarSesion", method = RequestMethod.GET)
    public ModelAndView iniciarSesion(ModelMap model,HttpServletRequest request){
        String correo = request.getParameter("correo");                
        String password = request.getParameter("contra");
        System.out.println("Intentando iniciar sesión con: " + correo + "," + password);
        Usuario nuevoUsuario = usuario_db.getUsuario(correo, password);
        if (nuevoUsuario==null){
            model.addAttribute("info","error");
            return new ModelAndView("IniciarSesionIH",model);
        } String sesion = obtenerSesion(nuevoUsuario);
        model.addAttribute("sesion", sesion);
        request.getSession().setAttribute("usuarioActual", nuevoUsuario);
        model.addAttribute("puestos", puesto_db.getPuestos(""));
        return paginaInicialIH(model,request);    
    }
    
    @RequestMapping(value="/registroIH", method = RequestMethod.GET)
    public ModelAndView registroIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("RegistroIH",model);            
    }
    
    @RequestMapping(value="/registrar", method = RequestMethod.GET)
    public ModelAndView registrar(ModelMap model,HttpServletRequest request){
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");                
        String password = request.getParameter("contra");
        String carrera = request.getParameter("carrera");
        if (correo==null || nombre==null || password==null){
            model.addAttribute("info","Error, todos los datos deben llenarse");
            model.addAttribute("sesion", obtenerSesion(null));
            return new ModelAndView("RegistroIH",model);
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setPsswd(password);
        nuevoUsuario.setCorreo(correo);
        usuario_db.guardar(nuevoUsuario);
        model.addAttribute("sesion", obtenerSesion(nuevoUsuario));
        request.getSession().setAttribute("usuarioActual", nuevoUsuario);
        //mail
        Properties props = System.getProperties();
        String from = "tu_correo@correo.com";
        String pass = "tu contraseña";
        String host = "smtp.gmail.com";
        String[] to = {correo};
        String subject = "Registrado en El Ciencero.";
        String body = nombre+" ha sido registrado exitosamente en El Ciencero";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch(Exception me){
            me.printStackTrace();
        }
        
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/comentar", method = RequestMethod.GET)
    public ModelAndView comentar(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        Puesto puestoActual = (Puesto)request.getSession().getAttribute("puestoActual");

        String comentario = request.getParameter("comentario");
        if(comentario == null){
                    model.addAttribute("puestos", puesto_db.getPuestos("%"));

            return new ModelAndView("PaginaInicialIH", model);
        }
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        Comentario c = new Comentario();
        c.setComentario(comentario);
        c.setPuesto(puestoActual);
        c.setUsuario(usuarioActual);
        comentario_db.guardar(c);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/calificar", method = RequestMethod.GET)
    public ModelAndView calificar(ModelMap model,HttpServletRequest request){
        Puesto puestoActual = (Puesto)request.getSession().getAttribute("puestoActual");
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        if(usuarioActual == null){
            return paginaInicialIH(model,request);
        }
        String nuevaCalificacion = request.getParameter("nuevaCalificacion");
        
        int nC = 10;
        try{
            nC = Integer.parseInt(nuevaCalificacion);
            if(nC < 0)
                nC = 0;
            if(nC > 10)
                nC = 10;
        }catch(Exception e){
            
        }
        Calificacion c = new Calificacion();
        c.setEstrellas(nC);
        c.setPuesto(puestoActual);
        c.setUsuario(usuarioActual);
        imprimir(c.getEstrellas() + "<>" + c.getPuesto() + c.getUsuario());        
        calificacion_db.guardar(c);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/paginaInicialIH", method = RequestMethod.GET)
    public ModelAndView paginaInicialIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("puestos", puesto_db.getPuestos(""));
        String sesion = obtenerSesion(usuarioActual);                
        model.addAttribute("sesion", sesion);
        if (usuarioActual!=null){
            model.addAttribute("nombre", usuarioActual.getNombre());
            model.addAttribute("correo", usuarioActual.getCorreo());
        } else {
            model.addAttribute("nombre", "No hay sesión iniciada.");
        }        
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        model.addAttribute("puestos", puesto_db.getPuestos("%"));
        model.addAttribute("botones1", botones1(usuarioActual));
        return new ModelAndView("PaginaInicioIH",model);
    }    
    
    @RequestMapping(value="/verPuestoIH", method = RequestMethod.GET)
    public ModelAndView verPuestoIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        int id_puesto = Integer.parseInt(request.getParameter("idPuesto"));
        Puesto puestoActual = puesto_db.getPuesto(id_puesto);
        String comidas, nombrePuesto, ubicacion, descripcion, calificacion;
        nombrePuesto = puestoActual.getNombre();
        ubicacion = "(" + puestoActual.getPosX() + "," + puestoActual.getPosY() + ")";           
        List<Comentario> comentariosActuales = puestoActual.getComentarios_usuario();       
        List<String> des = puestoActual.getTags();
        calificacion = promedioLista(puestoActual.getCalificaciones_usuario()) + "";                
        if (!des.isEmpty()){
            descripcion = des.get(0);
        } else {
            descripcion = "No hay comidas registradas";
        } model.addAttribute("nombrePuesto", nombrePuesto);
        model.addAttribute("comidas",descripcion);
        model.addAttribute("ubicacion",ubicacion);
        model.addAttribute("descripcion",descripcion);
        model.addAttribute("calificacion",calificacion);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        request.getSession().setAttribute("comentariosActuales", comentariosActuales);
        request.getSession().setAttribute("puestoActual", puestoActual);
                model.addAttribute("elimP", elimP(usuarioActual));
                model.addAttribute("elimC", elimC(usuarioActual));
                model.addAttribute("campos", campos(usuarioActual));
                if(usuarioActual!=null)
        model.addAttribute("nua", usuarioActual.getNombre());
        return new ModelAndView("VerPuestoIH",model);
    }    
    
    @RequestMapping(value="/busquedaIH", method = RequestMethod.GET)
    public ModelAndView busquedaIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("BusquedaIH",model);    
    }
    
    @RequestMapping(value="/agregarPuestoIH", method = RequestMethod.GET)
    public ModelAndView agregarPuestoIH(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("AgregarPuestoIH",model);    
    }
    
    /*
    * resultadosBusqueda = Lista con los puestos de la busqueda
    * puestoActual = Puesto actual que seleccionó el usuario
    */
    
    @RequestMapping(value="/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        String solicitud_busqueda = request.getParameter("nombrePuesto");
        ArrayList<Puesto> resultadosBusqueda = puesto_db.getPuestos("%"+solicitud_busqueda+"%");  
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        request.getSession().setAttribute("busquedaActual", resultadosBusqueda);
        return resultadosBusqueda(model,request);
    }
    
    @RequestMapping(value="/resultadosBusqueda", method = RequestMethod.GET)
    public ModelAndView resultadosBusqueda(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");      
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("ResultadosBusqueda",model);    
    }
    
    @RequestMapping(value="/agregarPuesto", method = RequestMethod.GET)
    public ModelAndView agregarPuesto(ModelMap model,HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        // Se obtienen los atributos
        String comidas, nombrePuesto, ubicacionX, ubicacionY, descripcion;
        ubicacionX = request.getParameter("ubicacionX");
        ubicacionY = request.getParameter("ubicacionY");
        double cordX = 19.3234472;
        double cordY = -99.1818304;
        try{
            cordX = Double.parseDouble(ubicacionX);
            cordY = Double.parseDouble(ubicacionY);
        }catch(Exception e){
            
        }
        
        nombrePuesto = request.getParameter("nombrePuesto");
        comidas = request.getParameter("comidas");           
        descripcion = request.getParameter("descripcion"); 
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        List<Comentario> comentarios = new ArrayList<Comentario>();
        String[] spam = comidas.split(",");
        List <String> nuevasComidas = Arrays.asList(spam);
        // Se asignan los atributos al nuevo puesto
        Puesto nuevoPuesto = new Puesto();
        nuevoPuesto.setNombre(nombrePuesto);
        nuevoPuesto.setPosX(cordX);
        nuevoPuesto.setPosY(cordY);           
        nuevoPuesto.setComentarios_usuario(comentarios);
        nuevoPuesto.setCalificaciones_usuario(calificaciones);
        nuevoPuesto.setTags(nuevasComidas);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        request.getSession().setAttribute("usuarioActual", usuarioActual);
        System.out.println("Agregando al puesto:  " + nuevoPuesto.getNombre() + ",[" + nuevoPuesto.getPosX() + "_" + nuevoPuesto.getPosY());
        puesto_db.guardar(nuevoPuesto);
        model.addAttribute("puestos", puesto_db.getPuestos("%"));
                model.addAttribute("botones1", botones1(usuarioActual));

        return new ModelAndView("PaginaInicioIH",model);    
    }
    
    private float promedioLista(List<Calificacion> calificaciones){
        float resultado = 0;
        int ponderacion = 0;
        for (Calificacion c : calificaciones){
            resultado+=c.getEstrellas();
            ponderacion++;
        }
        System.out.println(resultado +" "+ponderacion);
        if(ponderacion==0)
            return 0;
        return resultado/(float)ponderacion;
    }
    
    private void imprimir(Object o){
        System.out.println(o.toString());        
    }
    
    public Usuario obtenerUsuario(HttpServletRequest request){
        return (Usuario)request.getAttribute("usuarioActual");        
    }
    
    public String campos(Usuario u){
        String s = "";
        if(u!=null){
            s = "<form method=\"GET\" action=\"/Clase1/comentar\">\n" +
"              <input id=\"nombre02\" name=\"comentario\" type=\"text\" placeholder=\"Comenta algo\" class=\"w3-border w3-padding-large\">              \n" +
"              <p>\n" +
"              <button class=\"w3-button w3-theme-l1 w3-left-align\"><i class=\"fa fa-users fa-fw w3-margin-right\"></i>Comentar</button>\n" +
"              </form>\n" +
"              <form method=\"GET\" action=\"/Clase1/calificar\">\n" +
"              <input id=\"nombre02\" name=\"nuevaCalificacion\" type=\"text\" placeholder=\"Califica este puesto (0/10)\" class=\"w3-border w3-padding-large\">\n" +
"              <p>              \n" +
"              <button class=\"w3-button w3-theme-l1 w3-left-align\"><i class=\"fa fa-users fa-fw w3-margin-right\"></i>Calificar</button>\n" +
"              </form>";
        }
        return s;
    }
    
    public String elimC(Usuario u){
        String s = "";
        if(u!=null && u.getUsuario_id()==1){
            s = "<form method=\"POST\" action=\"/Clase1/eliminarComentario\">\n" +
"                    <input id=\"comentarioId\" name=\"comentarioId\" type=\"hidden\" value=\"${c.getComentario_id()}\"/>\n" +
"                    <span class=\"w3-right w3-opacity\"><button class=\"w3-button w3-theme-l1 w3-left-align\">Eliminar Comentario</button></span>     \n" +
"                    <hr class=\"w3-clear\">\n" +
"                </form>";
            return s;
        }
        return "";
    }
    
    public String elimP(Usuario u){
        String s = "";
        if(u!=null && u.getUsuario_id()==1){
            s = "<form method=\"POST\" action=\"/Clase1/eliminarPuesto\">\n" +
"            <input id=\"PuestoId\" name=\"PuestoId\" type=\"hidden\" value=\"${nombrePuesto}\"/>\n" +
"            <span class=\"w3-right w3-opacity\"><button class=\"w3-button w3-theme-l1 w3-left-align\"></i>Eliminar Puesto</button></span>            \n" +
"        </form>";
            return s;
        }
        return "";
    }
    
    public String botones1(Usuario u){
        String s = "";
        if(u!=null && u.getUsuario_id() == 1){
            s = "<form method=\"GET\" action=\"/Clase1/agregarPuestoIH\">\n" +
"                                <button class=\"w3-button w3-block w3-theme-l1 w3-left-align\"><i class=\"fa fa-users fa-fw w3-margin-right\"></i><%out.println(\"Agregar Puesto\");%></button>\n" +
"                            </form>\n" +
"                            <div id=\"Demo1\" class=\"w3-hide w3-container\">\n" +
"                                <p>Para buscar el puesto</p>\n" +
"                            </div>\n" +
"                            <form method=\"POST\" action=\"/Clase1/eliminarUsuario\">\n" +
"                                \n" +
"                                <input id=\"correoElim\" name=\"correoElim\" type=\"text\" placeholder=\"correo@ciencias.unam.mx\"/>\n" +
"                                <button class=\"w3-button w3-block w3-theme-l1 w3-left-align\"><i class=\"fa fa-calendar-check-o fa-fw w3-margin-right\"></i>Eliminar Usuario</button>\n" +
"                            </form>";
            return s;
        }
        return "";
    }
    
    public String obtenerSesion(Usuario nuevoUsuario){       
        String sesion = "";
        if(nuevoUsuario!=null){
            sesion = "<form method=\"GET\" action=\"/Clase1/cerrarSesion\">\n" +
"                  <button class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" title=\"Notifications\">Cerrar Sesion</button>\n" +
"                </form>";
        } else {
            sesion = "<form method=\"GET\" action=\"/Clase1/iniciarSesionIH\">\n" +
"    <button class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" title=\"Notifications\">Iniciar Sesión</button> \n" +
"    </form>" + "<form method=\"GET\" action=\"/Clase1/registroIH\">\n" +
"    <button class=\"w3-bar-item w3-button w3-padding-large w3-theme-d4\" title=\"Notifications\">Registrarse</button> \n" +
"    </form>    ";
        }
        return sesion;
    } 
    
        
}
