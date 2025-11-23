# buvette-backend
Gestion des commandes  buvette

### Spring security
pour gérer l'authentification des applications on utilise spring security après l'istalation des dependances nécessiares 
- Le fichier config/SecurityConfig.java repésente le chef de sécurity
- La méthode FilterChain qui permet de donner les ordres aux gardes de sécurité
- http.csrf(csrf->scrf.disable) : car on travail avec API axios et non pas par des formulaires HTML classique
- cors(Customizer.withDefaults()) va vérifier les régles de Cors (laisser passer les requetes venant de vue)
- permitAll()=> tous autorisées
- authenticated()=> nécessite Authentification
- formLogin(form->form.disable())  pour désactiver la page de connexion par défaut de Spring
- httpBasic(basic->basaic.disable())  pour désactiver la fenetre de navigateur qui demande le mot de passe
- config.setAllowedOrigins(List.of("url de vue")) pour autoriser explicitement le port de frontend (vue)
- config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS")); autoriser  les types d'actions
- pour toutes les fonctions de SpringSecurity on utiliser le syntaxe # Lamba(Spring security 6)
  ### JWT (authentification)
  pour gérer l'authentification d'une manière plus sécurisée sans avoir gérer plusieurs sessions on utlise le JWT(token)
  envoyé par le serveur au client sert à pouver l'identité de l'utilisateur dans chque requete
 ### (1) Login
- Le user envoie : email + password
### (2) Le backend vérifie
- Email existe ?
- Mot de passe correct ?
### (3) Le backend génère un JWT
. ce jeton possède les informations suivantes:
- email (sujet du token)
- date de création
- date d’expiration
- signature cryptographique
💡 Signature = garantie que le token n'a pas été modifié.
### (4) LE FONT END stocke le token dans localStorage
### (5) À chaque requête API front renvoit le tocken et la class filter  le vérifier 
- si le token bien validé le filtre laisse la requete passe au controller pour autre opérations 
- si non erreur de 403(forbidden) sera envoyé au front

### N.B Le service JWT pour créer nouveu jeton et la classe de filtre pour intércepter la requete avant atteint le controller
  


  



