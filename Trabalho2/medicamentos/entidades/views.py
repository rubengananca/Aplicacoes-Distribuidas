from django.utils import timezone
from django.shortcuts import get_object_or_404, redirect, render
from django.contrib.auth.forms import UserCreationForm
from django.contrib import messages
from entidades.decorators import unauthenticated_user
from entidades.forms import UtenteCreationForms, MedicoCreationForms, FamiliarCreationForms, UtenteLoginForm, MedicoLoginForm, FamiliarLoginForm, ConsultaCreationForm, ParametrosCreationForm, ExameCreationForm, PrescricaoCreationForm
from django.contrib.auth import authenticate,login,logout,get_user_model
from django.contrib.auth.decorators import login_required
from .models import Utente, Medico, Consulta, Medicamento, Familiar,Parametros, Exame,Prescricao, Utilizador
from django.contrib.auth.decorators import user_passes_test
from django.db.models import Count



Utilizador= get_user_model()


def is_utente(user):
    return user.tipo == 'U'

def is_medico(user):
    return user.tipo == 'M'

def is_familiar(user):
    return user.tipo == 'F'


@user_passes_test(is_medico, login_url='login') 
def listar_utentes(request):
    utentes = Utente.objects.all()
    return render(request, 'listar_utentes.html', {'utentes': utentes})


@user_passes_test(is_medico, login_url='login') 
def listar_medicos(request):
    medicos = Medico.objects.all()
    return render(request, 'listar_medicos.html', {'medicos': medicos})

 
@user_passes_test(is_medico, login_url='login') 
def listar_familiares(request):
    familiares = Familiar.objects.all()
    return render(request, 'listar_familiares.html', {'familiares': familiares})

        
@login_required(login_url="login") 
def listar_consultas(request):
    user = request.user

    if user.is_authenticated:
        now = timezone.now().date()


        if user.tipo == 'U':
            consultas_passadas = Consulta.objects.filter(utente=user.utente, data__date__lt=now)
            consultas_futuras = Consulta.objects.filter(utente=user.utente, data__date__gt=now)
        elif user.tipo == 'M':
            consultas_passadas = Consulta.objects.filter(medico=user.medico, data__date__lt=now)
            consultas_futuras = Consulta.objects.filter(medico=user.medico, data__date__gt=now)
        elif user.tipo == "F":
            
            consultas_passadas = Consulta.objects.filter(utente = user.familiar.id_associado, data__date__lt=now)
            consultas_futuras = Consulta.objects.filter(utente = user.familiar.id_associado, data__date__gt=now)
        else:
            # Adicione um comportamento padrão ou retorne listas vazias para outros tipos de usuários
            consultas_passadas = []
            consultas_futuras = []

        return render(request, 'listar_consultas.html', {'consultas_passadas': consultas_passadas, 'consultas_futuras': consultas_futuras})
    else:
        # Caso o usuário não esteja autenticado, você pode redirecioná-lo para a página de login
        return redirect('listar_utentes')
 
@user_passes_test(is_medico, login_url='login') 
def listar_medicamentos(request):
    medicamentos = Medicamento.objects.all()
    return render(request, 'listar_medicamentos.html', {'medicamentos': medicamentos})

@user_passes_test(is_medico, login_url='login') 
def criar_consulta(request):
    
    if request.method == "POST":
        user = request.user
        form = ConsultaCreationForm(user,request.POST)
        if form.is_valid():
            consulta = form.save()
            return redirect('home_medico')  # Redireciona para uma página de sucesso após a criação da consulta
    else:
        user = request.user
        form = ConsultaCreationForm(user)
    medico_id = request.user.id
    return render(request, 'criar_consulta.html', {'form': form,"medico_id":medico_id})


@user_passes_test(is_medico, login_url='login') 
def criar_parametro(request):
    
    if request.method == "POST":
        user = request.user
        form = ParametrosCreationForm(user,request.POST)
        if form.is_valid():
            parametro = form.save()
            
            consulta_associada = parametro.consulta
            
            consulta_associada.parametros.add(parametro)
            
            return redirect('home_medico')  # Redireciona para uma página de sucesso após a criação da consulta
    else:
        user = request.user
        form = ParametrosCreationForm(user)
    medico_id = request.user.id
    return render(request, 'criar_parametro.html', {'form': form,"medico_id":medico_id})

@login_required(login_url="login") 
def listar_parametro(request):
    user = request.user

    if user.is_authenticated:
        if user.tipo == 'U':
            parametros = Parametros.objects.filter(utente=user.utente)
        elif user.tipo == 'M':
            parametros = Parametros.objects.all()
        elif user.tipo == "F":
            parametros = Parametros.objects.filter(utente = user.familiar.id_associado)
        else:
            parametros = []

        return render(request, 'listar_parametros.html', {'parametros': parametros})
    else:
        # Caso o usuário não esteja autenticado, você pode redirecioná-lo para a página de login
        return redirect('listar_utentes')
    

@user_passes_test(is_medico, login_url='login') 
def criar_exame(request):
    
    if request.method == "POST":
        user = request.user
        form = ExameCreationForm(user,request.POST)
        if form.is_valid():
            exame = form.save()
            
            consulta_associada = exame.consulta
            
            consulta_associada.exames.add(exame)
            
            return redirect('home_medico')  # Redireciona para uma página de sucesso após a criação da consulta
    else:
        user = request.user
        form = ExameCreationForm(user)
    medico_id = request.user.id
    return render(request, 'criar_exame.html', {'form': form,"medico_id":medico_id})

@login_required(login_url="login") 
def listar_exames(request):
    user = request.user

    if user.is_authenticated:
        if user.tipo == 'U':
            exames = Exame.objects.filter(utente=user.utente)
        elif user.tipo == "M":
            exames = Exame.objects.all()
        elif user.tipo == "F":
            exames = Exame.objects.filter(utente = user.familiar.id_associado)
        else:
            exames = []

        return render(request, 'listar_exames.html', {'exames': exames})
    else:
        # Caso o usuário não esteja autenticado, você pode redirecioná-lo para a página de login
        return redirect('listar_utentes')
    

@user_passes_test(is_medico, login_url='login') 
def criar_prescricao(request):
    
    if request.method == "POST":
        user = request.user
        form = PrescricaoCreationForm(user,request.POST)
        if form.is_valid():
            prescricao = form.save()
            
            # Obtenha a consulta associada à prescrição
            consulta_associada = prescricao.consulta

            # Adicione a prescrição à lista de prescrições na consulta
            consulta_associada.prescricoes.add(prescricao)
                        
            return redirect('home_medico')  # Redireciona para uma página de sucesso após a criação da consulta
    else:
        user = request.user
        form = PrescricaoCreationForm(user)
    medico_id = request.user.id
    return render(request, 'criar_prescricao.html', {'form': form,"medico_id":medico_id})

@login_required(login_url="login") 
def listar_prescricoes(request):
    user = request.user

    if user.is_authenticated:
        if user.tipo == 'U':
            prescricoes = Prescricao.objects.filter(utente=user.utente)
        elif user.tipo == "M":
            prescricoes = Prescricao.objects.all()
        elif user.tipo == "F":
            prescricoes = Prescricao.objects.filter(utente = user.familiar.id_associado)
        else:
            prescricoes = []

        return render(request, 'listar_prescricoes.html', {'prescricoes': prescricoes})
    else:
        # Caso o usuário não esteja autenticado, você pode redirecioná-lo para a página de login
        return redirect('listar_utentes')

@login_required(login_url="login") 
def detalhes_utente(request, utente_id):
    utente = get_object_or_404(Utente, pk=utente_id)
    return render(request, 'detalhes_utente.html', {'utente': utente})

@user_passes_test(is_medico, login_url='login') 
def detalhes_medico(request, medico_id):
    medico = get_object_or_404(Medico, pk=medico_id)
    return render(request, 'detalhes_medico.html', {'medico': medico})

@login_required(login_url="login") 
def detalhes_familiar(request, familiar_id):
    familiar = get_object_or_404(Familiar, pk=familiar_id)
    return render(request, 'detalhes_familiar.html', {'familiar': familiar})  

@login_required(login_url="login")
def detalhes_consulta(request, consulta_id):
    consulta = get_object_or_404(Consulta, pk=consulta_id)
    return render(request, 'detalhes_consulta.html', {'consulta': consulta}) 

@login_required(login_url="login")
def detalhes_parametro(request, parametro_id):
    parametro = get_object_or_404(Parametros, pk=parametro_id)
    return render(request, 'detalhes_parametro.html', {'parametro': parametro}) 

@login_required(login_url="login")
def detalhes_exame(request, exame_id):
    exame = get_object_or_404(Exame, pk=exame_id)
    return render(request, 'detalhes_exame.html', {'exame': exame}) 

@login_required(login_url="login")
def detalhes_prescricao(request, prescricao_id):
    prescricao = get_object_or_404(Prescricao, pk=prescricao_id)
    return render(request, 'detalhes_prescricao.html', {'prescricao': prescricao}) 


def login_usuario(request):
    if request.user.is_authenticated:
        logout(request)
        
    tipo_utilizador = request.POST.get("tipo") or request.GET.get("tipo")
    form_mapping = {"U": UtenteLoginForm, "M": MedicoLoginForm, "F": FamiliarLoginForm}
    
    form_class = form_mapping.get(tipo_utilizador)
    form = form_class(request.POST) if form_class else None

    if request.method == "POST":
        if form.is_valid():
            email = form.cleaned_data.get("email")
            password = form.cleaned_data.get("password")
            
            user = authenticate(request, email=email, password=password)

            if user is not None:
                login(request, user)
                
                if isinstance(form,MedicoLoginForm):
                    return redirect("home_medico")
                elif isinstance(form,UtenteLoginForm):
                    return redirect("home_utente")
                elif isinstance(form,FamiliarLoginForm):
                    return redirect("home_familiar")

    context = {"form": form}
    return render(request, "login.html", context)

def registar_usuario(request):
    if request.user.is_authenticated:
        logout(request)

    tipo_utilizador = request.POST.get("tipo") or request.GET.get("tipo")
    form_mapping = {"U": UtenteCreationForms, "M": MedicoCreationForms, "F": FamiliarCreationForms}
    
    form_class = form_mapping.get(tipo_utilizador)
    form = form_class(request.POST) if form_class else None
    
    if request.method == "POST" and form and form.is_valid():
        user = form.save(commit = False)
        user.username = user.email
        user.set_password(form.cleaned_data["password1"])
        user.save()
        
        return redirect("login")
            
    context = {"form":form}
    return render(request, "registar.html",context)

def estatisticas():
    
    # Consulta para obter o médico que deu mais consultas
    medico_mais_consultas = Medico.objects.annotate(num_consultas=Count('consulta')).order_by('-num_consultas')[:5]

    # Consulta para obter a quantidade total de consultas dadas por todos os médicos
    total_consultas = Medico.objects.aggregate(total_consultas=Count('consulta'))
    
    medicamentos_mais_prescritos = Medicamento.objects.annotate(num_prescricoes=Count('prescricao')).order_by('-num_prescricoes')[:5]
    
    exames_mais_prescritos = Exame.objects.values('tipo_exames').annotate(num_exames=Count('tipo_exames')).order_by('-num_exames')[:5]

    return {
        'medico_mais_consultas': medico_mais_consultas,
        'total_consultas': total_consultas['total_consultas'],
        'medicamentos_mais_prescritos': medicamentos_mais_prescritos,
        'exames_mais_prescritos': exames_mais_prescritos,
    }

@user_passes_test(is_medico, login_url='login') 
def estatisticas_view(request):
    medico_id = request.user.medico.id
    estatisticas_data = estatisticas()
    return render(request, 'estatisticas.html', {'estatisticas': estatisticas_data, 'medico_id':medico_id,})


def home(request):
    return render(request,"home.html")


@user_passes_test(is_medico, login_url='login') 
def home_medico(request):
    nome_utilizador = request.user.nome
    medico_id = request.user.medico.id
    return render(request, 'home_medico.html', {'nome_utilizador': nome_utilizador,'medico_id':medico_id})


@user_passes_test(is_utente, login_url='login') 
def home_utente(request):
    nome_utilizador = request.user.nome
    utente_id = request.user.utente.id
    return render(request, 'home_utente.html', {'nome_utilizador': nome_utilizador,'utente_id':utente_id})

@user_passes_test(is_familiar, login_url='login') 
def home_familiar(request):
    familiar =Familiar.objects.get(email=request.user)
    utente_associado = familiar.id_associado
    nome_utente = utente_associado.nome
    return render(request, 'home_familiar.html', {'utente_associado': utente_associado,"familiar":familiar,"nome_utente":nome_utente})