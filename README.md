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
  



