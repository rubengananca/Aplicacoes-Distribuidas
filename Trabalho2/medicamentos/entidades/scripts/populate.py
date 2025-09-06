from entidades.models import *
import random

def run():
    for i in range(50):
        #u = Utente(nome=f"utente {i}")
        nome = f"utente {i}"
        idade = random.randint(18,100)
        sexo = random.choice(["M","F"])
        u = Utente(nome=nome, idade=idade, sexo=sexo)
        u.save()

    for i in range(50):
        m = Medico(nome=f"medico {i}")
        m.save()

    for i in range(50):
        med = Medicamento(nome=f"medicamento {i}")
        med.save()

    for i in range(50):
        u =Utente.objects.all().filter(nome=f"utente {i}")[0]
        m = Medico.objects.all().filter(nome=f"medico {i}")[0]
        med1 = Medicamento.objects.all().filter(nome=f"medicamento {i}")[0]
        z = (i*3)%50
        med2 = Medicamento.objects.all().filter(nome=f"medicamento {z}")[0]

        c = Consulta(utente = u, medico = m)
        c.save()
        c.prescricoes.add(med1)
        c.prescricoes.add(med2)

