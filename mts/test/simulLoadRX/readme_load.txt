Mode d'emploi pour le lancement des tests fonctionnels :


Ouvrir l'un des fichiers de test suivants :
=> simulLoadRX/testLoad.xml (interface RX entre AF et PRCF) ou 
=> simulLoadGQ/testLoad.xml (interface GQ entre AF et SPDF) TODO


Param�tres importants (editables):
hostAF => adresse IP de la fonction AF pour le protocole DIAMETER 
          (si simul�e par MTS alors adresse locale)
hostAF => port de la fonction AF pour le protocole DIAMETER
realmAF => nom de domaine (=royaume" du c�t� originating) pour le AF function

hostPCRF => adresse IP de la fonction PRCF pour le protocole DIAMETER 
          (si simul�e par MTS alors adresse locale)
hostPCRF => port de la fonction PCRF pour le protocole DIAMETER
realmPCRF => nom de domaine (=royaume" du c�t� originating) pour le PCRF function

originIPAddress => adresse IP du terminal du c�t� originating
termIPAddress => adresse IP du terminal du c�t� terminating

responseCode => response code to reply to requests
responseTime => response time (in seconds) for the transactions when sending a response
sessionTime => duration (in seconds) of the sessions

testDurationSec => duration (in seconds) of the test
speedAAR => Speed (in message per seconds) to send the RAR messages (for AF side)
speedRAR => Speed (in message per seconds) to send the RAR messages (for PCRF side) used in parallel mode ("Run parallel" menu item)
numberRAR => Number of the RAR messages to send used in manual mode ("Start" button)
speedASR => Speed (in message per seconds) to send the ASR messages (for PCRF side) used in parallel mode ("Run parallel" menu item)
numberASR => Number of the ASR messages to send used ion manual mode ("Start" button)

Proc�dure d'utilisation :
Lancer en mode manuel ('Start' button) ou en mode parallel le testcase (d�s�lectionner les autres testcases) : Responder_recAAR_STR_001
Lancer les registers + appels
Consulter les compteurs de statistiques utilisateur : nombre de sessions actives et dur�e des session des 2 cot�s : AF et PRCF

Vous avez les possibilit�s suivantes en cours de test :

1) Envoyer des messages ASR en BURST en lancant manuellement le testcase : Loader_SendASR 
vous pouvez sp�cifier le nombre de message � envoyer dans la case 'N'; 
la valeur par default est donn�e par le param�tre [numberASR]

2) Envoyer des messages ASR en CONTINU en lancant en mode parallel le testcase : Loader_SendASR 
vous pouvez sp�cifier le nombre de message � envoyer dans la case 'N'; 
la valeur par default est donn�e par le param�tre [numberASR]

3) Envoyer des messages RAR en BURST en lancant manuellement le testcase : Loader_SendRAR 
vous pouvez sp�cifier le nombre de message � envoyer dans la case 'N'; 
la valeur par default est donn�e par le param�tre [numberRAR]

4) Envoyer des messages ASR en CONTINU en lancant en mode parallel le testcase : Loader_SendASR 
vous pouvez sp�cifier le flux de message RAR � envoyer par le param�tre [speedRAR]

Compatibilit� avec MTS : 
MTS.5.8.4 et suivantes.

Routage interne dans MTS :
Le routage par d�fault des r�ponses a �t� d�bray� : ce qui veut dire que les 
r�ponses ne sont pas dispatch�es au scenario qui a �mis la requ�te correspondante.
(route.DEFAULT_RESPONSE = false)
Le routage par d�fault des requ�tes subs�quente a �t� d�bray� : ce qui veut dire que les 
requ�tes subs�quentes ne sont pas dispatch�es au scenario qui a trait� la requ�te initiale 
correspondante (nouvelle fonctionnalit� de la 5.8.4).
(route.DEFAULT_SUBSEQUENT = false)
Le routage des requ�tes initiales est effectu� en utilisant le m�casnisme du scenarioRouting
cad lorsqu'un message arrive, on �value les diff�rentes formules configu�es par le param�tre : 
route.SCENARIO_NAME=message.request,header.command
et on recherche le scenario qui a le m�me nom. Ceci nous permet de discriminer 
les requ�tes et les reponses de chaque type de message.

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
