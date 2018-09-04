# gittest

Revoir le processus en quatre phases:
- Vérification des paramètres
- Préparation du répertoire de travail
- Suppression des modifications extérieures
- Ecriture des scripts Gherkin
Se concentrer sur des méthodes plus atomiques.

Quelques règles
* Tout commit doit vérifier qu'il y a des modifications dans l'index
* Tout push doit être sûr que le répertoire est à jour avec le remote (pas de commit en retard)
et qu'il y a au moins un commit en avance (qu'il y a quelque chose à push)

Points de Vigilence
-> Faire attention aux paths donnés:
    - localRepositoryPath
    - featureFolderRelativePath
Il faut gérer le fait qu'ils puissent être écrits avec le pattern "src/test/resources/features", 
"/src/test/resources/features", "/src/test/resources/features/", "src/test/resources/features/". 