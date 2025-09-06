from django.contrib import admin

# Register your models here.

from .models import Utilizador, Utente, Medico, Familiar, Consulta, Medicamento, Prescricao, Exame, Parametros

admin.site.register(Utilizador)
admin.site.register(Utente)
admin.site.register(Medico)
admin.site.register(Familiar)
admin.site.register(Consulta)
admin.site.register(Medicamento)
admin.site.register(Prescricao)
admin.site.register(Exame)
admin.site.register(Parametros)