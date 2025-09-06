# nome_do_app/urls.py

from django.urls import path
from .views import detalhes_utente, listar_utentes, listar_medicos,listar_familiares, listar_consultas, listar_medicamentos, criar_consulta, home,home_medico,home_utente, home_familiar, login_usuario, registar_usuario, detalhes_medico, detalhes_familiar, detalhes_consulta, criar_parametro, listar_parametro, detalhes_parametro, criar_exame, listar_exames, detalhes_exame, criar_prescricao, listar_prescricoes, detalhes_prescricao, estatisticas_view
from django.contrib.auth import views as auth_views

urlpatterns = [
    path("",home,name="home"),
    path("home_medico",home_medico,name="home_medico"),
    path("home_utente",home_utente,name="home_utente"),
    path("home_familiar",home_familiar,name="home_familiar"),
    
    path('utentes/', listar_utentes, name='listar_utentes'),
    path('utentes/<int:utente_id>/', detalhes_utente, name='detalhes_utente'),
    path('medicos/', listar_medicos, name='listar_medicos'),
    path('medicos/<int:medico_id>/', detalhes_medico, name='detalhes_medico'),
    path('familiares/', listar_familiares, name='listar_familiares'),
    path('familiares/<int:familiar_id>/', detalhes_familiar, name='detalhes_familiar'),
    path('consultas/', listar_consultas, name='listar_consultas'),
    path('consultas/<int:consulta_id>/', detalhes_consulta, name='detalhes_consulta'),
    path('medicamentos/', listar_medicamentos, name='listar_medicamentos'),
    
    path('criar_consulta/', criar_consulta, name='criar_consulta'),
    path('criar_parametro/', criar_parametro, name='criar_parametro'),
    path('parametros/', listar_parametro, name='listar_parametros'),
    path('parametros/<int:parametro_id>/', detalhes_parametro, name='detalhes_parametro'),
    path('criar_exame/', criar_exame, name='criar_exame'),
    path('exames/', listar_exames, name='listar_exames'),
    path('exames/<int:exame_id>/', detalhes_exame, name='detalhes_exame'),  
    path('criar_prescricao/', criar_prescricao, name='criar_prescricao'),
    path('prescricoes/', listar_prescricoes, name='listar_prescricoes'),
    path('prescricoes/<int:prescricao_id>/', detalhes_prescricao, name='detalhes_prescricao'),
    path('estatisticas/', estatisticas_view, name='estatisticas'),
    
    path('login/', login_usuario, name='login'),
    path('registar/', registar_usuario, name='registar'),
    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    
]