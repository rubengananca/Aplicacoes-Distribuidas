# utils/auth_backends.py

from django.contrib.auth.backends import ModelBackend
from ..models import Utente, Medico, Familiar

class TipoEntidadeBackend(ModelBackend):
    def authenticate(self, request, email=None, password=None, **kwargs):
        user = None

        # Tente autenticar o usuário como Utente
        utente = Utente.objects.filter(email=email).first()
        if utente and utente.check_password(password):
            user = utente

        # Se não for Utente, tente como Médico
        if not user:
            medico = Medico.objects.filter(email=email).first()
            if medico and medico.check_password(password):
                user = medico

        # Se ainda não for, tente como Familiar
        if not user:
            familiar = Familiar.objects.filter(email=email).first()
            if familiar and familiar.check_password(password):
                user = familiar

        return user
