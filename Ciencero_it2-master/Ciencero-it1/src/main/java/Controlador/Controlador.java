package Controlador;

import Mapeo.*;
import Modelo.*;
import java.sql.Date;
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
        String pid = request.getParameter("nombrePuesto");
        Puesto p = puesto_db.getPuestos(pid).get(0);
        puesto_db.eliminar(p);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/eliminarComentario", method=RequestMethod.POST)
    public ModelAndView eliminarComentario(ModelMap model, HttpServletRequest request){
        String cid = request.getParameter("comentarioId");
        int id = Integer.parseInt(cid);
        System.out.println(" "+cid);
        Comentario c = comentario_db.getComentario(id);
        comentario_db.eliminar(c);
        return paginaInicialIH(model,request);
    }
    
    @RequestMapping(value="/irPuesto", method=RequestMethod.POST)
    public ModelAndView irPuesto(ModelMap model, HttpServletRequest request){
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        int id_puesto = Integer.parseInt(request.getParameter("irId"));
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
    
    /*@RequestMapping(value="/")
    public ModelAndView inicio(){
        ModelMap model = new ModelMap();
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Mark");
        String sesion = obtenerSesion(nuevoUsuario);
        ArrayList<Puesto> puestos = new ArrayList<Puesto>();
        Puesto p1 = new Puesto();
        Puesto p2 = new Puesto();
        p1.setNombre("Amm");
        p2.setNombre("Meh");
        puestos.add(p1);
        puestos.add(p2);
        model.addAttribute("puestosPrueba", puestos);
        model.addAttribute("sesion", sesion);
        model.addAttribute("usuarioActual", nuevoUsuario);
        return new ModelAndView("Prueba", model);
    }
    
    @RequestMapping(value="/matarUsuario", method = RequestMethod.GET)
    public ModelAndView matarUsuario(ModelMap model,HttpServletRequest request){
        Usuario nuevoUsuario = new Usuario();
        ArrayList<Puesto> puestos = new ArrayList<Puesto>();
        Puesto p1 = new Puesto();
        Puesto p2 = new Puesto();
        p1.setNombre("Amm");
        p2.setNombre("Meh");
        p1.setPuesto_id(2);
        p2.setPuesto_id(1);
        puestos.add(p1);
        puestos.add(p2);
        nuevoUsuario.setNombre("Mark");
        System.out.println(nuevoUsuario);
        request.getSession().setAttribute("usuarioActual", nuevoUsuario);
        request.getSession().setAttribute("puestosPrueba", puestos);
        model.addAttribute("sesion", obtenerSesion(nuevoUsuario));
        model.addAttribute("usuarioActual", nuevoUsuario);
        return new ModelAndView("Prueba2",model);
    }
    
    /*@RequestMapping(value="/generarUsuario", method = RequestMethod.GET)
    public ModelAndView generarUsuario(ModelMap model,HttpServletRequest request){        
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        System.out.println(usuarioActual.getNombre());
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        model.addAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("Prueba3",model);
    }
    
    @RequestMapping(value="/imprimirPuesto", method = RequestMethod.GET)
    public ModelAndView imprimirPuesto(ModelMap model,HttpServletRequest request){        
        ArrayList<Puesto> puestos = (ArrayList<Puesto>)request.getSession().getAttribute("puestosPrueba");
        Usuario usuarioActual = (Usuario)request.getSession().getAttribute("usuarioActual");
        System.out.println(usuarioActual.getNombre());
        for (Puesto p : puestos){
            System.out.println(p.getNombre());
        }
        String puestoActualID = request.getParameter("idPuesto");
        System.out.println("Id:" + puestoActualID);
        model.addAttribute("sesion", obtenerSesion(usuarioActual));
        model.addAttribute("usuarioActual", usuarioActual);
        return new ModelAndView("Prueba2",model);
    }*/
    
}
