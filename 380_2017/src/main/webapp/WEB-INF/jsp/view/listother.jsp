<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>All Post</h2>
        
        <security:authorize access="hasRole('ADMIN')">    
            <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
        </security:authorize>
        <a href="<c:url value="/ticket/create" />">New Post</a>  lecture
        lab
        other
        <br /><br />
       
        <c:choose>
            <c:when test="${fn:length(ticketDatabase) == 0}">
                <i>0 Post.</i>
            </c:when>
            <c:otherwise>

                       
                       <c:forEach items="${ticketDatabase}" var="entry4">
                            <c:if test="${entry4.value.categories == 'other'}" >only other<br>
                              Post #${entry4.key}:
                              <a href="<c:url value="/ticket/view/${entry4.key}" />"> 
                              <c:out value="${entry4.value.subject}" /></a>
                              (Post by: <c:out value="${entry4.value.customerName}" />)
                            </c:if>
                       </c:forEach>
                       
                       
                       
             
                      
                      
    
                    <security:authorize access="hasRole('ADMIN') or principal.username=='${entry.value.customerName}'">            
                        [<a href="<c:url value="/ticket/edit/${entry.key}" />">Edit</a>]
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">            
                        [<a href="<c:url value="/ticket/delete/${entry.key}" />">Delete</a>]
                    </security:authorize>
                    <br /><br />
                
            </c:otherwise>
        </c:choose>
    </body>
</html>