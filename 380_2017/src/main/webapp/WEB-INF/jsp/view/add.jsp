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

        <h2>New Post</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="ticketForm">
            <form:label path="subject">Title</form:label><br/>
            <form:input type="text" path="subject" /><br/><br/>
            <form:label path="body">Contents</form:label><br/>
            <form:textarea path="body" rows="5" cols="30" /><br/><br/>
            <form:radiobutton path="categories" value="lecture" />Lecture
            <form:radiobutton path="categories" value="lab" />Lab
            <form:radiobutton path="categories" value="other" />Other<br/><br/>
            <b>Files</b><br/>
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Submit"/>
        </form:form>
    </body>
</html>