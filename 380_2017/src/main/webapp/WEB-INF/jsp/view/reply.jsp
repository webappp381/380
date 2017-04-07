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

        <h2>Reply</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="replyForm">
            <form:label path="replybody">Reply Body</form:label><br/>
            <form:textarea type="text" path="replybody" rows="5" cols="30" /><br/><br/>
            <b>Attachments</b><br/>
            <input type="file" name="replyattachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>