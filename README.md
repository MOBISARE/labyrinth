# labyrinth
University ACL Project 

# Utilisation

- `./gradlew desktop:dist` pour build un .jar  dans le dossier `/labyrinth/desktop/desktop-num.version.jar`  
- Exécuter le .jar

# Fonctionnalités

## Résumé du jeu :

Le jeu est un style ARPG, vu du dessus à la Zelda. Le joueur aura un arc, pour se défendre devra affronter plusieurs ennemis dans un dans une sorte de donjon, une fois tous les ennemis vaincus, il pourra passer au niveau suivant. Une fois tous les niveaux finis le cycle recommence mais en plus difficile. 

### Héros :
Il est placé dans une salle de départ et peut se déplacer de salle en salle.
Il peut attaquer les ennemis à proximité ou à distance selon ses équipements.
Il peut être affecté par les effets des cases sur lesquelles il se déplace.

### Labyrinth : 
Le labyrinthe est défini par défaut sur un fichier.
Le labyrinthe est généré semi-aléatoirement le héros et les monstres ne peuvent pas traverser les murs.
Le labyrinthe est généré à partir de salles prédéfinies agencées aléatoirement.
Le labyrinthe est généré en fonction du niveau sélectionné. 
Certains cases du labyrinthe sont spéciales :
trésor : si le héros arrive sur la case il a gagné le jeu
pièges : quand un personnage arrive sur la case il subit des dégâts 
magiques : si un personnage arrive sur la case un effet est déclenché 
passages : un personnage qui arrive sur la case est téléporté à un autre endroit

### Monstres :
Des monstres sont placés de manière aléatoire dans le labyrinthe.
Les monstres se déplacent de manière aléatoire.
Les monstres se déplacent de manière intelligente en essayant d’attraper le héros.
Les fantômes sont des monstres qui peuvent traverser les murs.

### Attaques :
Le héros est tué au contact d’un monstre.
Le héros peut attaquer les montres avec lesquelles il est en contact - les deux perdent des points de vie.
Le héros peut attaquer les montres sur la case adjacente.



# Sprint 0 :

Affichage d’une fenêtre avec le héros qui se déplace à l’aide des flèches directionnelles ou des touches ZQSD. Le héros ne peut pas sortir des limites de la fenêtre.

Affichage d’une fenêtre avec un fond noir              - 3  
Affichage du héros                                     - 2  
Déplacement du héros                                   - 2  
Affichage des murs                                     - 2  
Gestion des collisions (héros-murs)                    - 2  


### Outils utilisés :

Langage : JAVA.   
Librairie Graphique : LibGDX  
Logiciel de Gestion de Projet : Trello.  
https://trello.com/w/projectlabyrinth5   
Outil de centralisation du code : GitHub  
https://github.com/MOBISARE/labyrinth  

