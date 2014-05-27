Mode d'emploi pour le lancement des tests fonctionnels :


Ouvrir l'un des fichiers de test suivants :
=> simulFuncRX/testFunc.xml (interface RX entre AF et PRCF) ou 
=> simulFuncGQ/testFunc.xml (interface GQ entre AF et SPDF)


Param�tres importants (editables):
hostAF => adresse IP de la fonction AF pour le protocole DIAMETER 
          (si simul�e par MTS alors adresse locale)
hostAF => port de la fonction AF pour le protocole DIAMETER
simulAF => flag pour simuler (valeur true)ou non (valeur false) le c�t� AF
realmAF => nom de domaine (=royaume" du c�t� originating) pour le AF function

hostPCRF => adresse IP de la fonction PRCF pour le protocole DIAMETER 
          (si simul�e par MTS alors adresse locale)
hostPCRF => port de la fonction PCRF pour le protocole DIAMETER
simulPCRF => flag pour simuler (valeur true)ou non (valeur false) le c�t� PCRF
realmPCRF => nom de domaine (=royaume" du c�t� originating) pour le PCRF function

originIPAddress => adresse IP du terminal du c�t� originating
termIPAddress => adresse IP du terminal du c�t� terminating

responseCode => response code to reply to requests
responseTime => response time (in seconds) for the transactions when sending a response
sessionTime => duration (in seconds) of the sessions

Proc�dure d'utilisation :
Lancer les testcases : RecAAR SendAAA + RecSTR SendSTA c�t� ORIGNINATING et TERMINATING
Lancer les registers 
D�clencher un appel
Envoyer un ASR par le test case : SendASR RecASA
Envoyer un RAR par le test case : SendRAR RecRAA
Terminer l'appel
