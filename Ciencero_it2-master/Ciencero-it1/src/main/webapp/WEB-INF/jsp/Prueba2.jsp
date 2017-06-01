<%-- 
    Document   : inicio
    Created on : 20/02/2017, 11:08:36 PM
    Author     : Julio
--%>



<!DOCTYPE html>
<!DOCTYPE html>
<html>
<title>Registrarse</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-blue-grey.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="Mapeo.*"%>
<%@page import="Controlador.Controlador" %>
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
  
    <!-- Left Column -->
    
      <!-- Profile -->
      <br>
      
      <c:if test="${usuario.nombre} != null">
          ${usuario.nombre}
      </c:if>
      
     <form method="GET" action="/Clase1/generarUsuario">
                    <input id="nombre01" name="nombre" type="text" placeholder="Nombre" class="w3-border w3-padding-large">
                    <p>
                    <input id="nombre02" name="correo" type="text" placeholder="Correo" class="w3-border w3-padding-large">
                    <p>
                    <input id="nombre01" name="contra" type="password" placeholder="Contraseña" class="w3-border w3-padding-large">
                    <p>
                    <input id="nombre02" name="fecha" type="date" placeholder="Fecha DD/MM/AAAA" class="w3-border w3-padding-large">
                    <p>
                    <input id="nombre02" name="carrera" type="text" placeholder="Carrera" class="w3-border w3-padding-large">
                    <p>
                    <input id="nombre02" name="foto" type="file" placeholder="Carrera" class="w3-border w3-padding-large">
                    <p>
                    <button class="w3-button w3-theme-l1 w3-left-align w3-padding-large"><i class="fa fa-calendar-check-o fa-fw w3-margin-right"></i>Registrarse    </button>
              </form>
      
      <!-- Alert Box -->
    
    <!-- End Left Column -->
    
    
    <!-- Middle Column -->
    <div class="w3-col m7">
      
    <!-- End Middle Column -->
    </div>
    
    <!-- Right Column -->
    <div class="w3-col m2">
      
    <!-- End Right Column -->
    </div>
    
  <!-- End Grid -->
  
  
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
