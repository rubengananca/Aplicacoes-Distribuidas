from django.utils import timezone
from django.db import models
from django.contrib.auth.models import AbstractUser, Group, Permission
from django.core.validators import MinLengthValidator, MaxLengthValidator

from django.urls import reverse

class Utilizador(AbstractUser):
    TIPO_CHOICES = [
       ('M', 'Médico'),
       ('U', 'Utente'),
       ('F', 'Familiar'),
    ]
    
    nome = models.CharField(max_length=200, unique=False)
    idade = models.IntegerField(default=18)
    sexo = models.CharField(max_length=10, choices=[("M", "Masculino"), ("F", "Feminino")]) 
    email = models.EmailField(max_length=50, unique=True)
    numSaude = models.CharField(max_length=9, unique=True, validators=[MinLengthValidator(limit_value=9), MaxLengthValidator(limit_value=9)])
    tipo = models.CharField(max_length=1, choices=TIPO_CHOICES)

    USERNAME_FIELD = "email"
    REQUIRED_FIELDS = ["username"]

    groups = models.ManyToManyField(Group, related_name='utilizador_groups')
    user_permissions = models.ManyToManyField(Permission, related_name='utilizador_user_permissions')

class Medico(Utilizador):

    TIPO_ESPECIALIDADES = [
        ("C","Cardiologia"),
        ("P", "Pediatria"),
        ("Ps","Psiquiatria"),
        ("CG","Clínica Geral"),
        ("N","Neurologia"),        
    ]
    especialidade = models.CharField(max_length=2, choices= TIPO_ESPECIALIDADES)

class Utente(Utilizador):
    pass

class Familiar(Utilizador):
    
    GRAU_PARENTESCO = [ 
        ("I","Irmã/Irmão"),
        ("P", "Pai"),
        ("M","Mãe"),
        ("O","Outro")      
    ]
        
    relacao = models.CharField(max_length=2, choices = GRAU_PARENTESCO)
    id_associado = models.ForeignKey(Utente, on_delete=models.CASCADE, related_name='familiares')
    
    def get_utente_url(self):
        return reverse('detalhes_utente', args=[str(self.id_associado.pk)])

class Medicamento(models.Model):
    nome = models.CharField(max_length=200, unique=True)
    
    def __str__(self):
        return self.nome

class Prescricao(models.Model):
    TOMAS = [
        ("U","Uma vez ao dia"),
        ("D","Duas vezes ao dia"),
        ("T","Três vezes ao dia"),
        ("Q","Quatro vezes ao dia"),
        ("O","Outros"),   
    ]
    
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    medicamento = models.ForeignKey(Medicamento, on_delete=models.CASCADE)
    data_prescricao = models.DateField(default=timezone.now)
    data_termino = models.DateField()
    tomas = models.CharField(max_length=1, choices= TOMAS)
    consulta = models.ForeignKey('Consulta', on_delete=models.CASCADE)

class Exame(models.Model):
    
    TIPO_EXAMES = [ 
        ("E","ECG"),
        ("R", "RaiosX"),
        ("H","Hemograma"),
        ("M","Ressonância Magnética"),
        ("C","Ecografia"), 
        ("O","Outros"),       
    ]
    
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    tipo_exames = models.CharField(max_length=1, choices= TIPO_EXAMES)
    consulta = models.ForeignKey('Consulta', on_delete=models.CASCADE)
    data = models.DateField(default=timezone.now)
    
class Parametros(models.Model):
      
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    peso = models.DecimalField(max_digits=5, decimal_places=2) 
    colesterol = models.DecimalField(max_digits=5, decimal_places=2) 
    bpm = models.DecimalField(max_digits=5, decimal_places=2)
    pressao = models.DecimalField(max_digits=5, decimal_places=2)
    altura = models.DecimalField(max_digits=5, decimal_places=2)
    temperatura = models.DecimalField(max_digits=5, decimal_places=2)
    consulta = models.ForeignKey('Consulta', on_delete=models.CASCADE, related_name='parametros_consulta')
    data = models.DateField(default=timezone.now)
    
class Consulta(models.Model):
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    medico = models.ForeignKey(Medico, on_delete=models.CASCADE)
    prescricoes = models.ManyToManyField(Prescricao, related_name='consultas_prescritas',null=True, blank=True)
    exames = models.ManyToManyField(Exame, related_name='exames_prescritos',null=True, blank=True)
    parametros = models.ManyToManyField(Parametros, related_name='parametros_prescritos',null=True, blank=True)
    data = models.DateTimeField(default=timezone.now)
    
    def __str__(self):
        return f"{self.data},{self.utente.nome}"

    

