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

        <h2>Post #${ticketId}: <c:out value="${ticket.subject}" /></h2>
        <security:authorize access="hasRole('ADMIN') or principal.username=='${ticket.customerName}'">            
            [<a href="<c:url value="/ticket/edit/${ticketId}" />">Edit</a>]
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')">            
            [<a href="<c:url value="/ticket/delete/${ticketId}" />">Delete</a>]
        </security:authorize>
        <br /><br />
        <i>Post by - <c:out value="${ticket.customerName}" /></i><br /><br />
        Categories: <c:out value="${ticket.categories}"/><br /><br />
        <c:out value="${ticket.body}" /><br /><br />
        <c:if test="${ticket.numberOfAttachments > 0}">
            File(s):
            <c:forEach items="${ticket.attachments}" var="attachment"
                       varStatus="status">
                <c:if test="${!status.first}">, </c:if>
                <a href="<c:url value="/ticket/${ticketId}/attachment/${attachment.name}" />">
                    <c:out value="${attachment.name}" /></a>       
            </c:forEach><br /><br />
        </c:if>
            
            <c:choose>
            <c:when test="${fn:length(selectedReply) == 0}">
                <i>There are no reply in the system.</i>
            </c:when>
            <c:otherwise>
 
                <c:forEach items="${selectedReply}" var="entry">
                    replyid <c:out value="${entry.key}"/>
                    
                    (reply cotent: <c:out value="${entry.value.replybody}" />)
                  
                    <br /><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
         <a href="<c:url value="/reply/${ticketId}" />">Reply</a>         
        <a href="<c:url value="/ticket" />">Return to list tickets</a>
    </body>
</html>