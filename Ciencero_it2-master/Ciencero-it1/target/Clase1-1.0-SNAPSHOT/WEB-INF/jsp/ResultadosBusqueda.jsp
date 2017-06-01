
<%-- 
    Document   : inicio
    Created on : 20/02/2017, 11:08:36 PM
    Author     : Julio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Mapeo.Usuario" %>
<%@page import="Mapeo.Puesto" %>
<%@page import="Mapeo.Comentario" %>
<%@page import="Mapeo.Calificacion" %>
<%@page import="Controlador.Controlador" %>
<%@page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!DOCTYPE html>
<html>
<title>Resultados de la Busqueda</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-blue-grey.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
html,body,h1,h2,h3,h4,h5 {font-family: "Open Sans", sans-serif}
</style>
<body class="w3-theme-l5">

<!-- Navbar -->
<div class="w3-top">
 <div class="w3-bar w3-theme-d2 w3-left-align w3-large">
  <form method="GET" action="/Clase1/paginaInicialIH">
  <button class="w3-bar-item w3-button w3-padding-large w3-theme-d4"><i class="fa fa-home w3-margin-right"></i>Inicio</button>
  </form>
    <!-- Nos redirige a la pagina principal-->
    ${sesion}
  </div>
 </div>

<!-- Navbar on small screens -->
<div id="navDemo" class="w3-bar-block w3-theme-d2 w3-hide w3-hide-large w3-hide-medium w3-large">
  <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 1</a>
  <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 2</a>
  <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 3</a>
  <a href="#" class="w3-bar-item w3-button w3-padding-large">My Profile</a>
</div>

<!-- Page Container -->
<div class="w3-container w3-content" style="max-width:1400px;margin-top:80px">    
  <!-- The Grid -->
  <div class="w3-row">
    <!-- Left Column -->
    <div class="w3-col m3">     
    
    <!-- End Left Column -->
    </div>
        
     <div class="w3-container w3-card-2 w3-white w3-round w3-margin"><br>
         <c:forEach var="s" items="${busquedaActual}" varStatus="status">                        
            <form method="GET" action="/Clase1/verPuestoIH">                            
            <h4> ${s.getNombre()} </h4>
            <input type="text" name="idPuesto" style="visibility: hidden" readonly value="${s.getPuesto_id()}"/>
            <span class="w3-right w3-opacity"><button class="w3-button w3-theme-l1 w3-left-align">Ver Puesto</button></span>     
            <hr class="w3-clear">
            </form>  
        </c:forEach>
      </div>
      
    <!-- End Middle Column -->
    </div>
    
    <!-- Right Column -->
    
    
  <!-- End Grid -->
  </div>
  
<!-- End Page Container -->
</div>
<br>

<!-- Footer -->
<footer class="w3-container w3-theme-d3 w3-padding-16">
  <h5>Pie de Página
  Facultad de Ciencias</h5>
</footer>

<footer class="w3-container w3-theme-d5">
  <p>CRONOWARE</p>
</footer>
 
<script>
// Accordion
function myFunction(id) {
    var x = document.getElementById(id);
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
        x.previousElementSibling.className += " w3-theme-d1";
    } else { 
        x.className = x.className.replace("w3-show", "");
        x.previousElementSibling.className = 
        x.previousElementSibling.className.replace(" w3-theme-d1", "");
    }
}

// Used to toggle the menu on smaller screens when clicking on the menu button
function openNav() {
    var x = document.getElementById("navDemo");
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
    } else { 
        x.className = x.className.replace(" w3-show", "");
    }
}
</script>

</body>
</html> 
