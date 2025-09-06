# forms.py
from django.utils import timezone
from typing import Any
from django.contrib.auth.forms import UserCreationForm
from django import forms
from .models import Utente, Medico, Familiar, Utilizador, Consulta, Prescricao, Exame, Parametros, Medicamento
from django.contrib.auth.forms import AuthenticationForm

class UtenteCreationForms(UserCreationForm):
    class Meta(UserCreationForm.Meta):
        model = Utente
        fields = ["nome","idade","sexo","tipo","numSaude","email","password1","password2"]
    
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        # Definir o valor do campo 'tipo' como 'U' ao inicializar o formulário
        
        self.fields["tipo"].initial = "U"
        self.fields["tipo"].widget = forms.HiddenInput()
        self.fields["tipo"].label = ""
        self.fields["numSaude"].label = "Número de Saúde"
        self.fields["password2"].label = "Confirmação de Password"


class MedicoCreationForms(UserCreationForm):
    class Meta(UserCreationForm.Meta):
        model = Medico
        fields = ["nome","idade","sexo","tipo","numSaude","email","especialidade","password1","password2"]
    
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
       
        self.fields["tipo"].initial = "M"
        self.fields["tipo"].widget = forms.HiddenInput()
        self.fields["tipo"].label = ""
        self.fields["numSaude"].label = "Número de Saúde"
        self.fields["password2"].label = "Confirmação de Password"

class FamiliarCreationForms(UserCreationForm):
    class Meta(UserCreationForm.Meta):
        model = Familiar
        fields = ["nome","idade","sexo","tipo","numSaude","email","relacao","id_associado","password1","password2"]
        widgets = {
            'id_associado': forms.Select(attrs={'class': 'form-control'}),
        }

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # Adiciona a lista de Utentes disponíveis ao campo 'id_associado'
        self.fields['id_associado'].queryset = Utente.objects.all()
        self.fields["id_associado"].label = "Utente associado"
        self.fields["tipo"].initial = "F"
        self.fields["tipo"].widget = forms.HiddenInput()
        self.fields["tipo"].label = ""
        self.fields["numSaude"].label = "Número de Saúde"
        self.fields["password2"].label = "Confirmação de Password"

class UtenteLoginForm(forms.Form):
    email = forms.EmailField(label="Email")
    password = forms.CharField(label="Password", strip=False, widget=forms.PasswordInput)

class MedicoLoginForm(forms.Form):
    email = forms.EmailField(label="Email")
    password = forms.CharField(label="Password", strip=False, widget=forms.PasswordInput)
    
class FamiliarLoginForm(forms.Form):
    email = forms.EmailField(label="Email")
    password = forms.CharField(label="Password", strip=False, widget=forms.PasswordInput)

class PrescricaoCreationForm(forms.ModelForm):
    
    class Meta:
        model = Prescricao
        fields = ["utente","medicamento","data_prescricao","data_termino","tomas","consulta"]
    
    def __init__(self,user, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        # Se o usuário for um médico, mostrar apenas os utentes associados a ele
        if user.tipo == 'M':
            self.fields["utente"].queryset = Utente.objects.filter(consulta__medico=user).distinct()
            self.fields["consulta"].queryset = Consulta.objects.filter(medico=user)
        else:
            self.fields["utente"].queryset = Utente.objects.all()

        self.fields['utente'].queryset = Utente.objects.all()
        self.fields["medicamento"].queryset = Medicamento.objects.all()
        self.fields["data_prescricao"].label = "Data da prescrição"
        self.fields["data_prescricao"].inicial = timezone.now()
        self.fields["data_termino"].label = "Data de término"
        self.fields["data_termino"].inicial = timezone.now()


class ConsultaCreationForm(forms.ModelForm):
    
    class Meta:
        model = Consulta
        fields = ["utente","medico","prescricoes","exames","data"]
    
    def __init__(self, user, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # Adiciona a lista de Utentes disponíveis ao campo 'id_associado'
        self.fields['utente'].queryset = Utente.objects.all()
        self.fields["medico"].queryset = Medico.objects.filter(email=user.email)
        self.fields["data"].inicial = timezone.now
        self.fields["prescricoes"].widget = forms.HiddenInput()
        self.fields["prescricoes"].label = ""
        self.fields["exames"].widget = forms.HiddenInput()
        self.fields["exames"].label = ""

class ExameCreationForm(forms.ModelForm):
    
    class Meta:
        model = Exame
        fields = ["utente","tipo_exames","consulta","data"]
    
    def __init__(self,user, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        if user.tipo == 'M':
            self.fields["utente"].queryset = Utente.objects.filter(consulta__medico=user).distinct()
            self.fields["consulta"].queryset = Consulta.objects.filter(medico=user)
        else:
            self.fields["utente"].queryset = Utente.objects.all()
        # Adiciona a lista de Utentes disponíveis ao campo 'id_associado'
        self.fields['utente'].queryset = Utente.objects.all()
        self.fields["tipo_exames"].label = "Tipo de Exame"
        self.fields["data"].inicial = timezone.now

class ParametrosCreationForm(forms.ModelForm):
    
    class Meta:
        model = Parametros
        fields = ["utente","peso","colesterol","bpm","pressao","altura","temperatura","consulta","data"]
    
    def __init__(self,user, *args, **kwargs):
        super().__init__(*args, **kwargs)
        
        if user.tipo == 'M':
            self.fields["utente"].queryset = Utente.objects.filter(consulta__medico=user).distinct()
            self.fields["consulta"].queryset = Consulta.objects.filter(medico=user)
        else:
            self.fields["utente"].queryset = Utente.objects.all()
            
        # Adiciona a lista de Utentes disponíveis ao campo 'id_associado'
        self.fields['utente'].queryset = Utente.objects.all()
        self.fields["data"].inicial = timezone.now
    
