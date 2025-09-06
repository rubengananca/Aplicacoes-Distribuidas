from django.http import HttpResponse
from django.shortcuts import redirect
from django.contrib.auth import logout

#Vai impedir um utilizador de ir a pagina de login e registo
# um decorator é uma função que toma outra como argumento e deixa adicionar uma funcionalidade extra antes da função original ser chamada
def unauthenticated_user(views_func):
    def wrapper_func(request, *args, **kwargs):
        if request.user.is_authenticated:
            return redirect("listar_utentes")
        else:
            return views_func(request, *args, **kwargs)
    
    return wrapper_func
    