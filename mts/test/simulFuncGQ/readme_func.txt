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

Compatibilit� avec MTS : 
MTS.5.8.4 et suivantes.

Routage interne dans MTS :
Le routage par d�fault des r�ponses est utilis� : ce qui veut dire que les 
r�ponses sont dispatch�es au scenario qui a �mis la requ�te correspondante.
(route.DEFAULT_RESPONSE = true)
Le routage par d�fault des requ�tes subs�quente est utilis� : ce qui veut dire que les 
requ�tes subs�quentes sont dispatch�es au scenario qui a trait� la requ�te initiale 
(route.DEFAULT_SUBSEQUENT = true)
correspondante (nouvelle fonctionnalit� de la 5.8.4).
Le routage des requ�tes initiales est effectu� en utilisant le m�casnisme du scenarioRouting
cad lorsqu'un message arrive, on �value les diff�rentes formules configu�es par le param�tre : 
route.SCENARIO_NAME=avp.8.value,header.command|avp.8.value,message.protocol
et on recherche le scenario qui a le m�me nom. Ceci nous permet de discriminer 
les c�t�s originationing et terminating en se basant sur l'AVP 8=Framed-IP-Address.

Restriction de la pile DIAMETER DK :
* Les messages CER/CEA sont g�r�s intrins�quement par la pile en se basant sur les 
param�tres de configuration capability.XXXX. Lorsque l'on cr�� un listenpoint DIAMETER 
on a la possibilit� de sp�cifier certains AVPS des messages CER/CEA.
Mais seuls un certain nombre d'AVP peuvent �tre envoy�s. 
* Idem pour les messages DPR/DPA avant la rupture de la connection TCP.
* Idem pour les messages DWR/DWA pour lesquels on peut configurer le temps entre 
2 requ�tes (node.WATCHDOG_INTERVAL).
* Il est possible de d�brayer donc d'assouplir les contr�les faits par la pile 
par rapport au CER/CEA aussi bien en �mission qu'en r�ception par le param�tre
capability.CONTROL_VALIDITY.
* Il est possible de me pas envoyer les messages CER/CEA mais dans ce cas il me semble 
qu'il n'est pas possible de les envoyer dans le scenario 
(param�tre capability.AUTO_CER_CEA_ENABLE).
* Il est possible de me pas envoyer les messages CER/CEA mais dans ce cas il me semble 
qu'il n'est pas possible de les envoyer dans le scenario.
(param�tre capability.AUTO_DPR_DPA_ENABLE)

Bug de la pile DIAMETER DK :
Lorsque 2 connections TCP sont ouvertes depuis la m�me machine vers 2 listenpoints
dynamiques (<createLIstenpointDIAMETER>) alors il semblerait qu'il y ait des m�langes 
des confusions dans les structures internes de la pile qui emp�chent l'�mission 
d'un message. Dans ce cas de figure le probl�me peut �tre contourner en changeant 
l'ordre de creation des listenpoints.

Fabien HENRY => Ericsson Lannion
