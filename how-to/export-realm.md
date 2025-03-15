# HowTo: Export a realm

Sometimes it is neccessary to export a realm for migration, backup or other tasks. 
There is an option to parially export a realm using the UI `Realm Settings > Action (top right corner) > Partial Export`.
The problem with this is that this export avoids exporting all secret information (cient secrets, users, etc.).
If you want to have a full export you have to use the cli.

Assuming our podman/docker setup you can use the following commands to export the javavienna realm:

```bash
podman exec -it keycloak /opt/keycloak/bin/kc.sh export --dir /tmp --realm javavienna
# for general infos
podman cp keycloak:/tmp/javavienna-realm.json ./
# for users (this might be pagenated for big userbases)
podman cp keycloak:/tmp/javavienna-users-0.json ./
```

If you want to import the realm and users together you can simply merge both jsons into one.

You can also use the CLI to import the realm again. Just have a look at the official documentation: https://www.keycloak.org/server/importExport#_importing_a_realm_from_a_file