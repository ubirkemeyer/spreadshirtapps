<span class="menuButton"><a class="neutral" href="${createLink(uri: '/')}"><g:message code="default.export.label"/></a></span>

<sec:ifLoggedIn>
  <span class="menuButton"><a class="neutral" href="${createLink(controller: 'amazonProductType')}"><g:message code="default.amazonProductType.label"/></a></span>
  <span class="menuButton"><a class="neutral" href="${createLink(controller: 'amazonColorMapping')}"><g:message code="default.amazonColorMapping.label"/></a></span>
  <span class="menuButton"><a class="neutral" href="${createLink(controller: 'logout')}"><g:message code="default.logout.label"/></a></span>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
  <span class="menuButton"><a class="neutral" href="${createLink(controller: 'login')}"><g:message code="default.login.label"/></a></span>
</sec:ifNotLoggedIn>