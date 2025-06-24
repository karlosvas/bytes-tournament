package com.equipo2.bytestournament.enums;

/**
 * Enum AuthorityPrivilegies representa los privilegios de autoridad que pueden ser asignados a los usuarios en el sistema.
 * Cada valor del enum corresponde a una acción específica que un usuario puede realizar, como crear, eliminar, actualizar o ver usuarios.
 * Estos privilegios son utilizados para controlar el acceso a diferentes funcionalidades dentro de la aplicación.
 * 
 * USER_CREATE: Permite a un usuario crear nuevos usuarios.
 * USER_DELETE: Permite a un usuario eliminar usuarios existentes.
 * USER_UPDATE: Permite a un usuario actualizar la información de otros usuarios.
 * USER_VIEW: Permite a un usuario ver la información de otros usuarios.
 */
public enum AuthorityPrivilegies {
    USER_CREATE,
    USER_DELETE,
    USER_UPDATE,
    USER_VIEW
}