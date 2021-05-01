<#assign
    known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isClient = user.isClient()
        isManager = user.isManager()
        isLibrarian = user.isLibrarian()
        isDirector = user.isDirector()
        isEmployee = isLibrarian || isManager || isDirector
        currentUserId = user.getId()
    >
<#else>
    <#assign
        name = "unknown"
        isClient = false
        isManager = false
        isLibrarian = false
        isDirector = false
        isEmployee = false
        currentUserId = -1
    >
</#if>
