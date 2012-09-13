<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.4" 
	xmlns="http://java.sun.com/xml/ns/j2ee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">

    <!-- Seam -->
    
    <listener>
        <listener-class>org.jboss.seam.servlet.SeamListener</listener-class>
    </listener>

    <filter>
        <filter-name>Seam Exception Filter</filter-name>
        <filter-class>org.jboss.seam.servlet.SeamExceptionFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>Seam Exception Filter</filter-name>
        <url-pattern>*.jsf</url-pattern>
    </filter-mapping>


    <!-- MyFaces -->
    
    <listener>
        <listener-class>org.apache.myfaces.webapp.StartupServletContextListener</listener-class>
    </listener>
    
    <context-param>
        <param-name>javax.faces.STATE_SAVING_METHOD</param-name>
        <param-value>client</param-value>
    </context-param>

	<servlet>
		<servlet-name>Faces Servlet</servlet-name>
		<servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>Faces Servlet</servlet-name>
		<url-pattern>*.jsf</url-pattern>
	</servlet-mapping>
	
</web-app>